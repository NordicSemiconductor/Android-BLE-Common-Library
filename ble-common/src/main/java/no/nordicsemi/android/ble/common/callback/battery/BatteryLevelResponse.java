package no.nordicsemi.android.ble.common.callback.battery;

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
 *     BatteryLevelResponse response = readCharacteristic(characteristic).awaitForValid(BatteryLevelResponse.class);
 *     int batteryLevel = response.getBatteryLevel();
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
public final class BatteryLevelResponse extends BatteryLevelDataCallback implements Parcelable {
	private int mBatteryLevel;

	public BatteryLevelResponse() {
		// empty
	}

	@Override
	public void onBatteryLevelChanged(@NonNull final BluetoothDevice device, final int batteryLevel) {
		mBatteryLevel = batteryLevel;
	}

	/**
	 * Returns the battery level value received.
	 *
	 * @return The battery level value received, in percent.
	 */
	public int getBatteryLevel() {
		return mBatteryLevel;
	}

	// Parcelable
	private BatteryLevelResponse(final Parcel in) {
		super(in);
		mBatteryLevel = in.readInt();
	}

	@Override
	public void writeToParcel(final Parcel dest, final int flags) {
		super.writeToParcel(dest, flags);
		dest.writeInt(mBatteryLevel);
	}

	public static final Creator<BatteryLevelResponse> CREATOR = new Creator<BatteryLevelResponse>() {
		@Override
		public BatteryLevelResponse createFromParcel(final Parcel in) {
			return new BatteryLevelResponse(in);
		}

		@Override
		public BatteryLevelResponse[] newArray(final int size) {
			return new BatteryLevelResponse[size];
		}
	};
}
