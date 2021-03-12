package relyy.re.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @Description
 * @Created by cairuirui
 * @Date 2021/3/8
 */
public class SelectorDemo {


	public static void main(String[] args) throws Exception{
		int[] ports = new int[4];
		ports[0] = 5001;
		ports[1] = 5002;
		ports[2] = 5003;
		ports[3] = 5004;

		Selector selector = Selector.open();
		for (int port : ports) {
			ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
			serverSocketChannel.configureBlocking(false);
			ServerSocket socket = serverSocketChannel.socket();
			socket.bind(new InetSocketAddress(port));
			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

			System.out.println("监听端口: "+port);
		}

		while (true){
			try {
				selector.select();
				Set<SelectionKey> selectionKeys = selector.selectedKeys();
				Iterator<SelectionKey> iterator = selectionKeys.iterator();
				while (iterator.hasNext()){
					SelectionKey next = iterator.next();
					if (next.isAcceptable()) {
						ServerSocketChannel socketChannel = (ServerSocketChannel) next.channel();

						SocketChannel channel = socketChannel.accept();
						channel.configureBlocking(false);
						channel.register(selector,SelectionKey.OP_READ);
						System.out.println("建立连接： "+ socketChannel);
						iterator.remove();
					}else if (next.isReadable()){
						SocketChannel socketChannel = (SocketChannel) next.channel();

						int readByte = 0;
						while (true){
							ByteBuffer buffer = ByteBuffer.allocate(512);
							buffer.clear();

							int read = socketChannel.read(buffer);
							if (read <= 0){
								break;
							}

							buffer.flip();
							socketChannel.write(buffer);
							readByte += read;
						}
						System.out.println("读取： "+readByte + ",来自于： " + socketChannel);
						iterator.remove();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

}
