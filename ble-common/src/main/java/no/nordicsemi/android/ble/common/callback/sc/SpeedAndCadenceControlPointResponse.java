package no.nordicsemi.android.ble.common.callback.sc;

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
 *     SpeedAndCadenceControlPointResponse response = setIndicationCallback(characteristic)
 *           .awaitValidAfter(() -> writeCharacteristic(characteristic, SpeedAndCadenceControlPointData.requestSupportedSensorLocations(),
 *           SpeedAndCadenceControlPointResponse.class);
 *     if (response.isOperationCompleted()) {
 *         int locations = response.getSupportedSensorLocations();
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
public final class SpeedAndCadenceControlPointResponse extends SpeedAndCadenceControlPointDataCallback implements Parcelable {
	private boolean operationCompleted;
	private int requestCode;
	private int errorCode;
	private int[] locations;

	public SpeedAndCadenceControlPointResponse() {
		// empty
	}

	@Override
	public void onSCOperationCompleted(@NonNull final BluetoothDevice device, final int requestCode) {
		this.operationCompleted = true;
		this.requestCode = requestCode;
	}

	@Override
	public void onSCOperationError(@NonNull final BluetoothDevice device, final int requestCode, final int errorCode) {
		this.operationCompleted = false;
		this.requestCode = requestCode;
		this.errorCode = errorCode;
	}

	@Override
	public void onSupportedSensorLocationsReceived(@NonNull final BluetoothDevice device, @NonNull final int[] locations) {
		this.operationCompleted = true;
		this.requestCode = SC_OP_CODE_REQUEST_SUPPORTED_SENSOR_LOCATIONS;
		this.locations = locations;
	}

	public boolean isOperationCompleted() {
		return operationCompleted;
	}

	public int getRequestCode() {
		return requestCode;
	}

	public int getErrorCode() {
		return errorCode;
	}

	@Nullable
	public int[] getSupportedSensorLocations() {
		return locations;
	}

	// Parcelable
	private SpeedAndCadenceControlPointResponse(final Parcel in) {
		super(in);
		operationCompleted = in.readByte() != 0;
		requestCode = in.readInt();
		errorCode = in.readInt();
		locations = in.createIntArray();
	}

	@Override
	public void writeToParcel(final Parcel dest, final int flags) {
		super.writeToParcel(dest, flags);
		dest.writeByte((byte) (operationCompleted ? 1 : 0));
		dest.writeInt(requestCode);
		dest.writeInt(errorCode);
		dest.writeIntArray(locations);
	}

	public static final Creator<SpeedAndCadenceControlPointResponse> CREATOR = new Creator<SpeedAndCadenceControlPointResponse>() {
		@Override
		public SpeedAndCadenceControlPointResponse createFromParcel(final Parcel in) {
			return new SpeedAndCadenceControlPointResponse(in);
		}

		@Override
		public SpeedAndCadenceControlPointResponse[] newArray(final int size) {
			return new SpeedAndCadenceControlPointResponse[size];
		}
	};
}
