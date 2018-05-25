package no.nordicsemi.android.ble.common.callback.glucose;

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
 *     GlucoseFeatureResponse response = readCharacteristic(characteristic)
 *           .awaitValid(GlucoseFeatureResponse.class);
 *     GlucoseFeatures features = response.getFeatures();
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
public final class GlucoseFeatureResponse extends GlucoseFeatureDataCallback implements Parcelable {
	@Nullable
	private GlucoseFeatures features;

	public GlucoseFeatureResponse() {
		// empty
	}

	@Override
	public void onGlucoseFeaturesReceived(@NonNull final BluetoothDevice device, @NonNull final GlucoseFeatures features) {
		this.features = features;
	}

	@Nullable
	public GlucoseFeatures getFeatures() {
		return features;
	}

	// Parcelable
	private GlucoseFeatureResponse(final Parcel in) {
		super(in);
		if (in.readByte() == 0) {
			features = null;
		} else {
			features = new GlucoseFeatures(in.readInt());
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

	public static final Creator<GlucoseFeatureResponse> CREATOR = new Creator<GlucoseFeatureResponse>() {
		@Override
		public GlucoseFeatureResponse createFromParcel(final Parcel in) {
			return new GlucoseFeatureResponse(in);
		}

		@Override
		public GlucoseFeatureResponse[] newArray(final int size) {
			return new GlucoseFeatureResponse[size];
		}
	};
}
