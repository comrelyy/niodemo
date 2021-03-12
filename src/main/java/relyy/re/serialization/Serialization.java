package relyy.re.serialization;

import java.io.IOException;

/**
 * @Description
 * @Created by cairuirui
 * @Date 2020/12/7
 */
public interface Serialization {

	<T> byte[] serialize(T obj) throws IOException;

	<T> T deSerialize(byte[] data,Class<T> clazz) throws IOException;
}
