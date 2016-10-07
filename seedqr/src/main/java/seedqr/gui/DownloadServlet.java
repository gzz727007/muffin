package seedqr.gui;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import net.glxn.qrgen.javase.QRCode;
import seedqr.model.QrCode;

@RolesAllowed("user")
public class DownloadServlet extends HttpServlet {
    @Inject
    private SessionData sessionData;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        switch (req.getParameter("type")) {
            case "qrCodes":
                downloadQrCodes(resp);
                return;
            default:
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    public void downloadQrCodes(HttpServletResponse resp) throws IOException {
        resp.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        resp.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename*=UTF-8''" + URLEncoder.encode("种子二维码.zip", "UTF-8"));

        ZipOutputStream out = new ZipOutputStream(resp.getOutputStream());
        List<QrCode> qrCodes = sessionData.getQrCodes();

        for (QrCode qrCode : qrCodes) {
            out.putNextEntry(new ZipEntry(qrCode.getUnitCode() + ".png"));
            QRCode.from(formatQrCode(qrCode)).withCharset("UTF-8").writeTo(out);
        }

        out.putNextEntry(new ZipEntry("二维码数据.txt"));
        out.write(qrCodes.stream().map(this::formatQrCode)
                .collect(Collectors.joining("\r\n\r\n")).getBytes());

        out.finish();
        sessionData.setQrCodes(null);
        sessionData.setManufacturer(null);
    }

    private String formatQrCode(QrCode qrCode) {
        return "品种名称：" + qrCode.getSeedName() + "\r\n生产经营者名称："
                + sessionData.getManufacturer() + "\r\n单元识别代码："
                + qrCode.getUnitCode() + "\r\n追溯网址：" + qrCode.getTrackingUrl();
    }
}
