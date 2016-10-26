package seedqr.gui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import seedqr.mapper.QrCodeMapper;
import seedqr.util.CodeUtil;
import seedqr.util.MybatisUtil;

@Named @ViewScoped
public class PackScanner implements Serializable {
    @Min(value = 20, message = "小包数量必须在 20 到 200 之间。")
    @Max(value = 200, message = "小包数量必须在 20 到 200 之间。")
    private int amount;
    @Size(min = 1, max = 400, message = "条码不能为空且不超过 400 个字符。")
    private String packCode;
    private String bulkPackCode;
    private String smallPackCodesCsv;
    private List<String> smallPackCodes;
    private int seedId;
    
    private boolean isOurSystem = true;

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

    public String getSmallPackCodesCsv() {
        return smallPackCodesCsv;
    }

    public void setSmallPackCodesCsv(String smallPackCodesCsv) {
        this.smallPackCodesCsv = smallPackCodesCsv;
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
        String parsePackCode = packCode;
        packCode = "";
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (parsePackCode.startsWith("1000") && parsePackCode.length() == 19) {
            bulkPackCode = parsePackCode;
        } else {
            if(amount == smallPackCodes.size()) {
                facesContext.addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_ERROR, "小包已经扫满。", null));
            } else {
                if (!CodeUtil.isXingchuCode(parsePackCode)) {
                    isOurSystem = false;
                }
                System.out.println(parsePackCode);
                parsePackCode = CodeUtil.parseCode(parsePackCode);
                if (parsePackCode.startsWith("1000") && parsePackCode.length() == 19) {
                    if(bulkPackCode!=null && bulkPackCode.length() ==19) {
                        facesContext.addMessage(null, new FacesMessage(
                        FacesMessage.SEVERITY_ERROR, "已经扫描大码。", null));
                    }else {
                        bulkPackCode = parsePackCode;
                    }
                    return;
                }
                if (parsePackCode.equals("")) {
                    return;
                }
                if(smallPackCodes.contains(parsePackCode)) {
                    facesContext.addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_ERROR, "重复扫码。", null));
                } else {
                    smallPackCodes.add(parsePackCode);
                }
            }
        }
        packCode = null;
    }

    public void bindCodes() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        smallPackCodes = Arrays.asList(smallPackCodesCsv.split(","));

        
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
        
        for (String smallQrCode : smallPackCodes) {
            if(smallQrCode.length() != 19) {
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "编码【"+ smallQrCode +"】不是正规编码！", null));
                return;
            }
            try {
                Long.parseLong(smallQrCode);
            }catch(Exception e) {
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "编码【"+ smallQrCode +"】不是正规编码！", null));
                return;
            }
        }
        
        if (seedId ==0) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "请选择种子批次！", null));
            return;
        }
        List<Long> result = MybatisUtil.call(QrCodeMapper.class,
                qrCodeMapper -> 
                    qrCodeMapper.validateQrCodes(smallPackCodes.stream().collect(Collectors.joining(",")), seedId)
                );
        if(result.size() != smallPackCodes.size()) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "你输入的种子二位码不是这个品种的！", null));
            return;
        }
        
        // 检查大小和小包是否已经被绑定
        MybatisUtil.run(QrCodeMapper.class,
                qrCodeMapper -> {qrCodeMapper.addQrCodeMapping(bulkPackCode,
                        smallPackCodes.stream().collect(Collectors.joining(",")));
                if (isOurSystem) 
                    qrCodeMapper.updateQrCodeSeedId(smallPackCodes.stream().collect(Collectors.joining(",")), seedId);});
        facesContext.addMessage(null, new FacesMessage(
                FacesMessage.SEVERITY_INFO, "绑定成功。", null));
        bulkPackCode = null;
        packCode = null;
        smallPackCodes.clear();
    }
    
}
