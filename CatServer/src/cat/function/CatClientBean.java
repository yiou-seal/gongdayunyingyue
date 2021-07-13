package cat.function;

import java.net.Socket;

public class CatClientBean {

	private String name;
	private Socket socket;
	private Thread threadname;
	private String ip;

	public String getIp()
	{
		return ip;
	}

	public void setIp(String ip)
	{
		this.ip = ip;
	}

	public void setThreadname(Thread threadname)
	{
		this.threadname = threadname;
	}

	public Thread getThreadname()
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
