package no.nordicsemi.android.ble.common.callback.ht;

import org.junit.Test;

import no.nordicsemi.android.ble.callback.profile.ProfileReadResponse;
import no.nordicsemi.android.ble.data.Data;

import static org.junit.Assert.*;

@SuppressWarnings("ConstantConditions")
public class MeasurementIntervalDataCallbackTest {
	private boolean called;

	@Test
	public void onMeasurementIntervalReceived() {
		final ProfileReadResponse response = new MeasurementIntervalDataCallback() {
			@Override
			public void onMeasurementIntervalReceived(final int interval) {
				called = true;
				assertEquals("Interval", 60, interval);
			}
		};

		called = false;
		final Data data = new Data(new byte[] { 60, 0 });
		response.onDataReceived(null, data);
		assertTrue(response.isValid());
		assertTrue(called);
	}

	@Test
	public void onInvalidDataReceived() {
		final ProfileReadResponse response = new MeasurementIntervalDataCallback() {
			@Override
			public void onMeasurementIntervalReceived(final int interval) {
				called = true;
			}
		};

		called = false;
		final Data data = new Data(new byte[] { 60 });
		response.onDataReceived(null, data);
		assertFalse(called);
		assertFalse(response.isValid());
	}
}