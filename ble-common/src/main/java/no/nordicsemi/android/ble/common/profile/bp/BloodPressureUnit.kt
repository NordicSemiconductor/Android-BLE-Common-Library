package no.nordicsemi.android.ble.common.profile.bp

import androidx.annotation.IntDef

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

@Retention(RetentionPolicy.SOURCE)
@IntDef(value = [BloodPressureTypes.UNIT_mmHg, BloodPressureTypes.UNIT_kPa])
annotation class BloodPressureUnit