package no.nordicsemi.android.ble.common.callback;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import no.nordicsemi.android.ble.callback.profile.ProfileReadResponse;
import no.nordicsemi.android.ble.common.profile.TimeZoneCallback;
import no.nordicsemi.android.ble.data.Data;

/**
 * Data callback that parses 1-byte value into a Time Zone offset.
 * If the value received is shorter than 1 byte the
 * {@link #onInvalidDataReceived(BluetoothDevice, Data)} callback will be called.
 * See: https://www.bluetooth.com/specifications/gatt/viewer?attributeXmlFile=org.bluetooth.characteristic.time_zone.xml
 */
@SuppressWarnings("WeakerAccess")
public abstract class TimeZoneDataCallback extends ProfileReadResponse implements TimeZoneCallback {
	public TimeZoneDataCallback() {
		// empty
	}

	protected TimeZoneDataCallback(final Parcel in) {
		super(in);
	}

	@Override
	public void onDataReceived(@NonNull final BluetoothDevice device, @NonNull final Data data) {
		super.onDataReceived(device, data);

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
