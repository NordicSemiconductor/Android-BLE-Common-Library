package no.nordicsemi.android.ble.common.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class CRC16Test {

	@Test
	public void CCITT_Kermit_A() {
		final byte[] data = new byte[] { 'A' };
		assertEquals(0x8D53, CRC16.CCITT_Kermit(data, 0, 1));
	}

	@Test
	public void CCITT_Kermit_123456789() {
		final byte[] data = "123456789".getBytes();
		assertEquals(0x8921, CRC16.CCITT_Kermit(data, 0, 9));
	}

	@Test
	public void CCITT_Kermit_123456789_offset() {
		final byte[] data = "SPACE123456789".getBytes();
		assertEquals(0x8921, CRC16.CCITT_Kermit(data, 5, 9));
	}

	@Test
	public void CCITT_Kermit_empty() {
		final byte[] data = new byte[0];
		assertEquals(0x0000, CRC16.CCITT_Kermit(data, 0, 1));
	}

	@Test
	public void CCITT_FALSE_A() {
		final byte[] data = new byte[] { 'A' };
		assertEquals(0xB915, CRC16.CCITT_FALSE(data, 0, 1));
	}

	@Test
	public void CCITT_FALSE_123456789() {
		final byte[] data = "123456789".getBytes();
		assertEquals(0x29B1, CRC16.CCITT_FALSE(data, 0, 9));
	}

	@Test
	public void CCITT_FALSE_A_offset() {
		final byte[] data = "1234567A89".getBytes();
		assertEquals(0xB915, CRC16.CCITT_FALSE(data, 7, 1));
	}

	@Test
	public void CCITT_FALSE_empty() {
		final byte[] data = new byte[0];
		assertEquals(0xFFFF, CRC16.CCITT_FALSE(data, 0, 0));
	}

	@Test
	public void CCITT_1D0F_A() {
		final byte[] data = new byte[] { 'A' };
		assertEquals(0x9479, CRC16.CCITT_1D9F(data, 0, 1));
	}

	@Test
	public void CCITT_1D0F_123456789() {
		final byte[] data = "123456789".getBytes();
		assertEquals(0xE5CC, CRC16.CCITT_1D9F(data, 0, 9));
	}

	@Test
	public void CCITT_1D0F_empty() {
		final byte[] data = new byte[0];
		assertEquals(0x1D0F, CRC16.CCITT_1D9F(data, 0, 9));
	}
}