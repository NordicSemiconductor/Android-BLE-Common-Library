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

package no.nordicsemi.android.ble.common.callback

import android.bluetooth.BluetoothDevice
import android.os.Parcel
import android.os.Parcelable
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
 * DateTimeResponse response = readCharacteristic(characteristic)
 * .awaitValid(DateTimeResponse.class);
 * Calendar calendar = response.getCalendar();
 * ...
 * } catch ([RequestFailedException] e) {
 * Log.w(TAG, "Request failed with status " + e.getStatus(), e);
 * } catch ([InvalidDataException] e) {
 * Log.w(TAG, "Invalid data received: " + e.getResponse().getRawData());
 * }
</pre> *
 *
 */
class DateTimeResponse : DateTimeDataCallback, Parcelable {
    var calendar: Calendar? = null
        private set

    constructor() {
        // empty
    }

    // Parcelable
    private constructor(`in`: Parcel) : super(`in`) {
        if (`in`.readByte().toInt() == 0) {
            calendar = null
        } else {
            calendar = Calendar.getInstance()
            calendar!!.timeInMillis = `in`.readLong()
        }
    }

    override fun onDateTimeReceived(device: BluetoothDevice, calendar: Calendar) {
        this.calendar = calendar
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        super.writeToParcel(dest, flags)
        if (calendar == null) {
            dest.writeByte(0.toByte())
        } else {
            dest.writeByte(1.toByte())
            dest.writeLong(calendar!!.timeInMillis)
        }
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<DateTimeResponse> =
            object : Parcelable.Creator<DateTimeResponse> {
                override fun createFromParcel(`in`: Parcel): DateTimeResponse {
                    return DateTimeResponse(`in`)
                }

                override fun newArray(size: Int): Array<DateTimeResponse?> {
                    return arrayOfNulls(size)
                }
            }
    }
}
