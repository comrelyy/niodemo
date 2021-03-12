package relyy.re.registry;

import com.google.common.collect.Maps;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceCache;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.details.InstanceSerializer;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Description
 * @Created by cairuirui
 * @Date 2020/12/9
 */
public class ZookeeperRegistry<T> implements Registry<T> {

	private Map<String,List<ServiceInStanceLister<T>>> listers = Maps.newConcurrentMap();

	private InstanceSerializer instanceSerializer = new JsonInstanceSerializer<>(ServiceInfo.class);

	private ServiceDiscovery<T> serviceDiscovery;

	private ServiceCache<T> serviceCache;

	private String address = "localhost:2128";

	public void start() throws Exception{
		String root = "demo/rpc";
		CuratorFramework client = CuratorFrameworkFactory.newClient(address, new ExponentialBackoffRetry(1000, 3));
		client.start();

		serviceDiscovery = ServiceDiscoveryBuilder.builder(ServiceInfo.class)
				.client(client).basePath(root)
				.serializer(instanceSerializer)
				.build();
		serviceDiscovery.start();

		serviceDiscovery.serviceCacheBuilder()
				.name("/demoService")
				.build();

		client.blockUntilConnected();
		serviceDiscovery.start();
		serviceCache.start();

	}

	@Override
	public void registerService(ServiceInstance<T> serviceInstance) throws Exception {
		serviceDiscovery.registerService(serviceInstance);
	}

	@Override
	public void unRegisterService(ServiceInstance<T> serviceInstance) throws Exception {
		serviceDiscovery.unregisterService(serviceInstance);
	}

	@Override
	public List<ServiceInstance<T>> queryForInstances(String name) throws Exception {
		return serviceCache.getInstances().stream()
				.filter(s -> Objects.equals(s.getName(),name))
				.collect(Collectors.toList());
	}
}
