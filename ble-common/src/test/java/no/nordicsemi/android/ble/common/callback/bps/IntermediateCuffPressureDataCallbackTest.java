package no.nordicsemi.android.ble.common.callback.bps;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.junit.Test;

import java.util.Calendar;

import no.nordicsemi.android.ble.callback.DataReceivedCallback;
import no.nordicsemi.android.ble.data.Data;
import no.nordicsemi.android.ble.data.MutableData;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@SuppressWarnings("ConstantConditions")
public class IntermediateCuffPressureDataCallbackTest {

	@Test
	public void onIntermediateCuffPressureReceived_full() {
		final DataReceivedCallback callback = new IntermediateCuffPressureDataCallback() {
			@Override
			public void onIntermediateCuffPressureReceived(@NonNull final BluetoothDevice device,
														   final float cuffPressure, final int unit,
														   @Nullable final Float pulseRate, @Nullable final Integer userID,
														   @Nullable final BPMStatus status, @Nullable final Calendar calendar) {
				assertEquals("Cuff pressure", 4.0, cuffPressure, 0);
				assertEquals("Unit: mmHg", 0, unit);
				assertNotNull("Pulse rate set", pulseRate);
				assertEquals("Pulse rate", 60.0, pulseRate, 0);
				assertNotNull("User ID set", userID);
				assertEquals("User ID", 1, userID.intValue());
				assertNotNull("Status set", status);
				assertTrue(status.bodyMovementDetected);
				assertTrue(status.cuffTooLose);
				assertTrue(status.irregularPulseDetected);
				assertTrue(status.pulseRateInRange);
				assertFalse(status.pulseRateExceedsUpperLimit);
				assertFalse(status.pulseRateIsLessThenLowerLimit);
				assertTrue(status.improperMeasurementPosition);
				assertNotNull("Calendar set", calendar);
				assertFalse(calendar.isSet(Calendar.YEAR));
				assertTrue(calendar.isSet(Calendar.MONTH));
				assertTrue(calendar.isSet(Calendar.DATE));
				assertEquals("Month", Calendar.APRIL, calendar.get(Calendar.MONTH));
				assertEquals("Day", 17, calendar.get(Calendar.DATE));
				assertEquals("Hour", 20, calendar.get(Calendar.HOUR_OF_DAY));
				assertEquals("Minute", 41, calendar.get(Calendar.MINUTE));
				assertEquals("Second", 59, calendar.get(Calendar.SECOND));
				assertEquals("Milliseconds", 0, calendar.get(Calendar.MILLISECOND));
			}

			@Override
			public void onInvalidDataReceived(@NonNull final BluetoothDevice device, @NonNull final Data data) {
				assertEquals("Correct ICP reported as invalid", 1, 2);
			}
		};
		final MutableData data = new MutableData(new byte[19]);
		// Flags
		data.setByte(0b11110, 0);
		// Cuff pressure in mmHg
		data.setValue(4, 0, Data.FORMAT_SFLOAT, 1);
		// Date and Time
		data.setValue(0, Data.FORMAT_UINT16, 7);
		data.setValue(4, Data.FORMAT_UINT8, 9);
		data.setValue(17, Data.FORMAT_UINT8, 10);
		data.setValue(20, Data.FORMAT_UINT8, 11);
		data.setValue(41, Data.FORMAT_UINT8, 12);
		data.setValue(59, Data.FORMAT_UINT8, 13);
		// Pulse rate
		data.setValue(60, 0, Data.FORMAT_SFLOAT, 14);
		// User ID
		data.setValue(1, Data.FORMAT_UINT8, 16);
		// Measurement status
		data.setValue(0b100111, Data.FORMAT_UINT16, 17);

		assertArrayEquals(
				new byte[] { 0x1E, (byte) 0x04, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x04, 0x11, 0x14, 0x29, 0x3B, 0x3C, 0x00, 0x01, 0x27, 0x00 },
				data.getValue()
		);

		callback.onDataReceived(null, data);
	}

