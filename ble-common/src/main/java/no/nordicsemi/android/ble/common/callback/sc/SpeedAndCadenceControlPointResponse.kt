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
import android.os.Parcelable
import no.nordicsemi.android.ble.common.profile.sc.SpeedAndCadenceControlPointCallback

import no.nordicsemi.android.ble.exception.InvalidDataException
import no.nordicsemi.android.ble.exception.RequestFailedException

/**
 * Response class that could be used as a result of a synchronous request.
 * The data received are available through getters, instead of a callback.
 *
 *
 * Usage example:
 * <pre>
 * try {
 * SpeedAndCadenceControlPointResponse response = waitForIndication(characteristic)
 * .trigger(writeCharacteristic(characteristic, SpeedAndCadenceControlPointData.requestSupportedSensorLocations()))
 * .awaitValid(SpeedAndCadenceControlPointResponse.class);
 * if (response.isOperationCompleted()) {
 * int locations = response.getSupportedSensorLocations();
 * ...
 * }
 * ...
 * } catch ([RequestFailedException] e) {
 * Log.w(TAG, "Request failed with status " + e.getStatus(), e);
 * } catch ([InvalidDataException] e) {
 * Log.w(TAG, "Invalid data received: " + e.getResponse().getRawData());
 * }
</pre> *
 *
 */
class SpeedAndCadenceControlPointResponse : SpeedAndCadenceControlPointDataCallback, Parcelable {
    var isOperationCompleted: Boolean = false
        private set
    var requestCode: Int = 0
        private set
    var errorCode: Int = 0
        private set
    var supportedSensorLocations: IntArray? = null
        private set

    constructor() {
        // empty
    }

    // Parcelable
    private constructor(`in`: Parcel) : super(`in`) {
        isOperationCompleted = `in`.readByte().toInt() != 0
        requestCode = `in`.readInt()
        errorCode = `in`.readInt()
        supportedSensorLocations = `in`.createIntArray()
    }

    override fun onSCOperationCompleted(device: BluetoothDevice, requestCode: Int) {
        this.isOperationCompleted = true
        this.requestCode = requestCode
    }

    override fun onSCOperationError(device: BluetoothDevice, requestCode: Int, errorCode: Int) {
        this.isOperationCompleted = false
        this.requestCode = requestCode
        this.errorCode = errorCode
    }

    override fun onSupportedSensorLocationsReceived(device: BluetoothDevice, locations: IntArray) {
        this.isOperationCompleted = true
        this.requestCode =
            SpeedAndCadenceControlPointCallback.SC_OP_CODE_REQUEST_SUPPORTED_SENSOR_LOCATIONS
        this.supportedSensorLocations = locations
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        super.writeToParcel(dest, flags)
        dest.writeByte((if (isOperationCompleted) 1 else 0).toByte())
        dest.writeInt(requestCode)
        dest.writeInt(errorCode)
        dest.writeIntArray(supportedSensorLocations)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<SpeedAndCadenceControlPointResponse> =
            object : Parcelable.Creator<SpeedAndCadenceControlPointResponse> {
                override fun createFromParcel(`in`: Parcel): SpeedAndCadenceControlPointResponse {
                    return SpeedAndCadenceControlPointResponse(`in`)
                }

                override fun newArray(size: Int): Array<SpeedAndCadenceControlPointResponse?> {
                    return arrayOfNulls(size)
                }
            }
    }
}
