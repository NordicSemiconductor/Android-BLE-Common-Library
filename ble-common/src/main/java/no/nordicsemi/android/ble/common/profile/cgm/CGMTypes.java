package no.nordicsemi.android.ble.common.profile.cgm;

@SuppressWarnings({"WeakerAccess", "unused"})
public interface CGMTypes {
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
		public int value;

		public CGMFeatures(final int features) {
			this.value = features;

			calibrationSupported = (features & 0x000001) != 0;
			patientHighLowAlertsSupported = (features & 0x000002) != 0;
			hypoAlertsSupported = (features & 0x000004) != 0;
			hyperAlertsSupported = (features & 0x000008) != 0;
			rateOfIncreaseDecreaseAlertsSupported = (features & 0x000010) != 0;
			deviceSpecificAlertSupported = (features & 0x000020) != 0;
			sensorMalfunctionDetectionSupported = (features & 0x000040) != 0;
			sensorTempHighLowDetectionSupported = (features & 0x000080) != 0;
			sensorResultHighLowSupported = (features & 0x000100) != 0;
			lowBatteryDetectionSupported = (features & 0x000200) != 0;
			sensorTypeErrorDetectionSupported = (features & 0x000400) != 0;
			generalDeviceFaultSupported = (features & 0x000800) != 0;
			e2eCrcSupported = (features & 0x001000) != 0;
			multipleBondSupported = (features & 0x002000) != 0;
			multipleSessionsSupported = (features & 0x004000) != 0;
			cgmTrendInfoSupported = (features & 0x008000) != 0;
			cgmQualityInfoSupported = (features & 0x010000) != 0;
		}
	}

	class CGMCalibrationStatus {
		public boolean rejected;
		public boolean dataOutOfRange;
		public boolean processPending;
		public int value;

		public CGMCalibrationStatus(final int status) {
			this.value = status;

			rejected = (status & 0x01) != 0;
			dataOutOfRange = (status & 0x02) != 0;
			processPending = (status & 0x04) != 0;
		}
	}

	class CGMStatus {
		public boolean sessionStopped;
		public boolean deviceBatteryLow;
		public boolean sensorTypeIncorrectForDevice;
		public boolean sensorMalfunction;
		public boolean deviceSpecificAlert;
		public boolean generalDeviceFault;
		public boolean timeSyncRequired;
		public boolean calibrationNotAllowed;
		public boolean calibrationRecommended;
		public boolean calibrationRequired;
		public boolean sensorTemperatureTooHigh;
		public boolean sensorTemperatureTooLow;
		public boolean sensorResultLowerThenPatientLowLevel;
		public boolean sensorResultHigherThenPatientHighLevel;
		public boolean sensorResultLowerThenHypoLevel;
		public boolean sensorResultHigherThenHyperLevel;
		public boolean sensorRateOfDecreaseExceeded;
		public boolean sensorRateOfIncreaseExceeded;
		public boolean sensorResultLowerThenDeviceCanProcess;
		public boolean sensorResultHigherThenDeviceCanProcess;
		public int warningStatus;
		public int calibrationTempStatus;
		public int sensorStatus;

		public CGMStatus(final int warningStatus, final int calibrationTempStatus, final int sensorStatus) {
			this.warningStatus = warningStatus;
			this.calibrationTempStatus = calibrationTempStatus;
			this.sensorStatus = sensorStatus;

			sessionStopped = (warningStatus & 0x01) != 0;
			deviceBatteryLow = (warningStatus & 0x02) != 0;
			sensorTypeIncorrectForDevice = (warningStatus & 0x04) != 0;
			sensorMalfunction = (warningStatus & 0x08) != 0;
			deviceSpecificAlert = (warningStatus & 0x10) != 0;
			generalDeviceFault = (warningStatus & 0x20) != 0;

			timeSyncRequired = (calibrationTempStatus & 0x01) != 0;
			calibrationNotAllowed = (calibrationTempStatus & 0x02) != 0;
			calibrationRecommended = (calibrationTempStatus & 0x04) != 0;
			calibrationRequired = (calibrationTempStatus & 0x08) != 0;
			sensorTemperatureTooHigh = (calibrationTempStatus & 0x10) != 0;
			sensorTemperatureTooLow = (calibrationTempStatus & 0x20) != 0;

			sensorResultLowerThenPatientLowLevel = (sensorStatus & 0x01) != 0;
			sensorResultHigherThenPatientHighLevel = (sensorStatus & 0x02) != 0;
			sensorResultLowerThenHypoLevel = (sensorStatus & 0x04) != 0;
			sensorResultHigherThenHyperLevel = (sensorStatus & 0x08) != 0;
			sensorRateOfDecreaseExceeded = (sensorStatus & 0x10) != 0;
			sensorRateOfIncreaseExceeded = (sensorStatus & 0x20) != 0;
			sensorResultLowerThenDeviceCanProcess = (sensorStatus & 0x40) != 0;
			sensorResultHigherThenDeviceCanProcess = (sensorStatus & 0x80) != 0;
		}
	}
}
