package cat.server;

import cat.function.CatBean;
import database.databasesess;
import database.entity.Music;
import database.entity.UsersEntity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.stream.Collectors;

public class CommandPraser
{
    private databasesess dbsession;

    public CommandPraser(databasesess dbsession)
    {
        this.dbsession = dbsession;
    }

    public void runCommand(CatServer.ClientThread cc, CatBean bean)
    {
        switch (bean.getType())
        {
            case 1:
            { // 聊天

                chat(cc, bean);
                break;
            }
            case 2:
            { // 请求接受文件
                receivemusic(cc, bean);

                break;
            }
            case 3:
            {
                sendmusic(cc, bean);
                break;
            }
            case 4:
            {
                CatBean serverBean = new CatBean();
                serverBean.setType(4);
                serverBean.setClients(bean.getClients()); // 文件来源
                serverBean.setTo(bean.getTo()); // 文件目的地
                serverBean.setFileName(bean.getFileName());
                serverBean.setInfo(bean.getInfo());
                serverBean.setName(bean.getName());// 接收的客户名称
                serverBean.setTimer(bean.getTimer());
                cc.sendMessage(serverBean);
                break;
            }
            case 9:
            {
                CatBean serverBean = new CatBean();

                serverBean.setType(9);
                serverBean.setClients(bean.getClients());
                serverBean.setTo(bean.getTo());
                serverBean.setName(bean.getName());
                serverBean.setTimer(bean.getTimer());
                cc.sendMessage(serverBean);

                break;
            }
            case 8:
            {
                CatBean serverBean = new CatBean();

                serverBean.setType(8);
                serverBean.setIcon(bean.getIcon());
                serverBean.setClients(bean.getClients());
                serverBean.setTo(bean.getTo());
                serverBean.setName(bean.getName());
                serverBean.setTimer(bean.getTimer());
                cc.sendMessage(serverBean);

                break;
            }
            case 10://查询信息
            {
                getuserinfo(cc, bean);

                break;
            }
            case 11://更改信息
            {
                edituserinfo(cc, bean);

                break;
            }
            case 12://添加好友
            {
                addfriend(cc, bean);

                break;
            }
            case 15://登录
            {
                signin(cc, bean);

                break;
            }
            case 16://注册
            {
                signup(cc, bean);

                break;
            }
            case 17://搜索音乐
            {
                getmusicinfo(cc, bean);

                break;
            }
            default:
            {
                break;
            }
        }
    }

    private synchronized void sendmusic(CatServer.ClientThread cc, CatBean bean)
    {
        try
        {
            Runtime runtime = Runtime.getRuntime();
            String[] command = {"D:\\大三\\软工课设\\tcp\\send.exe", "d:\\gdyunyingyue\\"+bean.getFileName()+".mp3", "8888"};
            Process process = runtime.exec(command);


            // 让对方启动receive
            CatBean serverBean = new CatBean();

            serverBean.setType(3);
            HashSet<String> target = new HashSet<String>();
            target.add(bean.getName());
            serverBean.setClients(target);
            serverBean.setTo(bean.getTo()); // 文件目的地
            serverBean.setFileName(""+bean.getFileName()+".mp3"); // 文件名称
            serverBean.setIp(bean.getIp());
            serverBean.setPort(bean.getPort());
            serverBean.setName(bean.getName()); // 接收的客户名称
            serverBean.setTimer(bean.getTimer());
            // 通知文件来源的客户，对方确定接收文件
            cc.sendMessagewithsocket(serverBean);
            int exitcode = process.waitFor();
            System.out.println("returnfff " + exitcode);
        } catch (Exception e)
        {
            System.out.println(e);
        }
    }

