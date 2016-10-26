package seedqr.gui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import seedqr.util.CodeUtil;

@Named
@ViewScoped
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
        String parsePackCode = packCode;
        packCode = "";
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (parsePackCode.startsWith("1000") && parsePackCode.length() == 19) {
            packCodes.add(parsePackCode);
        } else {
            System.out.println(parsePackCode);
            parsePackCode = CodeUtil.parseCode(parsePackCode);
            if (parsePackCode.startsWith("1000") && parsePackCode.length() == 19) {
                if (packCodes.contains(parsePackCode)) {
                    facesContext.addMessage(null, new FacesMessage(
                        FacesMessage.SEVERITY_ERROR, "你扫描的外袋码已经扫描！", null));
                } else {
                    packCodes.add(parsePackCode);
                }
            } else {
                facesContext.addMessage(null, new FacesMessage(
                        FacesMessage.SEVERITY_ERROR, "你扫描的不是外袋码！", null));
            }
        }
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

        for (String code : packCodes) {
            if (code == null || code.length() != 19 || !code.startsWith("1000")) {
                facesContext.addMessage(null, new FacesMessage(
                        FacesMessage.SEVERITY_ERROR, "你输入的" + code + "不满足编码要求，外袋码必须为19位数字，且满足格式。", null));
                return;
            }
            try {
                longCodes.add(Long.valueOf(code));
            } catch (Exception e) {
                facesContext.addMessage(null, new FacesMessage(
                        FacesMessage.SEVERITY_ERROR, "你输入的" + code + "不满足编码要求，外袋码必须为19位数字，且满足格式。", null));
                return;
            }
        }
        service.doOut(sessionData.getUser(), longCodes, salerId);
        facesContext.addMessage(null, new FacesMessage(
                FacesMessage.SEVERITY_INFO, "出库成功！", null));
        packCodes.clear();
//        
//        String region = MybatisUtil.call(RegionMapper.class, regionMapper -> regionMapper.getSalerRegion(salerId));
//        List<QrCode> qrcodes = MybatisUtil.call(QrCodeMapper.class, codeMapper -> codeMapper.getQrCodeByUnitId(region));

    }
}
