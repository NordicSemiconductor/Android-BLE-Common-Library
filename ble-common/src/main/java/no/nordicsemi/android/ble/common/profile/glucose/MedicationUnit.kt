package no.nordicsemi.android.ble.common.profile.glucose

import androidx.annotation.IntDef

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

@Retention(RetentionPolicy.SOURCE)
@IntDef(value = [GlucoseMeasurementContextCallback.UNIT_mg, GlucoseMeasurementContextCallback.UNIT_ml])
annotation class MedicationUnit