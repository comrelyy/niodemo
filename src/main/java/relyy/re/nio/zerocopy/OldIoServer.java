package relyy.re.nio.zerocopy;

import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Description
 * @Created by cairuirui
 * @Date 2021/3/12
 */
public class OldIoServer {

	public static void main(String[] args) throws Exception{
		ServerSocket serverSocket = new ServerSocket(8899);
		while (true) {
			Socket socket = serverSocket.accept();
			DataInputStream inputStream = new DataInputStream(socket.getInputStream());

			try{
				byte[] bytes = new byte[4096];
				while (true){
					int read = inputStream.read(bytes, 0, bytes.length);
					if (-1 == read){
						break;
					}

				}
				System.out.println("传输完成");
			}catch (Exception e){
				e.printStackTrace();
			}
			inputStream.close();
			socket.close();
		}
	}
}
