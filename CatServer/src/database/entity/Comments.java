package database.entity;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Comments extends Entityfather
{

    private int commentID;
    private String comment;
    private java.sql.Timestamp commenttime;
    private int musicID;
    private int accountID;


    public int getCommentID()
    {
        return commentID;
    }

    public void setCommentID(int commentID)
    {
        this.commentID = commentID;
    }


    public String getComment()
    {
        return comment;
    }

    public void setComment(String comment)
    {
        this.comment = comment;
    }


    public java.sql.Timestamp getCommenttime()
    {
        return commenttime;
    }

    public void setCommenttime(java.sql.Timestamp commenttime)
    {
        this.commenttime = commenttime;
    }


    public int getMusicID()
    {
        return musicID;
    }

    public void setMusicID(int musicID)
    {
        this.musicID = musicID;
    }


    public int getAccountID()
    {
        return accountID;
    }

    public void setAccountID(int accountID)
    {
        this.accountID = accountID;
    }

    public String toString()
    {
        String comstring =
                commentID +
                        "$" +
                        comment +
                        "$" +
                        commenttime +
                        "$" +
                        musicID +
                        "$" +
                        accountID +
                        "";
        return comstring;
    }

    public Comments()
    {

    }

    public Comments(String dollarstr)
    {
        DateFormat sdf = new SimpleDateFormat(timeformat);
        String[] spstr = dollarstr.split("\\$", -1);
        this.commentID = Integer.parseInt(spstr[0]);
        this.comment = spstr[1];
        this.commenttime = Timestamp.valueOf(spstr[2]);
        this.musicID = Integer.parseInt(spstr[3]);
        this.accountID = Integer.parseInt(spstr[4]);
    }

    public void setvalue(ResultSet result) throws SQLException
    {

        this.setCommentID(result.getInt("commentID"));
        this.setComment(result.getString("comment"));
        this.setCommenttime(result.getTimestamp("commenttime"));
        try
        {
            this.setMusicID(result.getInt("comments.musicID"));
        } catch (SQLException e)
        {
            this.setMusicID(result.getInt("musicID"));
        }

        this.setAccountID(result.getInt("accountID"));

    }

}
