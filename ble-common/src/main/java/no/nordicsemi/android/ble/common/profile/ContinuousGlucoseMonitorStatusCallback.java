package no.nordicsemi.android.ble.common.profile;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;

import no.nordicsemi.android.ble.data.Data;

public interface ContinuousGlucoseMonitorStatusCallback extends ContinuousGlucoseMonitorTypes {

	/**
	 * Callback called whenever the CGM Status characteristic was read..
	 * <p>
	 * If the E2E CRC field was present in the CGM packet, the data has been verified against it.
	 * If CRC check has failed, the {@link #onContinuousGlucoseMonitorStatusReceivedWithCrcError(BluetoothDevice,Data)}
	 * will be called instead.
	 * </p>
	 *
	 * @param device     target device.
	 * @param status     the status received.
	 * @param timeOffset time offset in minutes since Session Start Time.
	 * @param secured    true if the packet was sent with E2E-CRC value that was verified to match the packet,
	 *                   false if the packet didn't contain CRC field. For a callback in case of
	 *                   invalid CRC value check {@link #onContinuousGlucoseMonitorStatusReceivedWithCrcError(BluetoothDevice,Data)}.
	 */
	void onContinuousGlucoseMonitorStatusChanged(final @NonNull BluetoothDevice device,
												 final CGMStatus status, final int timeOffset,
												 final boolean secured);

	/**
	 * Callback called when a CGM packet with E2E field was received but the CRC check has failed.
	 *
	 * @param device target device.
	 * @param data   CGM packet data that was received, including the CRC field.
	 */
	default void onContinuousGlucoseMonitorStatusReceivedWithCrcError(final @NonNull BluetoothDevice device, final @NonNull Data data) {
		// ignore
	}
}
