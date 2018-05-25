package no.nordicsemi.android.ble.common.callback;

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
 *     DSTOffsetResponse response = readCharacteristic(characteristic).awaitValid(DSTOffsetResponse.class);
 *     DSTOffset offset = response.getDSTOffset();
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
public final class DSTOffsetResponse extends DSTOffsetDataCallback implements Parcelable {
	private DSTOffset offset;

	public DSTOffsetResponse() {
		// empty
	}

	@Override
	public void onDSTOffsetReceived(@NonNull final BluetoothDevice device, @NonNull final DSTOffset offset) {
		this.offset = offset;
	}

	public DSTOffset getDSTOffset() {
		return offset;
	}

	// Parcelable
	private DSTOffsetResponse(final Parcel in) {
		super(in);
		if (in.readByte() == 0) {
			offset = null;
		} else {
			offset = DSTOffset.values()[in.readInt()];
		}
	}

	@Override
	public void writeToParcel(final Parcel dest, final int flags) {
		super.writeToParcel(dest, flags);
		if (offset == null) {
			dest.writeByte((byte) 0);
		} else {
			dest.writeByte((byte) 1);
			dest.writeInt(offset.ordinal());
		}
	}

	public static final Creator<DSTOffsetResponse> CREATOR = new Creator<DSTOffsetResponse>() {
		@Override
		public DSTOffsetResponse createFromParcel(final Parcel in) {
			return new DSTOffsetResponse(in);
		}

		@Override
		public DSTOffsetResponse[] newArray(final int size) {
			return new DSTOffsetResponse[size];
		}
	};
}
