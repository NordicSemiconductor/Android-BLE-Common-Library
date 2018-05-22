package no.nordicsemi.android.ble.common.data.sc;

import no.nordicsemi.android.ble.common.profile.sc.SensorLocationTypes;
import no.nordicsemi.android.ble.data.Data;
import no.nordicsemi.android.ble.data.MutableData;

@SuppressWarnings("unused")
public final class SpeedAndCadenceControlPointData implements SensorLocationTypes {
	private final static byte SC_OP_CODE_SET_CUMULATIVE_VALUE = 1;
	private final static byte SC_OP_CODE_START_SENSOR_CALIBRATION = 2;
	private final static byte SC_OP_CODE_UPDATE_SENSOR_LOCATION = 3;
	private final static byte SC_OP_CODE_REQUEST_SUPPORTED_SENSOR_LOCATIONS = 4;

	/**
	 * Sets cumulative value. The value parameter is of type long to allow setting value
	 * from range 2<sup>31</sup>-1 to 2<sup>32</sup>.
	 *
	 * @param value new cumulative value.
	 * @return Data object.
	 */
	public static Data setCumulativeValue(final long value) {
		final MutableData data = new MutableData(new byte[5]);
		data.setByte(SC_OP_CODE_SET_CUMULATIVE_VALUE, 0);
		data.setValue(value, Data.FORMAT_UINT32, 1);
		return data;
	}

	/**
	 * Starts the calibration of the RSC Sensor.
	 * @return Data object.
	 */
	public static Data startSensorCalibration() {
		return new MutableData(new byte[] { SC_OP_CODE_START_SENSOR_CALIBRATION });
	}

	/**
	 * Update to the location of the sensor with the value sent as parameter to this op code.
	 *
	 * @param location new location id (UINT8). See SENSOR_LOCATION_* constants.
	 * @return Data object.
	 */
	public static Data updateSensorLocation(final int location) {
		final MutableData data =  new MutableData(new byte[2]);
		data.setByte(SC_OP_CODE_UPDATE_SENSOR_LOCATION, 0);
		data.setValue(location, Data.FORMAT_UINT8, 1);
		return data;
	}

	/**
	 * Request a list of supported locations where the sensor can be attached.
	 *
	 * @return Data object.
	 */
	public static Data requestSupportedSensorLocations() {
		return new MutableData(new byte[] { SC_OP_CODE_REQUEST_SUPPORTED_SENSOR_LOCATIONS });
	}
}
