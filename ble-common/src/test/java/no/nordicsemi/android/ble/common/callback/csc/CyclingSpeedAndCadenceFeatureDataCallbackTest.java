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

package no.nordicsemi.android.ble.common.callback.csc;

import android.bluetooth.BluetoothDevice;

import androidx.annotation.NonNull;

import org.junit.Test;

import no.nordicsemi.android.ble.callback.profile.ProfileReadResponse;
import no.nordicsemi.android.ble.data.Data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@SuppressWarnings("ConstantConditions")
public class CyclingSpeedAndCadenceFeatureDataCallbackTest {
    private boolean called;

    @Test
    public void onCyclingSpeedAndCadenceFeaturesReceived() {
        final ProfileReadResponse callback = new CyclingSpeedAndCadenceFeatureDataCallback() {
            @Override
            public void onCyclingSpeedAndCadenceFeaturesReceived(@NonNull final BluetoothDevice device,
                                                                 @NonNull final CSCFeatures features) {
                called = true;
                assertNotNull(features);
                assertTrue("Wheel revolutions supported", features.getWheelRevolutionDataSupported());
                assertTrue("Crank revolutions supported", features.getCrankRevolutionDataSupported());
                assertFalse("Multiple sensors not supported", features.getMultipleSensorDataSupported());
                assertEquals("Feature value", 0x03, features.getValue());
            }
        };

        called = false;
        final Data data = new Data(new byte[]{0x03, 0x00});
        callback.onDataReceived(null, data);
        assertTrue(called);
        assertTrue(callback.isValid());
    }

    @Test
    public void onInvalidDataReceived() {
        final ProfileReadResponse callback = new CyclingSpeedAndCadenceFeatureDataCallback() {
            @Override
            public void onCyclingSpeedAndCadenceFeaturesReceived(@NonNull final BluetoothDevice device,
                                                                 @NonNull final CSCFeatures features) {
                called = true;
            }
        };

        called = false;
        final Data data = new Data(new byte[]{0x03});
        callback.onDataReceived(null, data);
        assertFalse(called);
        assertFalse(callback.isValid());
    }
}