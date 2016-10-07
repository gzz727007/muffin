/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seedqr.model;

import java.util.Date;

/**
 *
 * @author muffin
 */
public class SaleInfo {
    private int id;
    private int qrCodeId;
    private int wholesalerId;
    private String message;
    private int type;
    private Date saleTime;
    
    public SaleInfo(){
        
    }

    public SaleInfo(int qrCodeId, int wholesalerId, String message, int type) {
        this.qrCodeId = qrCodeId;
        this.wholesalerId = wholesalerId;
        this.message = message;
        this.type = type;
    }

    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQrCodeId() {
        return qrCodeId;
    }

    public void setQrCodeId(int qrCodeId) {
        this.qrCodeId = qrCodeId;
    }

    public int getWholesalerId() {
        return wholesalerId;
    }

    public void setWholesalerId(int wholesalerId) {
        this.wholesalerId = wholesalerId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Date getSaleTime() {
        return saleTime;
    }

    public void setSaleTime(Date saleTime) {
        this.saleTime = saleTime;
    }

    @Override
    public String toString() {
        return "SaleInfo{" + "id=" + id + ", qrCodeId=" + qrCodeId + ", wholesalerId=" + wholesalerId + ", message=" + message + ", type=" + type + ", saleTime=" + saleTime + '}';
    }
    
}
