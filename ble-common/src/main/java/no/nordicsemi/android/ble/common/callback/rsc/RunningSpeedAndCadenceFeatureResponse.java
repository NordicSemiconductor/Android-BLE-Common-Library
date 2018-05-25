package no.nordicsemi.android.ble.common.callback.rsc;

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
 *     RunningSpeedAndCadenceFeatureResponse response = readCharacteristic(characteristic)
 *           .awaitValid(RunningSpeedAndCadenceFeatureResponse.class);
 *     RSCFeatures features = response.getFeatures();
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
public class RunningSpeedAndCadenceFeatureResponse extends RunningSpeedAndCadenceFeatureDataCallback implements Parcelable {
	@Nullable
	private RSCFeatures features;

	public RunningSpeedAndCadenceFeatureResponse() {
		// empty
	}

	@Override
	public void onRunningSpeedAndCadenceFeaturesReceived(@NonNull final BluetoothDevice device,
														 @NonNull final RSCFeatures features) {
		this.features = features;
	}

	@Nullable
	public RSCFeatures getFeatures() {
		return features;
	}

	// Parcelable
	private RunningSpeedAndCadenceFeatureResponse(final Parcel in) {
		super(in);
		if (in.readByte() == 0) {
			features = null;
		} else {
			features = new RSCFeatures(in.readInt());
		}
	}

	@Override
	public void writeToParcel(final Parcel dest, final int flags) {
		super.writeToParcel(dest, flags);
		if (features == null) {
			dest.writeByte((byte) 0);
		} else {
			dest.writeByte((byte) 1);
			dest.writeInt(features.value);
		}
	}

	public static final Creator<RunningSpeedAndCadenceFeatureResponse> CREATOR = new Creator<RunningSpeedAndCadenceFeatureResponse>() {
		@Override
		public RunningSpeedAndCadenceFeatureResponse createFromParcel(final Parcel in) {
			return new RunningSpeedAndCadenceFeatureResponse(in);
		}

		@Override
		public RunningSpeedAndCadenceFeatureResponse[] newArray(final int size) {
			return new RunningSpeedAndCadenceFeatureResponse[size];
		}
	};
}
