package seedqr.gui;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.zip.Adler32;
import java.util.zip.Checksum;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import net.glxn.qrgen.javase.QRCode;
import seedqr.mapper.QrCodeMapper;
import seedqr.mapper.SeqMapper;
import seedqr.model.QrCode;
import seedqr.model.QrCodeRequest;
import seedqr.model.Seed;
import seedqr.model.User;
import seedqr.util.MybatisUtil;

@Stateless @TransactionManagement(TransactionManagementType.BEAN)
public class QrCodeRequestService {
    public static final Path SEED_QR_DIR = Paths.get("/data/seedqr");

    @Asynchronous
    public void generateQrCodes(User user, Seed seed,
            String manufacturer, int amount, QrCodeRequest qrCodeRequest) {
        int taskSize = amount * 5 + 1;
        int finishedSize = 0;

        List<QrCode> qrCodes = new ArrayList<>(amount);
        List<QrCode> curr = new ArrayList<>(500);
        int sequence = MybatisUtil.call(SeqMapper.class,
                seqMapper -> seqMapper.nextVal(user.getCompanyCode(), amount));
        Random random = ThreadLocalRandom.current();

        for (int i = 0; i < amount; i++) {
            QrCode qrCode = new QrCode();
            qrCode.setCompanyCode(user.getCompanyCode());
            qrCode.setCompanyName(user.getCompanyName());
            qrCode.setSeedId(0);
            qrCode.setSeedName(seed.getSeedName());

            String unitCode = user.getCompanyCode() + String.format("%08d", sequence - i)
                    + "0" + String.format("%04d", random.nextInt(9999));
            Checksum checksum = new Adler32();
            checksum.update(unitCode.getBytes(), 0, unitCode.length());
            unitCode += String.format("%02d", checksum.getValue() % 100);

            qrCode.setUnitCode(Long.parseLong(unitCode));
            qrCode.setTrackingUrl("http://www.zgzzcx.com/s?id=" + unitCode);

            qrCodes.add(qrCode);
            curr.add(qrCode);
            if (i != 0 && i % 1000 == 0) {
                finishedSize += 1000;
                updateProgress(qrCodeRequest.getId(),
                        finishedSize * 100 / taskSize);
            }
            if (i != 0 && i % 500 == 0) {
                MybatisUtil.run(QrCodeMapper.class, qrCodeMapper -> {
                    qrCodeMapper.insertQrCode(curr);
                });
                curr.clear();
            }
        }
        
        if (curr.size() > 0 ) {
            MybatisUtil.run(QrCodeMapper.class, qrCodeMapper -> {
                    qrCodeMapper.insertQrCode(curr);
                });
        }

        MybatisUtil.run(QrCodeMapper.class, qrCodeMapper -> {
            qrCodeMapper.updateRequestProgress(qrCodeRequest.getId(), 60);
        });

        try {
            Files.createDirectories(SEED_QR_DIR);
            try (ZipOutputStream out = new ZipOutputStream(Files.newOutputStream(
                    SEED_QR_DIR.resolve(qrCodeRequest.getFileName())))) {
                for (int i = 0; i < amount; i++) {
                    QrCode qrCode = qrCodes.get(i);
                    out.putNextEntry(new ZipEntry(qrCode.getUnitCode() + ".png"));
                    QRCode.from(formatQrCode(qrCode, manufacturer))
                            .withCharset("UTF-8").writeTo(out);

                    if (i != 0 && i % 1000 == 0) {
                        finishedSize += 1000;
                        updateProgress(qrCodeRequest.getId(),
                                finishedSize * 100 / taskSize);
                    }
                }

                out.putNextEntry(new ZipEntry("二维码数据.txt"));
                out.write(qrCodes.stream().map(qrCode -> formatQrCode(qrCode, manufacturer))
                        .collect(Collectors.joining("\r\n\r\n")).getBytes());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        updateProgress(qrCodeRequest.getId(), 100);
    }

    private void updateProgress(int requestId, int progress) {
        MybatisUtil.run(QrCodeMapper.class,
                qrCodeMapper -> qrCodeMapper.updateRequestProgress(requestId, progress));
    }

    private String formatQrCode(QrCode qrCode, String manufacturer) {
        return "品种名称：" + qrCode.getSeedName() + "\r\n生产经营者名称："
                + manufacturer + "\r\n单元识别代码：" + qrCode.getUnitCode()
                + "\r\n追溯网址：" + qrCode.getTrackingUrl();
    }
}
