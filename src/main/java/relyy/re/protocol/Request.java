package relyy.re.protocol;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

/**
 * @Description
 * @Created by cairuirui
 * @Date 2020/12/7
 */
@Data
public class Request implements Serializable {
	private static final long serialVersionUID = -8498614501802375281L;

	private String serviceName;

	private String methodName;

	private Class[] argTypes;

	private Object[] args;

	public Request(String serviceName, String methodName, Object[] args) {
		this.serviceName = serviceName;
		this.methodName = methodName;
		this.args = args;
		this.argTypes = new Class[args.length];
		for (int j = 0; j <args.length; j++) {
			argTypes[j] = args[j].getClass();
		}
	}

	@Override
	public String toString() {
		return "Request{" +
				"serviceName='" + serviceName + '\'' +
				", methodName='" + methodName + '\'' +
				", argTypes=" + Arrays.toString(argTypes) +
				", args=" + Arrays.toString(args) +
				'}';
	}
}
