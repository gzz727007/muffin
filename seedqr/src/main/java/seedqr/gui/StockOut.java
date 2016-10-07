package seedqr.gui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.security.RolesAllowed;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import seedqr.mapper.UserMapper;
import seedqr.model.User;
import seedqr.util.MybatisUtil;

@Named @ViewScoped @RolesAllowed("user")
public class StockOut implements Serializable {
    @Inject
    private SessionData sessionData;
    private int userId;

    private List<User> salers;

    private String packCode;
    private List<String> packCodes;

    @PostConstruct
    private void init() {
        packCodes = new ArrayList<>();
        userId = sessionData.getUser().getId();
        salers = MybatisUtil.getMapper(UserMapper.class).getUsersByParent(userId);
    }


    public String getPackCode() {
        return packCode;
    }

    public void setPackCode(String packCode) {
        this.packCode = packCode;
    }


    public void addPackCode() {
        packCodes.add(packCode);
        packCode = null;
    }

    public List<User> getSalers() {
        return salers;
    }

    public void setSalers(List<User> salers) {
        this.salers = salers;
    }

    public List<String> getPackCodes() {
        return packCodes;
    }

    public void setPackCodes(List<String> packCodes) {
        this.packCodes = packCodes;
    }
    
    
    
    public void bindCodes() {
//        FacesContext facesContext = FacesContext.getCurrentInstance();
//        if (smallPackCodes.size() != amount) {
//            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
//                    "设定的小包数量（" + amount + "）与实际扫描的数量（"
//                    + smallPackCodes.size() + "）不一致。", null));
//            return;
//        }
//
//        MybatisUtil.getMapper(QrCodeMapper.class).addQrCodeMapping(bulkPackCode,
//                smallPackCodes.stream().collect(Collectors.joining(",")));
//        facesContext.addMessage(null, new FacesMessage(
//                FacesMessage.SEVERITY_INFO, "绑定成功。", null));
//        bulkPackCode = null;
//        packCode = null;
//        smallPackCodes.clear();
    }
}
