package no.nordicsemi.android.ble.common.data;

import android.support.annotation.NonNull;

import no.nordicsemi.android.ble.data.Data;

@SuppressWarnings("unused")
public final class RecordAccessControlPointData {
	private static final byte OP_CODE_REPORT_STORED_RECORDS = 1;
	private static final byte OP_CODE_DELETE_STORED_RECORDS = 2;
	private static final byte OP_CODE_ABORT_OPERATION = 3;
	private static final byte OP_CODE_REPORT_NUMBER_OF_RECORDS = 4;
	private static final byte OP_CODE_NUMBER_OF_STORED_RECORDS_RESPONSE = 5;
	private static final byte OP_CODE_RESPONSE_CODE = 6;

	private static final byte OPERATOR_NULL = 0;
	private static final byte OPERATOR_ALL_RECORDS = 1;
	private static final byte OPERATOR_LESS_THEN_OR_EQUAL = 2;
	private static final byte OPERATOR_GREATER_THEN_OR_EQUAL = 3;
	private static final byte OPERATOR_WITHING_RANGE = 4;
	private static final byte OPERATOR_FIRST_RECORD = 5;
	private static final byte OPERATOR_LAST_RECORD = 6;

	public enum FilterType {
		TIME_OFFSET(0x01),
		/** Alias of {@link #TIME_OFFSET} */
		SEQUENCE_NUMBER(0x01),
		USER_FACING_TIME(0x02);

		final byte type;

		FilterType(final int type) {
			this.type = (byte) type;
		}
	}

	private RecordAccessControlPointData() {
		// empty private constructor
	}

	public static Data reportAllStoredRecords() {
		return create(OP_CODE_REPORT_STORED_RECORDS, OPERATOR_ALL_RECORDS);
	}

	public static Data reportFirstStoredRecord() {
		return create(OP_CODE_REPORT_STORED_RECORDS, OPERATOR_FIRST_RECORD);
	}

	public static Data reportLastStoredRecord() {
		return create(OP_CODE_REPORT_STORED_RECORDS, OPERATOR_LAST_RECORD);
	}

	public static Data reportStoredRecordsLessThenOrEqualTo(final @NonNull FilterType filter, final int formatType, final int parameter) {
		return create(OP_CODE_REPORT_STORED_RECORDS, OPERATOR_LESS_THEN_OR_EQUAL, filter, formatType, parameter);
	}

	public static Data reportStoredRecordsGreaterThenOrEqualTo(final @NonNull FilterType filter, final int formatType, final int parameter) {
		return create(OP_CODE_REPORT_STORED_RECORDS, OPERATOR_GREATER_THEN_OR_EQUAL, filter, formatType, parameter);
	}

	public static Data reportStoredRecordsFromRange(final @NonNull FilterType filter, final int formatType, final int start, final int end) {
		return create(OP_CODE_REPORT_STORED_RECORDS, OPERATOR_WITHING_RANGE, filter, formatType, start, end);
	}

	public static Data reportStoredRecordsLessThenOrEqualTo(final int sequenceNumber) {
		return create(OP_CODE_REPORT_STORED_RECORDS, OPERATOR_LESS_THEN_OR_EQUAL, FilterType.SEQUENCE_NUMBER, Data.FORMAT_UINT16, sequenceNumber);
	}

	public static Data reportStoredRecordsGreaterThenOrEqualTo(final int sequenceNumber) {
		return create(OP_CODE_REPORT_STORED_RECORDS, OPERATOR_GREATER_THEN_OR_EQUAL, FilterType.SEQUENCE_NUMBER, Data.FORMAT_UINT16, sequenceNumber);
	}

	public static Data reportStoredRecordsFromRange(final int startSequenceNumber, final int endSequenceNumber) {
		return create(OP_CODE_REPORT_STORED_RECORDS, OPERATOR_WITHING_RANGE, FilterType.SEQUENCE_NUMBER, Data.FORMAT_UINT16, startSequenceNumber, endSequenceNumber);
	}

	public static Data deleteAllStoredRecords() {
		return create(OP_CODE_DELETE_STORED_RECORDS, OPERATOR_ALL_RECORDS);
	}

	public static Data deleteFirstStoredRecord() {
		return create(OP_CODE_DELETE_STORED_RECORDS, OPERATOR_FIRST_RECORD);
	}

	public static Data deleteLastStoredRecord() {
		return create(OP_CODE_DELETE_STORED_RECORDS, OPERATOR_LAST_RECORD);
	}

	public static Data deleteStoredRecordsLessThenOrEqualTo(final @NonNull FilterType filter, final int formatType, final int parameter) {
		return create(OP_CODE_DELETE_STORED_RECORDS, OPERATOR_LESS_THEN_OR_EQUAL, filter, formatType, parameter);
	}

