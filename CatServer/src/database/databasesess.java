package database;

import database.entity.*;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static database.entity.Entityfather.timeformat;

public class databasesess
{

    public static final String specialcommenttime = "1871-1-1 0:0:0";
    Connection con;

    public static void main(String[] args)
    {
//        String sql="select * from users";
//        try
//        {
////            Class.forName(driverName);
////            con= DriverManager.getConnection(dbURL,userName,userPwd);
////            st=con.createStatement();
////            result=st.executeQuery(sql);
////            int col=result.getMetaData().getColumnCount();
////
////            System.out.println("成功");
////            while(result.next())
////            {
////                for(int i=1;i<=col;i++)
////                    System.out.print(result.getString("username") + "\t");
////                System.out.println();
////            }
//            databasesess dbs = new databasesess();
//            UsersEntity usersEntity = dbs.getuserinfo("zth");
//            String str=usersEntity.toString();
//            UsersEntity usersEntity2=new UsersEntity(str);
//            usersEntity2.setEmail("zth@dad.vf");
//            dbs.setuserinfo(usersEntity2);
//
//            //dbs.setnewfriend("zth","qxy");
//            ArrayList<String> res=dbs.getfriendname("xwh");
//            String join = res.stream().map(String::valueOf).collect(Collectors.joining("$"));
//            System.out.println(join);
//        }catch(Exception e)
//        {
//            e.printStackTrace();
//        }
        //ceshi
//        databasesess dbs=new databasesess();
//        dbs.insertuserinfo(new UsersEntity("2$1$3$6$4$5$"));
        //ceshi
//        textcode
//        databasesess dbs=new databasesess();
//        dbs.insertuserinfo(new UsersEntity("臧天昊$224$3$6$4$5$"));
//        UsersEntity user=dbs.getuserinfo("224");
//        System.out.println(user.getUsername());
//        textcode
//        databasesess dbs=new databasesess();
//        dbs.aaa("斯卡布罗");
//        System.out.println(dbs.findmusicnamelike("斯卡布罗"));
//        textcode
        databasesess dbs=new databasesess();
        String sheetname="洗脑歌";
        Musicsheet musicsheet=new Musicsheet();
        dbs.findby(musicsheet,"musicsheet","sheetname",'\''+sheetname+'\'');
        int sheetid=musicsheet.getSheetID();
        System.out.println(sheetid);
//        textcode

    }

    public databasesess()
    {
        try
        {
            String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
            String dbURL = "jdbc:sqlserver://LAPTOP-U0GIVCOU\\ZANG;database=musicgwydb";
            String userName = "admin";
            String userPwd = "1234";
            Class.forName(driverName);
            con = DriverManager.getConnection(dbURL, userName, userPwd);

        } catch (Exception e)
        {

        }

    }

