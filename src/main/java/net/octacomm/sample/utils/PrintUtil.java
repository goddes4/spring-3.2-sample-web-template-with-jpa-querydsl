package net.octacomm.sample.utils;

import org.jboss.netty.buffer.ChannelBuffer;

public class PrintUtil {
	public static String printReceivedChannelBuffer(String msg, ChannelBuffer buffer) {
		StringBuilder str = new StringBuilder();

		str.append(msg + " : ");
		for (int i = 0; i < buffer.readableBytes(); i++) {
			str.append(String.format("%02X ", buffer.getUnsignedByte(i)));
		}
		return str.toString();
	}
}
