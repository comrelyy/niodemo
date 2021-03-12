package relyy.re.transport;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import relyy.re.Constants;
import relyy.re.codec.DemoRpcDecoder;
import relyy.re.codec.DemoRpcEncoder;

import java.io.Closeable;
import java.io.IOException;

/**
 * @Description
 * @Created by cairuirui
 * @Date 2020/12/9
 */
public class DemoRpcClient implements Closeable {

	protected Bootstrap clientBootstrap;
	protected EventLoopGroup group;
	private String host;
	private int port;

	public DemoRpcClient(String host, int port) {
		this.host = host;
		this.port = port;

		clientBootstrap = new Bootstrap();
		group = NettyEventLoopFactory.eventLoopGroup(Constants.DEFAULT_IO_THREADS, "NettyClientWork");
		clientBootstrap.group(group)
				.option(ChannelOption.TCP_NODELAY,true)
				.option(ChannelOption.SO_KEEPALIVE,true)
				.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
				.channel(NioSocketChannel.class)
				.handler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel socketChannel) throws Exception {
						//指定Channelhandler的顺序
						socketChannel.pipeline().addLast("demo-rpc-encoder",new DemoRpcEncoder());
						socketChannel.pipeline().addLast("demo-rpc-decoder",new DemoRpcDecoder());
						socketChannel.pipeline().addLast("client-handler",new DemoRpcClientHandler());
					}
				});
	}

	public ChannelFuture connect(){
		ChannelFuture connect = clientBootstrap.connect(host, port);
		connect.awaitUninterruptibly();
		return connect;
	}

	@Override
	public void close() throws IOException {
		group.shutdownGracefully();
	}
}
