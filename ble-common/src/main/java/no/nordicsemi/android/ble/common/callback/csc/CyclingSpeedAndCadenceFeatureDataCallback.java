package no.nordicsemi.android.ble.common.callback.csc;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.support.annotation.NonNull;

import no.nordicsemi.android.ble.callback.profile.ProfileReadResponse;
import no.nordicsemi.android.ble.common.profile.csc.CyclingSpeedAndCadenceFeatureCallback;
import no.nordicsemi.android.ble.data.Data;

/**
 * Data callback that parses value into CSC Feature data.
 * If the value received do not match required syntax
 * {@link #onInvalidDataReceived(BluetoothDevice, Data)} callback will be called.
 * will be called.
 * See: https://www.bluetooth.com/specifications/gatt/viewer?attributeXmlFile=org.bluetooth.characteristic.csc_feature.xml
 */
@SuppressWarnings("WeakerAccess")
public abstract class CyclingSpeedAndCadenceFeatureDataCallback extends ProfileReadResponse implements CyclingSpeedAndCadenceFeatureCallback {

	public CyclingSpeedAndCadenceFeatureDataCallback() {
		// empty
	}

	protected CyclingSpeedAndCadenceFeatureDataCallback(final Parcel in) {
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
		final CSCFeatures features = new CSCFeatures(value);
		onCyclingSpeedAndCadenceFeaturesReceived(features);
	}
}
