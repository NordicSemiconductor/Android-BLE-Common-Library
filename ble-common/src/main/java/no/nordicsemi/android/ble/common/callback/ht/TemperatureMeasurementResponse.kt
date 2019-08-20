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

package no.nordicsemi.android.ble.common.callback.ht

import android.bluetooth.BluetoothDevice
import android.os.Parcel
import android.os.Parcelable
import no.nordicsemi.android.ble.common.profile.ht.TemperatureMeasurementCallback
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
 * TemperatureMeasurementResponse response = waitForNotification(characteristic)
 * .awaitValid(TemperatureMeasurementResponse.class);
 * float tempCelsius = response.getTemperatureCelsius());
 * ...
 * } catch ([RequestFailedException] e) {
 * Log.w(TAG, "Request failed with status " + e.getStatus(), e);
 * } catch ([InvalidDataException] e) {
 * Log.w(TAG, "Invalid data received: " + e.getResponse().getRawData());
 * }
</pre> *
 *
 */
class TemperatureMeasurementResponse : TemperatureMeasurementDataCallback, Parcelable {
    var temperature: Float = 0.toFloat()
        private set
    var unit: Int = 0
        private set
    var timestamp: Calendar? = null
        private set
    var type: Int? = null
        private set

    val temperatureCelsius: Float
        get() = TemperatureMeasurementCallback.toCelsius(temperature, unit)

    val temperatureFahrenheit: Float
        get() = TemperatureMeasurementCallback.toFahrenheit(temperature, unit)

    constructor() {
        // empty
    }

    // Parcelable
    private constructor(`in`: Parcel) : super(`in`) {
        temperature = `in`.readFloat()
        unit = `in`.readInt()
        if (`in`.readByte().toInt() == 0) {
            timestamp = null
        } else {
            timestamp = Calendar.getInstance()
            timestamp!!.timeInMillis = `in`.readLong()
        }
        if (`in`.readByte().toInt() == 0) {
            type = null
        } else {
            type = `in`.readInt()
        }
    }

    override fun onTemperatureMeasurementReceived(
        device: BluetoothDevice,
        temperature: Float, unit: Int,
        calendar: Calendar?,
        type: Int?
    ) {
        this.temperature = temperature
        this.unit = unit
        this.timestamp = calendar
        this.type = type
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        super.writeToParcel(dest, flags)
        dest.writeFloat(temperature)
        dest.writeInt(unit)
        if (timestamp == null) {
            dest.writeByte(0.toByte())
        } else {
            dest.writeByte(1.toByte())
            dest.writeLong(timestamp!!.timeInMillis)
        }
        if (type == null) {
            dest.writeByte(0.toByte())
        } else {
            dest.writeByte(1.toByte())
            dest.writeInt(type!!)
        }
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<TemperatureMeasurementResponse> =
            object : Parcelable.Creator<TemperatureMeasurementResponse> {
                override fun createFromParcel(`in`: Parcel): TemperatureMeasurementResponse {
                    return TemperatureMeasurementResponse(`in`)
                }

                override fun newArray(size: Int): Array<TemperatureMeasurementResponse?> {
                    return arrayOfNulls(size)
                }
            }
    }
}
