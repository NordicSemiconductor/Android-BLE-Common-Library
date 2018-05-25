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
 *     IntermediateCuffPressureResponse response = readCharacteristic(characteristic)
 *           .awaitForValid(IntermediateCuffPressureResponse.class);
 *     float pressureMmHg = toMmHg(response.getCuffPressure(), response.getUnit());
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
public final class IntermediateCuffPressureResponse extends IntermediateCuffPressureDataCallback implements Parcelable {
	private float cuffPressure;
	private int unit;
	private Float pulseRate;
	private Integer userID;
	private BPMStatus status;
	private Calendar calendar;

	public IntermediateCuffPressureResponse() {
		// empty
	}

	@Override
	public void onIntermediateCuffPressureReceived(@NonNull final BluetoothDevice device,
												   final float cuffPressure, final int unit,
												   @Nullable final Float pulseRate,
												   @Nullable final Integer userID,
												   @Nullable final BPMStatus status,
												   @Nullable final Calendar calendar) {
		this.cuffPressure = cuffPressure;
		this.unit = unit;
		this.pulseRate = pulseRate;
		this.userID = userID;
		this.status = status;
		this.calendar = calendar;
	}

	/**
	 * Returns the received cuff pressure in the unit returned by {@link #getUnit()}.
	 *
	 * @return The cuff pressure value.
	 */
	public float getCuffPressure() {
		return cuffPressure;
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
	private IntermediateCuffPressureResponse(final Parcel in) {
		super(in);
		cuffPressure = in.readFloat();
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
		dest.writeFloat(cuffPressure);
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

	public static final Parcelable.Creator<IntermediateCuffPressureResponse> CREATOR = new Parcelable.Creator<IntermediateCuffPressureResponse>() {
		@Override
		public IntermediateCuffPressureResponse createFromParcel(final Parcel in) {
			return new IntermediateCuffPressureResponse(in);
		}

		@Override
		public IntermediateCuffPressureResponse[] newArray(final int size) {
			return new IntermediateCuffPressureResponse[size];
		}
	};
}
