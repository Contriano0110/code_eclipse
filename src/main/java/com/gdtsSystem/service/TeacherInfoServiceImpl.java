package com.gdtsSystem.service;

import com.gdtsSystem.action.AdminServlet;
import com.gdtsSystem.dao.XgDao;
import com.gdtsSystem.entity.TeacherInfo;
import com.gdtsSystem.service.Interface.TeacherInfoService;
import com.gdtsSystem.util.GdtsSystemUtil;
import org.apache.log4j.Logger;
import org.omg.CORBA.Request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class TeacherInfoServiceImpl implements TeacherInfoService {
	Logger logger = Logger.getLogger(String.valueOf(AdminServlet.class));

	private ArrayList<TeacherInfo> getNewTeacherInfo() {
		String sql = "select * from teacher_info order by tid desc";
		return XgDao.getTeacherInfos(sql);
	}

	private boolean isExistTid(String tid) {
		String sql = "select * from teacher_info where tid='" + tid + "'";
		return XgDao.isExist(sql);
	}

	public ArrayList<TeacherInfo> getTeacherInfos(String name) {
		String sql;
		if (name == null) {
			sql = "select * from teacher_info";
		} else
			sql = "select * from teacher_info where tname like '%" + name + "%'";
		logger.debug(sql);
		return XgDao.getTeacherInfos(sql);
	}

	@Override
	public boolean insertTeacherInfo(TeacherInfo teacherInfo) {
		String sql = "insert into teacher_info (tid,tname,tsex,tage,tlvl,tmail)" + "values('t'||seq_tid.nextval,'"
				+ teacherInfo.getTname() + "','" + teacherInfo.getTsex() + "','" + teacherInfo.getTage() + "','"
				+ teacherInfo.getTlvl() + "','" + teacherInfo.getTmail() + "')";
		logger.debug(sql);
		return XgDao.canExecute(sql);
	}

	@Override
	public boolean insertTeacherInfo(HashMap m) {
		String sql = "insert into teacher_info (tid,tname,tsex,tage,tlvl,tmail) values ('t'||seq_tid.nextval,'"
				+ m.get("tname") + "','" + m.get("tsex") + "','" + m.get("tage") + "','" + m.get("tlvl") + "','"
				+ m.get("tmail") + "')";
		logger.debug(sql);
		return XgDao.canExecute(sql);
	}

	@Override
	public boolean deleteTeacherInfo(TeacherInfo teacherInfo) {
		String sql = "delete from teacher_info where tid='" + teacherInfo.getTid() + "'";
		return XgDao.canExecute(sql);
	}

	@Override
	public boolean deleteTeacherInfo(HashMap m) {
		String ids = (String) m.get("tids");
		logger.debug(ids);
		ids = ids.replaceAll(",", "','");
		logger.debug(ids);
		String sql = "delete from teacher_info where tid in ('" + ids + "')";
		logger.debug(sql);
		return XgDao.canExecute(sql);
	}

	@Override
	public boolean updateTeacherInfo(TeacherInfo teacherInfo) {
		String sql = "update teacher_info set " + "tname='" + teacherInfo.getTname() + "',tsex='"
				+ teacherInfo.getTsex() + "',tage='" + teacherInfo.getTage() + "',tlvl='" + teacherInfo.getTlvl()
				+ "',tmail='" + teacherInfo.getTmail() + "' where tid='" + teacherInfo.getTid() + "'";
		logger.debug(sql);
		return XgDao.canExecute(sql);
	}

	@Override
	public boolean updateTeacherInfo(HashMap m) {
		String sql = "update teacher_info set " + "tname='" + m.get("tname") + "',tsex='" + m.get("tsex") + "',tage='"
				+ m.get("tage") + "',tlvl='" + m.get("tlvl") + "',tmail='" + m.get("tmail") + "' where tid='"
				+ m.get("tid") + "'";
		return XgDao.canExecute(sql);
	}

	@Override
	public ArrayList<TeacherInfo> getTeacherInfos(HashMap m) {
		String find = (String) m.get("find");
		if (find == null) {
			find = "";
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

		String sql = "select r.*,rownum rn from (select * from teacher_info a where tname like '%" + find + "%' "
				+ " order by "+m.get("sortField")+" "+m.get("sortOrder")
				+ ") r";
		
		sql = "select * from (" + sql + ") b where b.rn between " + (pageIndex * pageSize + 1) + " and "
				+ (pageIndex * pageSize + pageSize);
		logger.debug(sql);
		return XgDao.getTeacherInfos(sql);
	}

}
