package database.entity;

import javax.persistence.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

@Entity
@Table(name = "users", schema = "dbo", catalog = "musicgwydb")
public class UsersEntity extends Entityfather
{

    private String username;
    private int userId;
    private String sex;
    private String password;
    private String telenumber;
    private String email;
    private String accountstate;

    @Basic
    @Column(name = "username")
    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    @Id
    @Column(name = "userID")
    public int getUserId()
    {
        return userId;
    }

    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    @Basic
    @Column(name = "sex")
    public String getSex()
    {
        return sex;
    }

    public void setSex(String sex)
    {
        this.sex = sex;
    }

    @Basic
    @Column(name = "password")
    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    @Basic
    @Column(name = "telenumber")
    public String getTelenumber()
    {
        return telenumber;
    }

    public void setTelenumber(String telenumber)
    {
        this.telenumber = telenumber;
    }

    @Basic
    @Column(name = "email")
    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    @Basic
    @Column(name = "accountstate")
    public String getAccountstate()
    {
        return accountstate;
    }

    public void setAccountstate(String accountstate)
    {
        this.accountstate = accountstate;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsersEntity that = (UsersEntity) o;
        return userId == that.userId && Objects.equals(username, that.username) && Objects.equals(sex, that.sex) && Objects.equals(password, that.password) && Objects.equals(telenumber, that.telenumber) && Objects.equals(email, that.email) && Objects.equals(accountstate, that.accountstate);
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("");
        sb.append(username).append("");
        sb.append("$")
                .append(userId);
        sb.append("$")
                .append(sex).append("");
        sb.append("$")
                .append(password).append("");
        sb.append("$")
                .append(telenumber).append("");
        sb.append("$")
                .append(email).append("");
        sb.append("$")
                .append(accountstate).append("");
        sb.append("");
        return sb.toString();
    }

    public UsersEntity()
    {

    }

    public UsersEntity(String dollerstr)
    {
        String[] spstr=dollerstr.split("\\$",-1);
        this.username = spstr[0];
        this.userId = Integer.parseInt(spstr[1]);
        this.sex = spstr[2];
        this.password = spstr[3];
        this.telenumber = spstr[4];
        this.email = spstr[5];
        this.accountstate = spstr[6];
    }

    public void setvalue(ResultSet result) throws SQLException
    {
        this.setPassword(result.getString("password"));
        this.setUserId(result.getInt("userID"));
        this.setUsername(result.getString("username"));
        this.setSex(result.getString("sex"));
        this.setTelenumber(result.getString("telenumber"));
        this.setEmail(result.getString("email"));
    }


    @Override
    public int hashCode()
    {
        return Objects.hash(username, userId, sex, password, telenumber, email, accountstate);
    }
}
