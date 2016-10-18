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
    private String confirmPassword;

    @PostConstruct
    private void init() {
        MybatisUtil.run(UserMapper.class, CompanyMapper.class,
                (userMapper, companyMapper) -> {
            users = new LinkedList<>(userMapper.getAllUsers());
            manufacturers = companyMapper.getBriefManufacturers().stream()
                    .collect(Collectors.toMap(Company::getId, Company::getName));
        });
        roles = new LinkedHashMap<>();
        roles.put("admin", "系统管理");
        roles.put("user", "普通用户");
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
        comparePasswords();
        MybatisUtil.run(UserMapper.class, userMapper -> userMapper.updateUser(user));
        resetUser();
    }

    public void resetUser() {
        user = new User();
        confirmPassword = null;
    }

    private void comparePasswords() {
        if (!user.getPassword().equals(confirmPassword)) {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            facesContext.validationFailed();
            facesContext.addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_ERROR, "两次输入的密码不一致。", null));
        }
    }
}
