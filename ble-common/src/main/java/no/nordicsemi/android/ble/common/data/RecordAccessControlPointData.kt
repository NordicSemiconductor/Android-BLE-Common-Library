/*
 * Copyright (c) 2018, Nordic Semiconductor
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
 * USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package no.nordicsemi.android.ble.common.data

import androidx.annotation.IntRange

import no.nordicsemi.android.ble.data.Data
import no.nordicsemi.android.ble.data.MutableData

object RecordAccessControlPointData {
    private val OP_CODE_REPORT_STORED_RECORDS: Byte = 1
    private val OP_CODE_DELETE_STORED_RECORDS: Byte = 2
    private val OP_CODE_ABORT_OPERATION: Byte = 3
    private val OP_CODE_REPORT_NUMBER_OF_RECORDS: Byte = 4
    private val OP_CODE_NUMBER_OF_STORED_RECORDS_RESPONSE: Byte = 5
    private val OP_CODE_RESPONSE_CODE: Byte = 6

    private val OPERATOR_NULL: Byte = 0
    private val OPERATOR_ALL_RECORDS: Byte = 1
    private val OPERATOR_LESS_THEN_OR_EQUAL: Byte = 2
    private val OPERATOR_GREATER_THEN_OR_EQUAL: Byte = 3
    private val OPERATOR_WITHING_RANGE: Byte = 4
    private val OPERATOR_FIRST_RECORD: Byte = 5
    private val OPERATOR_LAST_RECORD: Byte = 6

    fun reportAllStoredRecords(): Data {
        return create(OP_CODE_REPORT_STORED_RECORDS, OPERATOR_ALL_RECORDS)
    }

    fun reportFirstStoredRecord(): Data {
        return create(OP_CODE_REPORT_STORED_RECORDS, OPERATOR_FIRST_RECORD)
    }

    fun reportLastStoredRecord(): Data {
        return create(OP_CODE_REPORT_STORED_RECORDS, OPERATOR_LAST_RECORD)
    }

    fun reportStoredRecordsLessThenOrEqualTo(
        filter: FilterType,
        @Data.IntFormat formatType: Int,
        parameter: Int
    ): Data {
        return create(
            OP_CODE_REPORT_STORED_RECORDS, OPERATOR_LESS_THEN_OR_EQUAL,
            filter, formatType, parameter
        )
    }

    fun reportStoredRecordsGreaterThenOrEqualTo(
        filter: FilterType,
        @Data.IntFormat formatType: Int,
        parameter: Int
    ): Data {
        return create(
            OP_CODE_REPORT_STORED_RECORDS, OPERATOR_GREATER_THEN_OR_EQUAL,
            filter, formatType, parameter
        )
    }

    fun reportStoredRecordsFromRange(
        filter: FilterType,
        @Data.IntFormat formatType: Int,
        start: Int, end: Int
    ): Data {
        return create(
            OP_CODE_REPORT_STORED_RECORDS, OPERATOR_WITHING_RANGE,
            filter, formatType, start, end
        )
    }

    fun reportStoredRecordsLessThenOrEqualTo(@IntRange(from = 0) sequenceNumber: Int): Data {
        return create(
            OP_CODE_REPORT_STORED_RECORDS, OPERATOR_LESS_THEN_OR_EQUAL,
            FilterType.SEQUENCE_NUMBER, Data.FORMAT_UINT16, sequenceNumber
        )
    }

    fun reportStoredRecordsGreaterThenOrEqualTo(@IntRange(from = 0) sequenceNumber: Int): Data {
        return create(
            OP_CODE_REPORT_STORED_RECORDS, OPERATOR_GREATER_THEN_OR_EQUAL,
            FilterType.SEQUENCE_NUMBER, Data.FORMAT_UINT16, sequenceNumber
        )
    }

    fun reportStoredRecordsFromRange(
        @IntRange(from = 0) startSequenceNumber: Int,
        @IntRange(from = 0) endSequenceNumber: Int
    ): Data {
        return create(
            OP_CODE_REPORT_STORED_RECORDS, OPERATOR_WITHING_RANGE,
            FilterType.SEQUENCE_NUMBER, Data.FORMAT_UINT16,
            startSequenceNumber, endSequenceNumber
        )
    }

    fun deleteAllStoredRecords(): Data {
        return create(OP_CODE_DELETE_STORED_RECORDS, OPERATOR_ALL_RECORDS)
    }

    fun deleteFirstStoredRecord(): Data {
        return create(OP_CODE_DELETE_STORED_RECORDS, OPERATOR_FIRST_RECORD)
    }

    fun deleteLastStoredRecord(): Data {
        return create(OP_CODE_DELETE_STORED_RECORDS, OPERATOR_LAST_RECORD)
    }

    fun deleteStoredRecordsLessThenOrEqualTo(
        filter: FilterType,
        @Data.IntFormat formatType: Int,
        parameter: Int
    ): Data {
        return create(
            OP_CODE_DELETE_STORED_RECORDS, OPERATOR_LESS_THEN_OR_EQUAL,
            filter, formatType, parameter
        )
    }

    fun deleteStoredRecordsGreaterThenOrEqualTo(
        filter: FilterType,
        @Data.IntFormat formatType: Int,
        parameter: Int
    ): Data {
        return create(
            OP_CODE_DELETE_STORED_RECORDS, OPERATOR_GREATER_THEN_OR_EQUAL,
            filter, formatType, parameter
        )
    }

    fun deleteStoredRecordsFromRange(
        filter: FilterType,
        @Data.IntFormat formatType: Int,
        start: Int, end: Int
    ): Data {
        return create(
            OP_CODE_DELETE_STORED_RECORDS, OPERATOR_WITHING_RANGE,
            filter, formatType, start, end
        )
    }

    fun deleteStoredRecordsLessThenOrEqualTo(@IntRange(from = 0) sequenceNumber: Int): Data {
        return create(
            OP_CODE_DELETE_STORED_RECORDS, OPERATOR_LESS_THEN_OR_EQUAL,
            FilterType.SEQUENCE_NUMBER, Data.FORMAT_UINT16, sequenceNumber
        )
    }

    fun deleteStoredRecordsGreaterThenOrEqualTo(@IntRange(from = 0) sequenceNumber: Int): Data {
        return create(
            OP_CODE_DELETE_STORED_RECORDS, OPERATOR_GREATER_THEN_OR_EQUAL,
            FilterType.SEQUENCE_NUMBER, Data.FORMAT_UINT16, sequenceNumber
        )
    }

    fun deleteStoredRecordsFromRange(
        @IntRange(from = 0) startSequenceNumber: Int,
        @IntRange(from = 0) endSequenceNumber: Int
    ): Data {
        return create(
            OP_CODE_DELETE_STORED_RECORDS, OPERATOR_WITHING_RANGE,
            FilterType.SEQUENCE_NUMBER, Data.FORMAT_UINT16,
            startSequenceNumber, endSequenceNumber
        )
    }

    fun reportNumberOfAllStoredRecords(): Data {
        return create(OP_CODE_REPORT_NUMBER_OF_RECORDS, OPERATOR_ALL_RECORDS)
    }

    fun reportNumberOfStoredRecordsLessThenOrEqualTo(
        filter: FilterType,
        @Data.IntFormat formatType: Int,
        parameter: Int
    ): Data {
        return create(
            OP_CODE_REPORT_NUMBER_OF_RECORDS, OPERATOR_LESS_THEN_OR_EQUAL,
            filter, formatType, parameter
        )
    }

    fun reportNumberOfStoredRecordsGreaterThenOrEqualTo(
        filter: FilterType,
        @Data.IntFormat formatType: Int,
        parameter: Int
    ): Data {
        return create(
            OP_CODE_REPORT_NUMBER_OF_RECORDS, OPERATOR_GREATER_THEN_OR_EQUAL,
            filter, formatType, parameter
        )
    }

    fun reportNumberOfStoredRecordsFromRange(
        filter: FilterType,
        @Data.IntFormat formatType: Int,
        start: Int, end: Int
    ): Data {
        return create(
            OP_CODE_REPORT_NUMBER_OF_RECORDS, OPERATOR_WITHING_RANGE,
            filter, formatType, start, end
        )
    }

    fun reportNumberOfStoredRecordsLessThenOrEqualTo(@IntRange(from = 0) sequenceNumber: Int): Data {
        return create(
            OP_CODE_REPORT_NUMBER_OF_RECORDS, OPERATOR_LESS_THEN_OR_EQUAL,
            FilterType.SEQUENCE_NUMBER, Data.FORMAT_UINT16, sequenceNumber
        )
    }

    fun reportNumberOfStoredRecordsGreaterThenOrEqualTo(@IntRange(from = 0) sequenceNumber: Int): Data {
        return create(
            OP_CODE_REPORT_NUMBER_OF_RECORDS, OPERATOR_GREATER_THEN_OR_EQUAL,
            FilterType.SEQUENCE_NUMBER, Data.FORMAT_UINT16, sequenceNumber
        )
    }

    fun reportNumberOfStoredRecordsFromRange(
        @IntRange(from = 0) startSequenceNumber: Int,
        @IntRange(from = 0) endSequenceNumber: Int
    ): Data {
        return create(
            OP_CODE_REPORT_NUMBER_OF_RECORDS, OPERATOR_WITHING_RANGE,
            FilterType.SEQUENCE_NUMBER, Data.FORMAT_UINT16,
            startSequenceNumber, endSequenceNumber
        )
    }

    fun abortOperation(): Data {
        return create(OP_CODE_ABORT_OPERATION, OPERATOR_NULL)
    }

    private fun create(opCode: Byte, operator: Byte): Data {
        return Data.opCode(opCode, operator)
    }

    private fun create(
        opCode: Byte, operator: Byte,
        filter: FilterType,
        @Data.IntFormat formatType: Int,
        vararg parameters: Int
    ): Data {
        val parameterLen = formatType and 0x0F

        val data = MutableData(ByteArray(2 + 1 + parameters.size * parameterLen))
        data.setByte(opCode.toInt(), 0)
        data.setByte(operator.toInt(), 1)
        if (parameters.size > 0) {
            data.setByte(filter.type.toInt(), 2)
            data.setValue(parameters[0], formatType, 3)
        }
        if (parameters.size == 2) {
            data.setValue(parameters[1], formatType, 3 + parameterLen)
        }
        return data
    }

    enum class FilterType(type: Int) {
        TIME_OFFSET(0x01),
        /**
         * Alias of [.TIME_OFFSET]
         */
        SEQUENCE_NUMBER(0x01),
        USER_FACING_TIME(0x02);

        internal val type: Byte

        init {
            this.type = type.toByte()
        }
    }
}// empty private constructor
