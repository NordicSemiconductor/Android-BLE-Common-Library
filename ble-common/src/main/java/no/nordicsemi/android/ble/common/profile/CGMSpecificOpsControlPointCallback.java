package no.nordicsemi.android.ble.common.profile;

import android.support.annotation.NonNull;

import no.nordicsemi.android.ble.data.Data;

public interface CGMSpecificOpsControlPointCallback extends ContinuousGlucoseMonitorTypes {

	/**
	 * Callback called when a CGM Specific Ops request has finished successfully.
	 */
	void onCGMSpecificOpsOperationCompleted();

	/**
	 * Callback called when a CGM Specific Ops request has failed.
	 *
	 * @param error the received error code, see CGM_ERROR_* constants.
	 */
	void onCGMSpecificOpsOperationError(final int error);

	/**
	 * Callback called when a CGM Specific Ops response was received with and incorrect E2E CRC.
	 *
	 * @param data CGM Specific Ops packet data that was received, including the CRC field.
	 */
	void onCGMSpecificOpsResponseReceivedWithCrcError(final @NonNull Data data);

	/**
	 * Callback called as a result of 'Fet CGM communication interval' procedure.
	 *
	 * @param interval the time interval in minutes after which the CGM Measurement is sent to the client.
	 * @param secured  true, if the value received was secured with E2E-CRC value and the CRC matched
	 *                 the packet. False, if the CRC field was not present.
	 */
	void onContinuousGlucoseCommunicationIntervalReceived(final int interval, final boolean secured);

	/**
	 * Callback called after a calibration value is received from the server.
	 *
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
	void onContinuousGlucoseCalibrationValueReceived(final float glucoseConcentrationOfCalibration,
													 final int calibrationTime, final int nextCalibrationTime,
													 final int type, final int sampleLocation,
													 final int calibrationDataRecordNumber,
													 final CGMCalibrationStatus status, final boolean secured);

	/**
	 * Callback called when the Patient High Alert response was received.
	 *
	 * @param alertLevel a level of glucose concentration in mg/dL to trigger the Patient High Alert
	 *                   in the Sensor Status Annunciation field.
	 * @param secured    true, if the value received was secured with E2E-CRC
	 *                   value and the CRC matched the packet. False, if the
	 *                   CRC field was not present.
	 */
	void onContinuousGlucosePatientHighAlertReceived(final float alertLevel, final boolean secured);

	/**
	 * Callback called when the Patient Low Alert response was received.
	 *
	 * @param alertLevel a level of glucose concentration in mg/dL to trigger the Patient Low Alert
	 *                   in the Sensor Status Annunciation field.
	 * @param secured    true, if the value received was secured with E2E-CRC
	 *                   value and the CRC matched the packet. False, if the
	 *                   CRC field was not present.
	 */
	void onContinuousGlucosePatientLowAlertReceived(final float alertLevel, final boolean secured);

	/**
	 * Callback called when the Hypo Alert response was received.
	 *
	 * @param alertLevel a level of glucose concentration in mg/dL to trigger the Hypo Alert
	 *                   in the Sensor Status Annunciation field.
	 * @param secured    true, if the value received was secured with E2E-CRC
	 *                   value and the CRC matched the packet. False, if the
	 *                   CRC field was not present.
	 */
	void onContinuousGlucoseHypoAlertReceived(final float alertLevel, final boolean secured);

	/**
	 * Callback called when the Hyper Alert response was received.
	 *
	 * @param alertLevel a level of glucose concentration in mg/dL to trigger the Hyper Alert
	 *                   in the Sensor Status Annunciation field.
	 * @param secured    true, if the value received was secured with E2E-CRC
	 *                   value and the CRC matched the packet. False, if the
	 *                   CRC field was not present.
	 */
	void onContinuousGlucoseHyperAlertReceived(final float alertLevel, final boolean secured);

	/**
	 * Callback called when the Rate Of Decrease Alert response was received.
	 *
	 * @param alertLevel a rate of glucose concentration change in mg/dL/min to trigger the
	 *                   Rate Of Decrease Alert in the Sensor Status Annunciation field.
	 * @param secured    true, if the value received was secured with E2E-CRC
	 *                   value and the CRC matched the packet. False, if the
	 *                   CRC field was not present.
	 */
	void onContinuousGlucoseRateOfDecreaseAlertReceived(final float alertLevel, final boolean secured);

	/**
	 * Callback called when the Hyper Alert response was received.
	 *
	 * @param alertLevel a rate of glucose concentration change in mg/dL/min to trigger the
	 *                   Rate Of Increase Alert in the Sensor Status Annunciation field.
	 * @param secured    true, if the value received was secured with E2E-CRC
	 *                   value and the CRC matched the packet. False, if the
	 *                   CRC field was not present.
	 */
	void onContinuousGlucoseRateOfIncreaseAlertReceived(final float alertLevel, final boolean secured);
}
