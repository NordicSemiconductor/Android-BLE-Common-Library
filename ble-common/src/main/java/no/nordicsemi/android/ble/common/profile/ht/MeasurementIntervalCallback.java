package no.nordicsemi.android.ble.common.profile.ht;

@SuppressWarnings("unused")
public interface MeasurementIntervalCallback {

	/**
	 * Callback called when Measurement Interval characteristic has been read.
	 *
	 * @param interval measurement interval in seconds. Maximum value is 65535 which is equal to
	 *                 18 hours, 12 minutes and 15 seconds.
	 */
	void onMeasurementIntervalReceived(final int interval);
}
