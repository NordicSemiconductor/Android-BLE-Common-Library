package no.nordicsemi.android.ble.common.callback;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;

import org.junit.Test;

import java.util.Calendar;

import no.nordicsemi.android.ble.callback.DataReceivedCallback;
import no.nordicsemi.android.ble.data.Data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

@SuppressWarnings("ConstantConditions")
public class DateTimeDataCallbackTest {

	@Test
	public void onDateTimeReceived_today() {
		final DataReceivedCallback callback = new DateTimeDataCallback() {
			@Override
			public void onDateTimeReceived(@NonNull final BluetoothDevice device, @NonNull final Calendar calendar) {
				assertTrue("Year set", calendar.isSet(Calendar.YEAR));
				assertEquals("Year", calendar.get(Calendar.YEAR), 2018);
				assertTrue("Month set", calendar.isSet(Calendar.MONTH));
				assertEquals("Month", calendar.get(Calendar.MONTH), Calendar.APRIL);
				assertTrue("Day set", calendar.isSet(Calendar.DATE));
				assertEquals("Day", 17, calendar.get(Calendar.DATE));
				assertEquals("Hour", 15, calendar.get(Calendar.HOUR_OF_DAY));
				assertEquals("Minute", 51, calendar.get(Calendar.MINUTE));
				assertEquals("Seconds", 0, calendar.get(Calendar.SECOND));
				assertEquals("Milliseconds", 0, calendar.get(Calendar.MILLISECOND));
			}

			@Override
			public void onInvalidDataReceived(@NonNull final BluetoothDevice device, @NonNull final Data data) {
				assertEquals("Correct Date and Time reported as invalid", 1, 2);
			}
		};
		final Data data = new Data(new byte[] {(byte) 0xE2, 0x07, 4, 17, 15, 51, 0 });
		callback.onDataReceived(null, data);
	}

	@Test
	public void onDateTimeReceived_dateNotKnown() {
		final DataReceivedCallback callback = new DateTimeDataCallback() {
			@Override
			public void onDateTimeReceived(@NonNull final BluetoothDevice device, @NonNull final Calendar calendar) {
				assertFalse("Year not set", calendar.isSet(Calendar.YEAR));
				assertFalse("Month not set", calendar.isSet(Calendar.MONTH));
				assertFalse("Day not set", calendar.isSet(Calendar.DATE));
				assertEquals("Hour", 18, calendar.get(Calendar.HOUR_OF_DAY));
				assertEquals("Minute", 26, calendar.get(Calendar.MINUTE));
				assertEquals("Second", 32, calendar.get(Calendar.SECOND));
				assertEquals("Milliseconds", 0, calendar.get(Calendar.MILLISECOND));
			}

			@Override
			public void onInvalidDataReceived(@NonNull final BluetoothDevice device, @NonNull final Data data) {
				assertEquals("Correct Date and Time reported as invalid", 1, 2);
			}
		};
		final Data data = new Data(new byte[] {(byte) 0x00, 0x00, 0, 0, 18, 26, 32 });
		callback.onDataReceived(null, data);
	}

	@Test
	public void onInvalidDataReceived_dataTooLong() {
		final DataReceivedCallback callback = new DateTimeDataCallback() {
			@Override
			public void onDateTimeReceived(@NonNull final BluetoothDevice device, @NonNull final Calendar calendar) {
				assertEquals("Incorrect Date and Time reported as correct", 1, 2);
			}

			@Override
			public void onInvalidDataReceived(@NonNull final BluetoothDevice device, @NonNull final Data data) {
				assertEquals("Incorrect Date and Time size", 0, data.size());
			}
		};
		final Data data = new Data();
		callback.onDataReceived(null, data);
	}

	@Test
	public void onInvalidDataReceived_dataTooShort() {
		final DataReceivedCallback callback = new DateTimeDataCallback() {
			@Override
			public void onDateTimeReceived(@NonNull final BluetoothDevice device, @NonNull final Calendar calendar) {
				assertEquals("Incorrect Date and Time reported as correct", 1, 2);
			}

			@Override
			public void onInvalidDataReceived(@NonNull final BluetoothDevice device, @NonNull final Data data) {
				assertNotEquals("Incorrect Date and Time size", 7, data.size());
			}
		};
		final Data data = new Data(new byte[] {(byte) 0xE2, 0x07, 4, 17, 15, 51 });
		callback.onDataReceived(null, data);
	}
}