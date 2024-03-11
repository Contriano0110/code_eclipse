package com.gdtsSystem.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.gdtsSystem.dao.XgDao;
import com.gdtsSystem.entity.GdtInfo;
import com.gdtsSystem.service.Interface.GdtInfoService;
import com.gdtsSystem.service.GdtInfoServiceImpl;
import com.gdtsSystem.util.GdtsSystemUtil;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

@WebServlet(urlPatterns = "/gdtinfoaction")
public class GdtInfoServelet extends HttpServlet {
    GdtInfoService gdtInfoService = new GdtInfoServiceImpl();
    Logger logger = Logger.getLogger(GdtInfoServelet.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.debug("对象的地址"+this);
        logger.debug("处理客户端请求的当前线程标识"+Thread.currentThread().getId());
        logger.debug("当前会话的id："+req.getSession().getId());
        String method = req.getParameter("method");
        if (method == null) {
            method = "";
        }
        switch (method) {
            case "insert":
                insert(req, resp);
                break;
            case "tselect":
                tselect(req, resp);
                break;
            case "tselected":
                tselected(req, resp);
                break;
            case "sselect":
                sselect(req, resp);
                break;
            case "select":
                select(req, resp);
                break;
            case "apply":
                apply(req,resp);
                break;
            case "delete":
                delete(req, resp);
                break;
            case "update":
                update(req, resp);
                break;
            case "getgdttypes":
                getGdtTypes(req, resp);
                break;
            case "getmydata":
            	getmydata(req, resp);
                break;
            case "root":
            	root(req, resp);
                break;
            default:
                getGdtInfos(req, resp);
                break;
        }
    }

    private void tselected(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		// TODO Auto-generated method stub
    	HashMap ma = (HashMap) req.getSession().getAttribute("acname");
        logger.debug(ma);
        String find = req.getParameter("find");
        resp.setContentType("application/json;charset=UTF-8");
        HashMap m = new HashMap<String, String>();
        GdtsSystemUtil.getRequestData(req, m);//?find=aa&pageIndex=2
        m.putAll(ma);
        logger.debug(m);
        resp.setContentType("application/json;charset=UTF-8");
        String jString = JSON.toJSONString(gdtInfoService.getGdtInfos_ed(m), SerializerFeature.WriteMapNullValue); //转为jsonstring时保留map中的null值
        logger.debug(jString);
        jString = jString.replaceAll("status\":null", "status\":\"可申请\"");
        logger.debug(jString);
        
        //jString = jString.substring(1,jString.length()-1);
        resp.getWriter().print(jString);
	}

	private void root(HttpServletRequest req, HttpServletResponse resp) {
		// TODO Auto-generated method stub
		String sqlgdt = "select count(*) from gdt_info where sid is null";
		String sqlstu = "select count(*) from student_info";
		//String sql
		int cnt = XgDao.getCount(sqlstu) - XgDao.getCount(sqlstu);
		
		
	}

	//管理员功能:判断剩余课题数是否大于未选题学生数 , 若大于 则为学生随机选定课题
    
    private void getmydata(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		// TODO Auto-generated method stub
    	HashMap idHashMap=(HashMap) req.getSession().getAttribute("acname");//{sid='s100011',passwd='aaaa1111'}
    	logger.debug(idHashMap);
        resp.setContentType("application/json;charset=UTF-8");

    	String idsqlString="select a.*,b.tname,c.typename from gdt_info a left join teacher_info b on b.tid = a.tid left join gdt_type c on a.gdttype = c.typeid where sid='"+idHashMap.get("sid")+"'";
    	logger.debug(idsqlString);
		ArrayList lst=XgDao.getDatas(idsqlString);
		logger.debug(lst);
		if (lst.size()>0) {
			String lString=JSON.toJSONString(lst.get(0));
			logger.debug(lString);
			resp.getWriter().print(lString);
		} else {
			resp.getWriter().print("NODATA");
		}
	}

