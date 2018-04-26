package no.nordicsemi.android.ble.common.callback.glucose;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.junit.Test;

import java.util.Calendar;

import no.nordicsemi.android.ble.callback.DataCallback;
import no.nordicsemi.android.ble.common.profile.glucose.GlucoseMeasurementCallback;
import no.nordicsemi.android.ble.data.Data;

import static org.junit.Assert.*;

public class GlucoseMeasurementDataCallbackTest {
	private boolean success;
	private boolean invalidData;

	private final DataCallback callback = new GlucoseMeasurementDataCallback() {
		@Override
		public void onGlucoseMeasurementReceived(@NonNull final BluetoothDevice device, final int sequenceNumber,
												 @NonNull final Calendar time, @Nullable final Float glucoseConcentration,
												 @Nullable final Integer unit, @Nullable final Integer type,
												 @Nullable final Integer sampleLocation, @Nullable final GlucoseStatus status,
												 final boolean contextInformationFollows) {
			success = true;
			assertNotNull(time);
			assertEquals(58, time.get(Calendar.MINUTE));
			assertNotNull(unit);
			assertEquals(GlucoseMeasurementCallback.UNIT_kg_L, unit.intValue());
			assertNotNull(status);
			assertEquals(261, status.value);
			assertTrue(status.deviceBatteryLow);
			assertFalse(status.generalDeviceFault);
		}

		@Override
		public void onInvalidDataReceived(@NonNull final BluetoothDevice device, @NonNull final Data data) {
			invalidData = true;
		}
	};

	@Test
	public void onGlucoseMeasurementReceived() {
		final Data data = new Data(new byte[] { (byte) 0b011011, 0, 0, (byte) 0xE2, 0x07, 4, 26, 11, 9, 30, 49, 0, 30, 0, 0x12, 0b101, 0b1 });
		callback.onDataReceived(null, data);
		assertTrue(success);
	}

	@Test
	public void onInvalidDataReceived() {
		final Data data = new Data(new byte[] { (byte) 0b011011, 0, 0, (byte) 0xE2, 0x07, 4, 26, 11, 9, 30, 30, 0, 0x12, 0b101, 0b1 }); // time offset missing
		callback.onDataReceived(null, data);
		assertTrue(invalidData);
	}

}