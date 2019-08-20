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

package no.nordicsemi.android.ble.common.data.cgm

import androidx.annotation.FloatRange
import androidx.annotation.IntRange

import no.nordicsemi.android.ble.common.profile.cgm.CGMTypes
import no.nordicsemi.android.ble.common.profile.glucose.GlucoseSampleLocation
import no.nordicsemi.android.ble.common.profile.glucose.GlucoseSampleType
import no.nordicsemi.android.ble.common.util.CRC16
import no.nordicsemi.android.ble.data.Data
import no.nordicsemi.android.ble.data.MutableData

class CGMSpecificOpsControlPointData private constructor()// empty private constructor
    : CGMTypes {
    companion object {
        private val OP_CODE_SET_COMMUNICATION_INTERVAL: Byte = 1
        private val OP_CODE_GET_COMMUNICATION_INTERVAL: Byte = 2
        private val OP_CODE_SET_CALIBRATION_VALUE: Byte = 4
        private val OP_CODE_GET_CALIBRATION_VALUE: Byte = 5
        private val OP_CODE_SET_PATIENT_HIGH_ALERT_LEVEL: Byte = 7
        private val OP_CODE_GET_PATIENT_HIGH_ALERT_LEVEL: Byte = 8
        private val OP_CODE_SET_PATIENT_LOW_ALERT_LEVEL: Byte = 10
        private val OP_CODE_GET_PATIENT_LOW_ALERT_LEVEL: Byte = 11
        private val OP_CODE_SET_HYPO_ALERT_LEVEL: Byte = 13
        private val OP_CODE_GET_HYPO_ALERT_LEVEL: Byte = 14
        private val OP_CODE_SET_HYPER_ALERT_LEVEL: Byte = 16
        private val OP_CODE_GET_HYPER_ALERT_LEVEL: Byte = 17
        private val OP_CODE_SET_RATE_OF_DECREASE_ALERT_LEVEL: Byte = 19
        private val OP_CODE_GET_RATE_OF_DECREASE_ALERT_LEVEL: Byte = 20
        private val OP_CODE_SET_RATE_OF_INCREASE_ALERT_LEVEL: Byte = 22
        private val OP_CODE_GET_RATE_OF_INCREASE_ALERT_LEVEL: Byte = 23
        private val OP_CODE_RESET_DEVICE_SPECIFIC_ERROR: Byte = 25
        private val OP_CODE_START_SESSION: Byte = 26
        private val OP_CODE_STOP_SESSION: Byte = 27

        fun startSession(secure: Boolean): Data {
            return create(OP_CODE_START_SESSION, secure)
        }

        fun stopSession(secure: Boolean): Data {
            return create(OP_CODE_STOP_SESSION, secure)
        }

        fun resetDeviceSpecificAlert(secure: Boolean): Data {
            return create(OP_CODE_RESET_DEVICE_SPECIFIC_ERROR, secure)
        }

        fun setCommunicationInterval(
            @IntRange(from = 0, to = 65535) interval: Int,
            secure: Boolean
        ): Data {
            return create(OP_CODE_SET_COMMUNICATION_INTERVAL, interval, Data.FORMAT_UINT8, secure)
        }

        fun setCommunicationIntervalToFastestSupported(secure: Boolean): Data {
            return create(OP_CODE_SET_COMMUNICATION_INTERVAL, 0xFF, Data.FORMAT_UINT8, secure)
        }

        fun disablePeriodicCommunication(secure: Boolean): Data {
            return create(OP_CODE_SET_COMMUNICATION_INTERVAL, 0xFF, Data.FORMAT_UINT8, secure)
        }

        fun getCommunicationInterval(secure: Boolean): Data {
            return create(OP_CODE_GET_COMMUNICATION_INTERVAL, secure)
        }

        fun setCalibrationValue(
            @FloatRange(from = 0.0) glucoseConcentrationOfCalibration: Float,
            @GlucoseSampleType sampleType: Int,
            @GlucoseSampleLocation sampleLocation: Int,
            @IntRange(from = 0, to = 65535) calibrationTime: Int,
            @IntRange(from = 0, to = 65535) nextCalibrationTime: Int,
            secure: Boolean
        ): Data {
            val data = MutableData(ByteArray(11 + if (secure) 2 else 0))
            data.setByte(OP_CODE_SET_CALIBRATION_VALUE.toInt(), 0)
            data.setValue(glucoseConcentrationOfCalibration, Data.FORMAT_SFLOAT, 1)
            data.setValue(calibrationTime, Data.FORMAT_UINT16, 3)
            val typeAndSampleLocation = sampleLocation and 0xF shl 8 or (sampleType and 0xF)
            data.setValue(typeAndSampleLocation, Data.FORMAT_UINT8, 5)
            data.setValue(nextCalibrationTime, Data.FORMAT_UINT16, 6)
            data.setValue(0, Data.FORMAT_UINT16, 8) // ignored: calibration data record number
            data.setValue(0, Data.FORMAT_UINT8, 10) // ignored: calibration status
            return appendCrc(data, secure)
        }

        fun getCalibrationValue(@IntRange(from = 0) calibrationDataRecordNumber: Int, secure: Boolean): Data {
            return create(
                OP_CODE_GET_CALIBRATION_VALUE,
                calibrationDataRecordNumber,
                Data.FORMAT_UINT16,
                secure
            )
        }

        fun getLastCalibrationValue(secure: Boolean): Data {
            return create(OP_CODE_GET_CALIBRATION_VALUE, 0xFFFF, Data.FORMAT_UINT16, secure)
        }

        fun setPatientHighAlertLevel(
            @FloatRange(from = 0.0) level: Float,
            secure: Boolean
        ): Data {
            return create(OP_CODE_SET_PATIENT_HIGH_ALERT_LEVEL, level, secure)
        }

        fun getPatientHighAlertLevel(secure: Boolean): Data {
            return create(OP_CODE_GET_PATIENT_HIGH_ALERT_LEVEL, secure)
        }

        fun setPatientLowAlertLevel(
            @FloatRange(from = 0.0) level: Float,
            secure: Boolean
        ): Data {
            return create(OP_CODE_SET_PATIENT_LOW_ALERT_LEVEL, level, secure)
        }

        fun getPatientLowAlertLevel(secure: Boolean): Data {
            return create(OP_CODE_GET_PATIENT_LOW_ALERT_LEVEL, secure)
        }

        fun setHypoAlertLevel(
            @FloatRange(from = 0.0) level: Float,
            secure: Boolean
        ): Data {
            return create(OP_CODE_SET_HYPO_ALERT_LEVEL, level, secure)
        }

        fun getHypoAlertLevel(secure: Boolean): Data {
            return create(OP_CODE_GET_HYPO_ALERT_LEVEL, secure)
        }

        fun setHyperAlertLevel(
            @FloatRange(from = 0.0) level: Float,
            secure: Boolean
        ): Data {
            return create(OP_CODE_SET_HYPER_ALERT_LEVEL, level, secure)
        }

        fun getHyperAlertLevel(secure: Boolean): Data {
            return create(OP_CODE_GET_HYPER_ALERT_LEVEL, secure)
        }

        fun setRateOfDecreaseAlertLevel(
            @FloatRange(from = 0.0) level: Float,
            secure: Boolean
        ): Data {
            return create(OP_CODE_SET_RATE_OF_DECREASE_ALERT_LEVEL, level, secure)
        }

        fun getRateOfDecreaseAlertLevel(secure: Boolean): Data {
            return create(OP_CODE_GET_RATE_OF_DECREASE_ALERT_LEVEL, secure)
        }

        fun setRateOfIncreaseAlertLevel(
            @FloatRange(from = 0.0) level: Float,
            secure: Boolean
        ): Data {
            return create(OP_CODE_SET_RATE_OF_INCREASE_ALERT_LEVEL, level, secure)
        }

        fun getRateOfIncreaseAlertLevel(secure: Boolean): Data {
            return create(OP_CODE_GET_RATE_OF_INCREASE_ALERT_LEVEL, secure)
        }

        private fun create(opCode: Byte, secure: Boolean): Data {
            val data = MutableData(ByteArray(1 + if (secure) 2 else 0))
            data.setByte(opCode.toInt(), 0)
            return appendCrc(data, secure)
        }

        private fun create(opCode: Byte, value: Int, format: Int, secure: Boolean): Data {
            val data = MutableData(ByteArray(1 + (format and 0xF) + if (secure) 2 else 0))
            data.setByte(opCode.toInt(), 0)
            data.setValue(value, format, 1)
            return appendCrc(data, secure)
        }

        private fun create(opCode: Byte, value: Float, secure: Boolean): Data {
            val data = MutableData(ByteArray(3 + if (secure) 2 else 0))
            data.setByte(opCode.toInt(), 0)
            data.setValue(value, Data.FORMAT_SFLOAT, 1)
            return appendCrc(data, secure)
        }

        private fun appendCrc(data: MutableData, secure: Boolean): Data {
            if (secure) {
                val length = data.size() - 2
                val crc = CRC16.MCRF4XX(data.value!!, 0, length)
                data.setValue(crc, Data.FORMAT_UINT16, length)
            }
            return data
        }
    }
}
