package net.octacomm.sample.netty.dev.handler;

import net.octacomm.sample.netty.dev.msg.common.OutgoingMessage;
import net.octacomm.sample.utils.PrintUtil;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractUsnServerEncoder extends OneToOneEncoder {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	protected Object encode(ChannelHandlerContext ctx, Channel channel, Object msg) {
		if (msg instanceof OutgoingMessage){
			OutgoingMessage message = (OutgoingMessage)msg;
			logger.debug("[Outgoing] {} => {}", channel, message);
			ChannelBuffer writeBuffer = encode(message);
			
			logger.debug(PrintUtil.printReceivedChannelBuffer("out", writeBuffer));
			return writeBuffer;
		} else {
			throw new UnsupportedOperationException("Supported Message is only OutgoingMessage.");
		}
	}

	/**
	 * 1. 데이터 전송에 필요한 ChannelBuffer를 생성한다.
	 * 2. 생성한 buffer에 데이터를 담고, 반환한다.
	 * 
	 * @param message
	 * @return
	 */
	public abstract ChannelBuffer encode(OutgoingMessage message);

}
