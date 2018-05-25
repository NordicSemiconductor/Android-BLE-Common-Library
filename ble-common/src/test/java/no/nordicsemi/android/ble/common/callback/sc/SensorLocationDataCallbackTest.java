package no.nordicsemi.android.ble.common.callback.sc;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;

import org.junit.Test;

import no.nordicsemi.android.ble.callback.profile.ProfileReadResponse;
import no.nordicsemi.android.ble.common.profile.sc.SensorLocationCallback;
import no.nordicsemi.android.ble.data.Data;

import static org.junit.Assert.*;

@SuppressWarnings("ConstantConditions")
public class SensorLocationDataCallbackTest {
	private boolean called;

	@Test
	public void onSensorLocationReceived() {
		final ProfileReadResponse callback = new SensorLocationDataCallback() {
			@Override
			public void onSensorLocationReceived(@NonNull final BluetoothDevice device, final int location) {
				called = true;
				assertEquals("Location", SensorLocationCallback.SENSOR_LOCATION_REAR_WHEEL, location);
			}
		};

		called = false;
		final Data data = new Data(new byte[] { 12 });
		callback.onDataReceived(null, data);
		assertTrue(called);
		assertTrue(callback.isValid());
	}

	@Test
	public void onInvalidDataReceived() {
		final ProfileReadResponse callback = new SensorLocationDataCallback() {
			@Override
			public void onSensorLocationReceived(@NonNull final BluetoothDevice device, final int location) {
				called = true;
			}
		};

		called = false;
		final Data data = new Data(new byte[] { 0x01, 0x02 });
		callback.onDataReceived(null, data);
		assertFalse(called);
		assertFalse(callback.isValid());
	}
}