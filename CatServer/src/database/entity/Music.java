package database.entity;


import java.sql.ResultSet;
import java.sql.SQLException;

public class Music extends Entityfather {

  private String name;
  private String singer;
  private int musicID;
  private String musictype;
  private String edition;
  private int authorID;
  private String fileplace;


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }


  public String getSinger() {
    return singer;
  }

  public void setSinger(String singer) {
    this.singer = singer;
  }


  public int getMusicID() {
    return musicID;
  }

  public void setMusicID(int musicID) {
    this.musicID = musicID;
  }


  public String getMusictype() {
    return musictype;
  }

  public void setMusictype(String musictype) {
    this.musictype = musictype;
  }


  public String getEdition() {
    return edition;
  }

  public void setEdition(String edition) {
    this.edition = edition;
  }


  public int getAuthorID() {
    return authorID;
  }

  public void setAuthorID(int authorID) {
    this.authorID = authorID;
  }


  public String getFileplace() {
    return fileplace;
  }

  public void setFileplace(String fileplace) {
    this.fileplace = fileplace;
  }

  @Override
  public String toString()
  {
    String sb = "" + "$" +
            name + "" +
            "$" +
            singer + "" +
            "$" +
            musicID +
            "$" +
            musictype + "" +
            "$" +
            edition + "" +
            "$" +
            authorID +
            "$" +
            fileplace + "" +
            "";
    return sb;
  }



  public Music()
  {

  }

  public Music(String dollarstr)
  {
    String[] spstr=dollarstr.split("\\$",-1);
    this.name = spstr[0];
    this.singer = spstr[1];
    this.musicID = Integer.parseInt(spstr[2]);
    this.musictype = spstr[3];
    this.edition = spstr[4];
    this.authorID = Integer.parseInt(spstr[5]);
    this.fileplace = spstr[6];
  }

  public void setvalue(ResultSet result) throws SQLException
  {
    this.setName(result.getString("name"));
    this.setMusictype(result.getString("musictype"));
    this.setEdition(result.getString("edition"));
    this.setAuthorID(result.getInt("authorID"));
    this.setFileplace(result.getString("fileplace"));
  }
}
