package no.nordicsemi.android.ble.common.callback.ht;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;

import org.junit.Test;

import no.nordicsemi.android.ble.callback.profile.ProfileReadResponse;
import no.nordicsemi.android.ble.common.profile.ht.HealthThermometerTypes;
import no.nordicsemi.android.ble.data.Data;

import static org.junit.Assert.*;

@SuppressWarnings("ConstantConditions")
public class TemperatureTypeDataCallbackTest {
	private boolean called;

	@Test
	public void onMeasurementIntervalReceived() {
		final ProfileReadResponse response = new TemperatureTypeDataCallback() {

			@Override
			public void onTemperatureTypeReceived(@NonNull final BluetoothDevice device,
												  final int type) {
				called = true;
				assertEquals("Temperature Type", HealthThermometerTypes.TYPE_EAR, type);
			}
		};

		called = false;
		final Data data = new Data(new byte[] { 3 });
		response.onDataReceived(null, data);
		assertTrue(response.isValid());
		assertTrue(called);
	}

	@Test
	public void onInvalidDataReceived() {
		final ProfileReadResponse response = new TemperatureTypeDataCallback() {
			@Override
			public void onTemperatureTypeReceived(@NonNull final BluetoothDevice device,
												  final int type) {
				called = true;
			}
		};

		called = false;
		final Data data = new Data(new byte[] { 3, 0 });
		response.onDataReceived(null, data);
		assertFalse(called);
		assertFalse(response.isValid());
	}

}