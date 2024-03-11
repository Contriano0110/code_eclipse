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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.channels.SelectableChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.InputStream;
import java.io.Reader;
import java.io.InputStreamReader;

@WebServlet(urlPatterns = "/chataction")
public class ChatServlet extends HttpServlet {
	Logger logger = Logger.getLogger(ChatServlet.class);

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(req, resp);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse resp) throws IOException {
		String method = request.getParameter("method");
		if (method == null) {
			method = "";
		}
		logger.debug(method);
		switch (method) {
		case "getchat"://学生获取聊天记录
			getchat(request, resp);
			break;
		case "gettchat"://老师获取聊天记录
			gettchat(request, resp);
			break;
		case "sendmsg"://学生发送消息
			sendmsg(request, resp);
			break;
		case "tsendmsg"://老师发送消息
			tsendmsg(request, resp);
			break;

		}
	}

	private void tsendmsg(HttpServletRequest request, HttpServletResponse resp) throws IOException {
		// TODO Auto-generated method stub
		String sid = request.getParameter("sid");
		logger.debug(sid);
		HashMap ac = (HashMap) request.getSession().getAttribute("acname");
		String tid = (String) ac.get("tid");

		String jsonString = "";
		try {
			// 从请求体中读取 JSON 字符串
			StringBuilder stringBuilder = new StringBuilder();
			InputStream inputStream = request.getInputStream();
			Reader reader = new InputStreamReader(inputStream, "UTF-8");
			BufferedReader bufferedReader = new BufferedReader(reader);
			char[] charBuffer = new char[200];//存放消息
			int bytesRead;
			while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
				stringBuilder.append(charBuffer, 0, bytesRead);
			}
			jsonString = stringBuilder.toString();
			logger.debug(jsonString);
			if (jsonString.length()<=14) {//14是消息为空的情况,此时jsonString长这样: {"message":""} 长度为14
				resp.getWriter().print("ERROR");
				return;
			}
			jsonString = jsonString.substring(12, jsonString.length() - 2); //截取字符串,只保留消息
			logger.debug(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (jsonString == "") {
			resp.getWriter().print("ERROR");
		} else {
			// 将数据插入数据库 eg. insert into chat (chatid,sender,receiver,sendtime,message)
			// values
			// ('msg'||lpad(seq_chatid.nextval,7,0),'t1003','s100003',to_date('2024-02-15
			// 11:27:30','yyyy-mm-dd hh24:mi:ss'),'怎么了');
			String sql = "insert into chat (chatid,sender,receiver,sendtime,message) values ("
					+ "'msg'||lpad(seq_chatid.nextval,7,0),'" + tid + "','" + sid + "',sysdate,'" + jsonString + "')";
			logger.debug(sql);
			if (XgDao.canExecute(sql)) {
				resp.getWriter().print("OK");
			} else {
				resp.getWriter().print("FAIL");
			}
		}
	}

	private void gettchat(HttpServletRequest request, HttpServletResponse resp) throws IOException {
		// TODO Auto-generated method stub
		String sid = request.getParameter("sid");
		logger.debug(sid);
		HashMap ac = (HashMap) request.getSession().getAttribute("acname");
		String tid = (String) ac.get("tid");
		// select * from chat where sender in('s100006','t1001') and receiver
		// in('s100006','t1001');   用于获取通信双方相关消息
		String sql = "select * from chat where sender in('" + sid + "','" + tid + "') and receiver in('" + sid + "','"
				+ tid + "') order by chatid";
		logger.debug(sql);
		ArrayList chatlst = XgDao.getDatas(sql);
		logger.debug(chatlst);
		if (chatlst.size() > 0) {
			resp.setContentType("application/json;charset=UTF-8");
			String jString = JSON.toJSONString(chatlst);
			logger.debug(jString);
			resp.getWriter().print(jString);
		} else {
			resp.getWriter().print("NODATA");
		}
	}

	private void sendmsg(HttpServletRequest request, HttpServletResponse resp) throws IOException {
		String tid = request.getParameter("tid");
		logger.debug(tid);
		HashMap ac = (HashMap) request.getSession().getAttribute("acname");
		String sid = (String) ac.get("sid");

		String jsonString = "";
		try {
			StringBuilder stringBuilder = new StringBuilder();
			InputStream inputStream = request.getInputStream();
			Reader reader = new InputStreamReader(inputStream, "UTF-8");
			BufferedReader bufferedReader = new BufferedReader(reader);
			char[] charBuffer = new char[200];
			int bytesRead;
			while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
				stringBuilder.append(charBuffer, 0, bytesRead);
			}
			jsonString = stringBuilder.toString();
			jsonString = jsonString.substring(12, jsonString.length() - 2);
			logger.debug(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (jsonString == "") {
			resp.getWriter().print("ERROR");
		} else {
			// 将数据插入数据库 eg. insert into chat (chatid,sender,receiver,sendtime,message)
			// values
			// ('msg'||lpad(seq_chatid.nextval,7,0),'t1003','s100003',to_date('2024-02-15
			// 11:27:30','yyyy-mm-dd hh24:mi:ss'),'怎么了');
			String sql = "insert into chat (chatid,sender,receiver,sendtime,message) values ("
					+ "'msg'||lpad(seq_chatid.nextval,7,0),'" + sid + "','" + tid + "',sysdate,'" + jsonString + "')";
			logger.debug(sql);
			if (XgDao.canExecute(sql)) {
				resp.getWriter().print("OK");
			} else {
				resp.getWriter().print("FAIL");
			}
		}
	}

	private void getchat(HttpServletRequest request, HttpServletResponse resp) throws IOException {
		// TODO Auto-generated method stub
		String tid = "", sid = "";
		if (request.getParameter("tid") != "") {
			tid = request.getParameter("tid");
			logger.debug(tid);
			HashMap ac = (HashMap) request.getSession().getAttribute("acname");
			sid = (String) ac.get("sid");
		} else {
			sid = request.getParameter("sid");
			logger.debug(sid);
			HashMap ac = (HashMap) request.getSession().getAttribute("acname");
			tid = (String) ac.get("tid");
		}
		// select * from chat where sender in('s100006','t1001') and receiver
		// in('s100006','t1001');
		String sql = "select * from chat where sender in('" + sid + "','" + tid + "') and receiver in('" + sid + "','"
				+ tid + "') order by chatid";
		logger.debug(sql);
		ArrayList chatlst = XgDao.getDatas(sql);
		logger.debug(chatlst);
		if (chatlst.size() > 0) {
			resp.setContentType("application/json;charset=UTF-8");
			String jString = JSON.toJSONString(chatlst);
			logger.debug(jString);
			resp.getWriter().print(jString);
		} else {
			resp.getWriter().print("NODATA");
		}
	}

}
