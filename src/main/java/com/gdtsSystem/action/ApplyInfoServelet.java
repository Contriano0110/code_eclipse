package com.gdtsSystem.action;

import com.alibaba.fastjson.JSON;
import com.gdtsSystem.dao.XgDao;
import com.gdtsSystem.entity.ApplyInfo;
import com.gdtsSystem.service.ApplyInfoServiceImpl;
import com.gdtsSystem.service.GdtInfoServiceImpl;
import com.gdtsSystem.service.Interface.ApplyInfoService;
import com.gdtsSystem.service.Interface.GdtInfoService;
import com.gdtsSystem.util.GdtsSystemUtil;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerRepository;

import javax.crypto.KeyAgreement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.soap.Detail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

@WebServlet(urlPatterns = "/applyinfoaction")
public class ApplyInfoServelet extends HttpServlet {
	ApplyInfoService applyInfoService = new ApplyInfoServiceImpl();
	Logger logger = Logger.getLogger(ApplyInfoServelet.class);

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		logger.debug("对象的地址" + this);
		logger.debug("处理客户端请求的当前线程标识" + Thread.currentThread().getId());
		logger.debug("当前会话的id：" + req.getSession().getId());
		String method = req.getParameter("method");
		logger.debug("-----------" + method);
		if (method == null) {
			method = "";
		}
		switch (method) {
		case "agree":
			agree(req,resp);
			break;
		case "refuse":
			refuse(req,resp);
			break;
		case "detail":
			detail(req, resp);
			break;
		case "tselect":
			tselect(req, resp);
			break;
		case "delete":
			delete(req, resp);
			break;
		case "sdelete":
			sdelete(req, resp);
			break;
		case "insert":
			insert(req, resp);
			break;
		case "update":
			update(req, resp);
			break;
		case "select":
			select(req, resp);
			break;
		default:
			getApplyInfos(req, resp);
			break;
		}
	}
	
	private void agree(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HashMap m = new HashMap();
        GdtsSystemUtil.getRequestData(req, m);
        logger.debug(m);
        String sql = "update apply_info set replytime=sysdate,status='导师已同意' where applyid='" + m.get("applyid") + "'";
        logger.debug(sql);
        if (XgDao.canExecute(sql)) {
            sql = "update apply_info set status='学生已选题' where sid='" + m.get("sid") + "' and status='申请中'";
            logger.debug("f"+sql);
            XgDao.canExecute(sql);
            sql = "update apply_info set status='课题已关闭' where status in ('申请中','已撤销申请') and gdtid ='" + m.get("gdtid") + "'";
            logger.debug("s"+sql);
            XgDao.canExecute(sql);
            sql = "update gdt_info set sid='" + m.get("sid") + "' where gdtid='" + m.get("gdtid") + "'";
            logger.debug("t"+sql);
            XgDao.canExecute(sql);
            resp.getWriter().print("OK");
        } else {
            resp.getWriter().print("FAIL");
        }
    }
	
	private void refuse(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HashMap m = new HashMap();
        GdtsSystemUtil.getRequestData(req, m);
        logger.debug(m);
        String sql = "update apply_info set replytime=sysdate,status='导师已拒绝' where applyid='" + m.get("applyid") + "'";
        logger.debug(sql);
        if (XgDao.canExecute(sql)) {
            resp.getWriter().print("OK");
        } else {
            resp.getWriter().print("FAIL");
        }
    }

	private void detail(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		HashMap m = new HashMap();
		GdtsSystemUtil.getRequestData(req, m);
		logger.debug(m);
		String applyid = (String) m.get("applyid");
		String sql = "select * from apply_info a left join student_info s on a.sid = s.sid where applyid = '"+applyid+"'";
		ArrayList a = XgDao.getDatas(sql);
		logger.debug(a);
		String jString = JSON.toJSONString(a);
		logger.debug(jString);
		jString = jString.substring(1, jString.length() - 1);
		logger.debug(jString);
		resp.setContentType("application/json;charset=UTF-8");
		if (a.size() > 0) {
			resp.getWriter().print(jString);
		} else {
			resp.getWriter().print("FAIL");
		}
	}

	private void sdelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		HashMap m = new HashMap<String, String>();
		GdtsSystemUtil.getRequestData(req, m);
		if (applyInfoService.updateApplyInfo_status(m)) {
			resp.getWriter().print("OK");
		} else {
			resp.getWriter().print("FAIL");
		}
	}

	private void tselect(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String find = req.getParameter("find");
		resp.setContentType("application/json;charset=UTF-8");
		HashMap m = new HashMap<String, String>();
		GdtsSystemUtil.getRequestData(req, m);// ?find=aa&pageIndex=2
		logger.debug(m);
		HashMap ma = (HashMap) req.getSession().getAttribute("acname");
		m.putAll(ma);
		logger.debug(m);
		String jsonStr = JSON.toJSONString(applyInfoService.getApplyInfos_tt(m));
		//jsonStr = jsonStr.substring(8, jsonStr.length() - 1);
		resp.setContentType("application/json;charset=UTF-8");
		resp.getWriter().print(jsonStr);
	}

	private void update(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String data = req.getParameter("data");
		logger.debug(data);
		data = data.substring(1, data.length() - 1);
		logger.debug(data);
		ApplyInfo d = JSON.parseObject(data, ApplyInfo.class);
		logger.debug(d);
		if (applyInfoService.updateApplyInfo(d)) {
			resp.getWriter().print("OK");
		} else {
			resp.getWriter().print("FAIL");
		}
	}

	private void delete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		HashMap m = new HashMap<String, String>();
		GdtsSystemUtil.getRequestData(req, m);
		logger.debug(m);
		if (applyInfoService.deleteApplyInfo(m)) {
			resp.getWriter().print("OK");
		} else {
			resp.getWriter().print("FAIL");
		}
	}

	private void insert(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String s = req.getParameter("status");
		logger.debug(s);
		String data = req.getParameter("data");// ?find=aa&pageIndex=2
		logger.debug(data);
//        Xgtbl xgtbl = new Xgtbl();
//        org.apache.jasper.runtime.JspRuntimeLibrary.introspect(xgtbl, req);
//        logger.debug(xgtbl);
		// resp.getWriter().print("test");
		data = data.substring(1, data.length() - 1);
		logger.debug(data);
		ApplyInfo d = JSON.parseObject(data, ApplyInfo.class);
		if (applyInfoService.insertApplyInfo(d)) {
			resp.getWriter().print("OK");
		} else {
			resp.getWriter().print("FAIL");
		}
	}

	private void select(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String find = req.getParameter("find");
		HashMap mp = (HashMap) req.getSession().getAttribute("acname");
		logger.debug(mp);
		resp.setContentType("application/json;charset=UTF-8");
		HashMap m = new HashMap<String, String>();
		GdtsSystemUtil.getRequestData(req, m);// ?find=aa&pageIndex=2
		logger.debug(m);
		m.putAll(mp);
		logger.debug(m);
		String jsonStr = JSON.toJSONString(applyInfoService.getApplyInfos(m));
		logger.debug(jsonStr);
		resp.setContentType("application/json;charset=UTF-8");
		resp.getWriter().print(jsonStr);
	}

	private void getApplyInfos(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		HashMap m = new HashMap();
		GdtsSystemUtil.getRequestData(req, m);
		logger.debug(m);
		HashMap hashMap = applyInfoService.getApplyInfos_2(m);
		logger.debug(hashMap);
		resp.setContentType("application/json;charset=UTF-8");
		resp.getWriter().print(JSON.toJSONString(hashMap));
	}
}
