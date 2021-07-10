package database.entity;


public class Comments extends Entityfather {

  private long commentID;
  private String comment;
  private java.sql.Timestamp commenttime;
  private long musicID;
  private long accountID;


  public long getCommentID() {
    return commentID;
  }

  public void setCommentID(long commentID) {
    this.commentID = commentID;
  }


  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }


  public java.sql.Timestamp getCommenttime() {
    return commenttime;
  }

  public void setCommenttime(java.sql.Timestamp commenttime) {
    this.commenttime = commenttime;
  }


  public long getMusicID() {
    return musicID;
  }

  public void setMusicID(long musicID) {
    this.musicID = musicID;
  }


  public long getAccountID() {
    return accountID;
  }

  public void setAccountID(long accountID) {
    this.accountID = accountID;
  }

}
