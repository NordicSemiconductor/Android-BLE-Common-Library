package no.nordicsemi.android.ble.common.callback.csc;

import org.junit.Test;

import no.nordicsemi.android.ble.callback.DataReceivedCallback;
import no.nordicsemi.android.ble.callback.profile.ProfileReadResponse;
import no.nordicsemi.android.ble.common.profile.csc.CyclingSpeedAndCadenceFeatureCallback;
import no.nordicsemi.android.ble.data.Data;

import static org.junit.Assert.*;

@SuppressWarnings("ConstantConditions")
public class CyclingSpeedAndCadenceFeatureDataCallbackTest {
	private boolean called;

	@Test
	public void onCyclingSpeedAndCadenceFeaturesReceived() {
		final ProfileReadResponse callback = new CyclingSpeedAndCadenceFeatureDataCallback() {
			@Override
			public void onCyclingSpeedAndCadenceFeaturesReceived(final CSCFeatures features) {
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
			public void onCyclingSpeedAndCadenceFeaturesReceived(final CSCFeatures features) {
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