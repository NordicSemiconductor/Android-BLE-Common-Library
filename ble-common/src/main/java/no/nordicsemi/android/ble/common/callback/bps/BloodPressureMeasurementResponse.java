package no.nordicsemi.android.ble.common.callback.bps;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Calendar;

import no.nordicsemi.android.ble.exception.InvalidDataException;
import no.nordicsemi.android.ble.exception.RequestFailedException;

/**
 * Response class that could be used as a result of a synchronous request.
 * The data received are available through getters, instead of a callback.
 * <p>
 * Usage example:
 * <pre>
 * try {
 *     BloodPressureMeasurementResponse response = readCharacteristic(characteristic)
 *           .awaitForValid(BloodPressureMeasurementResponse.class);
 *     if (response.getStatus() != null) {
 *         ...
 *     }
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
public final class BloodPressureMeasurementResponse extends BloodPressureMeasurementDataCallback implements Parcelable {
	private float systolic;
	private float diastolic;
	private float meanArterialPressure;
	private int unit;
	private Float pulseRate;
	private Integer userID;
	private BPMStatus status;
	private Calendar calendar;

	public BloodPressureMeasurementResponse() {
		// empty
	}

	@Override
	public void onBloodPressureMeasurementReceived(@NonNull final BluetoothDevice device,
												   final float systolic, final float diastolic,
												   final float meanArterialPressure, final int unit,
												   @Nullable final Float pulseRate,
												   @Nullable final Integer userID,
												   @Nullable final BPMStatus status,
												   @Nullable final Calendar calendar) {
		this.systolic = systolic;
		this.diastolic = diastolic;
		this.meanArterialPressure = meanArterialPressure;
		this.unit = unit;
		this.pulseRate = pulseRate;
		this.userID = userID;
		this.status = status;
		this.calendar = calendar;
	}

	public float getSystolic() {
		return systolic;
	}

	public float getDiastolic() {
		return diastolic;
	}

	public float getMeanArterialPressure() {
		return meanArterialPressure;
	}

	/**
	 * Returns the measurement unit, one of {@link #UNIT_mmHg} or {@link #UNIT_kPa}.
	 * To convert to proper unit, use {@link #toMmHg(float, int)} or {@link #toKPa(float, int)}.
	 *
	 * @return Unit of systolic, diastolic and mean arterial pressure.
	 */
	public int getUnit() {
		return unit;
	}

	@Nullable
	public Float getPulseRate() {
		return pulseRate;
	}

	@Nullable
	public Integer getUserID() {
		return userID;
	}

	@Nullable
	public BPMStatus getStatus() {
		return status;
	}

	@Nullable
	public Calendar getTimestamp() {
		return calendar;
	}

	// Parcelable
	private BloodPressureMeasurementResponse(final Parcel in) {
		super(in);
		systolic = in.readFloat();
		diastolic = in.readFloat();
		meanArterialPressure = in.readFloat();
		unit = in.readInt();
		if (in.readByte() == 0) {
			pulseRate = null;
		} else {
			pulseRate = in.readFloat();
		}
		if (in.readByte() == 0) {
			userID = null;
		} else {
			userID = in.readInt();
		}
		if (in.readByte() == 0) {
			status = null;
		} else {
			status = new BPMStatus(in.readInt());
		}
		if (in.readByte() == 0) {
			calendar = null;
		} else {
			calendar = Calendar.getInstance();
			calendar.setTimeInMillis(in.readLong());
		}
	}

	@Override
	public void writeToParcel(final Parcel dest, final int flags) {
		super.writeToParcel(dest, flags);
		dest.writeFloat(systolic);
		dest.writeFloat(diastolic);
		dest.writeFloat(meanArterialPressure);
		dest.writeInt(unit);
		if (pulseRate == null) {
			dest.writeByte((byte) 0);
		} else {
			dest.writeByte((byte) 1);
			dest.writeFloat(pulseRate);
		}
		if (userID == null) {
			dest.writeByte((byte) 0);
		} else {
			dest.writeByte((byte) 1);
			dest.writeInt(userID);
		}
		if (status == null) {
			dest.writeByte((byte) 0);
		} else {
			dest.writeByte((byte) 1);
			dest.writeInt(status.value);
		}
		if (calendar == null) {
			dest.writeByte((byte) 0);
		} else {
			dest.writeByte((byte) 1);
			dest.writeLong(calendar.getTimeInMillis());
		}
	}

	public static final Creator<BloodPressureMeasurementResponse> CREATOR = new Creator<BloodPressureMeasurementResponse>() {
		@Override
		public BloodPressureMeasurementResponse createFromParcel(final Parcel in) {
			return new BloodPressureMeasurementResponse(in);
		}

		@Override
		public BloodPressureMeasurementResponse[] newArray(final int size) {
			return new BloodPressureMeasurementResponse[size];
		}
	};
}
