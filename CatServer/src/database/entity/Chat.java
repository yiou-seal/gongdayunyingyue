package database.entity;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Chat extends Entityfather
{

    private int msgId;
    private int sendId;
    private int rcvId;
    private String msg;
    private java.sql.Timestamp msgtime;


    public int getMsgId()
    {
        return msgId;
    }

    public void setMsgId(int msgId)
    {
        this.msgId = msgId;
    }


    public int getSendId()
    {
        return sendId;
    }

    public void setSendId(int sendId)
    {
        this.sendId = sendId;
    }


    public int getRcvId()
    {
        return rcvId;
    }

    public void setRcvId(int rcvId)
    {
        this.rcvId = rcvId;
    }


    public String getMsg()
    {
        return msg;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }


    public java.sql.Timestamp getMsgtime()
    {
        return msgtime;
    }

    public void setMsgtime(java.sql.Timestamp msgtime)
    {
        this.msgtime = msgtime;
    }

    public String toString()
    {
        String chatstring = "" + msgId +
                "$" +
                sendId +
                "$" +
                rcvId +
                "$" +
                msg + "" +
                "$" +
                msgtime +
                "";
        return chatstring;
    }

    public Chat()
    {

    }

    public Chat(String dollarstr)
    {
        DateFormat ctdf = new SimpleDateFormat(timeformat);
        String[] spstr = dollarstr.split("\\$", -1);
        this.msgId = Integer.parseInt(spstr[0]);
        this.sendId = Integer.parseInt(spstr[1]);
        this.rcvId = Integer.parseInt(spstr[2]);
        this.msg = spstr[3];
        this.msgtime = Timestamp.valueOf(spstr[4]);
    }

    public void setvalue(ResultSet result) throws SQLException
    {

        this.setMsgId(result.getInt("msgID"));
        this.setSendId(result.getInt("sendID"));
        this.setRcvId(result.getInt("rcvID"));
        this.setMsg(result.getString("msg"));
        this.setMsgtime(result.getTimestamp("msgtime"));

    }

}
