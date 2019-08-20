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

package no.nordicsemi.android.ble.common.profile

import android.bluetooth.BluetoothDevice

import androidx.annotation.IntDef
import androidx.annotation.IntRange

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

interface RecordAccessControlPointCallback {

    /**
     * Callback called when the request has finished successfully, that is
     * all requested records were reported or deleted, or the operation has aborted,
     * depending on the request.
     *
     * @param device      the target device.
     * @param requestCode the request code that has completed, one of RACP_OP_CODE_* constants.
     */
    fun onRecordAccessOperationCompleted(
        device: BluetoothDevice,
        @RACPOpCode requestCode: Int
    )

    /**
     * Callback called when the request to report or delete records has finished
     * successfully, but no records were found matching given filter criteria.
     *
     * @param device      the target device.
     * @param requestCode the request code that has completed, one of RACP_OP_CODE_* constants.
     */
    fun onRecordAccessOperationCompletedWithNoRecordsFound(
        device: BluetoothDevice,
        @RACPOpCode requestCode: Int
    )

    /**
     * Callback called as a result to 'Report number of stored records' request, also when there
     * were no records found.
     *
     * @param device          the target device.
     * @param numberOfRecords the number of records matching given filter criteria.
     */
    fun onNumberOfRecordsReceived(
        device: BluetoothDevice,
        @IntRange(from = 0) numberOfRecords: Int
    )

    /**
     * Callback called in case an error has been returned from the Record Access Control Point
     * characteristic.
     *
     *
     * The 'No records found' error is returned as
     * [.onRecordAccessOperationCompletedWithNoRecordsFound] instead.
     *
     * @param device      the target device.
     * @param requestCode the request code that has finished with an error. One of RACP_OP_CODE_*
     * constants, or other if such requested.
     * @param errorCode   the error code, one of RACP_ERROR_* constants, or other (unknown)
     * is such was reported.
     */
    fun onRecordAccessOperationError(
        device: BluetoothDevice,
        @RACPOpCode requestCode: Int,
        @RACPErrorCode errorCode: Int
    )

    @Retention(RetentionPolicy.SOURCE)
    @IntDef(value = [RACP_OP_CODE_REPORT_STORED_RECORDS, RACP_OP_CODE_DELETE_STORED_RECORDS, RACP_OP_CODE_ABORT_OPERATION, RACP_OP_CODE_REPORT_NUMBER_OF_RECORDS])
    annotation class RACPOpCode

    @Retention(RetentionPolicy.SOURCE)
    @IntDef(value = [RACP_ERROR_OP_CODE_NOT_SUPPORTED, RACP_EEROR_INVALID_OPERATOR, RACP_ERROR_OPERATOR_NOT_SUPPORTED, RACP_ERROR_INVALID_OPERAND, RACP_ERROR_ABORT_UNSUCCESSFUL, RACP_ERROR_PROCEDURE_NOT_COMPLETED, RACP_ERROR_OPERAND_NOT_SUPPORTED])
    annotation class RACPErrorCode

    companion object {

        const val RACP_OP_CODE_REPORT_STORED_RECORDS = 1
        const val RACP_OP_CODE_DELETE_STORED_RECORDS = 2
        const val RACP_OP_CODE_ABORT_OPERATION = 3
        const val RACP_OP_CODE_REPORT_NUMBER_OF_RECORDS = 4
        // int RACP_RESPONSE_SUCCESS = 1;
        const val RACP_ERROR_OP_CODE_NOT_SUPPORTED = 2
        const val RACP_EEROR_INVALID_OPERATOR = 3
        const val RACP_ERROR_OPERATOR_NOT_SUPPORTED = 4
        const val RACP_ERROR_INVALID_OPERAND = 5
        // int RACP_ERROR_NO_RECORDS_FOUND = 6;
        const val RACP_ERROR_ABORT_UNSUCCESSFUL = 7
        const val RACP_ERROR_PROCEDURE_NOT_COMPLETED = 8
        const val RACP_ERROR_OPERAND_NOT_SUPPORTED = 9
    }
}
