package net.octacomm.sample.netty.dev.handler;

import net.octacomm.sample.netty.dev.ex.InvalidEndOfPacketException;
import net.octacomm.sample.netty.dev.ex.InvalidStartOfHeaderException;
import net.octacomm.sample.netty.dev.ex.NotSupprtedMessageIdException;
import net.octacomm.sample.netty.dev.msg.common.IncomingMessage;
import net.octacomm.sample.netty.dev.msg.common.MessageHeader;
import net.octacomm.sample.netty.dev.msg.common.MessageType;

import org.jboss.netty.buffer.ChannelBuffer;

public class DeviceDecoder extends AbstractUsnServerDecoder {

	/**
	 * 메시지 해더를 생성하고, 데이터를 입력한다.
	 * 
	 * @param buffer
	 * @throws InvalidStartOfHeaderException
	 * @throws InvalidDataSizeException
	 * @throws NotSupprtedMessageIdException
	 */
	public MessageHeader makeMessageHeader(ChannelBuffer buffer) 
			throws InvalidStartOfHeaderException, NotSupprtedMessageIdException {
		
		int stx = buffer.readUnsignedByte();
		
		if (stx != MessageHeader.STX) {
			throw new InvalidStartOfHeaderException(stx);
		}
		
		char cmd1 = (char) buffer.readUnsignedByte();
		char cmd2 = (char) buffer.readUnsignedByte();
		
		// header 설정
		MessageType msg = MessageType.valueOf(cmd1, cmd2);
		MessageHeader header = new MessageHeader(msg);
		
		header.setPayloadLength(buffer.readUnsignedShort());
		
		return header;
	}

	/**
	 * Body를 생성하는 과정에서 예외 발생시 Body 사이즈 만큼의 버퍼를 삭제한다.
	 */
	@Override
	public void discardBufferByFailBody(ChannelBuffer buffer, MessageHeader header) {
		// Message Size, Message ID (2bytes)
		buffer.readBytes(header.getRequiredBodySize());
	}


	@Override
	public boolean processChecksum(ChannelBuffer buffer, IncomingMessage incomingMessage) {
		int etx = buffer.readUnsignedByte();
		
		if (etx != 'E') {
			throw new InvalidEndOfPacketException(etx);
		}
		
		int checksum = buffer.readUnsignedByte();
		if (checksum == incomingMessage.checksum()) {
			return true;
		}
		return false;
	}

	/**
	 * Header를 생성하는 과정에서 예외 발생시 수신한 모든 버퍼를 삭제한다.
	 */
	@Override
	public void discardBufferByFailHeader(ChannelBuffer buffer) {
		buffer.readBytes(buffer.readableBytes());
	}
	
}
