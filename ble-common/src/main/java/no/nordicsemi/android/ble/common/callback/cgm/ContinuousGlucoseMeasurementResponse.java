package no.nordicsemi.android.ble.common.callback.cgm;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import no.nordicsemi.android.ble.data.Data;
import no.nordicsemi.android.ble.exception.InvalidDataException;
import no.nordicsemi.android.ble.exception.RequestFailedException;

/**
 * Response class that could be used as a result of a synchronous request.
 * The data received are available through getters, instead of a callback.
 * <p>
 * Usage example:
 * <pre>
 * try {
 *     ContinuousGlucoseMeasurementResponse response = setNotificationCallback(characteristic)
 *           .awaitValid(ContinuousGlucoseMeasurementResponse.class);
 *     if (response.isValid()) {
 *         float glucoseConcentration = response.getGlucoseConcentration();
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
public final class ContinuousGlucoseMeasurementResponse extends ContinuousGlucoseMeasurementDataCallback implements CRCSecuredResponse, Parcelable {
	private float glucoseConcentration;
	@Nullable
	private Float trend;
	@Nullable
	private Float quality;
	@Nullable
	private CGMStatus status;
	private int timeOffset;
	private boolean secured;
	private boolean crcValid;

	public ContinuousGlucoseMeasurementResponse() {
		// empty
	}

	@Override
	public void onContinuousGlucoseMeasurementReceived(@NonNull final BluetoothDevice device, final float glucoseConcentration,
													   @Nullable final Float cgmTrend, @Nullable final Float cgmQuality,
													   @Nullable final CGMStatus status, final int timeOffset, final boolean secured) {
		this.glucoseConcentration = glucoseConcentration;
		this.trend = cgmTrend;
		this.quality = cgmQuality;
		this.status = status;
		this.timeOffset = timeOffset;
		this.secured = secured;
		this.crcValid = secured;
	}

	@Override
	public void onContinuousGlucoseMeasurementReceivedWithCrcError(@NonNull final BluetoothDevice device, @NonNull final Data data) {
		onInvalidDataReceived(device, data);
		this.secured = true;
		this.crcValid = false;
	}

	public float getGlucoseConcentration() {
		return glucoseConcentration;
	}

	@Nullable
	public Float getTrend() {
		return trend;
	}

	@Nullable
	public Float getQuality() {
		return quality;
	}

	@Nullable
	public CGMStatus getStatus() {
		return status;
	}

	public int getTimeOffset() {
		return timeOffset;
	}

	@Override
	public boolean isSecured() {
		return secured;
	}

	@Override
	public boolean isCrcValid() {
		return crcValid;
	}

	// Parcelable
	private ContinuousGlucoseMeasurementResponse(final Parcel in) {
		super(in);
		glucoseConcentration = in.readFloat();
		if (in.readByte() == 0) {
			trend = null;
		} else {
			trend = in.readFloat();
		}
		if (in.readByte() == 0) {
			quality = null;
		} else {
			quality = in.readFloat();
		}
		if (in.readByte() == 0) {
			status = null;
		} else {
			final int warningStatus = in.readInt();
			final int calibrationTempStatus = in.readInt();
			final int sensorStatus = in.readInt();
			status = new CGMStatus(warningStatus, calibrationTempStatus, sensorStatus);
		}
		timeOffset = in.readInt();
		secured = in.readByte() != 0;
		crcValid = in.readByte() != 0;
	}

	@Override
	public void writeToParcel(final Parcel dest, final int flags) {
		super.writeToParcel(dest, flags);
		dest.writeFloat(glucoseConcentration);
		if (trend == null) {
			dest.writeByte((byte) 0);
		} else {
			dest.writeByte((byte) 1);
			dest.writeFloat(trend);
		}
		if (quality == null) {
			dest.writeByte((byte) 0);
		} else {
			dest.writeByte((byte) 1);
			dest.writeFloat(quality);
		}
		if (status == null) {
			dest.writeByte((byte) 0);
		} else {
			dest.writeByte((byte) 1);
			dest.writeInt(status.warningStatus);
			dest.writeInt(status.calibrationTempStatus);
			dest.writeInt(status.sensorStatus);
		}
		dest.writeInt(timeOffset);
		dest.writeByte((byte) (secured ? 1 : 0));
		dest.writeByte((byte) (crcValid ? 1 : 0));
	}

	public static final Creator<ContinuousGlucoseMeasurementResponse> CREATOR = new Creator<ContinuousGlucoseMeasurementResponse>() {
		@Override
		public ContinuousGlucoseMeasurementResponse createFromParcel(final Parcel in) {
			return new ContinuousGlucoseMeasurementResponse(in);
		}

		@Override
		public ContinuousGlucoseMeasurementResponse[] newArray(final int size) {
			return new ContinuousGlucoseMeasurementResponse[size];
		}
	};
}
