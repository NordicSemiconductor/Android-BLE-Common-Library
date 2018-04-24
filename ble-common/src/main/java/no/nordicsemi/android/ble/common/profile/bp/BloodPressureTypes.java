package no.nordicsemi.android.ble.common.profile.bp;

@SuppressWarnings("WeakerAccess")
public interface BloodPressureTypes {
	int UNIT_mmHG = 0;
	int UNIT_kPa = 1;

	class BPMStatus {
		public boolean bodyMovementDetected;
		public boolean cuffTooLose;
		public boolean irregularPulseDetected;
		public boolean pulseRateInRange;
		public boolean pulseRateExceedsUpperLimit;
		public boolean pulseRateIsLessThenLowerLimit;
		public boolean improperMeasurementPosition;
		public int value;

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
