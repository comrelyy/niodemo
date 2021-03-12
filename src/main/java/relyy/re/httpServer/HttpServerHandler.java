package relyy.re.httpServer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.shaded.com.google.common.net.HttpHeaders;

import java.nio.charset.Charset;

/**
 * @Description
 * @Created by cairuirui
 * @Date 2021/3/2
 */
@Slf4j
public class HttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, HttpObject httpObject) throws Exception {
		ByteBuf hello_world = Unpooled.copiedBuffer("Hello world", CharsetUtil.UTF_8);
		DefaultFullHttpResponse httpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
				HttpResponseStatus.OK, hello_world);

		httpResponse.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/plain");
		httpResponse.headers().set(HttpHeaderNames.CONTENT_LENGTH,hello_world.readableBytes());

		ctx.writeAndFlush(httpResponse);

	}
}
