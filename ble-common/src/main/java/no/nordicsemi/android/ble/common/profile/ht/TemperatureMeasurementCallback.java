package no.nordicsemi.android.ble.common.profile.ht;

import android.support.annotation.Nullable;

import java.util.Calendar;

@SuppressWarnings("unused")
public interface TemperatureMeasurementCallback extends HealthThermometerTypes {

	/**
	 * Callback called when Temperature Measurement or Intermediate Temperature
	 * characteristic has changed its value.
	 *
	 * @param temperature current temperature as received.
	 * @param unit temperature value (one of {@link #UNIT_C} or {@link #UNIT_F}).
	 * @param calendar optional timestamp.
	 * @param type optional type where the temperature was measured, see TYPE_* constants.
	 */
	void onTemperatureMeasurementReceived(final float temperature, final int unit,
										  @Nullable final Calendar calendar,
										  @Nullable final Integer type);
}
