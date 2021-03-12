package relyy.re.transport;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import relyy.re.Constants;
import relyy.re.protocol.Message;
import relyy.re.protocol.Response;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description
 * @Created by cairuirui
 * @Date 2020/12/9
 */
public class DemoRpcClientHandler extends SimpleChannelInboundHandler<Message<Response>> {
	private static ExecutorService executorService = Executors.newCachedThreadPool();

	@Override
	protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message<Response> responseMessage) throws Exception {
		NettyResponseFuture<Response> future = Connection.IN_FLIGHT_REQUEST_MAP.remove(responseMessage.getHeader().getMessageId());
		Response response = responseMessage.getContent();
		if (response == null && Constants.isHeartBeat(responseMessage.getHeader().getExtraInfo())) {
			response = new Response();
			response.setCode(Constants.HEARTBEAT_CODE);
		}
		future.getPromise().setSuccess(response);
	}
}
