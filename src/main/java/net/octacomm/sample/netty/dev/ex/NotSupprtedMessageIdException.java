package net.octacomm.sample.netty.dev.ex;

public class NotSupprtedMessageIdException extends RuntimeException {
	
	public NotSupprtedMessageIdException(String messageId) {
		super(messageId + " is invalid.");
	}
}
