/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seedqr.gui;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.validation.constraints.Size;
import seedqr.mapper.QrCodeMapper;
import seedqr.mapper.SaleMapper;
import seedqr.mapper.SeedMapper;
import seedqr.model.QrCode;
import seedqr.model.SaleInfo;
import seedqr.model.SeedConfig;
import seedqr.util.MybatisUtil;

/**
 *
 * @author muffin
 */
@Named
@ViewScoped
public class QueryQrCode implements Serializable {

    @Size(min = 1, message = "单元识别代码不能为空")
    private String id;

    private boolean isExisting = false;
    private boolean isUnit = false;
    private List<QrCode> codeList;
    private List<SeedConfig> seedConfigs;
    private List<SaleInfo> saleInfoList;

    @PostConstruct
    private void init() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        id = externalContext.getRequestParameterMap().get("id");

    }

    public void query() throws Exception {
        if (id != null && id.length() == 19) {
            if (id.startsWith("1000")) {
                
            } else {
                codeList = MybatisUtil.getMapper(QrCodeMapper.class).getQrCodeByUnitId(id);
                if (codeList!= null && codeList.size() > 0) {
                    isExisting = true;
                    isUnit = true;
                    seedConfigs = MybatisUtil.getMapper(SeedMapper.class).getAllSeedConfigBySeedId(codeList.get(0).getSeedId());
                    saleInfoList = MybatisUtil.getMapper(SaleMapper.class).getSaleInfoByType(codeList.get(0).getId(), 1);
                }
            }
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isExisting() {
        return isExisting;
    }

    public void setExisting(boolean isExisting) {
        this.isExisting = isExisting;
    }

    public boolean isUnit() {
        return isUnit;
    }

    public List<QrCode> getCodeList() {
        return codeList;
    }

    public void setCodeList(List<QrCode> codeList) {
        this.codeList = codeList;
    }

    public List<SeedConfig> getSeedConfigs() {
        return seedConfigs;
    }

    public void setSeedConfigs(List<SeedConfig> seedConfigs) {
        this.seedConfigs = seedConfigs;
    }

    public List<SaleInfo> getSaleInfoList() {
        return saleInfoList;
    }

    public void setSaleInfoList(List<SaleInfo> saleInfoList) {
        this.saleInfoList = saleInfoList;
    }
    
}
