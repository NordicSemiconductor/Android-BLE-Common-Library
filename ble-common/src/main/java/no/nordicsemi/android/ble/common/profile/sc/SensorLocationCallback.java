package no.nordicsemi.android.ble.common.profile.sc;

@SuppressWarnings("unused")
public interface SensorLocationCallback {
	int SENSOR_LOCATION_OTHER = 0;
	int SENSOR_LOCATION_TOP_OF_SHOE = 1;
	int SENSOR_LOCATION_IN_SHOE = 2;
	int SENSOR_LOCATION_HIP = 3;
	int SENSOR_LOCATION_FRONT_WHEEL = 4;
	int SENSOR_LOCATION_LEFT_CRANK = 5;
	int SENSOR_LOCATION_RIGHT_CRANK = 6;
	int SENSOR_LOCATION_LEFT_PEDAL = 7;
	int SENSOR_LOCATION_RIGHT_PEDAL = 8;
	int SENSOR_LOCATION_FRONT_HUB = 9;
	int SENSOR_LOCATION_REAR_DROPOUT = 10;
	int SENSOR_LOCATION_CHAINSTAY = 11;
	int SENSOR_LOCATION_REAR_WHEEL = 12;
	int SENSOR_LOCATION_REAR_HUB = 13;
	int SENSOR_LOCATION_CHEST = 14;
	int SENSOR_LOCATION_SPIDER = 15;
	int SENSOR_LOCATION_CHAIN_RING = 16;

	/**
	 * Callback called when Sensor Location characteristic was read.
	 *
	 * @param location the sensor location, one of SENSOR_LOCATION_* constants.
	 */
	void onSensorLocationReceived(final int location);
}
