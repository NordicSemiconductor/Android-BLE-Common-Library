package no.nordicsemi.android.ble.common.profile.ht;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;

@SuppressWarnings("unused")
public interface MeasurementIntervalCallback {

	/**
	 * Callback called when Measurement Interval characteristic has been read.
	 *
	 * @param device   the target device.
	 * @param interval measurement interval in seconds. Maximum value is 65535 which is equal to
	 *                 18 hours, 12 minutes and 15 seconds.
	 */
	void onMeasurementIntervalReceived(@NonNull final BluetoothDevice device,
									   final int interval);
}
