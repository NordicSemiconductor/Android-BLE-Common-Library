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
import no.nordicsemi.android.ble.common.profile.cgm.CGMStatusCallback
import no.nordicsemi.android.ble.common.profile.cgm.CGMTypes
import no.nordicsemi.android.ble.common.util.CRC16
import no.nordicsemi.android.ble.data.Data

/**
 * Data callback that parses value into CGM Status data.
 * If the value received do not match required syntax
 * [.onInvalidDataReceived] callback will be called.
 * If the device supports E2E CRC validation and the CRC is not valid, the
 * [.onContinuousGlucoseMonitorStatusReceivedWithCrcError]
 * will be called.
 * See: https://www.bluetooth.com/specifications/gatt/viewer?attributeXmlFile=org.bluetooth.characteristic.cgm_status.xml
 */
abstract class CGMStatusDataCallback : ProfileReadResponse, CGMStatusCallback {

    constructor() {
        // empty
    }

    protected constructor(`in`: Parcel) : super(`in`)

    override fun onDataReceived(device: BluetoothDevice, data: Data) {
        super.onDataReceived(device, data)

        if (data.size() != 5 && data.size() != 7) {
            onInvalidDataReceived(device, data)
            return
        }

        val timeOffset = data.getIntValue(Data.FORMAT_UINT16, 0)!!
        val warningStatus = data.getIntValue(Data.FORMAT_UINT8, 2)!!
        val calibrationTempStatus = data.getIntValue(Data.FORMAT_UINT8, 3)!!
        val sensorStatus = data.getIntValue(Data.FORMAT_UINT8, 4)!!

        val crcPresent = data.size() == 7
        if (crcPresent) {
            val actualCrc = CRC16.MCRF4XX(data.value!!, 0, 5)
            val expectedCrc = data.getIntValue(Data.FORMAT_UINT16, 5)!!
            if (actualCrc != expectedCrc) {
                onContinuousGlucoseMonitorStatusReceivedWithCrcError(device, data)
                return
            }
        }

        val status = CGMTypes.CGMStatus(warningStatus, calibrationTempStatus, sensorStatus)
        onContinuousGlucoseMonitorStatusChanged(device, status, timeOffset, crcPresent)
    }
}
