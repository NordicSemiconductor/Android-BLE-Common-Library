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

package no.nordicsemi.android.ble.common.callback.cgm;

import android.bluetooth.BluetoothDevice;

import androidx.annotation.NonNull;

import org.junit.Test;

import no.nordicsemi.android.ble.callback.DataReceivedCallback;
import no.nordicsemi.android.ble.data.Data;
import no.nordicsemi.android.ble.data.MutableData;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@SuppressWarnings("ConstantConditions")
public class CGMStatusDataCallbackTest {
    private boolean called = false;

    @Test
    public void onContinuousGlucoseMonitorStatusChanged_withCrc() {
        final DataReceivedCallback callback = new CGMStatusDataCallback() {
            @Override
            public void onContinuousGlucoseMonitorStatusChanged(@NonNull final BluetoothDevice device, @NonNull final CGMStatus status,
                                                                final int timeOffset, final boolean secured) {
                assertNotNull("Status present", status);
                assertTrue(status.getSessionStopped());
                assertTrue(status.getDeviceBatteryLow());
                assertTrue(status.getSensorTypeIncorrectForDevice());
                assertTrue(status.getSensorMalfunction());
                assertTrue(status.getDeviceSpecificAlert());
                assertTrue(status.getGeneralDeviceFault());
                assertTrue(status.getTimeSyncRequired());
                assertTrue(status.getCalibrationNotAllowed());
                assertTrue(status.getCalibrationRecommended());
                assertTrue(status.getCalibrationRequired());
                assertTrue(status.getSensorTemperatureTooHigh());
                assertTrue(status.getSensorTemperatureTooLow());
                assertTrue(status.getSensorResultLowerThenPatientLowLevel());
                assertTrue(status.getSensorResultHigherThenPatientHighLevel());
                assertTrue(status.getSensorResultLowerThenHypoLevel());
                assertTrue(status.getSensorResultHigherThenHyperLevel());
                assertTrue(status.getSensorRateOfDecreaseExceeded());
                assertTrue(status.getSensorRateOfIncreaseExceeded());
                assertTrue(status.getSensorResultLowerThenDeviceCanProcess());
                assertTrue(status.getSensorResultHigherThenDeviceCanProcess());
                assertEquals("Time offset", 5, timeOffset);
                assertTrue(secured);
            }

            @Override
            public void onContinuousGlucoseMonitorStatusReceivedWithCrcError(@NonNull final BluetoothDevice device, @NonNull final Data data) {
                assertEquals("Correct data reported as CRC error", 1, 2);
            }

            @Override
            public void onInvalidDataReceived(@NonNull final BluetoothDevice device, @NonNull final Data data) {
                assertEquals("Correct data reported as invalid", 1, 2);
            }
        };
        final MutableData data = new MutableData(new byte[7]);
        data.setValue(5, Data.FORMAT_UINT16, 0);
        data.setValue(0xff3f3f, Data.FORMAT_UINT24, 2); // all flags set
        data.setValue(0xE0A7, Data.FORMAT_UINT16, 5);
        callback.onDataReceived(null, data);
    }

