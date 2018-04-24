package no.nordicsemi.android.ble.common.callback;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;

import org.junit.Test;

import java.util.Calendar;

import no.nordicsemi.android.ble.callback.DataCallback;
import no.nordicsemi.android.ble.data.Data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

@SuppressWarnings("ConstantConditions")
public class TimeZoneDataCallbackTest {
	private int result;
	private boolean success;
	private boolean unknownTimeZone;
	private boolean invalidData;

	private final DataCallback callback = new TimeZoneDataCallback() {
		@Override
		public void onDataReceived(@NonNull final BluetoothDevice device, @NonNull final Data data) {
			success = false;
			unknownTimeZone = false;
			invalidData = false;
			result = Integer.MAX_VALUE;
			super.onDataReceived(device, data);
		}

		@Override
		public void onTimeZoneReceived(@NonNull final BluetoothDevice device, final int offset) {
			success = true;
			result = offset;
		}

		@Override
		public void onUnknownTimeZoneReceived(@NonNull final BluetoothDevice device) {
			unknownTimeZone = true;
		}

		@Override
		public void onInvalidDataReceived(@NonNull final BluetoothDevice device, @NonNull final Data data) {
			invalidData = true;
		}
	};

	@Test
	public void onTimeZoneReceived_basic() {
		final Data data = new Data(new byte[] { 1 });
		callback.onDataReceived(null, data);
		assertTrue(success);
		assertEquals(15, result);
	}

	@Test
	public void onTimeZoneReceived_unknown() {
		final Data data = new Data(new byte[] { -128 });
		callback.onDataReceived(null, data);
		assertTrue(unknownTimeZone);
	}

	@Test
	public void onTimeZoneReceived_invalid() {
		final Data data = new Data(new byte[] { 60 });
		callback.onDataReceived(null, data);
		assertTrue(invalidData);
	}
}