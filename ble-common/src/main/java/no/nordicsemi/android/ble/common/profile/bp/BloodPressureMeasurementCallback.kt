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

import android.bluetooth.BluetoothDevice
import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import java.util.*

interface BloodPressureMeasurementCallback : BloodPressureTypes {

    /**
     * Callback called when Blood Pressure Measurement packet has been received.
     * Use [.toKPa] or [.toMmHg] to convert pressure units.
     *
     * @param device               the target device.
     * @param systolic             the systolic compound of blood pressure measurement.
     * @param diastolic            the diastolic compound of blood pressure measurement.
     * @param meanArterialPressure the mean arterial pressure compound of blood pressure measurement.
     * @param unit                 the measurement unit, one of [.UNIT_mmHg] or [.UNIT_kPa].
     * @param pulseRate            an optional pulse rate in beats per minute.
     * @param userID               an optional user ID. Value 255 means 'unknown user'.
     * @param status               an optional measurement status.
     * @param calendar             an optional measurement timestamp.
     */
    fun onBloodPressureMeasurementReceived(
        device: BluetoothDevice,
        @FloatRange(from = 0.0) systolic: Float,
        @FloatRange(from = 0.0) diastolic: Float,
        @FloatRange(from = 0.0) meanArterialPressure: Float,
        @BloodPressureUnit unit: Int,
        @FloatRange(from = 0.0) pulseRate: Float?,
        @IntRange(from = 0, to = 255) userID: Int?,
        status: BloodPressureTypes.BPMStatus?,
        calendar: Calendar?
    )
}
