package relyy.re.laoke;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @Description
 * @Created by cairuirui
 * @Date 2021/3/3
 */
public class LaokeServer {

	public static void main(String[] args) throws Exception{
		NioEventLoopGroup boss = new NioEventLoopGroup();
		NioEventLoopGroup worker = new NioEventLoopGroup();

		try{
			ServerBootstrap serverBootstrap = new ServerBootstrap();
			serverBootstrap.group(boss,worker)
					.channel(NioServerSocketChannel.class)
					.childHandler(new LaokeServerInitialier());

			ChannelFuture channelFuture = serverBootstrap.bind(9876).sync();
			channelFuture.channel().closeFuture().sync();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			boss.shutdownGracefully();
			worker.shutdownGracefully();
		}
	}
}
