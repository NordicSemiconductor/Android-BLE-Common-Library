package no.nordicsemi.android.ble.common.profile.csc;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;

public interface CyclingSpeedAndCadenceMeasurementCallback {
	/**
	 * Method called when the data received had wheel revolution data present.
	 * The default implementation calculates the total distance, distance since connection and
	 * current speed and calls {@link CyclingSpeedAndCadenceCallback#onDistanceChanged(BluetoothDevice, float, float, float)}.
	 *
	 * @param device             target device.
	 * @param wheelRevolutions   cumulative wheel revolutions since the CSC device was reset (UINT32).
	 * @param lastWheelEventTime last wheel event time in 1/1024 s (UINT16).
	 */
	void onWheelMeasurementReceived(final @NonNull BluetoothDevice device, final long wheelRevolutions, final int lastWheelEventTime);

	/**
	 * Method called when the data received had crank revolution data present.
	 * The default implementation calculates the crank cadence and gear ratio and
	 * calls {@link CyclingSpeedAndCadenceCallback#onCrankDataChanged(BluetoothDevice, float, float)}
	 *
	 * @param device             target device.
	 * @param crankRevolutions   cumulative crank revolutions since the CSC device was reset (UINT16).
	 * @param lastCrankEventTime last crank event time in 1/1024 s (UINT16).
	 */
	void onCrankMeasurementReceived(final @NonNull BluetoothDevice device, final int crankRevolutions, final int lastCrankEventTime);
}
