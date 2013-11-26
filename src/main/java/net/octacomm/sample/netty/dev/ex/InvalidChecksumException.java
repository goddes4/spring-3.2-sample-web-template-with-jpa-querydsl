package net.octacomm.sample.netty.dev.ex;


public class InvalidChecksumException extends RuntimeException {

	public InvalidChecksumException(int checksum) {
		super("checksum : " + String.format("0x%02X", checksum));
	}
}
