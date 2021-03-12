package relyy.re.laoke;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

/**
 * @Description
 * @Created by cairuirui
 * @Date 2021/3/3
 */
public class LaokeServerInitialier extends ChannelInitializer<SocketChannel> {
	@Override
	protected void initChannel(SocketChannel channel) throws Exception {
		ChannelPipeline pipeline = channel.pipeline();

		pipeline.addLast(new DelimiterBasedFrameDecoder(4096, Delimiters.lineDelimiter()));
		pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8));
		pipeline.addLast(new StringEncoder(CharsetUtil.UTF_8));

		pipeline.addLast(new LaokeServerHandler());


	}
}
