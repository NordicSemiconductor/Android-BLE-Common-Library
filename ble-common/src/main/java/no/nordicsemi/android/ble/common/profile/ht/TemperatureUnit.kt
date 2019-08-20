package no.nordicsemi.android.ble.common.profile.ht

import androidx.annotation.IntDef

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

@Retention(RetentionPolicy.SOURCE)
@IntDef(value = [TemperatureMeasurementCallback.UNIT_C, TemperatureMeasurementCallback.UNIT_F])
annotation class TemperatureUnit