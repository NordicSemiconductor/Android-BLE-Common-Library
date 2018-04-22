package no.nordicsemi.android.ble.common.profile;

import android.support.annotation.NonNull;

import no.nordicsemi.android.ble.data.Data;

@SuppressWarnings("unused")
public interface ContinuousGlucoseMeasurementFeatureCallback {
	int TYPE_CAPILLARY_WHOLE_BLOOD = 1;
	int TYPE_CAPILLARY_PLASMA = 2;
	int TYPE_VENOUS_WHOLE_BLOOD = 3;
	int TYPE_VENOUS_PLASMA = 4;
	int TYPE_ARTERIAL_WHOLE_BLOOD = 5;
	int TYPE_ARTERIAL_PLASMA = 6;
	int TYPE_UNDETERMINED_WHOLE_BLOOD = 7;
	int TYPE_UNDETERMINED_PLASMA = 8;
	int TYPE_INTERSTITIAL_FLUID_ISF = 9;
	int TYPE_CONTROL_SOLUTION = 10;

	int SAMPLE_LOCATION_FINGER = 1;
	int SAMPLE_LOCATION_ALTERNATE_SITE_TEST = 2;
	int SAMPLE_LOCATION_EARLOBE = 3;
	int SAMPLE_LOCATION_CONTROL_SOLUTION = 4;
	int SAMPLE_LOCATION_SUBCUTANEOUS_TISSUE = 5;
	int SAMPLE_LOCATION_VALUE_NOT_AVAILABLE = 15;

	@SuppressWarnings("WeakerAccess")
	class CGMFeatures {
		public boolean calibrationSupported;
		public boolean patientHighLowAlertsSupported;
		public boolean hypoAlertsSupported;
		public boolean hyperAlertsSupported;
		public boolean rateOfIncreaseDecreaseAlertsSupported;
		public boolean deviceSpecificAlertSupported;
		public boolean sensorMalfunctionDetectionSupported;
		public boolean sensorTempHighLowDetectionSupported;
		public boolean sensorResultHighLowSupported;
		public boolean lowBatteryDetectionSupported;
		public boolean sensorTypeErrorDetectionSupported;
		public boolean generalDeviceFaultSupported;
		public boolean e2eCrcSupported;
		public boolean multipleBondSupported;
		public boolean multipleSessionsSupported;
		public boolean cgmTrendInfoSupported;
		public boolean cgmQualityInfoSupported;

		public CGMFeatures(final int features) {
			calibrationSupported                  = (features & 0x000001) != 0;
			patientHighLowAlertsSupported         = (features & 0x000002) != 0;
			hypoAlertsSupported                   = (features & 0x000004) != 0;
			hyperAlertsSupported                  = (features & 0x000008) != 0;
			rateOfIncreaseDecreaseAlertsSupported = (features & 0x000010) != 0;
			deviceSpecificAlertSupported          = (features & 0x000020) != 0;
			sensorMalfunctionDetectionSupported   = (features & 0x000040) != 0;
			sensorTempHighLowDetectionSupported   = (features & 0x000080) != 0;
			sensorResultHighLowSupported          = (features & 0x000100) != 0;
			lowBatteryDetectionSupported          = (features & 0x000200) != 0;
			sensorTypeErrorDetectionSupported     = (features & 0x000400) != 0;
			generalDeviceFaultSupported           = (features & 0x000800) != 0;
			e2eCrcSupported                       = (features & 0x001000) != 0;
			multipleBondSupported                 = (features & 0x002000) != 0;
			multipleSessionsSupported             = (features & 0x004000) != 0;
			cgmTrendInfoSupported                 = (features & 0x008000) != 0;
			cgmQualityInfoSupported               = (features & 0x010000) != 0;
		}
	}

	/**
	 * Callback called when Continuous Glucose Measurement Feature value was received.
	 *
	 * @param features supported features
	 * @param type sample type, see TYPE_* constants.
	 * @param sampleLocation sample location, see SAMPLE_LOCATION_* constants.
	 * @param secured true, if the value received was secured with E2E-CRC value and the CRC matched the
	 *                packet. False, if the {@link CGMFeatures#e2eCrcSupported} feature is not supported.
	 */
	void onContinuousGlucoseMeasurementFeaturesReceived(final @NonNull CGMFeatures features,
														final int type, final int sampleLocation,
														final boolean secured);

	/**
	 * Callback called when a CGM Feature packet with E2E field was received but the CRC check has failed.
	 * @param data CGM Feature packet data that was received, including the CRC field.
	 */
	default void onContinuousGlucoseMeasurementFeaturesReceivedWithCrcError(final @NonNull Data data) {
		// ignore
	}
}
