package relyy.re.laoke;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.local.LocalAddress;
import io.netty.channel.nio.NioEventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @Description
 * @Created by cairuirui
 * @Date 2021/3/4
 */
public class LaokeClient {

	public static void main(String[] args) throws Exception{

		NioEventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(group).channel(NioSocketChannel.class)
					.handler(new LaokeClientInitialier());
			ChannelFuture future = bootstrap.connect("127.0.0.1",9876).sync();
			Channel channel = future.channel();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
			while (true){
				channel.writeAndFlush(bufferedReader.readLine() + "\r\n");
			}
		} finally {
			group.shutdownGracefully();
		}
	}
}
