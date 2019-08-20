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

package no.nordicsemi.android.ble.common.callback.bps

import android.bluetooth.BluetoothDevice
import android.os.Parcel
import no.nordicsemi.android.ble.callback.profile.ProfileReadResponse
import no.nordicsemi.android.ble.common.callback.DateTimeDataCallback
import no.nordicsemi.android.ble.common.profile.bp.BloodPressureTypes
import no.nordicsemi.android.ble.common.profile.bp.IntermediateCuffPressureCallback
import no.nordicsemi.android.ble.data.Data
import java.util.*

/**
 * Data callback that parses value into Intermediate Cuff Pressure data.
 * If the value received do not match required syntax
 * [.onInvalidDataReceived] callback will be called.
 * See: https://www.bluetooth.com/specifications/gatt/viewer?attributeXmlFile=org.bluetooth.characteristic.intermediate_cuff_pressure.xml
 */
abstract class IntermediateCuffPressureDataCallback : ProfileReadResponse,
    IntermediateCuffPressureCallback {

    constructor() {
        // empty
    }

    protected constructor(`in`: Parcel) : super(`in`)

    override fun onDataReceived(device: BluetoothDevice, data: Data) {
        super.onDataReceived(device, data)

        if (data.size() < 7) {
            onInvalidDataReceived(device, data)
            return
        }
        // First byte: flags
        var offset = 0
        val flags = data.getIntValue(Data.FORMAT_UINT8, offset++)!!

        // See UNIT_* for unit options
        val unit =
            if (flags and 0x01 == BloodPressureTypes.Companion.UNIT_mmHg) BloodPressureTypes.Companion.UNIT_mmHg else BloodPressureTypes.Companion.UNIT_kPa
        val timestampPresent = flags and 0x02 != 0
        val pulseRatePresent = flags and 0x04 != 0
        val userIdPresent = flags and 0x08 != 0
        val measurementStatusPresent = flags and 0x10 != 0

        if (data.size() < (7
                    + (if (timestampPresent) 7 else 0) + (if (pulseRatePresent) 2 else 0)
                    + (if (userIdPresent) 1 else 0) + if (measurementStatusPresent) 2 else 0)
        ) {
            onInvalidDataReceived(device, data)
            return
        }

        // Following bytes - systolic, diastolic and mean arterial pressure
        val cuffPressure = data.getFloatValue(Data.FORMAT_SFLOAT, offset)!!
        // final float ignored_1 = data.getFloatValue(Data.FORMAT_SFLOAT, offset + 2);
        // final float ignored_2 = data.getFloatValue(Data.FORMAT_SFLOAT, offset + 4);
        offset += 6

        // Parse timestamp if present
        var calendar: Calendar? = null
        if (timestampPresent) {
            calendar = DateTimeDataCallback.readDateTime(data, offset)
            offset += 7
        }

        // Parse pulse rate if present
        var pulseRate: Float? = null
        if (pulseRatePresent) {
            pulseRate = data.getFloatValue(Data.FORMAT_SFLOAT, offset)
            offset += 2
        }

        // Read user id if present
        var userId: Int? = null
        if (userIdPresent) {
            userId = data.getIntValue(Data.FORMAT_UINT8, offset)
            offset += 1
        }

        // Read measurement status if present
        var status: BloodPressureTypes.BPMStatus? = null
        if (measurementStatusPresent) {
            val measurementStatus = data.getIntValue(Data.FORMAT_UINT16, offset)!!
            // offset += 2;
            status = BloodPressureTypes.BPMStatus(measurementStatus)
        }

        onIntermediateCuffPressureReceived(
            device,
            cuffPressure,
            unit,
            pulseRate,
            userId,
            status,
            calendar
        )
    }
}
