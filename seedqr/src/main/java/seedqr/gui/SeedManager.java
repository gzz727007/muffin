package seedqr.gui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.Valid;
import seedqr.mapper.SeedMapper;
import seedqr.model.Seed;
import seedqr.model.SeedConfig;
import seedqr.util.MybatisUtil;

@Named @ViewScoped
public class SeedManager implements Serializable {
    private static final Map<String, String> BASIC_SEED_CONFIG_PARA_NAMES = new LinkedHashMap<>();
    static {
        BASIC_SEED_CONFIG_PARA_NAMES.put("批次编码", "在此输入种子按批次的编码等。");
        BASIC_SEED_CONFIG_PARA_NAMES.put("种子亲本", "在此输入父本名称、母本名称等。");
        BASIC_SEED_CONFIG_PARA_NAMES.put("产地", "在此输入生产基地名称、基地负责人、代生产公司名称等。");
        BASIC_SEED_CONFIG_PARA_NAMES.put("生产时间", "在此输入年份、父母本播种时间、收种时间等。");
        BASIC_SEED_CONFIG_PARA_NAMES.put("检疫", "在此输入产地检疫证号、调运检疫证号等。");
        BASIC_SEED_CONFIG_PARA_NAMES.put("质量", "在此输入样品编号、代表数量、水份、净度、芽率、纯度、检验时间、检验员等。");
        BASIC_SEED_CONFIG_PARA_NAMES.put("是否渗混", "在此输入是或否。");
        BASIC_SEED_CONFIG_PARA_NAMES.put("包装加工", "在此输入包装仓库、加工班组等。");
        BASIC_SEED_CONFIG_PARA_NAMES.put("种衣剂", "在此输入种衣剂生产公司、种衣剂名称、种衣剂成分等。");
        BASIC_SEED_CONFIG_PARA_NAMES.put("生产公司", "在此输入公司名称、生产许可证号等。");
        BASIC_SEED_CONFIG_PARA_NAMES.put("经营公司", "在此输入公司名称、生产、经营许可证号等。");
        BASIC_SEED_CONFIG_PARA_NAMES.put("品种名称", "在此输入审定名称、审定编号等。");
        BASIC_SEED_CONFIG_PARA_NAMES.put("进口商", "在此输入地址、负责人、联系电话等。");
    }

    @Inject
    private SessionData sessionData;
    private int companyId;
    @Valid
    private Seed newSeed;
    private List<Seed> seeds;
    private Seed selectedSeed;
    @Valid
    private SeedConfig newSeedConfig;
    private List<SeedConfig> seedConfigs;
    
    private SeedConfig selectSeedConfig;

    @PostConstruct
    private void init() {
        companyId = sessionData.getCompanyId();
        seeds = new LinkedList<>(MybatisUtil.call(SeedMapper.class,
                seedMapper -> seedMapper.getAllSeeds(companyId)));
        resetNewSeed();
        resetNewSeedConfig();
    }

    public Seed getNewSeed() {
        return newSeed;
    }

    public List<Seed> getSeeds() {
        return seeds;
    }

    public Seed getSelectedSeed() {
        return selectedSeed;
    }

    public void setSelectedSeed(Seed selectedSeed) {
        this.selectedSeed = selectedSeed;
        seedConfigs = new ArrayList<>(MybatisUtil.call(SeedMapper.class,
                seedMapper -> seedMapper.getAllSeedConfigBySeedId(selectedSeed.getId())));
    }

    public SeedConfig getNewSeedConfig() {
        return newSeedConfig;
    }

    public Set<String> getStandardSeedConfigParaNames() {
        return BASIC_SEED_CONFIG_PARA_NAMES.keySet();
    }

    public String getSeedConfigHint() {
        return BASIC_SEED_CONFIG_PARA_NAMES.get(newSeedConfig.getParaName());
    }

    public List<SeedConfig> getSeedConfigs() {
        return seedConfigs;
    }

    public SeedConfig getSelectSeedConfig() {
        return selectSeedConfig;
    }

    public void setSelectSeedConfig(SeedConfig selectSeedConfig) {
        this.selectSeedConfig = selectSeedConfig;
    }
    

    public void addSeed() {
        MybatisUtil.run(SeedMapper.class, seedMapper -> seedMapper.addSeed(newSeed));
        seeds.add(0, newSeed);
        resetNewSeed();
    }

    public void addSeedConfig() {
        newSeedConfig.setSeedId(selectedSeed.getId());
        newSeedConfig.setOrderIndex(seedConfigs.size() + 1);
        seedConfigs.add(newSeedConfig);
        resetNewSeedConfig();
    }

    public void removeSeedConfig() {
        seedConfigs.remove(selectSeedConfig);
    }
    
    public void saveSeedConfigs() {
        MybatisUtil.run(SeedMapper.class, seedMapper -> {
            seedMapper.deleteSeedConfigBySeedId(selectedSeed.getId());
            if (!seedConfigs.isEmpty()) {
                seedMapper.insertSeedConfig(seedConfigs);
            }
        });
        
        FacesContext facesContext = FacesContext.getCurrentInstance();
            facesContext.addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_INFO, "种子属性修改成功！", null));
    }

    private void resetNewSeed() {
        newSeed = new Seed();
        newSeed.setUserId(companyId);
        newSeed.setCompanyId(companyId);
    }

    private void resetNewSeedConfig() {
        newSeedConfig = new SeedConfig();
        newSeedConfig.setType(1);
        newSeedConfig.setParaName("批次编码");
    }
}
