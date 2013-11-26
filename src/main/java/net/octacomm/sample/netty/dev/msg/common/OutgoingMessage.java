package net.octacomm.sample.netty.dev.msg.common;

import org.jboss.netty.buffer.ChannelBuffer;

public interface OutgoingMessage {

	void encode(ChannelBuffer buffer);

	int checksum();
	
	MessageType getMessageType();
	
	int getRequireBodySize();

}
