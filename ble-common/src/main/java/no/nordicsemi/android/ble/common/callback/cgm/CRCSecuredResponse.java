package no.nordicsemi.android.ble.common.callback.cgm;

public interface CRCSecuredResponse {

	/**
	 * Returns true if the packet was secured with E2E CRC.
	 * See {@link #isCrcValid()} to check if the CRC was valid.
	 *
	 * @return True if the packet was secured with E2E CRC.
	 */
	boolean isSecured();

	/**
	 * Returns true if the packet was secured with E2E CRC value and the CRC was valid.
	 * In all other cases it returns false.
	 *
	 * @return True if the packet was secured with E2E CRC value and the CRC was valid.
	 */
	boolean isCrcValid();
}
