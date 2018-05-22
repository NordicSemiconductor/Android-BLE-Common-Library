package no.nordicsemi.android.ble.common.profile.csc;

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
	 * @param features device features.
	 */
	void onCyclingSpeedAndCadenceFeaturesReceived(final CSCFeatures features);
}
