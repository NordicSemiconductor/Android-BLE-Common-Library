package no.nordicsemi.android.ble.common.callback.cgm;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;

import no.nordicsemi.android.ble.callback.profile.ProfileDataCallback;
import no.nordicsemi.android.ble.common.profile.cgm.CGMStatusCallback;
import no.nordicsemi.android.ble.common.util.CRC16;
import no.nordicsemi.android.ble.data.Data;

@SuppressWarnings("ConstantConditions")
public abstract class CGMStatusDataCallback implements ProfileDataCallback, CGMStatusCallback {

	@Override
	public void onDataReceived(@NonNull final BluetoothDevice device, @NonNull final Data data) {
		if (data.size() != 5 && data.size() != 7) {
			onInvalidDataReceived(device, data);
			return;
		}

		final int timeOffset = data.getIntValue(Data.FORMAT_UINT16, 0);
		final int warningStatus = data.getIntValue(Data.FORMAT_UINT8, 2);
		final int calibrationTempStatus = data.getIntValue(Data.FORMAT_UINT8, 3);
		final int sensorStatus = data.getIntValue(Data.FORMAT_UINT8, 4);

		final boolean crcPresent = data.size() == 7;
		if (crcPresent) {
			final int actualCrc = CRC16.MCRF4XX(data.getValue(), 0, 5);
			final int expectedCrc = data.getIntValue(Data.FORMAT_UINT16, 5);
			if (actualCrc != expectedCrc) {
				onContinuousGlucoseMonitorStatusReceivedWithCrcError(device, data);
				return;
			}
		}

		final CGMStatus status = new CGMStatus(warningStatus, calibrationTempStatus, sensorStatus);
		onContinuousGlucoseMonitorStatusChanged(device, status, timeOffset, crcPresent);
	}
}
