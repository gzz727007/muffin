/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seedqr.gui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.security.RolesAllowed;
import javax.faces.event.ValueChangeEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.Valid;
import seedqr.mapper.RegionMapper;
import seedqr.mapper.UserMapper;
import seedqr.model.Region;
import seedqr.model.User;
import seedqr.util.MybatisUtil;

/**
 *
 * @author muffin
 */

@Named @ViewScoped @RolesAllowed("user")
public class WholesalerEditor implements Serializable{
    @Inject
    private SessionData sessionData;
    private int userId;
    
    private List<User> salers;
    
    private List<Region> allRegion;
    
    private List<Region> provinces = new ArrayList<>();
    
    private List<Region> citys = new ArrayList<>();
    
    private List<Region> district = new ArrayList<>();
    
    @Valid
    private User user = new User();
    
    @PostConstruct
    private void init() {
        userId = sessionData.getUser().getId();
        salers = MybatisUtil.getMapper(UserMapper.class).getUsersByParent(userId);
        allRegion = MybatisUtil.getMapper(RegionMapper.class).getAllRegion();
        for(Region region : allRegion) {
            if (region.getLevel() == 1) {
                provinces.add(region);
            }
        }
        
    }

    
    public void addWholesaler(){
        
    }
    
    
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<User> getSalers() {
        return salers;
    }

    public void setSalers(List<User> salers) {
        this.salers = salers;
    }

    public List<Region> getProvinces() {
        return provinces;
    }

    public void setProvinces(List<Region> provinces) {
        this.provinces = provinces;
    }

    public List<Region> getCitys() {
        return citys;
    }

    public void setCitys(List<Region> citys) {
        this.citys = citys;
    }

    public List<Region> getDistrict() {
        return district;
    }

    public void setDistrict(List<Region> district) {
        this.district = district;
    }
    
    public void provinceChange(ValueChangeEvent event) 
    {
        int  provinceId= (Integer) event.getNewValue();
        System.out.println("provinceId:" + provinceId);
    }
    
}
