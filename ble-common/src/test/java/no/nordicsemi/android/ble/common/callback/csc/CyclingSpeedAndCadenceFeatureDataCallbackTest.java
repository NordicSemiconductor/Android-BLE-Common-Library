package no.nordicsemi.android.ble.common.callback.csc;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;

import org.junit.Test;

import no.nordicsemi.android.ble.callback.profile.ProfileReadResponse;
import no.nordicsemi.android.ble.data.Data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@SuppressWarnings("ConstantConditions")
public class CyclingSpeedAndCadenceFeatureDataCallbackTest {
	private boolean called;

	@Test
	public void onCyclingSpeedAndCadenceFeaturesReceived() {
		final ProfileReadResponse callback = new CyclingSpeedAndCadenceFeatureDataCallback() {
			@Override
			public void onCyclingSpeedAndCadenceFeaturesReceived(@NonNull final BluetoothDevice device,
																 @NonNull final CSCFeatures features) {
				called = true;
				assertNotNull(features);
				assertTrue("Wheel revolutions supported", features.wheelRevolutionDataSupported);
				assertTrue("Crank revolutions supported", features.crankRevolutionDataSupported);
				assertFalse("Multiple sensors not supported", features.multipleSensorDataSupported);
				assertEquals("Feature value", 0x03, features.value);
			}
		};

		called = false;
		final Data data = new Data(new byte[] { 0x03, 0x00 });
		callback.onDataReceived(null, data);
		assertTrue(called);
		assertTrue(callback.isValid());
	}

	@Test
	public void onInvalidDataReceived() {
		final ProfileReadResponse callback = new CyclingSpeedAndCadenceFeatureDataCallback() {
			@Override
			public void onCyclingSpeedAndCadenceFeaturesReceived(@NonNull final BluetoothDevice device,
																 @NonNull final CSCFeatures features) {
				called = true;
			}
		};

		called = false;
		final Data data = new Data(new byte[] { 0x03 });
		callback.onDataReceived(null, data);
		assertFalse(called);
		assertFalse(callback.isValid());
	}
}