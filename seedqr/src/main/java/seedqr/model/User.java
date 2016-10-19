package seedqr.model;

import java.sql.Date;
import javax.validation.constraints.Size;

public class User {
    private int id;
    @Size(min = 1, max = 20, message = "用户名不能为空且不超过 20 个字符。")
    private String userName;
    @Size(min = 5, max = 10, message = "密码不少于 5 个且不超过 20 个字符。")
    private String password;
    private String role;
    private Date createTime;
    private Date lastLoginTime;
    private int companyId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", userName=" + userName + ", password=" + password + ", role=" + role + ", createTime=" + createTime + ", lastLoginTime=" + lastLoginTime + ", companyId=" + companyId + '}';
    }
    
}
