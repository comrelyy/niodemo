package relyy.re.transport;

import io.netty.channel.ChannelHandlerContext;
import relyy.re.beanfactory.BeanManager;
import relyy.re.protocol.Header;
import relyy.re.protocol.Message;
import relyy.re.protocol.Request;
import relyy.re.protocol.Response;

import java.lang.reflect.Method;

/**
 * @Description
 * @Created by cairuirui
 * @Date 2020/12/8
 */
public class InvokeRunnable implements Runnable {
	private ChannelHandlerContext ctx;
	private Message<Request> message;

	public InvokeRunnable(Message<Request> message, ChannelHandlerContext ctx){
		this.message = message;
		this.ctx = ctx;
	}

	@Override
	public void run() {
		Response response = new Response();
		Object result = null;
		try{
			Request request = message.getContent();
			String serviceName = request.getServiceName();
			Object bean = BeanManager.getBean(serviceName);
			Method method = bean.getClass().getMethod(request.getMethodName(), request.getArgTypes());
			result = method.invoke(bean, request.getArgs());
		}catch(Exception e){

		}finally{

		}
		Header header = message.getHeader();
		header.setExtraInfo((byte)1);
		response.setResult(result);
		ctx.writeAndFlush(new Message(header,response));
	}
}
