package no.nordicsemi.android.ble.common.profile.cgm;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;

import java.util.Calendar;

import no.nordicsemi.android.ble.data.Data;

public interface CGMSessionStartTimeCallback {

	/**
	 * Callback called whenever the CGM Session Start Time characteristic was read.
	 * <p>
	 * If the E2E CRC field was present in the CGM packet, the data has been verified against it.
	 * If CRC check has failed, the {@link #onContinuousGlucoseMonitorSessionStartTimeReceivedWithCrcError(BluetoothDevice, Data)}
	 * will be called instead.
	 * </p>
	 *
	 * @param device   target device.
	 * @param calendar date and time received, as {@link Calendar} object. Time zone and DST offset
	 *                 are included in the calendar.
	 */
	void onContinuousGlucoseMonitorSessionStartTimeReceived(@NonNull final BluetoothDevice device, @NonNull final Calendar calendar, final boolean secured);

	/**
	 * Callback called when a CGM Session Start Time packet with E2E field was received but the CRC check has failed.
	 *
	 * @param device target device.
	 * @param data   CGM Session Start Time  packet data that was received, including the CRC field.
	 */
	default void onContinuousGlucoseMonitorSessionStartTimeReceivedWithCrcError(@NonNull final BluetoothDevice device, final @NonNull Data data) {
		// ignore
	}
}
