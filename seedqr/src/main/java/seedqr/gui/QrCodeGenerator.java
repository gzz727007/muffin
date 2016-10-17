package seedqr.gui;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import seedqr.mapper.QrCodeMapper;
import seedqr.mapper.SeedMapper;
import seedqr.model.QrCodeRequest;
import seedqr.model.Seed;
import seedqr.model.User;
import seedqr.util.MybatisUtil;

@Named @ViewScoped
public class QrCodeGenerator implements Serializable {
    @Inject
    private QrCodeRequestService qrCodeRequestService;
    @Inject
    private SessionData sessionData;
    private User user;
    private List<Seed> seeds;
    private int seedId;
    @Valid
    private Seed newSeed;
    @Size(min = 1, max = 50, message = "生产经营者名称不能为空且不超过 50 个字符。")
    private String manufacturer;
    @Min(value = 1, message = "数量必须在 1 到 50,000 之间。")
    @Max(value = 50000, message = "数量必须在 1 到 50,000 之间。")
    private int amount;
    private List<QrCodeRequest> qrCodeRequests;

    @PostConstruct
    private void init() {
        user = sessionData.getUser();
        MybatisUtil.run(SeedMapper.class, QrCodeMapper.class, (seedMapper, qrCodeMapper) -> {
            seeds = new LinkedList<>(seedMapper.getAllSeeds(user.getId()));
            qrCodeRequests = new LinkedList<>(qrCodeMapper.getAllRequest(user.getId()));
        });
        if (!seeds.isEmpty()) {
            seedId = seeds.get(0).getId();
        }
        resetNewSeed();
        manufacturer = sessionData.getCompany().getName();
        amount = 1000;
    }

    public List<Seed> getSeeds() {
        return seeds;
    }

    public int getSeedId() {
        return seedId;
    }

    public void setSeedId(int seedId) {
        this.seedId = seedId;
    }

    public Seed getNewSeed() {
        return newSeed;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public List<QrCodeRequest> getQrCodeRequests() {
        return qrCodeRequests;
    }

    public void addSeed() {
        MybatisUtil.run(SeedMapper.class, seedMapper -> seedMapper.addSeed(newSeed));
        seeds.add(0, newSeed);
        seedId = newSeed.getId();
        resetNewSeed();
    }

    public void generateQrCodes() throws Exception {
        Seed seed = seeds.stream().filter(s -> s.getId() == seedId).findFirst().get();

        QrCodeRequest qrCodeRequest = new QrCodeRequest();
        qrCodeRequest.setUserId(user.getId());
        qrCodeRequest.setSeedId(seed.getId());
        qrCodeRequest.setSeedName(seed.getSeedName());
        qrCodeRequest.setAmount(amount);
        qrCodeRequest.setCompanyName(manufacturer);
        Date date = new Date();
        qrCodeRequest.setCreateTime(date);
        qrCodeRequest.setProgress(0);
        qrCodeRequest.setFileName(seed.getSeedName() + "-"
                + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(date) + ".zip");

        MybatisUtil.run(QrCodeMapper.class,
                qrCodeMapper -> qrCodeMapper.addQrCodeRequest(qrCodeRequest));
        qrCodeRequests.add(0, qrCodeRequest);

        qrCodeRequestService.generateQrCodes(sessionData.getCompany(),
                seed, manufacturer, amount, qrCodeRequest);
    }

    public void downloadQrCodeZip(String fil) {
        
    }

    private void resetNewSeed() {
        newSeed = new Seed();
        newSeed.setUserId(user.getId());
    }
}
