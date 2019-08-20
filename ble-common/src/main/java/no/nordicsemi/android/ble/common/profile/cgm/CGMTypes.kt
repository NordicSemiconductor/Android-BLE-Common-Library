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

package no.nordicsemi.android.ble.common.profile.cgm

import no.nordicsemi.android.ble.common.profile.glucose.GlucoseTypes

interface CGMTypes : GlucoseTypes {

    class CGMFeatures(val value: Int) {
        val calibrationSupported: Boolean
        val patientHighLowAlertsSupported: Boolean
        val hypoAlertsSupported: Boolean
        val hyperAlertsSupported: Boolean
        val rateOfIncreaseDecreaseAlertsSupported: Boolean
        val deviceSpecificAlertSupported: Boolean
        val sensorMalfunctionDetectionSupported: Boolean
        val sensorTempHighLowDetectionSupported: Boolean
        val sensorResultHighLowSupported: Boolean
        val lowBatteryDetectionSupported: Boolean
        val sensorTypeErrorDetectionSupported: Boolean
        val generalDeviceFaultSupported: Boolean
        val e2eCrcSupported: Boolean
        val multipleBondSupported: Boolean
        val multipleSessionsSupported: Boolean
        val cgmTrendInfoSupported: Boolean
        val cgmQualityInfoSupported: Boolean

        init {

            calibrationSupported = value and 0x000001 != 0
            patientHighLowAlertsSupported = value and 0x000002 != 0
            hypoAlertsSupported = value and 0x000004 != 0
            hyperAlertsSupported = value and 0x000008 != 0
            rateOfIncreaseDecreaseAlertsSupported = value and 0x000010 != 0
            deviceSpecificAlertSupported = value and 0x000020 != 0
            sensorMalfunctionDetectionSupported = value and 0x000040 != 0
            sensorTempHighLowDetectionSupported = value and 0x000080 != 0
            sensorResultHighLowSupported = value and 0x000100 != 0
            lowBatteryDetectionSupported = value and 0x000200 != 0
            sensorTypeErrorDetectionSupported = value and 0x000400 != 0
            generalDeviceFaultSupported = value and 0x000800 != 0
            e2eCrcSupported = value and 0x001000 != 0
            multipleBondSupported = value and 0x002000 != 0
            multipleSessionsSupported = value and 0x004000 != 0
            cgmTrendInfoSupported = value and 0x008000 != 0
            cgmQualityInfoSupported = value and 0x010000 != 0
        }
    }

    class CGMCalibrationStatus(val value: Int) {
        val rejected: Boolean
        val dataOutOfRange: Boolean
        val processPending: Boolean

        init {

            rejected = value and 0x01 != 0
            dataOutOfRange = value and 0x02 != 0
            processPending = value and 0x04 != 0
        }
    }

    class CGMStatus(val warningStatus: Int, val calibrationTempStatus: Int, val sensorStatus: Int) {
        val sessionStopped: Boolean
        val deviceBatteryLow: Boolean
        val sensorTypeIncorrectForDevice: Boolean
        val sensorMalfunction: Boolean
        val deviceSpecificAlert: Boolean
        val generalDeviceFault: Boolean
        val timeSyncRequired: Boolean
        val calibrationNotAllowed: Boolean
        val calibrationRecommended: Boolean
        val calibrationRequired: Boolean
        val sensorTemperatureTooHigh: Boolean
        val sensorTemperatureTooLow: Boolean
        val sensorResultLowerThenPatientLowLevel: Boolean
        val sensorResultHigherThenPatientHighLevel: Boolean
        val sensorResultLowerThenHypoLevel: Boolean
        val sensorResultHigherThenHyperLevel: Boolean
        val sensorRateOfDecreaseExceeded: Boolean
        val sensorRateOfIncreaseExceeded: Boolean
        val sensorResultLowerThenDeviceCanProcess: Boolean
        val sensorResultHigherThenDeviceCanProcess: Boolean

        init {

            sessionStopped = warningStatus and 0x01 != 0
            deviceBatteryLow = warningStatus and 0x02 != 0
            sensorTypeIncorrectForDevice = warningStatus and 0x04 != 0
            sensorMalfunction = warningStatus and 0x08 != 0
            deviceSpecificAlert = warningStatus and 0x10 != 0
            generalDeviceFault = warningStatus and 0x20 != 0

            timeSyncRequired = calibrationTempStatus and 0x01 != 0
            calibrationNotAllowed = calibrationTempStatus and 0x02 != 0
            calibrationRecommended = calibrationTempStatus and 0x04 != 0
            calibrationRequired = calibrationTempStatus and 0x08 != 0
            sensorTemperatureTooHigh = calibrationTempStatus and 0x10 != 0
            sensorTemperatureTooLow = calibrationTempStatus and 0x20 != 0

            sensorResultLowerThenPatientLowLevel = sensorStatus and 0x01 != 0
            sensorResultHigherThenPatientHighLevel = sensorStatus and 0x02 != 0
            sensorResultLowerThenHypoLevel = sensorStatus and 0x04 != 0
            sensorResultHigherThenHyperLevel = sensorStatus and 0x08 != 0
            sensorRateOfDecreaseExceeded = sensorStatus and 0x10 != 0
            sensorRateOfIncreaseExceeded = sensorStatus and 0x20 != 0
            sensorResultLowerThenDeviceCanProcess = sensorStatus and 0x40 != 0
            sensorResultHigherThenDeviceCanProcess = sensorStatus and 0x80 != 0
        }
    }
}
