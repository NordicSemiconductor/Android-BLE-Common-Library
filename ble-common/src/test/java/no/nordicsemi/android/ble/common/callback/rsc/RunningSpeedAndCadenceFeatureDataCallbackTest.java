package no.nordicsemi.android.ble.common.callback.rsc;

import org.junit.Test;

import no.nordicsemi.android.ble.callback.profile.ProfileReadResponse;
import no.nordicsemi.android.ble.common.callback.csc.CyclingSpeedAndCadenceFeatureDataCallback;
import no.nordicsemi.android.ble.common.profile.rsc.RunningSpeedAndCadenceFeatureCallback;
import no.nordicsemi.android.ble.data.Data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@SuppressWarnings("ConstantConditions")
public class RunningSpeedAndCadenceFeatureDataCallbackTest {
	private boolean called;

	@Test
	public void onRunningSpeedAndCadenceFeaturesReceived() {
		final ProfileReadResponse callback = new RunningSpeedAndCadenceFeatureDataCallback() {
			@Override
			public void onRunningSpeedAndCadenceFeaturesReceived(final RSCFeatures features) {
				called = true;
				assertNotNull(features);
				assertTrue("Instantaneous Stride Length Measurement supported", features.instantaneousStrideLengthMeasurementSupported);
				assertFalse("Total Distance Measurement supported", features.totalDistanceMeasurementSupported);
				assertTrue("Walking Or Running Status supported", features.walkingOrRunningStatusSupported);
				assertTrue("Calibration Procedure supported", features.calibrationProcedureSupported);
				assertFalse("Multiple Sensor Locations supported", features.multipleSensorLocationsSupported);
				assertEquals("Feature value", 0b01101, features.value);
			}
		};

		called = false;
		final Data data = new Data(new byte[] { 0b01101, 0x00 });
		callback.onDataReceived(null, data);
		assertTrue(called);
		assertTrue(callback.isValid());
	}

	@Test
	public void onInvalidDataReceived() {
		final ProfileReadResponse callback = new RunningSpeedAndCadenceFeatureDataCallback() {
			@Override
			public void onRunningSpeedAndCadenceFeaturesReceived(final RSCFeatures features) {
				called = true;
			}
		};

		called = false;
		final Data data = new Data(new byte[] { 0b01101 });
		callback.onDataReceived(null, data);
		assertFalse(called);
		assertFalse(callback.isValid());
	}
}