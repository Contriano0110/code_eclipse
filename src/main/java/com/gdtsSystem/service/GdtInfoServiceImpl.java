package com.gdtsSystem.service;

import com.gdtsSystem.action.AdminServlet;
import com.gdtsSystem.dao.XgDao;
import com.gdtsSystem.entity.GdtInfo;
import com.gdtsSystem.service.Interface.GdtInfoService;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.border.AbstractBorder;

public class GdtInfoServiceImpl implements GdtInfoService {
	Logger logger = Logger.getLogger(AdminServlet.class);

	private boolean isExistGdtid(String gdtid) {
		String sql = "select * from gdt_info where gdtid='" + gdtid + "'";
		return XgDao.isExist(sql);
	}

	@Override
	public ArrayList<GdtInfo> getGdtInfos(String name) {
		String sql = null;
		if (name == null) {
			sql = "select * from gdt_info";
		} else
			sql = "select * from gdt_info where gdtname like '%" + name + "%'";
		// logger.debug(sql);
		return XgDao.getGdtInfos(sql);
	}

	@Override
	public ArrayList<GdtInfo> getGdtInfos(HashMap m) {
		String sid = (String) m.get("sid");
		String tid = (String) m.get("tid");
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

		String sql = "select a.*,g.typename,rownum rn from gdt_info a "
				+ " left join gdt_type g on a.gdttype=g.typeid "
				+ " where gdtname like '%" + find + "%' order by gdtid "+m.get("sortOrder");
		if (tid != null) {
			sql = "select a.*,g.typename,rownum rn from gdt_info a "
					+ " left join gdt_type g on a.gdttype=g.typeid "
					+ " where gdtname like '%" + find + "%' and tid = '" + tid + "' order by gdtid "+m.get("sortOrder");
		}
		if (sid != null) {
			sql = "select a.*,g.typename,q.status,rownum rn from gdt_info a "
					+ " left join gdt_type g on a.gdttype=g.typeid "
					+ " left join (select gdtid,status from apply_info where sid = '"+sid+"') q "
					+ " on  a.gdtid=q.gdtid where gdtname like '%" + find + "%' and tid = '"+tid+"' order by gdtid "+m.get("sortOrder");
		}
		logger.debug(sql);
		//分页查询
		sql = "select * from (" + sql + ") b where b.rn between " + (pageIndex * pageSize + 1) + " and "
				+ (pageIndex * pageSize + pageSize);
		logger.debug(sql);
		logger.debug(XgDao.getDatas(sql));
		return XgDao.getDatas(sql);
		// return XgDao.getDatas(sql);
	}
	
	@Override
	public ArrayList<GdtInfo> getGdtInfos_ed(HashMap m) {//教师进入课题ed使用
		String sid = (String) m.get("sid");
		String tid = (String) m.get("tid");
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

		String sql = "select a.*,g.typename,rownum rn from gdt_info a "
				+ " left join gdt_type g on a.gdttype=g.typeid "
				+ " where gdtname like '%" + find + "%' and sid is not null order by gdtid "+m.get("sortOrder");
		if (tid != null) {
			sql = "select a.*,g.typename,rownum rn from gdt_info a "
					+ " left join gdt_type g on a.gdttype=g.typeid "
					+ " where gdtname like '%" + find + "%' and tid = '" + tid + "' and sid is not null order by gdtid "+m.get("sortOrder");
		}
		logger.debug(sql);
		//分页查询
		sql = "select * from (" + sql + ") b where b.rn between " + (pageIndex * pageSize + 1) + " and "
				+ (pageIndex * pageSize + pageSize);
		logger.debug(sql);
		logger.debug(XgDao.getDatas(sql));
		return XgDao.getDatas(sql);
		// return XgDao.getDatas(sql);
	}

	@Override
	public ArrayList<GdtInfo> getGdtInfos_student(String sid, String find) {
		String sql = null;
		if (sid == null) {
			sql = "select * from apply_info where sid ='" + sid + "'";
		} else
			sql = "select * from apply_info where gdtname like '%" + sid + "%' and sid='" + sid + "'";
		logger.debug(sql);
		return XgDao.getGdtInfos(sql);
	}

	@Override
	public boolean insertGdtInfo(GdtInfo gdtInfo) {
		String sql = "insert into gdt_info (gdtid,gdtname,gdttype,gdtreq,gdtintro,tid,sid)"
				+ "values('Gdt'||to_char(sysdate,'mmdd')||lpad(seq_gdt.nextval,6,0),'" + gdtInfo.getGdtname() + "','"
				+ gdtInfo.getGdttype() + "','" + gdtInfo.getGdtreq() + "','" + gdtInfo.getGdtintro() + "','"
				+ gdtInfo.getTid() + "','" + gdtInfo.getSid() + "')";
		logger.debug("gdtinfo:" + sql);
		return XgDao.canExecute(sql);
	}

