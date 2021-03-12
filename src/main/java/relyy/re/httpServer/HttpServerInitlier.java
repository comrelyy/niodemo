package relyy.re.httpServer;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description
 * @Created by cairuirui
 * @Date 2021/3/2
 */
@Slf4j
public class HttpServerInitlier  extends ChannelInitializer<SocketChannel> {
	@Override
	protected void initChannel(SocketChannel channel) throws Exception {
		ChannelPipeline pipeline = channel.pipeline();
		pipeline.addLast("httpServerCodec",new HttpServerCodec());
		pipeline.addLast("httpResponse",new HttpServerHandler());
	}
}
