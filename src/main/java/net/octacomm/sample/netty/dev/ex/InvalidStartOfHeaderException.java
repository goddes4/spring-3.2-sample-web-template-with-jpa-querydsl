package net.octacomm.sample.netty.dev.ex;

public class InvalidStartOfHeaderException extends RuntimeException {

	public InvalidStartOfHeaderException(int soh) {
		super(soh + " is invalid.");
	}
}
