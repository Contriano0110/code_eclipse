package com.gdtsSystem.action;

import com.alibaba.fastjson.JSON;
import com.gdtsSystem.dao.XgDao;
import com.gdtsSystem.util.GdtsSystemUtil;
import com.gdtsSystem.util.SendEmail;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.channels.SelectableChannel;
import java.util.ArrayList;
import java.util.HashMap;


@WebServlet(urlPatterns = "/rootAction")
public class AdminServlet extends HttpServlet {
    Logger logger = Logger.getLogger(AdminServlet.class);

    @Override
    public void init() throws ServletException {
        try {
            String path = getServletContext().getRealPath("/");
            path += "WEB-INF\\classes\\log4j.properties";
            logger.debug(path);
            PropertyConfigurator.configure(path);
        } catch (Exception e) {
            logger.debug(e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        logger.debug("对象的地址"+this);
        logger.debug("处理客户端请求的当前线程标识"+Thread.currentThread().getId());
        logger.debug("当前会话的id："+request.getSession().getId());
        logger.debug("" instanceof String);
        String method = request.getParameter("method");
        if (method == null) {
            method = "";
        }
        logger.debug(method);
        switch (method) {
            case "login":
                login(request, resp);
                break;
            case "forget":
                forget(request, resp);
                break;
            case "maillogin":
            	maillogin(request, resp);
                break;
            case "changepwd":
                changepwd(request, resp);
                break;
            case "logout":
                logout(request, resp);
                break;
            case "jiaoyan":
            	jiaoyan(request,resp);
            	break;
            case "zhuce":
            	zhuce(request,resp);
            	break;
            case "zhucejiaoyan":
            	zhucejiaoyan(request,resp);
            	break;
            case "checkinfo":
            	checkinfo(request,resp);
            	break;
        }
    }

	private void checkinfo(HttpServletRequest request, HttpServletResponse resp) throws IOException {
    	//判断个人信息是否完整, 如果不完整弹窗提示
    	HashMap m = new HashMap();
        GdtsSystemUtil.getRequestData(request, m);
        logger.debug(m);
        String sql = "";
        HashMap acname = (HashMap) request.getSession().getAttribute("acname");
        if (acname.get("sid")!=null) {
        	String u = (String) acname.get("sid");
        	sql = "select * from student_info where sid ='" + u + "'";
        } else {
        	String u = (String) acname.get("tid");
        	sql = "select * from teacher_info where tid ='" + u + "'";
        }
        ArrayList lst = XgDao.getDatas(sql);
        if (lst.size()>0) {
        	HashMap map = (HashMap) lst.get(0);
        	logger.debug(map);
        	for (Object value : map.values()) {
                if (value == null) {  
                    resp.getWriter().print("FAIL"); // 发现null值
                    return;
                }  
            }  
        	resp.getWriter().print("OK"); // 没有发现null值
        } else {}
        
    }
    
    private void zhucejiaoyan(HttpServletRequest request, HttpServletResponse resp) throws IOException {
    	//先判断邮箱是否存在?(存在)MAILERROR : (不存在)发送验证码
    	HashMap m = new HashMap();
        GdtsSystemUtil.getRequestData(request, m);
        logger.debug(m);
        String sql = "";
        if (m.get("u").equals("s")) {
        	sql = "select * from student_info where smail ='" + m.get("mail") + "'";
        } else {
        	sql = "select * from teacher_info where tmail ='" + m.get("mail") + "'";
        }
        ArrayList lst = XgDao.getDatas(sql);
        if (lst.size()>0) {
        	resp.getWriter().print("MAILERROR");//邮箱已被注册
        	return;
        } else {
        	jiaoyan(request, resp);
        } 
    }
    
    private void zhuce(HttpServletRequest request, HttpServletResponse resp) throws IOException {
    	HashMap m = new HashMap();
        GdtsSystemUtil.getRequestData(request, m);
        logger.debug(m);
        //注册账号
        if (m.get("u").equals("s")) {
        	String sql = "select seq_sid.nextval from dual";
        	String sid = "s" + XgDao.getCount(sql);
        	sql = "insert into student_info (sid,sname,smail) values ('"+sid+"','"+m.get("name")+"','"+m.get("mail")+"')";
        	logger.debug(sql);
        	if (XgDao.canExecute(sql)) {
        		sql = "insert into student_account (sid,passwd) values ('"+sid+"','"+m.get("passwd")+"')";
        		logger.debug(sql);
        		XgDao.canExecute(sql);
        		HashMap acname = new HashMap();
        		acname.put("sid", sid);
        		request.getSession().setAttribute("acname",acname);
        		resp.getWriter().print("OK");
        	}
        } else if (m.get("u").equals("t")) {
        	String sql = "select seq_tid.nextval from dual";
        	String tid = "t" + XgDao.getCount(sql);
        	sql = "insert into teacher_info (tid,tname,tmail) values ('"+tid+"','"+m.get("name")+"','"+m.get("mail")+"')";
        	logger.debug(sql);
        	if (XgDao.canExecute(sql)) {
        		sql = "insert into teacher_account (tid,passwd) values ('"+tid+"','"+m.get("passwd")+"')";
        		logger.debug(sql);
        		XgDao.canExecute(sql);
        		HashMap acname = new HashMap();
        		acname.put("tid", tid);
        		request.getSession().setAttribute("acname",acname);
        		resp.getWriter().print("OK");
        	}
        } else {
        	resp.getWriter().print("FAIL");
        }
    }
    
    private void jiaoyan(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        String mail = request.getParameter("mail");
        logger.debug(mail);
        int jiaoyan = (int) (Math.random() * 9000) + 1000;
        logger.debug("生成的随机数是:"+jiaoyan);
        SendEmail.sendJiaoyan(mail, jiaoyan);
        resp.getWriter().print(jiaoyan);
    }

    private void maillogin(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        String u = request.getParameter("u");
        String mail = request.getParameter("mail");
        int nextval;
        String sql;
        HashMap acname = new HashMap();
        if (u.equals("t")) {//教师
        	sql = "select tid from teacher_info where tmail = '"+mail+"'";
        	logger.debug(sql);
        	ArrayList lst = XgDao.getDatas(sql);
        	logger.debug(lst);
            if (lst.size() != 0) {//登录
            	request.getSession().setAttribute("acname",lst.get(0));
            	logger.debug(lst.get(0));
            	resp.getWriter().print("login");
            	return;
            } else {//注册
            	nextval = XgDao.getCount("select seq_tid.nextval from dual");
            	String tid = "t"+nextval;
            	sql = "insert into teacher_info (tid,tmail) values ('"+tid+"','"+mail+"')";
            	acname.put("tid", tid);
            	logger.debug(acname);
            }
        } else {//学生
        	sql = "select sid from student_info where smail = '"+mail+"'";
        	logger.debug(sql);
        	ArrayList lst = XgDao.getDatas(sql);
        	logger.debug(lst);
            if (lst.size() != 0) {//登录
            	request.getSession().setAttribute("acname",lst.get(0));
            	resp.getWriter().print("login");
            	return;
            } else {//注册
            	nextval = XgDao.getCount("select seq_sid.nextval from dual");
            	logger.debug(nextval);
            	String sid = "s"+nextval;
            	sql = "insert into student_info (sid,smail) values ('"+sid+"','"+mail+"')";
            	acname.put("sid", sid);
            	logger.debug(acname);
            }
		}
        
        logger.debug(sql);
        if (XgDao.canExecute(sql)) {//注册
        	logger.debug(acname);
        	request.getSession().setAttribute("acname",acname);
            logger.debug("注册");
            resp.getWriter().print("zhuce");
        } else {//失败
        	resp.getWriter().print("FAIL");
        }
    }

    private void forget(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        String acname = request.getParameter("acname");
        String sql = "";
        PrintWriter out = resp.getWriter();
        if (acname.charAt(0) == 's') { //学生
            sql = "select sid from student_account where sid='" + acname + "'";
            logger.debug(sql);
            ArrayList lst = XgDao.getDatas(sql);
            logger.debug("ls" + lst);
            if (lst.size() != 0) {
                logger.debug("lstfgt:" + lst);
                request.getSession().setAttribute("acname", lst.get(0));
                sql = "select passwd from student_account where sid='" + acname + "'";
                logger.debug(sql);
                ArrayList lst2 = XgDao.getDatas(sql);
                sql = "select smail from student_info where sid='" + acname + "'";
                lst2.addAll(XgDao.getDatas(sql));
                logger.debug("lst2fgt:" + lst2);
                if (lst2.size() != 0) {
                    out.print("OK");
                    //lst2fgt:[{passwd=aaaa1111}, [{smail=2192726915@qq.com}]]
                    String passwd = (String) ((HashMap) lst2.get(0)).get("passwd");
                    String smail = (String) ((HashMap) lst2.get(1)).get("smail");
                    logger.debug(passwd);
                    logger.debug(smail);
                    SendEmail.sendPasswd(smail, passwd);
                    logger.debug("ok");
                }
            } else {
                request.getSession().setAttribute("acname", null);
                out.print("FAIL");
            }
        } else if (acname.charAt(0) == 't'){ //教师
            sql = "select tid from teacher_account where tid='" + acname + "'";
            logger.debug(sql);
            ArrayList lst = XgDao.getDatas(sql);
            logger.debug("ls" + lst);
            if (lst.size() != 0) {
                logger.debug("lstfgt:" + lst);
                request.getSession().setAttribute("acname", lst.get(0));
                sql = "select passwd from teacher_account where tid='" + acname + "'";
                logger.debug(sql);
                ArrayList lst2 = XgDao.getDatas(sql);
                sql = "select tmail from teacher_info where tid='" + acname + "'";
                lst2.addAll(XgDao.getDatas(sql));
                logger.debug("lst2fgt:" + lst2);
                if (lst2.size() != 0) {
                    out.print("OK");
                    //lst2fgt:[{passwd=aaaa1111}, [{smail=2192726915@qq.com}]]
                    String passwd = (String) ((HashMap) lst2.get(0)).get("passwd");
                    String tmail = (String) ((HashMap) lst2.get(1)).get("tmail");
                    logger.debug(passwd);
                    logger.debug(tmail);
                    SendEmail.sendPasswd(tmail, passwd);
                    logger.debug("ok");
                }
            } else {
                request.getSession().setAttribute("acname", null);
                out.print("FAIL");
            }
        } else {
            out.print("FAIL");
        }
    }

    private void logout(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        request.getSession().setAttribute("acname", null);
        resp.sendRedirect("bye.bye");
    }

    private void changepwd(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        String oldpasswd = request.getParameter("oldpasswd");
        String passwd = request.getParameter("passwd");
        PrintWriter out = resp.getWriter();
        HashMap acname = (HashMap) request.getSession().getAttribute("acname");
        logger.debug("exe:" + acname.get("sid"));
        if (acname.get("sid") != null) {
            if (!acname.get("passwd").equals(oldpasswd)) {
                out.print("OLDERROR");
                return;
            }
            String sql = "update student_account set passwd='" + passwd + "' where sid='" + acname.get("sid") + "'";
            logger.debug(sql);
            if (!XgDao.canExecute(sql)) {
                out.print("FAIL");
                logger.debug("FAIL");
            } else {
                acname.put("passwd", passwd);
                out.print("OK");
                logger.debug("OK");
            }
        } else if (acname.get("tid") != null) {
            if (!acname.get("passwd").equals(oldpasswd)) {
                out.print("OLDERROR");
                return;
            }
            String sql = "update teacher_account set passwd='" + passwd + "' where tid='" + acname.get("tid") + "'";
            logger.debug(sql);
            if (!XgDao.canExecute(sql)) {
                out.print("FAIL");
                logger.debug("FAIL");
            } else {
                acname.put("passwd", passwd);
                out.print("OK");
                logger.debug("OK");
            }
        } else {
            if (!acname.get("passwd").equals(oldpasswd)) {
                out.print("OLDERROR");
                return;
            }
            String sql = "update root_account set passwd='" + passwd + "' where racnt='" + acname.get("racnt") + "'";
            logger.debug(sql);
            if (!XgDao.canExecute(sql)) {
                out.print("FAIL");
                logger.debug("FAIL");
            } else {
                acname.put("passwd", passwd);
                out.print("OK");
                logger.debug("OK");
            }
        }

        //resp.getWriter().print("!!!x"+acname);
//        String op = request.getParameter("op");
//        if ("logout".equals(op)) {
//            request.getSession().setAttribute("acname", null);
//        }
////request.gdtSession.setAttribute("acc",null)
//        if (acname != null && passwd != null) {
//            String sql, des;
//            if (acname.charAt(0) == 'r') {//管理员
//                sql = "select * from root_account where racnt='"
//                        + acname + "' and passwd='" + passwd + "'";
//                des = "rootmenu.jsp";
//            } else if (acname.charAt(0) == 't') {//教师
//                sql = "select * from teacher_account where tid='"
//                        + acname + "' and passwd='" + passwd + "'";
//                des = "teacher/teachermenu.jsp";
//            } else {//学生
//                sql = "select * from student_account where sid='"
//                        + acname + "' and passwd='" + passwd + "'";
//                des = "student/studentmenu.jsp";
//            }
//            ArrayList lst = XgDao.getDatas(sql);
//            if (lst.size()!=0) {
//                out.print("ok!");
//                request.getSession().setAttribute("racname", lst.get(0));
//                //response.sendRedirect(des);
//            } else {
//                request.getSession().setAttribute("racname", null);
//                out.print("fail!");
//                //request.setAttribute("xg", "无效的用户或密码");
//            }
//
//        }
    }

    private void login(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        String acname = request.getParameter("acname");
        logger.debug(acname);
        String passwd = request.getParameter("passwd");
        PrintWriter out = resp.getWriter();

        if (acname != null && passwd != null) {
            String sql, des;
            if (acname.charAt(0) == 'r') {//管理员
                sql = "select * from root_account where racnt='"
                        + acname + "' and passwd='" + passwd + "'";
                //des = "rootmenu.jsp";
            } else if (acname.charAt(0) == 't') {//教师
                sql = "select * from teacher_account where tid='"
                        + acname + "' and passwd='" + passwd + "'";
                //des = "teacher/teachermenu.jsp";
            } else {//学生
                sql = "select * from student_account where sid='"
                        + acname + "' and passwd='" + passwd + "'";
                //des = "student/studentmenu.jsp";
            }
            logger.debug(sql);
            ArrayList lst = XgDao.getDatas(sql);
            if (lst.size() != 0) {
                out.print("ok!");
                logger.debug("lst:" + lst);
                logger.debug("get0:" + lst.get(0));
                request.getSession().setAttribute("acname", lst.get(0));
                logger.debug(request.getSession().getAttribute("acname"));
                //response.sendRedirect(des);
            } else {
                request.getSession().setAttribute("acname", null);
                out.print("fail!");
                //request.setAttribute("xg", "无效的用户或密码");
            }

        }
    }

}




