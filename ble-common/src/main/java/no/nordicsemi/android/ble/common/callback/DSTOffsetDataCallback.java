package no.nordicsemi.android.ble.common.callback;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import no.nordicsemi.android.ble.callback.profile.ProfileDataCallback;
import no.nordicsemi.android.ble.common.profile.DSTOffsetCallback;
import no.nordicsemi.android.ble.common.profile.TimeZoneCallback;
import no.nordicsemi.android.ble.data.Data;

@SuppressWarnings("ConstantConditions")
public abstract class DSTOffsetDataCallback implements ProfileDataCallback, DSTOffsetCallback {

	@Override
	public void onDataReceived(@NonNull final BluetoothDevice device, @NonNull final Data data) {
		final DSTOffset offset = readDSTOffset(data, 0);
		if (offset == null) {
			onInvalidDataReceived(device, data);
			return;
		}

		onDSTOffsetReceived(device, offset);
	}

	/**
	 * Returns the Daylight Saving Time Offset, or null if offset is out of data range.
	 *
	 * @param data input data.
	 * @param offset offset to read DST Offset from.
	 * @return DSTOffset field or null.
	 */
	@Nullable
	public static DSTOffset readDSTOffset(@NonNull final Data data, final int offset) {
		if (data.size() < offset + 1)
			return null;

		final int o = data.getIntValue(Data.FORMAT_UINT8, offset);
		return DSTOffset.from(o);
	}
}
