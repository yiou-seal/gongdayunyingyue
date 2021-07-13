package cat.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

import javax.swing.DefaultListModel;
import javax.swing.table.DefaultTableModel;

import cat.function.CatBean;
import cat.function.CatClientBean;
import cat.util.CatUtil;

import database.*;

public class CatServer
{
    private static final char MSGENDCHAR = 0xff;
    DefaultListModel myListmodel = new DefaultListModel<>();
    private static ServerView serverView = null;
    private static ServerSocket ss;
    public static HashMap<String, CatClientBean> onlines;//����������Ϣ
    public static Socket asock;

    static DefaultTableModel defaultTableModel = new DefaultTableModel();

    static Vector<String> column = new Vector<>();

    static
    {
        try
        {

            ss = new ServerSocket(8520);

            onlines = new HashMap<String, CatClientBean>();

            defaultTableModel.addColumn("�û���");
            defaultTableModel.addColumn("����ip��ַ");
            defaultTableModel.addColumn("��½ʱ��");
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    class ClientThread extends Thread
    {//�������ӵģ�ÿ���û�һ���߳�

        private Socket clientthis;
        private String ipthis;
        private CatBean bean;
        private ObjectInputStream ois;
        private ObjectOutputStream oos;
        databasesess dbsession;
        CommandPraser cp;
        HashSet<String> onlinefrind = new HashSet<String>();
        ArrayList<String> friends = new ArrayList<String>();
        public static final char MSGENDCHAR = 0xff;

        public ClientThread(Socket clientthis, databasesess dbsession)
        {
            this.clientthis = clientthis;
            this.dbsession = dbsession;
            cp = new CommandPraser(dbsession);
        }

        @Override
        public void run()
        {
            try
            {
                bean = new CatBean();
                bean.setType(41);

                BufferedInputStream buffIn = new BufferedInputStream(clientthis.getInputStream());
                DataInputStream dataIn = new DataInputStream(buffIn);
                // ��ͣ�Ĵӿͻ��˽�����Ϣ
                while (true)
                {


                    int c;

                    boolean didRun = false;
                    boolean isCommand = false;
                    //StringBuffer strBuff = new StringBuffer();
                    ArrayList<Byte> strBuff = new ArrayList<Byte>(0);
                    //sleep(10);
                    while ((c = dataIn.read()) != 0xff)
                    {
                        //strBuff.append((char)c);
                        int d = c;
                        strBuff.add((byte) (d & 0xFF));
                        if (!didRun) didRun = true;
                        if (c == 0xFD) isCommand = true;
                    }

                    System.out.println(strBuff.toString());
                    byte[] bj = new byte[strBuff.size()];
                    for (int i = 0; i < strBuff.size(); i++)
                    {
                        bj[i] = strBuff.get(i);
                    }
                    String receive = new String(bj);

                    System.out.println(receive);


                    // ��ȡ�ӿͻ��˽��յ���catbean��Ϣ
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

                    serverView.textArea.setText(receive);
                    bean = new CatBean(receive);
                    // ����catbean�У�type������һ������
                    switch (bean.getType())
                    {
                        // �����߸���
                        case 0:
                        { // ����
                            // ��¼���߿ͻ����û����Ͷ˿���clientbean��
                            CatClientBean cbean = new CatClientBean();
                            cbean.setName(bean.getUserid());
                            cbean.setSocket(clientthis);
                            cbean.setThreadname(Thread.currentThread().getName());
                            cbean.setIp(bean.getIp());
                            this.ipthis= bean.getIp();

                            // ��������û�
                            onlines.put(bean.getUserid(), cbean);

                            new Thread()
                            {
                                public void run()
                                {
                                    myListmodel.addElement(bean.getUserid());
                                    Object[] data = new Object[3];

                                    data[0] = (bean.getUserid());
//                                    data[1] = (onlines.get(bean.getUserid()).getSocket().getLocalSocketAddress().toString());
                                    data[1] = (onlines.get(bean.getUserid()).getIp());
                                    data[2] = (CatUtil.getTimer());

                                    defaultTableModel.addRow(data);//把信息添加到界面上
                                    serverView.list.setModel(myListmodel);
                                    serverView.table.setModel(defaultTableModel);
                                }

                                ;
                            }.start();

                            // ������������catbean�������͸��ͻ���
                            CatBean serverBean = new CatBean();
                            serverBean.setType(0);
                            serverBean.setInfo(bean.getTimer() + "  "
                                    + bean.getUserid() + "������");
                            // Ѱ�Һ����б�
                            friends = dbsession.getfriendid(bean.getUserid());
                            //�ҳ����ߵĺ���
                            getonlinefriends();
                            //onlinefrind.addAll(onlines.keySet());
                            serverBean.setClients(onlinefrind);//ֻ��ʾ���ߺ���
                            //serverBean.setClients(new HashSet<>(friends));
                            sendMessage(serverBean);

                            //���淢�Ͱ���������Ϣ�İ�

                            cp.sendfriendsinfo(this,bean);

                            break;
                        }
                        case -1:
                        { // ����
                            // ������������catbean�������͸��ͻ���
                            CatBean serverBean = new CatBean();
                            serverBean.setType(-1);

                            try
                            {
                                oos = new ObjectOutputStream(
                                        clientthis.getOutputStream());
                                oos.writeObject(serverBean);
                                oos.flush();
                            } catch (IOException e)
                            {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                            onlines.remove(bean.getUserid());
                            new Thread()
                            {
                                public void run()
                                {
                                    for (int i = 0; i < defaultTableModel.getRowCount(); i++)
                                    {
                                        if (defaultTableModel.getValueAt(i, 0).equals(bean.getUserid()))
                                        {
                                            defaultTableModel.removeRow(i);
                                        }
                                    }
                                    myListmodel.removeElement(bean.getUserid());
                                    serverView.list.setModel(myListmodel);

                                }

                                ;
                            }.start();
                            // ��ʣ�µ������û����������뿪��֪ͨ
                            CatBean serverBean2 = new CatBean();
                            serverBean2.setInfo("\r\n" + bean.getTimer() + "  "
                                    + bean.getUserid() + "" + " ������");
                            serverBean2.setType(0);

                            //�ҳ����ߵĺ���
                            getonlinefriends();
                            //onlinefrind.addAll(onlines.keySet());
                            serverBean2.setClients(onlinefrind);
                            sendMessage(serverBean2);

//							HashSet<String> set = new HashSet<String>();
//							set.addAll(onlines.keySet());
//							serverBean2.setClients(set);
//
//							sendAll(serverBean2);


                            //���̽���
                            return;
                        }
                        default:
                        {
                            cp.runCommand(this, bean);
                            break;
                        }
                    }
                }
            } catch (IOException e)
            {
                unexpectexit();
                // TODO Auto-generated catch block
                e.printStackTrace();
                //} catch (ClassNotFoundException e)
                //{
                // TODO Auto-generated catch block
            } finally
            {
                close();
            }
        }

//		private void addfriend()
//		{
//			CatBean serverBean = new CatBean();
//			serverBean.setType(12);
//			serverBean.setIcon(bean.getIcon());
//			serverBean.setClients(bean.getClients());
//			//serverBean.setTo(bean.getTo());
//			serverBean.setName(bean.getName());
//			serverBean.setTimer(bean.getTimer());
//			String[] str=bean.getInfo().split("\\$");
//			serverBean.setInfo( String.valueOf(dbsession.setnewfriend(str[0],str[1])));
//			sendMessage(serverBean);
//			//���º����б�
//			friends=dbsession.getfriendname(bean.getName());
//			//�ҳ����ߵĺ���
//			getonlinefriends();
//			//���淢�Ͱ���������Ϣ�İ���������ӵ���
//			sendfriendsinfo();
//
//			//������ӵ��˷���Ϣ
//			serverBean.setType(13);
//			serverBean.setIcon(bean.getIcon());
//			HashSet<String> target = new HashSet<String>();
//			target.add(str[1]);
//			serverBean.setClients(target);
//			//serverBean.setTo(bean.getTo());
//			serverBean.setName(bean.getName());
//			serverBean.setTimer(bean.getTimer());
//
//			serverBean.setInfo(str[0]);
//			sendMessage(serverBean);
//		}
//
//		private void edituserinfo()
//		{
//			CatBean serverBean = new CatBean();
//			serverBean.setType(11);
//			serverBean.setIcon(bean.getIcon());
//			serverBean.setClients(bean.getClients());
//			serverBean.setTo(bean.getTo());
//			serverBean.setName(bean.getName());
//			serverBean.setTimer(bean.getTimer());
//			//serverBean.setInfo( String.valueOf(dbsession.setuserinfo(new UsersEntity(bean.getInfo()))));//��Ҫ��
//			dbsession.setuserinfo(new UsersEntity(bean.getInfo()));
//			serverBean.setInfo(dbsession.getuserinfo(bean.getName()).toString());
//			sendMessage(serverBean);
//		}
//
//		private void getuserinfo()
//		{
//			CatBean serverBean = new CatBean();
//
//			serverBean.setType(10);
//			serverBean.setIcon(bean.getIcon());
//			serverBean.setClients(bean.getClients());
//			serverBean.setTo(bean.getTo());
//			serverBean.setName(bean.getName());
//			serverBean.setTimer(bean.getTimer());
//			serverBean.setInfo(dbsession.getuserinfo(bean.getName()).toString());
//			sendMessage(serverBean);
//		}

//        private void sendfriendsinfo()
//        {
//            CatBean serverBean = new CatBean();
//            serverBean.setType(14);//����������Ϣ�İ�
//            serverBean.setInfo(onlinefrind.stream().map(String::valueOf).collect(Collectors.joining("$")));
//            HashSet<String> target = new HashSet<String>();
//            target.add(bean.getName());
//            serverBean.setName(bean.getName());
//            serverBean.setClients(target);
//            sendMessage(serverBean);
//        }

//		private void signin()
//		{
//			UsersEntity user= dbsession.getuserinfo(bean.getName());
//			boolean result=user.getPassword().equals(bean.getInfo());
//
//			CatBean serverBean = new CatBean();
//			serverBean.setType(14);
//			serverBean.setIcon(bean.getIcon());
//			serverBean.setClients(bean.getClients());
//			serverBean.setTo(bean.getTo());
//			serverBean.setName(bean.getName());
//			serverBean.setTimer(bean.getTimer());
//			//serverBean.setInfo( String.valueOf(dbsession.setuserinfo(new UsersEntity(bean.getInfo()))));//��Ҫ��
//			serverBean.setInfo(String.valueOf(result));
//			sendMessage(serverBean);
//		}
//
//		private void signup()
//		{
//			UsersEntity user= dbsession.getuserinfo(bean.getName());
//			boolean result=user.getPassword().equals(bean.getInfo());
//
//			CatBean serverBean = new CatBean();
//			serverBean.setType(14);
//			serverBean.setIcon(bean.getIcon());
//			serverBean.setClients(bean.getClients());
//			serverBean.setTo(bean.getTo());
//			serverBean.setName(bean.getName());
//			serverBean.setTimer(bean.getTimer());
//			//serverBean.setInfo( String.valueOf(dbsession.setuserinfo(new UsersEntity(bean.getInfo()))));//��Ҫ��
//			serverBean.setInfo(String.valueOf(result));
//			sendMessage(serverBean);
//		}

        void getonlinefriends()
        {
            onlinefrind.clear();
            // �ҳ����ߵĺ���
            for (String key : onlines.keySet())
            {
                if (friends.contains(key))
                {
                    onlinefrind.add(key);
                }
            }
        }

        private void chat()
        {
            //		������������catbean�������͸��ͻ���
            CatBean serverBean = new CatBean();
            serverBean.setType(1);
            serverBean.setClients(bean.getClients());//Ŀ���û�
            serverBean.setInfo(bean.getInfo());
            serverBean.setUserid(bean.getUserid());
            if (bean.getAttributeSet() != null)
            {
                serverBean.setAttributeSet(bean.getAttributeSet());
            }
            serverBean.setTimer(bean.getTimer());
            // ��ѡ�еĿͻ���������
            sendMessage(serverBean);
        }


        // ��ѡ�е��û���������
        void sendMessage(CatBean serverBean)
        {
            // ����ȡ�����е�values
            Set<String> cbs = onlines.keySet();
            Iterator<String> it = cbs.iterator();
            // ѡ�пͻ�
            HashSet<String> clients = serverBean.getClients();
            while (it.hasNext())
            {
                // ���߿ͻ�
                String client = it.next();
                // ѡ�еĿͻ����������ߵģ��ͷ���serverbean
                if (clients.contains(client))
                {
                    Socket c = onlines.get(client).getSocket();
                    //ObjectOutputStream oos;
                    DataOutputStream oos;
                    try
                    {
                        //oos = new ObjectOutputStream(c.getOutputStream());
                        oos = new DataOutputStream(c.getOutputStream());
                        //oos.writeObject(serverBean);
                        oos.write(bean.toString().getBytes("gbk"));
                        oos.flush();
                    } catch (IOException e)
                    {
                        unexpectexit();
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }

        void sendMessagewithsocket(CatBean serverBean)
        {
                    //ObjectOutputStream oos;
                    DataOutputStream oos;
                    try
                    {
                        //oos = new ObjectOutputStream(c.getOutputStream());
                        oos = new DataOutputStream(clientthis.getOutputStream());
                        //oos.writeObject(serverBean);
                        oos.write(serverBean.toString().getBytes("gbk"));
                        oos.flush();
                    } catch (IOException e)
                    {
                        unexpectexit();
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }


        }

        // �����е��û���������
        private void sendAll(CatBean serverBean)
        {
            Collection<CatClientBean> clients = onlines.values();
            Iterator<CatClientBean> it = clients.iterator();
            ObjectOutputStream oos;
            while (it.hasNext())
            {
                Socket c = it.next().getSocket();
                try
                {
                    oos = new ObjectOutputStream(c.getOutputStream());
                    oos.writeObject(serverBean);
                    oos.flush();
                } catch (IOException e)
                {
                    unexpectexit();
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        private void close()
        {
            if (oos != null)
            {
                try
                {
                    oos.close();
                } catch (IOException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (ois != null)
            {
                try
                {
                    ois.close();
                } catch (IOException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (clientthis != null)
            {
                try
                {
                    clientthis.close();
                } catch (IOException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        public String getIpthis()
        {
            return ipthis;
        }

        public void setIpthis(String ipthis)
        {
            this.ipthis = ipthis;
        }

        private void unexpectexit()
        {
            onlines.remove(bean.getUserid());
            new Thread()
            {
                public void run()
                {
                    for (int i = 0; i < defaultTableModel.getRowCount(); i++)
                    {
                        if (defaultTableModel.getValueAt(i, 0).equals(bean.getUserid()))
                        {
                            defaultTableModel.removeRow(i);
                        }
                    }
                    myListmodel.removeElement(bean.getUserid());
                    serverView.list.setModel(myListmodel);

                }

                ;
            }.start();
            // ��ʣ�µ������û����������뿪��֪ͨ
            CatBean serverBean2 = new CatBean();
            serverBean2.setInfo("\r\n" + bean.getTimer() + "  "
                    + bean.getUserid() + "" + " ������");
            serverBean2.setType(0);

            //�ҳ����ߵĺ���
            getonlinefriends();
            //onlinefrind.addAll(onlines.keySet());
            serverBean2.setClients(onlinefrind);
            sendMessage(serverBean2);
        }
    }

    public void start()
    {
        databasesess dbs = new databasesess();
        try
        {
            while (true)
            {
                Socket client = ss.accept();
                asock = client;
                new ClientThread(client, dbs).start();
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }

    }


    public static void main(String[] args)
    {
//        try
//        {
//            Runtime runtime = Runtime.getRuntime();
//            String[] command = {"D:\\����\\������\\tcp\\send.exe","C:\\Users\\zang\\Music\\���ұ���.mp3","8888"};
//            Process process = runtime.exec(command);
//
//
////            while (true)
////            {
////                //System.out.println("dasd");
////                InputStream stream = process.getInputStream();
////                if (stream.available() > 0)
////                {
////                    byte[] btmp = new byte[stream.available()];
////                    stream.read(btmp);
////                    System.out.println(btmp);
////                }
////            }
//
//
//            int exitcode = process.waitFor();
//            System.out.println("returnfff "+exitcode);
//        } catch (Exception e)
//        {
//            System.out.println(e);
//        }

        serverView = new ServerView();
        serverView.setVisible(true);
        CatServer catServer = new CatServer();
        catServer.start();
        serverView.setCatServer(catServer);


    }
}


