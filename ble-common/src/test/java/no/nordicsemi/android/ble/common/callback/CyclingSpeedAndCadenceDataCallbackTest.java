package no.nordicsemi.android.ble.common.callback;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;

import org.junit.Test;

import no.nordicsemi.android.ble.callback.DataCallback;
import no.nordicsemi.android.ble.data.Data;

import static org.junit.Assert.*;

public class CyclingSpeedAndCadenceDataCallbackTest {

	@Test
	public void onWheelMeasurementReceived() {
		final DataCallback callback = new CyclingSpeedAndCadenceDataCallback() {
			@Override
			protected void onWheelMeasurementReceived(@NonNull final BluetoothDevice device, final long wheelRevolutions, final int lastWheelEventTime) {
				assertEquals("Wheel measurement", 12345, wheelRevolutions);
				assertEquals("Wheel last event time", 1000, lastWheelEventTime);
			}

			@Override
			public void onInvalidDataReceived(@NonNull final BluetoothDevice device, @NonNull final Data data) {
				assertEquals("Correct CSC reported as invalid", 1, 2);
			}

			@Override
			public void onDistanceChanged(@NonNull final BluetoothDevice device, final float totalDistance, final float distance, final float speed) {
				// ignore
			}

			@Override
			public void onCrankDataChanged(@NonNull final BluetoothDevice device, final float crankCadence, final float gearRatio) {
				// ignore
			}
		};
		final Data data = new Data(new byte[7]);
		// Flags
		assertTrue(data.setByte((byte) 0x01, 0));
		// Wheel revolutions
		assertTrue(data.setValue(12345, Data.FORMAT_UINT32, 1));
		assertTrue(data.setValue(1000, Data.FORMAT_UINT16, 5));

		callback.onDataReceived(null, data);
	}

	@Test
	public void onCrankMeasurementReceived() {
		final DataCallback callback = new CyclingSpeedAndCadenceDataCallback() {
			@Override
			protected void onCrankMeasurementReceived(@NonNull final BluetoothDevice device, final int crankRevolutions, final int lastCrankEventTime) {
				assertEquals("Crank measurement", 345, crankRevolutions);
				assertEquals("Crank last event time", 2000, lastCrankEventTime);
			}

			@Override
			public void onInvalidDataReceived(@NonNull final BluetoothDevice device, @NonNull final Data data) {
				assertEquals("Correct CSC data reported as invalid", 1, 2);
			}

			@Override
			public void onDistanceChanged(@NonNull final BluetoothDevice device, final float totalDistance, final float distance, final float speed) {
				// ignore
			}

			@Override
			public void onCrankDataChanged(@NonNull final BluetoothDevice device, final float crankCadence, final float gearRatio) {
				// ignore
			}
		};
		final Data data = new Data(new byte[5]);
		// Flags
		assertTrue(data.setByte((byte) 0x02, 0));
		// Crank revolutions
		assertTrue(data.setValue(345, Data.FORMAT_UINT16, 1));
		assertTrue(data.setValue(2000, Data.FORMAT_UINT16, 3));

		callback.onDataReceived(null, data);
	}

	@Test
	public void onWheelAndCrankMeasurementReceived() {
		final DataCallback callback = new CyclingSpeedAndCadenceDataCallback() {
			@Override
			protected void onWheelMeasurementReceived(@NonNull final BluetoothDevice device, final long wheelRevolutions, final int lastWheelEventTime) {
				assertEquals("Wheel measurement", 12345, wheelRevolutions);
				assertEquals("Wheel last event time", 1000, lastWheelEventTime);
			}

			@Override
			protected void onCrankMeasurementReceived(@NonNull final BluetoothDevice device, final int crankRevolutions, final int lastCrankEventTime) {
				assertEquals("Crank measurement", 345, crankRevolutions);
				assertEquals("Crank last event time", 2000, lastCrankEventTime);
			}

			@Override
			public void onInvalidDataReceived(@NonNull final BluetoothDevice device, @NonNull final Data data) {
				assertEquals("Correct CSC data reported as invalid", 1, 2);
			}

			@Override
			public void onDistanceChanged(@NonNull final BluetoothDevice device, final float totalDistance, final float distance, final float speed) {
				// ignore
			}

			@Override
			public void onCrankDataChanged(@NonNull final BluetoothDevice device, final float crankCadence, final float gearRatio) {
				// ignore
			}
		};
		final Data data = new Data(new byte[1 + 6 + 4]);
		// Flags
		assertTrue(data.setByte((byte) 0x03, 0));
		// Wheel revolutions
		assertTrue(data.setValue(12345, Data.FORMAT_UINT32, 1));
		assertTrue(data.setValue(1000, Data.FORMAT_UINT16, 5));
		// Crank revolutions
		assertTrue(data.setValue(345, Data.FORMAT_UINT16, 7));
		assertTrue(data.setValue(2000, Data.FORMAT_UINT16, 9));

		callback.onDataReceived(null, data);
	}

