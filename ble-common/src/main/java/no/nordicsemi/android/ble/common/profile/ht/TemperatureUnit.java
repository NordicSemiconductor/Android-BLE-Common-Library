package no.nordicsemi.android.ble.common.profile.ht;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@SuppressWarnings("WeakerAccess")
@Retention(RetentionPolicy.SOURCE)
@IntDef(value = {
		TemperatureMeasurementCallback.UNIT_C,
		TemperatureMeasurementCallback.UNIT_F
})
public @interface TemperatureUnit {}