    private synchronized void receivemusic(CatServer.ClientThread cc, CatBean bean)
    {
        int res=0;
        try
        {
            Runtime runtime = Runtime.getRuntime();
            String[] command = {"D:\\大三\\软工课设\\tcp\\receive.exe", cc.getIpthis(), "8888"};
            Process process = runtime.exec(command);

            int exitcode = process.waitFor();
            System.out.println("returnfff " + exitcode);
            dbsession.insertmusicinfo(new Music(bean.getInfo()));
            res=1;

        } catch (Exception e)
        {
            System.out.println(e);
            res=0;
        }
        // 让对方启动receive
        CatBean serverBean = new CatBean();

        serverBean.setType(2);
        HashSet<String> target = new HashSet<String>();
        target.add(bean.getName());
        serverBean.setClients(target);
        serverBean.setInfo(res+"");
        serverBean.setTo(bean.getTo()); // 文件目的地
        serverBean.setFileName(""+bean.getFileName()+".mp3"); // 文件名称
        serverBean.setIp(bean.getIp());
        serverBean.setPort(bean.getPort());
        serverBean.setName(bean.getName()); // 接收的客户名称
        serverBean.setTimer(bean.getTimer());
        // 通知文件来源的客户，对方确定接收文件
        cc.sendMessage(serverBean);
    }


    private void addfriend(CatServer.ClientThread cc, CatBean bean)
    {
        CatBean serverBean = new CatBean();
        serverBean.setType(12);
        serverBean.setIcon(bean.getIcon());
        HashSet<String> target = new HashSet<String>();
        target.add(bean.getName());
        serverBean.setClients(target);
        //serverBean.setTo(bean.getTo());
        serverBean.setName(bean.getName());
        serverBean.setTimer(bean.getTimer());
        String[] str = bean.getInfo().split("\\$");
        serverBean.setInfo(String.valueOf(dbsession.setnewfriend(str[0], str[1])));
        cc.sendMessage(serverBean);
        //更新好友列表
        cc.friends = dbsession.getfriendname(bean.getName());
        //找出在线的好友
        cc.getonlinefriends();
        //下面发送包含好友信息的包给发起添加的人
        sendfriendsinfo(cc, bean);

        //给被添加的人发消息
        serverBean.setType(13);
        serverBean.setIcon(bean.getIcon());
        target = new HashSet<String>();
        target.add(str[1]);
        serverBean.setClients(target);
        //serverBean.setTo(bean.getTo());
        serverBean.setName(bean.getName());
        serverBean.setTimer(bean.getTimer());

        serverBean.setInfo(str[0]);
        cc.sendMessage(serverBean);
    }

    private void edituserinfo(CatServer.ClientThread cc, CatBean bean)
    {
        CatBean serverBean = new CatBean();
        serverBean.setType(11);
        serverBean.setIcon(bean.getIcon());
        HashSet<String> target = new HashSet<String>();
        target.add(bean.getName());
        serverBean.setClients(target);
        serverBean.setTo(bean.getTo());
        serverBean.setName(bean.getName());
        serverBean.setTimer(bean.getTimer());
        //serverBean.setInfo( String.valueOf(dbsession.setuserinfo(new UsersEntity(bean.getInfo()))));//需要改
        dbsession.setuserinfo(new UsersEntity(bean.getInfo()));
        serverBean.setInfo(dbsession.getuserinfo(bean.getName()).toString());
        cc.sendMessage(serverBean);
    }

    private void getuserinfo(CatServer.ClientThread cc, CatBean bean)
    {
        CatBean serverBean = new CatBean();

        serverBean.setType(10);
        serverBean.setIcon(bean.getIcon());
        HashSet<String> target = new HashSet<String>();
        target.add(bean.getName());
        serverBean.setClients(target);
        serverBean.setTo(bean.getTo());
        serverBean.setName(bean.getName());
        serverBean.setTimer(bean.getTimer());
        serverBean.setInfo(dbsession.getuserinfo(bean.getName()).toString());
        cc.sendMessage(serverBean);
    }

