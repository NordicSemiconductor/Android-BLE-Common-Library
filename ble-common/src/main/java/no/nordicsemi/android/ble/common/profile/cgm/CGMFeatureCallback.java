package no.nordicsemi.android.ble.common.profile.cgm;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;

import no.nordicsemi.android.ble.data.Data;

@SuppressWarnings("unused")
public interface CGMFeatureCallback extends CGMTypes {

	/**
	 * Callback called when Continuous Glucose Monitor Feature value was received.
	 * <p>
	 * If the E2E CRC field was present in the CGM packet, the data has been verified against it.
	 * If CRC check has failed, the {@link #onContinuousGlucoseMonitorFeaturesReceivedWithCrcError(BluetoothDevice, Data)}
	 * will be called instead.
	 * </p>
	 *
	 * @param device         target device.
	 * @param features       supported features.
	 * @param type           sample type, see TYPE_* constants.
	 * @param sampleLocation sample location, see SAMPLE_LOCATION_* constants.
	 * @param secured        true, if the value received was secured with E2E-CRC value and the CRC matched the
	 *                       packet. False, if the {@link CGMFeatures#e2eCrcSupported} feature is not supported.
	 */
	void onContinuousGlucoseMonitorFeaturesReceived(final @NonNull BluetoothDevice device,
													final @NonNull CGMFeatures features,
													final int type, final int sampleLocation,
													final boolean secured);

	/**
	 * Callback called when a CGM Feature packet with E2E field was received but the CRC check has failed.
	 *
	 * @param device target device.
	 * @param data   CGM Feature packet data that was received, including the CRC field.
	 */
	default void onContinuousGlucoseMonitorFeaturesReceivedWithCrcError(final @NonNull BluetoothDevice device, final @NonNull Data data) {
		// ignore
	}
}
