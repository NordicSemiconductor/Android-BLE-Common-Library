package no.nordicsemi.android.ble.common.util;

import android.support.annotation.NonNull;

/**
 * CRC-16 class is a helper that calculates different types of CRC.
 * Testing is based on https://www.lammertbies.nl/comm/info/crc-calculation.html
 */
public final class CRC16 {

	private CRC16() {
		// empty private constructor
	}

	/**
	 * Calculates CRC CCITT (Kermit) over given range of bytes from the block of data.
	 * It is using the 0x1021 polynomial and 0x0000 initial value.
	 * <p>
	 * See: http://reveng.sourceforge.net/crc-catalogue/16.htm#crc.cat.kermit
	 *
	 * @param data   The input data block for computation.
	 * @param offset Offset from where the range starts.
	 * @param length Length of the range in bytes.
	 * @return the CRC-16 CCITT (Kermit_.
	 */
	public static int CCITT_Kermit(final @NonNull byte[] data, final int offset, final int length) {
		final int polynomial = 0x8408; // reversed 0x1021
		int crc = 0x0000;

		for (int index = offset; index < offset + length && index < data.length; index++) {
			crc ^= (data[index] & 0xFF);
			for (int i = 0; i < 8; ++i) {
				boolean bit = (crc & 1) == 1;
				crc >>= 1;
				if (bit) crc ^= polynomial;
			}

		}
		// Swap bytes order
		crc = ((crc & 0xFF00) >> 8) | (crc << 8);
		return crc & 0xFFFF;
	}

	/**
	 * Calculates CRC CCITT-FALSE over given range of bytes from the block of data.
	 * It is using the 0x1021 polynomial and 0xFFFF initial value.
	 * <p>
	 * See: http://reveng.sourceforge.net/crc-catalogue/16.htm#crc.cat.crc-16-ccitt-false
	 * See: http://srecord.sourceforge.net/crc16-ccitt.html
	 *
	 * @param data   The input data block for computation.
	 * @param offset Offset from where the range starts.
	 * @param length Length of the range in bytes.
	 * @return the CRC-16 CCITT-FALSE.
	 */
	public static int CCITT_FALSE(final @NonNull byte[] data, final int offset, final int length) {
		int crc = 0xFFFF;

		for (int i = offset; i < offset + length && i < data.length; ++i) {
			crc = (((crc & 0xFFFF) >> 8) | (crc << 8));
			crc ^= data[i];
			crc ^= (crc & 0xFF) >> 4;
			crc ^= (crc << 8) << 4;
			crc ^= ((crc & 0xFF) << 4) << 1;
		}

		// Other implementation of the same algorithm:
//		final int polynomial = 0x1021;
//		int crc = 0xFFFF;
//
//		for (int i = offset; i < offset + length && i < data.length; ++i) {
//			final byte b = data[i];
//			for (int j = 0; j < 8; j++) {
//				boolean bit = ((b >> (7 - j) & 1) == 1);
//				boolean c15 = ((crc >> 15 & 1) == 1);
//				crc <<= 1;
//				if (c15 ^ bit) crc ^= polynomial;
//			}
//		}

		return crc & 0xFFFF;
	}

	/**
	 * Calculates CRC CCITT-1D0F over given range of bytes from the block of data.
	 * It is using the 0x1021 polynomial and 0x1D0F initial value.
	 * <p>
	 * See: http://reveng.sourceforge.net/crc-catalogue/16.htm#crc.cat.crc-16-ccitt-false
	 * See: http://srecord.sourceforge.net/crc16-ccitt.html
	 *
	 * @param data   The input data block for computation.
	 * @param offset Offset from where the range starts.
	 * @param length Length of the range in bytes.
	 * @return the CRC-16 CCITT-1D0F.
	 */
	public static int CCITT_1D9F(final @NonNull byte[] data, final int offset, final int length) {
		final int polynomial = 0x1021;
		int crc = 0x1D0F;

		for (int i = offset; i < offset + length && i < data.length; ++i) {
			final byte b = data[i];
			for (int j = 0; j < 8; j++) {
				boolean bit = ((b >> (7 - j) & 1) == 1);
				boolean c15 = ((crc >> 15 & 1) == 1);
				crc <<= 1;
				if (c15 ^ bit) crc ^= polynomial;
			}
		}

		return crc & 0xFFFF;
	}
}
