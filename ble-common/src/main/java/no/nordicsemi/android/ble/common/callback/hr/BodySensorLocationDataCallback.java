package no.nordicsemi.android.ble.common.callback.hr;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.support.annotation.NonNull;

import no.nordicsemi.android.ble.callback.profile.ProfileReadResponse;
import no.nordicsemi.android.ble.common.profile.hr.BodySensorLocationCallback;
import no.nordicsemi.android.ble.data.Data;

/**
 * Data callback that parses value into Body Sensor Location data.
 * If the value received do not match required syntax
 * {@link #onInvalidDataReceived(BluetoothDevice, Data)} callback will be called.
 * will be called.
 * See: https://www.bluetooth.com/specifications/gatt/viewer?attributeXmlFile=org.bluetooth.characteristic.body_sensor_location.xml
 */
@SuppressWarnings({"WeakerAccess", "ConstantConditions"})
public abstract class BodySensorLocationDataCallback extends ProfileReadResponse implements BodySensorLocationCallback {

	public BodySensorLocationDataCallback() {
		// empty
	}

	protected BodySensorLocationDataCallback(final Parcel in) {
		super(in);
	}

	@Override
	public void onDataReceived(@NonNull final BluetoothDevice device, @NonNull final Data data) {
		super.onDataReceived(device, data);

		if (data.size() < 1) {
			onInvalidDataReceived(device, data);
			return;
		}

		final int sensorLocation = data.getIntValue(Data.FORMAT_UINT8, 0);
		onBodySensorLocationReceived(device, sensorLocation);
	}
}
