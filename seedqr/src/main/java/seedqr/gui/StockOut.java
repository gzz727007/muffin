package seedqr.gui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.security.RolesAllowed;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named @ViewScoped @RolesAllowed("user")
public class StockOut implements Serializable {
    @Inject
    private SessionData sessionData;
    private int userId;

    private int salerId;
    private String packCode;
    private List<String> packCodes;

    @PostConstruct
    private void init() {
        packCodes = new ArrayList<>();
        userId = sessionData.getUser().getId();
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

    public List<String> getPackCodes() {
        return packCodes;
    }

    public void setPackCodes(List<String> packCodes) {
        this.packCodes = packCodes;
    }

    public int getSalerId() {
        return salerId;
    }

    public void setSalerId(int salerId) {
        this.salerId = salerId;
    }
    
    
    public void doOut() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (packCodes.isEmpty()) {
            facesContext.addMessage(null, new FacesMessage(
                FacesMessage.SEVERITY_ERROR, "请先输入打包条码。", null));
            return;
        }
        
       // String region = MybatisUtil.getMapper(RegionMapper.class).getSalerRegion(salerId);
        
        
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
