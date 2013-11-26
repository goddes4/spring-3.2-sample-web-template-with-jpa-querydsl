package net.octacomm.sample.netty.listener;

public interface MessageSenderRegistry<T> {
	
	void setMessageSender(MessageSender<T> messageSender);
	
}
