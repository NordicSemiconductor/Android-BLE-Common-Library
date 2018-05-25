package no.nordicsemi.android.ble.common.profile.hr;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;

@SuppressWarnings("unused")
public interface BodySensorLocationCallback {
	int SENSOR_LOCATION_OTHER = 0;
	int SENSOR_LOCATION_CHEST = 1;
	int SENSOR_LOCATION_WRIST = 2;
	int SENSOR_LOCATION_FINGER = 3;
	int SENSOR_LOCATION_HAND = 4;
	int SENSOR_LOCATION_EAR_LOBE = 5;
	int SENSOR_LOCATION_FOOT = 6;
	int SENSOR_LOCATION_FIRST = SENSOR_LOCATION_OTHER;
	int SENSOR_LOCATION_LAST = SENSOR_LOCATION_FOOT;

	/**
	 * Callback received when Body Sensor Location characteristic has been read.
	 *
	 * @param device         the target device.
	 * @param sensorLocation the sensor location, see SENSOR_LOCATION_* constants.
	 */
	void onBodySensorLocationReceived(@NonNull final BluetoothDevice device,
									  final int sensorLocation);
}
