package relyy.re.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import relyy.re.Constants;
import relyy.re.compressor.Compressor;
import relyy.re.compressor.CompressorFactory;
import relyy.re.protocol.Header;
import relyy.re.protocol.Message;
import relyy.re.serialization.Serialization;
import relyy.re.serialization.SerializationFactory;

/**
 * @Description
 * @Created by cairuirui
 * @Date 2020/12/7
 */
public class DemoRpcEncoder extends MessageToByteEncoder<Message> {

	@Override
	protected void encode(ChannelHandlerContext channelHandlerContext, Message message, ByteBuf byteBuf) throws Exception {
		Header header = message.getHeader();

		byteBuf.writeShort(header.getMagic());
		byteBuf.writeByte(header.getVersion());
		byteBuf.writeByte(header.getExtraInfo());
		byteBuf.writeLong(header.getMessageId());

		if (Constants.isHeartBeat(header.getExtraInfo())) {
			byteBuf.writeInt(0);  //心跳消息，没有消息体，这里写入0
			return;
		}

		Serialization serialization = SerializationFactory.get(header.getExtraInfo());
		Compressor compressor = CompressorFactory.get(header.getExtraInfo());
		byte[] upLoad = compressor.compressor(serialization.serialize(message.getContent()));
		byteBuf.writeInt(upLoad.length);  //写入消息长度
		byteBuf.writeBytes(upLoad);       //写入消息体
	}
}
