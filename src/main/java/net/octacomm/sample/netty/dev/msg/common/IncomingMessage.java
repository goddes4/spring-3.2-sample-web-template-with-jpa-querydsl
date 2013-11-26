package net.octacomm.sample.netty.dev.msg.common;

import org.jboss.netty.buffer.ChannelBuffer;

public interface IncomingMessage {
	
	void decode(ChannelBuffer buffer);

	int checksum();

	MessageType getMessageType();
	
}
