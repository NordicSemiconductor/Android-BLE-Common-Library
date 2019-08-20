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

import no.nordicsemi.android.ble.callback.profile.ProfileReadResponse
import no.nordicsemi.android.ble.common.profile.csc.CyclingSpeedAndCadenceCallback
import no.nordicsemi.android.ble.common.profile.csc.CyclingSpeedAndCadenceMeasurementCallback
import no.nordicsemi.android.ble.data.Data

/**
 * Data callback that parses value into CSC Measurement data.
 * If the value received do not match required syntax
 * [.onInvalidDataReceived] callback will be called.
 * will be called.
 * See: https://www.bluetooth.com/specifications/gatt/viewer?attributeXmlFile=org.bluetooth.characteristic.csc_measurement.xml
 */
abstract class CyclingSpeedAndCadenceMeasurementDataCallback : ProfileReadResponse,
    CyclingSpeedAndCadenceMeasurementCallback, CyclingSpeedAndCadenceCallback {
    private var mInitialWheelRevolutions: Long = -1
    private var mLastWheelRevolutions: Long = -1
    private var mLastWheelEventTime = -1
    private var mLastCrankRevolutions = -1
    private var mLastCrankEventTime = -1
    private var mWheelCadence = -1f

    constructor() {
        // empty
    }

    protected constructor(`in`: Parcel) : super(`in`)

    override fun onDataReceived(device: BluetoothDevice, data: Data) {
        super.onDataReceived(device, data)

        if (data.size() < 1) {
            onInvalidDataReceived(device, data)
            return
        }

        // Decode the new data
        var offset = 0
        val flags = data.getByte(offset)!!.toInt()
        offset += 1

        val wheelRevPresent = flags and 0x01 != 0
        val crankRevPreset = flags and 0x02 != 0

        if (data.size() < 1 + (if (wheelRevPresent) 6 else 0) + (if (crankRevPreset) 4 else 0)) {
            onInvalidDataReceived(device, data)
            return
        }

        if (wheelRevPresent) {
            val wheelRevolutions = data.getIntValue(Data.FORMAT_UINT32, offset)
                ?.and(0xFFFFFFFFL.toInt())
            offset += 4

            val lastWheelEventTime = data.getIntValue(Data.FORMAT_UINT16, offset)!! // 1/1024 s
            offset += 2

            if (mInitialWheelRevolutions < 0)
                mInitialWheelRevolutions = wheelRevolutions?.toLong() ?: -1L

            // Notify listener about the new measurement
            onWheelMeasurementReceived(
                device,
                wheelRevolutions?.toLong() ?: -1L,
                lastWheelEventTime
            )
        }

        if (crankRevPreset) {
            val crankRevolutions = data.getIntValue(Data.FORMAT_UINT16, offset)!!
            offset += 2

            val lastCrankEventTime = data.getIntValue(Data.FORMAT_UINT16, offset)!!
            // offset += 2;

            // Notify listener about the new measurement
            onCrankMeasurementReceived(device, crankRevolutions, lastCrankEventTime)
        }
    }

    override fun onWheelMeasurementReceived(
        device: BluetoothDevice,
        wheelRevolutions: Long,
        lastWheelEventTime: Int
    ) {
        if (mLastWheelEventTime == lastWheelEventTime)
            return

        if (mLastWheelRevolutions >= 0) {
            val circumference = wheelCircumference

            val timeDifference: Float
            if (lastWheelEventTime < mLastWheelEventTime)
                timeDifference = (65535 + lastWheelEventTime - mLastWheelEventTime) / 1024.0f // [s]
            else
                timeDifference = (lastWheelEventTime - mLastWheelEventTime) / 1024.0f // [s]
            val distanceDifference =
                (wheelRevolutions - mLastWheelRevolutions) * circumference / 1000.0f // [m]
            val totalDistance = wheelRevolutions.toFloat() * circumference / 1000.0f // [m]
            val distance =
                (wheelRevolutions - mInitialWheelRevolutions).toFloat() * circumference / 1000.0f // [m]
            val speed = distanceDifference / timeDifference // [m/s]
            mWheelCadence =
                (wheelRevolutions - mLastWheelRevolutions) * 60.0f / timeDifference // [revolutions/minute]

            // Notify listener about the new measurement
            onDistanceChanged(device, totalDistance, distance, speed)
        }
        mLastWheelRevolutions = wheelRevolutions
        mLastWheelEventTime = lastWheelEventTime
    }

    override fun onCrankMeasurementReceived(
        device: BluetoothDevice,
        crankRevolutions: Int,
        lastCrankEventTime: Int
    ) {
        if (mLastCrankEventTime == lastCrankEventTime)
            return

        if (mLastCrankRevolutions >= 0) {
            val timeDifference: Float
            if (lastCrankEventTime < mLastCrankEventTime)
                timeDifference = (65535 + lastCrankEventTime - mLastCrankEventTime) / 1024.0f // [s]
            else
                timeDifference = (lastCrankEventTime - mLastCrankEventTime) / 1024.0f // [s]

            val crankCadence =
                (crankRevolutions - mLastCrankRevolutions) * 60.0f / timeDifference // [revolutions/minute]
            if (crankCadence > 0) {
                val gearRatio = if (mWheelCadence >= 0) mWheelCadence / crankCadence else 0.0f

                // Notify listener about the new measurement
                onCrankDataChanged(device, crankCadence, gearRatio)
            } else {
                // Notify listener about the new measurement
                // onCrankDataChanged(device, 0, 0);
            }
        }
        mLastCrankRevolutions = crankRevolutions
        mLastCrankEventTime = lastCrankEventTime
    }
}
