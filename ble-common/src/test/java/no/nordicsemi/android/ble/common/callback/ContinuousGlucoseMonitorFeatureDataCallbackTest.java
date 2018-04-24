package no.nordicsemi.android.ble.common.callback;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;

import org.junit.Test;

import no.nordicsemi.android.ble.callback.DataCallback;
import no.nordicsemi.android.ble.data.Data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ContinuousGlucoseMonitorFeatureDataCallbackTest {
	private boolean called;

	@Test
	public void onContinuousGlucoseMeasurementFeaturesReceived_full() {
		final DataCallback callback = new ContinuousGlucoseMonitorFeatureDataCallback() {
			@Override
			public void onContinuousGlucoseMonitorFeaturesReceived(@NonNull final CGMFeatures features, final int type, final int sampleLocation, final boolean secured) {
				called = true;
				assertNotNull(features);
				assertFalse(features.calibrationSupported);
				assertTrue(features.patientHighLowAlertsSupported);
				assertTrue(features.hypoAlertsSupported);
				assertTrue(features.hyperAlertsSupported);
				assertFalse(features.rateOfIncreaseDecreaseAlertsSupported);
				assertTrue(features.deviceSpecificAlertSupported);
				assertTrue(features.sensorMalfunctionDetectionSupported);
				assertFalse(features.sensorTempHighLowDetectionSupported);
				assertFalse(features.sensorResultHighLowSupported);
				assertTrue(features.lowBatteryDetectionSupported);
				assertTrue(features.sensorTypeErrorDetectionSupported);
				assertTrue(features.generalDeviceFaultSupported);
				assertTrue(features.e2eCrcSupported);
				assertFalse(features.multipleBondSupported);
				assertFalse(features.multipleSessionsSupported);
				assertTrue(features.cgmTrendInfoSupported);
				assertTrue(features.cgmQualityInfoSupported);
				assertEquals("Type", TYPE_ARTERIAL_PLASMA, type);
				assertEquals("Sample Location", SAMPLE_LOCATION_FINGER, sampleLocation);
				assertTrue(secured);
			}

			@Override
			public void onContinuousGlucoseMonitorFeaturesReceivedWithCrcError(@NonNull final Data data) {
				assertEquals("Correct packet but invalid CRC reported", 1, 2);
			}

			@Override
			public void onInvalidDataReceived(@NonNull final BluetoothDevice device, @NonNull final Data data) {
				assertEquals("Correct packet but invalid data reported", 1, 2);
			}
		};
		final Data data = new Data(new byte[6]);
		assertTrue(data.setValue(0b11001111001101110, Data.FORMAT_UINT24, 0));
		assertTrue(data.setValue(0x16, Data.FORMAT_UINT8, 3));
		assertTrue(data.setValue(0xC18A, Data.FORMAT_UINT16, 4));
		called = false;
		callback.onDataReceived(null, data);
		assertTrue(called);
	}

	@Test
	public void onContinuousGlucoseMeasurementFeaturesReceived_crcNotSupported() {
		final DataCallback callback = new ContinuousGlucoseMonitorFeatureDataCallback() {
			@Override
			public void onContinuousGlucoseMonitorFeaturesReceived(@NonNull final CGMFeatures features, final int type, final int sampleLocation, final boolean secured) {
				called = true;
				assertNotNull(features);
				assertFalse(features.calibrationSupported);
				assertTrue(features.patientHighLowAlertsSupported);
				assertTrue(features.hypoAlertsSupported);
				assertTrue(features.hyperAlertsSupported);
				assertFalse(features.rateOfIncreaseDecreaseAlertsSupported);
				assertTrue(features.deviceSpecificAlertSupported);
				assertTrue(features.sensorMalfunctionDetectionSupported);
				assertFalse(features.sensorTempHighLowDetectionSupported);
				assertFalse(features.sensorResultHighLowSupported);
				assertTrue(features.lowBatteryDetectionSupported);
				assertTrue(features.sensorTypeErrorDetectionSupported);
				assertTrue(features.generalDeviceFaultSupported);
				assertFalse(features.e2eCrcSupported);
				assertFalse(features.multipleBondSupported);
				assertFalse(features.multipleSessionsSupported);
				assertTrue(features.cgmTrendInfoSupported);
				assertTrue(features.cgmQualityInfoSupported);
				assertEquals("Type", TYPE_ARTERIAL_PLASMA, type);
				assertEquals("Sample Location", SAMPLE_LOCATION_FINGER, sampleLocation);
				assertFalse(secured);
			}

			@Override
			public void onContinuousGlucoseMonitorFeaturesReceivedWithCrcError(@NonNull final Data data) {
				assertEquals("Correct packet but invalid CRC reported", 1, 2);
			}

			@Override
			public void onInvalidDataReceived(@NonNull final BluetoothDevice device, @NonNull final Data data) {
				assertEquals("Correct packet but invalid data reported", 1, 2);
			}
		};
		final Data data = new Data(new byte[6]);
		data.setValue(0b11000111001101110, Data.FORMAT_UINT24, 0);
		data.setValue(0x16, Data.FORMAT_UINT8, 3);
		data.setValue(0xFFFF, Data.FORMAT_UINT16, 4);
		called = false;
		callback.onDataReceived(null, data);
		assertTrue(called);
	}

	@Test
	public void onContinuousGlucoseMeasurementFeaturesReceivedWithCrcError() {
		final DataCallback callback = new ContinuousGlucoseMonitorFeatureDataCallback() {
			@Override
			public void onContinuousGlucoseMonitorFeaturesReceived(@NonNull final CGMFeatures features, final int type, final int sampleLocation, final boolean secured) {
				assertEquals("Wrong CRC but data reported", 1, 2);
			}

			@Override
			public void onContinuousGlucoseMonitorFeaturesReceivedWithCrcError(@NonNull final Data data) {
				called = true;
			}

			@Override
			public void onInvalidDataReceived(@NonNull final BluetoothDevice device, @NonNull final Data data) {
				assertEquals("Wrong CRC but invalid data reported", 1, 2);
			}
		};
		final Data data = new Data(new byte[6]);
		assertTrue(data.setValue(0b11001111001101110, Data.FORMAT_UINT24, 0));
		assertTrue(data.setValue(0x16, Data.FORMAT_UINT8, 3));
		assertTrue(data.setValue(0xBEAF, Data.FORMAT_UINT16, 4));
		called = false;
		callback.onDataReceived(null, data);
		assertTrue(called);
	}

	@Test
	public void onInvalidDataReceived_noCrc() {
		final DataCallback callback = new ContinuousGlucoseMonitorFeatureDataCallback() {
			@Override
			public void onContinuousGlucoseMonitorFeaturesReceived(@NonNull final CGMFeatures features, final int type, final int sampleLocation, final boolean secured) {
				assertEquals("Invalid data but data reported", 1, 2);
			}

			@Override
			public void onContinuousGlucoseMonitorFeaturesReceivedWithCrcError(@NonNull final Data data) {
				assertEquals("Invalid data but wrong CRC reported", 1, 2);
			}

			@Override
			public void onInvalidDataReceived(@NonNull final BluetoothDevice device, @NonNull final Data data) {
				called = true;
			}
		};
		final Data data = new Data(new byte[4]);
		assertTrue(data.setValue(0b11001111001101110, Data.FORMAT_UINT24, 0));
		assertTrue(data.setValue(0x16, Data.FORMAT_UINT8, 3));
		called = false;
		callback.onDataReceived(null, data);
		assertTrue(called);
	}

	@Test
	public void onInvalidDataReceived_wrongDefaultCrc() {
		final DataCallback callback = new ContinuousGlucoseMonitorFeatureDataCallback() {
			@Override
			public void onContinuousGlucoseMonitorFeaturesReceived(@NonNull final CGMFeatures features, final int type, final int sampleLocation, final boolean secured) {
				assertEquals("Wrong CRC but data reported", 1, 2);
			}

			@Override
			public void onContinuousGlucoseMonitorFeaturesReceivedWithCrcError(@NonNull final Data data) {
				assertEquals("Correct packet but invalid CRC reported", 1, 2);
			}

			@Override
			public void onInvalidDataReceived(@NonNull final BluetoothDevice device, @NonNull final Data data) {
				called = true;
			}
		};
		final Data data = new Data(new byte[6]);
		assertTrue(data.setValue(0b11000111001101110, Data.FORMAT_UINT24, 0));
		assertTrue(data.setValue(0x16, Data.FORMAT_UINT8, 3));
		assertTrue(data.setValue(0xBEAF, Data.FORMAT_UINT16, 4));
		called = false;
		callback.onDataReceived(null, data);
		assertTrue(called);
	}
}