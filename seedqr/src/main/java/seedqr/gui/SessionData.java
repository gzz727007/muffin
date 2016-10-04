package seedqr.gui;

import java.io.Serializable;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import seedqr.model.User;

@Named @SessionScoped @RolesAllowed("user")
public class SessionData implements Serializable {
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
