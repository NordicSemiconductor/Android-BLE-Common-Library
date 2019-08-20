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

package no.nordicsemi.android.ble.common.callback.hr

import android.bluetooth.BluetoothDevice
import android.os.Parcel
import no.nordicsemi.android.ble.callback.profile.ProfileReadResponse
import no.nordicsemi.android.ble.common.profile.hr.HeartRateMeasurementCallback
import no.nordicsemi.android.ble.data.Data
import java.util.*

/**
 * Data callback that parses value into Heart Rate Measurement data.
 * If the value received do not match required syntax
 * [.onInvalidDataReceived] callback will be called.
 * will be called.
 * See: https://www.bluetooth.com/specifications/gatt/viewer?attributeXmlFile=org.bluetooth.characteristic.heart_rate_measurement.xml
 */
abstract class HeartRateMeasurementDataCallback : ProfileReadResponse,
    HeartRateMeasurementCallback {

    constructor() {
        // empty
    }

    protected constructor(`in`: Parcel) : super(`in`)

    override fun onDataReceived(device: BluetoothDevice, data: Data) {
        super.onDataReceived(device, data)

        if (data.size() < 2) {
            onInvalidDataReceived(device, data)
            return
        }

        // Read flags
        var offset = 0
        val flags = data.getIntValue(Data.FORMAT_UINT8, offset)!!
        val hearRateType = if (flags and 0x01 == 0) Data.FORMAT_UINT8 else Data.FORMAT_UINT16
        val sensorContactStatus = flags and 0x06 shr 1
        val sensorContactSupported = sensorContactStatus == 2 || sensorContactStatus == 3
        val sensorContactDetected = sensorContactStatus == 3
        val energyExpandedPresent = flags and 0x08 != 0
        val rrIntervalsPresent = flags and 0x10 != 0
        offset += 1

        // Validate packet length
        if (data.size() < (1 + (hearRateType and 0x0F)
                    + (if (energyExpandedPresent) 2 else 0)
                    + if (rrIntervalsPresent) 2 else 0)
        ) {
            onInvalidDataReceived(device, data)
            return
        }

        // Prepare data
        val sensorContact = if (sensorContactSupported) sensorContactDetected else null

        val heartRate = data.getIntValue(hearRateType, offset)!!
        offset += hearRateType and 0xF

        var energyExpanded: Int? = null
        if (energyExpandedPresent) {
            energyExpanded = data.getIntValue(Data.FORMAT_UINT16, offset)
            offset += 2
        }

        var rrIntervals: List<Int>? = null
        if (rrIntervalsPresent) {
            val count = (data.size() - offset) / 2
            val intervals = ArrayList<Int>(count)
            for (i in 0 until count) {
                intervals.add(data.getIntValue(Data.FORMAT_UINT16, offset)!!)
                offset += 2
            }
            rrIntervals = Collections.unmodifiableList(intervals)
        }

        onHeartRateMeasurementReceived(
            device,
            heartRate,
            sensorContact,
            energyExpanded,
            rrIntervals
        )
    }
}
