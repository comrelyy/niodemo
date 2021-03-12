package relyy.re.protocol;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description
 * @Created by cairuirui
 * @Date 2020/12/7
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Response implements Serializable {
	private static final long serialVersionUID = -7692172026624572258L;
	private  int code = 0;
	private String errMsg;
	private Object result;

}
