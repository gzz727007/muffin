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
import seedqr.mapper.QrCodeMapper;
import seedqr.mapper.RegionMapper;
import seedqr.model.QrCode;
import seedqr.model.SaleInfo;
import seedqr.util.MybatisUtil;

@Named @ViewScoped @RolesAllowed("user")
public class StockOut implements Serializable {
    @Inject
    private SessionData sessionData;
    @Inject
    private StockOutService service;
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
        List<Long> longCodes = new ArrayList<>();
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (packCodes.isEmpty()) {
            facesContext.addMessage(null, new FacesMessage(
                FacesMessage.SEVERITY_ERROR, "请先输入打包条码。", null));
            return;
        }
        
        for (String code : packCodes){
            if(code==null || code.length()!=19 || !code.startsWith("1000")) {
                facesContext.addMessage(null, new FacesMessage(
                FacesMessage.SEVERITY_ERROR, "你输入的" + code + "不满足编码要求，外袋码必须为19位数字，且满足格式。", null));
                return;
            }
            try {
                longCodes.add(Long.valueOf(code));
            }catch(Exception e)
            {
                facesContext.addMessage(null, new FacesMessage(
                FacesMessage.SEVERITY_ERROR, "你输入的" + code + "不满足编码要求，外袋码必须为19位数字，且满足格式。", null));
                return;
            }
        }
        service.doOut(sessionData.getUser(),longCodes,salerId);
        
//        
//        String region = MybatisUtil.call(RegionMapper.class, regionMapper -> regionMapper.getSalerRegion(salerId));
//        List<QrCode> qrcodes = MybatisUtil.call(QrCodeMapper.class, codeMapper -> codeMapper.getQrCodeByUnitId(region));
        
        
    }
}