    @Test
    public void onContinuousGlucoseMonitorStatusChanged_noCrc() {
        final DataReceivedCallback callback = new CGMStatusDataCallback() {
            @Override
            public void onContinuousGlucoseMonitorStatusChanged(@NonNull final BluetoothDevice device, @NonNull final CGMStatus status,
                                                                final int timeOffset, final boolean secured) {
                assertNotNull("Status present", status);
                assertTrue(status.getSessionStopped());
                assertFalse(status.getDeviceBatteryLow());
                assertFalse(status.getSensorTypeIncorrectForDevice());
                assertFalse(status.getSensorMalfunction());
                assertFalse(status.getDeviceSpecificAlert());
                assertFalse(status.getGeneralDeviceFault());
                assertTrue(status.getTimeSyncRequired());
                assertFalse(status.getCalibrationNotAllowed());
                assertFalse(status.getCalibrationRecommended());
                assertFalse(status.getCalibrationRequired());
                assertFalse(status.getSensorTemperatureTooHigh());
                assertFalse(status.getSensorTemperatureTooLow());
                assertTrue(status.getSensorResultLowerThenPatientLowLevel());
                assertFalse(status.getSensorResultHigherThenPatientHighLevel());
                assertFalse(status.getSensorResultLowerThenHypoLevel());
                assertFalse(status.getSensorResultHigherThenHyperLevel());
                assertFalse(status.getSensorRateOfDecreaseExceeded());
                assertFalse(status.getSensorRateOfIncreaseExceeded());
                assertFalse(status.getSensorResultLowerThenDeviceCanProcess());
                assertFalse(status.getSensorResultHigherThenDeviceCanProcess());
                assertEquals("Time offset", 6, timeOffset);
                assertFalse(secured);
            }

            @Override
            public void onContinuousGlucoseMonitorStatusReceivedWithCrcError(@NonNull final BluetoothDevice device, @NonNull final Data data) {
                assertEquals("Correct data reported as CRC error", 1, 2);
            }

            @Override
            public void onInvalidDataReceived(@NonNull final BluetoothDevice device, @NonNull final Data data) {
                assertEquals("Correct data reported as invalid", 1, 2);
            }
        };
        final MutableData data = new MutableData(new byte[5]);
        data.setValue(6, Data.FORMAT_UINT16, 0);
        data.setValue(0x010101, Data.FORMAT_UINT24, 2);
        callback.onDataReceived(null, data);
    }

    @Test
    public void onContinuousGlucoseMonitorStatusReceivedWithCrcError() {
        final DataReceivedCallback callback = new CGMStatusDataCallback() {
            @Override
            public void onContinuousGlucoseMonitorStatusChanged(@NonNull final BluetoothDevice device, @NonNull final CGMStatus status,
                                                                final int timeOffset, final boolean secured) {
                assertEquals("Invalid CRC reported as valid packet", 1, 2);
            }

            @Override
            public void onContinuousGlucoseMonitorStatusReceivedWithCrcError(@NonNull final BluetoothDevice device, @NonNull final Data data) {
                called = true;
            }

            @Override
            public void onInvalidDataReceived(@NonNull final BluetoothDevice device, @NonNull final Data data) {
                assertEquals("Correct data reported as invalid", 1, 2);
            }
        };
        final MutableData data = new MutableData(new byte[7]);
        data.setValue(6, Data.FORMAT_UINT16, 0);
        data.setValue(0x010101, Data.FORMAT_UINT24, 2);
        data.setValue(0xE0A7, Data.FORMAT_UINT16, 5);
        called = false;
        callback.onDataReceived(null, data);
        assertTrue(called);
    }

    @Test
    public void onInvalidDataReceived() {
        final DataReceivedCallback callback = new CGMStatusDataCallback() {
            @Override
            public void onContinuousGlucoseMonitorStatusChanged(@NonNull final BluetoothDevice device, @NonNull final CGMStatus status,
                                                                final int timeOffset, final boolean secured) {
                assertEquals("Invalid data reported as valid packet", 1, 2);
            }

            @Override
            public void onContinuousGlucoseMonitorStatusReceivedWithCrcError(@NonNull final BluetoothDevice device, @NonNull final Data data) {
                assertEquals("Invalid data reported as wrong CRC", 1, 2);
            }

            @Override
            public void onInvalidDataReceived(@NonNull final BluetoothDevice device, @NonNull final Data data) {
                called = true;
            }
        };
        final MutableData data = new MutableData(new byte[6]);
        data.setValue(6, Data.FORMAT_UINT16, 0);
        data.setValue(0x010101, Data.FORMAT_UINT24, 2);
        data.setValue(1, Data.FORMAT_UINT8, 5);
        called = false;
        callback.onDataReceived(null, data);
        assertTrue(called);
    }
}