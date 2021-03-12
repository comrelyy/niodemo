package relyy.re.serialization;

import com.caucho.hessian.io.Hessian2Output;
import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @Description 默认使用 Hessian序列化
 * @Created by cairuirui
 * @Date 2020/12/7
 */
public class HessianSerialization implements Serialization {
	@Override
	public <T> byte[] serialize(T obj) throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		HessianOutput hessianOutput = new HessianOutput(outputStream);
		hessianOutput.writeObject(obj);
		return outputStream.toByteArray();
	}

	@Override
	public <T> T deSerialize(byte[] data, Class<T> clazz) throws IOException {
		ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
		HessianInput hessianInput = new HessianInput(inputStream);
		return (T)hessianInput.readObject(clazz);
	}
}
