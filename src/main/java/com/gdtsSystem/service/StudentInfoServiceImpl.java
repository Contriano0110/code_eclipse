package com.gdtsSystem.service;

import com.gdtsSystem.action.AdminServlet;
import com.gdtsSystem.dao.XgDao;
import com.gdtsSystem.entity.StudentInfo;
import com.gdtsSystem.service.Interface.StudentInfoService;
import com.gdtsSystem.util.GdtsSystemUtil;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class StudentInfoServiceImpl implements StudentInfoService {
    Logger logger = Logger.getLogger(String.valueOf(AdminServlet.class));

    @Override
    public HashMap getStudentInfos_2(HashMap m) {
    	String oraclesql = "select a.*,rownum rn from student_info a";
		HashMap map = new HashMap();
		if (m.get("key") != null) {
			oraclesql = oraclesql + " where sname like '%" + m.get("key") + "%'";
		}

		int pageIndex = 0;
		try {
			pageIndex = Integer.parseInt((String) m.get("pageIndex"));
		} catch (Exception e) {
		}

		int pageSize = 10;
		try {
			pageSize = Integer.parseInt((String) m.get("pageSize"));
		} catch (Exception e) {
		}

		String sql = "select * from (" + oraclesql + ") x where x.rn between " + (pageIndex * pageSize + 1) + " and "
				+ (pageIndex * pageSize + pageSize);
		logger.debug(sql);

		ArrayList data = XgDao.getDatas(sql);
		logger.debug(data);

		sql = "select count(*) from (" + oraclesql + ")";
		int total = XgDao.getCount(sql);

		map.put("total", total);
		map.put("data", data);
		return map;
    }
    
    private boolean isExistSid(String sid) {
        String sql = "select * from student_info where sid='" + sid + "'";
        return XgDao.isExist(sql);
    }

    @Override
    public ArrayList<StudentInfo> getStudentInfos(String stu_name) {
        if (stu_name == null) stu_name = "";
        String sql = "select * from student_info where sname like '%" + stu_name + "%' order by sid";
        //logger.debug(sql);
        return XgDao.getStudentInfos(sql);
    }

    @Override
    public ArrayList<StudentInfo> getStudentInfos(HashMap m) {
        String find = (String) m.get("key");
        if (find == null) {
            find = "";
        }
        int pageIndex = 0;
        try {
            pageIndex = Integer.parseInt((String) m.get("pageIndex"));
        } catch (Exception e) {}
        int pageSize = 10;
        try {
        	pageSize = Integer.parseInt((String) m.get("pageSize"));
        } catch (Exception e) {}

        String sql = "select a.*,rownum rn from student_info a where sname like '%" + find + "%'";
        sql = "select * from ("
                + sql
                + ") b where b.rn between " + (pageIndex * pageSize + 1) + " and " + (pageIndex * pageSize + pageSize);
        logger.debug(sql);
        return XgDao.getStudentInfos(sql);
        //return XgDao.getDatas(sql);
    }

    public ArrayList<StudentInfo> getNewStudentInfo() {
        String sql = "select * from student_info order by sid desc";
        return XgDao.getStudentInfos(sql);
    }

    @Override
    public boolean insertStudentInfo(StudentInfo studentInfo) {
        String sql = "insert into student_info (sid,sname,ssex,sage,smail)" +
                "values('s'||seq_sid.nextval,'" + studentInfo.getSname() + "','"
                + studentInfo.getSsex() + "'," + studentInfo.getSage() + ",'" + studentInfo.getSmail() + "')";
        logger.debug(sql);
        return XgDao.canExecute(sql);
    }

    @Override
    public boolean insertStudentInfo(HashMap m) {
        String sql = "insert into student_info (sid,sname,ssex,sage,smail) values('s'||seq_sid.nextval,'"
                + m.get("sname") + "','"
                + m.get("ssex") + "','"
                + m.get("sage") + "','"
                + m.get("smail") + "')";
        logger.debug(sql);
        return XgDao.canExecute(sql);
    }

    @Override
    public boolean deleteStudentInfo(StudentInfo studentInfo) {
        String sql = "delete student_info where sid='" + studentInfo.getSid() + "'";
        logger.debug(sql);
        return XgDao.canExecute(sql);
    }

    @Override
    public boolean deleteStudentInfo(HashMap m) {
        String ids = (String) m.get("sids");
        ids = ids.replaceAll(",", "','");
        String sql = "delete student_info where sid in ('" + ids + "')";
        logger.debug(sql);
        return XgDao.canExecute(sql);
    }

    @Override
    public boolean updateStudentInfo(StudentInfo studentInfo) {
        String sql = "update student_info set " +
                "sname='" + studentInfo.getSname() +
                "',ssex='" + studentInfo.getSsex() +
                "',sage='" + studentInfo.getSage() +
                "',smail='" + studentInfo.getSmail() +
                "' where sid='" + studentInfo.getSid()
                + "'";
        logger.debug(sql);
        return XgDao.canExecute(sql);
    }

    @Override
    public boolean updateStudentInfo(HashMap m) {
        String sql = "update student_info set sid='"
                + m.get("sid") + "',sname='"
                + m.get("sname") + "',ssex='"
                + m.get("ssex") + "',sage='"
                + m.get("sage") + "',smail='"
                + m.get("smail") + "' where sid='" + m.get("oldsid") + "'";
        logger.debug(sql);
        return XgDao.canExecute(sql);
    }
}
