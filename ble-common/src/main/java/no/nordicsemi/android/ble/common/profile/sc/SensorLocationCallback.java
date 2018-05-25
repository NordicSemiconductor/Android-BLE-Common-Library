package no.nordicsemi.android.ble.common.profile.sc;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;

@SuppressWarnings("unused")
public interface SensorLocationCallback extends SensorLocationTypes {

	/**
	 * Callback called when Sensor Location characteristic was read.
	 *
	 * @param device   the target device.
	 * @param location the sensor location, one of SENSOR_LOCATION_* constants.
	 */
	void onSensorLocationReceived(@NonNull final BluetoothDevice device, final int location);
}
