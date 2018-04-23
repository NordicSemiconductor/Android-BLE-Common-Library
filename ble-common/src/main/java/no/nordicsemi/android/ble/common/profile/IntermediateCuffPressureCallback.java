package no.nordicsemi.android.ble.common.profile;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Calendar;

public interface IntermediateCuffPressureCallback extends BloodPressureMeasurementTypes {

	/**
	 * Callback called when Intermediate Cuff Pressure packet has been received.
	 *
	 * @param device       target device.
	 * @param cuffPressure cuff pressure.
	 * @param unit         measurement unit, one of {@link #UNIT_mmHG} or {@link #UNIT_kPa}.
	 * @param pulseRate    optional pulse rate in beats per minute.
	 * @param userID       optional user ID. Value 255 means 'unknown user'.
	 * @param status       optional measurement status.
	 * @param calendar     optional measurement timestamp.
	 */
	void onIntermediateCuffPressureReceived(final @NonNull BluetoothDevice device, final float cuffPressure, final int unit,
											final @Nullable Float pulseRate, final @Nullable Integer userID,
											final @Nullable BPMStatus status, final @Nullable Calendar calendar);
}
