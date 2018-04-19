package no.nordicsemi.android.ble.common.callback;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;

import org.junit.Test;

import no.nordicsemi.android.ble.callback.DataCallback;
import no.nordicsemi.android.ble.data.Data;

import static org.junit.Assert.assertEquals;

@SuppressWarnings("ConstantConditions")
public class BatteryLevelDataCallbackTest {

	@Test
	public void onBatteryLevelChanged_fullBattery() {
		final DataCallback callback = new BatteryLevelDataCallback() {
			@Override
			public void onBatteryLevelChanged(@NonNull final BluetoothDevice device, final int batteryLevel) {
				assertEquals("Correct data", batteryLevel, 100);
			}

			@Override
			public void onInvalidDataReceived(@NonNull final BluetoothDevice device, @NonNull final Data data) {
				assertEquals("Correct data reported as invalid", 1, 2);
			}
		};
		final Data data = new Data(new byte[] { 0x64 });
		callback.onDataReceived(null, data);
	}

	@Test
	public void onBatteryLevelChanged_lowBattery() {
		final DataCallback callback = new BatteryLevelDataCallback() {
			@Override
			public void onBatteryLevelChanged(@NonNull final BluetoothDevice device, final int batteryLevel) {
				assertEquals("Correct data", batteryLevel, 15);
			}

			@Override
			public void onInvalidDataReceived(@NonNull final BluetoothDevice device, @NonNull final Data data) {
				assertEquals("Correct data reported as invalid", 1, 2);
			}
		};
		final Data data = new Data(new byte[] { 0x0F });
		callback.onDataReceived(null, data);
	}

	@Test
	public void onInvalidDataReceived_dataTooLong() {
		final DataCallback callback = new BatteryLevelDataCallback() {
			@Override
			public void onBatteryLevelChanged(@NonNull final BluetoothDevice device, final int batteryLevel) {
				assertEquals("Invalid date returned Battery Level", 1, 2);
			}

			@Override
			public void onInvalidDataReceived(@NonNull final BluetoothDevice device, @NonNull final Data data) {
				assertEquals("Invalid data", data.size(), 2);
			}
		};
		final Data data = new Data(new byte[] { 0x64, 0x00 });
		callback.onDataReceived(null, data);
	}

	@Test
	public void onInvalidDataReceived_batteryLevelOutOfRange() {
		final DataCallback callback = new BatteryLevelDataCallback() {
			@Override
			public void onBatteryLevelChanged(@NonNull final BluetoothDevice device, final int batteryLevel) {
				assertEquals("Invalid date returned Battery Level", 1, 2);
			}

			@Override
			public void onInvalidDataReceived(@NonNull final BluetoothDevice device, @NonNull final Data data) {
				assertEquals("Invalid data", data.size(), 1);
			}
		};
		final Data data = new Data(new byte[] { 0x65 });
		callback.onDataReceived(null, data);
	}
}