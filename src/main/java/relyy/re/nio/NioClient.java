package relyy.re.nio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description
 * @Created by cairuirui
 * @Date 2021/3/10
 */
public class NioClient {

	public static void main(String[] args) throws Exception{
		SocketChannel socketChannel = SocketChannel.open();
		socketChannel.configureBlocking(false);
		//Socket socket = socketChannel.socket();
		socketChannel.connect(new InetSocketAddress("127.0.0.1",6098));
		//socket.connect(new InetSocketAddress("127.0.0.1",6098));

		Selector selector = Selector.open();
		socketChannel.register(selector, SelectionKey.OP_CONNECT);

		while (true){
			selector.select();
			Set<SelectionKey> selectionKeys = selector.selectedKeys();
			selectionKeys.forEach(selectionKey -> {
				try {
					final SocketChannel channel;
					if (selectionKey.isConnectable()) {
						channel = (SocketChannel)selectionKey.channel();
						if (channel.isConnectionPending()) {
							channel.finishConnect();
						}
						ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
						writeBuffer.put((LocalDateTime.now() + "连接成功").getBytes());
						writeBuffer.flip();
						channel.write(writeBuffer);

						ExecutorService executorService = Executors.newSingleThreadExecutor(Executors.defaultThreadFactory());
						executorService.submit(() -> {
							while (true){
								try {
									writeBuffer.clear();
									InputStreamReader input = new InputStreamReader(System.in);
									BufferedReader bufferedReader = new BufferedReader(input);
									String msg = bufferedReader.readLine();
									writeBuffer.put(msg.getBytes());
									writeBuffer.flip();
									channel.write(writeBuffer);
								}catch (Exception e){

								}
							}
						});

						channel.register(selector,SelectionKey.OP_READ);
					}else if(selectionKey.isReadable()){
						channel = (SocketChannel)selectionKey.channel();
						ByteBuffer readBuf = ByteBuffer.allocate(1024);
						int read = channel.read(readBuf);
						System.out.println(new String(readBuf.array(),0,read));

					}
				} catch (IOException e) {
					e.printStackTrace();
				}

			});
		}

	}
}
