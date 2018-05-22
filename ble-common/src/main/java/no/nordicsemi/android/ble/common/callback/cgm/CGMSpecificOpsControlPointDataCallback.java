package no.nordicsemi.android.ble.common.callback.cgm;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;

import no.nordicsemi.android.ble.callback.profile.ProfileReadResponse;
import no.nordicsemi.android.ble.common.profile.cgm.CGMSpecificOpsControlPointCallback;
import no.nordicsemi.android.ble.common.util.CRC16;
import no.nordicsemi.android.ble.data.Data;

/**
 * Data callback that parses value into CGM Session Start Time data.
 * If the value received do not match required syntax
 * {@link #onInvalidDataReceived(BluetoothDevice, Data)} callback will be called.
 * If the device supports E2E CRC validation and the CRC is not valid, the
 * {@link #onCGMSpecificOpsResponseReceivedWithCrcError(BluetoothDevice, Data)}
 * will be called.
 * See: https://www.bluetooth.com/specifications/gatt/viewer?attributeXmlFile=org.bluetooth.characteristic.cgm_specific_ops_control_point.xml
 */
@SuppressWarnings({"ConstantConditions", "UnnecessaryReturnStatement"})
public abstract class CGMSpecificOpsControlPointDataCallback extends ProfileReadResponse implements CGMSpecificOpsControlPointCallback {
	private final static int OP_CODE_COMMUNICATION_INTERVAL_RESPONSE = 3;
	private final static int OP_CODE_CALIBRATION_VALUE_RESPONSE = 6;
	private final static int OP_CODE_PATIENT_HIGH_ALERT_LEVEL_RESPONSE = 9;
	private final static int OP_CODE_PATIENT_LOW_ALERT_LEVEL_RESPONSE = 12;
	private final static int OP_CODE_HYPO_ALERT_LEVEL_RESPONSE = 15;
	private final static int OP_CODE_HYPER_ALERT_LEVEL_RESPONSE = 18;
	private final static int OP_CODE_RATE_OF_DECREASE_ALERT_LEVEL_RESPONSE = 21;
	private final static int OP_CODE_RATE_OF_INCREASE_ALERT_LEVEL_RESPONSE = 24;
	private final static int OP_CODE_RESPONSE_CODE = 28;
	private final static int CGM_RESPONSE_SUCCESS = 1;

