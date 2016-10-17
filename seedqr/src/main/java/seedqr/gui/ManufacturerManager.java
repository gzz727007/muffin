package seedqr.gui;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.Valid;
import seedqr.mapper.CompanyMapper;
import seedqr.mapper.RegionMapper;
import seedqr.model.Company;
import seedqr.model.Region;
import seedqr.util.MybatisUtil;

@Named @ViewScoped
public class ManufacturerManager implements Serializable {
    @Inject
    private SessionData sessionData;
    private List<Company> manufacturers;
    @Valid
    private Company manufacturer;
    private List<Region> regions;
    private List<Region> provinces;
    private List<Region> cities;
    private List<Region> districts;
    private int province;
    private int city;

    @PostConstruct
    private void init() {
        MybatisUtil.run(CompanyMapper.class, RegionMapper.class,
                (compamyMapper, regionMapper) -> {
            manufacturers = new LinkedList<>(compamyMapper.getManufacturers());
            regions = regionMapper.getAllRegion();
        });
        resetManufacturer();
        provinces = regions.stream().filter(region -> region.getLevel() == 1)
                .collect(Collectors.toList());
        province = provinces.get(0).getId();
        updateCities();
    }

    public List<Company> getManufacturers() {
        return manufacturers;
    }

    public Company getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(Company manufacturer) {
        this.manufacturer = manufacturer;
    }

    public List<Region> getProvinces() {
        return provinces;
    }

    public List<Region> getCities() {
        return cities;
    }

    public List<Region> getDistricts() {
        return districts;
    }

    public int getProvince() {
        return province;
    }

    public void setProvince(int province) {
        this.province = province;
    }

    public int getCity() {
        return city;
    }

    public void setCity(int city) {
        this.city = city;
    }

    public void updateCities() {
        cities = regions.stream().filter(region -> region.getParentId() == province)
                .collect(Collectors.toList());
        city = cities.get(0).getId();
        updateDistricts();
    }

    public void updateDistricts() {
        districts = regions.stream().filter(region -> region.getParentId() == city)
                .collect(Collectors.toList());
        manufacturer.setRegionId(districts.get(0).getId());
    }

    public void addManufacturer() {
        MybatisUtil.run(CompanyMapper.class, companyMapper -> {
            companyMapper.addCompany(manufacturer);
        });
        manufacturers.add(0, manufacturer);
        resetManufacturer();
    }

    private void resetManufacturer() {
        manufacturer = new Company();
        manufacturer.setType(1);
        manufacturer.setParentId(sessionData.getUserId());
    }
}