    public void sendfriendsinfo(CatServer.ClientThread cc, CatBean bean)
    {
        CatBean serverBean = new CatBean();
        serverBean.setType(10);//包含好友信息的包
        serverBean.setInfo(cc.onlinefrind.stream().map(String::valueOf).collect(Collectors.joining("$")));
        HashSet<String> target = new HashSet<String>();
        target.add(bean.getName());
        serverBean.setName(bean.getName());
        serverBean.setClients(target);
        cc.sendMessage(serverBean);
    }

    private void signup(CatServer.ClientThread cc, CatBean bean)
    {//注册
        int res = dbsession.insertuserinfo(new UsersEntity(bean.getInfo()));

        CatBean serverBean = new CatBean();
        serverBean.setType(16);
        serverBean.setIcon(bean.getIcon());
        HashSet<String> target = new HashSet<String>();
        target.add(bean.getName());
        serverBean.setClients(target);
        serverBean.setTo(bean.getTo());
        serverBean.setName(bean.getName());
        serverBean.setTimer(bean.getTimer());
        //serverBean.setInfo( String.valueOf(dbsession.setuserinfo(new UsersEntity(bean.getInfo()))));//需要改
        serverBean.setInfo(String.valueOf(res));
        cc.sendMessagewithsocket(serverBean);
    }

    private void signin(CatServer.ClientThread cc, CatBean bean)
    {//登录
        UsersEntity user = dbsession.getuserinfo(bean.getName());
        UsersEntity thisuser=new UsersEntity(bean.getInfo());
        boolean result = user.getPassword().equals(thisuser.getPassword());

        CatBean serverBean = new CatBean();
        serverBean.setType(15);
        serverBean.setIcon(bean.getIcon());
        HashSet<String> target = new HashSet<String>();
        target.add(bean.getName());
        serverBean.setClients(target);
        serverBean.setTo(bean.getTo());
        serverBean.setName(bean.getName());
        serverBean.setTimer(bean.getTimer());
        //serverBean.setInfo( String.valueOf(dbsession.setuserinfo(new UsersEntity(bean.getInfo()))));//需要改
        if (result)
            serverBean.setInfo("1");
        else
            serverBean.setInfo("0");

        cc.sendMessagewithsocket(serverBean);
    }

    private void chat(CatServer.ClientThread cc, CatBean bean)
    {
        //		创建服务器的catbean，并发送给客户端
        CatBean serverBean = new CatBean();
        serverBean.setType(1);
        HashSet<String> target = new HashSet<String>();
        target.add(bean.getWantsendto());
        serverBean.setClients(target);
        serverBean.setInfo(bean.getInfo());
        serverBean.setName(bean.getName());
        if (bean.getAttributeSet() != null)
        {
            serverBean.setAttributeSet(bean.getAttributeSet());
        }
        serverBean.setTimer(bean.getTimer());
        // 向选中的客户发送数据
        cc.sendMessage(serverBean);
    }

    private void getmusicinfo(CatServer.ClientThread cc, CatBean bean)
    {
        Music music=new Music();
        ArrayList<String> result=dbsession.findmusicnamelike(bean.getInfo());
        StringBuilder info= new StringBuilder(new String(""));
        for (int i=0;i<result.size();i++)
        {
            if (i==0)
            {
                //System.out.println("aaaaaaaaaa");
                info.append(result.get(i));
            }

            else
            {
                //System.out.println("ddddddddd");
                info.append("$").append(result.get(i));
            }

        }
        String strinfo=info.toString();

        CatBean serverBean = new CatBean();

        serverBean.setType(17);
        serverBean.setIcon(bean.getIcon());
        HashSet<String> target = new HashSet<String>();
        target.add(bean.getName());
        serverBean.setClients(target);
        serverBean.setTo(bean.getTo());
        serverBean.setName(bean.getName());
        serverBean.setTimer(bean.getTimer());
        serverBean.setInfo(strinfo);
        cc.sendMessagewithsocket(serverBean);
    }

    public static void aaa(String a)
    {
        System.out.println(a);
    }

    public static void main(String [] args)
    {
        String a="斯卡布罗";

        aaa(a);


    }
}
