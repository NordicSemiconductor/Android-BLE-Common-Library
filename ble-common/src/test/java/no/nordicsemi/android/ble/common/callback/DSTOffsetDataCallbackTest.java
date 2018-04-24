package no.nordicsemi.android.ble.common.callback;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;

import org.junit.Test;

import no.nordicsemi.android.ble.callback.DataCallback;
import no.nordicsemi.android.ble.common.profile.DSTOffsetCallback;
import no.nordicsemi.android.ble.data.Data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

@SuppressWarnings("ConstantConditions")
public class DSTOffsetDataCallbackTest {
	private DSTOffsetCallback.DSTOffset result;
	private boolean success;
	private boolean invalidData;

	private final DataCallback callback = new DSTOffsetDataCallback() {
		@Override
		public void onDataReceived(@NonNull final BluetoothDevice device, @NonNull final Data data) {
			success = false;
			invalidData = false;
			result = null;
			super.onDataReceived(device, data);
		}

		@Override
		public void onDSTOffsetReceived(@NonNull final BluetoothDevice device, @NonNull final DSTOffset offset) {
			success = true;
			result = offset;
		}

		@Override
		public void onInvalidDataReceived(@NonNull final BluetoothDevice device, @NonNull final Data data) {
			invalidData = true;
		}
	};

	@Test
	public void onDSTOffsetReceived_standard() {
		final Data data = new Data(new byte[] { 0 });
		callback.onDataReceived(null, data);
		assertTrue(success);
		assertSame(DSTOffsetCallback.DSTOffset.STANDARD_TIME, result);
	}

	@Test
	public void onDSTOffsetReceived_half() {
		final Data data = new Data(new byte[] { 2 });
		callback.onDataReceived(null, data);
		assertTrue(success);
		assertSame(DSTOffsetCallback.DSTOffset.HALF_AN_HOUR_DAYLIGHT_TIME, result);
	}

	@Test
	public void onDSTOffsetReceived_daylight() {
		final Data data = new Data(new byte[] { 4 });
		callback.onDataReceived(null, data);
		assertTrue(success);
		assertSame(DSTOffsetCallback.DSTOffset.DAYLIGHT_TIME, result);
	}

	@Test
	public void onDSTOffsetReceived_double() {
		final Data data = new Data(new byte[] { 8 });
		callback.onDataReceived(null, data);
		assertTrue(success);
		assertSame(DSTOffsetCallback.DSTOffset.DOUBLE_DAYLIGHT_TIME, result);
	}

	@Test
	public void onDSTOffsetReceived_unknown() {
		final Data data = new Data(new byte[] {(byte) 255});
		callback.onDataReceived(null, data);
		assertTrue(success);
		assertSame(DSTOffsetCallback.DSTOffset.UNKNOWN, result);
	}

	@Test
	public void onDSTOffsetReceived_invalid() {
		final Data data = new Data(new byte[] { 17 });
		callback.onDataReceived(null, data);
		assertTrue(invalidData);
	}

	@Test
	public void onDSTOffsetReceived_tooShort() {
		final Data data = new Data(new byte[0]);
		callback.onDataReceived(null, data);
		assertTrue(invalidData);
	}
}