package relyy.re.webSocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.net.InetSocketAddress;

/**
 * @Description
 * @Created by cairuirui
 * @Date 2021/3/4
 */
public class WebSocketServer {

	public static void main(String[] args) {
		EventLoopGroup boss = new NioEventLoopGroup(1);
		EventLoopGroup worker = new NioEventLoopGroup(16);
		try{
			ServerBootstrap serverBootstrap = new ServerBootstrap();
			serverBootstrap.group(boss,worker)
					.channel(NioServerSocketChannel.class)
					.handler(new LoggingHandler(LogLevel.INFO))
					.childHandler(new WebSocketChannelInitialier());

			ChannelFuture channelFuture = serverBootstrap.bind(new InetSocketAddress(9876)).sync();
			channelFuture.channel().closeFuture().sync();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			boss.shutdownGracefully();
			worker.shutdownGracefully();
		}
	}
}
