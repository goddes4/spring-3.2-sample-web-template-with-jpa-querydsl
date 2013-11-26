package net.octacomm.sample.utils;

import org.jboss.netty.buffer.ChannelBuffer;

public class PrintUtil {
	public static String printReceivedChannelBuffer(String msg, ChannelBuffer buffer) {
		ChannelBuffer copyBuffer = buffer.copy();
		StringBuilder str = new StringBuilder();

		str.append(msg + " : ");
		while (copyBuffer.readable()) {
			str.append(String.format("%02X ", copyBuffer.readUnsignedByte()));
		}

		return str.toString();
	}
}
