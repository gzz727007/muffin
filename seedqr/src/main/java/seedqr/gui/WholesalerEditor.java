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
import javax.faces.event.ValueChangeEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.Valid;
import seedqr.mapper.CompanyMapper;
import seedqr.mapper.RegionMapper;
import seedqr.model.Company;
import seedqr.model.Region;
import seedqr.util.MybatisUtil;

/**
 *
 * @author muffin
 */
@Named
@ViewScoped
public class WholesalerEditor implements Serializable {

    @Inject
    private SessionData sessionData;
    private int userId;
    private int companyId;

    private List<Company> salers;

    private List<Region> allRegion;

    private List<Region> provinces = new ArrayList<>();

    private List<Region> citys = new ArrayList<>();

    private List<Region> district = new ArrayList<>();
    
    private int selectProvinceId;
    
    private int selectCityId;

    private int selectDistId;
    
    private Company selectedSaler;
    
    @Valid
    private Company saler = new Company();

    @PostConstruct
    private void init() {
        userId = sessionData.getUserId();
        companyId = sessionData.getCompanyId();
        salers = MybatisUtil.call(CompanyMapper.class,
                companyMapper -> companyMapper.getWholesalers(userId));
        allRegion = MybatisUtil.call(RegionMapper.class,
                regionMapper -> regionMapper.getAllRegion());
        //salers = MybatisUtil.getMapper(UserMapper.class).getUsersByParent(userId);
        //allRegion = MybatisUtil.getMapper(RegionMapper.class).getAllRegion();
        for (Region region : allRegion) {
            if (region.getLevel() == 1) {
                provinces.add(region);
            }
        }
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
            if (region.getLevel() == 2 && region.getParentId() == provinceId) {
                citys.add(region);
            }
        }
    }

    private void resetDistrict(int cityId) {
        district.clear();
        for (Region region : allRegion) {
            if (region.getLevel() == 3 && region.getParentId() == cityId) {
                district.add(region);
            }
        }
        if (district.size()>0) {
            selectDistId = district.get(0).getId();
        }
    }

    public void addWholesaler() {
        if (saler!=null) {
            System.out.println("user!=null:");
//            saler.setUserName(System.currentTimeMillis()+"");
            System.out.println("selectDistId:" + selectDistId);
            if (selectDistId > 0 ){
                saler.setParentId(companyId);
                saler.setUserId(userId);
//                saler.setRegionId(selectDistId);
                saler.setType(2);
//                saler.setUrole("saler");
//                saler.setPassword("12345678");
                MybatisUtil.run(CompanyMapper.class,
                        companyMapper -> companyMapper.addCompany(saler));
                //MybatisUtil.getMapper(UserMapper.class).addResaleUser(user);
            }
        }
        salers = MybatisUtil.call(CompanyMapper.class,
                companyMapper -> companyMapper.getWholesalers(userId));
        //salers = MybatisUtil.getMapper(UserMapper.class).getUsersByParent(userId);
    }
    
    public void modifyWholesaler() {
//        MybatisUtil.run(UserMapper.class, userMapper -> {
//            userMapper.updateUser(selectedSaler);
//            salers = userMapper.getUsersByParent(userId);
//        });
    }
    
    public void deleteWholesaler() {
        MybatisUtil.run(CompanyMapper.class, companyMapper -> {
            companyMapper.deleteCompany(selectedSaler.getId());
            salers = companyMapper.getWholesalers(userId);
        });
    }

    public Company getSaler() {
        return saler;
    }

    public void setSaler(Company saler) {
        this.saler = saler;
    }

    public List<Company> getSalers() {
        return salers;
    }

    public List<Region> getProvinces() {
        return provinces;
    }

    public List<Region> getCitys() {
        return citys;
    }

    public List<Region> getDistrict() {
        return district;
    }

    public Company getSelectedSaler() {
        System.out.println("getSelectedUser:" + selectedSaler);
        return selectedSaler;
    }

    public void setSelectedSaler(Company selectedSaler) {
        System.out.println("setSelectedUser:" + selectedSaler);
        this.selectedSaler = selectedSaler;
    }

    public void provinceChange(ValueChangeEvent event) {
        int provinceId = Integer.valueOf(event.getNewValue().toString());
        resetCity(provinceId);
        if (citys.size() > 0) {
            resetDistrict(citys.get(0).getId());
        }
    }

    public void cityChange(ValueChangeEvent event) {
        int cityId = Integer.valueOf(event.getNewValue().toString());
        resetDistrict(cityId);
    }

    public void districtChange(ValueChangeEvent event) {
        int districtId = Integer.valueOf(event.getNewValue().toString());
    }
}
