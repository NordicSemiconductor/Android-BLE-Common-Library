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

package no.nordicsemi.android.ble.common.callback.sc

import android.bluetooth.BluetoothDevice
import android.os.Parcel

import no.nordicsemi.android.ble.callback.profile.ProfileReadResponse
import no.nordicsemi.android.ble.common.profile.sc.SpeedAndCadenceControlPointCallback
import no.nordicsemi.android.ble.data.Data

/**
 * Data callback that parses value into SC Control Point data.
 * If the value received do not match required syntax
 * [.onInvalidDataReceived] callback will be called.
 * will be called.
 * See: https://www.bluetooth.com/specifications/gatt/viewer?attributeXmlFile=org.bluetooth.characteristic.sc_control_point.xml
 */
abstract class SpeedAndCadenceControlPointDataCallback : ProfileReadResponse,
    SpeedAndCadenceControlPointCallback {

    constructor() {
        // empty
    }

    protected constructor(`in`: Parcel) : super(`in`)

    override fun onDataReceived(device: BluetoothDevice, data: Data) {
        super.onDataReceived(device, data)

        if (data.size() < 3) {
            onInvalidDataReceived(device, data)
            return
        }

        val responseCode = data.getIntValue(Data.FORMAT_UINT8, 0)!!
        val requestCode = data.getIntValue(Data.FORMAT_UINT8, 1)!!
        val status = data.getIntValue(Data.FORMAT_UINT8, 2)!!

        if (responseCode != SC_OP_CODE_RESPONSE_CODE) {
            onInvalidDataReceived(device, data)
            return
        }

        if (status != SC_RESPONSE_SUCCESS) {
            onSCOperationError(device, requestCode, status)
            return
        }

        when (requestCode) {
            SpeedAndCadenceControlPointCallback.SC_OP_CODE_REQUEST_SUPPORTED_SENSOR_LOCATIONS -> {
                val size = data.size() - 3
                val locations = IntArray(size)
                for (i in 0 until size) {
                    locations[i] = data.getIntValue(Data.FORMAT_UINT8, 3 + i)!!
                }
                onSupportedSensorLocationsReceived(device, locations)
            }
            else -> {
                onSCOperationCompleted(device, requestCode)
            }
        }
    }

    companion object {
        private val SC_OP_CODE_RESPONSE_CODE = 16
        private val SC_RESPONSE_SUCCESS = 1
    }
}
