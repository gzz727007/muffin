/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seedqr.model;

/**
 *
 * @author muffin
 */
public class SeedConfig {
    private int id;
    private int seedId;
    private String paraName;
    private String paraValue;
    private int type;
    private int orderIndex;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSeedId() {
        return seedId;
    }

    public void setSeedId(int seedId) {
        this.seedId = seedId;
    }

    public String getParaName() {
        return paraName;
    }

    public void setParaName(String paraName) {
        this.paraName = paraName;
    }

    public String getParaValue() {
        return paraValue;
    }

    public void setParaValue(String paraValue) {
        this.paraValue = paraValue;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }

    @Override
    public String toString() {
        return "SeedConfig{" + "id=" + id + ", seedId=" + seedId + ", paraName=" + paraName + ", paraValue=" + paraValue + ", type=" + type + ", orderIndex=" + orderIndex + '}';
    }
    
}
