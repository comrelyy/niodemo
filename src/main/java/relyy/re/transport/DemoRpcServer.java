package relyy.re.transport;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import relyy.re.codec.DemoRpcDecoder;
import relyy.re.codec.DemoRpcEncoder;

/**
 * @Description  服务端启动引导类：配置线程池，channel初始化（绑定用户自定义业务处理逻辑），端口绑定
 * @Created by cairuirui
 * @Date 2020/12/8
 */
public class DemoRpcServer {

	private EventLoopGroup bossGroup;
	private EventLoopGroup workerGroup;
	private ServerBootstrap serverBootstrap;
	private Channel channel;

	protected int port;

	public DemoRpcServer(int port) throws InterruptedException{
		this.port = port;

		bossGroup = NettyEventLoopFactory.eventLoopGroup(1, "NettyServerBoss");
		workerGroup = NettyEventLoopFactory.eventLoopGroup(
				Math.min(Runtime.getRuntime().availableProcessors() + 1, 32),
				"NettyServerWorker");

		serverBootstrap = new ServerBootstrap().group(bossGroup,workerGroup)
				.channel(NioServerSocketChannel.class)
				.option(ChannelOption.SO_REUSEADDR,Boolean.TRUE)
				.childOption(ChannelOption.TCP_NODELAY,Boolean.TRUE)
				.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
				.handler(new LoggingHandler(LogLevel.INFO)).childHandler(
				new ChannelInitializer<SocketChannel>() {

					@Override
					protected void initChannel(SocketChannel socketChannel) throws Exception {
						socketChannel.pipeline().addLast("demo-rpc-decoder",new DemoRpcDecoder());
						socketChannel.pipeline().addLast("demo-rpc-encoder",new DemoRpcEncoder());
						socketChannel.pipeline().addLast("server-handler",new DemoRpcServerHandler());
					}
				});
	}

	public ChannelFuture start() throws InterruptedException{
		//指定监听端口
		ChannelFuture channelFuture = serverBootstrap.bind(port);
		//ChannelFuture channelFuture = serverBootstrap.bind(port).sync();  阻塞
		channel = channelFuture.channel();
		channel.closeFuture();
		return channelFuture;
	}

	public void startAndWait() throws InterruptedException{
		try{
			channel.closeFuture().wait();
		} catch(InterruptedException e){
			Thread.interrupted();
		}
	}

	public void shutdown() throws InterruptedException{
		channel.close().sync();
		if (bossGroup != null){
			bossGroup.shutdownGracefully().awaitUninterruptibly(15000);
		}
		if (workerGroup != null){
			workerGroup.shutdownGracefully().awaitUninterruptibly(15000);
		}
	}


}
