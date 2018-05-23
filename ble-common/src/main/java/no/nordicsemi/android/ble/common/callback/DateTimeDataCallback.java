package no.nordicsemi.android.ble.common.callback;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Calendar;

import no.nordicsemi.android.ble.callback.profile.ProfileReadResponse;
import no.nordicsemi.android.ble.common.profile.DateTimeCallback;
import no.nordicsemi.android.ble.data.Data;

/**
 * Data callback that parses 7-byte value into a Calendar instance.
 * If the value received is shorter than 7 bytes the
 * {@link #onInvalidDataReceived(BluetoothDevice, Data)} callback will be called.
 * See: https://www.bluetooth.com/specifications/gatt/viewer?attributeXmlFile=org.bluetooth.characteristic.date_time.xml
 */
@SuppressWarnings({"ConstantConditions", "WeakerAccess"})
public abstract class DateTimeDataCallback extends ProfileReadResponse implements DateTimeCallback {

	public DateTimeDataCallback() {
		// empty
	}

	protected DateTimeDataCallback(final Parcel in) {
		super(in);
	}

	@Override
	public void onDataReceived(@NonNull final BluetoothDevice device, @NonNull final Data data) {
		super.onDataReceived(device, data);

		final Calendar calendar = readDateTime(data, 0);
		if (calendar == null) {
			onInvalidDataReceived(device, data);
			return;
		}
		onDateTimeReceived(device, calendar);
	}

	/**
	 * Returns a Gregorian Calendar object with YEAR, MONTH, DATE, HOUR, MINUTE, SECONDS set from
	 * the data at given offset using the Date Time characteristic format.
	 * MILLISECONDS are set to 0. Time Zone and DST offset are from the local time zone.
	 * <p>
	 * If YEAR, MONTH or DATE are set to 0 in the data, the corresponding fields in the calendar are 'unset',
	 * that is {@code calendar.isSet(Calendar.YEAR)} returns false.
	 * </p>
	 *
	 * @param data input data (7 bytes required).
	 * @param offset offset to read from.
	 * @return Calendar object or null.
	 */
	@Nullable
	public static Calendar readDateTime(@NonNull final Data data, final int offset) {
		if (data.size() < offset + 7)
			return null;

		final Calendar calendar = Calendar.getInstance();
		final int year = data.getIntValue(Data.FORMAT_UINT16, offset);
		final int month = data.getIntValue(Data.FORMAT_UINT8, offset + 2);
		final int day = data.getIntValue(Data.FORMAT_UINT8, offset + 3);
		if (year > 0)
			calendar.set(Calendar.YEAR, year);
		else
			calendar.clear(Calendar.YEAR);
		if (month > 0)
			calendar.set(Calendar.MONTH, month - 1); // months are 1-based in Date Time characteristic
		else
			calendar.clear(Calendar.MONTH);
		if (day > 0)
			calendar.set(Calendar.DATE, day);
		else
			calendar.clear(Calendar.DATE);
		calendar.set(Calendar.HOUR_OF_DAY, data.getIntValue(Data.FORMAT_UINT8, offset + 4));
		calendar.set(Calendar.MINUTE, data.getIntValue(Data.FORMAT_UINT8, offset + 5));
		calendar.set(Calendar.SECOND, data.getIntValue(Data.FORMAT_UINT8, offset + 6));
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar;
	}
}