	private void sselect(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HashMap ma = (HashMap) req.getSession().getAttribute("acname");
        resp.setContentType("application/json;charset=UTF-8");
        HashMap m = new HashMap<String, String>();
        GdtsSystemUtil.getRequestData(req, m);//?find=aa&pageIndex=2
        m.putAll(ma);
        logger.debug(m);
        resp.setContentType("application/json;charset=UTF-8");
        String sql = "select a.*,g.typename,t.tname,q.status,rownum rn from gdt_info a "
        		+ " left join gdt_type g on g.typeid=a.gdttype "
        		+ " left join teacher_info t on t.tid=a.tid"
        		+ " left join (select gdtid,status from apply_info where sid = '"+m.get("sid")+"') q "
        		+ " on a.gdtid=q.gdtid where a.gdtid = '"+m.get("gdtid")+"'";
        logger.debug(sql);
        logger.debug(XgDao.getDatas(sql));
        logger.debug(JSON.toJSONString(XgDao.getDatas(sql)));
        String jString = JSON.toJSONString(XgDao.getDatas(sql));
        jString = jString.substring(1,jString.length()-1);
        logger.debug(jString);
        if (jString.indexOf("status") == -1) {
        	jString = "{\"status\":\"可申请\","+jString.substring(1);
        	logger.debug(jString);
        }
        int cnt = XgDao.getCount("select count(*) from gdt_info where sid='"+m.get("sid")+"'");
        logger.debug(cnt);
        if (cnt>0) {
        	jString = jString.replaceFirst("可申请", "已选题");
        	logger.debug(jString);
        }
        resp.getWriter().print(jString);
    }

    private void apply(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HashMap m = new HashMap<String, String>();
        GdtsSystemUtil.getRequestData(req, m);//?find=aa&pageIndex=2
        HashMap ma = (HashMap) req.getSession().getAttribute("acname");
        m.putAll(ma);
        logger.debug(m);
        if (gdtInfoService.applyGdtInfo(m)) {
            resp.getWriter().print("OK");
        } else {
            resp.getWriter().print("FAIL");
        }
    }

    private void tselect(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HashMap ma = (HashMap) req.getSession().getAttribute("acname");
        logger.debug(ma);
        String find = req.getParameter("find");
        resp.setContentType("application/json;charset=UTF-8");
        HashMap m = new HashMap<String, String>();
        GdtsSystemUtil.getRequestData(req, m);//?find=aa&pageIndex=2
        m.putAll(ma);
        logger.debug(m);
        resp.setContentType("application/json;charset=UTF-8");
        String jString = JSON.toJSONString(gdtInfoService.getGdtInfos(m), SerializerFeature.WriteMapNullValue);
        logger.debug(jString);
        jString = jString.replaceAll("status\":null", "status\":\"可申请\"");
        logger.debug(jString);
        int cnt = XgDao.getCount("select count(*) from gdt_info where sid='"+m.get("sid")+"'");
        logger.debug(cnt);
        if (cnt>0) {
        	jString = jString.replaceAll("可申请", "已选题");
        	logger.debug(jString);
        }
        //jString = jString.substring(1,jString.length()-1);
        resp.getWriter().print(jString);
    }

    private void select(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String find = req.getParameter("find");
        resp.setContentType("application/json;charset=UTF-8");
        HashMap m = new HashMap<String, String>();
        GdtsSystemUtil.getRequestData(req, m);//?find=aa&pageIndex=2
        logger.debug(m);

        //resp.getWriter().print(gdtInfoService.getGdtInfos(m));
//        JSONArray jsonArray = new JSONArray(gdtInfoService.getGdtInfos(m));
//        resp.getWriter().print(jsonArray.toString());
        resp.setContentType("application/json;charset=UTF-8");
        resp.getWriter().print(JSON.toJSONString(gdtInfoService.getGdtInfos(m)));
    }

    private void update(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String data = req.getParameter("data");

        logger.debug(data);
        data = data.substring(1, data.length() - 1);
        logger.debug(data);
        GdtInfo d = JSON.parseObject(data, GdtInfo.class);
        //d.setGdtid(req.getParameter("id"));
        logger.debug(d);
        
        //分辨谁在修改?
        HashMap acname = (HashMap) req.getSession().getAttribute("acname");
        if (acname.get("tid")!=null) {
			//老师你好
        	d.setTid((String) acname.get("tid"));
		}
        
        if (gdtInfoService.updateGdtInfo(d)) {
            resp.getWriter().print("OK");
        } else {
            resp.getWriter().print("FAIL");
        }
    }

