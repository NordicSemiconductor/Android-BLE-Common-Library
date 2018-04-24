package no.nordicsemi.android.ble.common.profile;

import android.support.annotation.NonNull;

import no.nordicsemi.android.ble.data.Data;

@SuppressWarnings("unused")
public interface ContinuousGlucoseMonitorFeatureCallback extends ContinuousGlucoseMonitorTypes {

	/**
	 * Callback called when Continuous Glucose Monitor Feature value was received.
	 *
	 * @param features       supported features.
	 * @param type           sample type, see TYPE_* constants.
	 * @param sampleLocation sample location, see SAMPLE_LOCATION_* constants.
	 * @param secured        true, if the value received was secured with E2E-CRC value and the CRC matched the
	 *                       packet. False, if the {@link CGMFeatures#e2eCrcSupported} feature is not supported.
	 */
	void onContinuousGlucoseMonitorFeaturesReceived(final @NonNull CGMFeatures features,
													final int type, final int sampleLocation,
													final boolean secured);

	/**
	 * Callback called when a CGM Feature packet with E2E field was received but the CRC check has failed.
	 *
	 * @param data CGM Feature packet data that was received, including the CRC field.
	 */
	default void onContinuousGlucoseMonitorFeaturesReceivedWithCrcError(final @NonNull Data data) {
		// ignore
	}
}
