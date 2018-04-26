package no.nordicsemi.android.ble.common.callback.glucose;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;

import no.nordicsemi.android.ble.callback.profile.ProfileDataCallback;
import no.nordicsemi.android.ble.common.profile.glucose.GlucoseFeatureCallback;
import no.nordicsemi.android.ble.data.Data;

@SuppressWarnings("ConstantConditions")
public abstract class GlucoseFeatureDataCallback implements ProfileDataCallback, GlucoseFeatureCallback {

	@Override
	public void onDataReceived(@NonNull final BluetoothDevice device, @NonNull final Data data) {
		if (data.size() != 2) {
			onInvalidDataReceived(device, data);
			return;
		}

		final int value = data.getIntValue(Data.FORMAT_UINT16, 0);
		final GlucoseFeatures features = new GlucoseFeatures(value);
		onGlucoseFeaturesReceived(device, features);
	}
}
