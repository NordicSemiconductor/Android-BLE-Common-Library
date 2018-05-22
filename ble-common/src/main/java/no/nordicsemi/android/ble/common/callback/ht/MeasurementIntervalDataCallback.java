package no.nordicsemi.android.ble.common.callback.ht;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;

import no.nordicsemi.android.ble.callback.profile.ProfileReadResponse;
import no.nordicsemi.android.ble.common.profile.ht.MeasurementIntervalCallback;
import no.nordicsemi.android.ble.data.Data;

/**
 * Data callback that parses value into Measurement Interval data.
 * If the value received do not match required syntax
 * {@link #onInvalidDataReceived(BluetoothDevice, Data)} callback will be called.
 * will be called.
 * See: https://www.bluetooth.com/specifications/gatt/viewer?attributeXmlFile=org.bluetooth.characteristic.measurement_interval.xml
 */
public abstract class MeasurementIntervalDataCallback extends ProfileReadResponse implements MeasurementIntervalCallback {

	@Override
	public void onDataReceived(@NonNull final BluetoothDevice device, @NonNull final Data data) {
		super.onDataReceived(device, data);

		if (data.size() != 2) {
			onInvalidDataReceived(device, data);
			return;
		}

		final int interval = data.getIntValue(Data.FORMAT_UINT16, 0);
		onMeasurementIntervalReceived(interval);
	}
}
