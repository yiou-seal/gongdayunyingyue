package cat.server;

import cat.function.CatBean;
import cat.util.CatUtil;
import database.databasesess;
import database.entity.Comments;
import database.entity.Music;
import database.entity.Musicsheet;
import database.entity.UsersEntity;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.stream.Collectors;

import static java.lang.Thread.sleep;

public class CommandPraser
{
    public static final String musicPath = "d:\\gdyunyingyue\\";
    private databasesess dbsession;

    public CommandPraser(databasesess dbsession)
    {
        this.dbsession = dbsession;
    }

    public void runCommand(CatServer.ClientThread cc, CatBean bean) throws InterruptedException
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
                serverBean.setUserid(bean.getUserid());// 接收的客户名称
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
                serverBean.setUserid(bean.getUserid());
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
                serverBean.setUserid(bean.getUserid());
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
                searchmusic(cc, bean);

                break;
            }
            case 18://获得音乐点赞信息，以音乐名查找
            {
                getmusicparise(cc, bean);

                break;
            }
            case 19://获得音乐评论，以音乐名查找
            {
                getmusiccommend(cc, bean);

                break;
            }
            case 20://点赞
            {
                insertmusicparise(cc, bean);

                break;
            }
            case 21://评论
            {
                insertcomment(cc, bean);

                break;
            }
            case 22://删评论
            {
                deletecomment(cc, bean);

                break;
            }
            case 23://删赞
            {
                deleteparise(cc, bean);

                break;
            }
            case 30://查看一个好友的歌单
            {
                getafriendsheet(cc, bean);

                break;
            }
            case 31://查看一个歌单的歌
            {
                getsheetmusic(cc, bean);

                break;
            }
            case 32://上传歌单
            {
                insertsheet(cc, bean);

                break;
            }
            case 33://向歌单添加歌
            {
                insertmusictosheet(cc, bean);

                break;
            }
            case 34://从歌单删除歌
            {
                deletmusicfromsheet(cc, bean);

                break;
            }
            case 35://删除歌单
            {
                deletsheet(cc, bean);

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
            String[] command = {"D:\\大三\\软工课设\\tcp\\send.exe", musicPath +bean.getFileName()+".mp3", "8888"};
            Process process = runtime.exec(command);


            // 让对方启动receive
            CatBean serverBean = new CatBean();

            serverBean.setType(3);
            HashSet<String> target = new HashSet<String>();
            target.add(bean.getUserid());
            serverBean.setClients(target);
            serverBean.setTo(bean.getTo()); // 文件目的地
            serverBean.setFileName(""+bean.getFileName()+".mp3"); // 文件名称
            serverBean.setIp(bean.getIp());
            serverBean.setPort(bean.getPort());
            serverBean.setUserid(bean.getUserid()); // 接收的客户名称
            serverBean.setTimer(bean.getTimer());
            // 通知文件来源的客户，对方确定接收文件
            cc.sendMessagewithsocket(serverBean);
            int exitcode = process.waitFor();
            System.out.println("returnfff " + exitcode);

            serverBean.setType(4);
            serverBean.setClients(target);
            serverBean.setTo(bean.getTo()); // 文件目的地
            serverBean.setFileName(""+bean.getFileName()+".mp3"); // 文件名称
            serverBean.setIp(bean.getIp());
            serverBean.setPort(bean.getPort());
            serverBean.setUserid(bean.getUserid()); // 接收的客户名称
            serverBean.setTimer(bean.getTimer());
            if (exitcode==0)
            {
                serverBean.setInfo("0");
            }
            else
            {
                serverBean.setInfo("-1");
            }
            cc.sendMessagewithsocket(serverBean);
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
            String targetip=bean.getIp();
            Runtime runtime = Runtime.getRuntime();
            String[] command = {"D:\\大三\\软工课设\\tcp\\receive.exe", targetip, "8888"};
            Process process = runtime.exec(command);

            int exitcode = process.waitFor();
            if (exitcode==0)
            {
                Music music=new Music(bean.getInfo());
                //music.setAuthorID(Integer.parseInt(bean.getUserid()));
                music.setFileplace(music.getName()+".mp3");
                dbsession.insertmusicinfo(new Music(bean.getInfo()));
                File oldName = new File(musicPath+bean.getFileName());
                oldName.renameTo(new File(musicPath+music.getName()));
                //重名情况待改/////////////////////////////////////////
            }
            System.out.println("returnfff " + exitcode);
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
        target.add(bean.getUserid());
        serverBean.setClients(target);
        serverBean.setInfo(res+"");
        serverBean.setTo(bean.getTo()); // 文件目的地
        serverBean.setFileName(""+bean.getFileName()+".mp3"); // 文件名称
        serverBean.setIp(bean.getIp());
        serverBean.setPort(bean.getPort());
        serverBean.setUserid(bean.getUserid()); // 接收的客户名称
        serverBean.setTimer(bean.getTimer());
        // 通知文件来源的客户，对方确定接收文件
        //cc.sendMessage(serverBean);
    }


    private void addfriend(CatServer.ClientThread cc, CatBean bean)
    {
        CatBean serverBean = new CatBean();
        serverBean.setType(12);
        serverBean.setIcon(bean.getIcon());
        HashSet<String> target = new HashSet<String>();
        target.add(bean.getUserid());
        serverBean.setClients(target);
        //serverBean.setTo(bean.getTo());
        serverBean.setUserid(bean.getUserid());
        serverBean.setTimer(bean.getTimer());
        String[] str = bean.getInfo().split("\\$");
        serverBean.setInfo(String.valueOf(dbsession.setnewfriend(str[0], str[1])));
        cc.sendMessage(serverBean);
        //更新好友列表
        cc.friends = dbsession.getfriendname(bean.getUserid());
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
        serverBean.setUserid(bean.getUserid());
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
        target.add(bean.getUserid());
        serverBean.setClients(target);
        serverBean.setTo(bean.getTo());
        serverBean.setUserid(bean.getUserid());
        serverBean.setTimer(bean.getTimer());
        //serverBean.setInfo( String.valueOf(dbsession.setuserinfo(new UsersEntity(bean.getInfo()))));//需要改
        UsersEntity usersEntity=new UsersEntity(bean.getInfo());
        UsersEntity usersEntity1=dbsession.getuserinfo(usersEntity.getUserId()+"");
        usersEntity.setPassword(usersEntity1.getPassword());
        dbsession.setuserinfo(usersEntity);
        serverBean.setInfo(dbsession.getuserinfo(bean.getUserid()).toString());
        System.out.println("用户信息"+dbsession.getuserinfo(bean.getUserid()).toString());
        cc.sendMessagewithsocket(serverBean);
    }
    private void edituserpassword(CatServer.ClientThread cc, CatBean bean)
    {
        CatBean serverBean = new CatBean();
        serverBean.setType(11);
        serverBean.setIcon(bean.getIcon());
        HashSet<String> target = new HashSet<String>();
        target.add(bean.getUserid());
        serverBean.setClients(target);
        serverBean.setTo(bean.getTo());
        serverBean.setUserid(bean.getUserid());
        serverBean.setTimer(bean.getTimer());
        //serverBean.setInfo( String.valueOf(dbsession.setuserinfo(new UsersEntity(bean.getInfo()))));//需要改
        UsersEntity usersEntity=new UsersEntity(bean.getInfo());
        UsersEntity usersEntity1=dbsession.getuserinfo(usersEntity.getUserId()+"");
        usersEntity.setPassword(usersEntity1.getPassword());
        dbsession.setuserinfo(usersEntity);
        serverBean.setInfo(dbsession.getuserinfo(bean.getUserid()).toString());
        cc.sendMessage(serverBean);
    }



    private void getuserinfo(CatServer.ClientThread cc, CatBean bean)
    {
        CatBean serverBean = new CatBean();

        serverBean.setType(10);
        serverBean.setIcon(bean.getIcon());
        HashSet<String> target = new HashSet<String>();
        target.add(bean.getUserid());
        serverBean.setClients(target);
        serverBean.setTo(bean.getTo());
        serverBean.setUserid(bean.getUserid());
        serverBean.setTimer(bean.getTimer());
        serverBean.setInfo(dbsession.getuserinfo(bean.getUserid()).toString());
        cc.sendMessage(serverBean);
    }

    public void sendfriendsinfo(CatServer.ClientThread cc, CatBean bean)
    {
        String friendinfo="";
        for (String fn: cc.friends)
        {
            String currentinfo="";
            String friendname=dbsession.getuserinfo(fn).getUsername();
            if (cc.onlinefrind.contains(fn))
            {
                currentinfo=fn+"$"+friendname+"$"+"1";
            }
            else
            {
                currentinfo=fn+"$"+friendname+"$"+"0";
            }
            friendinfo=friendinfo+"|"+currentinfo;
        }
        friendinfo=friendinfo.substring(1);
        for (String s:cc.onlinefrind) {
            System.out.println(s);
        }
        CatBean serverBean = new CatBean();
        serverBean.setType(10);//包含好友信息的包
        //serverBean.setInfo(cc.onlinefrind.stream().map(String::valueOf).collect(Collectors.joining("$")));
        serverBean.setInfo(friendinfo);
        HashSet<String> target = new HashSet<String>();
        target.add(bean.getUserid());
        serverBean.setUserid(bean.getUserid());
        serverBean.setClients(target);
        cc.sendMessage(serverBean);
    }

    public void sendfriendsandmyselfinfo(CatServer.ClientThread cc, CatBean bean) throws InterruptedException
    {
        String friendinfo="";
        for (String fn: cc.friends)
        {
            String currentinfo="";
            String friendname=dbsession.getuserinfo(fn).getUsername();
            if (cc.onlinefrind.contains(fn))
            {
                currentinfo=fn+"$"+friendname+"$"+"1";
            }
            else
            {
                currentinfo=fn+"$"+friendname+"$"+"0";
            }
            friendinfo=friendinfo+"|"+currentinfo;
        }
        friendinfo=friendinfo.substring(1);
        for (String s:cc.onlinefrind) {
            System.out.println(s);
        }
        CatBean serverBean = new CatBean();
        serverBean.setType(10);//包含好友信息的包
        //serverBean.setInfo(cc.onlinefrind.stream().map(String::valueOf).collect(Collectors.joining("$")));
        serverBean.setInfo(friendinfo);
        HashSet<String> target = new HashSet<String>();
        target.add(bean.getUserid());
        serverBean.setUserid(bean.getUserid());
        serverBean.setClients(target);
        serverBean.setFileName(dbsession.getuserinfo(bean.getUserid()).toString());
        System.out.println("qiana");
        sleep(1000);
        System.out.println("hou");
        cc.sendMessagewithsocket(serverBean);
    }

    private void signup(CatServer.ClientThread cc, CatBean bean) throws InterruptedException
    {//注册
        int res = dbsession.insertuserinfo(new UsersEntity(bean.getInfo()));

        CatBean serverBean = new CatBean();
        serverBean.setType(16);
        serverBean.setIcon(bean.getIcon());
        HashSet<String> target = new HashSet<String>();
        target.add(bean.getUserid());
        serverBean.setClients(target);
        serverBean.setTo(bean.getTo());
        serverBean.setUserid(bean.getUserid());
        serverBean.setTimer(bean.getTimer());
        //serverBean.setInfo( String.valueOf(dbsession.setuserinfo(new UsersEntity(bean.getInfo()))));//需要改
        serverBean.setInfo(String.valueOf(res));
        sleep(1000);
        cc.sendMessagewithsocket(serverBean);
    }

    private void signin(CatServer.ClientThread cc, CatBean bean)
    {//登录
        UsersEntity user = dbsession.getuserinfo(bean.getUserid());
        UsersEntity thisuser=new UsersEntity(bean.getInfo());
        boolean result = user.getPassword().equals(thisuser.getPassword()) && !user.getAccountstate().equals("封禁");

        CatBean serverBean = new CatBean();
        serverBean.setType(15);
        serverBean.setIcon(bean.getIcon());
        HashSet<String> target = new HashSet<String>();
        target.add(bean.getUserid());
        serverBean.setClients(target);
        serverBean.setTo(bean.getTo());
        serverBean.setUserid(bean.getUserid());
        serverBean.setTimer(bean.getTimer());
        //serverBean.setInfo( String.valueOf(dbsession.setuserinfo(new UsersEntity(bean.getInfo()))));//需要改
        if (result)
        {
            serverBean.setInfo("0");
        }
        else
        {
            serverBean.setInfo("-1");
        }


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
        serverBean.setUserid(bean.getUserid());
        if (bean.getAttributeSet() != null)
        {
            serverBean.setAttributeSet(bean.getAttributeSet());
        }
        serverBean.setTimer(bean.getTimer());
        // 向选中的客户发送数据
        cc.sendMessage(serverBean);
    }

    private void searchmusic(CatServer.ClientThread cc, CatBean bean)
    {
        Music music=new Music();
        ArrayList<Music> result=dbsession.findmusicnamelikeReturninfo(bean.getInfo());
        StringBuilder info= new StringBuilder(new String(""));
        for (int i=0;i<result.size();i++)
        {
            if (i==0)
            {
                //System.out.println("aaaaaaaaaa");
                info.append(result.get(i).toString());
            }

            else
            {
                //System.out.println("ddddddddd");
                info.append("|").append(result.get(i).toString());
            }

        }
        String strinfo=info.toString();

        CatBean serverBean = new CatBean();

        serverBean.setType(17);
        serverBean.setIcon(bean.getIcon());
        HashSet<String> target = new HashSet<String>();
        target.add(bean.getUserid());
        serverBean.setClients(target);
        serverBean.setTo(bean.getTo());
        serverBean.setUserid(bean.getUserid());
        serverBean.setTimer(bean.getTimer());
        serverBean.setInfo(strinfo);
        cc.sendMessagewithsocket(serverBean);
    }

    private void getmusiccommend(CatServer.ClientThread cc, CatBean bean)
    {

        ArrayList<Comments> result=dbsession.findcommendbymusic(bean.getInfo());
        StringBuilder info= new StringBuilder(new String(""));
        for (int i=0;i<result.size();i++)
        {
            if (i==0)
            {
                //System.out.println("aaaaaaaaaa");
                info.append(result.get(i).toString());
            }

            else
            {
                //System.out.println("ddddddddd");
                info.append("|").append(result.get(i).toString());
            }

        }
        String strinfo=info.toString();
        if (strinfo.equals(""))
        {
            strinfo="none";
        }

        CatBean serverBean = new CatBean();

        serverBean.setType(19);
        serverBean.setIcon(bean.getIcon());
        HashSet<String> target = new HashSet<String>();
        target.add(bean.getUserid());
        serverBean.setClients(target);
        serverBean.setTo(bean.getTo());
        serverBean.setUserid(bean.getUserid());
        serverBean.setTimer(bean.getTimer());
        serverBean.setInfo(strinfo);
        cc.sendMessagewithsocket(serverBean);
    }

    private void getmusicparise(CatServer.ClientThread cc, CatBean bean)
    {

        String strinfo=dbsession.getispraise$praisenum(bean.getInfo(),bean.getUserid());

        CatBean serverBean = new CatBean();

        serverBean.setType(18);
        serverBean.setIcon(bean.getIcon());
        HashSet<String> target = new HashSet<String>();
        target.add(bean.getUserid());
        serverBean.setClients(target);
        serverBean.setTo(bean.getTo());
        serverBean.setUserid(bean.getUserid());
        serverBean.setTimer(bean.getTimer());
        serverBean.setInfo(strinfo);
        cc.sendMessagewithsocket(serverBean);
    }

    private void insertmusicparise(CatServer.ClientThread cc, CatBean bean)
    {

        int res=dbsession.insertpraise(bean.getInfo(),bean.getUserid());

        CatBean serverBean = new CatBean();

        serverBean.setType(20);
        serverBean.setIcon(bean.getIcon());
        HashSet<String> target = new HashSet<String>();
        target.add(bean.getUserid());
        serverBean.setClients(target);
        serverBean.setTo(bean.getTo());
        serverBean.setUserid(bean.getUserid());
        serverBean.setTimer(bean.getTimer());
        serverBean.setInfo(res+"");
        cc.sendMessagewithsocket(serverBean);
    }

    private void insertcomment(CatServer.ClientThread cc, CatBean bean)
    {
        Comments comments=new Comments(bean.getInfo(),dbsession);
        int res=dbsession.insertcommend(comments);
        System.out.println(comments.getComment());
        CatBean serverBean = new CatBean();

        serverBean.setType(21);
        serverBean.setIcon(bean.getIcon());
        HashSet<String> target = new HashSet<String>();
        target.add(bean.getUserid());
        serverBean.setClients(target);
        serverBean.setTo(bean.getTo());
        serverBean.setUserid(bean.getUserid());
        serverBean.setTimer(bean.getTimer());
        serverBean.setInfo(res+"");
        cc.sendMessagewithsocket(serverBean);
    }

    private void deletecomment(CatServer.ClientThread cc, CatBean bean)
    {
        //String []infoss=bean.getInfo().split("\\$",-1);
        int res=dbsession.delcommendbyid(bean.getInfo());

        CatBean serverBean = new CatBean();

        serverBean.setType(22);
        serverBean.setIcon(bean.getIcon());
        HashSet<String> target = new HashSet<String>();
        target.add(bean.getUserid());
        serverBean.setClients(target);
        serverBean.setTo(bean.getTo());
        serverBean.setUserid(bean.getUserid());
        serverBean.setTimer(bean.getTimer());
        serverBean.setInfo(res+"");
        cc.sendMessagewithsocket(serverBean);
    }

    private void getafriendsheet(CatServer.ClientThread cc, CatBean bean)
    {
        Music music=new Music();
        ArrayList<String> result=dbsession.getfriendsheet(bean.getInfo());
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

        serverBean.setType(30);
        serverBean.setIcon(bean.getIcon());
        HashSet<String> target = new HashSet<String>();
        target.add(bean.getUserid());
        serverBean.setClients(target);
        serverBean.setTo(bean.getTo());
        serverBean.setUserid(bean.getUserid());
        serverBean.setTimer(bean.getTimer());
        serverBean.setInfo(strinfo);
        cc.sendMessagewithsocket(serverBean);
    }

    private void getsheetmusic(CatServer.ClientThread cc, CatBean bean)
    {
        ArrayList<String> result=dbsession.getmusicfromsheet(bean.getInfo());
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

        serverBean.setType(31);
        serverBean.setIcon(bean.getIcon());
        HashSet<String> target = new HashSet<String>();
        target.add(bean.getUserid());
        serverBean.setClients(target);
        serverBean.setTo(bean.getTo());
        serverBean.setUserid(bean.getUserid());
        serverBean.setTimer(bean.getTimer());
        serverBean.setInfo(strinfo);
        cc.sendMessagewithsocket(serverBean);
    }

    private void insertsheet(CatServer.ClientThread cc, CatBean bean)
    {
        Musicsheet ms=new Musicsheet();
        ms.setSheetname(bean.getInfo());
        ms.setAccountID(Integer.parseInt(bean.getUserid()));
        ms.setSheettime(new Timestamp(System.currentTimeMillis()));
        int res=dbsession.insertsheetinfo(ms);

        CatBean serverBean = new CatBean();

        serverBean.setType(32);
        serverBean.setIcon(bean.getIcon());
        HashSet<String> target = new HashSet<String>();
        target.add(bean.getUserid());
        serverBean.setClients(target);
        serverBean.setTo(bean.getTo());
        serverBean.setUserid(bean.getUserid());
        serverBean.setTimer(bean.getTimer());
        serverBean.setInfo(res+"");
        cc.sendMessagewithsocket(serverBean);
    }

    private void insertmusictosheet(CatServer.ClientThread cc, CatBean bean)
    {
        String []infoss=bean.getInfo().split("\\$",-1);
        int res=dbsession.insertmusictosheet(infoss[0],infoss[1]);

        CatBean serverBean = new CatBean();

        serverBean.setType(33);
        serverBean.setIcon(bean.getIcon());
        HashSet<String> target = new HashSet<String>();
        target.add(bean.getUserid());
        serverBean.setClients(target);
        serverBean.setTo(bean.getTo());
        serverBean.setUserid(bean.getUserid());
        serverBean.setTimer(bean.getTimer());
        serverBean.setInfo(res+"");
        cc.sendMessagewithsocket(serverBean);
    }

    private void deletmusicfromsheet(CatServer.ClientThread cc, CatBean bean)
    {
        String []infoss=bean.getInfo().split("\\$",-1);
        int res=dbsession.deletemusicfromsheet(infoss[0],infoss[1]);

        CatBean serverBean = new CatBean();

        serverBean.setType(34);
        serverBean.setIcon(bean.getIcon());
        HashSet<String> target = new HashSet<String>();
        target.add(bean.getUserid());
        serverBean.setClients(target);
        serverBean.setTo(bean.getTo());
        serverBean.setUserid(bean.getUserid());
        serverBean.setTimer(bean.getTimer());
        serverBean.setInfo(res+"");
        cc.sendMessagewithsocket(serverBean);
    }

    private void deletsheet(CatServer.ClientThread cc, CatBean bean)
    {
        //String []infoss=bean.getInfo().split("\\$",-1);
        int res=dbsession.deletesheet(bean.getInfo());

        CatBean serverBean = new CatBean();

        serverBean.setType(35);
        serverBean.setIcon(bean.getIcon());
        HashSet<String> target = new HashSet<String>();
        target.add(bean.getUserid());
        serverBean.setClients(target);
        serverBean.setTo(bean.getTo());
        serverBean.setUserid(bean.getUserid());
        serverBean.setTimer(bean.getTimer());
        serverBean.setInfo(res+"");
        cc.sendMessagewithsocket(serverBean);
    }

    private void deleteparise(CatServer.ClientThread cc, CatBean bean)
    {
        //String []infoss=bean.getInfo().split("\\$",-1);
        int res=dbsession.deletepraise(bean.getInfo(),bean.getUserid());

        CatBean serverBean = new CatBean();

        serverBean.setType(23);
        serverBean.setIcon(bean.getIcon());
        HashSet<String> target = new HashSet<String>();
        target.add(bean.getUserid());
        serverBean.setClients(target);
        serverBean.setTo(bean.getTo());
        serverBean.setUserid(bean.getUserid());
        serverBean.setTimer(bean.getTimer());
        serverBean.setInfo(res+"");
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
