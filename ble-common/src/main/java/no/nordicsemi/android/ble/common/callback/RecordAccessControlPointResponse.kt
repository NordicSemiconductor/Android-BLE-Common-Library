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

package no.nordicsemi.android.ble.common.callback

import android.bluetooth.BluetoothDevice
import android.os.Parcel
import android.os.Parcelable
import no.nordicsemi.android.ble.common.profile.RecordAccessControlPointCallback

import no.nordicsemi.android.ble.exception.InvalidDataException
import no.nordicsemi.android.ble.exception.RequestFailedException

/**
 * Response class that could be used as a result of a synchronous request.
 * The data received are available through getters, instead of a callback.
 *
 *
 * Usage example:
 * <pre>
 * try {
 * RecordAccessControlPointResponse response = readCharacteristic(characteristic)
 * .awaitValid(RecordAccessControlPointResponse.class);
 * if (response.isOperationCompleted() && response.wereRecordsFound()) {
 * int number = response.getNumberOfRecords();
 * ...
 * }
 * ...
 * } catch ([RequestFailedException] e) {
 * Log.w(TAG, "Request failed with status " + e.getStatus(), e);
 * } catch ([InvalidDataException] e) {
 * Log.w(TAG, "Invalid data received: " + e.getResponse().getRawData());
 * }
</pre> *
 *
 */
class RecordAccessControlPointResponse : RecordAccessControlPointDataCallback, Parcelable {
    /**
     * Returns true if the operation has completed successfully.
     *
     * @return True in case of success, false if an error was reported. The error code can be
     * obtained using [.getErrorCode].
     */
    var isOperationCompleted: Boolean = false
        private set
    private var recordsFound: Boolean = false
    /**
     * Returns number of records found matching filter criteria. This is only valid if
     * [.RACP_OP_CODE_REPORT_NUMBER_OF_RECORDS] request was made.
     *
     * @return Number of records or -1 if not requested.
     */
    var numberOfRecords = -1
        private set
    /**
     * Returned error code. Check RACP_ERROR_* constants.
     *
     * @return The error code.
     */
    var errorCode: Int = 0
        private set
    /**
     * Returns the request Op Code. One of [.RACP_OP_CODE_REPORT_STORED_RECORDS],
     * [.RACP_OP_CODE_DELETE_STORED_RECORDS], [.RACP_OP_CODE_DELETE_STORED_RECORDS] or
     * [.RACP_OP_CODE_REPORT_NUMBER_OF_RECORDS].
     *
     * @return The request Op Code.
     */
    var requestCode: Int = 0
        private set

    constructor() {
        // empty
    }

    // Parcelable
    private constructor(`in`: Parcel) : super(`in`) {
        isOperationCompleted = `in`.readByte().toInt() != 0
        recordsFound = `in`.readByte().toInt() != 0
        numberOfRecords = `in`.readInt()
        errorCode = `in`.readInt()
        requestCode = `in`.readInt()
    }

    override fun onRecordAccessOperationCompleted(device: BluetoothDevice, requestCode: Int) {
        this.isOperationCompleted = true
        this.recordsFound = true
        this.requestCode = requestCode
    }

    override fun onRecordAccessOperationCompletedWithNoRecordsFound(
        device: BluetoothDevice,
        requestCode: Int
    ) {
        this.isOperationCompleted = true
        this.numberOfRecords = 0
        this.recordsFound = false
        this.requestCode = requestCode
    }

    override fun onNumberOfRecordsReceived(device: BluetoothDevice, numberOfRecords: Int) {
        this.isOperationCompleted = true
        this.numberOfRecords = numberOfRecords
        this.recordsFound = numberOfRecords > 0
        this.requestCode = RecordAccessControlPointCallback.RACP_OP_CODE_REPORT_NUMBER_OF_RECORDS
    }

    override fun onRecordAccessOperationError(
        device: BluetoothDevice,
        requestCode: Int,
        errorCode: Int
    ) {
        this.isOperationCompleted = false
        this.errorCode = errorCode
        this.requestCode = requestCode
    }

    /**
     * Returns false if operation completed with error [.RACP_ERROR_NO_RECORDS_FOUND],
     * true in other cases.
     *
     * @return True if records were found.
     */
    fun wereRecordsFound(): Boolean {
        return recordsFound
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        super.writeToParcel(dest, flags)
        dest.writeByte((if (isOperationCompleted) 1 else 0).toByte())
        dest.writeByte((if (recordsFound) 1 else 0).toByte())
        dest.writeInt(numberOfRecords)
        dest.writeInt(errorCode)
        dest.writeInt(requestCode)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<RecordAccessControlPointResponse> =
            object : Parcelable.Creator<RecordAccessControlPointResponse> {
                override fun createFromParcel(`in`: Parcel): RecordAccessControlPointResponse {
                    return RecordAccessControlPointResponse(`in`)
                }

                override fun newArray(size: Int): Array<RecordAccessControlPointResponse?> {
                    return arrayOfNulls(size)
                }
            }
    }
}
