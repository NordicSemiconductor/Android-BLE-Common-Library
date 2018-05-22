package no.nordicsemi.android.ble.common.profile.sc;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;

@SuppressWarnings("unused")
public interface SpeedAndCadenceControlPointCallback extends SensorLocationTypes {
	int SC_OP_CODE_SET_CUMULATIVE_VALUE = 1;
	int SC_OP_CODE_START_SENSOR_CALIBRATION = 2;
	int SC_OP_CODE_UPDATE_SENSOR_LOCATION = 3;
	int SC_OP_CODE_REQUEST_SUPPORTED_SENSOR_LOCATIONS = 4;

	// int SC_RESPONSE_SUCCESS = 1;
	int SC_ERROR_OP_CODE_NOT_SUPPORTED = 2;
	int SC_ERROR_INVALID_PARAMETER = 3;
	int SC_ERROR_OPERATION_FAILED = 4;

	/**
	 * Callback called when a SC Control Point request has finished successfully.
	 * In case of {@link #SC_OP_CODE_REQUEST_SUPPORTED_SENSOR_LOCATIONS} request, the
	 * {@link #onSupportedSensorLocationsReceived(BluetoothDevice, int[])} will be called instead.
	 *
	 * @param device      target device.
	 * @param requestCode request code that has completed. One of SC_OP_CODE_* constants.
	 */
	void onSCOperationCompleted(@NonNull final BluetoothDevice device, final int requestCode);

	/**
	 * Callback called when a SC Control Point request has failed.
	 *
	 * @param device      target device.
	 * @param requestCode request code that has completed with an error. One of SC_OP_CODE_* constants,
	 *                    or other if such was requested.
	 * @param errorCode   the received error code, see SC_ERROR_* constants.
	 */
	void onSCOperationError(@NonNull final BluetoothDevice device, final int requestCode,
							final int errorCode);

	/**
	 * Callback indicating successful response for
	 * {@link #SC_OP_CODE_REQUEST_SUPPORTED_SENSOR_LOCATIONS} request.
	 *
	 * @param device    target device.
	 * @param locations array of supported locations. See SENSOR_LOCATION_* constants.
	 */
	void onSupportedSensorLocationsReceived(@NonNull final BluetoothDevice device,
											@NonNull final int[] locations);
}
