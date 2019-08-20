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
 * RunningSpeedAndCadenceMeasurementResponse response = waitForIndication(characteristic)
 * .awaitValid(RunningSpeedAndCadenceMeasurementResponse.class);
 * float speedMetersPerSecond = response.getInstantaneousSpeed();
 * ...
 * } catch ([RequestFailedException] e) {
 * Log.w(TAG, "Request failed with status " + e.getStatus(), e);
 * } catch ([InvalidDataException] e) {
 * Log.w(TAG, "Invalid data received: " + e.getResponse().getRawData());
 * }
</pre> *
 *
 */
class RunningSpeedAndCadenceMeasurementResponse : RunningSpeedAndCadenceMeasurementDataCallback,
    Parcelable {
    /**
     * True if running has been detected. False otherwise (walking, or activity status not supported).
     *
     * @return True if user is running, false otherwise.
     */
    var isRunning: Boolean = false
        private set
    /**
     * Returns instantaneous speed.
     *
     * @return Speed in meters per second.
     */
    var instantaneousSpeed: Float = 0.toFloat()
        private set
    /**
     * Returns instantaneous cadence in revolution per minute (RPM).
     *
     * @return instantaneous cadence in 1/minute unit (RPM).
     */
    var instantaneousCadence: Int = 0
        private set
    /**
     * Returns the stride length in centimeters if such data was present in the packet.
     *
     * @return Stride length in centimeters or null, if data not available.
     */
    var strideLength: Int? = null
        private set
    /**
     * Returns total distance traveled with the device, in meters. This value is returned as
     * Long as the type returned is UINT32, and may be greater than Integer range.
     *
     * @return Total distance, in meters.
     */
    var totalDistance: Long? = null
        private set

    constructor() {
        // empty
    }

    // Parcelable
    private constructor(`in`: Parcel) : super(`in`) {
        isRunning = `in`.readByte().toInt() != 0
        instantaneousSpeed = `in`.readFloat()
        instantaneousCadence = `in`.readInt()
        if (`in`.readByte().toInt() == 0) {
            strideLength = null
        } else {
            strideLength = `in`.readInt()
        }
        if (`in`.readByte().toInt() == 0) {
            totalDistance = null
        } else {
            totalDistance = `in`.readLong()
        }
    }

    override fun onRSCMeasurementReceived(
        device: BluetoothDevice, running: Boolean,
        instantaneousSpeed: Float, instantaneousCadence: Int,
        strideLength: Int?,
        totalDistance: Long?
    ) {
        this.isRunning = running
        this.instantaneousSpeed = instantaneousSpeed
        this.instantaneousCadence = instantaneousCadence
        this.strideLength = strideLength
        this.totalDistance = totalDistance
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        super.writeToParcel(dest, flags)
        dest.writeByte((if (isRunning) 1 else 0).toByte())
        dest.writeFloat(instantaneousSpeed)
        dest.writeInt(instantaneousCadence)
        if (strideLength == null) {
            dest.writeByte(0.toByte())
        } else {
            dest.writeByte(1.toByte())
            dest.writeInt(strideLength!!)
        }
        if (totalDistance == null) {
            dest.writeByte(0.toByte())
        } else {
            dest.writeByte(1.toByte())
            dest.writeLong(totalDistance!!)
        }
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<RunningSpeedAndCadenceMeasurementResponse> =
            object : Parcelable.Creator<RunningSpeedAndCadenceMeasurementResponse> {
                override fun createFromParcel(`in`: Parcel): RunningSpeedAndCadenceMeasurementResponse {
                    return RunningSpeedAndCadenceMeasurementResponse(`in`)
                }

                override fun newArray(size: Int): Array<RunningSpeedAndCadenceMeasurementResponse?> {
                    return arrayOfNulls(size)
                }
            }
    }
}
