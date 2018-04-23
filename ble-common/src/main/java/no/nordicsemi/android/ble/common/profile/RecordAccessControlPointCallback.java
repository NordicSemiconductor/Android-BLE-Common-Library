package no.nordicsemi.android.ble.common.profile;

public interface RecordAccessControlPointCallback {
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
	 */
	void onRecordAccessOperationCompleted();

	/**
	 * Callback called when the request to report or delete records has finished
	 * successfully, but no records were found matching given filter criteria.
	 */
	void onRecordAccessOperationCompletedWithNoRecordsFound();

	/**
	 * Callback called as a result to 'Report number of stored records' request.
	 *
	 * @param numberOfRecords number of records matching given filter criteria.
	 */
	void onNumberOfRecordsReceived(final int numberOfRecords);

	/**
	 * Callback called in case an error has been returned from the Record Access Control Point
	 * characteristic.
	 * <p>
	 * The 'No records found' error is returned as
	 * {@link #onRecordAccessOperationCompletedWithNoRecordsFound()} instead.
	 *
	 * @param errorCode the error code, one of RACP_ERROR_* constants, or other (unknown) is such was reported.
	 */
	void onRecordAccessOperationError(final int errorCode);
}
