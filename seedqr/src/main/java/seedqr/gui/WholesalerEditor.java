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
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.Valid;
import org.primefaces.component.selectonemenu.SelectOneMenu;
import seedqr.mapper.RegionMapper;
import seedqr.mapper.UserMapper;
import seedqr.model.Region;
import seedqr.model.User;
import seedqr.util.MybatisUtil;

/**
 *
 * @author muffin
 */
@Named
@ViewScoped
@RolesAllowed("user")
public class WholesalerEditor implements Serializable {

    @Inject
    private SessionData sessionData;
    private int userId;

    private List<User> salers;

    private List<Region> allRegion;

    private List<Region> provinces = new ArrayList<>();

    private List<Region> citys = new ArrayList<>();

    private List<Region> district = new ArrayList<>();
    
    private int selectProvinceId;
    
    private int selectCityId;

    private int selectDistId;
    
    @Valid
    private User user = new User();

    @PostConstruct
    private void init() {
        userId = sessionData.getUser().getId();
        salers = MybatisUtil.getMapper(UserMapper.class).getUsersByParent(userId);
        allRegion = MybatisUtil.getMapper(RegionMapper.class).getAllRegion();
        for (Region region : allRegion) {
            if (region.getLevel() == 1) {
                provinces.add(region);
            }
        }
        System.out.println("init:" + provinces.size() );
        if (provinces.size() > 0) {
            resetCity(provinces.get(0).getId());
        }
        if (citys.size() > 0) {
            resetDistrict(citys.get(0).getId());
        }
    }

    private void resetCity(int provinceId) {
        citys.clear();
        for (Region region : allRegion) {
            System.out.println("Region:" + region.toString() );
            System.out.println("provinceId:" + provinceId );
            if (region.getLevel() == 2 && region.getParentId() == provinceId) {
                System.out.println("!!!!!!!!!!!provinceId:" + provinceId);
                citys.add(region);
            }
        }
    }

    private void resetDistrict(int cityId) {
        district.clear();
        for (Region region : allRegion) {
            if (region.getLevel() == 3 && region.getParentId() == cityId) {
                System.out.println("provinceId:" + cityId);
                district.add(region);
            }
        }
        if (district.size()>0) {
            selectDistId = district.get(0).getId();
        }
    }

    public void addWholesaler() {
        if (user!=null) {
            System.out.println("user!=null:");
            user.setUserName(user.getHandphone());
            System.out.println("selectDistId:" + selectDistId);
            if (selectDistId > 0 ){
                user.setParentId(userId);
                user.setRegionId(selectDistId);
                user.setUrole("saler");
                user.setPassword("12345678");
                MybatisUtil.getMapper(UserMapper.class).addResaleUser(user);
            }
        }
        salers = MybatisUtil.getMapper(UserMapper.class).getUsersByParent(userId);
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

    public void provinceChange(ValueChangeEvent event) {
        System.out.println("provinceChange:" + event.getNewValue());
        int provinceId = Integer.valueOf(event.getNewValue().toString());
        System.out.println("provinceId:" + provinceId);
        resetCity(provinceId);
        if (citys.size() > 0) {
            resetDistrict(citys.get(0).getId());
        }
    }

    public void cityChange(ValueChangeEvent event) {
        int cityId = Integer.valueOf(event.getNewValue().toString());
        System.out.println("cityId:" + cityId);
        resetDistrict(cityId);
    }

    public void districtChange(ValueChangeEvent event) {
        int districtId = Integer.valueOf(event.getNewValue().toString());
        System.out.println("districtId:" + districtId);
    }
}
