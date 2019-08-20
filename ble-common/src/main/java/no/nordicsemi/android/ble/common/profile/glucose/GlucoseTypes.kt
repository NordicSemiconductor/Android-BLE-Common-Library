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

interface GlucoseTypes {
    companion object {
        const val TYPE_CAPILLARY_WHOLE_BLOOD = 1
        const val TYPE_CAPILLARY_PLASMA = 2
        const val TYPE_VENOUS_WHOLE_BLOOD = 3
        const val TYPE_VENOUS_PLASMA = 4
        const val TYPE_ARTERIAL_WHOLE_BLOOD = 5
        const val TYPE_ARTERIAL_PLASMA = 6
        const val TYPE_UNDETERMINED_WHOLE_BLOOD = 7
        const val TYPE_UNDETERMINED_PLASMA = 8
        const val TYPE_INTERSTITIAL_FLUID_ISF = 9
        const val TYPE_CONTROL_SOLUTION = 10

        const val SAMPLE_LOCATION_FINGER = 1
        const val SAMPLE_LOCATION_ALTERNATE_SITE_TEST = 2
        const val SAMPLE_LOCATION_EARLOBE = 3
        const val SAMPLE_LOCATION_CONTROL_SOLUTION = 4
        const val SAMPLE_LOCATION_SUBCUTANEOUS_TISSUE = 5 // used in CGM
        const val SAMPLE_LOCATION_VALUE_NOT_AVAILABLE = 15
    }
}
