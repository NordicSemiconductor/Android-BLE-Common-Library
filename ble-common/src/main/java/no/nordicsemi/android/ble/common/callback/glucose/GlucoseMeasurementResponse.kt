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
import android.os.Parcelable
import no.nordicsemi.android.ble.common.profile.glucose.GlucoseMeasurementCallback
import no.nordicsemi.android.ble.exception.InvalidDataException
import no.nordicsemi.android.ble.exception.RequestFailedException
import java.util.*

/**
 * Response class that could be used as a result of a synchronous request.
 * The data received are available through getters, instead of a callback.
 *
 *
 * Usage example:
 * <pre>
 * try {
 * GlucoseMeasurementResponse response = waitForNotification(characteristic)
 * .awaitValid(GlucoseMeasurementResponse.class);
 * if (response.contextInformationFollows()) {
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
class GlucoseMeasurementResponse// Parcelable
private constructor(`in`: Parcel) : GlucoseMeasurementDataCallback(`in`), Parcelable {
    var sequenceNumber: Int = 0
        private set
    var time: Calendar? = null
        private set
    var glucoseConcentration: Float? = null
        private set
    var unit: Int? = null
        private set
    var type: Int? = null
        private set
    var sampleLocation: Int? = null
        private set
    var status: GlucoseMeasurementCallback.GlucoseStatus? = null
        private set
    private var contextInformationFollows: Boolean = false

    init {
        sequenceNumber = `in`.readInt()
        if (`in`.readByte().toInt() == 0) {
            time = null
        } else {
            time = Calendar.getInstance()
            time!!.timeInMillis = `in`.readLong()
        }
        if (`in`.readByte().toInt() == 0) {
            glucoseConcentration = null
        } else {
            glucoseConcentration = `in`.readFloat()
        }
        if (`in`.readByte().toInt() == 0) {
            unit = null
        } else {
            unit = `in`.readInt()
        }
        if (`in`.readByte().toInt() == 0) {
            type = null
        } else {
            type = `in`.readInt()
        }
        if (`in`.readByte().toInt() == 0) {
            sampleLocation = null
        } else {
            sampleLocation = `in`.readInt()
        }
        if (`in`.readByte().toInt() == 0) {
            status = null
        } else {
            status = GlucoseMeasurementCallback.GlucoseStatus(`in`.readInt())
        }
        contextInformationFollows = `in`.readByte().toInt() != 0
    }

    override fun onGlucoseMeasurementReceived(
        device: BluetoothDevice,
        sequenceNumber: Int,
        time: Calendar,
        glucoseConcentration: Float?,
        unit: Int?,
        type: Int?,
        sampleLocation: Int?,
        status: GlucoseMeasurementCallback.GlucoseStatus?,
        contextInformationFollows: Boolean
    ) {
        this.sequenceNumber = sequenceNumber
        this.time = time
        this.glucoseConcentration = glucoseConcentration
        this.unit = unit
        this.type = type
        this.sampleLocation = sampleLocation
        this.status = status
        this.contextInformationFollows = contextInformationFollows
    }

    fun contextInformationFollows(): Boolean {
        return contextInformationFollows
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        super.writeToParcel(dest, flags)
        dest.writeInt(sequenceNumber)
        if (time == null) {
            dest.writeByte(0.toByte())
        } else {
            dest.writeByte(1.toByte())
            dest.writeLong(time!!.timeInMillis)
        }
        if (glucoseConcentration == null) {
            dest.writeByte(0.toByte())
        } else {
            dest.writeByte(1.toByte())
            dest.writeFloat(glucoseConcentration!!)
        }
        if (unit == null) {
            dest.writeByte(0.toByte())
        } else {
            dest.writeByte(1.toByte())
            dest.writeInt(unit!!)
        }
        if (type == null) {
            dest.writeByte(0.toByte())
        } else {
            dest.writeByte(1.toByte())
            dest.writeInt(type!!)
        }
        if (sampleLocation == null) {
            dest.writeByte(0.toByte())
        } else {
            dest.writeByte(1.toByte())
            dest.writeInt(sampleLocation!!)
        }
        super.writeToParcel(dest, flags)
        if (status == null) {
            dest.writeByte(0.toByte())
        } else {
            dest.writeByte(1.toByte())
            dest.writeInt(status!!.value)
        }
        dest.writeByte((if (contextInformationFollows) 1 else 0).toByte())
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<GlucoseMeasurementResponse> =
            object : Parcelable.Creator<GlucoseMeasurementResponse> {
                override fun createFromParcel(`in`: Parcel): GlucoseMeasurementResponse {
                    return GlucoseMeasurementResponse(`in`)
                }

                override fun newArray(size: Int): Array<GlucoseMeasurementResponse?> {
                    return arrayOfNulls(size)
                }
            }
    }
}
