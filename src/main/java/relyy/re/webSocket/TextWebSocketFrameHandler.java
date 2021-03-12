package relyy.re.webSocket;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.EventExecutorGroup;

/**
 * @Description
 * @Created by cairuirui
 * @Date 2021/3/4
 */
public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame textWebSocketFrame) throws Exception {
		System.out.println("收到消息: "+ textWebSocketFrame.text());
		ctx.channel().writeAndFlush(new TextWebSocketFrame("hello ,i am server reply"));
	}

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		System.out.println("handle add : " + ctx.channel().id().asLongText());
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("channel active : " + ctx.channel().id().asLongText());
		new Thread() {
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(1000);
						ctx.channel().writeAndFlush(new TextWebSocketFrame("来之服务器的推送"));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		System.out.println("handle remove : " + ctx.channel().id().asLongText());
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		System.out.println("error cause");
		ctx.close();
	}
}
