package relyy.re.laoke;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.EventExecutorGroup;

/**
 * @Description
 * @Created by cairuirui
 * @Date 2021/3/4
 */
public class LaokeClientHandler extends SimpleChannelInboundHandler<String> {
	@Override
	protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
		System.out.println(s);
	}
}
