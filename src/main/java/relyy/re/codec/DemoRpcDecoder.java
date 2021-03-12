package relyy.re.codec;

import com.caucho.hessian.io.HessianOutput;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import relyy.re.Constants;
import relyy.re.compressor.Compressor;
import relyy.re.compressor.CompressorFactory;
import relyy.re.protocol.Header;
import relyy.re.protocol.Message;
import relyy.re.protocol.Request;
import relyy.re.serialization.Serialization;
import relyy.re.serialization.SerializationFactory;

import java.util.List;

/**
 * @Description
 * @Created by cairuirui
 * @Date 2020/12/7
 */
public class DemoRpcDecoder extends ByteToMessageDecoder {
	@Override
	protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> out) throws Exception {

		if (byteBuf.readableBytes() < Constants.HEADER_SIZE){
			return;
		}

		//记录当前readIndex指针的位置，方便重置
		byteBuf.markReaderIndex();

		short magic = byteBuf.readShort();
		if (magic != Constants.MAGIC) { // 魔数不匹配会抛出异常
			byteBuf.resetReaderIndex(); // 重置index
			throw new RuntimeException("magic number error: "+magic);
		}

		byte version = byteBuf.readByte();
		byte extraInfo = byteBuf.readByte();
		long messageId = byteBuf.readLong();
		int size = byteBuf.readInt();

		Object request = null;
		//不处理心跳消息 心跳消息没有消息体
		if (!Constants.isHeartBeat(extraInfo)) {
			//对于非心跳消息，没有累积到足够的数据量是无法进行反序列化的
			if (byteBuf.readableBytes() < size) {
				byteBuf.resetReaderIndex();
				return;
			}

			//读取消息体并进行反序列化
			byte[] playLode = new byte[size];
			byteBuf.readBytes(playLode);
			//这里根据消息头中的extraInfo选择相应的序列化和压缩方式
			Serialization serialization = SerializationFactory.get(extraInfo);
			Compressor compressor = CompressorFactory.get(extraInfo);

			request = serialization.deSerialize(compressor.unCompressor(playLode), Request.class);
			Header header = Header.builder().magic(magic).version(version).extraInfo(extraInfo)
					.messageId(messageId).size(size).build();
			//组装向后传递
			Message message = new Message(header, request);
			out.add(message);
		}


	}
}
