package no.nordicsemi.android.ble.common.profile.bp;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Calendar;

public interface IntermediateCuffPressureCallback extends BloodPressureTypes {

	/**
	 * Callback called when Intermediate Cuff Pressure packet has been received.
	 * Use {@link #toKPa(float, int)} or {@link #toMmHg(float, int)} to convert pressure units.
	 *
	 * @param device       the target device.
	 * @param cuffPressure the cuff pressure.
	 * @param unit         the measurement unit, one of {@link #UNIT_mmHg} or {@link #UNIT_kPa}.
	 * @param pulseRate    an optional pulse rate in beats per minute.
	 * @param userID       an optional user ID. Value 255 means 'unknown user'.
	 * @param status       an optional measurement status.
	 * @param calendar     an optional measurement timestamp.
	 */
	void onIntermediateCuffPressureReceived(@NonNull final BluetoothDevice device, final float cuffPressure, final int unit,
											@Nullable final Float pulseRate, @Nullable final Integer userID,
											@Nullable final BPMStatus status, @Nullable final Calendar calendar);
}
