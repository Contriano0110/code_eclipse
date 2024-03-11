package com.gdtsSystem;

import com.gdtsSystem.action.AdminServlet;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

@WebFilter(urlPatterns = "/*")
public class XgFilter implements Filter {
    Logger logger = Logger.getLogger(XgFilter.class);
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //StringBuffer url = request.getRequestURL();
        String uri = request.getRequestURI();
        //logger.debug(url);
//        logger.debug(uri);
        uri=uri.substring(uri.lastIndexOf("/")+1);
//        logger.debug(uri);
        String sext = uri.substring(uri.lastIndexOf(".")+1);
//        logger.debug(sext);
        boolean bGo = false;
        bGo = uri.equals("login.html")
                || sext.equals("png")
                || uri.equals("forget.html")
                || uri.equals("zhuce.html")
                || sext.equals("jpg")
                || sext.equals("js")
                || sext.equals("css")
                || uri.equals("rootAction");
        HashMap map = new HashMap();
        String str = "";
        if(request.getSession().getAttribute("acname") != null) {
        	try {
				map = (HashMap) request.getSession().getAttribute("acname");
			} catch (Exception e) {
				// TODO: handle exception
			}
        	
        }
	
		if (request.getSession().getAttribute("acname") != null) {
			try {
				str = (String) request.getSession().getAttribute("acname");
			} catch (Exception e) {
				
			}
		}
		  
		  // TODO: handle exception  
		 
        if (!bGo) {
        //if (false) { //需要关闭filter时解开注释
            if (map.isEmpty() && str == "") {
                logger.debug("无权访问" + uri);
//                logger.debug(request.getContextPath());
                response.sendRedirect(request.getContextPath()+"/login.html");
                return;
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);

    }

    @Override
    public void destroy() {

    }
}
