package relyy.re.proxy;

import com.google.common.collect.Maps;
import io.netty.channel.ChannelFuture;
import org.apache.curator.x.discovery.ServiceInstance;
import relyy.re.Constants;
import relyy.re.protocol.Header;
import relyy.re.protocol.Message;
import relyy.re.protocol.Request;
import relyy.re.registry.Registry;
import relyy.re.registry.ServiceInfo;
import relyy.re.transport.Connection;
import relyy.re.transport.DemoRpcClient;
import relyy.re.transport.NettyResponseFuture;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * @Description
 * @Created by cairuirui
 * @Date 2020/12/9
 */
public class DemoRpcProxy implements InvocationHandler {

	  private String serviceName;

	  private Registry<ServiceInfo> registry;

	  private Map<Method, Header> headCache =Maps.newConcurrentMap();

	public DemoRpcProxy(String serviceName, Registry<ServiceInfo> registry) throws Exception{
		this.serviceName = serviceName;
		this.registry = registry;
	}

	public static <T> T newInstance(Class<T> clazz,Registry<ServiceInfo> registry) throws Exception{
		return (T)Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
					new Class[]{clazz},
					new DemoRpcProxy("demoService",registry));
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		List<ServiceInstance<ServiceInfo>> serviceInstances = registry.queryForInstances(serviceName);
		ServiceInstance<ServiceInfo> serviceInfo = serviceInstances.get(ThreadLocalRandom.current().nextInt(serviceInstances.size()));
		String methodName = method.getName();
		Header header = headCache.computeIfAbsent(method, h -> new Header(Constants.MAGIC, Constants.VERSION_1));
		Message<Request> message = new Message<>(header, new Request(serviceName, methodName, args));
		return remoteCall(serviceInfo.getPayload(),message);
	}

	protected Object remoteCall(ServiceInfo serviceInfo, Message message) throws Exception{
		if (serviceInfo == null) {
			throw new RuntimeException("get available server error");
		}
		Object result;
		try{
			DemoRpcClient demoRpcClient = new DemoRpcClient(serviceInfo.getHost(), serviceInfo.getPort());
			ChannelFuture channelFuture = demoRpcClient.connect().awaitUninterruptibly();
			Connection connection = new Connection(channelFuture, true);
			NettyResponseFuture request = connection.request(message, Constants.DEFAULT_TIMEOUT);
			result = request.getPromise().get(Constants.DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS);
		} catch(Exception e){
		    throw e;
		}
		return result;
	}
}
