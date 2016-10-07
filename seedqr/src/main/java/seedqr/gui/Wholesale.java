package seedqr.gui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.security.RolesAllowed;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named @ViewScoped @RolesAllowed("user")
public class Wholesale implements Serializable {
    private String bulkPackCode;
    private String smallPackCode;
    private List<String> smallPackCodes;

    @PostConstruct
    private void init() {
        smallPackCodes = new ArrayList<>();
    }

    public String getBulkPackCode() {
        return bulkPackCode;
    }

    public void setBulkPackCode(String bulkPackCode) {
        this.bulkPackCode = bulkPackCode;
    }

    public String getSmallPackCode() {
        return smallPackCode;
    }

    public void setSmallPackCode(String smallPackCode) {
        this.smallPackCode = smallPackCode;
    }

    public List<String> getSmallPackCodes() {
        return smallPackCodes;
    }

    public void addSmallPackCode() {
        smallPackCodes.add(smallPackCode);
    }

    public void bindCodes() {
        
    }
}
