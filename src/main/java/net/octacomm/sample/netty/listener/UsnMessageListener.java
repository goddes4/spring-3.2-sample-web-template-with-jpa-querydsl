package net.octacomm.sample.netty.listener;


public interface UsnMessageListener<T> {

	void connectionStateChanged(boolean isConnected);

	void messageReceived(String deviceSn, T message);
	
}
