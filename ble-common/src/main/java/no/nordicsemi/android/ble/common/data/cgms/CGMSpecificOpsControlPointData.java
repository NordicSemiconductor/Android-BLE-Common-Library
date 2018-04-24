package no.nordicsemi.android.ble.common.data.cgms;

import no.nordicsemi.android.ble.common.profile.cgm.ContinuousGlucoseMonitorTypes;
import no.nordicsemi.android.ble.common.util.CRC16;
import no.nordicsemi.android.ble.data.Data;

public final class CGMSpecificOpsControlPointData implements ContinuousGlucoseMonitorTypes {
	private static final byte OP_CODE_SET_COMMUNICATION_INTERVAL = 1;
	private static final byte OP_CODE_GET_COMMUNICATION_INTERVAL = 2;
	private static final byte OP_CODE_SET_CALIBRATION_VALUE = 4;
	private static final byte OP_CODE_GET_CALIBRATION_VALUE = 5;
	private static final byte OP_CODE_SET_PATIENT_HIGH_ALERT_LEVEL = 7;
	private static final byte OP_CODE_GET_PATIENT_HIGH_ALERT_LEVEL = 8;
	private static final byte OP_CODE_SET_PATIENT_LOW_ALERT_LEVEL = 10;
	private static final byte OP_CODE_GET_PATIENT_LOW_ALERT_LEVEL = 11;
	private static final byte OP_CODE_SET_HYPO_ALERT_LEVEL = 13;
	private static final byte OP_CODE_GET_HYPO_ALERT_LEVEL = 14;
	private static final byte OP_CODE_SET_HYPER_ALERT_LEVEL = 16;
	private static final byte OP_CODE_GET_HYPER_ALERT_LEVEL = 17;
	private static final byte OP_CODE_SET_RATE_OF_DECREASE_ALERT_LEVEL = 19;
	private static final byte OP_CODE_GET_RATE_OF_DECREASE_ALERT_LEVEL = 20;
	private static final byte OP_CODE_SET_RATE_OF_INCREASE_ALERT_LEVEL = 22;
	private static final byte OP_CODE_GET_RATE_OF_INCREASE_ALERT_LEVEL = 23;
	private static final byte OP_CODE_RESET_DEVICE_SPECIFIC_ERROR = 25;
	private static final byte OP_CODE_START_SESSION = 26;
	private static final byte OP_CODE_STOP_SESSION = 27;

	private CGMSpecificOpsControlPointData() {
		// empty private constructor
	}

	public static Data startSession(final boolean secure) {
		return create(OP_CODE_START_SESSION, secure);
	}

	public static Data stopSession(final boolean secure) {
		return create(OP_CODE_STOP_SESSION, secure);
	}

	public static Data resetDeviceSpecificAlert(final boolean secure) {
		return create(OP_CODE_RESET_DEVICE_SPECIFIC_ERROR, secure);
	}

	public static Data setCommunicationInterval(final int interval, final boolean secure) {
		return create(OP_CODE_SET_COMMUNICATION_INTERVAL, interval, Data.FORMAT_UINT8, secure);
	}

	public static Data setCommunicationIntervalToFastestSupported(final boolean secure) {
		return create(OP_CODE_SET_COMMUNICATION_INTERVAL, 0xFF, Data.FORMAT_UINT8, secure);
	}

	public static Data disablePeriodicCommunication(final boolean secure) {
		return create(OP_CODE_SET_COMMUNICATION_INTERVAL, 0xFF, Data.FORMAT_UINT8, secure);
	}

	public static Data getCommunicationInterval(final boolean secure) {
		return create(OP_CODE_GET_COMMUNICATION_INTERVAL, secure);
	}

	public static Data setCalibrationValue(final float glucoseConcentrationOfCalibration,
										   final int sampleType, final int sampleLocation,
										   final int calibrationTime, final int nextCalibrationTime,
										   final boolean secure) {
		final Data data = new Data(new byte[11 + (secure ? 2 : 0)]);
		data.setByte(OP_CODE_SET_CALIBRATION_VALUE, 0);
		data.setValue(glucoseConcentrationOfCalibration, Data.FORMAT_SFLOAT, 1);
		data.setValue(calibrationTime, Data.FORMAT_UINT16, 3);
		final int typeAndSampleLocation = ((sampleLocation & 0xF) << 8) | (sampleType & 0xF);
		data.setValue(typeAndSampleLocation, Data.FORMAT_UINT8, 5);
		data.setValue(nextCalibrationTime, Data.FORMAT_UINT16, 6);
		data.setValue(0, Data.FORMAT_UINT16, 8); // ignored: calibration data record number
		data.setValue(0, Data.FORMAT_UINT8, 10); // ignored: calibration status
		return appendCrc(data, secure);
	}

