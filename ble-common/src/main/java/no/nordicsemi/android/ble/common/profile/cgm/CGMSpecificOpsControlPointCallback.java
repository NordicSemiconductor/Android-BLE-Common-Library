package no.nordicsemi.android.ble.common.profile.cgm;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;

import no.nordicsemi.android.ble.data.Data;

@SuppressWarnings("unused")
public interface CGMSpecificOpsControlPointCallback extends CGMTypes {
	int CGM_OP_CODE_SET_COMMUNICATION_INTERVAL = 1;
	int CGM_OP_CODE_SET_CALIBRATION_VALUE = 4;
	int CGM_OP_CODE_SET_PATIENT_HIGH_ALERT_LEVEL = 7;
	int CGM_OP_CODE_SET_PATIENT_LOW_ALERT_LEVEL = 10;
	int CGM_OP_CODE_SET_HYPO_ALERT_LEVEL = 13;
	int CGM_OP_CODE_SET_HYPER_ALERT_LEVEL = 16;
	int CGM_OP_CODE_SET_RATE_OF_DECREASE_ALERT_LEVEL = 19;
	int CGM_OP_CODE_SET_RATE_OF_INCREASE_ALERT_LEVEL = 22;
	int CGM_OP_CODE_RESET_DEVICE_SPECIFIC_ERROR = 25;
	int CGM_OP_CODE_START_SESSION = 26;
	int CGM_OP_CODE_STOP_SESSION = 27;

	// int CGM_RESPONSE_SUCCESS = 1;
	int CGM_ERROR_OP_CODE_NOT_SUPPORTED = 2;
	int CGM_ERROR_INVALID_OPERAND = 3;
	int CGM_ERROR_PROCEDURE_NOT_COMPLETED = 4;
	int CGM_ERROR_PARAMETER_OUT_OF_RANGE = 5;

	/**
	 * Callback called when a CGM Specific Ops request has finished successfully.
	 *
	 * @param device      target device.
	 * @param requestCode request code that has completed. One of CGM_OP_CODE_* constants.
	 * @param secured     true, if the value received was secured with E2E-CRC value and the CRC matched
	 *                    the packet. False, if the CRC field was not present.
	 */
	void onCGMSpecificOpsOperationCompleted(@NonNull final BluetoothDevice device, final int requestCode, final boolean secured);

	/**
	 * Callback called when a CGM Specific Ops request has failed.
	 *
	 * @param device      target device.
	 * @param requestCode request code that has completed with an error. One of CGM_OP_CODE_* constants,
	 *                    or other if such was requested.
	 * @param errorCode   the received error code, see CGM_ERROR_* constants.
	 * @param secured     true, if the value received was secured with E2E-CRC value and the CRC matched
	 *                    the packet. False, if the CRC field was not present.
	 */
	void onCGMSpecificOpsOperationError(@NonNull final BluetoothDevice device, final int requestCode, final int errorCode, final boolean secured);

	/**
	 * Callback called when a CGM Specific Ops response was received with and incorrect E2E CRC.
	 *
	 * @param device target device.
	 * @param data   CGM Specific Ops packet data that was received, including the CRC field.
	 */
	default void onCGMSpecificOpsResponseReceivedWithCrcError(@NonNull final BluetoothDevice device, final @NonNull Data data) {
		// empty
	}

	/**
	 * Callback called as a result of 'Fet CGM communication interval' procedure.
	 *
	 * @param device   target device.
	 * @param interval the time interval in minutes after which the CGM Measurement is sent to the client.
	 * @param secured  true, if the value received was secured with E2E-CRC value and the CRC matched
	 *                 the packet. False, if the CRC field was not present.
	 */
	default void onContinuousGlucoseCommunicationIntervalReceived(@NonNull final BluetoothDevice device, final int interval, final boolean secured) {
		// empty
	}

