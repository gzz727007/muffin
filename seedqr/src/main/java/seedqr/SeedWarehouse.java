package seedqr;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;
import java.util.zip.Adler32;
import java.util.zip.Checksum;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.annotation.PostConstruct;
import javax.faces.FacesException;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import net.glxn.qrgen.javase.QRCode;

@Named @ViewScoped
public class SeedWarehouse implements Serializable {
    private static final Checksum CHECKSUM = new Adler32();
    private static final String TRACE_URL = "http://www.zgzzcx.com/s?id=";

    @Inject
    private DataService dataService;

    @Size(min = 1, message = "种子名称不能为空。")
    private String seedName;
    @NotNull
    private Manufacturer manufacturer;
    @Size(min = 1)
    private String newManufacturerName;
    @Min(value = 1)
    @Max(value = 999999)
    private int packAmount;

    @PostConstruct
    private void init() {
        manufacturer = dataService.getManufacturer(1);
        packAmount = 1000;
    }

    public String getSeedName() {
        return seedName;
    }

    public void setSeedName(String seedName) {
        this.seedName = seedName;
    }

    public Collection<Manufacturer> getManufacturers() {
        return dataService.getManufacturers();
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getNewManufacturerName() {
        return newManufacturerName;
    }

    public void setNewManufacturerName(String newManufacturerName) {
        this.newManufacturerName = newManufacturerName;
    }

    public int getPackAmount() {
        return packAmount;
    }

    public void setPackAmount(int packAmount) {
        this.packAmount = packAmount;
    }

    public void addManufacturer() {
        manufacturer = dataService.addManufacturer(newManufacturerName);
    }

    public void generateQrCodes() throws IOException {
        String prefix = String.format("%04d", manufacturer.getId())
                + LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
        StringBuilder qrData = new StringBuilder();

        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        externalContext.setResponseContentType(MediaType.APPLICATION_OCTET_STREAM);
        externalContext.setResponseHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"qrcodes.zip\"");

        ZipOutputStream out = new ZipOutputStream(
                externalContext.getResponseOutputStream());

        int[] index = {1};
        ThreadLocalRandom.current().ints(packAmount, 0, 999999).forEach(random -> {
            try {
                String serial = prefix + String.format("%06d", random);
                CHECKSUM.update(serial.getBytes(), 0, serial.length());
                serial += String.format("%03d", CHECKSUM.getValue() % 1000);

                String qrDatum = seedName + "\n" + manufacturer.getName()
                        + "\n" + serial + "\n" + TRACE_URL + serial;
                qrData.append("\n\n").append(qrDatum);

                out.putNextEntry(new ZipEntry(index[0] + ".png"));
                QRCode.from(qrDatum).writeTo(out);
                index[0]++;
            } catch (IOException ex) {
                throw new FacesException(ex);
            } finally {
                CHECKSUM.reset();
            }
        });

        out.putNextEntry(new ZipEntry("二维码数据.txt"));
        out.write(qrData.substring(2).getBytes());

        out.finish();
        facesContext.responseComplete();
    }
}
