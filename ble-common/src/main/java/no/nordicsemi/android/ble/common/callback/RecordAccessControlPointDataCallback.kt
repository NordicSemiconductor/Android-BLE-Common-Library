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

import no.nordicsemi.android.ble.callback.profile.ProfileReadResponse
import no.nordicsemi.android.ble.common.profile.RecordAccessControlPointCallback
import no.nordicsemi.android.ble.data.Data

/**
 * Record Access Control Point callback that parses received data.
 * If the value does match characteristic specification the
 * [.onInvalidDataReceived] callback will be called.
 * See: https://www.bluetooth.com/specifications/gatt/viewer?attributeXmlFile=org.bluetooth.characteristic.record_access_control_point.xml
 */
abstract class RecordAccessControlPointDataCallback : ProfileReadResponse,
    RecordAccessControlPointCallback {

    constructor() {
        // empty
    }

    protected constructor(`in`: Parcel) : super(`in`)

    override fun onDataReceived(device: BluetoothDevice, data: Data) {
        super.onDataReceived(device, data)

        if (data.size() < 3) {
            onInvalidDataReceived(device, data)
            return
        }

        val opCode = data.getIntValue(Data.FORMAT_UINT8, 0)!!
        if (opCode != OP_CODE_NUMBER_OF_STORED_RECORDS_RESPONSE && opCode != OP_CODE_RESPONSE_CODE) {
            onInvalidDataReceived(device, data)
            return
        }

        val operator = data.getIntValue(Data.FORMAT_UINT8, 1)!!
        if (operator != OPERATOR_NULL) {
            onInvalidDataReceived(device, data)
            return
        }

        when (opCode) {
            OP_CODE_NUMBER_OF_STORED_RECORDS_RESPONSE -> {
                // Field size is defined per service
                val numberOfRecords: Int

                when (data.size() - 2) {
                    1 -> numberOfRecords = data.getIntValue(Data.FORMAT_UINT8, 2)!!
                    2 -> numberOfRecords = data.getIntValue(Data.FORMAT_UINT16, 2)!!
                    4 -> numberOfRecords = data.getIntValue(Data.FORMAT_UINT32, 2)!!
                    else -> {
                        // Other field sizes are not supported
                        onInvalidDataReceived(device, data)
                        return
                    }
                }
                onNumberOfRecordsReceived(device, numberOfRecords)
            }
            OP_CODE_RESPONSE_CODE -> {
                if (data.size() != 4) {
                    onInvalidDataReceived(device, data)
                    return
                }

                val requestCode = data.getIntValue(Data.FORMAT_UINT8, 2)!!
                val responseCode = data.getIntValue(Data.FORMAT_UINT8, 3)!!
                if (responseCode == RACP_RESPONSE_SUCCESS) {
                    onRecordAccessOperationCompleted(device, requestCode)
                } else if (responseCode == RACP_ERROR_NO_RECORDS_FOUND) {
                    onRecordAccessOperationCompletedWithNoRecordsFound(device, requestCode)
                } else {
                    onRecordAccessOperationError(device, requestCode, responseCode)
                }
            }
        }
    }

    companion object {
        private val OP_CODE_NUMBER_OF_STORED_RECORDS_RESPONSE = 5
        private val OP_CODE_RESPONSE_CODE = 6
        private val OPERATOR_NULL = 0
        private val RACP_RESPONSE_SUCCESS = 1
        private val RACP_ERROR_NO_RECORDS_FOUND = 6
    }
}
