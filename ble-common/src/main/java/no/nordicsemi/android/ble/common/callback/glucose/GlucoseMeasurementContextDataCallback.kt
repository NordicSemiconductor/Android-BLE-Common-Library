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

package no.nordicsemi.android.ble.common.callback.glucose

import android.bluetooth.BluetoothDevice
import android.os.Parcel

import no.nordicsemi.android.ble.callback.profile.ProfileReadResponse
import no.nordicsemi.android.ble.common.profile.glucose.GlucoseMeasurementContextCallback
import no.nordicsemi.android.ble.data.Data

/**
 * Data callback that parses value into Glucose Measurement Context data.
 * If the value received do not match required syntax
 * [.onInvalidDataReceived] callback will be called.
 * will be called.
 * See: https://www.bluetooth.com/specifications/gatt/viewer?attributeXmlFile=org.bluetooth.characteristic.glucose_measurement_context.xml
 */
abstract class GlucoseMeasurementContextDataCallback : ProfileReadResponse,
    GlucoseMeasurementContextCallback {

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

        var offset = 0

        val flags = data.getIntValue(Data.FORMAT_UINT8, offset++)!!
        val carbohydratePresent = flags and 0x01 != 0
        val mealPresent = flags and 0x02 != 0
        val testerHealthPresent = flags and 0x04 != 0
        val exercisePresent = flags and 0x08 != 0
        val medicationPresent = flags and 0x10 != 0
        val medicationUnitLiter = flags and 0x20 != 0
        val HbA1cPresent = flags and 0x40 != 0
        val extendedFlagsPresent = flags and 0x80 != 0

        if (data.size() < (3 + (if (carbohydratePresent) 3 else 0) + (if (mealPresent) 1 else 0) + (if (testerHealthPresent) 1 else 0)
                    + (if (exercisePresent) 3 else 0) + (if (medicationPresent) 3 else 0) + (if (HbA1cPresent) 2 else 0)
                    + if (extendedFlagsPresent) 1 else 0)
        ) {
            onInvalidDataReceived(device, data)
            return
        }

        val sequenceNumber = data.getIntValue(Data.FORMAT_UINT16, offset)!!
        offset += 2

        // Optional fields
        if (extendedFlagsPresent) {
            // ignore extended flags
            offset += 1
        }

        var carbohydrate: GlucoseMeasurementContextCallback.Carbohydrate? = null
        var carbohydrateAmount: Float? = null
        if (carbohydratePresent) {
            val carbohydrateId = data.getIntValue(Data.FORMAT_UINT8, offset)!!
            carbohydrate = GlucoseMeasurementContextCallback.Carbohydrate.from(carbohydrateId)
            carbohydrateAmount = data.getFloatValue(Data.FORMAT_SFLOAT, offset + 1) // in grams
            offset += 3
        }

        var meal: GlucoseMeasurementContextCallback.Meal? = null
        if (mealPresent) {
            val mealId = data.getIntValue(Data.FORMAT_UINT8, offset)!!
            meal = GlucoseMeasurementContextCallback.Meal.from(mealId)
            offset += 1
        }

        var tester: GlucoseMeasurementContextCallback.Tester? = null
        var health: GlucoseMeasurementContextCallback.Health? = null
        if (testerHealthPresent) {
            val testerAndHealth = data.getIntValue(Data.FORMAT_UINT8, offset)!!
            tester = GlucoseMeasurementContextCallback.Tester.from(testerAndHealth and 0x0F)
            health = GlucoseMeasurementContextCallback.Health.from(testerAndHealth shr 4)
            offset += 1
        }

        var exerciseDuration: Int? = null
        var exerciseIntensity: Int? = null
        if (exercisePresent) {
            exerciseDuration = data.getIntValue(Data.FORMAT_UINT16, offset) // in seconds
            exerciseIntensity = data.getIntValue(Data.FORMAT_UINT8, offset + 2) // in percentage
            offset += 3
        }

        var medication: GlucoseMeasurementContextCallback.Medication? = null
        var medicationAmount: Float? = null
        var medicationUnit: Int? = null
        if (medicationPresent) {
            val medicationId = data.getIntValue(Data.FORMAT_UINT8, offset)!!
            medication = GlucoseMeasurementContextCallback.Medication.from(medicationId)
            medicationAmount = data.getFloatValue(Data.FORMAT_SFLOAT, offset + 1) // mg or ml
            medicationUnit =
                if (medicationUnitLiter) GlucoseMeasurementContextCallback.UNIT_ml else GlucoseMeasurementContextCallback.UNIT_mg
            offset += 3
        }

        var HbA1c: Float? = null
        if (HbA1cPresent) {
            HbA1c = data.getFloatValue(Data.FORMAT_SFLOAT, offset)
            // offset += 2;
        }

        onGlucoseMeasurementContextReceived(
            device, sequenceNumber, carbohydrate, carbohydrateAmount,
            meal, tester, health, exerciseDuration, exerciseIntensity,
            medication, medicationAmount, medicationUnit, HbA1c
        )
    }
}
