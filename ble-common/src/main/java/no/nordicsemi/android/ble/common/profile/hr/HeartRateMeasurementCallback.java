package no.nordicsemi.android.ble.common.profile.hr;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

public interface HeartRateMeasurementCallback {

	/**
	 * Callback called when Hear Rate notification has been received.
	 *
	 * @param device          the target device.
	 * @param heartRate       the current heart rate in beats per minute.
	 * @param contactDetected information whether sensor contact has been detected, or not.
	 *                        Null if this feature is not supported.
	 * @param energyExpanded  the energy expanded in kilo Joules, or null if feature is not supported.
	 * @param rrIntervals     an optional list of RR intervals, each in 1/1024 of a second unit.
	 *                        Null if this feature is not supported.
	 */
	void onHeartRateMeasurementReceived(@NonNull final BluetoothDevice device,
										final int heartRate,
										@Nullable final Boolean contactDetected,
										@Nullable final Integer energyExpanded,
										@Nullable final List<Integer> rrIntervals);
}
