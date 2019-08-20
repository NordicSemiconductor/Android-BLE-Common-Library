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
import android.os.Parcelable
import no.nordicsemi.android.ble.common.profile.glucose.GlucoseMeasurementContextCallback

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
 * GlucoseMeasurementContextResponse response = waitForNotification(characteristic)
 * .awaitValid(GlucoseMeasurementContextResponse.class);
 * Carbohydrate carbohydrate = response.getCarbohydrate();
 * if (carbohydrate != null) {
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
class GlucoseMeasurementContextResponse : GlucoseMeasurementContextDataCallback, Parcelable {
    var sequenceNumber: Int = 0
        private set
    var carbohydrate: GlucoseMeasurementContextCallback.Carbohydrate? = null
        private set
    var carbohydrateAmount: Float? = null
        private set
    var meal: GlucoseMeasurementContextCallback.Meal? = null
        private set
    var tester: GlucoseMeasurementContextCallback.Tester? = null
        private set
    var health: GlucoseMeasurementContextCallback.Health? = null
        private set
    var exerciseDuration: Int? = null
        private set
    var exerciseIntensity: Int? = null
        private set
    var medication: GlucoseMeasurementContextCallback.Medication? = null
        private set
    var medicationAmount: Float? = null
        private set
    var medicationUnit: Int? = null
        private set
    var hbA1c: Float? = null
        private set

    constructor() {
        // empty
    }

    // Parcelable
    private constructor(`in`: Parcel) : super(`in`) {
        sequenceNumber = `in`.readInt()
        if (`in`.readByte().toInt() == 0) {
            carbohydrateAmount = null
        } else {
            carbohydrateAmount = `in`.readFloat()
        }
        if (`in`.readByte().toInt() == 0) {
            exerciseDuration = null
        } else {
            exerciseDuration = `in`.readInt()
        }
        if (`in`.readByte().toInt() == 0) {
            exerciseIntensity = null
        } else {
            exerciseIntensity = `in`.readInt()
        }
        if (`in`.readByte().toInt() == 0) {
            medicationAmount = null
        } else {
            medicationAmount = `in`.readFloat()
        }
        if (`in`.readByte().toInt() == 0) {
            medicationUnit = null
        } else {
            medicationUnit = `in`.readInt()
        }
        if (`in`.readByte().toInt() == 0) {
            hbA1c = null
        } else {
            hbA1c = `in`.readFloat()
        }
    }

    override fun onGlucoseMeasurementContextReceived(
        device: BluetoothDevice,
        sequenceNumber: Int,
        carbohydrate: GlucoseMeasurementContextCallback.Carbohydrate?,
        carbohydrateAmount: Float?,
        meal: GlucoseMeasurementContextCallback.Meal?,
        tester: GlucoseMeasurementContextCallback.Tester?,
        health: GlucoseMeasurementContextCallback.Health?,
        exerciseDuration: Int?,
        exerciseIntensity: Int?,
        medication: GlucoseMeasurementContextCallback.Medication?,
        medicationAmount: Float?,
        medicationUnit: Int?,
        HbA1c: Float?
    ) {
        this.sequenceNumber = sequenceNumber
        this.carbohydrate = carbohydrate
        this.carbohydrateAmount = carbohydrateAmount
        this.meal = meal
        this.tester = tester
        this.health = health
        this.exerciseDuration = exerciseDuration
        this.exerciseIntensity = exerciseIntensity
        this.medication = medication
        this.medicationAmount = medicationAmount
        this.medicationUnit = medicationUnit
        this.hbA1c = HbA1c
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        super.writeToParcel(dest, flags)
        dest.writeInt(sequenceNumber)
        if (carbohydrateAmount == null) {
            dest.writeByte(0.toByte())
        } else {
            dest.writeByte(1.toByte())
            dest.writeFloat(carbohydrateAmount!!)
        }
        if (exerciseDuration == null) {
            dest.writeByte(0.toByte())
        } else {
            dest.writeByte(1.toByte())
            dest.writeInt(exerciseDuration!!)
        }
        if (exerciseIntensity == null) {
            dest.writeByte(0.toByte())
        } else {
            dest.writeByte(1.toByte())
            dest.writeInt(exerciseIntensity!!)
        }
        if (medicationAmount == null) {
            dest.writeByte(0.toByte())
        } else {
            dest.writeByte(1.toByte())
            dest.writeFloat(medicationAmount!!)
        }
        if (medicationUnit == null) {
            dest.writeByte(0.toByte())
        } else {
            dest.writeByte(1.toByte())
            dest.writeInt(medicationUnit!!)
        }
        if (hbA1c == null) {
            dest.writeByte(0.toByte())
        } else {
            dest.writeByte(1.toByte())
            dest.writeFloat(hbA1c!!)
        }
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<GlucoseMeasurementContextResponse> =
            object : Parcelable.Creator<GlucoseMeasurementContextResponse> {
                override fun createFromParcel(`in`: Parcel): GlucoseMeasurementContextResponse {
                    return GlucoseMeasurementContextResponse(`in`)
                }

                override fun newArray(size: Int): Array<GlucoseMeasurementContextResponse?> {
                    return arrayOfNulls(size)
                }
            }
    }
}
