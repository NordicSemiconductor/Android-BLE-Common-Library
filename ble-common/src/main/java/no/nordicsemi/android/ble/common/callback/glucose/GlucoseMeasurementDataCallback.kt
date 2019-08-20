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

package no.nordicsemi.android.ble.common.callback.glucose

import android.bluetooth.BluetoothDevice
import android.os.Parcel
import no.nordicsemi.android.ble.callback.profile.ProfileReadResponse
import no.nordicsemi.android.ble.common.callback.DateTimeDataCallback
import no.nordicsemi.android.ble.common.profile.glucose.GlucoseMeasurementCallback
import no.nordicsemi.android.ble.data.Data
import java.util.*

/**
 * Data callback that parses value into Glucose Measurement data.
 * If the value received do not match required syntax
 * [.onInvalidDataReceived] callback will be called.
 * will be called.
 * See: https://www.bluetooth.com/specifications/gatt/viewer?attributeXmlFile=org.bluetooth.characteristic.glucose_measurement.xml
 */
abstract class GlucoseMeasurementDataCallback : ProfileReadResponse, GlucoseMeasurementCallback {

    constructor() {
        // empty
    }

    protected constructor(`in`: Parcel) : super(`in`)

    override fun onDataReceived(device: BluetoothDevice, data: Data) {
        super.onDataReceived(device, data)

        if (data.size() < 10) {
            onInvalidDataReceived(device, data)
            return
        }

        var offset = 0

        val flags = data.getIntValue(Data.FORMAT_UINT8, offset++)!!
        val timeOffsetPresent = flags and 0x01 != 0
        val glucoseDataPresent = flags and 0x02 != 0
        val unitMolL = flags and 0x04 != 0
        val sensorStatusAnnunciationPresent = flags and 0x08 != 0
        val contextInformationFollows = flags and 0x10 != 0

        if (data.size() < (10 + (if (timeOffsetPresent) 2 else 0) + (if (glucoseDataPresent) 3 else 0)
                    + if (sensorStatusAnnunciationPresent) 2 else 0)
        ) {
            onInvalidDataReceived(device, data)
            return
        }

        // Required fields
        val sequenceNumber = data.getIntValue(Data.FORMAT_UINT16, offset)!!
        offset += 2
        val baseTime = DateTimeDataCallback.readDateTime(data, 3)
        offset += 7

        if (baseTime == null) {
            onInvalidDataReceived(device, data)
            return
        }

        // Optional fields
        if (timeOffsetPresent) {
            val timeOffset = data.getIntValue(Data.FORMAT_SINT16, offset)!!
            offset += 2

            baseTime.add(Calendar.MINUTE, timeOffset)
        }

        var glucoseConcentration: Float? = null
        var unit: Int? = null
        var type: Int? = null
        var sampleLocation: Int? = null
        if (glucoseDataPresent) {
            glucoseConcentration = data.getFloatValue(Data.FORMAT_SFLOAT, offset)
            val typeAndSampleLocation = data.getIntValue(Data.FORMAT_UINT8, offset + 2)!!
            offset += 3

            type = typeAndSampleLocation and 0x0F
            sampleLocation = typeAndSampleLocation shr 4
            unit =
                if (unitMolL) GlucoseMeasurementCallback.Companion.UNIT_mol_L else GlucoseMeasurementCallback.Companion.UNIT_kg_L
        }

        var status: GlucoseMeasurementCallback.GlucoseStatus? = null
        if (sensorStatusAnnunciationPresent) {
            val value = data.getIntValue(Data.FORMAT_UINT16, offset)!!
            // offset += 2;

            status = GlucoseMeasurementCallback.GlucoseStatus(value)
        }

        onGlucoseMeasurementReceived(
            device, sequenceNumber, baseTime /* with offset */,
            glucoseConcentration, unit, type, sampleLocation, status, contextInformationFollows
        )
    }
}
