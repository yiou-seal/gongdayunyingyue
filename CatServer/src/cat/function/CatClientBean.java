package cat.function;

import java.net.Socket;

public class CatClientBean {

	private String name;
	private Socket socket;
	private String threadname;

	public void setThreadname(String threadname)
	{
		this.threadname = threadname;
	}

	public String getThreadname()
	{
		return threadname;
	}

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
