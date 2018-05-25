package no.nordicsemi.android.ble.common.profile.csc;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;

public interface CyclingSpeedAndCadenceFeatureCallback {

	class CSCFeatures {
		public final boolean wheelRevolutionDataSupported;
		public final boolean crankRevolutionDataSupported;
		public final boolean multipleSensorDataSupported;
		public final int value;

		public CSCFeatures(final int features) {
			this.value = features;

			wheelRevolutionDataSupported = (features & 0x0001) != 0;
			crankRevolutionDataSupported = (features & 0x0002) != 0;
			multipleSensorDataSupported = (features & 0x0004) != 0;
		}
	}

	/**
	 * Method called when the CSC Feature characteristic has been read.
	 *
	 * @param device   the target device.
	 * @param features the device features.
	 */
	void onCyclingSpeedAndCadenceFeaturesReceived(@NonNull final BluetoothDevice device,
												  @NonNull final CSCFeatures features);
}
