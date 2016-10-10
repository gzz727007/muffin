package seedqr.gui;

import java.io.IOException;
import java.nio.file.Files;
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
import seedqr.model.User;
import seedqr.util.MybatisUtil;

@Stateless @TransactionManagement(TransactionManagementType.BEAN)
public class StockOutService {

    @Asynchronous
    public void doOut(User user,List<Long> longCodes,int salerId) {
        
    }
}
