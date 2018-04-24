package no.nordicsemi.android.ble.common.profile;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public interface DSTOffsetCallback {
	enum DSTOffset {
		STANDARD_TIME(0),
		HALF_AN_HOUR_DAYLIGHT_TIME(2),
		DAYLIGHT_TIME(4),
		DOUBLE_DAYLIGHT_TIME(8),
		UNKNOWN(255);

		/**
		 * Offset of the Daylight Saving Time in minutes.
		 */
		public int offset;

		DSTOffset(final int code) {
			if (code != 255)
				this.offset = code * 15; // convert to minutes
			else
				this.offset = 0;
		}

		@Nullable
		public static DSTOffset from(final int value) {
			switch (value) {
				case 0: return STANDARD_TIME;
				case 2: return HALF_AN_HOUR_DAYLIGHT_TIME;
				case 4: return DAYLIGHT_TIME;
				case 8: return DOUBLE_DAYLIGHT_TIME;
				case 255: return UNKNOWN;
				default: return null;
			}
		}
	}

	/**
	 * Callback called when DST Offset packet has been received.
	 *
	 * @param device target device.
	 * @param offset Daylight Saving Time offset.
	 */
	void onDSTOffsetReceived(@NonNull final BluetoothDevice device, final @NonNull DSTOffset offset);
}
