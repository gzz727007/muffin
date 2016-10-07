package seedqr.gui;

import java.io.Serializable;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import seedqr.model.QrCode;
import seedqr.model.User;

@Named @SessionScoped @RolesAllowed("user")
public class SessionData implements Serializable {
    private User user;
    private List<QrCode> qrCodes;
    private String manufacturer;

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
}
