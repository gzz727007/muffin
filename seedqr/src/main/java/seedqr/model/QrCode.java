/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seedqr.model;

import java.sql.Date;

/**
 *
 * @author muffin
 */
public class QrCode {
    private int id;
    private long unitCode;
    private String companyCode;
    private String extUnitCode;
    private String seedName;
    private String companyName;
    private String trackingUrl;
    private int seedId;
    private int status;
    private Date createTime;
    private Date bindTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(long unitCode) {
        this.unitCode = unitCode;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getExtUnitCode() {
        return extUnitCode;
    }

    public void setExtUnitCode(String extUnitCode) {
        this.extUnitCode = extUnitCode;
    }

    public String getSeedName() {
        return seedName;
    }

    public void setSeedName(String seedName) {
        this.seedName = seedName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getTrackingUrl() {
        return trackingUrl;
    }

    public void setTrackingUrl(String trackingUrl) {
        this.trackingUrl = trackingUrl;
    }

    public int getSeedId() {
        return seedId;
    }

    public void setSeedId(int seedId) {
        this.seedId = seedId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getBindTime() {
        return bindTime;
    }

    public void setBindTime(Date bindTime) {
        this.bindTime = bindTime;
    }

    @Override
    public String toString() {
        return "QrCode{" + "id=" + id + ", unitCode=" + unitCode + ", companyCode=" + companyCode + ", extUnitCode=" + extUnitCode + ", seedName=" + seedName + ", companyName=" + companyName + ", trackingUrl=" + trackingUrl + ", seedId=" + seedId + ", status=" + status + ", createTime=" + createTime + ", bindTime=" + bindTime + '}';
    }
}
