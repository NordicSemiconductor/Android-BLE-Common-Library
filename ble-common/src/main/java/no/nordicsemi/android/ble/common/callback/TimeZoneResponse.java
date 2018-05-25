package no.nordicsemi.android.ble.common.callback;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import no.nordicsemi.android.ble.exception.InvalidDataException;
import no.nordicsemi.android.ble.exception.RequestFailedException;

/**
 * Response class that could be used as a result of a synchronous request.
 * The data received are available through getters instead of a callback.
 * <p>
 * Usage example:
 * <pre>
 * try {
 *     TimeZoneResponse response = readCharacteristic(characteristic).awaitForValid(TimeZoneResponse.class);
 *     if (response.isTimeZoneOffsetKnown()) {
 *         int offset = response.getTimeZoneOffset();
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
public final class TimeZoneResponse extends TimeZoneDataCallback implements Parcelable {
	private int timeZoneOffset;
	private boolean timeZoneOffsetKnown;

	public TimeZoneResponse() {
		// empty
	}

	@Override
	public void onTimeZoneReceived(@NonNull final BluetoothDevice device, final int offset) {
		this.timeZoneOffset = offset;
		this.timeZoneOffsetKnown = true;
	}

	@Override
	public void onUnknownTimeZoneReceived(@NonNull final BluetoothDevice device) {
		this.timeZoneOffsetKnown = false;
	}

	public int getTimeZoneOffset() {
		return timeZoneOffset;
	}

	public boolean isTimeZoneOffsetKnown() {
		return timeZoneOffsetKnown;
	}

	// Parcelable
	private TimeZoneResponse(final Parcel in) {
		super(in);
		timeZoneOffset = in.readInt();
		timeZoneOffsetKnown = in.readByte() != 0;
	}

	@Override
	public void writeToParcel(final Parcel dest, final int flags) {
		super.writeToParcel(dest, flags);
		dest.writeInt(timeZoneOffset);
		dest.writeByte((byte) (timeZoneOffsetKnown ? 1 : 0));
	}

	public static final Creator<TimeZoneResponse> CREATOR = new Creator<TimeZoneResponse>() {
		@Override
		public TimeZoneResponse createFromParcel(final Parcel in) {
			return new TimeZoneResponse(in);
		}

		@Override
		public TimeZoneResponse[] newArray(final int size) {
			return new TimeZoneResponse[size];
		}
	};
}
