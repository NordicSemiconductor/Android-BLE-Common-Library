package no.nordicsemi.android.ble.common.callback.battery;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;

import no.nordicsemi.android.ble.callback.profile.ProfileReadResponse;
import no.nordicsemi.android.ble.common.profile.battery.BatteryLevelCallback;
import no.nordicsemi.android.ble.data.Data;

/**
 * Data callback that parses 1-byte value into Battery Level value.
 * The data must be in range 0-100 inclusive (in percent), otherwise
 * {@link #onInvalidDataReceived(BluetoothDevice, Data)} is called.
 * See: https://www.bluetooth.com/specifications/gatt/viewer?attributeXmlFile=org.bluetooth.characteristic.battery_level.xml
 */
@SuppressWarnings("WeakerAccess")
public abstract class BatteryLevelDataCallback extends ProfileReadResponse implements BatteryLevelCallback {

	@SuppressWarnings("ConstantConditions")
	@Override
	public void onDataReceived(@NonNull final BluetoothDevice device, final @NonNull Data data) {
		super.onDataReceived(device, data);

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
