package relyy.re.nio.zerocopy;

import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

/**
 * @Description
 * @Created by cairuirui
 * @Date 2021/3/12
 */
public class NewIoClient {
	public static void main(String[] args) throws Exception{
		SocketChannel socketChannel = SocketChannel.open();
		socketChannel.connect(new InetSocketAddress("127.0.0.1",8899));
		socketChannel.configureBlocking(true);
		String fileName = "D:\\rcs_command.2021-03-11-09.1";

		FileChannel fileChannel = new FileInputStream(fileName).getChannel();

		long startTime = System.currentTimeMillis();

		long l = fileChannel.transferTo(0, fileChannel.size(), socketChannel);

		System.out.println("传输的长度: "+ l + "花费时间："+(System.currentTimeMillis() - startTime));
		fileChannel.close();
		socketChannel.close();

	}
}
