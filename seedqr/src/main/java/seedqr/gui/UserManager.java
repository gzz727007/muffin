package seedqr.gui;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.Valid;
import seedqr.mapper.CompanyMapper;
import seedqr.mapper.UserMapper;
import seedqr.model.Company;
import seedqr.model.User;
import seedqr.util.MybatisUtil;

@Named @ViewScoped
public class UserManager implements Serializable {
    private List<User> users;
    private Map<Integer, String> manufacturers;
    private Map<String, String> roles;
    @Valid
    private User user;
    
    private String newPassword;
    private String confirmPassword;

    @Inject
    private SessionData sessionData;
    
    @PostConstruct
    private void init() {
        MybatisUtil.run(UserMapper.class, CompanyMapper.class,
                (userMapper, companyMapper) -> {
            users = new LinkedList<>(userMapper.getAllUsers());
            manufacturers = companyMapper.getBriefManufacturers().stream()
                    .collect(Collectors.toMap(Company::getId, Company::getName));
        });
        roles = new LinkedHashMap<>();
        roles.put("user", "管理人员");
        roles.put("keeper", "库管人员");
        roles.put("packer", "打包人员");
        user = new User();
    }

    public List<User> getUsers() {
        return users;
    }

    public Map<Integer, String> getManufacturers() {
        return manufacturers;
    }

    public Map<String, String> getRoles() {
        return roles;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        confirmPassword = user.getPassword();
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public void addUser() {
        comparePasswords();
        MybatisUtil.run(UserMapper.class, userMapper -> userMapper.addUser(user));
        users.add(0, user);
        resetUser();
    }

    public void updateUser() {
        if (comparePasswords()) {
            MybatisUtil.run(UserMapper.class, userMapper -> userMapper.updateUser(user));
            resetUser();
        }
    }
    
    public void updateUserPassword() {
        user = sessionData.getUser();
        if (checkPasswords()) {
            user.setPassword(newPassword);
            MybatisUtil.run(UserMapper.class, userMapper -> userMapper.updateUser(user));
            FacesContext facesContext = FacesContext.getCurrentInstance();
            facesContext.addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_INFO, "密码修改成功，下次登陆请使用新密码", null));
        }
    }

    public void resetUser() {
        user = new User();
        confirmPassword = null;
    }

    private boolean comparePasswords() {
        if (!user.getPassword().equals(confirmPassword)) {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            facesContext.validationFailed();
            facesContext.addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_ERROR, "两次输入的密码不一致。", null));
            return false;
        }
        return true;
    }
    
    private boolean checkPasswords() {
        if (newPassword == null || newPassword.equals("")) {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            facesContext.validationFailed();
            facesContext.addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_ERROR, "新密码不能为空", null));
            return false;
        }
        if (!newPassword.equals(confirmPassword)) {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            facesContext.validationFailed();
            facesContext.addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_ERROR, "两次输入的密码不一致。", null));
            return false;
        }
        return true;
    }
}
