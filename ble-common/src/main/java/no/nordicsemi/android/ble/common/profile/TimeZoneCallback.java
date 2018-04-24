package no.nordicsemi.android.ble.common.profile;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;

import no.nordicsemi.android.ble.data.Data;

public interface TimeZoneCallback {

	/**
	 * Callback called when time zone packet has been received.
	 *
	 * @param device target device.
	 * @param offset Offset from UTC in minutes. The offset defined in this characteristic is
	 *               constant, regardless whether daylight savings is in effect.
	 */
	void onTimeZoneReceived(@NonNull final BluetoothDevice device, final int offset);

	/**
	 * Callback called when the time zone packet was received with value -128 (unknown time zone).
	 *
	 * @param device target device.
	 */
	void onUnknownTimeZoneReceived(@NonNull final BluetoothDevice device);
}
