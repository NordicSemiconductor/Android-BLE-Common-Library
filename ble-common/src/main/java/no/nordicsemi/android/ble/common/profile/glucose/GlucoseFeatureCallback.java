package no.nordicsemi.android.ble.common.profile.glucose;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;

@SuppressWarnings("unused")
public interface GlucoseFeatureCallback {

	@SuppressWarnings("WeakerAccess")
	class GlucoseFeatures {
		public boolean lowBatteryDetectionSupported;
		public boolean sensorMalfunctionDetectionSupported;
		public boolean sensorSampleSizeSupported;
		public boolean sensorStripInsertionErrorDetectionSupported;
		public boolean sensorStripTypeErrorDetectionSupported;
		public boolean sensorResultHighLowSupported;
		public boolean sensorTempHighLowDetectionSupported;
		public boolean sensorReadInterruptDetectionSupported;
		public boolean generalDeviceFaultSupported;
		public boolean timeFaultSupported;
		public boolean multipleBondSupported;
		public int value;

		public GlucoseFeatures(final int features) {
			this.value = features;

			lowBatteryDetectionSupported = (features & 0x0001) != 0;
			sensorMalfunctionDetectionSupported = (features & 0x0002) != 0;
			sensorSampleSizeSupported = (features & 0x0004) != 0;
			sensorStripInsertionErrorDetectionSupported = (features & 0x0008) != 0;
			sensorStripTypeErrorDetectionSupported = (features & 0x0010) != 0;
			sensorResultHighLowSupported = (features & 0x0020) != 0;
			sensorTempHighLowDetectionSupported = (features & 0x0040) != 0;
			sensorReadInterruptDetectionSupported = (features & 0x0080) != 0;
			generalDeviceFaultSupported = (features & 0x0100) != 0;
			timeFaultSupported = (features & 0x0200) != 0;
			multipleBondSupported = (features & 0x0400) != 0;
		}
	}

	/**
	 * Callback called when Glucose Feature value was received.
	 *
	 * @param device   target device.
	 * @param features supported features.
	 */
	void onGlucoseFeaturesReceived(final @NonNull BluetoothDevice device, final @NonNull GlucoseFeatures features);
}
