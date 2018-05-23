package no.nordicsemi.android.ble.common.callback.cgm;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.support.annotation.NonNull;

import no.nordicsemi.android.ble.callback.profile.ProfileReadResponse;
import no.nordicsemi.android.ble.common.profile.cgm.CGMSessionRunTimeCallback;
import no.nordicsemi.android.ble.common.util.CRC16;
import no.nordicsemi.android.ble.data.Data;

/**
 * Data callback that parses value into CGM Session Run Time data.
 * If the value received do not match required syntax
 * {@link #onInvalidDataReceived(BluetoothDevice, Data)} callback will be called.
 * If the device supports E2E CRC validation and the CRC is not valid, the
 * {@link #onContinuousGlucoseMonitorSessionRunTimeReceivedWithCrcError(BluetoothDevice, Data)}
 * will be called.
 * See: https://www.bluetooth.com/specifications/gatt/viewer?attributeXmlFile=org.bluetooth.characteristic.cgm_session_run_time.xml
 */
@SuppressWarnings({"ConstantConditions", "WeakerAccess"})
public abstract class CGMSessionRunTimeDataCallback extends ProfileReadResponse implements CGMSessionRunTimeCallback {

	public CGMSessionRunTimeDataCallback() {
		// empty
	}

	protected CGMSessionRunTimeDataCallback(final Parcel in) {
		super(in);
	}

	@Override
	public void onDataReceived(@NonNull final BluetoothDevice device, @NonNull final Data data) {
		super.onDataReceived(device, data);

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
