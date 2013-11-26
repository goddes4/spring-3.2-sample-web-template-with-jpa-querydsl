package net.octacomm.sample.netty.dev.msg.common;

import java.lang.reflect.InvocationTargetException;

import lombok.Getter;
import net.octacomm.sample.netty.dev.ex.NotSupprtedMessageIdException;
import net.octacomm.sample.netty.dev.ex.ReflectiveOperationException;

public enum MessageType {
	CONNECTION_CHECK_REQUEST('A', 'R'), // 연결 체크
	;
	
	private Class<? extends IncomingMessage> incomingClass;
	@Getter	private char cmd1;
	@Getter	private char cmd2;
	private String desc;

	private MessageType(char cmd1, char cmd2) {
		this(cmd1, cmd2, null);
	}
	
	private MessageType(char cmd1, char cmd2, Class<? extends IncomingMessage> incomingClass) {
		this.cmd1 = cmd1;
		this.cmd2 = cmd2;
		this.incomingClass = incomingClass;
		
		desc = name() + String.format("{ cmd1:%c, cmd2:%c, class:%s }", cmd1, cmd2, (incomingClass == null ? null : incomingClass.getSimpleName()));
	}
	
	public String toString() {
		return desc;
	}
	
	public IncomingMessage newInstance(MessageHeader header) throws ReflectiveOperationException {
		try {
			return incomingClass.getConstructor(MessageHeader.class).newInstance(header);
		} catch (IllegalArgumentException e) {
			throw new ReflectiveOperationException(e);
		} catch (SecurityException e) {
			throw new ReflectiveOperationException(e);
		} catch (InstantiationException e) {
			throw new ReflectiveOperationException(e);
		} catch (IllegalAccessException e) {
			throw new ReflectiveOperationException(e);
		} catch (InvocationTargetException e) {
			throw new ReflectiveOperationException(e);
		} catch (NoSuchMethodException e) {
			throw new ReflectiveOperationException(e);
		}
	}

	public static MessageType valueOf(char cmd1, char cmd2) throws NotSupprtedMessageIdException {
		for (MessageType msg : MessageType.values()) {
			if (msg.cmd1 == cmd1 && msg.cmd2 == cmd2 && msg.incomingClass != null) {
				return msg;
			}
		}
		throw new NotSupprtedMessageIdException(cmd1 + ", " + cmd2);
	}
	
}
