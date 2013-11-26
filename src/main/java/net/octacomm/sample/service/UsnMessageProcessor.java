package net.octacomm.sample.service;

import net.octacomm.logger.Log;
import net.octacomm.sample.netty.dev.msg.common.IncomingMessage;
import net.octacomm.sample.netty.dev.msg.common.OutgoingMessage;
import net.octacomm.sample.netty.listener.MessageSender;
import net.octacomm.sample.netty.listener.UsnMessageListener;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class UsnMessageProcessor implements UsnMessageListener<IncomingMessage>  {

	@Log private Logger logger;
	
	@Autowired
	@Qualifier("usn")
	private MessageSender<OutgoingMessage> usnMessageSender;

	@Override
	public void messageReceived(String deviceSn, IncomingMessage packet) {
		switch (packet.getMessageType()) {

		default:
			throw new UnsupportedOperationException();
		}
		
	}

	@Override
	public void connectionStateChanged(boolean isConnected) {

	}
	
}
