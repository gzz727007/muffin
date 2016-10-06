/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seedqr.gui;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author muffin
 */
public class QueryFilter implements Filter{

    @Override
    public void init(FilterConfig fc) throws ServletException {
        
    }

    @Override
    public void doFilter(ServletRequest sr, ServletResponse sr1, FilterChain fc) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) sr;  
        HttpServletResponse resp = (HttpServletResponse) sr1;  
        String path = req.getContextPath();  
        String id = req.getParameter("id");
        resp.sendRedirect(path+"/query.xhtml?id=" + id);  
    }

    @Override
    public void destroy() {
        
    }
    
}
