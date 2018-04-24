package no.nordicsemi.android.ble.common.callback;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;

import org.junit.Test;

import no.nordicsemi.android.ble.callback.DataCallback;
import no.nordicsemi.android.ble.data.Data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@SuppressWarnings("ConstantConditions")
public class ContinuousGlucoseMonitorStatusDataCallbackTest {
	private boolean called = false;

	@Test
	public void onContinuousGlucoseMonitorStatusChanged_withCrc() {
		final DataCallback callback = new ContinuousGlucoseMonitorStatusDataCallback() {
			@Override
			public void onContinuousGlucoseMonitorStatusChanged(final CGMStatus status, final int timeOffset, final boolean secured) {
				assertNotNull("Status present", status);
				assertTrue(status.sessionStopped);
				assertTrue(status.deviceBatteryLow);
				assertTrue(status.sensorTypeIncorrectForDevice);
				assertTrue(status.sensorMalfunction);
				assertTrue(status.deviceSpecificAlert);
				assertTrue(status.generalDeviceFault);
				assertTrue(status.timeSyncRequired);
				assertTrue(status.calibrationNotAllowed);
				assertTrue(status.calibrationRecommended);
				assertTrue(status.calibrationRequired);
				assertTrue(status.sensorTemperatureTooHigh);
				assertTrue(status.sensorTemperatureTooLow);
				assertTrue(status.sensorResultLowerThenPatientLowLevel);
				assertTrue(status.sensorResultHigherThenPatientHighLevel);
				assertTrue(status.sensorResultLowerThenHypoLevel);
				assertTrue(status.sensorResultHigherThenHyperLevel);
				assertTrue(status.sensorRateOfDecreaseExceeded);
				assertTrue(status.sensorRateOfIncreaseExceeded);
				assertTrue(status.sensorResultLowerThenDeviceCanProcess);
				assertTrue(status.sensorResultHigherThenDeviceCanProcess);
				assertEquals("Time offset", 5, timeOffset);
				assertTrue(secured);
			}

			@Override
			public void onContinuousGlucoseMonitorStatusReceivedWithCrcError(@NonNull final Data data) {
				assertEquals("Correct data reported as CRC error", 1, 2);
			}

			@Override
			public void onInvalidDataReceived(@NonNull final BluetoothDevice device, @NonNull final Data data) {
				assertEquals("Correct data reported as invalid", 1, 2);
			}
		};
		final Data data = new Data(new byte[7]);
		data.setValue(5, Data.FORMAT_UINT16, 0);
		data.setValue(0xff3f3f, Data.FORMAT_UINT24, 2); // all flags set
		data.setValue(0xE0A7, Data.FORMAT_UINT16, 5);
		callback.onDataReceived(null, data);
	}

	@Test
	public void onContinuousGlucoseMonitorStatusChanged_noCrc() {
		final DataCallback callback = new ContinuousGlucoseMonitorStatusDataCallback() {
			@Override
			public void onContinuousGlucoseMonitorStatusChanged(final CGMStatus status, final int timeOffset, final boolean secured) {
				assertNotNull("Status present", status);
				assertTrue(status.sessionStopped);
				assertFalse(status.deviceBatteryLow);
				assertFalse(status.sensorTypeIncorrectForDevice);
				assertFalse(status.sensorMalfunction);
				assertFalse(status.deviceSpecificAlert);
				assertFalse(status.generalDeviceFault);
				assertTrue(status.timeSyncRequired);
				assertFalse(status.calibrationNotAllowed);
				assertFalse(status.calibrationRecommended);
				assertFalse(status.calibrationRequired);
				assertFalse(status.sensorTemperatureTooHigh);
				assertFalse(status.sensorTemperatureTooLow);
				assertTrue(status.sensorResultLowerThenPatientLowLevel);
				assertFalse(status.sensorResultHigherThenPatientHighLevel);
				assertFalse(status.sensorResultLowerThenHypoLevel);
				assertFalse(status.sensorResultHigherThenHyperLevel);
				assertFalse(status.sensorRateOfDecreaseExceeded);
				assertFalse(status.sensorRateOfIncreaseExceeded);
				assertFalse(status.sensorResultLowerThenDeviceCanProcess);
				assertFalse(status.sensorResultHigherThenDeviceCanProcess);
				assertEquals("Time offset", 6, timeOffset);
				assertFalse(secured);
			}

			@Override
			public void onContinuousGlucoseMonitorStatusReceivedWithCrcError(@NonNull final Data data) {
				assertEquals("Correct data reported as CRC error", 1, 2);
			}

			@Override
			public void onInvalidDataReceived(@NonNull final BluetoothDevice device, @NonNull final Data data) {
				assertEquals("Correct data reported as invalid", 1, 2);
			}
		};
		final Data data = new Data(new byte[5]);
		data.setValue(6, Data.FORMAT_UINT16, 0);
		data.setValue(0x010101, Data.FORMAT_UINT24, 2);
		callback.onDataReceived(null, data);
	}

	@Test
	public void onContinuousGlucoseMonitorStatusReceivedWithCrcError() {
		final DataCallback callback = new ContinuousGlucoseMonitorStatusDataCallback() {
			@Override
			public void onContinuousGlucoseMonitorStatusChanged(final CGMStatus status, final int timeOffset, final boolean secured) {
				assertEquals("Invalid CRC reported as valid packet", 1, 2);
			}

			@Override
			public void onContinuousGlucoseMonitorStatusReceivedWithCrcError(@NonNull final Data data) {
				called = true;
			}

			@Override
			public void onInvalidDataReceived(@NonNull final BluetoothDevice device, @NonNull final Data data) {
				assertEquals("Correct data reported as invalid", 1, 2);
			}
		};
		final Data data = new Data(new byte[7]);
		data.setValue(6, Data.FORMAT_UINT16, 0);
		data.setValue(0x010101, Data.FORMAT_UINT24, 2);
		data.setValue(0xE0A7, Data.FORMAT_UINT16, 5);
		called = false;
		callback.onDataReceived(null, data);
		assertTrue(called);
	}

	@Test
	public void onInvalidDataReceived() {
		final DataCallback callback = new ContinuousGlucoseMonitorStatusDataCallback() {
			@Override
			public void onContinuousGlucoseMonitorStatusChanged(final CGMStatus status, final int timeOffset, final boolean secured) {
				assertEquals("Invalid data reported as valid packet", 1, 2);
			}

			@Override
			public void onContinuousGlucoseMonitorStatusReceivedWithCrcError(@NonNull final Data data) {
				assertEquals("Invalid data reported as wrong CRC", 1, 2);
			}

			@Override
			public void onInvalidDataReceived(@NonNull final BluetoothDevice device, @NonNull final Data data) {
				called = true;
			}
		};
		final Data data = new Data(new byte[6]);
		data.setValue(6, Data.FORMAT_UINT16, 0);
		data.setValue(0x010101, Data.FORMAT_UINT24, 2);
		data.setValue(1, Data.FORMAT_UINT8, 5);
		called = false;
		callback.onDataReceived(null, data);
		assertTrue(called);
	}
}