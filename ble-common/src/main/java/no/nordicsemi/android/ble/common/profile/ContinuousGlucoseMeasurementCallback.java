package no.nordicsemi.android.ble.common.profile;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import no.nordicsemi.android.ble.data.Data;

public interface ContinuousGlucoseMeasurementCallback extends ContinuousGlucoseMeasurementTypes {

	/**
	 * Callback called when a Continuous Glucose Measurement packet has been received.
	 * If Sensor Status Annunciation was present in the packet,
	 * the {@link #onContinuousGlucoseSensorStatusChanged(CGMStatus, int, boolean)} will be called
	 * immediately afterwards with the same time offset, even if the status hasn't changed since
	 * the last packet.
	 * <p>
	 * If the E2E CRC field was present in the CGM packet, the data has been verified against it.
	 * If CRC check has failed, the {@link #onContinuousGlucoseMeasurementReceivedWithCrcError(Data)}
	 * will be called instead.
	 * </p>
	 * <p>
	 * The Glucose concentration is reported in mg/dL. To convert it to mmol/L use:
	 * <pre>value [mg/dL] = 18.02 * value [mmol/L]</pre>
	 * Note that the conversion factor is compliant to the Continua blood glucose meter specification.
	 * </p>
	 *
	 * @param glucoseConcentration glucose concentration in mg/dL.
	 * @param cgmTrend             optional CGM Trend information, in (mg/dL)/min.
	 * @param cgmQuality           optional CGM Quality information in percent.
	 * @param timeOffset           time offset in minutes since Session Start Time.
	 * @param secured              true if the packet was sent with E2E-CRC value that was verified to match the packet,
	 *                             false if the packet didn't contain CRC field. For a callback in case of
	 *                             invalid CRC value check {@link #onContinuousGlucoseMeasurementReceivedWithCrcError(Data)}.
	 */
	void onContinuousGlucoseMeasurementReceived(final float glucoseConcentration,
												final @Nullable Float cgmTrend, final @Nullable Float cgmQuality,
												final int timeOffset, final boolean secured);

	/**
	 * Callback called whenever the Sensor Status Annunciation field was
	 * present int the CGM packet.
	 * <p>
	 * If the E2E CRC field was present in the CGM packet, the data has been verified against it.
	 * If CRC check has failed, the {@link #onContinuousGlucoseMeasurementReceivedWithCrcError(Data)}
	 * will be called instead.
	 * </p>
	 *
	 * @param status     the status received.
	 * @param timeOffset time offset in minutes since Session Start Time.
	 * @param secured    true if the packet was sent with E2E-CRC value that was verified to match the packet,
	 *                   false if the packet didn't contain CRC field. For a callback in case of
	 *                   invalid CRC value check {@link #onContinuousGlucoseMeasurementReceivedWithCrcError(Data)}.
	 */
	void onContinuousGlucoseSensorStatusChanged(final CGMStatus status, final int timeOffset,
												final boolean secured);

	/**
	 * Callback called when a CGM packet with E2E field was received but the CRC check has failed.
	 *
	 * @param data CGM packet data that was received, including the CRC field.
	 */
	default void onContinuousGlucoseMeasurementReceivedWithCrcError(final @NonNull Data data) {
		// ignore
	}
}
