package no.nordicsemi.android.ble.common.profile.ht;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Calendar;

@SuppressWarnings("unused")
public interface TemperatureMeasurementCallback extends HealthThermometerTypes {
	int UNIT_C = 0;
	int UNIT_F = 1;

	/**
	 * Callback called when Temperature Measurement or Intermediate Temperature
	 * characteristic has changed its value.
	 * <p>
	 * Use {@link #toCelsius(float, int)} or {@link #toFahrenheit(float, int)} to convert units.
	 *
	 * @param device      the target device.
	 * @param temperature the temperature received in given unit
	 * @param unit        the temperature unit ({@link #UNIT_C} or {@link #UNIT_F}).
	 * @param calendar    an optional timestamp of measurement.
	 * @param type        an optional type where the temperature was measured, see TYPE_* constants.
	 */
	void onTemperatureMeasurementReceived(@NonNull final BluetoothDevice device,
										  final float temperature, final int unit,
										  @Nullable final Calendar calendar,
										  @Nullable final Integer type);

	/**
	 * Converts the value provided in given unit to Celsius degrees.
	 * If the unit is already {@link #UNIT_C} it will be returned as is.
	 *
	 * @param temperature the temperature value in given unit.
	 * @param unit        the unit of the value ({@link #UNIT_C} or {@link #UNIT_F}).
	 * @return Value in C.
	 */
	static float toCelsius(final float temperature, final int unit) {
		if (unit == UNIT_C) {
			return temperature;
		} else {
			return (temperature - 32f) / 1.8f;
		}
	}

	/**
	 * Converts the value provided in given unit to Fahrenheit degrees.
	 * If the unit is already {@link #UNIT_F} it will be returned as is.
	 *
	 * @param temperature the temperature value in given unit.
	 * @param unit        the unit of the value ({@link #UNIT_C} or {@link #UNIT_F}).
	 * @return Value in F.
	 */
	static float toFahrenheit(final float temperature, final int unit) {
		if (unit == UNIT_F) {
			return temperature;
		} else {
			return temperature * 1.8f + 32f;
		}
	}
}
