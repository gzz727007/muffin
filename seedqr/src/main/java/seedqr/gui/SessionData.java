package seedqr.gui;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import org.primefaces.component.tabview.TabView;
import org.primefaces.event.TabChangeEvent;
import seedqr.mapper.CompanyMapper;
import seedqr.mapper.UserMapper;
import seedqr.model.Company;
import seedqr.model.User;
import seedqr.util.MybatisUtil;

@Named @SessionScoped
public class SessionData implements Serializable {
    private User user;
    private Company company;
    private int tab;

    public int getTab() {
        return tab;
    }

    public void setTab(int tab) {
        this.tab = tab;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        company = MybatisUtil.call(CompanyMapper.class,
                companyMapper -> companyMapper.getCompany(user.getCompanyId()));
    }

    public Company getCompany() {
        return company;
    }

    public void tabChanged(TabChangeEvent tabChangeEvent) {
        tab = ((TabView) tabChangeEvent.getComponent()).getChildren()
                .stream().filter(tab -> tab.isRendered())
                .collect(Collectors.toList())
                .indexOf(tabChangeEvent.getTab());
    }

    public boolean isUserInRole(List<String> roles) {
        return roles.contains(user.getRole());
    }

    public int getUserId() {
        return user.getId();
    }

    public int getCompanyId() {
        return company.getId();
    }
    
    public void updateUserStatus() {
        MybatisUtil.run(UserMapper.class, userMapper -> userMapper.updateUserOnlineStatus(user));
    }
}
