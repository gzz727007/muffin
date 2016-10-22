/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seedqr.model;

import javax.validation.constraints.Size;

/**
 *
 * @author muffin
 */
public class Seed {
    private int id;
    private int userId;
    private int companyId;
    @Size(min = 1, max = 50, message = "种子名称不能为空且不超过 50 个字符。")
    private String seedName;
    @Size(min = 1, max = 50, message = "显示名称不能为空且不超过 50 个字符。")
    private String seedUiDisplay;
    
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getSeedName() {
        return seedName;
    }

    public void setSeedName(String seedName) {
        this.seedName = seedName;
    }

    public String getSeedUiDisplay() {
        return seedUiDisplay;
    }

    public void setSeedUiDisplay(String seedUiDisplay) {
        this.seedUiDisplay = seedUiDisplay;
    }

    @Override
    public String toString() {
        return "Seed{" + "id=" + id + ", userId=" + userId + ", seedName=" + seedName + ", seedUiDisplay=" + seedUiDisplay + '}';
    }
    
}
