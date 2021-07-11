package database.entity;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Musicsheet extends Entityfather
{

    private int sheetID;
    private String sheetname;
    private int musicnum;
    private java.sql.Timestamp sheettime;
    private int accountID;


    public int getSheetID()
    {
        return sheetID;
    }

    public void setSheetID(int sheetID)
    {
        this.sheetID = sheetID;
    }


    public String getSheetname()
    {
        return sheetname;
    }

    public void setSheetname(String sheetname)
    {
        this.sheetname = sheetname;
    }


    public int getMusicnum()
    {
        return musicnum;
    }

    public void setMusicnum(int musicnum)
    {
        this.musicnum = musicnum;
    }


    public java.sql.Timestamp getSheettime()
    {
        return sheettime;
    }

    public void setSheettime(java.sql.Timestamp sheettime)
    {
        this.sheettime = sheettime;
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
        String sheetstring = "" + sheetID +
                "$" +
                sheetname + "" +
                "$" +
                musicnum +
                "$" +
                sheettime +
                "$" +
                accountID +
                "";
        return sheetstring;
    }

    public Musicsheet()
    {

    }

    public Musicsheet(String dollarstr)
    {
        DateFormat d = new SimpleDateFormat(timeformat);
        String[] spstr = dollarstr.split("\\$", -1);
        this.sheetID = Integer.parseInt(spstr[0]);
        this.sheetname = spstr[1];
        this.musicnum = Integer.parseInt(spstr[2]);
        this.sheettime = Timestamp.valueOf(spstr[3]);
        this.accountID = Integer.parseInt(spstr[4]);
    }

    public void setvalue(ResultSet result) throws SQLException
    {
        this.setSheetID(result.getInt("sheetID"));
        this.setSheetname(result.getString("sheetname"));
        this.setMusicnum(result.getInt("musicnum"));
        this.setSheettime(result.getTimestamp("sheettime"));
        this.setAccountID(result.getInt("accountID"));
    }
}
