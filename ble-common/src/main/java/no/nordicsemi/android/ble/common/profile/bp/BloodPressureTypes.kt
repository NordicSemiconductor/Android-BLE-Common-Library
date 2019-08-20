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

package no.nordicsemi.android.ble.common.profile.bp

interface BloodPressureTypes {

    class BPMStatus(val value: Int) {
        val bodyMovementDetected: Boolean
        val cuffTooLose: Boolean
        val irregularPulseDetected: Boolean
        val pulseRateInRange: Boolean
        val pulseRateExceedsUpperLimit: Boolean
        val pulseRateIsLessThenLowerLimit: Boolean
        val improperMeasurementPosition: Boolean

        init {

            bodyMovementDetected = value and 0x01 != 0
            cuffTooLose = value and 0x02 != 0
            irregularPulseDetected = value and 0x04 != 0
            pulseRateInRange = value and 0x18 shr 3 == 0
            pulseRateExceedsUpperLimit = value and 0x18 shr 3 == 1
            pulseRateIsLessThenLowerLimit = value and 0x18 shr 3 == 2
            improperMeasurementPosition = value and 0x20 != 0
        }
    }

    companion object {
        const val UNIT_mmHg = 0
        const val UNIT_kPa = 1

        /**
         * Converts the value provided in given unit to mmHg.
         * If the unit is already [.UNIT_mmHg] it will be returned as is.
         *
         * @param value the pressure value in given unit.
         * @param unit  the unit of the value ([.UNIT_mmHg] or [.UNIT_kPa]).
         * @return Value in mmHg.
         */
        fun toMmHg(value: Float, @BloodPressureUnit unit: Int): Float {
            return if (unit == UNIT_mmHg) {
                value
            } else {
                value / 0.133322387415f
            }
        }

        /**
         * Converts the value provided in given unit to kPa.
         * If the unit is already [.UNIT_kPa] it will be returned as is.
         *
         * @param value the pressure value in given unit.
         * @param unit  the unit of the value ([.UNIT_mmHg] or [.UNIT_kPa]).
         * @return Value in kPa.
         */
        fun toKPa(value: Float, @BloodPressureUnit unit: Int): Float {
            return if (unit == UNIT_kPa) {
                value
            } else {
                value * 0.133322387415f
            }
        }
    }
}
