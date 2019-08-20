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
import android.os.Parcelable
import no.nordicsemi.android.ble.common.profile.cgm.CGMTypes

import no.nordicsemi.android.ble.data.Data
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
 * CGMFeatureResponse response = readCharacteristic(characteristic)
 * .awaitValid(CGMFeatureResponse.class);
 * if (response.getFeatures().calibrationSupported) {
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
class CGMFeatureResponse : CGMFeatureDataCallback, CRCSecuredResponse, Parcelable {
    var features: CGMTypes.CGMFeatures? = null
        private set
    var type: Int = 0
        private set
    var sampleLocation: Int = 0
        private set
    private var secured: Boolean = false
    private var crcValid: Boolean = false

    constructor() {
        // empty
    }

    // Parcelable
    private constructor(`in`: Parcel) : super(`in`) {
        if (`in`.readByte().toInt() == 0) {
            features = null
        } else {
            features = CGMTypes.CGMFeatures(`in`.readInt())
        }
        type = `in`.readInt()
        sampleLocation = `in`.readInt()
        secured = `in`.readByte().toInt() != 0
        crcValid = `in`.readByte().toInt() != 0
    }

    override fun onContinuousGlucoseMonitorFeaturesReceived(
        device: BluetoothDevice,
        features: CGMTypes.CGMFeatures,
        type: Int, sampleLocation: Int,
        secured: Boolean
    ) {
        this.features = features
        this.type = type
        this.sampleLocation = sampleLocation
        this.secured = secured
        this.crcValid = secured
    }

    override fun onContinuousGlucoseMonitorFeaturesReceivedWithCrcError(
        device: BluetoothDevice,
        data: Data
    ) {
        onInvalidDataReceived(device, data)
        this.secured = true
        this.crcValid = false
    }

    override val isSecured: Boolean
        get() = secured
    override val isCrcValid: Boolean
        get() = crcValid

    override fun writeToParcel(dest: Parcel, flags: Int) {
        super.writeToParcel(dest, flags)
        if (features == null) {
            dest.writeByte(0.toByte())
        } else {
            dest.writeByte(1.toByte())
            dest.writeInt(features!!.value)
        }
        dest.writeInt(type)
        dest.writeInt(sampleLocation)
        dest.writeByte((if (secured) 1 else 0).toByte())
        dest.writeByte((if (crcValid) 1 else 0).toByte())
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<CGMFeatureResponse> =
            object : Parcelable.Creator<CGMFeatureResponse> {
                override fun createFromParcel(`in`: Parcel): CGMFeatureResponse {
                    return CGMFeatureResponse(`in`)
                }

                override fun newArray(size: Int): Array<CGMFeatureResponse?> {
                    return arrayOfNulls(size)
                }
            }
    }
}