	@Test
	public void onDistanceChanged() {
		final DataCallback callback = new CyclingSpeedAndCadenceDataCallback() {
			@Override
			public void onInvalidDataReceived(@NonNull final BluetoothDevice device, @NonNull final Data data) {
				assertEquals("Correct CSC data reported as invalid", 1, 2);
			}

			@Override
			public void onDistanceChanged(@NonNull final BluetoothDevice device, final float totalDistance, final float distance, final float speed) {
				assertEquals("Total distance", 2 * 23.4f, totalDistance, 0.01);
				assertEquals("Distance", 23.4, distance, 0.01);
				assertEquals("Speed", 23.4f, speed, 0.01);
			}

			@Override
			public void onCrankDataChanged(@NonNull final BluetoothDevice device, final float crankCadence, final float gearRatio) {
				assertEquals("Crank data not available and reported", 1, 2);
			}
		};
		final Data data = new Data(new byte[7]);
		// Flags
		assertTrue(data.setByte((byte) 0x01, 0));
		// Wheel revolutions
		assertTrue(data.setValue(10, Data.FORMAT_UINT32, 1));
		assertTrue(data.setValue(0, Data.FORMAT_UINT16, 5));

		callback.onDataReceived(null, data);

		// Update wheel revolutions
		assertTrue(data.setValue(20, Data.FORMAT_UINT32, 1));
		assertTrue(data.setValue(1024, Data.FORMAT_UINT16, 5)); // 1 second
		callback.onDataReceived(null, data);
	}

	@Test
	public void onCrankDataChanged_onlyCrankData() {
		final DataCallback callback = new CyclingSpeedAndCadenceDataCallback() {
			@Override
			public void onInvalidDataReceived(@NonNull final BluetoothDevice device, @NonNull final Data data) {
				assertEquals("Correct CSC data reported as invalid", 1, 2);
			}

			@Override
			public void onDistanceChanged(@NonNull final BluetoothDevice device, final float totalDistance, final float distance, final float speed) {
				assertEquals("Crank data not available and reported", 1, 2);
			}

			@Override
			public void onCrankDataChanged(@NonNull final BluetoothDevice device, final float crankCadence, final float gearRatio) {
				assertEquals("Crank cadence", 60.0f, crankCadence, 0);
				assertEquals("Gear ratio", 0.0f, gearRatio, 0); // Gear ration not available, as no wheel data
			}
		};
		final Data data = new Data(new byte[5]);
		// Flags
		assertTrue(data.setByte((byte) 0x02, 0));
		// Crank revolutions
		assertTrue(data.setValue(10, Data.FORMAT_UINT16, 1));
		assertTrue(data.setValue(0, Data.FORMAT_UINT16, 3));

		callback.onDataReceived(null, data);

		// Update crank revolutions
		assertTrue(data.setValue(11, Data.FORMAT_UINT16, 1));
		assertTrue(data.setValue(1024, Data.FORMAT_UINT16, 3)); // 1 second
		callback.onDataReceived(null, data);
	}

	@Test
	public void onCrankDataChanged() {
		final DataCallback callback = new CyclingSpeedAndCadenceDataCallback() {
			@Override
			public void onInvalidDataReceived(@NonNull final BluetoothDevice device, @NonNull final Data data) {
				assertEquals("Correct CSC data reported as invalid", 1, 2);
			}

			@Override
			public void onDistanceChanged(@NonNull final BluetoothDevice device, final float totalDistance, final float distance, final float speed) {
				assertEquals("Total distance", 3 * 23.4f, totalDistance, 0.01);
				assertEquals("Distance", 23.4, distance, 0.01);
				assertEquals("Speed", 23.4f, speed, 0.01);
			}

			@Override
			public void onCrankDataChanged(@NonNull final BluetoothDevice device, final float crankCadence, final float gearRatio) {
				assertEquals("Crank cadence", 60.0f, crankCadence, 0);
				assertEquals("Gear ratio", 10.0f, gearRatio, 0);
			}
		};
		final Data data = new Data(new byte[11]);
		// Flags
		assertTrue(data.setByte((byte) 0x03, 0));
		// Wheel revolutions
		assertTrue(data.setValue(20, Data.FORMAT_UINT32, 1));
		assertTrue(data.setValue(0, Data.FORMAT_UINT16, 5));
		// Crank revolutions
		assertTrue(data.setValue(10, Data.FORMAT_UINT16, 7));
		assertTrue(data.setValue(0, Data.FORMAT_UINT16, 9));

		callback.onDataReceived(null, data);

		// Update wheel revolutions
		assertTrue(data.setValue(30, Data.FORMAT_UINT32, 1));
		assertTrue(data.setValue(1024, Data.FORMAT_UINT16, 5)); // 1 second
		// Update crank revolutions
		assertTrue(data.setValue(11, Data.FORMAT_UINT16, 7));
		assertTrue(data.setValue(1024, Data.FORMAT_UINT16, 9)); // 1 second
		callback.onDataReceived(null, data);
	}

	@Test
	public void onInvalidDataReceived() {
		final DataCallback callback = new CyclingSpeedAndCadenceDataCallback() {
			@Override
			public void onInvalidDataReceived(@NonNull final BluetoothDevice device, @NonNull final Data data) {
				assertEquals("Invalid CSC data", 1, 1);
			}

			@Override
			public void onDistanceChanged(@NonNull final BluetoothDevice device, final float totalDistance, final float distance, final float speed) {
				assertEquals("Invalid data reported as correct", 1, 2);
			}

			@Override
			public void onCrankDataChanged(@NonNull final BluetoothDevice device, final float crankCadence, final float gearRatio) {
				assertEquals("Invalid data reported as correct", 1, 2);
			}
		};
		final Data data = new Data(new byte[9]); // 11 bytes are required
		// Flags
		assertTrue(data.setByte((byte) 0x03, 0));
		// Wheel revolutions
		assertTrue(data.setValue(20, Data.FORMAT_UINT32, 1));
		assertTrue(data.setValue(0, Data.FORMAT_UINT16, 5));
		// Crank revolutions
		assertTrue(data.setValue(10, Data.FORMAT_UINT16, 7));
		assertFalse(data.setValue(0, Data.FORMAT_UINT16, 9));

		callback.onDataReceived(null, data);
	}
}