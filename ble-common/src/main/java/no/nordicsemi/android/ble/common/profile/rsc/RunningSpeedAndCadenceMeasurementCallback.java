package no.nordicsemi.android.ble.common.profile.rsc;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

@SuppressWarnings("unused")
public interface RunningSpeedAndCadenceMeasurementCallback {

	/**
	 * Method called when the Running Speed And Cadence Measurement data was received.
	 *
	 * @param device               the target device.
	 * @param running              true if running was detected, false if walking.
	 * @param instantaneousSpeed   instantaneous speed in m/s unit.
	 * @param instantaneousCadence instantaneous cadence in steps per minute.
	 * @param strideLength         instantaneous stride length in centimeters.
	 * @param totalDistance        the total distance in meters (UINT32).
	 */
	void onRSCMeasurementReceived(@NonNull final BluetoothDevice device, final boolean running,
								  final float instantaneousSpeed, final int instantaneousCadence,
								  @Nullable final Integer strideLength,
								  @Nullable final Long totalDistance);
}
