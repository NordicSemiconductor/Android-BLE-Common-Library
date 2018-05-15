package no.nordicsemi.android.ble.common.callback.cgm;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;

import no.nordicsemi.android.ble.callback.profile.ProfileReadResponse;
import no.nordicsemi.android.ble.common.profile.cgm.CGMStatusCallback;
import no.nordicsemi.android.ble.common.util.CRC16;
import no.nordicsemi.android.ble.data.Data;

/**
 * Data callback that parses value into CGM Status data.
 * If the value received do not match required syntax
 * {@link #onInvalidDataReceived(BluetoothDevice, Data)} callback will be called.
 * If the device supports E2E CRC validation and the CRC is not valid, the
 * {@link #onContinuousGlucoseMonitorStatusReceivedWithCrcError(BluetoothDevice, Data)}
 * will be called.
 * See: https://www.bluetooth.com/specifications/gatt/viewer?attributeXmlFile=org.bluetooth.characteristic.cgm_status.xml
 */
@SuppressWarnings("ConstantConditions")
public abstract class CGMStatusDataCallback extends ProfileReadResponse implements CGMStatusCallback {

	@Override
	public void onDataReceived(@NonNull final BluetoothDevice device, @NonNull final Data data) {
		super.onDataReceived(device, data);
		
		if (data.size() != 5 && data.size() != 7) {
			onInvalidDataReceived(device, data);
			return;
		}

		final int timeOffset = data.getIntValue(Data.FORMAT_UINT16, 0);
		final int warningStatus = data.getIntValue(Data.FORMAT_UINT8, 2);
		final int calibrationTempStatus = data.getIntValue(Data.FORMAT_UINT8, 3);
		final int sensorStatus = data.getIntValue(Data.FORMAT_UINT8, 4);

		final boolean crcPresent = data.size() == 7;
		if (crcPresent) {
			final int actualCrc = CRC16.MCRF4XX(data.getValue(), 0, 5);
			final int expectedCrc = data.getIntValue(Data.FORMAT_UINT16, 5);
			if (actualCrc != expectedCrc) {
				onContinuousGlucoseMonitorStatusReceivedWithCrcError(device, data);
				return;
			}
		}

		final CGMStatus status = new CGMStatus(warningStatus, calibrationTempStatus, sensorStatus);
		onContinuousGlucoseMonitorStatusChanged(device, status, timeOffset, crcPresent);
	}
}
