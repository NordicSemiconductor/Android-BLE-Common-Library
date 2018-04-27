package no.nordicsemi.android.ble.common.callback.glucose;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;

import org.junit.Test;

import no.nordicsemi.android.ble.callback.DataReceivedCallback;
import no.nordicsemi.android.ble.common.profile.glucose.GlucoseFeatureCallback;
import no.nordicsemi.android.ble.data.Data;

import static org.junit.Assert.*;

public class GlucoseFeatureDataCallbackTest {
	private boolean success;
	private boolean invalidData;
	private GlucoseFeatureCallback.GlucoseFeatures result;

	private DataReceivedCallback callback = new GlucoseFeatureDataCallback() {
		@Override
		public void onDataReceived(@NonNull final BluetoothDevice device, @NonNull final Data data) {
			success = false;
			invalidData = false;
			result = null;
			super.onDataReceived(device, data);
		}

		@Override
		public void onGlucoseFeaturesReceived(@NonNull final BluetoothDevice device, @NonNull final GlucoseFeatures features) {
			success = true;
			result = features;
		}

		@Override
		public void onInvalidDataReceived(@NonNull final BluetoothDevice device, @NonNull final Data data) {
			invalidData = true;
		}
	};

	@Test
	public void onGlucoseFeaturesReceived() {
		final Data data = new Data(new byte[] { (byte) 0b11110000, (byte) 0b00000011 });
		callback.onDataReceived(null, data);
		assertTrue(success);
		assertFalse(result.lowBatteryDetectionSupported);
		assertFalse(result.sensorMalfunctionDetectionSupported);
		assertFalse(result.sensorSampleSizeSupported);
		assertFalse(result.sensorStripInsertionErrorDetectionSupported);
		assertTrue(result.sensorStripTypeErrorDetectionSupported);
		assertTrue(result.sensorResultHighLowSupported);
		assertTrue(result.sensorTempHighLowDetectionSupported);
		assertTrue(result.sensorReadInterruptDetectionSupported);
		assertTrue(result.generalDeviceFaultSupported);
		assertTrue(result.timeFaultSupported);
		assertFalse(result.multipleBondSupported);
	}

	@Test
	public void onInvalidDataReceived() {
		final Data data = new Data();
		callback.onDataReceived(null, data);
		assertTrue(invalidData);
	}
}