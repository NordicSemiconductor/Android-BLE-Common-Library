package no.nordicsemi.android.ble.common.callback.cgm;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;

import no.nordicsemi.android.ble.callback.profile.ProfileReadResponse;
import no.nordicsemi.android.ble.common.profile.cgm.CGMFeatureCallback;
import no.nordicsemi.android.ble.common.util.CRC16;
import no.nordicsemi.android.ble.data.Data;

/**
 * Data callback that parses value into CGM Feature data.
 * If the value received do not match required syntax
 * {@link #onInvalidDataReceived(BluetoothDevice, Data)} callback will be called.
 * If the device supports E2E CRC validation and the CRC is not valid, the
 * {@link #onContinuousGlucoseMonitorFeaturesReceivedWithCrcError(BluetoothDevice, Data)}
 * will be called.
 * See: https://www.bluetooth.com/specifications/gatt/viewer?attributeXmlFile=org.bluetooth.characteristic.cgm_feature.xml
 */
@SuppressWarnings("ConstantConditions")
public abstract class CGMFeatureDataCallback extends ProfileReadResponse implements CGMFeatureCallback {

	@Override
	public void onDataReceived(@NonNull final BluetoothDevice device, @NonNull final Data data) {
		super.onDataReceived(device, data);

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
