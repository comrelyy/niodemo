package relyy.re.registry;

import org.apache.curator.x.discovery.ServiceInstance;

/**
 * @Description
 * @Created by cairuirui
 * @Date 2020/12/9
 */
public interface ServiceInStanceLister<T> {

	void onRegister(ServiceInstance<T> serviceInstance);

	void onRemove(ServiceInstance<T> serviceInstance);

	void onUpdate(ServiceInstance<T> serviceInstance);

	void onRefresh(ServiceInstance<T> serviceInstance,ServerInfoEvent event);

	enum ServerInfoEvent{
		ON_REGISTER,
		ON_UPDATE,
		ON_REMOVE,
		;
	}
}
