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
 *     TemperatureTypeResponse response = readCharacteristic(characteristic)
 *           .awaitValid(TemperatureTypeResponse.class);
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
public final class TemperatureTypeResponse extends TemperatureTypeDataCallback implements Parcelable {
	private int type;

	public TemperatureTypeResponse() {
		// empty
	}

	@Override
	public void onTemperatureTypeReceived(@NonNull final BluetoothDevice device, final int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}

	// Parcelable
	private TemperatureTypeResponse(final Parcel in) {
		super(in);
		type = in.readInt();
	}

	@Override
	public void writeToParcel(final Parcel dest, final int flags) {
		super.writeToParcel(dest, flags);
		dest.writeInt(type);
	}

	public static final Creator<TemperatureTypeResponse> CREATOR = new Creator<TemperatureTypeResponse>() {
		@Override
		public TemperatureTypeResponse createFromParcel(final Parcel in) {
			return new TemperatureTypeResponse(in);
		}

		@Override
		public TemperatureTypeResponse[] newArray(final int size) {
			return new TemperatureTypeResponse[size];
		}
	};
}
