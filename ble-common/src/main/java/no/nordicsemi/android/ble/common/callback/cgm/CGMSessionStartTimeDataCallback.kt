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

package no.nordicsemi.android.ble.common.callback.cgm

import android.bluetooth.BluetoothDevice
import android.os.Parcel
import no.nordicsemi.android.ble.callback.profile.ProfileReadResponse
import no.nordicsemi.android.ble.common.callback.DSTOffsetDataCallback
import no.nordicsemi.android.ble.common.callback.DateTimeDataCallback
import no.nordicsemi.android.ble.common.callback.TimeZoneDataCallback
import no.nordicsemi.android.ble.common.profile.cgm.CGMSessionStartTimeCallback
import no.nordicsemi.android.ble.common.util.CRC16
import no.nordicsemi.android.ble.data.Data
import java.util.*

/**
 * Data callback that parses value into CGM Session Start Time data.
 * If the value received do not match required syntax
 * [.onInvalidDataReceived] callback will be called.
 * If the device supports E2E CRC validation and the CRC is not valid, the
 * [.onContinuousGlucoseMonitorSessionStartTimeReceivedWithCrcError]
 * will be called.
 * See: https://www.bluetooth.com/specifications/gatt/viewer?attributeXmlFile=org.bluetooth.characteristic.cgm_session_start_time.xml
 */
abstract class CGMSessionStartTimeDataCallback : ProfileReadResponse, CGMSessionStartTimeCallback {

    constructor() {
        // empty
    }

    protected constructor(`in`: Parcel) : super(`in`)

    override fun onDataReceived(device: BluetoothDevice, data: Data) {
        super.onDataReceived(device, data)

        if (data.size() != 9 && data.size() != 11) {
            onInvalidDataReceived(device, data)
            return
        }

        val crcPresent = data.size() == 11
        if (crcPresent) {
            val actualCrc = CRC16.MCRF4XX(data.value!!, 0, 9)
            val expectedCrc = data.getIntValue(Data.FORMAT_UINT16, 9)!!
            if (actualCrc != expectedCrc) {
                onContinuousGlucoseMonitorSessionStartTimeReceivedWithCrcError(device, data)
                return
            }
        }

        val calendar = DateTimeDataCallback.readDateTime(data, 0)
        val timeZoneOffset = TimeZoneDataCallback.readTimeZone(data, 7) // [minutes]
        val dstOffset = DSTOffsetDataCallback.readDSTOffset(data, 8)

        if (calendar == null || timeZoneOffset == null || dstOffset == null) {
            onInvalidDataReceived(device, data)
            return
        }

        val timeZone = object : TimeZone() {
            override fun getOffset(
                era: Int,
                year: Int,
                month: Int,
                day: Int,
                dayOfWeek: Int,
                milliseconds: Int
            ): Int {
                return (timeZoneOffset + dstOffset.offset) * 60000 // convert minutes to milliseconds
            }

            override fun getRawOffset(): Int {
                return timeZoneOffset * 60000
            }

            override fun setRawOffset(offsetMillis: Int) {
                throw UnsupportedOperationException("Can't set raw offset for this TimeZone")
            }

            override fun useDaylightTime(): Boolean {
                return true
            }

            override fun inDaylightTime(date: Date): Boolean {
                // Use of DST is dependent on the input data only
                return dstOffset.offset > 0
            }

            override fun getDSTSavings(): Int {
                return dstOffset.offset * 60000
            }

            // TODO add TimeZone ID
            //			@Override
            //			public String getID() {
            //				return super.getID();
            //			}
        }

        calendar.timeZone = timeZone

        onContinuousGlucoseMonitorSessionStartTimeReceived(device, calendar, crcPresent)
    }
}
