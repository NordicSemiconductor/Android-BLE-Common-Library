package no.nordicsemi.android.ble.common.callback;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;

import no.nordicsemi.android.ble.common.profile.BatteryLevelCallback;
import no.nordicsemi.android.ble.data.Data;
import no.nordicsemi.android.ble.callback.profile.ProfileDataCallback;

/**
 * Data callback that parses 1-byte value into Battery Level value.
 * The data must be in range 0-100 inclusive, in percent.
 * See: https://www.bluetooth.com/specifications/gatt/viewer?attributeXmlFile=org.bluetooth.characteristic.battery_level.xml
 */
@SuppressWarnings("WeakerAccess")
public abstract class BatteryLevelDataCallback implements ProfileDataCallback, BatteryLevelCallback {

	@SuppressWarnings("ConstantConditions")
	@Override
	public void onDataReceived(@NonNull final BluetoothDevice device, final @NonNull Data data) {
		if (data.size() == 1) {
			final int batteryLevel = data.getIntValue(Data.FORMAT_UINT8, 0);
			if (batteryLevel >= 0 && batteryLevel <= 100) {
				onBatteryLevelChanged(device, batteryLevel);
				return;
			}
		}
		onInvalidDataReceived(device, data);
	}
}
