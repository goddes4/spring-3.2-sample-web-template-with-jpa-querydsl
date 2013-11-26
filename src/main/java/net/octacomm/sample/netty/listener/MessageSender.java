package net.octacomm.sample.netty.listener;

import java.util.concurrent.Future;

import net.octacomm.sample.netty.dev.msg.common.IncomingMessage;

public interface MessageSender<T> {
	
	void forceClose();
	
	boolean isConnected();
	
	IncomingMessage sendSyncMessage(T packet);
	
	Future<Boolean> sendAsyncMessage(T packet);
	
}
