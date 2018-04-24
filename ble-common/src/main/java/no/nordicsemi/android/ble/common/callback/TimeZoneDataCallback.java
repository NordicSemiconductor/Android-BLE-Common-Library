package no.nordicsemi.android.ble.common.callback;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import no.nordicsemi.android.ble.callback.profile.ProfileDataCallback;
import no.nordicsemi.android.ble.common.profile.TimeZoneCallback;
import no.nordicsemi.android.ble.data.Data;

public abstract class TimeZoneDataCallback implements ProfileDataCallback, TimeZoneCallback {

	@Override
	public void onDataReceived(@NonNull final BluetoothDevice device, @NonNull final Data data) {
		final Integer offset = readTimeZone(data, 0);
		if (offset == null) {
			onInvalidDataReceived(device, data);
			return;
		}

		if (offset == -128) {
			onUnknownTimeZoneReceived(device);
		} else if (offset < -48 || offset > 56) {
			onInvalidDataReceived(device, data);
		} else {
			onTimeZoneReceived(device, offset * 15);
		}
	}

	/**
	 * Offset from UTC in number of 15 minutes increments. A value of -128 means that the time zone
	 * offset is not known. The offset defined in this characteristic is constant, regardless
	 * whether daylight savings is in effect.
	 *
	 * @param data   data received.
	 * @param offset offset from which the time zone is to be read.
	 * @return the time offset in 15 minutes increments, or null if offset is outside ot range.
	 */
	@Nullable
	public static Integer readTimeZone(@NonNull final Data data, final int offset) {
		if (data.size() < offset + 1)
			return null;

		return data.getIntValue(Data.FORMAT_SINT8, offset);
	}
}
