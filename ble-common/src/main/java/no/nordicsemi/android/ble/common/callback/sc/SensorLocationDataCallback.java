package no.nordicsemi.android.ble.common.callback.sc;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.support.annotation.NonNull;

import no.nordicsemi.android.ble.callback.profile.ProfileReadResponse;
import no.nordicsemi.android.ble.common.profile.sc.SensorLocationCallback;
import no.nordicsemi.android.ble.data.Data;

/**
 * Data callback that parses value into Sensor Location data.
 * If the value received do not match required syntax
 * {@link #onInvalidDataReceived(BluetoothDevice, Data)} callback will be called.
 * will be called.
 * See: https://www.bluetooth.com/specifications/gatt/viewer?attributeXmlFile=org.bluetooth.characteristic.sensor_location.xml
 */
@SuppressWarnings("WeakerAccess")
public abstract class SensorLocationDataCallback extends ProfileReadResponse implements SensorLocationCallback {

	public SensorLocationDataCallback() {
		// empty
	}

	protected SensorLocationDataCallback(final Parcel in) {
		super(in);
	}

	@Override
	public void onDataReceived(@NonNull final BluetoothDevice device, @NonNull final Data data) {
		super.onDataReceived(device, data);

		if (data.size() != 1) {
			onInvalidDataReceived(device, data);
			return;
		}

		final int location = data.getIntValue(Data.FORMAT_UINT8, 0);
		onSensorLocationReceived(location);
	}
}
