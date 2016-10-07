package seedqr.gui;

import java.io.Serializable;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import org.primefaces.component.tabview.TabView;
import org.primefaces.event.TabChangeEvent;
import seedqr.model.QrCode;
import seedqr.model.User;

@Named @SessionScoped @RolesAllowed("user")
public class SessionData implements Serializable {
    private User user;
    private int tab;
    private List<QrCode> qrCodes;
    private String manufacturer;

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

    public List<QrCode> getQrCodes() {
        return qrCodes;
    }

    public void setQrCodes(List<QrCode> qrCodes) {
        this.qrCodes = qrCodes;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public void tabChanged(TabChangeEvent tabChangeEvent) {
        tab = ((TabView) tabChangeEvent.getComponent()).getChildren()
                .indexOf(tabChangeEvent.getTab());
    }
}
