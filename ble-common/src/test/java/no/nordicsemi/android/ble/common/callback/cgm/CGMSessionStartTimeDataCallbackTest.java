package no.nordicsemi.android.ble.common.callback.cgm;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;

import org.junit.Test;

import java.util.Calendar;

import no.nordicsemi.android.ble.callback.DataCallback;
import no.nordicsemi.android.ble.data.Data;

import static org.junit.Assert.*;

@SuppressWarnings("ConstantConditions")
public class CGMSessionStartTimeDataCallbackTest {
	private boolean success;
	private boolean invalidData;
	private boolean invalidCrc;
	private boolean verified;
	private Calendar result;

	private final DataCallback callback = new CGMSessionStartTimeDataCallback() {
		@Override
		public void onContinuousGlucoseMonitorSessionStartTimeReceived(@NonNull final BluetoothDevice device, @NonNull final Calendar calendar, final boolean secured) {
			success = true;
			verified = secured;
			result = calendar;
		}

		@Override
		public void onContinuousGlucoseMonitorSessionStartTimeReceivedWithCrcError(@NonNull final BluetoothDevice device, @NonNull final Data data) {
			invalidCrc = true;
		}

		@Override
		public void onInvalidDataReceived(@NonNull final BluetoothDevice device, @NonNull final Data data) {
			invalidData = true;
		}
	};

	@Test
	public void onContinuousGlucoseMonitorSessionStartTimeReceived() {
		final Data data = new Data(new byte[] {(byte) 0xE2, 0x07, 4, 24, 13, 8, 24, 8, 4, (byte) 0xE0, (byte) 0xC2 });
		callback.onDataReceived(null, data);
		assertTrue(success);
		assertTrue(verified);
		assertEquals(2018, result.get(Calendar.YEAR));
		assertEquals(Calendar.APRIL, result.get(Calendar.MONTH));
		assertEquals(24, result.get(Calendar.DATE));
		assertEquals(13, result.get(Calendar.HOUR_OF_DAY));
		assertEquals(8, result.get(Calendar.MINUTE));
		assertEquals(24, result.get(Calendar.SECOND));
		assertEquals(8 * 60000, result.get(Calendar.ZONE_OFFSET));
		assertEquals(4 * 15 * 60000, result.get(Calendar.DST_OFFSET));
	}

	@Test
	public void onContinuousGlucoseMonitorSessionStartTimeReceived_noYear() {
		final Data data = new Data(new byte[] {(byte) 0, 0, 4, 24, 13, 8, 24, 8, 2 });
		callback.onDataReceived(null, data);
		assertTrue(success);
		assertFalse(verified);
		assertFalse(result.isSet(Calendar.YEAR));
		assertEquals(Calendar.APRIL, result.get(Calendar.MONTH));
		assertEquals(24, result.get(Calendar.DATE));
		assertEquals(13, result.get(Calendar.HOUR_OF_DAY));
		assertEquals(8, result.get(Calendar.MINUTE));
		assertEquals(24, result.get(Calendar.SECOND));
		assertEquals(8 * 60000, result.get(Calendar.ZONE_OFFSET));
		assertEquals(2 * 15 * 60000, result.get(Calendar.DST_OFFSET));
	}

	@Test
	public void onContinuousGlucoseMonitorSessionStartTimeReceivedWithCrcError() {
		final Data data = new Data(new byte[] {(byte) 0xE2, 0x07, 4, 24, 13, 8, 24, 8, 4, (byte) 0xE0, (byte) 0xC3 });
		callback.onDataReceived(null, data);
		assertTrue(invalidCrc);
	}

	@Test
	public void onInvalidDataReceived() {
		final Data data = new Data(new byte[] {(byte) 0xE2, 0x07, 4, 24, 13, 8, 24, 8, 4, (byte) 0xE0 });
		callback.onDataReceived(null, data);
		assertTrue(invalidData);
	}
}