package database.entity;


public class Music extends Entityfather {

  private String name;
  private String singer;
  private int musicId;
  private String musictype;
  private String edition;
  private int authorId;
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


  public int getMusicId() {
    return musicId;
  }

  public void setMusicId(int musicId) {
    this.musicId = musicId;
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


  public int getAuthorId() {
    return authorId;
  }

  public void setAuthorId(int authorId) {
    this.authorId = authorId;
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
            musicId +
            "$" +
            musictype + "" +
            "$" +
            edition + "" +
            "$" +
            authorId +
            "$" +
            fileplace + "" +
            "";
    return sb;
  }
}
