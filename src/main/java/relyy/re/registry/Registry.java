package relyy.re.registry;

import org.apache.curator.x.discovery.ServiceInstance;

import java.util.List;

/**
 * @Description
 * @Created by cairuirui
 * @Date 2020/12/9
 */
public interface Registry<T> {

	void registerService(ServiceInstance<T> serviceInstance) throws Exception;

	void unRegisterService(ServiceInstance<T> serviceInstance) throws Exception;

	List<ServiceInstance<T>> queryForInstances(String name) throws Exception;
}
