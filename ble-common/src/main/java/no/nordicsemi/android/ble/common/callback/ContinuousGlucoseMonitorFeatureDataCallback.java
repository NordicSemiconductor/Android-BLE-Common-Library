package no.nordicsemi.android.ble.common.callback;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;

import no.nordicsemi.android.ble.callback.profile.ProfileDataCallback;
import no.nordicsemi.android.ble.common.profile.ContinuousGlucoseMonitorFeatureCallback;
import no.nordicsemi.android.ble.common.util.CRC16;
import no.nordicsemi.android.ble.data.Data;

@SuppressWarnings("ConstantConditions")
public abstract class ContinuousGlucoseMonitorFeatureDataCallback implements ProfileDataCallback, ContinuousGlucoseMonitorFeatureCallback {

	@Override
	public void onDataReceived(@NonNull final BluetoothDevice device, @NonNull final Data data) {
		if (data.size() != 6) {
			onInvalidDataReceived(device, data);
			return;
		}

		final int featuresValue = data.getIntValue(Data.FORMAT_UINT24, 0);
		final int typeAndSampleLocation = data.getIntValue(Data.FORMAT_UINT8, 3);
		final int expectedCrc = data.getIntValue(Data.FORMAT_UINT16, 4);

		final CGMFeatures features = new CGMFeatures(featuresValue);
		if (features.e2eCrcSupported) {
			final int actualCrc = CRC16.MCRF4XX(data.getValue(), 0, 4);
			if (actualCrc != expectedCrc) {
				onContinuousGlucoseMonitorFeaturesReceivedWithCrcError(device, data);
				return;
			}
		} else {
			// If the device doesn't support E2E-safety the value of the field shall be set to 0xFFFF.
			if (expectedCrc != 0xFFFF) {
				onInvalidDataReceived(device, data);
				return;
			}
		}

		final int type = typeAndSampleLocation & 0x0F; // least significant nibble
		final int sampleLocation = typeAndSampleLocation >> 4; // most significant nibble

		onContinuousGlucoseMonitorFeaturesReceived(device, features, type, sampleLocation, features.e2eCrcSupported);
	}
}
