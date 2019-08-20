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

package no.nordicsemi.android.ble.common.profile.sc

interface SensorLocationTypes {
    companion object {
        const val SENSOR_LOCATION_OTHER = 0
        const val SENSOR_LOCATION_TOP_OF_SHOE = 1
        const val SENSOR_LOCATION_IN_SHOE = 2
        const val SENSOR_LOCATION_HIP = 3
        const val SENSOR_LOCATION_FRONT_WHEEL = 4
        const val SENSOR_LOCATION_LEFT_CRANK = 5
        const val SENSOR_LOCATION_RIGHT_CRANK = 6
        const val SENSOR_LOCATION_LEFT_PEDAL = 7
        const val SENSOR_LOCATION_RIGHT_PEDAL = 8
        const val SENSOR_LOCATION_FRONT_HUB = 9
        const val SENSOR_LOCATION_REAR_DROPOUT = 10
        const val SENSOR_LOCATION_CHAINSTAY = 11
        const val SENSOR_LOCATION_REAR_WHEEL = 12
        const val SENSOR_LOCATION_REAR_HUB = 13
        const val SENSOR_LOCATION_CHEST = 14
        const val SENSOR_LOCATION_SPIDER = 15
        const val SENSOR_LOCATION_CHAIN_RING = 16
    }
}
