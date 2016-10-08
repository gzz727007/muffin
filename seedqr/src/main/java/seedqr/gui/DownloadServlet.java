package seedqr.gui;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import javax.annotation.security.RolesAllowed;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

@RolesAllowed("user")
public class DownloadServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String file = req.getParameter("file");
        resp.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        resp.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename*=UTF-8''" + URLEncoder.encode(file, "UTF-8"));
        Files.copy(QrCodeRequestService.SEED_QR_DIR.resolve(file), resp.getOutputStream());
    }
}
