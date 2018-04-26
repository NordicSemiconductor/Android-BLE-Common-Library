package no.nordicsemi.android.ble.common.profile.bp;

@SuppressWarnings("WeakerAccess")
public interface BloodPressureTypes {
	int UNIT_mmHg = 0;
	int UNIT_kPa = 1;

	class BPMStatus {
		public final boolean bodyMovementDetected;
		public final boolean cuffTooLose;
		public final boolean irregularPulseDetected;
		public final boolean pulseRateInRange;
		public final boolean pulseRateExceedsUpperLimit;
		public final boolean pulseRateIsLessThenLowerLimit;
		public final boolean improperMeasurementPosition;
		public final int value;

		public BPMStatus(final int status) {
			this.value = status;

			bodyMovementDetected = (status & 0x01) != 0;
			cuffTooLose = (status & 0x02) != 0;
			irregularPulseDetected = (status & 0x04) != 0;
			pulseRateInRange = (status & 0x18) >> 3 == 0;
			pulseRateExceedsUpperLimit = (status & 0x18) >> 3 == 1;
			pulseRateIsLessThenLowerLimit = (status & 0x18) >> 3 == 2;
			improperMeasurementPosition = (status & 0x20) != 0;
		}
	}
}