    public UsersEntity getuserinfo(String username)
    {

        Statement st;
        ResultSet result;


        String sql = "select * from users where userID =" + username + "";
        UsersEntity user = new UsersEntity();
        try
        {
            st = con.createStatement();
            result = st.executeQuery(sql);
            int col = result.getMetaData().getColumnCount();

            System.out.println("success getuserinfo");
            while (result.next())
            {

                System.out.print(result.getString("username") + "\t");
                user.setPassword(result.getString("password"));
                user.setUserId(result.getInt("userID"));
                user.setUsername(result.getString("username"));
                user.setSex(result.getString("sex"));
                user.setTelenumber(result.getString("telenumber"));
                user.setEmail(result.getString("email"));

                System.out.println();
            }

        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return user;
    }

    public boolean setuserinfo(UsersEntity user)
    {

        Statement st;
        ResultSet result;


        String sql = "update users set username='" + user.getUsername() + "',sex='" + user.getSex() + "',password='" + user.getPassword() + "',email='" + user.getEmail() + "',telenumber='" + user.getTelenumber() + "' where userID=" + user.getUserId();

        try
        {
            st = con.createStatement();
            st.executeUpdate(sql);

            System.out.println("success setuserinfo");

        } catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public int insertuserinfo(UsersEntity user)
    {


        Statement st;
        ResultSet result;
        int count = count("users", "userID");
        count++;


//        String sql="INSERT INTO users VALUES(N'"+user.getUsername()+"',"+user.getUserId()+",'"+user.getSex()+"','"+user.getPassword()+"','"+user.getEmail()+"','"+user.getTelenumber()+"','"+"正常"+"')";
        String sql = "INSERT INTO users VALUES('" + user.getUsername() + "'," + user.getUserId() + ",'" + user.getSex() + "','" + user.getPassword() + "','" + user.getEmail() + "','" + user.getTelenumber() + "','" + "正常" + "')";
        try
        {
//            sql=new String (sql.getBytes("utf-8"),"ISO-8859-1");

        } catch (Exception e)
        {

        }


        try
        {
            st = con.createStatement();
            st.executeUpdate(sql);

            System.out.println("success setuserinfo");
            return 0;

        } catch (Exception e)
        {
            e.printStackTrace();
            return -1;
        }
    }

    public ArrayList<String> getfriendname(String username)
    {

        Statement st;
        ResultSet rsresult;
        UsersEntity usersEntity = getuserinfo(username);

        String sql = "select DISTINCT username\n" +
                "from friends,users\n" +
                "where (user1= " + usersEntity.getUserId() + "and user2=userID)or (user2=" + usersEntity.getUserId() + " and user1=userID)";
        ArrayList<String> res = new ArrayList<String>();
        try
        {
            st = con.createStatement();
            rsresult = st.executeQuery(sql);
            int col = rsresult.getMetaData().getColumnCount();
            System.out.println("success getfriend");
            while (rsresult.next())
            {
                res.add(rsresult.getString("username"));
            }
            res.add(username);

        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return res;
    }

    public ArrayList<String> getfriendid(String userid)
    {

        Statement st;
        ResultSet rsresult;
        //UsersEntity usersEntity = getuserinfo(username);

        String sql = "select DISTINCT userID\n" +
                "from friends,users\n" +
                "where (user1= " + userid + "and user2=userID)or (user2=" + userid + " and user1=userID)";
        ArrayList<String> res = new ArrayList<String>();
        try
        {
            st = con.createStatement();
            rsresult = st.executeQuery(sql);
            int col = rsresult.getMetaData().getColumnCount();
            System.out.println("success getfriend");
            while (rsresult.next())
            {
                res.add(rsresult.getString("userID"));
            }
            res.add(userid);

        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return res;
    }

    public boolean setnewfriend(String username1, String username2)
    {

        Statement st;

        UsersEntity usersEntity1 = getuserinfo(username1);
        UsersEntity usersEntity2 = getuserinfo(username2);
        String sql = "insert into friends values (" + usersEntity1.getUserId() + "," + usersEntity2.getUserId() + ")";
        try
        {
            st = con.createStatement();
            st.executeUpdate(sql);

        } catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void findby(Entityfather et, String shet, String colum, String value)//value自己加单引号
    {

        Statement st;
        ResultSet result;
        String sql = "select * from " + shet + " where " + colum + " = " + value + "";
        try
        {
            st = con.createStatement();
            result = st.executeQuery(sql);
            int col = result.getMetaData().getColumnCount();

            System.out.println("success getuserinfo");
            while (result.next())
            {
                et.setvalue(result);
                System.out.println("findby");
            }

        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public ArrayList<String> findmusicnamelike(String value)
    {

        Statement st;
        ResultSet result;
        String sql = "select * from music where name like '%" + value + "%'";
        ArrayList<String> alist = new ArrayList<String>();
        try
        {
            st = con.createStatement();
            result = st.executeQuery(sql);
            int col = result.getMetaData().getColumnCount();

            System.out.println("success getuserinfo");

            while (result.next())
            {
//                et.setvalue(result);
                alist.add(result.getString("name"));
                System.out.println("findby");
            }

        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return alist;

    }

    public int insertmusicinfo(Music music)
    {


        Statement st;
        ResultSet result;
        int count = count("music", "musicID");
        count++;


        String sql = String.format("insert into music values('%s', '%s', %d, '%s', '%s', %d,'ddd')", music.getName(), music.getSinger(), count, music.getMusictype(), music.getEdition(), music.getAuthorID());


        try
        {
            st = con.createStatement();
            st.executeUpdate(sql);

            System.out.println("success setuserinfo");
            return 0;

        } catch (Exception e)
        {
            e.printStackTrace();
            return -1;
        }
    }

    public int insertcommend(Comments comments)
    {


        Statement st;
        ResultSet result;
        int count = count("comments", "commentID");
        count++;


        DateFormat sdf = new SimpleDateFormat(timeformat);
        String sql = String.format("insert into comments values(%d, '%s', '%s', %d, %d)", count, comments.getComment(), sdf.format(comments.getCommenttime()), comments.getMusicID(), comments.getAccountID());


        try
        {
            st = con.createStatement();
            st.executeUpdate(sql);

            System.out.println("success setcomment");
            return 0;

        } catch (Exception e)
        {
            e.printStackTrace();
            return -1;
        }
    }

    public int insertpraise(String musicname,String accountid)
    {


        Statement st;
        ResultSet result;
        int count = count("comments", "commentID");
        count++;

        Music music=new Music();
        findby(music,"music","name",'\''+musicname+'\'');
        int musicid=music.getMusicID();

        DateFormat sdf = new SimpleDateFormat(timeformat);
        String sql = String.format("insert into comments values(%d, '%s', '%s', %d, %d)", count, "点赞", specialcommenttime, musicid, Integer.parseInt(accountid));


        try
        {
            st = con.createStatement();
            st.executeUpdate(sql);

            System.out.println("success setcomment");
            return 0;

        } catch (Exception e)
        {
            e.printStackTrace();
            return -1;
        }
    }

    public int deletepraise(String musicname,String accountid)
    {


        Statement st;
        ResultSet result;
        int count = count("comments", "commentID");
        count++;

        Music music=new Music();
        findby(music,"music","name",'\''+musicname+'\'');
        int musicid=music.getMusicID();

        DateFormat sdf = new SimpleDateFormat(timeformat);
        String sql = String.format("DELETE FROM comments WHERE musicID = %d and accountID=%s and comment='%s'", musicid, accountid, "点赞");


        try
        {
            st = con.createStatement();
            st.executeUpdate(sql);

            System.out.println("success setcomment");
            return 0;

        } catch (Exception e)
        {
            e.printStackTrace();
            return -1;
        }
    }

    public String getispraise$praisenum(String musicname,String accountid)
    {
        Music music=new Music();
        findby(music,"music","name",'\''+musicname+'\'');
        int musicid=music.getMusicID();

        Statement st;
        ResultSet result;
        String sql1 = String.format("select count(*) from comments where comment='点赞' and commenttime='%s' and musicID= %d",specialcommenttime,musicid);
        String sql2 = String.format("select count(*) from comments where comment='点赞' and commenttime='%s' and accountID=%d and musicID= %d",specialcommenttime,Integer.parseInt(accountid),musicid);
        try
        {int num=0,isornot=0;
            st = con.createStatement();
            result = st.executeQuery(sql1);
            System.out.println("success getcount");
            if (result.next())
            {
                num= result.getInt(1);
            }
            st = con.createStatement();
            result = st.executeQuery(sql2);
            System.out.println("success getcount");
            if (result.next())
            {
                isornot= result.getInt(1);
            }
            return isornot+"$"+num;

        } catch (Exception e)
        {
            e.printStackTrace();
            return "0$0";
        }


    }

    public int delcommendbyneirong(int musicid, int userid, String comment)
    {

        Statement st;
        ResultSet result;


        String sql = String.format("DELETE FROM comments WHERE musicID = %d and accountID=%d and comment='%s'", musicid, userid, comment);


        try
        {
            st = con.createStatement();
            st.executeUpdate(sql);

            System.out.println("success setuserinfo");
            return 0;

        } catch (Exception e)
        {
            e.printStackTrace();
            return -1;
        }
    }

    public int delcommendbyid( String commentid)
    {

        Statement st;
        ResultSet result;


        String sql = String.format("DELETE FROM comments WHERE commentID=%s",  commentid);


        try
        {
            st = con.createStatement();
            st.executeUpdate(sql);

            System.out.println("success setuserinfo");
            return 0;

        } catch (Exception e)
        {
            e.printStackTrace();
            return -1;
        }
    }

    public ArrayList<Comments> findcommendbymusic(String musicname)
    {


        Statement st;
        ResultSet result;


        DateFormat sdf = new SimpleDateFormat(timeformat);
        String sql = String.format("select * from comments, music where comments.musicID=music.musicID and music.name = '%s' and commenttime <>'%s'", musicname,specialcommenttime);
        ArrayList<Comments> commentsArrayList = new ArrayList<>();
        try
        {
            st = con.createStatement();
            result = st.executeQuery(sql);
            int col = result.getMetaData().getColumnCount();
            System.out.println("success getuserinfo");

            while (result.next())
            {
                Comments comments = new Comments();
                comments.setvalue(result);
                commentsArrayList.add(comments);
                System.out.println("findby");
            }

        } catch (Exception e)
        {
            e.printStackTrace();

        }
        return commentsArrayList;
    }

    public int insertsheetinfo(Musicsheet musicsheet)
    {


        Statement st;
        ResultSet result;
        int count = count("musicsheet", "sheetID");
        count++;


        String sql = String.format("insert into musicsheet values(%d, '%s', %d, '%s', %d);", count, musicsheet.getSheetname(), 0, musicsheet.getSheettime(), musicsheet.getAccountID());


        try
        {
            st = con.createStatement();
            st.executeUpdate(sql);

            System.out.println("success setuserinfo");
            return 0;

        } catch (Exception e)
        {
            e.printStackTrace();
            return -1;
        }
    }

    public ArrayList<String> getmusicfromsheet(String sheetname)
    {


        Statement st;
        ResultSet result;


        String sql = String.format("select music.name from musicsheet, music,madeup where musicsheet.sheetID=madeup.sheetID and madeup.musicID = music.musicID and sheetname='%s'", sheetname);


        ArrayList<String> alist = new ArrayList<String>();
        try
        {
            st = con.createStatement();
            result = st.executeQuery(sql);
            int col = result.getMetaData().getColumnCount();

            System.out.println("success getuserinfo");

            while (result.next())
            {
//                et.setvalue(result);
                alist.add(result.getString("music.name"));
                System.out.println("findby");
            }

        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return alist;
    }

    public int insertmusictosheet(String sheetname,String musicname)
    {
        Musicsheet musicsheet=new Musicsheet();
        findby(musicsheet,"musicsheet","sheetname",'\''+sheetname+'\'');
        int sheetid=musicsheet.getSheetID();
        Music music=new Music();
        findby(music,"music","name",'\''+musicname+'\'');
        int musicid=music.getMusicID();



        String sql = String.format("insert into madeup values(%d, %d);", sheetid,musicid);

        Statement st;
        try
        {
            st = con.createStatement();
            st.executeUpdate(sql);

            System.out.println("success setuserinfo");
            return 0;

        } catch (Exception e)
        {
            e.printStackTrace();
            return -1;
        }
    }

    public int deletemusicfromsheet(String sheetname,String musicname)
    {
        Musicsheet musicsheet=new Musicsheet();
        findby(musicsheet,"musicsheet","sheetname",'\''+sheetname+'\'');
        int sheetid=musicsheet.getSheetID();
        Music music=new Music();
        findby(music,"music","name",'\''+musicname+'\'');
        int musicid=music.getMusicID();

        String sql = String.format("DELETE FROM madeup WHERE sheetID=%d and musicID=%d", sheetid,musicid);

        Statement st;
        try
        {
            st = con.createStatement();
            st.executeUpdate(sql);

            System.out.println("success setuserinfo");
            return 0;

        } catch (Exception e)
        {
            e.printStackTrace();
            return -1;
        }
    }

    public int deletesheet(String sheetname)
    {

        String sql = String.format("DELETE FROM musicsheet WHERE sheetname='%s'", sheetname);

        Statement st;
        try
        {
            st = con.createStatement();
            st.executeUpdate(sql);

            System.out.println("success setuserinfo");
            return 0;

        } catch (Exception e)
        {
            e.printStackTrace();
            return -1;
        }
    }



    public int count(String shet, String col)
    {

        Statement st;
        ResultSet result;
        String sql = "SELECT max(" + col + ") from " + shet;
        try
        {
            st = con.createStatement();
            result = st.executeQuery(sql);

            System.out.println("success getcount");
            if (result.next())
            {
                return result.getInt(1);
            }

        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return 0;

    }


}

