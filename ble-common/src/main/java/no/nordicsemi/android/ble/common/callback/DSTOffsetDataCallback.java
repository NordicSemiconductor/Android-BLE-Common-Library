package no.nordicsemi.android.ble.common.callback;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import no.nordicsemi.android.ble.callback.profile.ProfileReadResponse;
import no.nordicsemi.android.ble.common.profile.DSTOffsetCallback;
import no.nordicsemi.android.ble.data.Data;

/**
 * Data callback that parses 1-byte value into a {@link DSTOffset}.
 * If the value received is shorter than 1 byte the
 * {@link #onInvalidDataReceived(BluetoothDevice, Data)} callback will be called.
 * See: https://www.bluetooth.com/specifications/gatt/viewer?attributeXmlFile=org.bluetooth.characteristic.dst_offset.xml
 */
@SuppressWarnings({"ConstantConditions", "unused"})
public abstract class DSTOffsetDataCallback extends ProfileReadResponse implements DSTOffsetCallback {

	@Override
	public void onDataReceived(@NonNull final BluetoothDevice device, @NonNull final Data data) {
		super.onDataReceived(device, data);

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
