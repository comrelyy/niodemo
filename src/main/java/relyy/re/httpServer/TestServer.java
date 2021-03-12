package relyy.re.httpServer;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description
 * @Created by cairuirui
 * @Date 2021/3/2
 */
@Slf4j
public class TestServer {
	public static void main(String[] args) {
		NioEventLoopGroup workerGroup = new NioEventLoopGroup();
		NioEventLoopGroup bossGroup = new NioEventLoopGroup();

		try {
			ServerBootstrap serverBootstrap = new ServerBootstrap();
			serverBootstrap.group(bossGroup,workerGroup)
					.channel(NioServerSocketChannel.class)
					.childHandler(new HttpServerInitlier());

			ChannelFuture channelFuture = serverBootstrap.bind(9876).sync();
			channelFuture.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
}
