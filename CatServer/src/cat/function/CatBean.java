package cat.function;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;

import javax.swing.Icon;
import javax.swing.text.SimpleAttributeSet;

public class CatBean implements Serializable {
	
	//�ַ�����
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
	private int type; // 1˽�� 0�����߸��� -1�������� 2�������ļ� 3.ȷ�������ļ�

	private HashSet<String> clients; // ���ѡ�еĿͻ�

	private HashSet<String> to;//send
	
	public HashMap<String, CatClientBean> onlines;

	private String info;//send

	private String timer;//send

	private String userid;//id

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
		this.userid = "";
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

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
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
				userid + "" +
				"#" +
				fileName + "" +
				"#" +
				wantsendto + "" +
				"#" +
				size +
				"#" +
				ip + "" +
				"+*+";
		return sb;
	}

	public CatBean(String dollerstr)
	{
		String[] spstr=dollerstr.split("#",-1);
		this.type = Integer.parseInt(spstr[0]);
		this.info = spstr[1];
		this.timer = spstr[2];
		this.userid = spstr[3];
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
