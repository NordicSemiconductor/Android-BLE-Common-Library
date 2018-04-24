package no.nordicsemi.android.ble.common.callback.cgms;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import no.nordicsemi.android.ble.callback.profile.ProfileDataCallback;
import no.nordicsemi.android.ble.common.callback.DSTOffsetDataCallback;
import no.nordicsemi.android.ble.common.callback.DateTimeDataCallback;
import no.nordicsemi.android.ble.common.callback.TimeZoneDataCallback;
import no.nordicsemi.android.ble.common.profile.cgm.CGMSessionStartTimeCallback;
import no.nordicsemi.android.ble.common.profile.DSTOffsetCallback;
import no.nordicsemi.android.ble.common.util.CRC16;
import no.nordicsemi.android.ble.data.Data;

@SuppressWarnings("ConstantConditions")
public abstract class CGMSessionStartTimeDataCallback implements ProfileDataCallback, CGMSessionStartTimeCallback {

	@Override
	public void onDataReceived(@NonNull final BluetoothDevice device, @NonNull final Data data) {
		if (data.size() != 9 && data.size() != 11) {
			onInvalidDataReceived(device, data);
			return;
		}

		final boolean crcPresent = data.size() == 11;
		if (crcPresent) {
			final int actualCrc = CRC16.MCRF4XX(data.getValue(), 0, 9);
			final int expectedCrc = data.getIntValue(Data.FORMAT_UINT16, 9);
			if (actualCrc != expectedCrc) {
				onContinuousGlucoseMonitorSessionStartTimeReceivedWithCrcError(device, data);
				return;
			}
		}

		final Calendar calendar = DateTimeDataCallback.readDateTime(data, 0);
		final Integer timeZoneOffset = TimeZoneDataCallback.readTimeZone(data, 7); // [minutes]
		final DSTOffsetCallback.DSTOffset dstOffset = DSTOffsetDataCallback.readDSTOffset(data, 8);

		if (calendar == null || timeZoneOffset == null || dstOffset == null) {
			onInvalidDataReceived(device, data);
			return;
		}

		final TimeZone timeZone = new TimeZone() {
			@Override
			public int getOffset(final int era, final int year, final int month, final int day, final int dayOfWeek, final int milliseconds) {
				return (timeZoneOffset + dstOffset.offset) * 60000; // convert minutes to milliseconds
			}

			@Override
			public void setRawOffset(final int offsetMillis) {
				throw new UnsupportedOperationException("Can't set raw offset for this TimeZone");
			}

			@Override
			public int getRawOffset() {
				return timeZoneOffset * 60000;
			}

			@Override
			public boolean useDaylightTime() {
				return true;
			}

			@Override
			public boolean inDaylightTime(final Date date) {
				// Use of DST is dependent on the input data only
				return dstOffset.offset > 0;
			}

			@Override
			public int getDSTSavings() {
				return dstOffset.offset * 60000;
			}

			// TODO add TimeZone ID
//			@Override
//			public String getID() {
//				return super.getID();
//			}
		};

		calendar.setTimeZone(timeZone);

		onContinuousGlucoseMonitorSessionStartTimeReceived(device, calendar, crcPresent);
	}
}