	/**
	 * Callback called after a calibration value is received from the server.
	 *
	 * @param device                            target device.
	 * @param glucoseConcentrationOfCalibration glucose concentration value is transmitted in mg/dL.
	 * @param calibrationTime                   the time the calibration value has been measured as
	 *                                          relative offset to the Session Start Time in minutes.
	 * @param nextCalibrationTime               the relative offset to the Session Start Time when the
	 *                                          next calibration is required. A value of 0 means that
	 *                                          a calibration is required instantly.
	 * @param type                              sample type, see TYPE_* constants.
	 * @param sampleLocation                    sample location, see SAMPLE_LOCATION_* constants.
	 * @param calibrationDataRecordNumber       a unique number of the calibration record.
	 *                                          A value of 0 represents no calibration value is stored.
	 * @param status                            the status of the calibration procedure of the Server
	 *                                          related to the Calibration Data Record.
	 * @param secured                           true, if the value received was secured with E2E-CRC
	 *                                          value and the CRC matched the packet. False, if the
	 *                                          CRC field was not present.
	 */
	default void onContinuousGlucoseCalibrationValueReceived(final @NonNull BluetoothDevice device,
													 final float glucoseConcentrationOfCalibration,
													 final int calibrationTime, final int nextCalibrationTime,
													 final int type, final int sampleLocation,
													 final int calibrationDataRecordNumber,
													 final @NonNull CGMCalibrationStatus status,
													 final boolean secured) {
		// empty
	}

	/**
	 * Callback called when the Patient High Alert response was received.
	 *
	 * @param device     target device.
	 * @param alertLevel a level of glucose concentration in mg/dL to trigger the Patient High Alert
	 *                   in the Sensor Status Annunciation field.
	 * @param secured    true, if the value received was secured with E2E-CRC
	 *                   value and the CRC matched the packet. False, if the
	 *                   CRC field was not present.
	 */
	default void onContinuousGlucosePatientHighAlertReceived(@NonNull final BluetoothDevice device, final float alertLevel, final boolean secured) {
		// empty
	}

	/**
	 * Callback called when the Patient Low Alert response was received.
	 *
	 * @param device     target device.
	 * @param alertLevel a level of glucose concentration in mg/dL to trigger the Patient Low Alert
	 *                   in the Sensor Status Annunciation field.
	 * @param secured    true, if the value received was secured with E2E-CRC
	 *                   value and the CRC matched the packet. False, if the
	 *                   CRC field was not present.
	 */
	default void onContinuousGlucosePatientLowAlertReceived(@NonNull final BluetoothDevice device, final float alertLevel, final boolean secured) {
		// empty
	}

	/**
	 * Callback called when the Hypo Alert response was received.
	 *
	 * @param alertLevel a level of glucose concentration in mg/dL to trigger the Hypo Alert
	 *                   in the Sensor Status Annunciation field.
	 * @param secured    true, if the value received was secured with E2E-CRC
	 *                   value and the CRC matched the packet. False, if the
	 *                   CRC field was not present.
	 */
	default void onContinuousGlucoseHypoAlertReceived(@NonNull final BluetoothDevice device, final float alertLevel, final boolean secured) {
		// empty
	}

	/**
	 * Callback called when the Hyper Alert response was received.
	 *
	 * @param device     target device.
	 * @param alertLevel a level of glucose concentration in mg/dL to trigger the Hyper Alert
	 *                   in the Sensor Status Annunciation field.
	 * @param secured    true, if the value received was secured with E2E-CRC
	 *                   value and the CRC matched the packet. False, if the
	 *                   CRC field was not present.
	 */
	default void onContinuousGlucoseHyperAlertReceived(@NonNull final BluetoothDevice device, final float alertLevel, final boolean secured) {
		// empty
	}

	/**
	 * Callback called when the Rate Of Decrease Alert response was received.
	 *
	 * @param device     target device.
	 * @param alertLevel a rate of glucose concentration change in mg/dL/min to trigger the
	 *                   Rate Of Decrease Alert in the Sensor Status Annunciation field.
	 * @param secured    true, if the value received was secured with E2E-CRC
	 *                   value and the CRC matched the packet. False, if the
	 *                   CRC field was not present.
	 */
	default void onContinuousGlucoseRateOfDecreaseAlertReceived(@NonNull final BluetoothDevice device, final float alertLevel, final boolean secured) {
		// empty
	}

	/**
	 * Callback called when the Hyper Alert response was received.
	 *
	 * @param alertLevel a rate of glucose concentration change in mg/dL/min to trigger the
	 *                   Rate Of Increase Alert in the Sensor Status Annunciation field.
	 * @param secured    true, if the value received was secured with E2E-CRC
	 *                   value and the CRC matched the packet. False, if the
	 *                   CRC field was not present.
	 */
	default void onContinuousGlucoseRateOfIncreaseAlertReceived(@NonNull final BluetoothDevice device, final float alertLevel, final boolean secured) {
		// empty
	}
}
