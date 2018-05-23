package no.nordicsemi.android.ble.common.callback.hr;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;

import org.junit.Test;

import no.nordicsemi.android.ble.callback.profile.ProfileReadResponse;
import no.nordicsemi.android.ble.common.profile.hr.BodySensorLocationCallback;
import no.nordicsemi.android.ble.data.Data;

import static org.junit.Assert.*;

@SuppressWarnings("ConstantConditions")
public class BodySensorLocationDataCallbackTest {
	private boolean success;
	private int sensorLocation;

	private final ProfileReadResponse response = new BodySensorLocationDataCallback() {
		@Override
		public void onBodySensorLocationReceived(@NonNull final BluetoothDevice device, final int sensorLocation) {
			BodySensorLocationDataCallbackTest.this.success = true;
			BodySensorLocationDataCallbackTest.this.sensorLocation = sensorLocation;
		}
	};

	@Test
	public void onBodySensorLocationReceived() {
		success = false;
		final Data data = new Data(new byte[] { BodySensorLocationCallback.SENSOR_LOCATION_EAR_LOBE });
		response.onDataReceived(null, data);
		assertTrue(response.isValid());
		assertTrue(success);
		assertEquals(BodySensorLocationCallback.SENSOR_LOCATION_EAR_LOBE, sensorLocation);
	}

	@Test
	public void onInvalidDataReceived() {
		success = false;
		final Data data = new Data();
		response.onDataReceived(null, data);
		assertFalse(response.isValid());
		assertFalse(success);
	}

}