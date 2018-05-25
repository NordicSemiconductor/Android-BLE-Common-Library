package no.nordicsemi.android.ble.common.callback.hr;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import no.nordicsemi.android.ble.callback.profile.ProfileReadResponse;
import no.nordicsemi.android.ble.common.profile.hr.HeartRateMeasurementCallback;
import no.nordicsemi.android.ble.data.Data;

/**
 * Data callback that parses value into Heart Rate Measurement data.
 * If the value received do not match required syntax
 * {@link #onInvalidDataReceived(BluetoothDevice, Data)} callback will be called.
 * will be called.
 * See: https://www.bluetooth.com/specifications/gatt/viewer?attributeXmlFile=org.bluetooth.characteristic.heart_rate_measurement.xml
 */
@SuppressWarnings({"WeakerAccess", "ConstantConditions"})
public abstract class HeartRateMeasurementDataCallback extends ProfileReadResponse implements HeartRateMeasurementCallback {

	public HeartRateMeasurementDataCallback() {
		// empty
	}

	protected HeartRateMeasurementDataCallback(final Parcel in) {
		super(in);
	}

	@Override
	public void onDataReceived(@NonNull final BluetoothDevice device, @NonNull final Data data) {
		super.onDataReceived(device, data);

		if (data.size() < 2) {
			onInvalidDataReceived(device, data);
			return;
		}

		// Read flags
		int offset = 0;
		final int flags = data.getIntValue(Data.FORMAT_UINT8, offset);
		final int hearRateType = (flags & 0x01) == 0 ? Data.FORMAT_UINT8 : Data.FORMAT_UINT16;
		final int sensorContactStatus = (flags & 0x06) >> 1;
		final boolean sensorContactSupported = sensorContactStatus == 2 || sensorContactStatus == 3;
		final boolean sensorContactDetected = sensorContactStatus == 3;
		final boolean energyExpandedPresent = (flags & 0x08) != 0;
		final boolean rrIntervalsPresent = (flags & 0x10) != 0;
		offset += 1;

		// Validate packet length
		if (data.size() < 1 + (hearRateType & 0x0F)
				+ (energyExpandedPresent ? 2 : 0)
				+ (rrIntervalsPresent ? 2 : 0)) {
			onInvalidDataReceived(device, data);
			return;
		}

		// Prepare data
		final Boolean sensorContact = sensorContactSupported ? sensorContactDetected : null;

		final int heartRate = data.getIntValue(hearRateType, offset);
		offset += hearRateType & 0xF;

		Integer energyExpanded = null;
		if (energyExpandedPresent) {
			energyExpanded = data.getIntValue(Data.FORMAT_UINT16, offset);
			offset += 2;
		}

		List<Integer> rrIntervals = null;
		if (rrIntervalsPresent) {
			final int count = (data.size() - offset) / 2;
			final List<Integer> intervals = new ArrayList<>(count);
			for (int i = 0; i < count; ++i) {
				intervals.add(data.getIntValue(Data.FORMAT_UINT16, offset));
				offset += 2;
			}
			rrIntervals = Collections.unmodifiableList(intervals);
		}

		onHeartRateMeasurementReceived(device, heartRate, sensorContact, energyExpanded, rrIntervals);
	}
}
