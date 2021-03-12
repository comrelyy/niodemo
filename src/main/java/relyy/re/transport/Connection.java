package relyy.re.transport;

import io.netty.channel.ChannelFuture;
import io.netty.channel.DefaultEventLoop;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.Promise;
import jdk.nashorn.internal.ir.CallNode;
import relyy.re.Constants;
import relyy.re.protocol.Header;
import relyy.re.protocol.Message;
import relyy.re.protocol.Request;
import relyy.re.protocol.Response;

import java.io.Closeable;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @Description
 * @Created by cairuirui
 * @Date 2020/12/9
 */
public class Connection implements Closeable {
	//生成消息ID,需全局唯一
	private final static AtomicLong MSG_ID = new AtomicLong(0);

	//时间轮定时删除
	public final static Map<Long,NettyResponseFuture<Response>> IN_FLIGHT_REQUEST_MAP
			= new ConcurrentHashMap<>();

	private ChannelFuture channelFuture;

	private AtomicBoolean isConnected = new AtomicBoolean();

	public Connection(){
		this.isConnected.set(false);
		channelFuture = null;
	}

	public Connection(ChannelFuture channelFuture, boolean isConnected) {
		this.channelFuture = channelFuture;
		this.isConnected.set(isConnected);
	}

	public ChannelFuture getChannelFuture() {
		return channelFuture;
	}

	public void setChannelFuture(ChannelFuture channelFuture) {
		this.channelFuture = channelFuture;
	}

	public boolean getIsConnected() {
		return this.isConnected.get();
	}

	public void setIsConnected(boolean isConnected) {
		this.isConnected.set(isConnected);
	}

	public NettyResponseFuture<Response> request(Message<Request> message,long timeout){
		//生成消息id
		long messageId = MSG_ID.incrementAndGet();
		message.getHeader().setMessageId(messageId);
		//创建消息关联的future
		NettyResponseFuture nettyResponseFuture = new NettyResponseFuture(System.currentTimeMillis(), timeout,
				message, channelFuture.channel(), new DefaultPromise(new DefaultEventLoop()));
		//将message关联的Future记录
		IN_FLIGHT_REQUEST_MAP.put(messageId,nettyResponseFuture);
		try{
			channelFuture.channel().writeAndFlush(nettyResponseFuture);
		} catch(Exception e){
			IN_FLIGHT_REQUEST_MAP.remove(messageId);
			throw e;
		}
		return nettyResponseFuture;
	}

	public boolean ping(){
		Header heartBeatHeader = new Header(Constants.MAGIC, Constants.VERSION_1);
		heartBeatHeader.setExtraInfo(Constants.HEART_EXTRA_INFO);
		Message message = new Message(heartBeatHeader, null);
		NettyResponseFuture<Response> request = request(message, Constants.DEFAULT_TIMEOUT);
		try{
			Promise<Response> await = request.getPromise().await();
			return await.get().getCode() == Constants.HEARTBEAT_CODE;
		} catch(Exception e){
			return false;
		}
	}

	@Override
	public void close() throws IOException {
		channelFuture.channel().close();
	}
}
