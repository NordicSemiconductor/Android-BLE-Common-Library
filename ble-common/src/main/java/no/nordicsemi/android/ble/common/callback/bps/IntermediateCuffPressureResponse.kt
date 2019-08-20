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

package no.nordicsemi.android.ble.common.callback.bps

import android.bluetooth.BluetoothDevice
import android.os.Parcel
import android.os.Parcelable
import no.nordicsemi.android.ble.common.profile.bp.BloodPressureTypes
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
 * IntermediateCuffPressureResponse response = readCharacteristic(characteristic)
 * .awaitValid(IntermediateCuffPressureResponse.class);
 * float pressureMmHg = toMmHg(response.getCuffPressure(), response.getUnit());
 * ...
 * } catch ([RequestFailedException] e) {
 * Log.w(TAG, "Request failed with status " + e.getStatus(), e);
 * } catch ([InvalidDataException] e) {
 * Log.w(TAG, "Invalid data received: " + e.getResponse().getRawData());
 * }
</pre> *
 *
 */
class IntermediateCuffPressureResponse : IntermediateCuffPressureDataCallback, Parcelable {
    /**
     * Returns the received cuff pressure in the unit returned by [.getUnit].
     *
     * @return The cuff pressure value.
     */
    var cuffPressure: Float = 0.toFloat()
        private set
    /**
     * Returns the measurement unit, one of [.UNIT_mmHg] or [.UNIT_kPa].
     * To convert to proper unit, use [.toMmHg] or [.toKPa].
     *
     * @return Unit of systolic, diastolic and mean arterial pressure.
     */
    var unit: Int = 0
        private set
    var pulseRate: Float? = null
        private set
    var userID: Int? = null
        private set
    var status: BloodPressureTypes.BPMStatus? = null
        private set
    var timestamp: Calendar? = null
        private set

    constructor() {
        // empty
    }

    // Parcelable
    private constructor(`in`: Parcel) : super(`in`) {
        cuffPressure = `in`.readFloat()
        unit = `in`.readInt()
        if (`in`.readByte().toInt() == 0) {
            pulseRate = null
        } else {
            pulseRate = `in`.readFloat()
        }
        if (`in`.readByte().toInt() == 0) {
            userID = null
        } else {
            userID = `in`.readInt()
        }
        if (`in`.readByte().toInt() == 0) {
            status = null
        } else {
            status = BloodPressureTypes.BPMStatus(`in`.readInt())
        }
        if (`in`.readByte().toInt() == 0) {
            timestamp = null
        } else {
            timestamp = Calendar.getInstance()
            timestamp!!.timeInMillis = `in`.readLong()
        }
    }

    override fun onIntermediateCuffPressureReceived(
        device: BluetoothDevice,
        cuffPressure: Float, unit: Int,
        pulseRate: Float?,
        userID: Int?,
        status: BloodPressureTypes.BPMStatus?,
        calendar: Calendar?
    ) {
        this.cuffPressure = cuffPressure
        this.unit = unit
        this.pulseRate = pulseRate
        this.userID = userID
        this.status = status
        this.timestamp = calendar
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        super.writeToParcel(dest, flags)
        dest.writeFloat(cuffPressure)
        dest.writeInt(unit)
        if (pulseRate == null) {
            dest.writeByte(0.toByte())
        } else {
            dest.writeByte(1.toByte())
            dest.writeFloat(pulseRate!!)
        }
        if (userID == null) {
            dest.writeByte(0.toByte())
        } else {
            dest.writeByte(1.toByte())
            dest.writeInt(userID!!)
        }
        if (status == null) {
            dest.writeByte(0.toByte())
        } else {
            dest.writeByte(1.toByte())
            dest.writeInt(status!!.value)
        }
        if (timestamp == null) {
            dest.writeByte(0.toByte())
        } else {
            dest.writeByte(1.toByte())
            dest.writeLong(timestamp!!.timeInMillis)
        }
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<IntermediateCuffPressureResponse> =
            object : Parcelable.Creator<IntermediateCuffPressureResponse> {
                override fun createFromParcel(`in`: Parcel): IntermediateCuffPressureResponse {
                    return IntermediateCuffPressureResponse(`in`)
                }

                override fun newArray(size: Int): Array<IntermediateCuffPressureResponse?> {
                    return arrayOfNulls(size)
                }
            }
    }
}
