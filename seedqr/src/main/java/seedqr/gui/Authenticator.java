package seedqr.gui;

import javax.enterprise.inject.Model;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import seedqr.mapper.UserMapper;
import seedqr.util.MybatisUtil;

@Model
public class Authenticator {
    @Inject
    private SessionData sessionData;
    private String user;
    private String password;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void login() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        try {
            ((HttpServletRequest) externalContext.getRequest()).login(user, password);
            sessionData.setUser(MybatisUtil.call(UserMapper.class,
                    userMapper -> userMapper.getUserByUserName(user)));
        } catch (Exception ex) {
            facesContext.addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_ERROR, "登录失败。", ex.getMessage()));
        }
    }

    public void logout() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        try {
            ((HttpServletRequest) externalContext.getRequest()).logout();
        } catch (Exception ex) {
            facesContext.addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_ERROR, "退出失败。", ex.getMessage()));
        } finally {
            externalContext.invalidateSession();
        }
    }
}
