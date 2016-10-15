package seedqr.gui.admin;

import seedqr.gui.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.security.RolesAllowed;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import seedqr.mapper.QrCodeMapper;
import seedqr.mapper.SeedMapper;
import seedqr.model.QrCodeRequest;
import seedqr.model.User;
import seedqr.util.MybatisUtil;

@Named @ViewScoped @RolesAllowed("admin")
public class PackageCodeGenerator implements Serializable {
    @Inject
    private QrCodeRequestService qrCodeRequestService;
    @Inject
    private SessionData sessionData;
    private User user;
    @Min(value = 1, message = "数量必须在 1 到 50,000 之间。")
    @Max(value = 50000, message = "数量必须在 1 到 50,000 之间。")
    private int amount;
    private List<QrCodeRequest> qrCodeRequests;

    @PostConstruct
    private void init() {
        user = sessionData.getUser();
        MybatisUtil.run(QrCodeMapper.class, qrCodeMapper -> {
            qrCodeRequests = new LinkedList<>(qrCodeMapper.getAllRequest(user.getId()));
        });
        amount = 1000;
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

    public void generateQrCodes() throws Exception {
        QrCodeRequest qrCodeRequest = new QrCodeRequest();
        qrCodeRequest.setUserId(user.getId());
        qrCodeRequest.setSeedId(0);
        qrCodeRequest.setSeedName("");
        qrCodeRequest.setAmount(amount);
        qrCodeRequest.setCompanyName("星楚物流");
        Date date = new Date();
        qrCodeRequest.setCreateTime(date);
        qrCodeRequest.setProgress(0);
        qrCodeRequest.setFileName("外袋码" + "-"
                + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(date) + ".zip");

        MybatisUtil.run(QrCodeMapper.class,
                qrCodeMapper -> qrCodeMapper.addQrCodeRequest(qrCodeRequest));
        qrCodeRequests.add(0, qrCodeRequest);

        qrCodeRequestService.generatePackQrCodes(amount, qrCodeRequest);
    }

    public void downloadQrCodeZip(String fil) {
        
    }
}
