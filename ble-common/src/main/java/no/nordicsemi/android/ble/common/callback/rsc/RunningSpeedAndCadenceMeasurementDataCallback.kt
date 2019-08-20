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

package no.nordicsemi.android.ble.common.callback.rsc

import android.bluetooth.BluetoothDevice
import android.os.Parcel

import no.nordicsemi.android.ble.callback.profile.ProfileReadResponse
import no.nordicsemi.android.ble.common.profile.rsc.RunningSpeedAndCadenceMeasurementCallback
import no.nordicsemi.android.ble.data.Data

/**
 * Data callback that parses value into RSC Measurement data.
 * If the value received do not match required syntax
 * [.onInvalidDataReceived] callback will be called.
 * will be called.
 * See: https://www.bluetooth.com/specifications/gatt/viewer?attributeXmlFile=org.bluetooth.characteristic.rsc_measurement.xml
 */
abstract class RunningSpeedAndCadenceMeasurementDataCallback : ProfileReadResponse,
    RunningSpeedAndCadenceMeasurementCallback {

    constructor() {
        // empty
    }

    protected constructor(`in`: Parcel) : super(`in`)

    override fun onDataReceived(device: BluetoothDevice, data: Data) {
        super.onDataReceived(device, data)

        if (data.size() < 4) {
            onInvalidDataReceived(device, data)
            return
        }

        var offset = 0
        val flags = data.getIntValue(Data.FORMAT_UINT8, offset)!!
        val instantaneousStrideLengthPresent = flags and 0x01 != 0
        val totalDistancePresent = flags and 0x02 != 0
        val statusRunning = flags and 0x04 != 0
        offset += 1

        val speed = data.getIntValue(Data.FORMAT_UINT16, offset)!! / 256f // [m/s]
        offset += 2
        val cadence = data.getIntValue(Data.FORMAT_UINT8, offset)!!
        offset += 1

        if (data.size() < (4
                    + (if (instantaneousStrideLengthPresent) 2 else 0)
                    + if (totalDistancePresent) 4 else 0)
        ) {
            onInvalidDataReceived(device, data)
            return
        }

        var strideLength: Int? = null
        if (instantaneousStrideLengthPresent) {
            strideLength = data.getIntValue(Data.FORMAT_UINT16, offset)
            offset += 2
        }

        var totalDistance: Long? = null
        if (totalDistancePresent) {
            totalDistance = data.getLongValue(Data.FORMAT_UINT32, offset)
            // offset += 4;
        }

        onRSCMeasurementReceived(device, statusRunning, speed, cadence, strideLength, totalDistance)
    }
}
