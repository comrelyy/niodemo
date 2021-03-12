package relyy.re.webSocket;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @Description
 * @Created by cairuirui
 * @Date 2021/3/4
 */
public class WebSocketChannelInitialier extends ChannelInitializer<SocketChannel> {
	@Override
	protected void initChannel(SocketChannel socketChannel) throws Exception {
		ChannelPipeline pipeline = socketChannel.pipeline();

		pipeline.addLast(new HttpServerCodec());
		pipeline.addLast(new ChunkedWriteHandler());
		pipeline.addLast(new HttpObjectAggregator(8192));
		pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));

		pipeline.addLast(new TextWebSocketFrameHandler());
	}
}
