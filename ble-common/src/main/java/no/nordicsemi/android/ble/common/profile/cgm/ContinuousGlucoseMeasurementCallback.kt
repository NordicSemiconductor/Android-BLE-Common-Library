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

import android.bluetooth.BluetoothDevice

import androidx.annotation.FloatRange
import androidx.annotation.IntRange

import no.nordicsemi.android.ble.data.Data

interface ContinuousGlucoseMeasurementCallback : CGMTypes {

    /**
     * Callback called when a Continuous Glucose Measurement packet has been received.
     *
     *
     * If the E2E CRC field was present in the CGM packet, the data has been verified against it.
     * If CRC check has failed, the [.onContinuousGlucoseMeasurementReceivedWithCrcError]
     * will be called instead.
     *
     *
     * The Glucose concentration is reported in mg/dL. To convert it to mmol/L use:
     * <pre>value [mg/dL] = 18.02 * value [mmol/L]</pre>
     * or simply call [.toMgPerDecilitre].
     *
     *
     * Note that the conversion factor is compliant to the Continua blood glucose meter specification.
     *
     * @param device               the target device.
     * @param glucoseConcentration the glucose concentration in mg/dL.
     * @param cgmTrend             an optional CGM Trend information, in (mg/dL)/min.
     * @param cgmQuality           an optional CGM Quality information in percent.
     * @param status               the status of the measurement.
     * @param timeOffset           the time offset in minutes since Session Start Time.
     * @param secured              true if the packet was sent with E2E-CRC value that was verified
     * to match the packet, false if the packet didn't contain CRC field.
     * For a callback in case of invalid CRC value check
     * [.onContinuousGlucoseMeasurementReceivedWithCrcError].
     */
    fun onContinuousGlucoseMeasurementReceived(
        device: BluetoothDevice,
        @FloatRange(from = 0.0) glucoseConcentration: Float,
        cgmTrend: Float?,
        cgmQuality: Float?,
        status: CGMTypes.CGMStatus?,
        @IntRange(from = 0) timeOffset: Int,
        secured: Boolean
    )

    /**
     * Callback called when a CGM packet with E2E field was received but the CRC check has failed.
     *
     * @param device the target device.
     * @param data   the CGM packet data that was received, including the CRC field.
     */
    fun onContinuousGlucoseMeasurementReceivedWithCrcError(
        device: BluetoothDevice,
        data: Data
    ) {
        // ignore
    }

    companion object {

        /**
         * Converts the value from mmol/L to mg/dL.
         *
         * @param value the glucose concentration value in given unit.
         * @return Value in mg/dL.
         */
        fun toMgPerDecilitre(value: Float): Float {
            return value * 18.2f
        }
    }
}