	@Test
	public void onIntermediateCuffPressureReceived_some() {
		final DataReceivedCallback callback = new IntermediateCuffPressureDataCallback() {
			@Override
			public void onIntermediateCuffPressureReceived(@NonNull final BluetoothDevice device,
														   final float cuffPressure, final int unit,
														   @Nullable final Float pulseRate, @Nullable final Integer userID,
														   @Nullable final BPMStatus status, @Nullable final Calendar calendar) {
				assertEquals("Cuff pressure", 11.1, cuffPressure, 0.01);
				assertEquals("Unit: kPa", 1, unit);
				assertNotNull("Pulse rate set", pulseRate);
				assertEquals("Pulse rate", 60.0, pulseRate, 0);
				assertNull("User ID not set", userID);
				assertNotNull("Status set", status);
				assertFalse(status.bodyMovementDetected);
				assertTrue(status.cuffTooLose);
				assertFalse(status.irregularPulseDetected);
				assertFalse(status.pulseRateInRange);
				assertFalse(status.pulseRateExceedsUpperLimit);
				assertTrue(status.pulseRateIsLessThenLowerLimit);
				assertFalse(status.improperMeasurementPosition);
				assertNull("Calendar not set", calendar);
			}

			@Override
			public void onInvalidDataReceived(@NonNull final BluetoothDevice device, @NonNull final Data data) {
				assertEquals("Correct ICP reported as invalid", 1, 2);
			}
		};
		final MutableData data = new MutableData(new byte[11]);
		// Flags
		data.setByte(0b10101, 0);
		// Systolic, diastolic and mean AP in mmHg
		data.setValue(111, -1, Data.FORMAT_SFLOAT, 1);
		// Pulse rate
		data.setValue(60, 0, Data.FORMAT_SFLOAT, 7);
		// Measurement status
		data.setValue(0b010010, Data.FORMAT_UINT16, 9);

		assertArrayEquals(
				new byte[] { 0x15, 0x6F, (byte) 0xF0, 0x00, 0x00, 0x00, 0x00, 0x3C, 0x0, 0x12, 0x0 },
				data.getValue()
		);
		callback.onDataReceived(null, data);
	}

	@Test
	public void onIntermediateCuffPressureReceived_minimal() {
		final DataReceivedCallback callback = new IntermediateCuffPressureDataCallback() {
			@Override
			public void onIntermediateCuffPressureReceived(@NonNull final BluetoothDevice device,
														   final float cuffPressure, final int unit,
														   @Nullable final Float pulseRate, @Nullable final Integer userID,
														   @Nullable final BPMStatus status, @Nullable final Calendar calendar) {
				assertEquals("Cuff pressure", 21.5, cuffPressure, 0.01);
				assertEquals("Unit: mmHg", 0, unit);
				assertNull("Pulse rate not set", pulseRate);
				assertNull("User ID not set", userID);
				assertNull("Status not set", status);
				assertNull("Calendar not set", calendar);
			}

			@Override
			public void onInvalidDataReceived(@NonNull final BluetoothDevice device, @NonNull final Data data) {
				assertEquals("Correct ICP reported as invalid", 1, 2);
			}
		};
		final MutableData data = new MutableData(new byte[7]);
		// Flags
		data.setByte(0b00000, 0);
		// Systolic, diastolic and mean AP in mmHg
		data.setValue(215, -1, Data.FORMAT_SFLOAT, 1);

		assertArrayEquals(
				new byte[] { 0x00, (byte) 0xD7, (byte) 0xF0, 0x00, 0x00, 0x00, 0x00 },
				data.getValue()
		);
		callback.onDataReceived(null, data);
	}

	@Test
	public void onInvalidDataReceived_toShort() {
		final DataReceivedCallback callback = new IntermediateCuffPressureDataCallback() {
			@Override
			public void onIntermediateCuffPressureReceived(@NonNull final BluetoothDevice device,
														   final float cuffPressure, final int unit,
														   @Nullable final Float pulseRate, @Nullable final Integer userID,
														   @Nullable final BPMStatus status, @Nullable final Calendar calendar) {
				assertEquals("Invalid data reported as correct", 1, 2);
			}

			@Override
			public void onInvalidDataReceived(@NonNull final BluetoothDevice device, @NonNull final Data data) {
				assertEquals("Invalid ICP", 6, data.size());
			}
		};
		final Data data = new Data(new byte[] { 1, 2, 3, 4, 5, 6 });

		assertArrayEquals(
				new byte[] { 1, 2, 3, 4, 5, 6 },
				data.getValue()
		);
		callback.onDataReceived(null, data);
	}

	@Test
	public void onInvalidDataReceived_noTimestamp() {
		final DataReceivedCallback callback = new IntermediateCuffPressureDataCallback() {
			@Override
			public void onIntermediateCuffPressureReceived(@NonNull final BluetoothDevice device,
														   final float cuffPressure, final int unit,
														   @Nullable final Float pulseRate, @Nullable final Integer userID,
														   @Nullable final BPMStatus status, @Nullable final Calendar calendar) {
				assertEquals("Invalid data reported as correct", 1, 2);
			}

			@Override
			public void onInvalidDataReceived(@NonNull final BluetoothDevice device, @NonNull final Data data) {
				assertEquals("Invalid ICP", 7, data.size());
			}
		};
		final Data data = new Data(new byte[] { 3, 2, 3, 4, 5, 6, 7 });

		assertArrayEquals(
				new byte[] { 3, 2, 3, 4, 5, 6, 7 },
				data.getValue()
		);
		callback.onDataReceived(null, data);
	}
}