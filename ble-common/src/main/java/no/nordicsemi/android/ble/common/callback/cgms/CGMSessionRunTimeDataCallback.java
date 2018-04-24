package no.nordicsemi.android.ble.common.callback.cgms;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;

import no.nordicsemi.android.ble.callback.profile.ProfileDataCallback;
import no.nordicsemi.android.ble.common.profile.cgm.CGMSessionRunTimeCallback;
import no.nordicsemi.android.ble.common.util.CRC16;
import no.nordicsemi.android.ble.data.Data;

@SuppressWarnings("ConstantConditions")
public abstract class CGMSessionRunTimeDataCallback implements ProfileDataCallback, CGMSessionRunTimeCallback {

	@Override
	public void onDataReceived(@NonNull final BluetoothDevice device, @NonNull final Data data) {
		if (data.size() != 2 && data.size() != 4) {
			onInvalidDataReceived(device, data);
			return;
		}

		final int sessionRunTime = data.getIntValue(Data.FORMAT_UINT16, 0);

		final boolean crcPresent = data.size() == 4;
		if (crcPresent) {
			final int actualCrc = CRC16.MCRF4XX(data.getValue(), 0, 2);
			final int expectedCrc = data.getIntValue(Data.FORMAT_UINT16, 2);
			if (actualCrc != expectedCrc) {
				onContinuousGlucoseMonitorSessionRunTimeReceivedWithCrcError(device, data);
				return;
			}
		}

		onContinuousGlucoseMonitorSessionRunTimeReceived(device, sessionRunTime, crcPresent);
	}
}
