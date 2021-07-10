package database;

import database.entity.Entityfather;
import database.entity.UsersEntity;

import javax.xml.registry.infomodel.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class databasesess
{
    Connection con;
    public static void main(String [] args)
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
        databasesess dbs=new databasesess();


    }

    public databasesess()
    {
        try
        {
            String driverName="com.microsoft.sqlserver.jdbc.SQLServerDriver";
            String dbURL="jdbc:sqlserver://LAPTOP-U0GIVCOU\\ZANG;database=musicgwydb?characterEncoding=UTF-8";
            String userName="admin";
            String userPwd="1234";
            Class.forName(driverName);
            con= DriverManager.getConnection(dbURL,userName,userPwd);

        }
        catch (Exception e)
        {

        }

    }

    public  UsersEntity getuserinfo(String username)
    {

        Statement st;
        ResultSet result;



        String sql="select * from users where userID ="+username+"";
        UsersEntity user=new UsersEntity();
        try
        {
            st=con.createStatement();
            result=st.executeQuery(sql);
            int col=result.getMetaData().getColumnCount();

            System.out.println("success getuserinfo");
            while(result.next())
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

        }catch(Exception e)
        {
            e.printStackTrace();
        }
        return user;
    }

    public  boolean setuserinfo(UsersEntity user)
    {

        Statement st;
        ResultSet result;


        String sql="update users set username='"+user.getUsername()+"',sex='"+user.getSex()+"',password='"+user.getPassword()+"',email='"+user.getEmail()+"',telenumber='"+user.getTelenumber()+"' where userID="+user.getUserId();

        try
        {
            st=con.createStatement();
            st.executeUpdate(sql);

            System.out.println("success setuserinfo");

        }catch(Exception e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public  int insertuserinfo(UsersEntity user)
    {

        Statement st;
        ResultSet result;
        int count=count("users");
        count++;



        String sql="INSERT INTO users VALUES('"+user.getUsername()+"',"+user.getUserId()+",'"+user.getSex()+"','"+user.getPassword()+"','"+user.getEmail()+"','"+user.getTelenumber()+"','"+"正常"+"')";

        try
        {
            st=con.createStatement();
            st.executeUpdate(sql);

            System.out.println("success setuserinfo");
            return 0;

        }catch(Exception e)
        {
            e.printStackTrace();
            return -1;
        }
    }

    public ArrayList<String> getfriendname(String username)
    {

        Statement st;
        ResultSet rsresult;
        UsersEntity usersEntity=getuserinfo(username);

        String sql="select DISTINCT username\n" +
                "from friends,users\n" +
                "where (user1= "+usersEntity.getUserId()+"and user2=userID)or (user2="+usersEntity.getUserId()+" and user1=userID)";
        ArrayList<String> res=new ArrayList<String>();
        try
        {
            st=con.createStatement();
            rsresult=st.executeQuery(sql);
            int col=rsresult.getMetaData().getColumnCount();
            System.out.println("success getfriend");
            while(rsresult.next())
            {
                res.add(rsresult.getString("username"));
            }
            res.add(username);

        }catch(Exception e)
        {
            e.printStackTrace();
        }
        return res;
    }

    public boolean setnewfriend(String username1,String username2)
    {

        Statement st;

        UsersEntity usersEntity1=getuserinfo(username1);
        UsersEntity usersEntity2=getuserinfo(username2);
        String sql="insert into friends values ("+usersEntity1.getUserId()+","+usersEntity2.getUserId()+")";
        try
        {
            st=con.createStatement();
            st.executeUpdate(sql);

        }catch(Exception e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void findby(Entityfather et,String shet,String colum,String value)//value自己加单引号
    {

        Statement st;
        ResultSet result;
        String sql="select * from "+shet+" where "+colum+" = "+value+"";
        try
        {
            st=con.createStatement();
            result=st.executeQuery(sql);
            int col=result.getMetaData().getColumnCount();

            System.out.println("success getuserinfo");
            while(result.next())
            {
                et.setvalue(result);
                System.out.println("findby");
            }

        }catch(Exception e)
        {
            e.printStackTrace();
        }

    }

    public ArrayList<String> findmusicnamelike(String value)//value自己加单引号
    {

        Statement st;
        ResultSet result;
        String sql="select * from music where name like '%"+value+"%'";
        ArrayList<String> alist=new ArrayList<String>();
        try
        {
            st=con.createStatement();
            result=st.executeQuery(sql);
            int col=result.getMetaData().getColumnCount();

            System.out.println("success getuserinfo");

            while(result.next())
            {
//                et.setvalue(result);
                alist.add(result.getString("name"));
                System.out.println("findby");
            }

        }catch(Exception e)
        {
            e.printStackTrace();
        }
        return alist;

    }

    public int count(String shet)
    {

        Statement st;
        ResultSet result;
        String sql="SELECT COUNT(*) from "+shet;
        try
        {
            st=con.createStatement();
            result=st.executeQuery(sql);

            System.out.println("success getcount");
            if(result.next())
            {
                return result.getInt(1);
            }

        }catch(Exception e)
        {
            e.printStackTrace();
        }
        return 0;

    }
}

