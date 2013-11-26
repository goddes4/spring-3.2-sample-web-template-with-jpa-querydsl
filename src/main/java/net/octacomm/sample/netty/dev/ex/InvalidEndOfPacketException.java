package net.octacomm.sample.netty.dev.ex;

public class InvalidEndOfPacketException extends RuntimeException {

	public InvalidEndOfPacketException(int etx) {
		super(etx + " is invalid.");
	}
}
