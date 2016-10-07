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
    @Size(min = 1, max = 20, message = "大包码不能为空且不超过 20 个字符。")
    private String bulkPackCode;
    @Size(min = 1, max = 20, message = "小包码不能为空且不超过 20 个字符。")
    private String smallPackCode;
    private List<String> smallPackCodes;

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
        smallPackCode = null;
    }

    public void bindCodes() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (smallPackCodes.size() != amount) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "设定的小包数量（" + amount + "）与实际扫描的数量（"
                    + smallPackCodes.size() + "）不一致。", null));
            return;
        }

        MybatisUtil.getMapper(QrCodeMapper.class).addQrCodeMapping(bulkPackCode,
                smallPackCodes.stream().collect(Collectors.joining(",")));
        facesContext.addMessage(null, new FacesMessage(
                FacesMessage.SEVERITY_INFO, "绑定成功。", null));
        bulkPackCode = null;
        smallPackCode = null;
        smallPackCodes.clear();
    }
}
