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

package no.nordicsemi.android.ble.common.profile.hr

import android.bluetooth.BluetoothDevice

interface BodySensorLocationCallback {

    /**
     * Callback received when Body Sensor Location characteristic has been read.
     *
     * @param device         the target device.
     * @param sensorLocation the sensor location, see SENSOR_LOCATION_* constants.
     */
    fun onBodySensorLocationReceived(
        device: BluetoothDevice,
        @BodySensorLocation sensorLocation: Int
    )

    companion object {
        const val SENSOR_LOCATION_OTHER = 0
        const val SENSOR_LOCATION_CHEST = 1
        const val SENSOR_LOCATION_WRIST = 2
        const val SENSOR_LOCATION_FINGER = 3
        const val SENSOR_LOCATION_HAND = 4
        const val SENSOR_LOCATION_EAR_LOBE = 5
        const val SENSOR_LOCATION_FOOT = 6
        const val SENSOR_LOCATION_FIRST = SENSOR_LOCATION_OTHER
        const val SENSOR_LOCATION_LAST = SENSOR_LOCATION_FOOT
    }
}
