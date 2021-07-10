package database.entity;


import database.entity.Entityfather;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class MusicsheetEntity extends Entityfather
{

  private int sheetId;
  private String sheetname;
  private int musicnum;
  private java.sql.Timestamp sheettime;
  private int accountId;


  public int getSheetId() {
    return sheetId;
  }

  public void setSheetId(int sheetId) {
    this.sheetId = sheetId;
  }


  public String getSheetname() {
    return sheetname;
  }

  public void setSheetname(String sheetname) {
    this.sheetname = sheetname;
  }


  public int getMusicnum() {
    return musicnum;
  }

  public void setMusicnum(int musicnum) {
    this.musicnum = musicnum;
  }


  public java.sql.Timestamp getSheettime() {
    return sheettime;
  }

  public void setSheettime(java.sql.Timestamp sheettime) {
    this.sheettime = sheettime;
  }


  public int getAccountId() {
    return accountId;
  }

  public void setAccountId(int accountId) {
    this.accountId = accountId;
  }

  public MusicsheetEntity()
  {
  }

  public void setvalue(ResultSet result) throws SQLException
  {

    this.sheetId = result.getInt("sheetID");
    this.sheetname = result.getString("sheetname");
    this.musicnum = result.getInt("musicnum");
    this.sheettime = result.getTimestamp("sheettime");
    this.accountId = result.getInt("accountID");;
  }

  @Override
  public String toString()
  {
    String sb = "" + "" +
            sheetId +
            "$" +
            sheetname + "" +
            "$" +
            musicnum +
            "$" +
            sheettime + "" +
            "$" +
            accountId +
            "";
    return sb;
  }

  public MusicsheetEntity(String dollerstr)
  {
    String[] spstr=dollerstr.split("\\$",-1);
    this.sheetId = Integer.parseInt(spstr[0]);
    this.sheetname = spstr[1];
    this.musicnum = Integer.parseInt(spstr[2]);
    this.sheettime = Timestamp.valueOf(spstr[3]);
    this.accountId = Integer.parseInt(spstr[4]);
  }
}
