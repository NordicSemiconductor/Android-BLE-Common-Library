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

package no.nordicsemi.android.ble.common.callback.csc

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
 * CyclingSpeedAndCadenceResponse response = waitForNotification(characteristic)
 * .awaitValid(CyclingSpeedAndCadenceResponse.class);
 * float totalDistance = response.getTotalDistance(2340.0f);
 * ...
 * } catch ([RequestFailedException] e) {
 * Log.w(TAG, "Request failed with status " + e.getStatus(), e);
 * } catch ([InvalidDataException] e) {
 * Log.w(TAG, "Invalid data received: " + e.getResponse().getRawData());
 * }
</pre> *
 *
 */
class CyclingSpeedAndCadenceMeasurementResponse : CyclingSpeedAndCadenceMeasurementDataCallback,
    Parcelable {
    var wheelRevolutions: Long = 0
        private set
    var crankRevolutions: Long = 0
        private set
    private var lastWheelEventTime: Int = 0
    private var lastCrankEventTime: Int = 0

    constructor() {
        // empty
    }

    // Parcelable
    private constructor(`in`: Parcel) : super(`in`) {
        wheelRevolutions = `in`.readLong()
        crankRevolutions = `in`.readLong()
        lastWheelEventTime = `in`.readInt()
        lastCrankEventTime = `in`.readInt()
    }

    override fun onWheelMeasurementReceived(
        device: BluetoothDevice,
        wheelRevolutions: Long,
        lastWheelEventTime: Int
    ) {
        this.wheelRevolutions = wheelRevolutions
        this.lastWheelEventTime = lastWheelEventTime
    }

    override fun onCrankMeasurementReceived(
        device: BluetoothDevice,
        crankRevolutions: Int,
        lastCrankEventTime: Int
    ) {
        this.crankRevolutions = crankRevolutions.toLong()
        this.lastCrankEventTime = lastCrankEventTime
    }

    override fun onDistanceChanged(
        device: BluetoothDevice,
        totalDistance: Float,
        distance: Float,
        speed: Float
    ) {
        // never called, as the wheel circumference is not known here
    }

    override fun onCrankDataChanged(
        device: BluetoothDevice,
        crankCadence: Float,
        gearRatio: Float
    ) {
        // never called
    }

    fun getLastWheelEventTime(): Long {
        return lastWheelEventTime.toLong()
    }

    fun getLastCrankEventTime(): Long {
        return lastCrankEventTime.toLong()
    }

    /**
     * Returns the total distance since the device was reset.
     *
     * @param wheelCircumference the wheel circumference in millimeters.
     * @return total distance traveled, in meters.
     */
    fun getTotalDistance(wheelCircumference: Float): Float {
        return wheelRevolutions.toFloat() * wheelCircumference / 1000.0f // [m]
    }

    /**
     * Returns the distance traveled since the given response was received.
     *
     * @param wheelCircumference the wheel circumference in millimeters.
     * @param previous           a previous response.
     * @return distance traveled since the previous response, in meters.
     */
    fun getDistance(
        wheelCircumference: Float,
        previous: CyclingSpeedAndCadenceMeasurementResponse
    ): Float {
        return (wheelRevolutions - previous.wheelRevolutions).toFloat() * wheelCircumference / 1000.0f // [m]
    }

    /**
     * Returns the average speed since the previous response was received.
     *
     * @param wheelCircumference the wheel circumference in millimeters.
     * @param previous           a previous response.
     * @return speed in meters per second.
     */
    fun getSpeed(
        wheelCircumference: Float,
        previous: CyclingSpeedAndCadenceMeasurementResponse
    ): Float {
        val timeDifference: Float
        if (lastWheelEventTime < previous.lastWheelEventTime)
            timeDifference =
                (65535 + lastWheelEventTime - previous.lastWheelEventTime) / 1024.0f // [s]
        else
            timeDifference = (lastWheelEventTime - previous.lastWheelEventTime) / 1024.0f // [s]

        return getDistance(wheelCircumference, previous) / timeDifference // [m/s]
    }

    /**
     * Returns average wheel cadence since the previous message was received.
     *
     * @param previous a previous response.
     * @return wheel cadence in revolutions per minute.
     */
    fun getWheelCadence(previous: CyclingSpeedAndCadenceMeasurementResponse): Float {
        val timeDifference: Float
        if (lastWheelEventTime < previous.lastWheelEventTime)
            timeDifference =
                (65535 + lastWheelEventTime - previous.lastWheelEventTime) / 1024.0f // [s]
        else
            timeDifference = (lastWheelEventTime - previous.lastWheelEventTime) / 1024.0f // [s]

        return if (timeDifference == 0f) 0.0f else (wheelRevolutions - previous.wheelRevolutions) * 60.0f / timeDifference

// [revolutions/minute];
    }

    /**
     * Returns average crank cadence since the previous message was received.
     *
     * @param previous a previous response.
     * @return crank cadence in revolutions per minute.
     */
    fun getCrankCadence(previous: CyclingSpeedAndCadenceMeasurementResponse): Float {
        val timeDifference: Float
        if (lastCrankEventTime < previous.lastCrankEventTime)
            timeDifference =
                (65535 + lastCrankEventTime - previous.lastCrankEventTime) / 1024.0f // [s]
        else
            timeDifference = (lastCrankEventTime - previous.lastCrankEventTime) / 1024.0f // [s]

        return if (timeDifference == 0f) 0.0f else (crankRevolutions - previous.crankRevolutions) * 60.0f / timeDifference

// [revolutions/minute];
    }

    /**
     * Returns the gear ratio (equal to wheel cadence / crank cadence).
     *
     * @param previous a previous response.
     * @return gear ratio.
     */
    fun getGearRatio(previous: CyclingSpeedAndCadenceMeasurementResponse): Float {
        val crankCadence = getCrankCadence(previous)
        return if (crankCadence > 0) {
            getWheelCadence(previous) / crankCadence
        } else {
            0.0f
        }
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        super.writeToParcel(dest, flags)
        dest.writeLong(wheelRevolutions)
        dest.writeLong(crankRevolutions)
        dest.writeInt(lastWheelEventTime)
        dest.writeInt(lastCrankEventTime)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<CyclingSpeedAndCadenceMeasurementResponse> =
            object : Parcelable.Creator<CyclingSpeedAndCadenceMeasurementResponse> {
                override fun createFromParcel(`in`: Parcel): CyclingSpeedAndCadenceMeasurementResponse {
                    return CyclingSpeedAndCadenceMeasurementResponse(`in`)
                }

                override fun newArray(size: Int): Array<CyclingSpeedAndCadenceMeasurementResponse?> {
                    return arrayOfNulls(size)
                }
            }
    }
}
