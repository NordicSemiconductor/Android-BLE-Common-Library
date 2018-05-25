package no.nordicsemi.android.ble.common.callback.glucose;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import no.nordicsemi.android.ble.exception.InvalidDataException;
import no.nordicsemi.android.ble.exception.RequestFailedException;

/**
 * Response class that could be used as a result of a synchronous request.
 * The data received are available through getters, instead of a callback.
 * <p>
 * Usage example:
 * <pre>
 * try {
 *     GlucoseMeasurementContextResponse response = setNotificationCallback(characteristic)
 *           .awaitValid(GlucoseMeasurementContextResponse.class);
 *     Carbohydrate carbohydrate = response.getCarbohydrate();
 *     if (carbohydrate != null) {
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
public final class GlucoseMeasurementContextResponse extends GlucoseMeasurementContextDataCallback implements Parcelable {
	private int sequenceNumber;
	@Nullable
	private Carbohydrate carbohydrate;
	@Nullable
	private Float carbohydrateAmount;
	@Nullable
	private Meal meal;
	@Nullable
	private Tester tester;
	@Nullable
	private Health health;
	@Nullable
	private Integer exerciseDuration;
	@Nullable
	private Integer exerciseIntensity;
	@Nullable
	private Medication medication;
	@Nullable
	private Float medicationAmount;
	@Nullable
	private Integer medicationUnit;
	@Nullable
	private Float HbA1c;

	public GlucoseMeasurementContextResponse() {
		// empty
	}

	@Override
	public void onGlucoseMeasurementContextReceived(@NonNull final BluetoothDevice device,
													final int sequenceNumber,
													@Nullable final Carbohydrate carbohydrate,
													@Nullable final Float carbohydrateAmount,
													@Nullable final Meal meal,
													@Nullable final Tester tester,
													@Nullable final Health health,
													@Nullable final Integer exerciseDuration,
													@Nullable final Integer exerciseIntensity,
													@Nullable final Medication medication,
													@Nullable final Float medicationAmount,
													@Nullable final Integer medicationUnit,
													@Nullable final Float HbA1c) {
		this.sequenceNumber = sequenceNumber;
		this.carbohydrate = carbohydrate;
		this.carbohydrateAmount = carbohydrateAmount;
		this.meal = meal;
		this.tester = tester;
		this.health = health;
		this.exerciseDuration = exerciseDuration;
		this.exerciseIntensity = exerciseIntensity;
		this.medication = medication;
		this.medicationAmount = medicationAmount;
		this.medicationUnit = medicationUnit;
		this.HbA1c = HbA1c;
	}

	public int getSequenceNumber() {
		return sequenceNumber;
	}

	@Nullable
	public Carbohydrate getCarbohydrate() {
		return carbohydrate;
	}

	@Nullable
	public Float getCarbohydrateAmount() {
		return carbohydrateAmount;
	}

	@Nullable
	public Meal getMeal() {
		return meal;
	}

	@Nullable
	public Tester getTester() {
		return tester;
	}

	@Nullable
	public Health getHealth() {
		return health;
	}

	@Nullable
	public Integer getExerciseDuration() {
		return exerciseDuration;
	}

	@Nullable
	public Integer getExerciseIntensity() {
		return exerciseIntensity;
	}

	@Nullable
	public Medication getMedication() {
		return medication;
	}

	@Nullable
	public Float getMedicationAmount() {
		return medicationAmount;
	}

	@Nullable
	public Integer getMedicationUnit() {
		return medicationUnit;
	}

	@Nullable
	public Float getHbA1c() {
		return HbA1c;
	}

	// Parcelable
	private GlucoseMeasurementContextResponse(final Parcel in) {
		super(in);
		sequenceNumber = in.readInt();
		if (in.readByte() == 0) {
			carbohydrateAmount = null;
		} else {
			carbohydrateAmount = in.readFloat();
		}
		if (in.readByte() == 0) {
			exerciseDuration = null;
		} else {
			exerciseDuration = in.readInt();
		}
		if (in.readByte() == 0) {
			exerciseIntensity = null;
		} else {
			exerciseIntensity = in.readInt();
		}
		if (in.readByte() == 0) {
			medicationAmount = null;
		} else {
			medicationAmount = in.readFloat();
		}
		if (in.readByte() == 0) {
			medicationUnit = null;
		} else {
			medicationUnit = in.readInt();
		}
		if (in.readByte() == 0) {
			HbA1c = null;
		} else {
			HbA1c = in.readFloat();
		}
	}

	@Override
	public void writeToParcel(final Parcel dest, final int flags) {
		super.writeToParcel(dest, flags);
		dest.writeInt(sequenceNumber);
		if (carbohydrateAmount == null) {
			dest.writeByte((byte) 0);
		} else {
			dest.writeByte((byte) 1);
			dest.writeFloat(carbohydrateAmount);
		}
		if (exerciseDuration == null) {
			dest.writeByte((byte) 0);
		} else {
			dest.writeByte((byte) 1);
			dest.writeInt(exerciseDuration);
		}
		if (exerciseIntensity == null) {
			dest.writeByte((byte) 0);
		} else {
			dest.writeByte((byte) 1);
			dest.writeInt(exerciseIntensity);
		}
		if (medicationAmount == null) {
			dest.writeByte((byte) 0);
		} else {
			dest.writeByte((byte) 1);
			dest.writeFloat(medicationAmount);
		}
		if (medicationUnit == null) {
			dest.writeByte((byte) 0);
		} else {
			dest.writeByte((byte) 1);
			dest.writeInt(medicationUnit);
		}
		if (HbA1c == null) {
			dest.writeByte((byte) 0);
		} else {
			dest.writeByte((byte) 1);
			dest.writeFloat(HbA1c);
		}
	}

	public static final Creator<GlucoseMeasurementContextResponse> CREATOR = new Creator<GlucoseMeasurementContextResponse>() {
		@Override
		public GlucoseMeasurementContextResponse createFromParcel(final Parcel in) {
			return new GlucoseMeasurementContextResponse(in);
		}

		@Override
		public GlucoseMeasurementContextResponse[] newArray(final int size) {
			return new GlucoseMeasurementContextResponse[size];
		}
	};
}
