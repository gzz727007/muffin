package seedqr.gui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.zip.Adler32;
import java.util.zip.Checksum;
import javax.annotation.PostConstruct;
import javax.annotation.security.RolesAllowed;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import seedqr.mapper.QrCodeMapper;
import seedqr.mapper.SeqMapper;
import seedqr.model.QrCode;
import seedqr.model.User;
import seedqr.util.MybatisUtil;

@Named @ViewScoped @RolesAllowed("user")
public class QrGenerator implements Serializable {
    @Inject
    private SessionData sessionData;
    private User user;
    @Size(min = 1, max = 50, message = "品种名称不能为空且不超过 50 个字符。")
    private String seedName;
    @Size(min = 1, max = 50, message = "生产经营者名称不能为空且不超过 50 个字符。")
    private String manufacturer;
    @Min(value = 1, message = "数量必须在 1 到 10,000 之间。")
    @Max(value = 10000, message = "数量必须在 1 到 10,000 之间。")
    private int amount;

    @PostConstruct
    private void init() {
        user = sessionData.getUser();
        manufacturer = user.getCompanyName();
        amount = 1000;
    }

    public String getSeedName() {
        return seedName;
    }

    public void setSeedName(String seedName) {
        this.seedName = seedName;
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

    public void generateQrCodes() throws Exception {
        List<QrCode> qrCodes = new ArrayList<>(amount);
        //int sequence = MybatisUtil.getMapper(SeqMapper.class).nextVal(user.getCompanyCode(), amount);
        int sequence = MybatisUtil.call(SeqMapper.class, seqMapper -> seqMapper.nextVal(user.getCompanyCode(), amount));
        Random random = ThreadLocalRandom.current();

        for (int i = 0; i < amount; i++) {
            QrCode qrCode = new QrCode();
            qrCode.setCompanyCode(user.getCompanyCode());
            qrCode.setCompanyName(user.getCompanyName());
            qrCode.setSeedId(0);
            qrCode.setSeedName(seedName);

            String unitCode = user.getCompanyCode() + String.format("%08d", sequence - i)
                    + "0" + String.format("%04d", random.nextInt(9999));
            Checksum checksum = new Adler32();
            checksum.update(unitCode.getBytes(), 0, unitCode.length());
            unitCode += String.format("%02d", checksum.getValue() % 100);

            qrCode.setUnitCode(Long.parseLong(unitCode));
            qrCode.setTrackingUrl("http://www.zgzzcx.com/s?id=" + unitCode);

            qrCodes.add(qrCode);
        }

        //MybatisUtil.getMapper(QrCodeMapper.class).insertQrCode(qrCodes);
        MybatisUtil.run(QrCodeMapper.class, qrCodeMapper -> qrCodeMapper.insertQrCode(qrCodes));
        sessionData.setQrCodes(qrCodes);
        sessionData.setManufacturer(manufacturer);

        FacesContext.getCurrentInstance().getExternalContext().redirect(
                "download?type=qrCodes&faces-redirect=true");
    }
}
