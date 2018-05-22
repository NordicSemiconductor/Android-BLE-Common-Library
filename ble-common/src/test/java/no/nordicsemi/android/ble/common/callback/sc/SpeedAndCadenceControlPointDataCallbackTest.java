package no.nordicsemi.android.ble.common.callback.sc;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;

import org.junit.Test;

import no.nordicsemi.android.ble.callback.profile.ProfileReadResponse;
import no.nordicsemi.android.ble.data.MutableData;

import static org.junit.Assert.*;

@SuppressWarnings("ConstantConditions")
public class SpeedAndCadenceControlPointDataCallbackTest {
	private boolean success;
	private int requestCode;
	private int errorCode;
	private int[] locations;

	private final ProfileReadResponse response = new SpeedAndCadenceControlPointDataCallback() {
		@Override
		public void onSCOperationCompleted(@NonNull final BluetoothDevice device, final int requestCode) {
			SpeedAndCadenceControlPointDataCallbackTest.this.success = true;
			SpeedAndCadenceControlPointDataCallbackTest.this.requestCode = requestCode;
		}

		@Override
		public void onSCOperationError(@NonNull final BluetoothDevice device, final int requestCode, final int errorCode) {
			SpeedAndCadenceControlPointDataCallbackTest.this.success = false;
			SpeedAndCadenceControlPointDataCallbackTest.this.errorCode = errorCode;
			SpeedAndCadenceControlPointDataCallbackTest.this.requestCode = requestCode;
		}

		@Override
		public void onSupportedSensorLocationsReceived(@NonNull final BluetoothDevice device, @NonNull final int[] locations) {
			SpeedAndCadenceControlPointDataCallbackTest.this.success = true;
			SpeedAndCadenceControlPointDataCallbackTest.this.requestCode = SC_OP_CODE_REQUEST_SUPPORTED_SENSOR_LOCATIONS;
			SpeedAndCadenceControlPointDataCallbackTest.this.locations = locations;
		}
	};

	@Test
	public void onSCOperationCompleted() {
		final MutableData data = new MutableData(new byte[] { 0x10, 0x01, 0x01});
		response.onDataReceived(null, data);
		assertTrue(success);
		assertEquals(0, errorCode);
		assertEquals(1, requestCode);
		assertNull(locations);
	}

	@Test
	public void onSCOperationError() {
		final MutableData data = new MutableData(new byte[] { 0x10, 0x02, 0x02});
		response.onDataReceived(null, data);
		assertFalse(success);
		assertEquals(2, errorCode);
		assertEquals(2, requestCode);
		assertNull(locations);
	}

	@Test
	public void onSupportedSensorLocationsReceived() {
		final MutableData data = new MutableData(new byte[] { 0x10, 0x04, 0x01, 1, 2, 3});
		response.onDataReceived(null, data);
		assertTrue(success);
		assertEquals(0, errorCode);
		assertEquals(4, requestCode);
		assertNotNull(locations);
		assertEquals(3, locations.length);
		assertEquals(2, locations[1]);
	}

	@Test
	public void onInvalidDataReceived() {
		final MutableData data = new MutableData(new byte[] { 0x01, 0x01, 0x00, 0x00, 0x00});
		response.onDataReceived(null, data);
		assertFalse(success);
		assertEquals(0, errorCode);
		assertFalse(response.isValid());
		assertEquals(0, requestCode);
		assertNull(locations);
	}
}