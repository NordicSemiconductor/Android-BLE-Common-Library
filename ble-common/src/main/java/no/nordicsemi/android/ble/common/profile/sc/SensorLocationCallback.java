package no.nordicsemi.android.ble.common.profile.sc;

@SuppressWarnings("unused")
public interface SensorLocationCallback extends SensorLocationTypes {

	/**
	 * Callback called when Sensor Location characteristic was read.
	 *
	 * @param location the sensor location, one of SENSOR_LOCATION_* constants.
	 */
	void onSensorLocationReceived(final int location);
}
