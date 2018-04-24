package no.nordicsemi.android.ble.common.callback;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;

import org.junit.Test;

import no.nordicsemi.android.ble.callback.DataCallback;
import no.nordicsemi.android.ble.data.Data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@SuppressWarnings("ConstantConditions")
public class RecordAccessControlPointDataCallbackTest {
	private boolean success;
	private boolean successNoRecords;
	private boolean invalidData;
	private int error;
	private int numberOfRecords;

	private final DataCallback callback = new RecordAccessControlPointDataCallback() {
		@Override
		public void onDataReceived(@NonNull final BluetoothDevice device, @NonNull final Data data) {
			// Reset flags
			RecordAccessControlPointDataCallbackTest.this.success = false;
			RecordAccessControlPointDataCallbackTest.this.successNoRecords = false;
			RecordAccessControlPointDataCallbackTest.this.invalidData = false;
			RecordAccessControlPointDataCallbackTest.this.error = 0;
			RecordAccessControlPointDataCallbackTest.this.numberOfRecords = 0;

			super.onDataReceived(device, data);
		}

		@Override
		public void onRecordAccessOperationCompleted(@NonNull final BluetoothDevice device) {
			RecordAccessControlPointDataCallbackTest.this.success = true;
		}

		@Override
		public void onRecordAccessOperationCompletedWithNoRecordsFound(@NonNull final BluetoothDevice device) {
			RecordAccessControlPointDataCallbackTest.this.successNoRecords = true;
		}

		@Override
		public void onNumberOfRecordsReceived(@NonNull final BluetoothDevice device, final int numberOfRecords) {
			RecordAccessControlPointDataCallbackTest.this.numberOfRecords = numberOfRecords;
		}

		@Override
		public void onRecordAccessOperationError(@NonNull final BluetoothDevice device, final int errorCode) {
			RecordAccessControlPointDataCallbackTest.this.error = errorCode;
		}

		@Override
		public void onInvalidDataReceived(@NonNull final BluetoothDevice device, @NonNull final Data data) {
			RecordAccessControlPointDataCallbackTest.this.invalidData = true;
		}
	};

	@Test
	public void onRecordAccessOperationCompleted() {
		final Data data = new Data(new byte[] { 6, 0, 1 });
		callback.onDataReceived(null, data);
		assertTrue(success);
	}

	@Test
	public void onRecordAccessOperationError_opCodeNotSupported() {
		final Data data = new Data(new byte[] { 6, 0, 2 });
		callback.onDataReceived(null, data);
		assertEquals(error, 2);
	}

	@Test
	public void onRecordAccessOperationError_invalidOperator() {
		final Data data = new Data(new byte[] { 6, 0, 3 });
		callback.onDataReceived(null, data);
		assertEquals(error, 3);
	}

	@Test
	public void onRecordAccessOperationError_operatorNotSupported() {
		final Data data = new Data(new byte[] { 6, 0, 4 });
		callback.onDataReceived(null, data);
		assertEquals(error, 4);
	}

	@Test
	public void onRecordAccessOperationError_invalidOperand() {
		final Data data = new Data(new byte[] { 6, 0, 5 });
		callback.onDataReceived(null, data);
		assertEquals(error, 5);
	}

	@Test
	public void onRecordAccessOperationCompletedWithNoRecords() {
		final Data data = new Data(new byte[] { 6, 0, 6 });
		callback.onDataReceived(null, data);
		assertTrue(successNoRecords);
	}

	@Test
	public void onRecordAccessOperationError_abortUnsuccessful() {
		final Data data = new Data(new byte[] { 6, 0, 7 });
		callback.onDataReceived(null, data);
		assertEquals(error, 7);
	}

	@Test
	public void onRecordAccessOperationError_procedureNotCompleted() {
		final Data data = new Data(new byte[] { 6, 0, 8 });
		callback.onDataReceived(null, data);
		assertEquals(error, 8);
	}

	@Test
	public void onRecordAccessOperationError_operandNotSupported() {
		final Data data = new Data(new byte[] { 6, 0, 9 });
		callback.onDataReceived(null, data);
		assertEquals(error, 9);
	}

	@Test
	public void onRecordAccessOperationError_unknownErrorCode() {
		final Data data = new Data(new byte[] { 6, 0, 10 });
		callback.onDataReceived(null, data);
		assertEquals(error, 10);
	}

	@Test
	public void onNumberOfRecordsReceived_uint8() {
		final Data data = new Data(new byte[] { 5, 0, 1 });
		callback.onDataReceived(null, data);
		assertEquals(numberOfRecords, 1);
	}

	@Test
	public void onNumberOfRecordsReceived_uint16() {
		final Data data = new Data(new byte[] { 5, 0, 2, 1 });
		callback.onDataReceived(null, data);
		assertEquals(numberOfRecords, 258);
	}

	@Test
	public void onNumberOfRecordsReceived_uint32() {
		final Data data = new Data(new byte[] { 5, 0, 4, 3, 2, 1 });
		callback.onDataReceived(null, data);
		assertEquals(numberOfRecords, 16909060);
	}

	@Test
	public void onNumberOfRecordsReceived_unsupportedLength() {
		final Data data = new Data(new byte[] { 5, 0, 3, 2, 1 });
		callback.onDataReceived(null, data);
		assertTrue(invalidData);
	}

	@Test
	public void onInvalidDataReceived_tooShort() {
		final Data data = new Data(new byte[] { 1, 1 });
		callback.onDataReceived(null, data);
		assertTrue(invalidData);
	}

	@Test
	public void onInvalidDataReceived_wrongOpCode() {
		final Data data = new Data(new byte[] { 1, 3, 1, 2, 0 });
		callback.onDataReceived(null, data);
		assertTrue(invalidData);
	}

	@Test
	public void onInvalidDataReceived_invalidOperator() {
		final Data data = new Data(new byte[] { 6, 1, 1 });
		callback.onDataReceived(null, data);
		assertTrue(invalidData);
	}
}