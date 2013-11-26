package net.octacomm.sample.netty.dev.handler;

import java.util.ArrayList;
import java.util.List;

import net.octacomm.sample.netty.dev.ex.InvalidChecksumException;
import net.octacomm.sample.netty.dev.ex.ReflectiveOperationException;
import net.octacomm.sample.netty.dev.msg.common.IncomingMessage;
import net.octacomm.sample.netty.dev.msg.common.MessageHeader;
import net.octacomm.sample.utils.PrintUtil;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractUsnServerDecoder extends FrameDecoder{
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private MessageHeader header;
	
	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) {
		Object ret = decode(buffer);
		logger.debug(PrintUtil.printReceivedChannelBuffer("After decoding", buffer));		
		return ret;
	}

	private List<IncomingMessage> decode(ChannelBuffer buffer) {
		List<IncomingMessage> packetList = new ArrayList<IncomingMessage>();

		logger.debug(PrintUtil.printReceivedChannelBuffer("in", buffer));

		while (buffer.readable()) {
			// 이전 수신 처리에서 Header 까지 완료 되었는지 확인
			if (header == null) {
				// Header를 처리하기 위한 데이터가 있는지 확인
				if (buffer.readableBytes() < MessageHeader.getRequiredHeaderSize()) {
					return completePacket(packetList);
				}
				
				try {
					header = makeMessageHeader(buffer);
				} catch (RuntimeException e) {
					discardBufferByFailHeader(buffer);
					logger.error("", e);
					continue;
				}
			}
			
			// Body 를 처리하기 위한 데이터가 있는지 확인
			if (buffer.readableBytes() < header.getRequiredBodySize()) {
				return completePacket(packetList);
			}
	
			try {
				packetList.add(makeMessageBody(buffer));
			} catch (InvalidChecksumException e) {
				logger.error("", e);
			} catch (ReflectiveOperationException e) {
				logger.error("", e);
			} catch (RuntimeException e) {
				header = null;
				throw e;
			}

			// 패킷이 완성이 되면 리스트에 저장하고, 반드시 Header는 null로 초기화 해야 한다.
			header = null;
		}
		
		return completePacket(packetList);
	}

	/**
	 * 메시지 해더를 생성하고, 데이터를 입력한다.
	 * ex1) 현재 수신한 모든 버퍼를 삭제한다.
	 * ex2) 예외를 발생시켜 채널을 종료한다.
	 * 
	 * @param buffer
	 */
	public abstract MessageHeader makeMessageHeader(ChannelBuffer buffer);

	/**
	 * 버퍼를 이용해 IncomingMessage 생성한다.
	 * 예외가 발생할 경우 size만큼 데이터를 읽어서 버린다.
	 * Checksum 비교
	 *  
	 * @param buffer
	 */
	private IncomingMessage makeMessageBody(ChannelBuffer buffer) throws InvalidChecksumException, ReflectiveOperationException {
		IncomingMessage incomingMessage;
		
		try {
			incomingMessage = header.getMessageType().newInstance(header);
		} catch (ReflectiveOperationException e) {
			discardBufferByFailBody(buffer, header);
			throw e;
		}
		incomingMessage.decode(buffer);
		
		if (!processChecksum(buffer, incomingMessage)) {
			throw new InvalidChecksumException(incomingMessage.checksum());
		}
		return incomingMessage;
	}
	
	/**
	 * Header 처리시 예외가 발생 했을 경우 buffer 처리
	 * 
	 * @param buffer
	 */
	public abstract void discardBufferByFailHeader(ChannelBuffer buffer);
	
	/**
	 * Body 처리시 예외가 발생 했을 경우 buffer 처리
	 * ex1) 현재 수신한 모든 버퍼를 삭제한다.
	 * ex2) 예외를 발생시켜 채널을 종료한다.
	 * ex3) BodySize 를 버퍼에서 삭제한다. (뒤에 메시지를 보존하기 위해)
	 * 
	 * @param buffer
	 * @param header
	 */
	public abstract void discardBufferByFailBody(ChannelBuffer buffer, MessageHeader header);

	/**
	 * USN 메시지가 checksum 이 존재 할경우 해당 패킷을 처리한다.
	 * 
	 * @param buffer
	 * @param incomingMessage
	 * @return
	 */
	public abstract boolean processChecksum(ChannelBuffer buffer, IncomingMessage incomingMessage);
	
	/**
	 * 완성된 패킷이 한개 이상 있을경우 상위 Decoder에 처리를 넘긴다.
	 * 
	 * @param packetList
	 * @return
	 */
	private List<IncomingMessage> completePacket(List<IncomingMessage> packetList) {
		if (packetList.size() > 0) {
			return packetList;
		}
		return null;
	}
}
