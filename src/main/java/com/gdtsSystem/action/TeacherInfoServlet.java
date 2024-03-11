package com.gdtsSystem.action;

import com.gdtsSystem.service.Interface.TeacherInfoService;
import com.alibaba.fastjson.JSON;
import com.gdtsSystem.entity.TeacherInfo;
import com.gdtsSystem.service.TeacherInfoServiceImpl;
import com.gdtsSystem.util.GdtsSystemUtil;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

@WebServlet(urlPatterns = "/teacherinfoaction")
public class TeacherInfoServlet extends HttpServlet {
	TeacherInfoService teacherInfoService = new TeacherInfoServiceImpl();
	Logger logger = Logger.getLogger(TeacherInfoServlet.class);

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
			logger.debug("insert");
			insert(req, resp);
			break;
		case "check":
			check(req, resp);
			break;
		case "delete":
			logger.debug("del");
			delete(req, resp);
			break;
		case "update":
			logger.debug("upd");
			update(req, resp);
			break;
		default:
			select(req, resp);
			break;
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
		if (teacherInfoService.deleteTeacherInfo(m)) {
			resp.getWriter().print("OK");
		} else {
			resp.getWriter().print("FAIL");
		}
	}

	private void update(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String data = req.getParameter("data");
		data = data.substring(1,data.length()-1);
		logger.debug(data);
		TeacherInfo d = JSON.parseObject(data,TeacherInfo.class);
		if (teacherInfoService.updateTeacherInfo(d)) {
			resp.getWriter().print("OK");
		} else {
			resp.getWriter().print("FAIL");
		}
	}

	private void select(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String find = req.getParameter("find");
		resp.setContentType("application/json;charset=UTF-8");
		HashMap m = new HashMap<String, String>();
		GdtsSystemUtil.getRequestData(req, m);// ?find=aa&pageIndex=2
		logger.debug(m);
		String jString = JSON.toJSONString(teacherInfoService.getTeacherInfos(m));
		logger.debug(jString);
		resp.getWriter().print(jString);
	}

	private void insert(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String data = req.getParameter("data");
		logger.debug(data);
		data = data.substring(1, data.length() - 1);
		TeacherInfo d = JSON.parseObject(data, TeacherInfo.class);
		logger.debug(d);
        if (teacherInfoService.insertTeacherInfo(d)) {
            resp.getWriter().print("OK");
        } else {
            resp.getWriter().print("FAIL");
        }
	}
}
