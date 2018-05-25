package no.nordicsemi.android.ble.common.callback.rsc;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.support.annotation.NonNull;

import no.nordicsemi.android.ble.callback.profile.ProfileReadResponse;
import no.nordicsemi.android.ble.common.profile.rsc.RunningSpeedAndCadenceFeatureCallback;
import no.nordicsemi.android.ble.data.Data;

/**
 * Data callback that parses value into CSC Feature data.
 * If the value received do not match required syntax
 * {@link #onInvalidDataReceived(BluetoothDevice, Data)} callback will be called.
 * will be called.
 * See: https://www.bluetooth.com/specifications/gatt/viewer?attributeXmlFile=org.bluetooth.characteristic.rsc_feature.xml
 */
@SuppressWarnings({"WeakerAccess", "ConstantConditions"})
public abstract class RunningSpeedAndCadenceFeatureDataCallback extends ProfileReadResponse implements RunningSpeedAndCadenceFeatureCallback {

	public RunningSpeedAndCadenceFeatureDataCallback() {
		// empty
	}

	protected RunningSpeedAndCadenceFeatureDataCallback(final Parcel in) {
		super(in);
	}

	@Override
	public void onDataReceived(@NonNull final BluetoothDevice device, @NonNull final Data data) {
		super.onDataReceived(device, data);

		if (data.size() != 2) {
			onInvalidDataReceived(device, data);
			return;
		}

		final int value = data.getIntValue(Data.FORMAT_UINT16, 0);
		final RSCFeatures features = new RSCFeatures(value);
		onRunningSpeedAndCadenceFeaturesReceived(device, features);
	}
}
