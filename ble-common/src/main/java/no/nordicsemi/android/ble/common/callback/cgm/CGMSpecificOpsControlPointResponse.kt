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
import no.nordicsemi.android.ble.common.profile.cgm.CGMSpecificOpsControlPointCallback
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
 * CGMSpecificOpsControlPointResponse response = waitForIndication(characteristic)
 * .trigger(CGMSpecificOpsControlPointData.startSession())
 * .awaitValid(CGMSpecificOpsControlPointResponse.class);
 * if (response.isOperationCompleted()) {
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
class CGMSpecificOpsControlPointResponse : CGMSpecificOpsControlPointDataCallback,
    CRCSecuredResponse, Parcelable {
    var isOperationCompleted: Boolean = false
        private set
    override var isSecured: Boolean = false
        private set
    override var isCrcValid: Boolean = false
        private set
    var requestCode: Int = 0
        private set
    var errorCode: Int = 0
        private set
    var glucoseCommunicationInterval: Int = 0
        private set
    var glucoseConcentrationOfCalibration: Float = 0.toFloat()
        private set
    var calibrationTime: Int = 0
        private set
    var nextCalibrationTime: Int = 0
        private set
    var type: Int = 0
        private set
    var sampleLocation: Int = 0
        private set
    var calibrationDataRecordNumber: Int = 0
        private set
    var calibrationStatus: CGMTypes.CGMCalibrationStatus? = null
        private set
    var alertLevel: Float = 0.toFloat()
        private set

    constructor() {
        // empty
    }

    // Parcelable
    private constructor(`in`: Parcel) : super(`in`) {
        isOperationCompleted = `in`.readByte().toInt() != 0
        isSecured = `in`.readByte().toInt() != 0
        isCrcValid = `in`.readByte().toInt() != 0
        requestCode = `in`.readInt()
        errorCode = `in`.readInt()
        glucoseCommunicationInterval = `in`.readInt()
        glucoseConcentrationOfCalibration = `in`.readFloat()
        calibrationTime = `in`.readInt()
        nextCalibrationTime = `in`.readInt()
        type = `in`.readInt()
        sampleLocation = `in`.readInt()
        calibrationDataRecordNumber = `in`.readInt()
        if (`in`.readByte().toInt() == 0) {
            calibrationStatus = null
        } else {
            calibrationStatus = CGMTypes.CGMCalibrationStatus(`in`.readInt())
        }
        alertLevel = `in`.readFloat()
    }

    override fun onCGMSpecificOpsOperationCompleted(
        device: BluetoothDevice,
        requestCode: Int,
        secured: Boolean
    ) {
        this.isOperationCompleted = true
        this.requestCode = requestCode
        this.isSecured = secured
        this.isCrcValid = secured
    }

    override fun onCGMSpecificOpsOperationError(
        device: BluetoothDevice,
        requestCode: Int,
        errorCode: Int,
        secured: Boolean
    ) {
        this.isOperationCompleted = false
        this.requestCode = requestCode
        this.errorCode = errorCode
        this.isSecured = secured
        this.isCrcValid = secured
    }

    override fun onCGMSpecificOpsResponseReceivedWithCrcError(device: BluetoothDevice, data: Data) {
        onInvalidDataReceived(device, data)
        this.isOperationCompleted = false
        this.isSecured = true
        this.isCrcValid = false
    }

    override fun onContinuousGlucoseCommunicationIntervalReceived(
        device: BluetoothDevice,
        interval: Int,
        secured: Boolean
    ) {
        this.isOperationCompleted = true
        this.requestCode =
            CGMSpecificOpsControlPointCallback.Companion.CGM_OP_CODE_SET_COMMUNICATION_INTERVAL
        this.glucoseCommunicationInterval = interval
        this.isSecured = secured
        this.isCrcValid = secured
    }

    override fun onContinuousGlucoseCalibrationValueReceived(
        device: BluetoothDevice, glucoseConcentrationOfCalibration: Float,
        calibrationTime: Int, nextCalibrationTime: Int,
        type: Int, sampleLocation: Int, calibrationDataRecordNumber: Int,
        status: CGMTypes.CGMCalibrationStatus, secured: Boolean
    ) {
        this.isOperationCompleted = true
        this.requestCode =
            CGMSpecificOpsControlPointCallback.Companion.CGM_OP_CODE_SET_CALIBRATION_VALUE
        this.glucoseConcentrationOfCalibration = glucoseConcentrationOfCalibration
        this.calibrationTime = calibrationTime
        this.nextCalibrationTime = nextCalibrationTime
        this.type = type
        this.sampleLocation = sampleLocation
        this.calibrationDataRecordNumber = calibrationDataRecordNumber
        this.calibrationStatus = status
        this.isSecured = secured
        this.isCrcValid = secured
    }

    override fun onContinuousGlucosePatientHighAlertReceived(
        device: BluetoothDevice,
        alertLevel: Float,
        secured: Boolean
    ) {
        this.isOperationCompleted = true
        this.requestCode =
            CGMSpecificOpsControlPointCallback.Companion.CGM_OP_CODE_SET_PATIENT_HIGH_ALERT_LEVEL
        this.alertLevel = alertLevel
        this.isSecured = secured
        this.isCrcValid = secured
    }

    override fun onContinuousGlucosePatientLowAlertReceived(
        device: BluetoothDevice,
        alertLevel: Float,
        secured: Boolean
    ) {
        this.isOperationCompleted = true
        this.requestCode =
            CGMSpecificOpsControlPointCallback.Companion.CGM_OP_CODE_SET_PATIENT_LOW_ALERT_LEVEL
        this.alertLevel = alertLevel
        this.isSecured = secured
        this.isCrcValid = secured
    }

    override fun onContinuousGlucoseHypoAlertReceived(
        device: BluetoothDevice,
        alertLevel: Float,
        secured: Boolean
    ) {
        this.isOperationCompleted = true
        this.requestCode =
            CGMSpecificOpsControlPointCallback.Companion.CGM_OP_CODE_SET_HYPO_ALERT_LEVEL
        this.alertLevel = alertLevel
        this.isSecured = secured
        this.isCrcValid = secured
    }

    override fun onContinuousGlucoseHyperAlertReceived(
        device: BluetoothDevice,
        alertLevel: Float,
        secured: Boolean
    ) {
        this.isOperationCompleted = true
        this.requestCode =
            CGMSpecificOpsControlPointCallback.Companion.CGM_OP_CODE_SET_HYPER_ALERT_LEVEL
        this.alertLevel = alertLevel
        this.isSecured = secured
        this.isCrcValid = secured
    }

    override fun onContinuousGlucoseRateOfDecreaseAlertReceived(
        device: BluetoothDevice,
        alertLevel: Float,
        secured: Boolean
    ) {
        this.isOperationCompleted = true
        this.requestCode =
            CGMSpecificOpsControlPointCallback.Companion.CGM_OP_CODE_SET_RATE_OF_DECREASE_ALERT_LEVEL
        this.alertLevel = alertLevel
        this.isSecured = secured
        this.isCrcValid = secured
    }

    override fun onContinuousGlucoseRateOfIncreaseAlertReceived(
        device: BluetoothDevice,
        alertLevel: Float,
        secured: Boolean
    ) {
        this.isOperationCompleted = true
        this.requestCode =
            CGMSpecificOpsControlPointCallback.Companion.CGM_OP_CODE_SET_RATE_OF_INCREASE_ALERT_LEVEL
        this.alertLevel = alertLevel
        this.isSecured = secured
        this.isCrcValid = secured
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        super.writeToParcel(dest, flags)
        dest.writeByte((if (isOperationCompleted) 1 else 0).toByte())
        dest.writeByte((if (isSecured) 1 else 0).toByte())
        dest.writeByte((if (isCrcValid) 1 else 0).toByte())
        dest.writeInt(requestCode)
        dest.writeInt(errorCode)
        dest.writeInt(glucoseCommunicationInterval)
        dest.writeFloat(glucoseConcentrationOfCalibration)
        dest.writeInt(calibrationTime)
        dest.writeInt(nextCalibrationTime)
        dest.writeInt(type)
        dest.writeInt(sampleLocation)
        dest.writeInt(calibrationDataRecordNumber)
        if (calibrationStatus == null) {
            dest.writeByte(0.toByte())
        } else {
            dest.writeByte(1.toByte())
            dest.writeInt(calibrationStatus!!.value)
        }
        dest.writeFloat(alertLevel)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<CGMSpecificOpsControlPointResponse> =
            object : Parcelable.Creator<CGMSpecificOpsControlPointResponse> {
                override fun createFromParcel(`in`: Parcel): CGMSpecificOpsControlPointResponse {
                    return CGMSpecificOpsControlPointResponse(`in`)
                }

                override fun newArray(size: Int): Array<CGMSpecificOpsControlPointResponse?> {
                    return arrayOfNulls(size)
                }
            }
    }
}
