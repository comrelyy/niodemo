package relyy.re.protocol;

import lombok.Builder;
import lombok.Data;

/**
 * @Description 消息头
 * @Created by cairuirui
 * @Date 2020/12/7
 */
@Data
@Builder
public class Header {

	private short magic; //魔数
	private byte version; //版本号
	private byte extraInfo; //附加信息
	private Long messageId; //消息ID
	private Integer size;   //消息体长度

	public Header(short magic, byte version) {
		this.magic = magic;
		this.version = version;
	}

	public Header(short magic, byte version, byte extraInfo, Long messageId, Integer size) {
		this.magic = magic;
		this.version = version;
		this.extraInfo = extraInfo;
		this.messageId = messageId;
		this.size = size;
	}

	public void setSerialization(byte serialization){
		this.extraInfo |= serialization;
	}

	public void setCompressor(byte compressor){
		this.extraInfo |= compressor;
	}
}
