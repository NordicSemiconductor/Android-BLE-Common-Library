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

package no.nordicsemi.android.ble.common.profile.ht

import android.bluetooth.BluetoothDevice
import java.util.*

interface TemperatureMeasurementCallback : HealthThermometerTypes {

    /**
     * Callback called when Temperature Measurement or Intermediate Temperature
     * characteristic has changed its value.
     *
     *
     * Use [.toCelsius] or [.toFahrenheit] to convert units.
     *
     * @param device      the target device.
     * @param temperature the temperature received in given unit
     * @param unit        the temperature unit ([.UNIT_C] or [.UNIT_F]).
     * @param calendar    an optional timestamp of measurement.
     * @param type        an optional type where the temperature was measured, see TYPE_* constants.
     */
    fun onTemperatureMeasurementReceived(
        device: BluetoothDevice,
        temperature: Float,
        @TemperatureUnit unit: Int,
        calendar: Calendar?,
        @TemperatureType type: Int?
    )

    companion object {
        const val UNIT_C = 0
        const val UNIT_F = 1

        /**
         * Converts the value provided in given unit to Celsius degrees.
         * If the unit is already [.UNIT_C] it will be returned as is.
         *
         * @param temperature the temperature value in given unit.
         * @param unit        the unit of the value ([.UNIT_C] or [.UNIT_F]).
         * @return Value in C.
         */
        fun toCelsius(temperature: Float, @TemperatureUnit unit: Int): Float {
            return if (unit == UNIT_C) {
                temperature
            } else {
                (temperature - 32f) / 1.8f
            }
        }

        /**
         * Converts the value provided in given unit to Fahrenheit degrees.
         * If the unit is already [.UNIT_F] it will be returned as is.
         *
         * @param temperature the temperature value in given unit.
         * @param unit        the unit of the value ([.UNIT_C] or [.UNIT_F]).
         * @return Value in F.
         */
        fun toFahrenheit(temperature: Float, @TemperatureUnit unit: Int): Float {
            return if (unit == UNIT_F) {
                temperature
            } else {
                temperature * 1.8f + 32f
            }
        }
    }
}
