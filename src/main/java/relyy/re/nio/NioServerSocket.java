package relyy.re.nio;

import com.google.common.collect.Maps;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * @Description
 * @Created by cairuirui
 * @Date 2021/3/9
 */
public class NioServerSocket {

	private static Map<String,SocketChannel> clientMap = Maps.newHashMap();
	public static void main(String[] args) throws Exception{

		Selector selector = Selector.open();

		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.configureBlocking(false);
		ServerSocket socket = serverSocketChannel.socket();

		socket.bind(new InetSocketAddress(6098));
		serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		System.out.println("监听端口【6098】");

		while (true){
			try{
				selector.select();
				Set<SelectionKey> selectionKeys = selector.selectedKeys();
				int size = selectionKeys.size();

				selectionKeys.forEach(selectionKey ->  {
					final SocketChannel client;
					try {
						if (selectionKey.isAcceptable()) {
							ServerSocketChannel socketChannel = (ServerSocketChannel)selectionKey.channel();
							client = socketChannel.accept();
							client.configureBlocking(false);
							client.register(selector,SelectionKey.OP_READ);
							String key = UUID.randomUUID().toString();
							clientMap.put(key,client);
							System.out.println("建立了连接: "+client);
						}else if(selectionKey.isReadable()){
							client = (SocketChannel)selectionKey.channel();
							ByteBuffer buffer = ByteBuffer.allocate(1024);
							int read = client.read(buffer);
							if (read > 0) {
								buffer.flip();
								Charset charset = Charset.forName("utf-8");
								String message = String.valueOf(charset.decode(buffer).array());
								System.out.println(client +":"+ message);
								String key = null;
								for (Map.Entry<String, SocketChannel> entry : clientMap.entrySet()) {
									if (entry.getValue() == client) {
										key = entry.getKey();
										break;
									}
								}
								String sendMsg =  key + "send: " + message;
								clientMap.forEach((id, socketClient) -> {
									try {
										ByteBuffer writeBuf = ByteBuffer.allocate(1024);
										writeBuf.put(sendMsg.getBytes());
										writeBuf.flip();
										socketClient.write(writeBuf);
									} catch (IOException e) {
										e.printStackTrace();
									}
								});
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
					}
			});
				selectionKeys.clear();
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				//selector.close();
			}
		}
	}
}
