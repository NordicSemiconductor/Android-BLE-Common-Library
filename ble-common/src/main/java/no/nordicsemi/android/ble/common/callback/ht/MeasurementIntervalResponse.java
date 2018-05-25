package no.nordicsemi.android.ble.common.callback.ht;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import no.nordicsemi.android.ble.exception.InvalidDataException;
import no.nordicsemi.android.ble.exception.RequestFailedException;

/**
 * Response class that could be used as a result of a synchronous request.
 * The data received are available through getters, instead of a callback.
 * <p>
 * Usage example:
 * <pre>
 * try {
 *     MeasurementIntervalResponse response = readCharacteristic(characteristic)
 *           .awaitValid(MeasurementIntervalResponse.class);
 *     int type = response.getType();
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
public final class MeasurementIntervalResponse extends MeasurementIntervalDataCallback implements Parcelable {
	private int interval;

	public MeasurementIntervalResponse() {
		// empty
	}

	@Override
	public void onMeasurementIntervalReceived(@NonNull final BluetoothDevice device,
											  final int interval) {
		this.interval = interval;
	}

	/**
	 * Returns the measurement interval in seconds.
	 *
	 * @return The measurement interval in seconds.
	 */
	public int getInterval() {
		return interval;
	}

	// Parcelable
	private MeasurementIntervalResponse(final Parcel in) {
		super(in);
		interval = in.readInt();
	}

	@Override
	public void writeToParcel(final Parcel dest, final int flags) {
		super.writeToParcel(dest, flags);
		dest.writeInt(interval);
	}

	public static final Creator<MeasurementIntervalResponse> CREATOR = new Creator<MeasurementIntervalResponse>() {
		@Override
		public MeasurementIntervalResponse createFromParcel(final Parcel in) {
			return new MeasurementIntervalResponse(in);
		}

		@Override
		public MeasurementIntervalResponse[] newArray(final int size) {
			return new MeasurementIntervalResponse[size];
		}
	};
}
