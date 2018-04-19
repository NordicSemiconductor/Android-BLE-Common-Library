package no.nordicsemi.android.ble.common.profile;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import no.nordicsemi.android.ble.data.Data;

public interface ContinuousGlucoseMeasurementCallback {

	@SuppressWarnings("WeakerAccess")
	class Status {
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

		public Status(final int warningStatus, final int calibrationTempStatus, final int sensorStatus) {
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

	/**
	 * Callback called when a Continuous Glucose Measurement packet has been received.
	 * If Sensor Status Annunciation was present in the packet,
	 * the {@link #onSensorStatusChanged(Status, int)} will be called immediately afterwards
	 * with the same time offset, even if the status hasn't changed since the last packet.
	 * <p>
	 *     If the E2E CRC field was present in the CGM packet, the data has been verified against it.
	 *     If CRC check has failed, the {@link #onCrcError(Data)} will be called instead.
	 * </p>
	 * <p>
	 *     The Glucose concentration is reported in mg/dL. To convert it to mmol/L use:
	 *     <pre>value [mg/dL] = 18.02 * value [mmol/L]</pre>
	 *     Note that the conversion factor is compliant to the Continua blood glucose meter specification.
	 * </p>
	 *
	 * @param glucoseConcentration glucose concentration in mg/dL.
	 * @param cgmTrend optional CGM Trend information, in (mg/dL)/min.
	 * @param cgmQuality optional CGM Quality information in percent.
	 * @param timeOffset time offset in minutes since Session Start Time.
	 */
	void onContinuousGlucoseMeasurementReceived(final float glucoseConcentration, final @Nullable Float cgmTrend, final @Nullable Float cgmQuality, final int timeOffset);

	/**
	 * Callback called whenever the Sensor Status Annunciation field was
	 * present int the CGM packet.
	 * <p>
	 *     If the E2E CRC field was present in the CGM packet, the data has been verified against it.
	 *     If CRC check has failed, the {@link #onCrcError(Data)} will be called instead.
	 * </p>
	 *
	 * @param status the status received.
	 * @param timeOffset time offset in minutes since Session Start Time.
	 */
	void onSensorStatusChanged(final Status status, final int timeOffset);

	/**
	 * Callback called when a CGM packet with E2E field was received but the CRC check hsa failed.
	 * @param data CGM packet data that was received, including the CRC field.
	 */
	default void onCrcError(final @NonNull Data data) {
		// ignore
	}
}
