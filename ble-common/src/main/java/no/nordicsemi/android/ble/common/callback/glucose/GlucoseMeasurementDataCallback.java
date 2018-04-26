package no.nordicsemi.android.ble.common.callback.glucose;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;

import java.util.Calendar;

import no.nordicsemi.android.ble.callback.profile.ProfileDataCallback;
import no.nordicsemi.android.ble.common.callback.DateTimeDataCallback;
import no.nordicsemi.android.ble.common.profile.glucose.GlucoseMeasurementCallback;
import no.nordicsemi.android.ble.data.Data;

@SuppressWarnings("ConstantConditions")
public abstract class GlucoseMeasurementDataCallback implements ProfileDataCallback, GlucoseMeasurementCallback {

	@Override
	public void onDataReceived(@NonNull final BluetoothDevice device, @NonNull final Data data) {
		if (data.size() < 10) {
			onInvalidDataReceived(device, data);
			return;
		}

		int offset = 0;

		final int flags = data.getIntValue(Data.FORMAT_UINT8, offset++);
		final boolean timeOffsetPresent = (flags & 0x01) != 0;
		final boolean glucoseDataPresent = (flags & 0x02) != 0;
		final boolean unitMolL = (flags & 0x04) != 0;
		final boolean sensorStatusAnnunciationPresent = (flags & 0x08) != 0;
		final boolean contextInformationFollows = (flags & 0x10) != 0;

		if (data.size() < 10 + (timeOffsetPresent ? 2 : 0) + (glucoseDataPresent ? 3 : 0)
			+ (sensorStatusAnnunciationPresent ? 2 : 0)) {
			onInvalidDataReceived(device, data);
			return;
		}

		// Required fields
		final int sequenceNumber = data.getIntValue(Data.FORMAT_UINT16, offset);
		offset += 2;
		final Calendar baseTime = DateTimeDataCallback.readDateTime(data, 3);
		offset += 7;

		if (baseTime == null) {
			onInvalidDataReceived(device, data);
			return;
		}

		// Optional fields
		if (timeOffsetPresent) {
			final int timeOffset = data.getIntValue(Data.FORMAT_UINT16, offset);
			offset += 2;

			baseTime.add(Calendar.MINUTE, timeOffset);
		}

		Float glucoseConcentration = null;
		Integer unit = null;
		Integer type = null;
		Integer sampleLocation = null;
		if (glucoseDataPresent) {
			glucoseConcentration = data.getFloatValue(Data.FORMAT_SFLOAT, offset);
			final int typeAndSampleLocation = data.getIntValue(Data.FORMAT_UINT8, offset + 2);
			offset += 3;

			type = typeAndSampleLocation & 0x0F;
			sampleLocation = typeAndSampleLocation >> 4;
			unit = unitMolL ? UNIT_mol_L : UNIT_kg_L;
		}

		GlucoseStatus status = null;
		if (sensorStatusAnnunciationPresent) {
			final int value = data.getIntValue(Data.FORMAT_UINT16, offset);
			// offset += 2;

			status = new GlucoseStatus(value);
		}

		onGlucoseMeasurementReceived(device, sequenceNumber, baseTime /* with offset */,
				glucoseConcentration, unit, type, sampleLocation, status, contextInformationFollows);
	}
}
