package cat.function;

import java.net.Socket;

public class ClientBean {
	private String name;		//用户名
	private Socket socket;		//用户的套接字

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}
}
