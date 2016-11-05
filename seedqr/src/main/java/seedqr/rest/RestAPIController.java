/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seedqr.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import seedqr.gui.StockOutService;
import seedqr.mapper.CompanyMapper;
import seedqr.mapper.UserMapper;
import seedqr.model.Company;
import seedqr.model.RestResult;
import seedqr.model.User;
import seedqr.util.MybatisUtil;

/**
 *
 * @author muffin
 */
@Path("/api")
public class RestAPIController {

    @Context
    HttpServletResponse response;
    @Context
    HttpServletRequest request;
    @Inject
    private StockOutService service;

    @GET
    @Path("/user/login")
    @Produces(MediaType.APPLICATION_JSON)
    public RestResult login(@QueryParam("username") String userName, @QueryParam("password") String password) {
        HttpSession session = request.getSession();
        RestResult result = new RestResult();
        try {
            request.login(userName, password);
            User user = MybatisUtil.call(UserMapper.class,
                    userMapper -> userMapper.getUserByUserName(userName));
            Company company = MybatisUtil.call(CompanyMapper.class,
                companyMapper -> companyMapper.getCompany(user.getCompanyId()));
            if ("user".equals(user.getRole()) || "keeper".equals(user.getRole())) {
                session.setAttribute("user", user);
                result.setStatus(true);
                result.setData(company);
                result.setCode("0000");
                result.setMessage("登陆成功！");
            } else {
                session.invalidate();
                request.logout();
                result.setStatus(false);
                result.setCode("1002");
                result.setMessage("登陆失败！原因：权限不够！");
            }
        } catch (Exception ex) {
            result.setStatus(false);
            result.setCode("1001");
            result.setMessage("登陆失败！原因：" + ex.getMessage());
        }
        return result;
    }

    @GET
    @Path("/user/logout")
    @Produces(MediaType.APPLICATION_JSON)
    public RestResult logout() {
        HttpSession session = request.getSession();
        RestResult result = new RestResult();
        try {
            session.invalidate();
            request.logout();
            result.setStatus(true);
            result.setCode("0000");
            result.setMessage("注销成功！");
        } catch (Exception ex) {
            result.setStatus(false);
            result.setCode("1002");
            result.setMessage("注销失败！原因：" + ex.getMessage());
        }
        return result;
    }

    @GET
    @Path("/salers")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"user", "keeper"})
    public RestResult getAllSalers() {
        HttpSession session = request.getSession();
        Object userObj = session.getAttribute("user");
        RestResult result = new RestResult();
        if (userObj == null) {
            result.setStatus(false);
            result.setCode("2001");
            result.setMessage("用户没有登陆！");
        } else {
            User user = (User) userObj;
            List<Company> salers = MybatisUtil.call(CompanyMapper.class,
                    companyMapper -> companyMapper.getWholesalers(user.getCompanyId()));
            result.setStatus(true);
            result.setCode("0000");
            result.setData(salers);
        }
        return result;
    }

    @POST
    @Path("/package/out")
    @Produces(MediaType.APPLICATION_JSON)
    public RestResult packageOut(@FormParam("codes") String codes, @FormParam("companyId") int companyId) {
        HttpSession session = request.getSession();
        Object userObj = session.getAttribute("user");
        RestResult result = new RestResult();
        if (userObj == null) {
            result.setStatus(false);
            result.setCode("2001");
            result.setMessage("用户没有登陆！");
        } else {
            try {
                List<Long> longCodes = new ArrayList<>();
                String[] packCodes = codes.split(",");
                for (String code : packCodes) {
                    longCodes.add(Long.parseLong(code));
                }
                User user = (User) userObj;
                service.doOut(user, longCodes, companyId);
                result.setStatus(true);
                result.setCode("0000");
                result.setMessage("出库成功！");
            } catch (Exception e) {
                result.setStatus(false);
                result.setCode("2002");
                result.setMessage("出库失败，原因:" + e.getMessage());
            }
        }
        return result;
    }
    
    
    @GET
    @Path("/version")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> getUpdateVersion() {
        Map<String, Object> result = new HashMap<>();
        result.put("versionCode", 10);
        result.put("versionName", "1.1.0");
        result.put("downloadUrl", "http://www.zgzzcx.com/app/seedapp.1.1.0.apk");
        return result;
    }
}
