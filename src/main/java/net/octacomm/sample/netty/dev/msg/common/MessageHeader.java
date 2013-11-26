package net.octacomm.sample.netty.dev.msg.common;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 1. 해더에 포함되는 필드를 선언한다.
 * 2. getRequiredHeaderSize() 와 getRequiredBodySize() 를 구현한다.
 * 3. 해더의 필드에서 계산되는 checksum() 을 구현한다.
 * 
 * @author taeyo
 *
 */
@ToString
public class MessageHeader {
	
	public static final char STX = 'S';
	public static final char ETX = 'E';
	
	// STX : 1 byte, CMD : 2 byte, LEN : 2 byte
	public static final int MESSAGE_HEADER_LENGTH = 5;
	// ETX : 1 byte, CRC : 1 byte
	public static final int MESSAGE_FOOTER_LENGTH = 2;
	
	@Setter @Getter
	private int payloadLength;
	
	@Getter
	private MessageType messageType;
	
	public MessageHeader(MessageType messageType) {
		this.messageType = messageType;
	}
	
	/**
	 * Header를 생성하기 위해 필요한 사이즈
	 * @return
	 */
	public static int getRequiredHeaderSize() {
		return MESSAGE_HEADER_LENGTH;
	}
	
	/**
	 * Body를 생성하기 위해 필요한 사이즈
	 * 
	 * @return
	 */
	public int getRequiredBodySize() {
		return payloadLength;
	}

	/**
	 * Header에서 checksum에 포함되는 값을 계산
	 * 
	 * @return
	 */
	public int checksum() {
		return STX ^ messageType.getCmd1() ^ messageType.getCmd2() ^ payloadLength ^ ETX; 
	}

}
