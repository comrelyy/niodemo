package relyy.re.transport;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import relyy.re.Constants;
import relyy.re.protocol.Message;
import relyy.re.protocol.Request;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description
 * @Created by cairuirui
 * @Date 2020/12/8
 */
public class DemoRpcServerHandler extends SimpleChannelInboundHandler<Message<Request>> {

	private static ExecutorService executorService = Executors.newCachedThreadPool();

	@Override
	protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message<Request> requestMessage) throws Exception {
		byte extraInfo = requestMessage.getHeader().getExtraInfo();
		if (Constants.isHeartBeat(extraInfo)) {
			channelHandlerContext.writeAndFlush(requestMessage);
			return;
		}
		executorService.execute(new InvokeRunnable(requestMessage,channelHandlerContext));

	}
}
