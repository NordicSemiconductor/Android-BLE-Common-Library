package no.nordicsemi.android.ble.common.profile;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;

@SuppressWarnings("unused")
public interface RecordAccessControlPointCallback {
	int RACP_OP_CODE_REPORT_STORED_RECORDS = 1;
	int RACP_OP_CODE_DELETE_STORED_RECORDS = 2;
	int RACP_OP_CODE_ABORT_OPERATION = 3;
	int RACP_OP_CODE_REPORT_NUMBER_OF_RECORDS = 4;

	// int RACP_RESPONSE_SUCCESS = 1;
	int RACP_ERROR_OP_CODE_NOT_SUPPORTED = 2;
	int RACP_EEROR_INVALID_OPERATOR = 3;
	int RACP_ERROR_OPERATOR_NOT_SUPPORTED = 4;
	int RACP_ERROR_INVALID_OPERAND = 5;
	// int RACP_ERROR_NO_RECORDS_FOUND = 6;
	int RACP_ERROR_ABORT_UNSUCCESSFUL = 7;
	int RACP_ERROR_PROCEDURE_NOT_COMPLETED = 8;
	int RACP_ERROR_OPERAND_NOT_SUPPORTED = 9;

	/**
	 * Callback called when the request has finished successfully, that is
	 * all requested records were reported or deleted, or the operation has aborted,
	 * depending on the request.
	 *
	 * @param device      target device.
	 * @param requestCode request code that has completed, one of RACP_OP_CODE_* constants.
	 */
	void onRecordAccessOperationCompleted(final @NonNull BluetoothDevice device, final int requestCode);

	/**
	 * Callback called when the request to report or delete records has finished
	 * successfully, but no records were found matching given filter criteria.
	 *
	 * @param device      target device.
	 * @param requestCode request code that has completed, one of RACP_OP_CODE_* constants.
	 */
	void onRecordAccessOperationCompletedWithNoRecordsFound(final @NonNull BluetoothDevice device, final int requestCode);

	/**
	 * Callback called as a result to 'Report number of stored records' request.
	 *
	 * @param device          target device.
	 * @param numberOfRecords number of records matching given filter criteria.
	 */
	void onNumberOfRecordsReceived(final @NonNull BluetoothDevice device, final int numberOfRecords);

	/**
	 * Callback called in case an error has been returned from the Record Access Control Point
	 * characteristic.
	 * <p>
	 * The 'No records found' error is returned as
	 * {@link #onRecordAccessOperationCompletedWithNoRecordsFound(BluetoothDevice, int)} instead.
	 *
	 * @param device      target device.
	 * @param requestCode request code that has finished with an error. One of RACP_OP_CODE_* constants, or other
	 *                    if such requested.
	 * @param errorCode   the error code, one of RACP_ERROR_* constants, or other (unknown) is such was reported.
	 */
	void onRecordAccessOperationError(final @NonNull BluetoothDevice device, final int requestCode, final int errorCode);
}
