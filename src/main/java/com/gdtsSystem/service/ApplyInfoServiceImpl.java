package com.gdtsSystem.service;

import com.gdtsSystem.action.AdminServlet;
import com.gdtsSystem.dao.XgDao;
import com.gdtsSystem.entity.ApplyInfo;
import com.gdtsSystem.service.Interface.ApplyInfoService;
import com.gdtsSystem.util.GdtsSystemUtil;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class ApplyInfoServiceImpl implements ApplyInfoService {
	private static Scanner scanner = new Scanner(System.in);
	Logger logger = Logger.getLogger(String.valueOf(AdminServlet.class));

	// private String applyid;
//    private String sid;
//    private String gdtid;
//    private String gdtname;
//    private Timestamp applytime;
//    private Timestamp replytime;
//    private String status;

	private boolean isExistApplyid(String applyid) {
		String sql = "select * from apply_info where applyid='" + applyid + "'";
		return XgDao.isExist(sql);
	}

	@Override
	public ArrayList<ApplyInfo> getApplyInfos(String name) {
		String sql = null;
		if (name == null) {
			sql = "select * from apply_info";
		} else
			sql = "select * from apply_info where gdtname like '%" + name + "%'";
		// logger.debug(sql);
		return XgDao.getApplyInfos(sql);
	}

	@Override
	public HashMap getApplyInfos(HashMap m) {//sapply
		String key = (String) m.get("find");
		if (key == null)
			key = "";
		String oraclesql = "select r.*,rownum rn from (select a.*,t.tid,t.tname from apply_info a "
				+ " left join gdt_info b on a.gdtid=b.gdtid "
				+ " left join teacher_info t on t.tid=b.tid "
				+ " where a.gdtname like '%" + key + "%' "
				+ " order by a."+m.get("sortField") + " " + m.get("sortOrder") 
				+ " ) r";
		HashMap map = new HashMap();
		String sid = (String) m.get("sid");
		if (sid != null) {
			oraclesql = "select r.*,rownum rn from (select a.*,t.tid,t.tname from apply_info a "
					+ " left join gdt_info b on a.gdtid=b.gdtid "
					+ " left join teacher_info t on t.tid=b.tid "
					+ " where a.gdtname like '%" + key + "%' and a.sid = '" + sid + "'"
					+ " order by a."+m.get("sortField") + " " + m.get("sortOrder") 
					+ " ) r";
		}
		logger.debug(oraclesql);
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
		int total = XgDao.getCount("select count(*) from ("+oraclesql+")");
		logger.debug(total);

		String sql = "select * from (" + oraclesql + ") x where x.rn between " + (pageIndex * pageSize + 1)
				+ " and " + (pageIndex * pageSize + pageSize);
		logger.debug(sql);

		ArrayList data = XgDao.getDatas(sql);
		logger.debug(data);

		map.put("total", total);
		map.put("data", data);
		logger.debug(map);
		return map;
	}

	@Override
	public HashMap getApplyInfos_t(HashMap m) {
		String key = (String) m.get("find");
		if (key == null)
			key = "";
		String oraclesql = "select a.*,rownum rn from apply_info a where gdtname like '%" + key + "%'";
		HashMap map = new HashMap();
		String id = (String) m.get("tid");
		if (id != null) {
			oraclesql = "select a.*,rownum rn from apply_info a where gdtname like '%" + key + "%' and gdtid in "
					+ "( select gdtid from gdt_info where tid = '" + id + "')";
		}
		oraclesql += " order by "+m.get("sortField")+" "+m.get("sortOrder");	
		logger.debug(oraclesql);
		
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

		String sql = "select * from (" + oraclesql + ") x where x.rn between " + (pageIndex * pageSize + 1)
				+ " and " + (pageIndex * pageSize + pageSize);
		logger.debug(sql);

		ArrayList data = XgDao.getDatas(sql);
		logger.debug(data);

		map.put("data", data);
		return map;
	}
	
	@Override
	public HashMap getApplyInfos_tt(HashMap m) {  //教师		申请		父窗口
		String key = (String) m.get("find");
		if (key == null)
			key = "";
		String oraclesql = "select a.*,rownum rn from apply_info a where gdtname like '%" + key + "%'";
		HashMap map = new HashMap();
		String id = (String) m.get("tid");
		if (id != null) {
			oraclesql = "select a.*,rownum rn from apply_info a where gdtname like '%" + key + "%' and gdtid in "
					+ "( select gdtid from gdt_info where tid = '" + id + "')";
		}
		oraclesql += " order by "+m.get("sortField")+" "+m.get("sortOrder");	
		logger.debug(oraclesql);
		int total = XgDao.getCount("select count(*) from ("+oraclesql+")");
		
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

		String sql = "select * from (" + oraclesql + ") x where x.rn between " + (pageIndex * pageSize + 1)
				+ " and " + (pageIndex * pageSize + pageSize);
		logger.debug(sql);

		ArrayList data = XgDao.getDatas(sql);
		logger.debug(data);
		map.put("total", total);
		map.put("data", data);
		logger.debug(map);
		return map;
	}

	@Override
	public boolean updateApplyInfo_status(HashMap m) {
		String sql = "update apply_info set status = '已撤销申请' where applyid ='" + m.get("applyid") + "'";
		logger.debug(sql);
		return XgDao.canExecute(sql);
	}

	@Override
	public boolean insertApplyInfo(ApplyInfo applyInfo) {
		String sql = "insert into apply_info (applyid,sid,gdtid,gdtname,applytime,status)"
				+ "values('Apply'||to_char(sysdate,'mmdd')||lpad(seq_apply.nextval,8,0),'" + applyInfo.getSid() + "','"
				+ applyInfo.getGdtid() + "','" + applyInfo.getGdtname() + "'," + "sysdate,'" + applyInfo.getStatus()
				+ "')";
		logger.debug(sql);
		return XgDao.canExecute(sql);
	}

	@Override
	public boolean insertApplyInfo(HashMap m) {
		String sql = "insert into apply_info (applyid,sid,gdtid,gdtname,applytime,status)"
				+ "values('Apply'||to_char(sysdate,'mmdd')||lpad(seq_apply.nextval,8,0),'" + m.get("sid") + "','"
				+ m.get("gdtid") + "','" + m.get("gdtname") + "'," + "sysdate,'" + m.get("status") + "')";
		logger.debug(sql);
		return XgDao.canExecute(sql);
	}

	@Override
	public boolean deleteApplyInfo(ApplyInfo applyInfo) {
		String sql = "delete apply_info where applyid='" + applyInfo.getApplyid() + "'";
		return XgDao.canExecute(sql);
	}

	@Override
	public boolean deleteApplyInfo(HashMap m) {
		String ids = (String) m.get("applyids");
		logger.debug(ids);
		ids = ids.replaceAll(",", "','");
		logger.debug(ids);
		String sql = "delete apply_info where applyid in ('" + ids + "')";
		logger.debug(sql);
		return XgDao.canExecute(sql);
	}

	@Override
	public boolean updateApplyInfo(ApplyInfo applyInfo) {
		String sql = "update apply_info set sid='" + applyInfo.getSid() + "',gdtid='" + applyInfo.getGdtid()
				+ "',gdtname='" + applyInfo.getGdtname() + "',status='" + applyInfo.getStatus() + "' where applyid='"
				+ applyInfo.getApplyid() + "'";
		logger.debug(sql);
		return XgDao.canExecute(sql);
	}

	@Override
	public boolean updateApplyInfo(HashMap m) {
		String sql = "update apply_info set sid='" + m.get("sid") + "',gdtid='" + m.get("gdtid") + "',gdtname='"
				+ m.get("gdtname") + "',status='" + m.get("status") + "' where applyid='" + m.get("applyid") + "'";
		logger.debug(sql);
		return XgDao.canExecute(sql);
	}

	@Override
	public HashMap getApplyInfos_2(HashMap m) {
		String oraclesql = "select a.*,rownum rn from apply_info a";
		HashMap map = new HashMap();
		if (m.get("key") != null) {
			oraclesql = oraclesql + " where gdtname like '%" + m.get("key") + "%'";
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
}
