package no.nordicsemi.android.ble.common.profile.cgm;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;

import no.nordicsemi.android.ble.data.Data;

public interface CGMSessionRunTimeCallback {

	/**
	 * Callback called whenever the CGM Session Run Time characteristic was read.
	 * <p>
	 * If the E2E CRC field was present in the CGM packet, the data has been verified against it.
	 * If CRC check has failed, the
	 * {@link #onContinuousGlucoseMonitorSessionRunTimeReceivedWithCrcError(BluetoothDevice, Data)}
	 * will be called instead.
	 *
	 * @param device         the target device.
	 * @param sessionRunTime the expected run time of the CGM session depending on physiological
	 *                       effects in future devices, in hours.
	 * @param secured        true if the packet was sent with E2E-CRC value that was verified to
	 *                       match the packet, false if the packet didn't contain CRC field.
	 *                       For a callback in case of invalid CRC value check
	 *                       {@link #onContinuousGlucoseMonitorSessionRunTimeReceivedWithCrcError(BluetoothDevice, Data)}.
	 */
	void onContinuousGlucoseMonitorSessionRunTimeReceived(@NonNull final BluetoothDevice device,
														  final int sessionRunTime,
														  final boolean secured);

	/**
	 * Callback called when a CGM Session Run Time packet with E2E field was received but the CRC
	 * check has failed.
	 *
	 * @param device the target device.
	 * @param data   the CGM Session Run Time packet data that was received, including the CRC field.
	 */
	default void onContinuousGlucoseMonitorSessionRunTimeReceivedWithCrcError(@NonNull final BluetoothDevice device,
																			  @NonNull final Data data) {
		// ignore
	}
}
