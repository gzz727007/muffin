package seedqr.gui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.annotation.security.RolesAllowed;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import seedqr.mapper.QrCodeMapper;
import seedqr.util.MybatisUtil;

@Named @ViewScoped @RolesAllowed("user")
public class PackScanner implements Serializable {
    @Min(value = 20, message = "小包数量必须在 20 到 100 之间。")
    @Max(value = 100, message = "小包数量必须在 20 到 100 之间。")
    private int amount;
    @Size(min = 1, max = 20, message = "条码不能为空且不超过 20 个字符。")
    private String packCode;
    private String bulkPackCode;
    private List<String> smallPackCodes;
    private int seedId;

    @PostConstruct
    private void init() {
        amount = 50;
        smallPackCodes = new ArrayList<>();
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getPackCode() {
        return packCode;
    }

    public void setPackCode(String packCode) {
        this.packCode = packCode;
    }

    public String getBulkPackCode() {
        return bulkPackCode;
    }

    public List<String> getSmallPackCodes() {
        return smallPackCodes;
    }

    public int getSeedId() {
        return seedId;
    }

    public void setSeedId(int seedId) {
        this.seedId = seedId;
    }

    
    
    public void addPackCode() {
        if (packCode.startsWith("1000")) {
            bulkPackCode = packCode;
        } else {
            if(amount == smallPackCodes.size()) {
                FacesContext facesContext = FacesContext.getCurrentInstance();
                facesContext.addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_ERROR, "小包已经扫满。", null));
            } else {
                smallPackCodes.add(packCode);
            }
        }
        packCode = null;
    }

    public void bindCodes() {
        FacesContext facesContext = FacesContext.getCurrentInstance();

        if (bulkPackCode == null || bulkPackCode.isEmpty() || bulkPackCode.length() > 20) {
            facesContext.addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_ERROR, "没有扫描大包条码。", null));
            return;
        }

        if (smallPackCodes.size() != amount) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "设定的小包数量（" + amount + "）与实际扫描的数量（"
                    + smallPackCodes.size() + "）不一致。", null));
            return;
        }
        
        if (seedId ==0) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "请选择种子批次！", null));
            return;
        }
        // 检查大小和小包是否已经被绑定
        MybatisUtil.run(QrCodeMapper.class,
                qrCodeMapper -> {qrCodeMapper.addQrCodeMapping(bulkPackCode,
                        smallPackCodes.stream().collect(Collectors.joining(",")));
                qrCodeMapper.updateQrCodeSeedId(smallPackCodes.stream().collect(Collectors.joining(",")), seedId);});
        facesContext.addMessage(null, new FacesMessage(
                FacesMessage.SEVERITY_INFO, "绑定成功。", null));
        bulkPackCode = null;
        packCode = null;
        smallPackCodes.clear();
    }
}
