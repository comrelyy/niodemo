package relyy.re.laoke;

import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * @Description
 * @Created by cairuirui
 * @Date 2021/3/3
 */
public class LaokeServerHandler extends SimpleChannelInboundHandler<String> {


	private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String s) throws Exception {
		Channel channel = ctx.channel();
		channelGroup.forEach(ch -> {
			if (ch != channel){
				ch.writeAndFlush(channel.remoteAddress() + "说："+ s + "\n");
			}else {
				ch.writeAndFlush("【自己】说："+ s + "\n");
			}
		});
	}

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		Channel channel = ctx.channel();
		channelGroup.writeAndFlush("【服务器】- " + channel.remoteAddress() + "加入\n");
		channelGroup.add(channel);
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		Channel channel = ctx.channel();
		channelGroup.writeAndFlush("【服务器】- " + channel.remoteAddress() + "离开\n");
	}

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		super.channelRegistered(ctx);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		Channel channel = ctx.channel();
		System.out.println("【服务器】- " + channel.remoteAddress() + "上线\n");
	}



	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		Channel channel = ctx.channel();
		System.out.println("【服务器】- " + channel.remoteAddress() + "下线\n");
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}


}