	public static Data deleteStoredRecordsGreaterThenOrEqualTo(final @NonNull FilterType filter, final int formatType, final int parameter) {
		return create(OP_CODE_DELETE_STORED_RECORDS, OPERATOR_GREATER_THEN_OR_EQUAL, filter, formatType, parameter);
	}

	public static Data deleteStoredRecordsFromRange(final @NonNull FilterType filter, final int formatType, final int start, final int end) {
		return create(OP_CODE_DELETE_STORED_RECORDS, OPERATOR_WITHING_RANGE, filter, formatType, start, end);
	}

	public static Data deleteStoredRecordsLessThenOrEqualTo(final int sequenceNumber) {
		return create(OP_CODE_DELETE_STORED_RECORDS, OPERATOR_LESS_THEN_OR_EQUAL, FilterType.SEQUENCE_NUMBER, Data.FORMAT_UINT16, sequenceNumber);
	}

	public static Data deleteStoredRecordsGreaterThenOrEqualTo(final int sequenceNumber) {
		return create(OP_CODE_DELETE_STORED_RECORDS, OPERATOR_GREATER_THEN_OR_EQUAL, FilterType.SEQUENCE_NUMBER, Data.FORMAT_UINT16, sequenceNumber);
	}

	public static Data deleteStoredRecordsFromRange(final int startSequenceNumber, final int endSequenceNumber) {
		return create(OP_CODE_DELETE_STORED_RECORDS, OPERATOR_WITHING_RANGE, FilterType.SEQUENCE_NUMBER, Data.FORMAT_UINT16, startSequenceNumber, endSequenceNumber);
	}

	public static Data reportNumberOfAllStoredRecords() {
		return create(OP_CODE_REPORT_NUMBER_OF_RECORDS, OPERATOR_ALL_RECORDS);
	}

	public static Data reportNumberOfStoredRecordsLessThenOrEqualTo(final @NonNull FilterType filter, final int formatType, final int parameter) {
		return create(OP_CODE_REPORT_NUMBER_OF_RECORDS, OPERATOR_LESS_THEN_OR_EQUAL, filter, formatType, parameter);
	}

	public static Data reportNumberOfStoredRecordsGreaterThenOrEqualTo(final @NonNull FilterType filter, final int formatType, final int parameter) {
		return create(OP_CODE_REPORT_NUMBER_OF_RECORDS, OPERATOR_GREATER_THEN_OR_EQUAL, filter, formatType, parameter);
	}

	public static Data reportNumberOfStoredRecordsFromRange(final @NonNull FilterType filter, final int formatType, final int start, final int end) {
		return create(OP_CODE_REPORT_NUMBER_OF_RECORDS, OPERATOR_WITHING_RANGE, filter, formatType, start, end);
	}

	public static Data reportNumberOfStoredRecordsLessThenOrEqualTo(final int sequenceNumber) {
		return create(OP_CODE_REPORT_NUMBER_OF_RECORDS, OPERATOR_LESS_THEN_OR_EQUAL, FilterType.SEQUENCE_NUMBER, Data.FORMAT_UINT16, sequenceNumber);
	}

	public static Data reportNumberOfStoredRecordsGreaterThenOrEqualTo(final int sequenceNumber) {
		return create(OP_CODE_REPORT_NUMBER_OF_RECORDS, OPERATOR_GREATER_THEN_OR_EQUAL, FilterType.SEQUENCE_NUMBER, Data.FORMAT_UINT16, sequenceNumber);
	}

	public static Data reportNumberOfStoredRecordsFromRange(final int startSequenceNumber, final int endSequenceNumber) {
		return create(OP_CODE_REPORT_NUMBER_OF_RECORDS, OPERATOR_WITHING_RANGE, FilterType.SEQUENCE_NUMBER, Data.FORMAT_UINT16, startSequenceNumber, endSequenceNumber);
	}

	public static Data abortOperation() {
		return create(OP_CODE_ABORT_OPERATION, OPERATOR_NULL);
	}

	private static Data create(final byte opCode, final byte operator) {
		return Data.opCode(opCode, operator);
	}

	private static Data create(final byte opCode, final byte operator, final @NonNull FilterType filter, final int formatType, final int... parameters) {
		final int parameterLen = formatType & 0x0F;

		final Data data = new Data(new byte[2 + 1 + parameters.length * parameterLen]);
		data.setByte(opCode, 0);
		data.setByte(operator, 1);
		if (parameters.length > 0) {
			data.setByte(filter.type, 2);
			data.setValue(parameters[0], formatType, 3);
		}
		if (parameters.length == 2) {
			data.setValue(parameters[1], formatType, 3 + parameterLen);
		}
		return data;
	}
}
