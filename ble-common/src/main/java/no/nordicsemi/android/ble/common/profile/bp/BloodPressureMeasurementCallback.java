package no.nordicsemi.android.ble.common.profile.bp;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Calendar;

@SuppressWarnings("unused")
public interface BloodPressureMeasurementCallback extends BloodPressureTypes {

	/**
	 * Callback called when Blood Pressure Measurement packet has been received.
	 * Use {@link #toKPa(float, int)} or {@link #toMmHg(float, int)} to convert pressure units.
	 *
	 * @param device               the target device.
	 * @param systolic             the systolic compound of blood pressure measurement.
	 * @param diastolic            the diastolic compound of blood pressure measurement.
	 * @param meanArterialPressure the mean arterial pressure compound of blood pressure measurement.
	 * @param unit                 the measurement unit, one of {@link #UNIT_mmHg} or {@link #UNIT_kPa}.
	 * @param pulseRate            an optional pulse rate in beats per minute.
	 * @param userID               an optional user ID. Value 255 means 'unknown user'.
	 * @param status               an optional measurement status.
	 * @param calendar             an optional measurement timestamp.
	 */
	void onBloodPressureMeasurementReceived(@NonNull final BluetoothDevice device,
											final float systolic, final float diastolic,
											final float meanArterialPressure, final int unit,
											@Nullable final Float pulseRate,
											@Nullable final Integer userID,
											@Nullable final BPMStatus status,
											@Nullable final Calendar calendar);
}
