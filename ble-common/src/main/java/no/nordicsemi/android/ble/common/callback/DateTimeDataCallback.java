package no.nordicsemi.android.ble.common.callback;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Calendar;

import no.nordicsemi.android.ble.common.profile.DateTimeCallback;
import no.nordicsemi.android.ble.data.Data;
import no.nordicsemi.android.ble.callback.profile.ProfileDataCallback;

@SuppressWarnings("ConstantConditions")
public abstract class DateTimeDataCallback implements ProfileDataCallback, DateTimeCallback {

	@Override
	public void onDataReceived(@NonNull final BluetoothDevice device, @NonNull final Data data) {
		final Calendar calendar = readDateTime(data, 0);
		if (calendar == null) {
			onInvalidDataReceived(device, data);
			return;
		}
		onDateTimeReceived(device, calendar);
	}

	@Nullable
	static Calendar readDateTime(@NonNull final Data data, final int offset) {
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
			calendar.set(Calendar.MONTH, month - 1); // months are 1-based
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
