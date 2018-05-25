package no.nordicsemi.android.ble.common.callback.hr;

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
 *     BodySensorLocationResponse response = readCharacteristic(characteristic)
 *           .awaitValid(BodySensorLocationResponse.class);
 *     int location = response.getSensorLocation();
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
public final class BodySensorLocationResponse extends BodySensorLocationDataCallback implements Parcelable {
	private int sensorLocation;

	public BodySensorLocationResponse() {
		// empty
	}

	@Override
	public void onBodySensorLocationReceived(@NonNull final BluetoothDevice device, final int sensorLocation) {
		this.sensorLocation = sensorLocation;
	}

	public int getSensorLocation() {
		return sensorLocation;
	}

	// Parcelable
	private BodySensorLocationResponse(final Parcel in) {
		super(in);
		sensorLocation = in.readInt();
	}

	@Override
	public void writeToParcel(final Parcel dest, final int flags) {
		super.writeToParcel(dest, flags);
		dest.writeInt(sensorLocation);
	}

	public static final Creator<BodySensorLocationResponse> CREATOR = new Creator<BodySensorLocationResponse>() {
		@Override
		public BodySensorLocationResponse createFromParcel(final Parcel in) {
			return new BodySensorLocationResponse(in);
		}

		@Override
		public BodySensorLocationResponse[] newArray(final int size) {
			return new BodySensorLocationResponse[size];
		}
	};
}
