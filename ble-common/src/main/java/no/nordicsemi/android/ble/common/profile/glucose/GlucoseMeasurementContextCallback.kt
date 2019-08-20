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

package no.nordicsemi.android.ble.common.profile.glucose

import android.bluetooth.BluetoothDevice

import androidx.annotation.FloatRange
import androidx.annotation.IntRange

interface GlucoseMeasurementContextCallback {

    /**
     * Callback called when Glucose Measurement Context value was received.
     *
     * @param device             the target device.
     * @param sequenceNumber     the sequence number that matches the Glucose Measurement
     * sequence number.
     * @param carbohydrate       an optional carbohydrate ID.
     * @param carbohydrateAmount amount of carbohydrate in grams.
     * @param meal               an optional meal ID.
     * @param tester             an optional tester ID.
     * @param health             an optional health information.
     * @param exerciseDuration   exercise duration in seconds. Value 65535 means an overrun.
     * @param exerciseIntensity  exercise intensity in percent.
     * @param medication         an optional medication ID.
     * @param medicationAmount   amount of medication in milligrams or milliliters,
     * depending on the medicationUnit value.
     * @param medicationUnit     the unit of medication amount ([.UNIT_mg] or [.UNIT_ml]).
     * @param HbA1c              the amount of glycated haemoglobin, in percentage.
     */
    fun onGlucoseMeasurementContextReceived(
        device: BluetoothDevice,
        @IntRange(from = 0, to = 65535) sequenceNumber: Int,
        carbohydrate: Carbohydrate?,
        carbohydrateAmount: Float?,
        meal: Meal?,
        tester: Tester?,
        health: Health?,
        @IntRange(from = 0, to = 65535) exerciseDuration: Int?,
        @IntRange(from = 0, to = 100) exerciseIntensity: Int?,
        medication: Medication?,
        medicationAmount: Float?,
        @MedicationUnit medicationUnit: Int?,
        @FloatRange(from = 0.0, to = 100.0) HbA1c: Float?
    )

    enum class Carbohydrate(code: Int) {
        RESERVED(0),
        BREAKFAST(1),
        LUNCH(2),
        DINNER(3),
        SNACK(4),
        DRINK(5),
        SUPPER(6),
        BRUNCH(7);

        val value: Byte

        init {
            this.value = code.toByte()
        }

        companion object {

            fun from(code: Int): Carbohydrate {
                when (code) {
                    1 -> return BREAKFAST
                    2 -> return LUNCH
                    3 -> return DINNER
                    4 -> return SNACK
                    5 -> return DRINK
                    6 -> return SUPPER
                    7 -> return BRUNCH
                    else -> return RESERVED
                }
            }
        }
    }

    enum class Meal(code: Int) {
        RESERVED(0),
        PREPRANDIAL(1),
        POSTPRANDIAL(2),
        FASTING(3),
        CASUAL(4),
        BEDTIME(5);

        val value: Byte

        init {
            this.value = code.toByte()
        }

        companion object {

            fun from(code: Int): Meal {
                when (code) {
                    1 -> return PREPRANDIAL
                    2 -> return POSTPRANDIAL
                    3 -> return FASTING
                    4 -> return CASUAL
                    5 -> return BEDTIME
                    else -> return RESERVED
                }
            }
        }
    }

    enum class Tester(code: Int) {
        RESERVED(0),
        SELF(1),
        HEALTH_CARE_PROFESSIONAL(2),
        LAB_TEST(3),
        NOT_AVAILABLE(15);

        val value: Byte

        init {
            this.value = code.toByte()
        }

        companion object {

            fun from(code: Int): Tester {
                when (code) {
                    1 -> return SELF
                    2 -> return HEALTH_CARE_PROFESSIONAL
                    3 -> return LAB_TEST
                    15 -> return NOT_AVAILABLE
                    else -> return RESERVED
                }
            }
        }
    }

    enum class Health(code: Int) {
        RESERVED(0),
        MINOR_HEALTH_ISSUES(1),
        MAJOR_HEALTH_ISSUES(2),
        DURING_MENSES(3),
        UNDER_STRESS(4),
        NO_HEALTH_ISSUES(5),
        NOT_AVAILABLE(15);

        val value: Byte

        init {
            this.value = code.toByte()
        }

        companion object {

            fun from(code: Int): Health {
                when (code) {
                    1 -> return MINOR_HEALTH_ISSUES
                    2 -> return MAJOR_HEALTH_ISSUES
                    3 -> return DURING_MENSES
                    4 -> return UNDER_STRESS
                    5 -> return NO_HEALTH_ISSUES
                    15 -> return NOT_AVAILABLE
                    else -> return RESERVED
                }
            }
        }
    }

    enum class Medication(code: Int) {
        RESERVED(0),
        RAPID_ACTING_INSULIN(1),
        SHORT_ACTING_INSULIN(2),
        INTERMEDIATE_ACTING_INSULIN(3),
        LONG_ACTING_INSULIN(4),
        PRE_MIXED_INSULIN(5);

        val value: Byte

        init {
            this.value = code.toByte()
        }

        companion object {

            fun from(code: Int): Medication {
                when (code) {
                    1 -> return RAPID_ACTING_INSULIN
                    2 -> return SHORT_ACTING_INSULIN
                    3 -> return INTERMEDIATE_ACTING_INSULIN
                    4 -> return LONG_ACTING_INSULIN
                    5 -> return PRE_MIXED_INSULIN
                    else -> return RESERVED
                }
            }
        }
    }

    companion object {
        const val UNIT_mg = 0
        const val UNIT_ml = 1
    }
}
