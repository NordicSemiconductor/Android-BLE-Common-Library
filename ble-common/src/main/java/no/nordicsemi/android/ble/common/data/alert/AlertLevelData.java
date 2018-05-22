package no.nordicsemi.android.ble.common.data.alert;

import no.nordicsemi.android.ble.data.Data;

@SuppressWarnings("unused")
public final class AlertLevelData {
	private static final byte[] HIGH_ALERT = { 0x02 };
	private static final byte[] MILD_ALERT = { 0x01 };
	private static final byte[] NO_ALERT = { 0x00 };

	public static Data noAlert() {
		return new Data(NO_ALERT);
	}

	public static Data mildAlert() {
		return new Data(MILD_ALERT);
	}

	public static Data highAlert() {
		return new Data(HIGH_ALERT);
	}
}
