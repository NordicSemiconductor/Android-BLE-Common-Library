package no.nordicsemi.android.ble.common.callback.rsc;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.support.annotation.NonNull;

import no.nordicsemi.android.ble.callback.profile.ProfileReadResponse;
import no.nordicsemi.android.ble.common.profile.rsc.RunningSpeedAndCadenceMeasurementCallback;
import no.nordicsemi.android.ble.data.Data;

/**
 * Data callback that parses value into RSC Measurement data.
 * If the value received do not match required syntax
 * {@link #onInvalidDataReceived(BluetoothDevice, Data)} callback will be called.
 * will be called.
 * See: https://www.bluetooth.com/specifications/gatt/viewer?attributeXmlFile=org.bluetooth.characteristic.rsc_measurement.xml
 */
@SuppressWarnings("WeakerAccess")
public abstract class RunningSpeedAndCadenceMeasurementDataCallback extends ProfileReadResponse
		implements RunningSpeedAndCadenceMeasurementCallback {

	public RunningSpeedAndCadenceMeasurementDataCallback() {
		// empty
	}

	protected RunningSpeedAndCadenceMeasurementDataCallback(final Parcel in) {
		super(in);
	}

	@Override
	public void onDataReceived(@NonNull final BluetoothDevice device, @NonNull final Data data) {
		super.onDataReceived(device, data);

		if (data.size() < 4) {
			onInvalidDataReceived(device, data);
			return;
		}

		int offset = 0;
		final int flags = data.getIntValue(Data.FORMAT_UINT8, offset);
		final boolean instantaneousStrideLengthPresent = (flags & 0x01) != 0;
		final boolean totalDistancePresent = (flags & 0x02) != 0;
		final boolean statusRunning = (flags & 0x04) != 0;
		offset += 1;

		final float speed = data.getIntValue(Data.FORMAT_UINT16, offset) / 256.f; // [m/s]
		offset += 2;
		final int cadence = data.getIntValue(Data.FORMAT_UINT8, offset);
		offset += 1;

		if (data.size() < 4
				+ (instantaneousStrideLengthPresent ? 2 : 0)
				+ (totalDistancePresent ? 4 : 0)) {
			onInvalidDataReceived(device, data);
			return;
		}

		Integer strideLength = null;
		if (instantaneousStrideLengthPresent) {
			strideLength = data.getIntValue(Data.FORMAT_UINT16, offset);
			offset += 2;
		}

		Long totalDistance = null;
		if (totalDistancePresent) {
			totalDistance = data.getLongValue(Data.FORMAT_UINT32, offset);
			// offset += 4;
		}

		onRSCMeasurementReceived(device, statusRunning, speed, cadence, strideLength, totalDistance);
	}
}
