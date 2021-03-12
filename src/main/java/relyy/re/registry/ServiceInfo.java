package relyy.re.registry;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description
 * @Created by cairuirui
 * @Date 2020/12/9
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceInfo implements Serializable {
	private static final long serialVersionUID = -1599296805177403194L;

	private String host;
	private int port;

}
