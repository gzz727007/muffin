package seedqr.gui;

import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Date;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
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
import seedqr.model.User;

@Named @ViewScoped @RolesAllowed("user")
public class QrGenerator implements Serializable {
    private static final String QR_DATUM
            = "品种名称：%s\r\n生产经营者名称：%s\r\n单元识别代码：%s\r\n追溯网址：%s";
    private static final String TRACKING_URL = "http://www.zgzzcx.com/s?id=";
    private static String DOWNLOAD_NAME;

    static {
        try {
            DOWNLOAD_NAME = "attachment; filename*=UTF-8''"
                    + URLEncoder.encode("种子二维码.zip", "UTF-8");
        } catch (UnsupportedEncodingException ex) {
        }
    }

    @Inject
    private SessionData sessionData;
    private User user;
    @Size(min = 1, message = "品种名称不能为空。")
    private String seedName;
    @Size(min = 1, message = "生产经营者名称不能为空。")
    private String manufacturer;
    @Min(value = 1)
    @Max(value = 99999999)
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

    public void generateQrCodes() throws IOException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        externalContext.setResponseContentType(MediaType.APPLICATION_OCTET_STREAM);
        externalContext.setResponseHeader(HttpHeaders.CONTENT_DISPOSITION, DOWNLOAD_NAME);

        ZipOutputStream out = new ZipOutputStream(
                externalContext.getResponseOutputStream());
        StringBuilder qrData = new StringBuilder();
        Random random = ThreadLocalRandom.current();

        for (int i = 0; i < amount; i++) {
            String serial = user.getCompanyCode() + "<sequence>" + "0"
                    + String.format("%04d", random.nextInt(9999));
            Checksum checksum = new Adler32();
            checksum.update(serial.getBytes(), 0, serial.length());
            serial += String.format("%02d", checksum.getValue() % 10000);

            String qrDatum = String.format(QR_DATUM, seedName,
                    manufacturer, serial, TRACKING_URL + serial);
            qrData.append("\r\n\r\n").append(qrDatum);

            out.putNextEntry(new ZipEntry(i + ".png"));
            QRCode.from(qrDatum).withCharset("UTF-8").writeTo(out);
        }

        out.putNextEntry(new ZipEntry("二维码数据.txt"));
        out.write(qrData.substring(4).getBytes());

        out.finish();
        facesContext.responseComplete();
    }
}
