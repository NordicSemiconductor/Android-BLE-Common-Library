package no.nordicsemi.android.ble.common.profile.ht;

@SuppressWarnings("unused")
public interface TemperatureTypeCallback extends HealthThermometerTypes {

	/**
	 * Methods called when Temperature Type characteristic has been read.
	 *
	 * @param type indicates where the temperature was measured, see TYPE_* constants.
	 */
	void onTemperatureTypeReceived(final int type);
}
