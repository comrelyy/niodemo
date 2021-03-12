package relyy.re.transport;

import io.netty.channel.Channel;
import io.netty.util.concurrent.Promise;
import lombok.AllArgsConstructor;
import lombok.Data;
import relyy.re.protocol.Message;

/**
 * @Description
 * @Created by cairuirui
 * @Date 2020/12/8
 */
@AllArgsConstructor
@Data
public class NettyResponseFuture<T> {
	private long createTime;
	private long timeOut;
	private Message request;
	private Channel channel;
	private Promise<T> promise;

}
