package net.octacomm.sample.netty.dev.handler;

import java.nio.ByteOrder;

import net.octacomm.sample.netty.dev.msg.common.MessageHeader;
import net.octacomm.sample.netty.dev.msg.common.OutgoingMessage;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

public class DeviceEncoder extends AbstractUsnServerEncoder {

	public ChannelBuffer encode(OutgoingMessage message) {
		int bufferLength = MessageHeader.getRequiredHeaderSize() + message.getRequireBodySize() + MessageHeader.MESSAGE_FOOTER_LENGTH;
		ChannelBuffer writeBuffer = ChannelBuffers.directBuffer(ByteOrder.BIG_ENDIAN, bufferLength);
		
		// Header (4bytes)
		writeBuffer.writeByte(MessageHeader.STX);
		writeBuffer.writeByte(message.getMessageType().getCmd1());
		writeBuffer.writeByte(message.getMessageType().getCmd2());
		writeBuffer.writeShort(message.getRequireBodySize());
		// Body
		message.encode(writeBuffer);
		// Footer (2bytes)
		writeBuffer.writeByte(MessageHeader.ETX);
		writeBuffer.writeByte(message.checksum());
		
		return writeBuffer;
	}
}
