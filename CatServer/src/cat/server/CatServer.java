package cat.server;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import javax.swing.DefaultListModel;
import javax.swing.table.DefaultTableModel;
import cat.function.CatBean;
import cat.function.CatClientBean;
import cat.util.CatUtil;
import com.google.gson.JsonObject;

import database.*;
import database.entity.UsersEntity;

public class CatServer {
	private static final char MSGENDCHAR = 0xff;
	DefaultListModel myListmodel =new DefaultListModel<>();
	private static ServerView serverView =null ;
	private static ServerSocket ss;
	public static HashMap<String, CatClientBean> onlines;//保存连接信息
	public static Socket asock;

	static DefaultTableModel defaultTableModel = new DefaultTableModel();

	static Vector<String> column =new Vector<>();

	static {
		try {

			ss = new ServerSocket(8520);

			onlines = new HashMap<String, CatClientBean>();

			defaultTableModel.addColumn("用户名");
			defaultTableModel.addColumn("主机ip地址");
			defaultTableModel.addColumn("登陆时间");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	class ClientThread extends Thread {//用于连接的，每个用户一个线程

		private Socket client;
		private CatBean bean;
		private ObjectInputStream ois;
		private ObjectOutputStream oos;
		databasesess dbsession;
		CommandPraser cp;
		HashSet<String> onlinefrind = new HashSet<String>();
		ArrayList<String> friends=new ArrayList<String>();
		public static final char MSGENDCHAR = 0xff;

		public ClientThread(Socket client,databasesess dbsession) {
			this.client = client;
			this.dbsession=dbsession;
			cp=new CommandPraser(dbsession);
		}

		@Override
		public void run()
		{
			try
			{
				bean = new CatBean();
				bean.setType(41);

				BufferedInputStream buffIn = new BufferedInputStream(client.getInputStream());
				DataInputStream dataIn = new DataInputStream(buffIn);
				// 不停的从客户端接收信息
				while (true)
				{



						int c;

						boolean didRun = false;
						boolean isCommand = false;
						//StringBuffer strBuff = new StringBuffer();
						ArrayList<Byte> strBuff=new ArrayList<Byte>(0);
						//sleep(10);
						while( (c=dataIn.read()) != 0xff) {
							//strBuff.append((char)c);
							int d=c;
							strBuff.add((byte)(d & 0xFF));
							if(!didRun) didRun=true;
							if(c==0xFD) isCommand = true;
						}

						System.out.println(strBuff.toString());
					byte[] bj=new byte[strBuff.size()];
					for (int i=0;i<strBuff.size();i++)
					{
						bj[i]=strBuff.get(i);
					}
						String receive=new String(bj);

					System.out.println(receive);



					// 读取从客户端接收到的catbean信息
//					ois = new ObjectInputStream(client.getInputStream());
//					bean = (CatBean) ois.readObject();


//						int c;
//						boolean didRun = false;
//						boolean isCommand = false;
//						//StringBuffer strBuff = new StringBuffer(0);
//						ArrayList<Byte> strBuff=new ArrayList<Byte>(0);
//						//sleep(10);
//						while( (c=dataIn.read()) != 0xff) {
//							if (c==-1)
//								break;
//							strBuff.add((byte)(c & 0xFF));
//							if(!didRun) didRun=true;
//							if(c==0xFD) isCommand = true;
//						}
//					byte[] bj=new byte[strBuff.size()];
//					for (int i=0;i<strBuff.size();i++)
//					{
//						bj[i]=strBuff.get(i);
//					}
//						String receive=new String(bj);
//
//					System.out.println(receive);





//					byte[] by=receive.getBytes("utf-8");
//					ArrayList<Byte> ne=new ArrayList<Byte>(0);
//					for (int i=by.length-1;i>=0;i--)
//					{
//						if(by[i]!=0)
//							continue;
//						ne.add(by[i]);
//					}
//					byte[] bj=new byte[ne.size()];
//					for (int i=0;i<ne.size();i++)
//					{
//						bj[i]=ne.get(i);
//					}
//
//					String jiema = new String(bj);
					//String jiema= URLDecoder.decode(receive,"utf-8");
					String aaa ="你";
					byte[] bbb=aaa.getBytes();

//						System.out.println(jiema);
//E5
					serverView.textArea.setText(receive);
					// 分析catbean中，type是那样一种类型
					switch (bean.getType())
					{
						// 上下线更新
						case 0:
						{ // 上线
							// 记录上线客户的用户名和端口在clientbean中
							CatClientBean cbean = new CatClientBean();
							cbean.setName(bean.getName());
							cbean.setSocket(client);
							cbean.setThreadname(Thread.currentThread().getName());
							// 添加在线用户
							onlines.put(bean.getName(), cbean);

							new Thread()
							{
								public void run()
								{
									myListmodel.addElement(bean.getName());
									Object[] data = new Object[3];

									data[0] = (bean.getName());
									data[1] = (onlines.get(bean.getName()).getSocket().getLocalSocketAddress().toString());
									data[2] = (CatUtil.getTimer());

									defaultTableModel.addRow(data);//登录的用户名，ip,时间，加到界面的表里
									serverView.list.setModel(myListmodel);
									serverView.table.setModel(defaultTableModel);
								}

								;
							}.start();

							// 创建服务器的catbean，并发送给客户端
							CatBean serverBean = new CatBean();
							serverBean.setType(0);
							serverBean.setInfo(bean.getTimer() + "  "
									+ bean.getName() + "上线了");
							// 寻找好友列表
							friends=dbsession.getfriendname(bean.getName());
							//找出在线的好友
							getonlinefriends();
							//onlinefrind.addAll(onlines.keySet());
							serverBean.setClients(onlinefrind);
							sendMessage(serverBean);

							//下面发送包含好友信息的包

							sendfriendsinfo();

							break;
						}
						case -1:
						{ // 下线
							// 创建服务器的catbean，并发送给客户端
							CatBean serverBean = new CatBean();
							serverBean.setType(-1);

							try
							{
								oos = new ObjectOutputStream(
										client.getOutputStream());
								oos.writeObject(serverBean);
								oos.flush();
							} catch (IOException e)
							{
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							onlines.remove(bean.getName());
							new Thread()
							{
								public void run()
								{
									for (int i = 0; i < defaultTableModel.getRowCount(); i++)
									{
										if (defaultTableModel.getValueAt(i, 0).equals(bean.getName()))
										{
											defaultTableModel.removeRow(i);
										}
									}
									myListmodel.removeElement(bean.getName());
									serverView.list.setModel(myListmodel);

								}

								;
							}.start();
							// 向剩下的在线用户发送有人离开的通知
							CatBean serverBean2 = new CatBean();
							serverBean2.setInfo("\r\n" + bean.getTimer() + "  "
									+ bean.getName() + "" + " 下线了");
							serverBean2.setType(0);

							//找出在线的好友
							getonlinefriends();
							//onlinefrind.addAll(onlines.keySet());
							serverBean.setClients(onlinefrind);
							sendMessage(serverBean);

//							HashSet<String> set = new HashSet<String>();
//							set.addAll(onlines.keySet());
//							serverBean2.setClients(set);
//
//							sendAll(serverBean2);




							//进程结束
							return;
						}
						default:
						{
							cp.runCommand(this,bean);
							break;
						}
					}
				}
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			//} catch (ClassNotFoundException e)
			//{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally
			{
				close();
			}
		}

		private void addfriend()
		{
			CatBean serverBean = new CatBean();
			serverBean.setType(12);
			serverBean.setIcon(bean.getIcon());
			serverBean.setClients(bean.getClients());
			//serverBean.setTo(bean.getTo());
			serverBean.setName(bean.getName());
			serverBean.setTimer(bean.getTimer());
			String[] str=bean.getInfo().split("\\$");
			serverBean.setInfo( String.valueOf(dbsession.setnewfriend(str[0],str[1])));
			sendMessage(serverBean);
			//更新好友列表
			friends=dbsession.getfriendname(bean.getName());
			//找出在线的好友
			getonlinefriends();
			//下面发送包含好友信息的包给发起添加的人
			sendfriendsinfo();

			//给被添加的人发消息
			serverBean.setType(12);
			serverBean.setIcon(bean.getIcon());
			HashSet<String> target = new HashSet<String>();
			target.add(str[1]);
			serverBean.setClients(target);
			//serverBean.setTo(bean.getTo());
			serverBean.setName(bean.getName());
			serverBean.setTimer(bean.getTimer());

			serverBean.setInfo(str[0]);
			sendMessage(serverBean);
		}

		private void edituserinfo()
		{
			CatBean serverBean = new CatBean();
			serverBean.setType(11);
			serverBean.setIcon(bean.getIcon());
			serverBean.setClients(bean.getClients());
			serverBean.setTo(bean.getTo());
			serverBean.setName(bean.getName());
			serverBean.setTimer(bean.getTimer());
			//serverBean.setInfo( String.valueOf(dbsession.setuserinfo(new UsersEntity(bean.getInfo()))));//需要改
			dbsession.setuserinfo(new UsersEntity(bean.getInfo()));
			serverBean.setInfo(dbsession.getuserinfo(bean.getName()).toString());
			sendMessage(serverBean);
		}

		private void getuserinfo()
		{
			CatBean serverBean = new CatBean();

			serverBean.setType(10);
			serverBean.setIcon(bean.getIcon());
			serverBean.setClients(bean.getClients());
			serverBean.setTo(bean.getTo());
			serverBean.setName(bean.getName());
			serverBean.setTimer(bean.getTimer());
			serverBean.setInfo(dbsession.getuserinfo(bean.getName()).toString());
			sendMessage(serverBean);
		}

		private void sendfriendsinfo()
		{
			CatBean serverBean = new CatBean();
			serverBean.setType(13);//包含好友信息的包
			serverBean.setInfo(onlinefrind.stream().map(String::valueOf).collect(Collectors.joining("$")));
			HashSet<String> target = new HashSet<String>();
			target.add(bean.getName());
			serverBean.setName(bean.getName());
			serverBean.setClients(target);
			sendMessage(serverBean);
		}

		private void signin()
		{
			UsersEntity user= dbsession.getuserinfo(bean.getName());
			boolean result=user.getPassword().equals(bean.getInfo());

			CatBean serverBean = new CatBean();
			serverBean.setType(14);
			serverBean.setIcon(bean.getIcon());
			serverBean.setClients(bean.getClients());
			serverBean.setTo(bean.getTo());
			serverBean.setName(bean.getName());
			serverBean.setTimer(bean.getTimer());
			//serverBean.setInfo( String.valueOf(dbsession.setuserinfo(new UsersEntity(bean.getInfo()))));//需要改
			serverBean.setInfo(String.valueOf(result));
			sendMessage(serverBean);
		}

		private void signup()
		{
			UsersEntity user= dbsession.getuserinfo(bean.getName());
			boolean result=user.getPassword().equals(bean.getInfo());

			CatBean serverBean = new CatBean();
			serverBean.setType(14);
			serverBean.setIcon(bean.getIcon());
			serverBean.setClients(bean.getClients());
			serverBean.setTo(bean.getTo());
			serverBean.setName(bean.getName());
			serverBean.setTimer(bean.getTimer());
			//serverBean.setInfo( String.valueOf(dbsession.setuserinfo(new UsersEntity(bean.getInfo()))));//需要改
			serverBean.setInfo(String.valueOf(result));
			sendMessage(serverBean);
		}

		void getonlinefriends()
		{
			onlinefrind.clear();
			// 找出在线的好友
			for (String key : onlines.keySet()) {
				if (friends.contains(key))
				{
					onlinefrind.add(key);
				}
			}
		}

		private void chat()
		{
			//		创建服务器的catbean，并发送给客户端
			CatBean serverBean = new CatBean();
			serverBean.setType(1);
			serverBean.setClients(bean.getClients());//目标用户
			serverBean.setInfo(bean.getInfo());
			serverBean.setName(bean.getName());
			if (bean.getAttributeSet() != null)
			{
				serverBean.setAttributeSet(bean.getAttributeSet());
			}
			serverBean.setTimer(bean.getTimer());
			// 向选中的客户发送数据
			sendMessage(serverBean);
		}


		// 向选中的用户发送数据
		void sendMessage(CatBean serverBean) {
			// 首先取得所有的values
			Set<String> cbs = onlines.keySet();
			Iterator<String> it = cbs.iterator();
			// 选中客户
			HashSet<String> clients = serverBean.getClients();
			while (it.hasNext()) {
				// 在线客户
				String client = it.next();
				// 选中的客户中若是在线的，就发送serverbean
				if (clients.contains(client)) {
					Socket c = onlines.get(client).getSocket();
					ObjectOutputStream oos;
					try {
						oos = new ObjectOutputStream(c.getOutputStream());
						oos.writeObject(serverBean);
						oos.flush();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		}

		// 向所有的用户发送数据
		private void sendAll(CatBean serverBean) {
			Collection<CatClientBean> clients = onlines.values();
			Iterator<CatClientBean> it = clients.iterator();
			ObjectOutputStream oos;
			while (it.hasNext()) {
				Socket c = it.next().getSocket();
				try {
					oos = new ObjectOutputStream(c.getOutputStream());
					oos.writeObject(serverBean);
					oos.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		private void close() {
			if (oos != null) {
				try {
					oos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (ois != null) {
				try {
					ois.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (client != null) {
				try {
					client.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public void start() {
		databasesess dbs=new databasesess();
		try {
			while (true) {
				Socket client = ss.accept();
				asock=client;
				new ClientThread(client,dbs).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}



	public static void main(String[] args) {
		String aaa ="" + "你" + MSGENDCHAR;
		byte[] bbb=aaa.getBytes();
		String ccc=new String(bbb);
		serverView = new ServerView();
		serverView.setVisible(true);
		CatServer catServer =new CatServer();
		catServer.start();
		serverView.setCatServer(catServer);
	}

}


