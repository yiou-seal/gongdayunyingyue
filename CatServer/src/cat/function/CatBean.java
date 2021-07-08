package cat.function;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.text.SimpleAttributeSet;

public class CatBean implements Serializable {
	
	//字符属性
	private SimpleAttributeSet attributeSet;

	public SimpleAttributeSet getAttributeSet() {
		return attributeSet;
	}

	public void setAttributeSet(SimpleAttributeSet attributeSet) {
		this.attributeSet = attributeSet;
	}

	private Icon icon;
	
	public Icon getIcon() {
		return icon;
	}

	public void setIcon(Icon icon) {
		this.icon = icon;
	}
	private int type; // 1私聊 0上下线更新 -1下线请求 2请求发送文件 3.确定接收文件

	private HashSet<String> clients; // 存放选中的客户

	private HashSet<String> to;//send
	
	public HashMap<String, CatClientBean> onlines;

	private String info;//send

	private String timer;//send

	private String name;//id

	private String fileName;

	private String wantsendto;

	private int size;

	private String ip;

	private int port;

	public CatBean()
	{
		this.type = 0;
		this.info = "";
		this.timer = "";
		this.name = "";
		this.fileName = "";
		this.wantsendto = "";
		this.size = 0;
		this.ip = "";
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public HashSet<String> getTo() {
		return to;
	}

	public void setTo(HashSet<String> to) {
		this.to = to;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public HashSet<String> getClients() {
		return clients;
	}

	public void setClients(HashSet<String> clients) {
		this.clients = clients;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getTimer() {
		return timer;
	}

	public void setTimer(String timer) {
		this.timer = timer;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getWantsendto()
	{
		return wantsendto;
	}

	public void setWantsendto(String wantsendto)
	{
		this.wantsendto = wantsendto;
	}

	public HashMap<String, CatClientBean> getOnlines() {
		return onlines;
	}

	public void setOnlines(HashMap<String, CatClientBean> onlines) {
		this.onlines = onlines;
	}

	@Override
	public String toString()
	{
		String sb = "" + "" +
				type +
				"#" +
				info + "" +
				"#" +
				timer + "" +
				"#" +
				name + "" +
				"#" +
				fileName + "" +
				"#" +
				wantsendto + "" +
				"#" +
				size +
				"#" +
				ip + "" +
				"";//最后必须加#
		return sb;
	}

	public CatBean(String dollerstr)
	{
		String[] spstr=dollerstr.split("#",-1);
		this.type = Integer.parseInt(spstr[0]);
		this.info = spstr[1];
		this.timer = spstr[2];
		this.name = spstr[3];
		this.fileName = spstr[4];
		this.wantsendto = spstr[5];
		this.size =  Integer.parseInt(spstr[6]);
		this.ip = spstr[7];
	}

	public static void main(String args[])
	{
		CatBean a=new CatBean();
		String c=a.toString();
		CatBean b=new CatBean(a.toString());
		System.out.println(b);
	}
}
