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
 * ContinuousGlucoseMeasurementResponse response = waitForNotification(characteristic)
 * .awaitValid(ContinuousGlucoseMeasurementResponse.class);
 * float glucoseConcentration = response.getGlucoseConcentration();
 * ...
 * } catch ([RequestFailedException] e) {
 * Log.w(TAG, "Request failed with status " + e.getStatus(), e);
 * } catch ([InvalidDataException] e) {
 * Log.w(TAG, "Invalid data received: " + e.getResponse().getRawData());
 * }
</pre> *
 *
 */
class ContinuousGlucoseMeasurementResponse : ContinuousGlucoseMeasurementDataCallback,
    CRCSecuredResponse, Parcelable {
    var glucoseConcentration: Float = 0.toFloat()
        private set
    var trend: Float? = null
        private set
    var quality: Float? = null
        private set
    var status: CGMTypes.CGMStatus? = null
        private set
    var timeOffset: Int = 0
        private set
    override var isSecured: Boolean = false
        private set
    override var isCrcValid: Boolean = false
        private set

    constructor() {
        // empty
    }

    // Parcelable
    private constructor(`in`: Parcel) : super(`in`) {
        glucoseConcentration = `in`.readFloat()
        if (`in`.readByte().toInt() == 0) {
            trend = null
        } else {
            trend = `in`.readFloat()
        }
        if (`in`.readByte().toInt() == 0) {
            quality = null
        } else {
            quality = `in`.readFloat()
        }
        if (`in`.readByte().toInt() == 0) {
            status = null
        } else {
            val warningStatus = `in`.readInt()
            val calibrationTempStatus = `in`.readInt()
            val sensorStatus = `in`.readInt()
            status = CGMTypes.CGMStatus(warningStatus, calibrationTempStatus, sensorStatus)
        }
        timeOffset = `in`.readInt()
        isSecured = `in`.readByte().toInt() != 0
        isCrcValid = `in`.readByte().toInt() != 0
    }

    override fun onContinuousGlucoseMeasurementReceived(
        device: BluetoothDevice, glucoseConcentration: Float,
        cgmTrend: Float?, cgmQuality: Float?,
        status: CGMTypes.CGMStatus?, timeOffset: Int, secured: Boolean
    ) {
        this.glucoseConcentration = glucoseConcentration
        this.trend = cgmTrend
        this.quality = cgmQuality
        this.status = status
        this.timeOffset = timeOffset
        this.isSecured = secured
        this.isCrcValid = secured
    }

    override fun onContinuousGlucoseMeasurementReceivedWithCrcError(
        device: BluetoothDevice,
        data: Data
    ) {
        onInvalidDataReceived(device, data)
        this.isSecured = true
        this.isCrcValid = false
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        super.writeToParcel(dest, flags)
        dest.writeFloat(glucoseConcentration)
        if (trend == null) {
            dest.writeByte(0.toByte())
        } else {
            dest.writeByte(1.toByte())
            dest.writeFloat(trend!!)
        }
        if (quality == null) {
            dest.writeByte(0.toByte())
        } else {
            dest.writeByte(1.toByte())
            dest.writeFloat(quality!!)
        }
        if (status == null) {
            dest.writeByte(0.toByte())
        } else {
            dest.writeByte(1.toByte())
            dest.writeInt(status!!.warningStatus)
            dest.writeInt(status!!.calibrationTempStatus)
            dest.writeInt(status!!.sensorStatus)
        }
        dest.writeInt(timeOffset)
        dest.writeByte((if (isSecured) 1 else 0).toByte())
        dest.writeByte((if (isCrcValid) 1 else 0).toByte())
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<ContinuousGlucoseMeasurementResponse> =
            object : Parcelable.Creator<ContinuousGlucoseMeasurementResponse> {
                override fun createFromParcel(`in`: Parcel): ContinuousGlucoseMeasurementResponse {
                    return ContinuousGlucoseMeasurementResponse(`in`)
                }

                override fun newArray(size: Int): Array<ContinuousGlucoseMeasurementResponse?> {
                    return arrayOfNulls(size)
                }
            }
    }
}
