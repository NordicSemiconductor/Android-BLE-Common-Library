package no.nordicsemi.android.ble.common.profile.cgm;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import no.nordicsemi.android.ble.data.Data;

public interface ContinuousGlucoseMeasurementCallback extends CGMTypes {

	/**
	 * Callback called when a Continuous Glucose Measurement packet has been received.
	 * <p>
	 * If the E2E CRC field was present in the CGM packet, the data has been verified against it.
	 * If CRC check has failed, the {@link #onContinuousGlucoseMeasurementReceivedWithCrcError(BluetoothDevice, Data)}
	 * will be called instead.
	 * </p>
	 * <p>
	 * The Glucose concentration is reported in mg/dL. To convert it to mmol/L use:
	 * <pre>value [mg/dL] = 18.02 * value [mmol/L]</pre>
	 * Note that the conversion factor is compliant to the Continua blood glucose meter specification.
	 * </p>
	 *
	 * @param device               target device.
	 * @param glucoseConcentration glucose concentration in mg/dL.
	 * @param cgmTrend             optional CGM Trend information, in (mg/dL)/min.
	 * @param cgmQuality           optional CGM Quality information in percent.
	 * @param status               the status of the measurement.
	 * @param timeOffset           time offset in minutes since Session Start Time.
	 * @param secured              true if the packet was sent with E2E-CRC value that was verified to match the packet,
	 *                             false if the packet didn't contain CRC field. For a callback in case of
	 *                             invalid CRC value check {@link #onContinuousGlucoseMeasurementReceivedWithCrcError(BluetoothDevice, Data)}.
	 */
	void onContinuousGlucoseMeasurementReceived(final @NonNull BluetoothDevice device, final float glucoseConcentration,
												final @Nullable Float cgmTrend, final @Nullable Float cgmQuality,
												final CGMStatus status, final int timeOffset, final boolean secured);

	/**
	 * Callback called when a CGM packet with E2E field was received but the CRC check has failed.
	 *
	 * @param device target device.
	 * @param data   CGM packet data that was received, including the CRC field.
	 */
	default void onContinuousGlucoseMeasurementReceivedWithCrcError(final @NonNull BluetoothDevice device, final @NonNull Data data) {
		// ignore
	}
}
