package net.octacomm.sample.netty.dev.msg.common;

public interface MessageGenerator {

	String generateMessage();

	MessageType getResponseMessageType();
	
}
