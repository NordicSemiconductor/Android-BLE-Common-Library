package no.nordicsemi.android.ble.common.callback;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;

import no.nordicsemi.android.ble.callback.profile.ProfileDataCallback;
import no.nordicsemi.android.ble.common.profile.ContinuousGlucoseMeasurementCallback;
import no.nordicsemi.android.ble.data.Data;

public abstract class ContinuousGlucoseMeasurementDataCallback implements ProfileDataCallback, ContinuousGlucoseMeasurementCallback {

	@Override
	public void onDataReceived(@NonNull final BluetoothDevice device, @NonNull final Data data) {

	}
}
