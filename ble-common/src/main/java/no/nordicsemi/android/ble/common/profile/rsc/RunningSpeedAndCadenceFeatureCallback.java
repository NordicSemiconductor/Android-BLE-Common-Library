package no.nordicsemi.android.ble.common.profile.rsc;

@SuppressWarnings("unused")
public interface RunningSpeedAndCadenceFeatureCallback {

	@SuppressWarnings("WeakerAccess")
	class RSCFeatures {
		public final boolean instantaneousStrideLengthMeasurementSupported;
		public final boolean totalDistanceMeasurementSupported;
		public final boolean walkingOrRunningStatusSupported;
		public final boolean calibrationProcedureSupported;
		public final boolean multipleSensorLocationsSupported;
		public final int value;

		public RSCFeatures(final int features) {
			this.value = features;

			instantaneousStrideLengthMeasurementSupported = (features & 0x0001) != 0;
			totalDistanceMeasurementSupported = (features & 0x0002) != 0;
			walkingOrRunningStatusSupported = (features & 0x0004) != 0;
			calibrationProcedureSupported = (features & 0x0008) != 0;
			multipleSensorLocationsSupported = (features & 0x0010) != 0;
		}
	}

	/**
	 * Method called when the RSC Feature characteristic has been read.
	 *
	 * @param features device features.
	 */
	void onRunningSpeedAndCadenceFeaturesReceived(final RSCFeatures features);
}
