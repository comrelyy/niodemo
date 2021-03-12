package relyy.re.nio.zerocopy;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @Description
 * @Created by cairuirui
 * @Date 2021/3/12
 */
public class NewIoServer {

	public static void main(String[] args) throws Exception{
		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
		ServerSocket socket = serverSocketChannel.socket();
		socket.setReuseAddress(true);
		socket.bind(new InetSocketAddress(8899));

		ByteBuffer buffer = ByteBuffer.allocate(4096);
		while (true){
			SocketChannel socketChannel = serverSocketChannel.accept();
			socketChannel.configureBlocking(true);
			long readCount = 0;
			while (-1 != readCount){
				try{
					readCount = socketChannel.read(buffer);
				}catch (Exception e){
					e.printStackTrace();
				}
				buffer.rewind();
			}

			System.out.println("读取完成");
			socketChannel.close();

		}

	}
}
