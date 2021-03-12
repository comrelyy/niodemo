package relyy.re.protocol;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description
 * @Created by cairuirui
 * @Date 2020/12/7
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message<T> {

	private Header header;

	private T content;


}
