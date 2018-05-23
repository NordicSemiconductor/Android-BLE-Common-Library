package no.nordicsemi.android.ble.common.data.hr;

import no.nordicsemi.android.ble.data.Data;

@SuppressWarnings("unused")
public final class HeartRateControlPointData {
	private static final byte[] RESET = { 0x01 };

	/**
	 * Reset Energy Expended: resets the value of the Energy Expended field in
	 * the Heart Rate Measurement characteristic to 0.
	 */
	public static Data reset() {
		return new Data(RESET);
	}
}
