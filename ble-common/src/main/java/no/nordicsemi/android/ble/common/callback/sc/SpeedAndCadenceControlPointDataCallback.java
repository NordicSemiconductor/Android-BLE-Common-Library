package no.nordicsemi.android.ble.common.callback.sc;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.support.annotation.NonNull;

import no.nordicsemi.android.ble.callback.profile.ProfileReadResponse;
import no.nordicsemi.android.ble.common.profile.sc.SpeedAndCadenceControlPointCallback;
import no.nordicsemi.android.ble.data.Data;

/**
 * Data callback that parses value into SC Control Point data.
 * If the value received do not match required syntax
 * {@link #onInvalidDataReceived(BluetoothDevice, Data)} callback will be called.
 * will be called.
 * See: https://www.bluetooth.com/specifications/gatt/viewer?attributeXmlFile=org.bluetooth.characteristic.sc_control_point.xml
 */
@SuppressWarnings({"WeakerAccess", "ConstantConditions"})
public abstract class SpeedAndCadenceControlPointDataCallback extends ProfileReadResponse implements SpeedAndCadenceControlPointCallback {
	private final static int SC_OP_CODE_RESPONSE_CODE = 16;
	private final static int SC_RESPONSE_SUCCESS = 1;

	public SpeedAndCadenceControlPointDataCallback() {
		// empty
	}

	protected SpeedAndCadenceControlPointDataCallback(final Parcel in) {
		super(in);
	}

	@Override
	public void onDataReceived(@NonNull final BluetoothDevice device, @NonNull final Data data) {
		super.onDataReceived(device, data);

		if (data.size() < 3) {
			onInvalidDataReceived(device, data);
			return;
		}

		final int responseCode = data.getIntValue(Data.FORMAT_UINT8, 0);
		final int requestCode = data.getIntValue(Data.FORMAT_UINT8, 1);
		final int status = data.getIntValue(Data.FORMAT_UINT8, 2);

		if (responseCode != SC_OP_CODE_RESPONSE_CODE) {
			onInvalidDataReceived(device, data);
			return;
		}

		if (status != SC_RESPONSE_SUCCESS) {
			onSCOperationError(device, requestCode, status);
			return;
		}

		switch (requestCode) {
			case SC_OP_CODE_REQUEST_SUPPORTED_SENSOR_LOCATIONS: {
				final int size = data.size() - 3;
				final int[] locations = new int[size];
				for (int i = 0; i < size; ++i) {
					locations[i] = data.getIntValue(Data.FORMAT_UINT8, 3 + i);
				}
				onSupportedSensorLocationsReceived(device, locations);
				break;
			}
			default: {
				onSCOperationCompleted(device, requestCode);
				break;
			}
		}
	}
}
