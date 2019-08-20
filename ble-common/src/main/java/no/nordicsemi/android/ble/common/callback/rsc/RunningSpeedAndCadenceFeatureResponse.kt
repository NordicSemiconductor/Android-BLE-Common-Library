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
import android.os.Parcelable
import no.nordicsemi.android.ble.common.profile.rsc.RunningSpeedAndCadenceFeatureCallback

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
 * RunningSpeedAndCadenceFeatureResponse response = readCharacteristic(characteristic)
 * .awaitValid(RunningSpeedAndCadenceFeatureResponse.class);
 * RSCFeatures features = response.getFeatures();
 * ...
 * } catch ([RequestFailedException] e) {
 * Log.w(TAG, "Request failed with status " + e.getStatus(), e);
 * } catch ([InvalidDataException] e) {
 * Log.w(TAG, "Invalid data received: " + e.getResponse().getRawData());
 * }
</pre> *
 *
 */
class RunningSpeedAndCadenceFeatureResponse : RunningSpeedAndCadenceFeatureDataCallback,
    Parcelable {
    var features: RunningSpeedAndCadenceFeatureCallback.RSCFeatures? = null
        private set

    constructor() {
        // empty
    }

    // Parcelable
    private constructor(`in`: Parcel) : super(`in`) {
        if (`in`.readByte().toInt() == 0) {
            features = null
        } else {
            features = RunningSpeedAndCadenceFeatureCallback.RSCFeatures(`in`.readInt())
        }
    }

    override fun onRunningSpeedAndCadenceFeaturesReceived(
        device: BluetoothDevice,
        features: RunningSpeedAndCadenceFeatureCallback.RSCFeatures
    ) {
        this.features = features
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        super.writeToParcel(dest, flags)
        if (features == null) {
            dest.writeByte(0.toByte())
        } else {
            dest.writeByte(1.toByte())
            dest.writeInt(features!!.value)
        }
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<RunningSpeedAndCadenceFeatureResponse> =
            object : Parcelable.Creator<RunningSpeedAndCadenceFeatureResponse> {
                override fun createFromParcel(`in`: Parcel): RunningSpeedAndCadenceFeatureResponse {
                    return RunningSpeedAndCadenceFeatureResponse(`in`)
                }

                override fun newArray(size: Int): Array<RunningSpeedAndCadenceFeatureResponse?> {
                    return arrayOfNulls(size)
                }
            }
    }
}
