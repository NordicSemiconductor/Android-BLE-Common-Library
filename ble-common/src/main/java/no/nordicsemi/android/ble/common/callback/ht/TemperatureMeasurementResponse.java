package no.nordicsemi.android.ble.common.callback.ht;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Calendar;

import no.nordicsemi.android.ble.common.profile.ht.TemperatureMeasurementCallback;
import no.nordicsemi.android.ble.exception.InvalidDataException;
import no.nordicsemi.android.ble.exception.RequestFailedException;

/**
 * Response class that could be used as a result of a synchronous request.
 * The data received are available through getters, instead of a callback.
 * <p>
 * Usage example:
 * <pre>
 * try {
 *     TemperatureMeasurementResponse response = setNotificationCallback(characteristic)
 *           .awaitValid(TemperatureMeasurementResponse.class);
 *     float tempCelsius = response.getTemperatureCelsius());
 *     ...
 * } catch ({@link RequestFailedException} e) {
 *     Log.w(TAG, "Request failed with status " + e.getStatus(), e);
 * } catch ({@link InvalidDataException} e) {
 *     Log.w(TAG, "Invalid data received: " + e.getResponse().getRawData());
 * }
 * </pre>
 * </p>
 */
@SuppressWarnings("unused")
public final class TemperatureMeasurementResponse extends TemperatureMeasurementDataCallback implements Parcelable {
	private float temperature;
	private int unit;
	@Nullable
	private Calendar timestamp;
	@Nullable
	private Integer type;

	public TemperatureMeasurementResponse() {
		// empty
	}

	@Override
	public void onTemperatureMeasurementReceived(@NonNull final BluetoothDevice device,
												 final float temperature, final int unit,
												 @Nullable final Calendar calendar,
												 @Nullable final Integer type) {
		this.temperature = temperature;
		this.unit = unit;
		this.timestamp = calendar;
		this.type = type;
	}

	public float getTemperature() {
		return temperature;
	}

	public float getTemperatureCelsius() {
		return TemperatureMeasurementCallback.toCelsius(temperature, unit);
	}

	public float getTemperatureFahrenheit() {
		return TemperatureMeasurementCallback.toFahrenheit(temperature, unit);
	}

	public int getUnit() {
		return unit;
	}

	@Nullable
	public Calendar getTimestamp() {
		return timestamp;
	}

	@Nullable
	public Integer getType() {
		return type;
	}

	// Parcelable
	@SuppressWarnings("ConstantConditions")
	private TemperatureMeasurementResponse(final Parcel in) {
		super(in);
		temperature = in.readFloat();
		unit = in.readInt();
		if (in.readByte() == 0) {
			timestamp = null;
		} else {
			timestamp = Calendar.getInstance();
			timestamp.setTimeInMillis(in.readLong());
		}
		if (in.readByte() == 0) {
			type = null;
		} else {
			type = in.readInt();
		}
	}

	@Override
	public void writeToParcel(final Parcel dest, final int flags) {
		super.writeToParcel(dest, flags);
		dest.writeFloat(temperature);
		dest.writeInt(unit);
		if (timestamp == null) {
			dest.writeByte((byte) 0);
		} else {
			dest.writeByte((byte) 1);
			dest.writeLong(timestamp.getTimeInMillis());
		}
		if (type == null) {
			dest.writeByte((byte) 0);
		} else {
			dest.writeByte((byte) 1);
			dest.writeInt(type);
		}
	}

	public static final Creator<TemperatureMeasurementResponse> CREATOR = new Creator<TemperatureMeasurementResponse>() {
		@Override
		public TemperatureMeasurementResponse createFromParcel(final Parcel in) {
			return new TemperatureMeasurementResponse(in);
		}

		@Override
		public TemperatureMeasurementResponse[] newArray(final int size) {
			return new TemperatureMeasurementResponse[size];
		}
	};
}
