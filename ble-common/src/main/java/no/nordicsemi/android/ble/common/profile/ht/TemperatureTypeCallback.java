package no.nordicsemi.android.ble.common.profile.ht;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;

@SuppressWarnings("unused")
public interface TemperatureTypeCallback extends HealthThermometerTypes {

	/**
	 * Methods called when Temperature Type characteristic has been read.
	 *
	 * @param device the target device.
	 * @param type   indicates where the temperature was measured, see TYPE_* constants.
	 */
	void onTemperatureTypeReceived(@NonNull final BluetoothDevice device, final int type);
}
