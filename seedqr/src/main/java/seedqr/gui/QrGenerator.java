package seedqr.gui;

import java.io.Serializable;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.zip.Adler32;
import java.util.zip.Checksum;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.annotation.PostConstruct;
import javax.annotation.security.RolesAllowed;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import net.glxn.qrgen.javase.QRCode;
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
    @Size(min = 1, message = "品种名称不能为空。")
    private String seedName;
    @Size(min = 1, message = "生产经营者名称不能为空。")
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
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        externalContext.setResponseContentType(MediaType.APPLICATION_OCTET_STREAM);
        externalContext.setResponseHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename*=UTF-8''" + URLEncoder.encode("种子二维码.zip", "UTF-8"));

        List<QrCode> qrCodes = new ArrayList<>(amount);
        int sequence = MybatisUtil.getMapper(SeqMapper.class)
                .nextVal(user.getCompanyCode(), amount);
        Random random = ThreadLocalRandom.current();

        for (int i = 0; i < amount; i++) {
            QrCode qrCode = new QrCode();
            qrCode.setCompanyCode(user.getCompanyCode());
            qrCode.setCompanyName(user.getCompanyName());
            qrCode.setSeedId(0);
            qrCode.setSeedName(seedName);

            String unitCode = user.getCompanyCode() + String.format("%08d", sequence - 1)
                    + "0" + String.format("%04d", random.nextInt(9999));
            Checksum checksum = new Adler32();
            checksum.update(unitCode.getBytes(), 0, unitCode.length());
            unitCode += String.format("%02d", checksum.getValue() % 100);

            qrCode.setUnitCode(Long.parseLong(unitCode));
            qrCode.setTrackingUrl("http://www.zgzzcx.com/s?id=" + unitCode);

            qrCodes.add(qrCode);
        }

        MybatisUtil.getMapper(QrCodeMapper.class).insertQrCode(qrCodes);

        ZipOutputStream out = new ZipOutputStream(
                externalContext.getResponseOutputStream());
        for (QrCode qrCode : qrCodes) {
            out.putNextEntry(new ZipEntry(qrCode.getUnitCode() + ".png"));
            QRCode.from(formatQrCode(qrCode)).withCharset("UTF-8").writeTo(out);
        }

        out.putNextEntry(new ZipEntry("二维码数据.txt"));
        out.write(qrCodes.stream().map(this::formatQrCode)
                .collect(Collectors.joining("\r\n\r\n")).getBytes());

        out.finish();
        facesContext.responseComplete();
    }

    private String formatQrCode(QrCode qrCode) {
        return String.format("品种名称：%s\r\n生产经营者名称：%s\r\n单元识别代码：%s\r\n追溯网址：%s",
                seedName, manufacturer, qrCode.getUnitCode(), qrCode.getTrackingUrl());
    }
}
