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
public class Seed {
    private int id;
    private int userId;
    private String seedName;
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
