package relyy.re.registry;

import org.apache.curator.x.discovery.ServiceInstance;

/**
 * @Description
 * @Created by cairuirui
 * @Date 2020/12/9
 */
public abstract class AbstractServiceInstanceLister<T> implements ServiceInStanceLister<T> {

	@Override
	public void onRefresh(ServiceInstance<T> serviceInstance, ServerInfoEvent event) {
		switch (event){
			case ON_REGISTER:
				onRegister(serviceInstance);
				break;
			case ON_UPDATE:
				onUpdate(serviceInstance);
				break;
			case ON_REMOVE:
				onRemove(serviceInstance);
				break;
			default:
				break;
		}
	}
}
