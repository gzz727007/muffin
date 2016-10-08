package seedqr.gui;

import java.io.Serializable;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import org.primefaces.component.tabview.TabView;
import org.primefaces.event.TabChangeEvent;
import seedqr.model.User;

@Named @SessionScoped @RolesAllowed("user")
public class SessionData implements Serializable {
    private User user;
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
    }

    public void tabChanged(TabChangeEvent tabChangeEvent) {
        tab = ((TabView) tabChangeEvent.getComponent()).getChildren()
                .indexOf(tabChangeEvent.getTab());
    }
}