    private void delete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HashMap m = new HashMap<String, String>();
        GdtsSystemUtil.getRequestData(req, m);
        logger.debug(m);
        
        //判断用户是否为老师
        HashMap acname = (HashMap) req.getSession().getAttribute("acname");
        if (acname.get("tid")!=null) {
			//老师你好
        	if (m.get("flag").equals("true")) {
        		return;
        	}
		}
        
        if (gdtInfoService.deleteGdtInfo(m)) {
            resp.getWriter().print("OK");
        } else {
            resp.getWriter().print("FAIL");
        }
    }

    private void insert(HttpServletRequest req, HttpServletResponse resp) throws IOException {

//        if (req.getParameter("data") == null) {
//            HashMap m = new HashMap<String, String>();
//            GdtsSystemUtil.getRequestData(req, m);
//            logger.debug(m);
//            if (gdtInfoService.insertGdtInfo(m)) {
//                resp.getWriter().print("OK");
//            } else {
//                resp.getWriter().print("FAIL");
//            }
//
//            return;
//        }

        String data = req.getParameter("data");
        logger.debug(data);
        data = data.substring(1, data.length() - 1);
        logger.debug(data);
        GdtInfo d = JSON.parseObject(data, GdtInfo.class);
        logger.debug(d);
        
        //分辨是谁在增加
        HashMap acname = (HashMap) req.getSession().getAttribute("acname");
        logger.debug(acname);
        if ( acname.get("tid") != null) {
        	//老师在增加
        	d.setTid((String) acname.get("tid"));
        }
        
        logger.debug(d);
        if (gdtInfoService.insertGdtInfo(d)) {
            resp.getWriter().print("OK");
        } else {
            resp.getWriter().print("FAIL");
        }

//        Emp emp = new Emp();
//        org.apache.jasper.runtime.JspRuntimeLibrary.introspect(emp, req);
//        logger.debug(emp);
//        //resp.getWriter().print("test");
//        if (empService.insertEmp(emp)) {
//            resp.getWriter().print("OK");
//        } else {
//            resp.getWriter().print("FAIL");
//        }
    }

//    public String delzkh(String str) {
//        StringBuilder newStr = new StringBuilder();
//        for (char ch : str.toCharArray()) {
//            if (ch != '[' && ch != ']') {
//                newStr.append(ch);
//            }
//        }
//        return newStr.toString();
//    }
//
//    public String changezkh(String str) {
//        StringBuilder newStr = new StringBuilder();
//        for (char ch : str.toCharArray()) {
//            if (ch != '[' && ch != ']') {
//                newStr.append(ch);
//            } else if (ch == '[') {
//                newStr.append('{');
//            } else {
//                newStr.append('}');
//            }
//        }
//        return newStr.toString();
//    }


    private void getGdtTypes(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ArrayList lst = XgDao.getDatas("select * from gdt_type");
        logger.debug(lst);

        resp.setContentType("application/json;charset=UTF-8");
        String a = JSON.toJSONString(lst);
        logger.debug(a);
        resp.getWriter().print(a);
    }

    private void getGdtInfos(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HashMap m = new HashMap();
        GdtsSystemUtil.getRequestData(req, m);
        HashMap ma = (HashMap) req.getSession().getAttribute("acname");
        m.putAll(ma);
        logger.debug(m);
        HashMap hashMap = gdtInfoService.getGdtInfos_2(m);
        logger.debug(hashMap);

        String jString = JSON.toJSONString(hashMap, SerializerFeature.WriteMapNullValue);
        jString = jString.replaceAll("status\":null", "status\":\"可申请\"");
        logger.debug(jString);
        int cnt = XgDao.getCount("select count(*) from gdt_info where sid='"+m.get("sid")+"'");
        logger.debug(cnt);
        if (cnt>0) {
        	jString = jString.replaceAll("可申请", "已选题");
        	logger.debug(jString);
        }
        resp.setContentType("application/json;charset=UTF-8");
        resp.getWriter().print(jString);
    }
}
