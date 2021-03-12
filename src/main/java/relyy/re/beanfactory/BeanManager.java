package relyy.re.beanfactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description
 * @Created by cairuirui
 * @Date 2020/12/8
 */
public class BeanManager {

	private static Map<String, Object> services = new ConcurrentHashMap<>();

	public static void registerBean(String serviceName, Object bean) {
		services.put(serviceName, bean);
	}

	public static Object getBean(String serviceName) {
		return services.get(serviceName);
	}
}
