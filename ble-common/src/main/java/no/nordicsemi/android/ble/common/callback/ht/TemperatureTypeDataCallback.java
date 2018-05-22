package no.nordicsemi.android.ble.common.callback.ht;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;

import no.nordicsemi.android.ble.callback.profile.ProfileReadResponse;
import no.nordicsemi.android.ble.common.profile.ht.TemperatureTypeCallback;
import no.nordicsemi.android.ble.data.Data;

/**
 * Data callback that parses value into Temperature Type data.
 * If the value received do not match required syntax
 * {@link #onInvalidDataReceived(BluetoothDevice, Data)} callback will be called.
 * will be called.
 * See: https://www.bluetooth.com/specifications/gatt/viewer?attributeXmlFile=org.bluetooth.characteristic.temperature_type.xml
 */
public abstract class TemperatureTypeDataCallback extends ProfileReadResponse implements TemperatureTypeCallback {

	@Override
	public void onDataReceived(@NonNull final BluetoothDevice device, @NonNull final Data data) {
		super.onDataReceived(device, data);

		if (data.size() != 1) {
			onInvalidDataReceived(device, data);
			return;
		}

		final int type = data.getIntValue(Data.FORMAT_UINT8, 0);
		onTemperatureTypeReceived(type);
	}
}
