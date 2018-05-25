package no.nordicsemi.android.ble.common.callback.sc;

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
 *     SensorLocationResponse response = readCharacteristic(characteristic)
 *           .awaitValid(SensorLocationResponse.class);
 *     int location = response.getLocation();
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
public final class SensorLocationResponse extends SensorLocationDataCallback implements Parcelable {
	private int location;

	public SensorLocationResponse() {
		// empty
	}

	@Override
	public void onSensorLocationReceived(@NonNull final BluetoothDevice device, final int location) {
		this.location = location;
	}

	public int getLocation() {
		return location;
	}

	// Parcelable
	private SensorLocationResponse(final Parcel in) {
		super(in);
		location = in.readInt();
	}

	@Override
	public void writeToParcel(final Parcel dest, final int flags) {
		super.writeToParcel(dest, flags);
		dest.writeInt(location);
	}

	public static final Creator<SensorLocationResponse> CREATOR = new Creator<SensorLocationResponse>() {
		@Override
		public SensorLocationResponse createFromParcel(final Parcel in) {
			return new SensorLocationResponse(in);
		}

		@Override
		public SensorLocationResponse[] newArray(final int size) {
			return new SensorLocationResponse[size];
		}
	};
}
