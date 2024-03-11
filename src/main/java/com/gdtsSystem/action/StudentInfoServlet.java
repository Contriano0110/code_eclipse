package com.gdtsSystem.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.gdtsSystem.service.Interface.StudentInfoService;
import com.gdtsSystem.dao.XgDao;
import com.gdtsSystem.entity.StudentInfo;
import com.gdtsSystem.service.StudentInfoServiceImpl;
import com.gdtsSystem.util.GdtsSystemUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

@WebServlet(urlPatterns = "/studentinfoaction")
public class StudentInfoServlet extends HttpServlet {
	StudentInfoService studentInfoService = new StudentInfoServiceImpl();
	Logger logger = Logger.getLogger(StudentInfoServlet.class);

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(req, resp);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String method = req.getParameter("method");
		if (method == null)
			method = "";
		switch (method) {
		case "insert":
			insert(req, resp);
			break;
		case "check":
			check(req, resp);
			break;
		case "delete":
			delete(req, resp);
			break;
		case "update":
			update(req, resp);
			break;
		case "updateInfo":
			updateInfo(req, resp);
			break;
		case "getInfo":
			getInfo(req, resp);
			break;
		default:
			select(req, resp);
			break;
		}
	}
	
	

	private void updateInfo(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		HashMap m = new HashMap();
		GdtsSystemUtil.getRequestData(req, m);
		logger.debug(m);
		if (m.get("flag").equals("new")) {//需要insert进账户表
			String sql = "insert into student_account (sid,passwd) values ('"+m.get("sid")+"','"+m.get("passwd")+"')";
			logger.debug(sql);
			XgDao.canExecute(sql);//执行一下
		} else {//老用户无法在此修改密码
		}
		
		//更新个人信息(新老用户相同)
		String sql1 = "update student_info set "
				+ "sname='"+m.get("sname")
				+"',ssex='"+m.get("ssex")
				+"',sage='"+m.get("sage")
				+"',smail='"+m.get("smail")
				+"' where sid = '"+m.get("sid")+"'";
		logger.debug(sql1);
		if (XgDao.canExecute(sql1)) {
			resp.getWriter().print("OK");
		} else {
			resp.getWriter().print("MAILERROR");
		}
	}

	private void getInfo(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("application/json;charset=UTF-8");
		HashMap acname = (HashMap) req.getSession().getAttribute("acname");
		String sid = (String) acname.get("sid");
		String sql = "select a.*,b.passwd from student_info a " +
					" left join student_account b on a.sid=b.sid " +
					" where a.sid = '" + sid + "'";
		logger.debug(sql);
		ArrayList lst = XgDao.getDatas(sql);
		if (lst.size()>0) {
			String data = JSON.toJSONString(lst.get(0), SerializerFeature.WriteMapNullValue);
			resp.getWriter().print(data);
		} else {
			resp.getWriter().print("FAIL");
		}
	}

	private void check(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//        HashMap m=new HashMap<String,String>();
//        GdtsSystemUtil.getRequestData(req,m);//?find=aa&pageIndex=2
//        if (xgtblService.checkXga(m)) {
//            resp.getWriter().print("exist");
//        } else {
//            resp.getWriter().print("not");
//        }
	}

	private void delete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		HashMap m = new HashMap<String, String>();
		GdtsSystemUtil.getRequestData(req, m);
		logger.debug(m);
		if (studentInfoService.deleteStudentInfo(m)) {
			resp.getWriter().print("OK");
		} else {
			resp.getWriter().print("FAIL");
		}
	}

	private void update(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String data = req.getParameter("data");
		logger.debug(data);
		data = data.substring(1, data.length() - 1);
		logger.debug(data);
		StudentInfo d = JSON.parseObject(data,StudentInfo.class);
		if (studentInfoService.updateStudentInfo(d)) {
			resp.getWriter().print("OK");
		} else {
			resp.getWriter().print("FAIL");
		}
	}

	private void select(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		HashMap m = new HashMap();
		GdtsSystemUtil.getRequestData(req, m);
		logger.debug(m);
		HashMap hashMap = studentInfoService.getStudentInfos_2(m);
		logger.debug(hashMap);

		resp.setContentType("application/json;charset=UTF-8");
		resp.getWriter().print(JSON.toJSONString(hashMap));
	}

	private void insert(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String data = req.getParameter("data");
		logger.debug(data);
		data = data.substring(1, data.length() - 1);
		logger.debug(data);
		StudentInfo d = JSON.parseObject(data,StudentInfo.class);
		if (studentInfoService.insertStudentInfo(d)) {
			resp.getWriter().print("OK");
		} else {
			resp.getWriter().print("FAIL");
		}
	}
}
