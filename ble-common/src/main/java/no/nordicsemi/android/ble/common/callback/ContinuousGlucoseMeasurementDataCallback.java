package no.nordicsemi.android.ble.common.callback;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;

import no.nordicsemi.android.ble.callback.profile.ProfileDataCallback;
import no.nordicsemi.android.ble.common.profile.ContinuousGlucoseMeasurementCallback;
import no.nordicsemi.android.ble.common.util.CRC16;
import no.nordicsemi.android.ble.data.Data;

@SuppressWarnings("ConstantConditions")
public abstract class ContinuousGlucoseMeasurementDataCallback implements ProfileDataCallback, ContinuousGlucoseMeasurementCallback {

	@Override
	public void onDataReceived(@NonNull final BluetoothDevice device, @NonNull final Data data) {
		int offset = 0;

		while (offset < data.size()) {
			// Packet size
			final int size = data.getIntValue(Data.FORMAT_UINT8, offset);

			if (size < 6 || offset + size > data.size()) {
				onInvalidDataReceived(device, data);
				return;
			}

			// Flags
			final int flags = data.getIntValue(Data.FORMAT_UINT8, offset + 1);

			final boolean cgmTrendInformationPresent   = (flags & 0x01) != 0;
			final boolean cgmQualityInformationPresent = (flags & 0x02) != 0;
			final boolean sensorWarningOctetPresent    = (flags & 0x20) != 0;
			final boolean sensorCalTempOctetPresent    = (flags & 0x40) != 0;
			final boolean sensorStatusOctetPresent     = (flags & 0x80) != 0;

			final int dataSize = 6 + (cgmTrendInformationPresent ? 2 : 0) + (cgmQualityInformationPresent ? 2 : 0)
					+ (sensorWarningOctetPresent ? 1 : 0) + (sensorCalTempOctetPresent ? 1 : 0)
					+ (sensorStatusOctetPresent ? 1 : 0);

			if (size != dataSize && size != dataSize + 2) {
				onInvalidDataReceived(device, data);
				return;
			}

			final boolean crcPresent = size == dataSize + 2;
			if (crcPresent) {
				final int expectedCrc = data.getIntValue(Data.FORMAT_UINT16, offset + dataSize);
				final int actualCrc = CRC16.MCRF4XX(data.getValue(), offset, dataSize);
				if (expectedCrc != actualCrc) {
					onCrcError(data);
					return;
				}
			}

			offset += 2;
			// Glucose concentration
			final float glucoseConcentration = data.getFloatValue(Data.FORMAT_SFLOAT, offset);
			offset += 2;

			// Time offset (in minutes since Session Start)
			final int timeOffset = data.getIntValue(Data.FORMAT_UINT16, offset);
			offset += 2;

			// Sensor Status Annunciation
			int warningStatus = 0;
			int calibrationTempStatus = 0;
			int sensorStatus = 0;
			Status status = null;

			if (sensorWarningOctetPresent) {
				warningStatus = data.getIntValue(Data.FORMAT_UINT8, offset++);
			}
			if (sensorCalTempOctetPresent) {
				calibrationTempStatus = data.getIntValue(Data.FORMAT_UINT8, offset++);
			}
			if (sensorStatusOctetPresent) {
				sensorStatus = data.getIntValue(Data.FORMAT_UINT8, offset++);
			}
			if (sensorWarningOctetPresent || sensorCalTempOctetPresent || sensorStatusOctetPresent) {
				status = new Status(warningStatus, calibrationTempStatus, sensorStatus);
			}

			// CGM Trend Information
			Float trend = null;
			if (cgmTrendInformationPresent) {
				trend = data.getFloatValue(Data.FORMAT_SFLOAT, offset);
				offset += 2;
			}

			// CGM Quality Information
			Float quality = null;
			if (cgmQualityInformationPresent) {
				quality = data.getFloatValue(Data.FORMAT_SFLOAT, offset);
				offset += 2;
			}

			// E2E-CRC
			if (crcPresent) {
				offset += 2;
			}

			onContinuousGlucoseMeasurementReceived(glucoseConcentration, trend, quality, timeOffset);
			if (status != null) {
				onSensorStatusChanged(status, timeOffset);
			}
		}
	}
}
