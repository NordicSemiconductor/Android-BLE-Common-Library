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

package no.nordicsemi.android.ble.common.profile.glucose;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Calendar;

@SuppressWarnings("unused")
public interface GlucoseMeasurementCallback {
	/**
	 * Unit kg/L
	 */
	int UNIT_kg_L = 0;
	/**
	 * Unit mol/L
	 */
	int UNIT_mol_L = 1;

	int TYPE_CAPILLARY_WHOLE_BLOOD = 1;
	int TYPE_CAPILLARY_PLASMA = 2;
	int TYPE_VENOUS_WHOLE_BLOOD = 3;
	int TYPE_VENOUS_PLASMA = 4;
	int TYPE_ARTERIAL_WHOLE_BLOOD = 5;
	int TYPE_ARTERIAL_PLASMA = 6;
	int TYPE_UNDETERMINED_WHOLE_BLOOD = 7;
	int TYPE_UNDETERMINED_PLASMA = 8;
	int TYPE_INTERSTITIAL_FLUID_ISF = 9;
	int TYPE_CONTROL_SOLUTION = 10;

	int SAMPLE_LOCATION_FINGER = 1;
	int SAMPLE_LOCATION_ALTERNATE_SITE_TEST = 2;
	int SAMPLE_LOCATION_EARLOBE = 3;
	int SAMPLE_LOCATION_CONTROL_SOLUTION = 4;
	int SAMPLE_LOCATION_VALUE_NOT_AVAILABLE = 15;

	@SuppressWarnings("WeakerAccess")
	class GlucoseStatus {
		public boolean deviceBatteryLow;
		public boolean sensorMalfunction;
		public boolean sampleSizeInsufficient;
		public boolean stripInsertionError;
		public boolean stripTypeIncorrect;
		public boolean sensorResultLowerThenDeviceCanProcess;
		public boolean sensorResultHigherThenDeviceCanProcess;
		public boolean sensorTemperatureTooHigh;
		public boolean sensorTemperatureTooLow;
		public boolean sensorReadInterrupted;
		public boolean generalDeviceFault;
		public boolean timeFault;
		public int value;

		public GlucoseStatus(final int value) {
			this.value = value;

			deviceBatteryLow = (value & 0x0001) != 0;
			sensorMalfunction = (value & 0x0002) != 0;
			sampleSizeInsufficient = (value & 0x0004) != 0;
			stripInsertionError = (value & 0x0008) != 0;
			stripTypeIncorrect = (value & 0x0010) != 0;
			sensorResultLowerThenDeviceCanProcess = (value & 0x0020) != 0;
			sensorResultHigherThenDeviceCanProcess = (value & 0x0040) != 0;
			sensorTemperatureTooHigh = (value & 0x0080) != 0;
			sensorTemperatureTooLow = (value & 0x0100) != 0;
			sensorReadInterrupted = (value & 0x0200) != 0;
			generalDeviceFault = (value & 0x0400) != 0;
			timeFault = (value & 0x0800) != 0;
		}
	}

	/**
	 * Callback called when Glucose Measurement value was received.
	 * Except from the BluetoothDevice, only the sequence number and sample time is required
	 * to be non-null.
	 *
	 * @param device                    the target device.
	 * @param sequenceNumber            represents the chronological order of the patient records
	 *                                  in the Server measurement database. The initial default
	 *                                  value is 0. The Sequence Number is incremented by 1 for
	 *                                  each successive Glucose Measurement characteristic value.
	 *                                  The maximum value for Sequence Number permitted is 0xFFFF.
	 *                                  Assuming a high use of 8 times per day, the maximum value
	 *                                  of the Sequence Number would be reached in ~22 years.
	 *                                  Since product life expectancy of a Glucose Sensor is
	 *                                  ~ 5 years, this value significantly exceeds that expectation.
	 * @param time                      the base time with time offset added, if such was present
	 *                                  in the packet.
	 * @param glucoseConcentration      the glucose concentration in the provided unit.
	 * @param unit                      the sample unit ({@link #UNIT_kg_L} or {@link #UNIT_mol_L}).
	 * @param type                      the sample type, see TYPE_* constants.
	 * @param sampleLocation            the sample location, see SAMPLE_LOCATION_* constants.
	 * @param contextInformationFollows true, if Glucose Measurement Context will be sent
	 *                                  immediately after this packet. False otherwise.
	 */
	void onGlucoseMeasurementReceived(@NonNull final BluetoothDevice device,
									  final int sequenceNumber, @NonNull final Calendar time,
									  @Nullable final Float glucoseConcentration, @Nullable final Integer unit,
									  @Nullable final Integer type, @Nullable final Integer sampleLocation,
									  @Nullable final GlucoseStatus status, final boolean contextInformationFollows);

	/**
	 * Converts the value provided in given unit to kg/L.
	 * If the unit is already {@link #UNIT_kg_L} it will be returned as is.
	 *
	 * @param value the glucose concentration value in given unit.
	 * @param unit  the unit of the value ({@link #UNIT_kg_L} or {@link #UNIT_mol_L}).
	 * @return Value in kg/L.
	 */
	static float toKgPerL(final float value, final int unit) {
		if (unit == UNIT_kg_L) {
			return value;
		} else {
			return value * 18.2f / 100f;
		}
	}

	/**
	 * Converts the value provided in given unit to mg/dL.
	 *
	 * @param value the glucose concentration value in given unit.
	 * @param unit  the unit of the value ({@link #UNIT_kg_L} or {@link #UNIT_mol_L}).
	 * @return Value in mg/dL.
	 */
	static float toMgPerDecilitre(final float value, final int unit) {
		if (unit == UNIT_kg_L) {
			return value * 100000f;
		} else {
			return value * 18.2f * 1000f;
		}
	}

	/**
	 * Converts the value provided in given unit to mol/L.
	 * If the unit is already {@link #UNIT_mol_L} it will be returned as is.
	 *
	 * @param value the glucose concentration value in given unit.
	 * @param unit  the unit of the value ({@link #UNIT_kg_L} or {@link #UNIT_mol_L}).
	 * @return Value in mol/L.
	 */
	static float toMolPerL(final float value, final int unit) {
		if (unit == UNIT_mol_L) {
			return value;
		} else {
			return value * 100f / 18.2f;
		}
	}

	/**
	 * Converts the value provided in given unit to mmol/L.
	 *
	 * @param value the glucose concentration value in given unit.
	 * @param unit  the unit of the value ({@link #UNIT_kg_L} or {@link #UNIT_mol_L}).
	 * @return Value in mmol/L.
	 */
	static float toMmolPerL(final float value, final int unit) {
		if (unit == UNIT_mol_L) {
			return value * 1000f;
		} else {
			return value * 100000f / 18.2f;
		}
	}
}
