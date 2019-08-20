/*
 * Copyright (c) 2018, Nordic Semiconductor
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
 * USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package no.nordicsemi.android.ble.common.callback.cgm

import android.bluetooth.BluetoothDevice
import android.os.Parcel

import no.nordicsemi.android.ble.callback.profile.ProfileReadResponse
import no.nordicsemi.android.ble.common.profile.cgm.CGMTypes
import no.nordicsemi.android.ble.common.profile.cgm.ContinuousGlucoseMeasurementCallback
import no.nordicsemi.android.ble.common.util.CRC16
import no.nordicsemi.android.ble.data.Data

/**
 * Data callback that parses value into CGM data.
 * If the value received do not match required syntax
 * [.onInvalidDataReceived] callback will be called.
 * If the device supports E2E CRC validation and the CRC is not valid, the
 * [.onContinuousGlucoseMeasurementReceivedWithCrcError]
 * will be called.
 * See: https://www.bluetooth.com/specifications/gatt/viewer?attributeXmlFile=org.bluetooth.characteristic.cgm_measurement.xml
 */
abstract class ContinuousGlucoseMeasurementDataCallback : ProfileReadResponse,
    ContinuousGlucoseMeasurementCallback {

    constructor() {
        // empty
    }

    protected constructor(`in`: Parcel) : super(`in`)

    override fun onDataReceived(device: BluetoothDevice, data: Data) {
        super.onDataReceived(device, data)

        if (data.size() < 1) {
            onInvalidDataReceived(device, data)
            return
        }

        var offset = 0

        while (offset < data.size()) {
            // Packet size
            val size = data.getIntValue(Data.FORMAT_UINT8, offset)!!

            if (size < 6 || offset + size > data.size()) {
                onInvalidDataReceived(device, data)
                return
            }

            // Flags
            val flags = data.getIntValue(Data.FORMAT_UINT8, offset + 1)!!

            val cgmTrendInformationPresent = flags and 0x01 != 0
            val cgmQualityInformationPresent = flags and 0x02 != 0
            val sensorWarningOctetPresent = flags and 0x20 != 0
            val sensorCalTempOctetPresent = flags and 0x40 != 0
            val sensorStatusOctetPresent = flags and 0x80 != 0

            val dataSize =
                (6 + (if (cgmTrendInformationPresent) 2 else 0) + (if (cgmQualityInformationPresent) 2 else 0)
                        + (if (sensorWarningOctetPresent) 1 else 0) + (if (sensorCalTempOctetPresent) 1 else 0)
                        + if (sensorStatusOctetPresent) 1 else 0)

            if (size != dataSize && size != dataSize + 2) {
                onInvalidDataReceived(device, data)
                return
            }

            val crcPresent = size == dataSize + 2
            if (crcPresent) {
                val expectedCrc = data.getIntValue(Data.FORMAT_UINT16, offset + dataSize)!!
                val actualCrc = CRC16.MCRF4XX(data.value!!, offset, dataSize)
                if (expectedCrc != actualCrc) {
                    onContinuousGlucoseMeasurementReceivedWithCrcError(device, data)
                    return
                }
            }

            offset += 2
            // Glucose concentration
            val glucoseConcentration = data.getFloatValue(Data.FORMAT_SFLOAT, offset)!!
            offset += 2

            // Time offset (in minutes since Session Start)
            val timeOffset = data.getIntValue(Data.FORMAT_UINT16, offset)!!
            offset += 2

            // Sensor Status Annunciation
            var warningStatus = 0
            var calibrationTempStatus = 0
            var sensorStatus = 0
            var status: CGMTypes.CGMStatus? = null

            if (sensorWarningOctetPresent) {
                warningStatus = data.getIntValue(Data.FORMAT_UINT8, offset++)!!
            }
            if (sensorCalTempOctetPresent) {
                calibrationTempStatus = data.getIntValue(Data.FORMAT_UINT8, offset++)!!
            }
            if (sensorStatusOctetPresent) {
                sensorStatus = data.getIntValue(Data.FORMAT_UINT8, offset++)!!
            }
            if (sensorWarningOctetPresent || sensorCalTempOctetPresent || sensorStatusOctetPresent) {
                status = CGMTypes.CGMStatus(warningStatus, calibrationTempStatus, sensorStatus)
            }

            // CGM Trend Information
            var trend: Float? = null
            if (cgmTrendInformationPresent) {
                trend = data.getFloatValue(Data.FORMAT_SFLOAT, offset)
                offset += 2
            }

            // CGM Quality Information
            var quality: Float? = null
            if (cgmQualityInformationPresent) {
                quality = data.getFloatValue(Data.FORMAT_SFLOAT, offset)
                offset += 2
            }

            // E2E-CRC
            if (crcPresent) {
                offset += 2
            }

            onContinuousGlucoseMeasurementReceived(
                device,
                glucoseConcentration,
                trend,
                quality,
                status,
                timeOffset,
                crcPresent
            )
        }
    }
}