	public static Data getCalibrationValue(final int calibrationDataRecordNumber, final boolean secure) {
		return create(OP_CODE_GET_CALIBRATION_VALUE, calibrationDataRecordNumber, Data.FORMAT_UINT16, secure);
	}

	public static Data getLastCalibrationValue(final boolean secure) {
		return create(OP_CODE_GET_CALIBRATION_VALUE, 0xFFFF, Data.FORMAT_UINT16, secure);
	}

	public static Data setPatientHighAlertLevel(final float level, final boolean secure) {
		return create(OP_CODE_SET_PATIENT_HIGH_ALERT_LEVEL, level, secure);
	}

	public static Data getPatientHighAlertLevel(final boolean secure) {
		return create(OP_CODE_GET_PATIENT_HIGH_ALERT_LEVEL, secure);
	}

	public static Data setPatientLowAlertLevel(final float level, final boolean secure) {
		return create(OP_CODE_SET_PATIENT_LOW_ALERT_LEVEL, level, secure);
	}

	public static Data getPatientLowAlertLevel(final boolean secure) {
		return create(OP_CODE_GET_PATIENT_LOW_ALERT_LEVEL, secure);
	}

	public static Data setHypoAlertLevel(final float level, final boolean secure) {
		return create(OP_CODE_SET_HYPO_ALERT_LEVEL, level, secure);
	}

	public static Data getHypoAlertLevel(final boolean secure) {
		return create(OP_CODE_GET_HYPO_ALERT_LEVEL, secure);
	}

	public static Data setHyperAlertLevel(final float level, final boolean secure) {
		return create(OP_CODE_SET_HYPER_ALERT_LEVEL, level, secure);
	}

	public static Data getHyperAlertLevel(final boolean secure) {
		return create(OP_CODE_GET_HYPER_ALERT_LEVEL, secure);
	}

	public static Data setRateOfDecreaseAlertLevel(final float level, final boolean secure) {
		return create(OP_CODE_SET_RATE_OF_DECREASE_ALERT_LEVEL, level, secure);
	}

	public static Data getRateOfDecreaseAlertLevel(final boolean secure) {
		return create(OP_CODE_GET_RATE_OF_DECREASE_ALERT_LEVEL, secure);
	}

	public static Data setRateOfIncreaseAlertLevel(final float level, final boolean secure) {
		return create(OP_CODE_SET_RATE_OF_INCREASE_ALERT_LEVEL, level, secure);
	}

	public static Data getRateOfIncreaseAlertLevel(final boolean secure) {
		return create(OP_CODE_GET_RATE_OF_INCREASE_ALERT_LEVEL, secure);
	}

	private static Data create(final byte opCode, final boolean secure) {
		final Data data = new Data(new byte[1 + (secure ? 2 : 0)]);
		data.setByte(opCode, 0);
		return appendCrc(data, secure);
	}

	private static Data create(final byte opCode, final int value, final int format, final boolean secure) {
		final Data data = new Data(new byte[1 + (format & 0xF) + (secure ? 2 : 0)]);
		data.setByte(opCode, 0);
		data.setValue(value, format, 1);
		return appendCrc(data, secure);
	}

	private static Data create(final byte opCode, final float value, final boolean secure) {
		final Data data = new Data(new byte[3 + (secure ? 2 : 0)]);
		data.setByte(opCode, 0);
		data.setValue(value, Data.FORMAT_SFLOAT, 1);
		return appendCrc(data, secure);
	}

	private static Data appendCrc(final Data data, final boolean secure) {
		if (secure) {
			final int length = data.size() - 2;
			final int crc = CRC16.MCRF4XX(data.getValue(), 0, length);
			data.setValue(crc, Data.FORMAT_UINT16, length);
		}
		return data;
	}
}