	@Override
	public boolean insertGdtInfo(HashMap m) {
//        logger.debug("she:"+m.get("tid"));
//        logger.debug("he:"+m.get("acname"));
		if (m.get("tid") == "") {
			m.put("tid", m.get("acname"));
		}
		String sql = "insert into gdt_info (gdtid,gdtname,gdttype,gdtreq,gdtintro,tid,sid)"
				+ "values('Gdt'||to_char(sysdate,'mmdd')||lpad(seq_gdt.nextval,6,0),'" + m.get("gdtname") + "','"
				+ m.get("gdttype") + "','" + m.get("gdtreq") + "','" + m.get("gdtintro") + "','" + m.get("tid") + "','"
				+ m.get("sid") + "')";
		logger.debug("hashmap:" + sql);
		return XgDao.canExecute(sql);
	}

	@Override
	public boolean deleteGdtInfo(GdtInfo gdtInfo) {
		String sql = "delete gdt_info where gdtid='" + gdtInfo.getGdtid() + "'";
		return XgDao.canExecute(sql);
	}

	@Override
	public boolean deleteGdtInfo(HashMap m) {
		String ids = (String) m.get("gdtids");
		logger.debug(ids);
//		if (m.get("gdtid") != null) {
//			ids += "," + m.get("gdtid");
//			logger.debug(ids);
//		}
		ids = ids.replaceAll(",", "','");// 注意,replaceAll返回修改后的值而不是直接修改.
		logger.debug(ids);
//        StringBuilder newids = new StringBuilder(); //以下注释效果同replaceAll
//        for (char ch : ids.toCharArray()) {
//            if (ch != ',') {
//                newids.append(ch);
//            } else {
//                newids.append("\',\'");
//            }
//        }
//        logger.debug(newids);
		String sql = "delete gdt_info where gdtid in ('" + ids + "')";
		logger.debug("del:" + sql);
		return XgDao.canExecute(sql);
	}

	@Override
	public boolean updateGdtInfo(GdtInfo gdtInfo) {
		String sql = "update gdt_info set gdtname='" + gdtInfo.getGdtname() + "',gdttype=" + gdtInfo.getGdttype()
				+ ",gdtreq='" + gdtInfo.getGdtreq() + "',gdtintro='" + gdtInfo.getGdtintro() + "',tid='"
				+ gdtInfo.getTid() + "',sid='" + gdtInfo.getSid() + "' where gdtid='" + gdtInfo.getGdtid() + "'";
		logger.debug(sql);
		return XgDao.canExecute(sql);
	}

	@Override
	public boolean updateGdtInfo(HashMap m) {
		String sql = "update gdt_info set gdtname='" + m.get("gdtname") + "',gdttype=" + m.get("gdttype") + ",gdtreq='"
				+ m.get("gdtreq") + "',gdtintro='" + m.get("gdtintro") + "',tid='" + m.get("tid") + "',sid='"
				+ m.get("sid") + "' where gdtid='" + m.get("gdtid") + "'";
		logger.debug(sql);
		return XgDao.canExecute(sql);
	}

	@Override
	public HashMap getGdtInfos_2(HashMap m) {
		String oraclesql = "select a.*,rownum rn from gdt_info a";
		HashMap map = new HashMap();
		if (m.get("sid") != null) {
			oraclesql = "select r.*,rownum rn from ("
					+"select a.*,q.status,g.typename,t.tname from gdt_info a "
					+ " left join teacher_info t on t.tid=a.tid "
					+ " left join gdt_type g on g.typeid=a.gdttype "
					+ " left join (select gdtid,status from apply_info where sid = '"+m.get("sid")+"') q "
					+ " on a.gdtid=q.gdtid order by "+ m.get("sortField") +" "+m.get("sortOrder")
					+ ") r";
		}
		if (m.get("key") != null) {
			oraclesql = oraclesql + " where gdtname like '%" + m.get("key") + "%'";
			oraclesql = "select r.*,rownum rn from ("
					+"select a.*,q.status,g.typename,t.tname from gdt_info a "
					+ " left join teacher_info t on t.tid=a.tid "
					+ " left join gdt_type g on g.typeid=a.gdttype "
					+ " left join (select gdtid,status from apply_info where sid = '"+m.get("sid")+"') q "
					+ " on a.gdtid=q.gdtid where gdtname like '%" + m.get("key") + "%'"
					+ " order by "+ m.get("sortField") +" "+m.get("sortOrder")
					+ ") r"; 
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

		String sql = "select * from (" + oraclesql + ") x where x.rn between " + ((pageIndex) * pageSize + 1) + " and "
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

	@Override
	public boolean applyGdtInfo(HashMap m) {
		String sql;
        logger.debug(m.get("status"));
        if (m.get("status").equals("已撤销申请")) {
            sql = "update apply_info set status='申请中',msg='"+m.get("msg")+"' where sid='" + m.get("sid") + "' and gdtid='" + m.get("gdtid") + "'";
        } else {
            sql = "insert into apply_info (applyid,sid,gdtid,gdtname,applytime,replytime,status,msg) " +
                    "values ('Apply'||to_char(sysdate,'mmdd')||lpad(seq_apply.nextval,8,'0'),'" +
                    m.get("sid") + "','" + m.get("gdtid") + "','" + m.get("gdtname") 
                    + "',sysdate,null,'申请中','"+m.get("msg")+"')";
        }
        logger.debug("apply:" + sql);
        return XgDao.canExecute(sql);
	}
}
