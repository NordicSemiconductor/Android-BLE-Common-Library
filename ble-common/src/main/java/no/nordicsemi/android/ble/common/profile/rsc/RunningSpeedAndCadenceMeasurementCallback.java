package no.nordicsemi.android.ble.common.profile.rsc;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

@SuppressWarnings("unused")
public interface RunningSpeedAndCadenceMeasurementCallback {

	/**
	 * Method called when the Running Speed And Cadence Measurement data was received.
	 *
	 * @param device               target device.
	 * @param running              true if running was detected, false if walking.
	 * @param instantaneousSpeed   instantaneous Speed in m/s unit.
	 * @param instantaneousCadence instantaneous Cadence in steps per minute.
	 * @param strideLength         instantaneous Stride Length in centimeters.
	 * @param totalDistance        the Total Distance in meters (UINT32).
	 */
	void onRSCMeasurementReceived(final @NonNull BluetoothDevice device, final boolean running,
								  final float instantaneousSpeed, final int instantaneousCadence,
								  final @Nullable Integer strideLength,
								  final @Nullable Long totalDistance);
}
