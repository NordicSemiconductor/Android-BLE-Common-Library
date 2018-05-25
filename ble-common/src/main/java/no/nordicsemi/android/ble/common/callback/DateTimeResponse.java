package no.nordicsemi.android.ble.common.callback;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

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
 *     DateTimeResponse response = readCharacteristic(characteristic).awaitValid(DateTimeResponse.class);
 *     Calendar calendar = response.getCalendar();
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
public final class DateTimeResponse extends DateTimeDataCallback implements Parcelable {
	private Calendar calendar;

	public DateTimeResponse() {
		// empty
	}

	@Override
	public void onDateTimeReceived(@NonNull final BluetoothDevice device, @NonNull final Calendar calendar) {
		this.calendar = calendar;
	}

	public Calendar getCalendar() {
		return calendar;
	}

	// Parcelable
	private DateTimeResponse(final Parcel in) {
		super(in);
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
		if (calendar == null) {
			dest.writeByte((byte) 0);
		} else {
			dest.writeByte((byte) 1);
			dest.writeLong(calendar.getTimeInMillis());
		}
	}

	public static final Creator<DateTimeResponse> CREATOR = new Creator<DateTimeResponse>() {
		@Override
		public DateTimeResponse createFromParcel(final Parcel in) {
			return new DateTimeResponse(in);
		}

		@Override
		public DateTimeResponse[] newArray(final int size) {
			return new DateTimeResponse[size];
		}
	};
}
