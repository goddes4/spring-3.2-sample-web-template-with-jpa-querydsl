package net.octacomm.sample.netty.dev.msg.common;

import java.util.Collection;


/**
 * AbstractMessage 를 상속하는 클래스는 IncomingMessage와 OutgoingMessage로 분리 된다.
 * 
 * IncomingMessage은 Netty Decoder를 통해서 파싱 되기 때문에
 * MessagePacket보다 MessageHeader가 먼저 생성된다.
 * 그래서 IncomingMessage는 MessageHeader를 생성자 파라메터로 입력해야 한다.
 * 
 * OutgoingMessage는 서버에서 외부로 송신되는 메시지로
 * 생성자에서 MessageHeader를 만든다.
 * 
 * @author taeyo
 *
 */
public abstract class AbstractMessage {

	private MessageHeader header;

	public AbstractMessage(MessageHeader header) {
		this.header = header;
	}
	
	public MessageType getMessageType() {
		return header.getMessageType();
	}
	
	public int getRequireBodySize() {
		return header.getRequiredBodySize();
	}
	
	/**
	 * MessageHeader 와 MessageBody 의 데이터를 합친 checksum
	 * 
	 * @return
	 */
	public int checksum() {
		return bodyDataSum() ^ header.checksum();
	}
	
	protected abstract int bodyDataSum();

	/**
	 * 필드의 데이터 사이즈가 2 바이트인 경우에 
	 * Checksum 계산을 위해서 상위 바이트와 하위 바이트를 더한다. 
	 * 
	 * @param twoBytesArray
	 * @return
	 */
	protected static int calqTwoBytesChecksum(int ... twoBytesArray) {
		int result = 0;
		for (int twoBytes : twoBytesArray) {
			result ^= ((twoBytes >> 8) & 0xFF) ^ (twoBytes & 0xFF);
		}
		return result;
	}

	protected static int calqTwoBytesChecksum(Collection<Integer> twoBytesList) {
		int result = 0;
		for (int twoBytes : twoBytesList) {
			result ^= ((twoBytes >> 8) & 0xFF) ^ (twoBytes & 0xFF);
		}
		return result;
	}
	
	protected static int calqByteArrayChecksum(byte[] byteArray) {
		int sum = 0;
		for (byte oneByte : byteArray) {
			sum ^= oneByte;
		}
		return sum;
	}
	
	protected static int calqStringArrayChecksum(String[] strs) {
		int sum = 0;
		for (String str : strs) {
			sum ^= Integer.parseInt(str);
		}
		return sum;
	}

	protected String makeTime(int hour, int min, int sec) {
		return String.format("%02d:%02d:%02d", hour, min, sec);
	}

	public String toString() {
		return header.toString();
	}
}