	@Override
	public void onDataReceived(@NonNull final BluetoothDevice device, @NonNull final Data data) {
		super.onDataReceived(device, data);

		if (data.size() < 2) {
			onInvalidDataReceived(device, data);
			return;
		}

		// Read the Op Code
		final int opCode = data.getIntValue(Data.FORMAT_UINT8, 0);

		// Estimate the expected operand size based on the Op Code
		int expectedOperandSize;
		switch (opCode) {
			case OP_CODE_COMMUNICATION_INTERVAL_RESPONSE:
				// UINT8
				expectedOperandSize = 1;
				break;
			case OP_CODE_CALIBRATION_VALUE_RESPONSE:
				// Calibration Value
				expectedOperandSize = 10;
				break;
			case OP_CODE_PATIENT_HIGH_ALERT_LEVEL_RESPONSE:
			case OP_CODE_PATIENT_LOW_ALERT_LEVEL_RESPONSE:
			case OP_CODE_HYPO_ALERT_LEVEL_RESPONSE:
			case OP_CODE_HYPER_ALERT_LEVEL_RESPONSE:
			case OP_CODE_RATE_OF_DECREASE_ALERT_LEVEL_RESPONSE:
			case OP_CODE_RATE_OF_INCREASE_ALERT_LEVEL_RESPONSE:
				// SFLOAT
				expectedOperandSize = 2;
				break;
			case OP_CODE_RESPONSE_CODE:
				// Request Op Code (UINT8), Response Code Value (UINT8)
				expectedOperandSize = 2;
				break;
			default:
				onInvalidDataReceived(device, data);
				return;
		}

		// Verify packet length
		if (data.size() != 1 + expectedOperandSize && data.size() != 1 + expectedOperandSize + 2) {
			onInvalidDataReceived(device, data);
			return;
		}

		// Verify CRC if present
		final boolean crcPresent = data.size() == 1 + expectedOperandSize + 2; // opCode + expected operand + CRC
		if (crcPresent) {
			final int expectedCrc = data.getIntValue(Data.FORMAT_UINT16, 1 + expectedOperandSize);
			final int actualCrc   = CRC16.MCRF4XX(data.getValue(), 0, 1 + expectedOperandSize);
			if (expectedCrc != actualCrc) {
				onCGMSpecificOpsResponseReceivedWithCrcError(device, data);
				return;
			}
		}

		switch (opCode) {
			case OP_CODE_COMMUNICATION_INTERVAL_RESPONSE:
				final int interval = data.getIntValue(Data.FORMAT_UINT8, 1);
				onContinuousGlucoseCommunicationIntervalReceived(device, interval, crcPresent);
				return;
			case OP_CODE_CALIBRATION_VALUE_RESPONSE:
				final float glucoseConcentrationOfCalibration = data.getFloatValue(Data.FORMAT_SFLOAT, 1);
				final int calibrationTime = data.getIntValue(Data.FORMAT_UINT16, 3);
				final int calibrationTypeAndSampleLocation = data.getIntValue(Data.FORMAT_UINT8, 5);
				final int calibrationType = calibrationTypeAndSampleLocation & 0x0F;
				final int calibrationSampleLocation = calibrationTypeAndSampleLocation >> 4;
				final int nextCalibrationTime = data.getIntValue(Data.FORMAT_UINT16, 6);
				final int calibrationDataRecordNumber = data.getIntValue(Data.FORMAT_UINT16, 8);
				final int calibrationStatus = data.getIntValue(Data.FORMAT_UINT8, 10);
				onContinuousGlucoseCalibrationValueReceived(device, glucoseConcentrationOfCalibration,
						calibrationTime, nextCalibrationTime, calibrationType, calibrationSampleLocation,
						calibrationDataRecordNumber, new CGMCalibrationStatus(calibrationStatus), crcPresent);
				return;
			case OP_CODE_RESPONSE_CODE:
				final int requestCode = data.getIntValue(Data.FORMAT_UINT8, 1); // ignore
				final int responseCode = data.getIntValue(Data.FORMAT_UINT8, 2);
				if (responseCode == CGM_RESPONSE_SUCCESS) {
					onCGMSpecificOpsOperationCompleted(device, requestCode, crcPresent);
				} else {
					onCGMSpecificOpsOperationError(device, requestCode, responseCode, crcPresent);
				}
				return;
		}

		// Read SFLOAT value
		final float value = data.getFloatValue(Data.FORMAT_SFLOAT, 1);
		switch (opCode) {
			case OP_CODE_PATIENT_HIGH_ALERT_LEVEL_RESPONSE:
				onContinuousGlucosePatientHighAlertReceived(device, value, crcPresent);
				return;
			case OP_CODE_PATIENT_LOW_ALERT_LEVEL_RESPONSE:
				onContinuousGlucosePatientLowAlertReceived(device, value, crcPresent);
				return;
			case OP_CODE_HYPO_ALERT_LEVEL_RESPONSE:
				onContinuousGlucoseHypoAlertReceived(device, value, crcPresent);
				return;
			case OP_CODE_HYPER_ALERT_LEVEL_RESPONSE:
				onContinuousGlucoseHyperAlertReceived(device, value, crcPresent);
				return;
			case OP_CODE_RATE_OF_DECREASE_ALERT_LEVEL_RESPONSE:
				onContinuousGlucoseRateOfDecreaseAlertReceived(device, value, crcPresent);
				return;
			case OP_CODE_RATE_OF_INCREASE_ALERT_LEVEL_RESPONSE:
				onContinuousGlucoseRateOfIncreaseAlertReceived(device, value, crcPresent);
				return;
		}
	}
}
