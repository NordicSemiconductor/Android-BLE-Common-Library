package no.nordicsemi.android.ble.common.callback.csc;

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
 *     CyclingSpeedAndCadenceFeatureResponse response = readCharacteristic(characteristic)
 *           .awaitValid(CyclingSpeedAndCadenceFeatureResponse.class);
 *     CSCFeatures features = response.getFeatures();
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
public final class CyclingSpeedAndCadenceFeatureResponse extends CyclingSpeedAndCadenceFeatureDataCallback implements Parcelable {
	@Nullable
	private CSCFeatures features;

	public CyclingSpeedAndCadenceFeatureResponse() {
		// empty
	}

	@Override
	public void onCyclingSpeedAndCadenceFeaturesReceived(@NonNull final BluetoothDevice device,
														 @NonNull final CSCFeatures features) {
		this.features = features;
	}

	@Nullable
	public CSCFeatures getFeatures() {
		return features;
	}

	// Parcelable
	private CyclingSpeedAndCadenceFeatureResponse(final Parcel in) {
		super(in);
		if (in.readByte() == 0) {
			features = null;
		} else {
			features = new CSCFeatures(in.readInt());
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

	public static final Creator<CyclingSpeedAndCadenceFeatureResponse> CREATOR = new Creator<CyclingSpeedAndCadenceFeatureResponse>() {
		@Override
		public CyclingSpeedAndCadenceFeatureResponse createFromParcel(final Parcel in) {
			return new CyclingSpeedAndCadenceFeatureResponse(in);
		}

		@Override
		public CyclingSpeedAndCadenceFeatureResponse[] newArray(final int size) {
			return new CyclingSpeedAndCadenceFeatureResponse[size];
		}
	};
}
