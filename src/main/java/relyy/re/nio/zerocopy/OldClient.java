package relyy.re.nio.zerocopy;



import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.Socket;

/**
 * @Description
 * @Created by cairuirui
 * @Date 2021/3/12
 */
public class OldClient {

	public static void main(String[] args) throws Exception{
		Socket socket = new Socket("127.0.0.1", 8899);
		String fileName = "D:\\rcs_command.2021-03-11-09.1";
		InputStream fileInputStream = new FileInputStream(fileName);
		DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

		byte[] bytes = new byte[4096];
		long readCount = 0;
		long total = 0;

		long startTime = System.currentTimeMillis();
		while ((readCount=fileInputStream.read(bytes)) >= 0){
			total += readCount;
			dataOutputStream.write(bytes);
		}

		System.out.println("发送总字节数:" + total + ",花费时间:" + (System.currentTimeMillis() - startTime));

		fileInputStream.close();
		dataOutputStream.close();
		socket.close();
	}
}
