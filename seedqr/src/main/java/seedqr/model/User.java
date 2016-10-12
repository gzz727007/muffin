/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seedqr.model;

import java.sql.Date;
import javax.validation.constraints.Size;

/**
 *
 * @author muffin
 */
public class User {
    private int id;
    private String userName;
    @Size(min = 1, max = 20, message = "用户昵称不能为空且不超过 20 个字符。")
    private String name;
    private String password;
    private String urole;
    private String email;
    @Size(min = 1, max = 20, message = "用户手机不能为空且不超过 20 个字符。")
    private String handphone;
    private Date createTime;
    private String companyCode;
    @Size(min = 1, max = 50, message = "用户昵称不能为空且不超过 50 个字符。")
    private String companyName;
    private Date lastLoginTime;
    private int type;
    private int parentId;
    private int status;
    private int regionId;
    @Size(min = 1, max = 20, message = "联系人不能为空且不超过 20 个字符。")
    private String contact;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrole() {
        return urole;
    }

    public void setUrole(String urole) {
        this.urole = urole;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHandphone() {
        return handphone;
    }

    public void setHandphone(String handphone) {
        this.handphone = handphone;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getRegionId() {
        return regionId;
    }

    public void setRegionId(int regionId) {
        this.regionId = regionId;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", userName=" + userName + ", name=" + name + ", password=" + password + ", urole=" + urole + ", email=" + email + ", handphone=" + handphone + ", createTime=" + createTime + ", companyCode=" + companyCode + ", companyName=" + companyName + ", lastLoginTime=" + lastLoginTime + ", type=" + type + ", parentId=" + parentId + ", status=" + status + ", regionId=" + regionId + ", contact=" + contact + '}';
    }
    
    
    
}
