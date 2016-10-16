/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seedqr.gui.admin;

import seedqr.gui.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.security.RolesAllowed;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.Valid;
import seedqr.mapper.UserMapper;
import seedqr.model.Region;
import seedqr.model.User;
import seedqr.util.MybatisUtil;

/**
 *
 * @author muffin
 */
@Named
@ViewScoped
@RolesAllowed("admin")
public class UserMgmt implements Serializable {

    @Inject
    private SessionData sessionData;
    private int userId = 0;

    private List<User> users;

    private List<Region> allRegion;

    private List<Region> provinces = new ArrayList<>();

    private List<Region> citys = new ArrayList<>();

    private List<Region> district = new ArrayList<>();

    private int selectProvinceId;

    private int selectCityId;

    private int selectDistId;

    @Valid
    private User user = new User();

    @PostConstruct
    private void init() {
        users = MybatisUtil.call(UserMapper.class, userMapper -> userMapper.getUsersByParent(userId));
    }

    public void addUser() {
        User exist =  MybatisUtil.call(UserMapper.class, userMapper -> userMapper.getUserByUserName(user.getUserName()));
        if (exist!=null) {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "用户名已经存在，请重新输入不同的用户名！", null));
            return;
        }
        if (user != null) {
            System.out.println("user!=null:");
//            user.setParentId(userId);
//            user.setRegionId(selectDistId);
            user.setUrole("user");
            user.setPassword("12345678");
            MybatisUtil.run(UserMapper.class, userMapper -> userMapper.addProductUser(user));
        }
        user = new User();
        users = MybatisUtil.call(UserMapper.class, userMapper -> userMapper.getUsersByParent(userId));
        //salers = MybatisUtil.getMapper(UserMapper.class).getUsersByParent(userId);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
