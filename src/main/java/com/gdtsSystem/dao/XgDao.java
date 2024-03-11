package com.gdtsSystem.dao;

import com.gdtsSystem.entity.*;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class XgDao {
	
//    private static String driver;
//    private static String url;
//    private static String user;
//    private static String pass;

//    static {
//        Properties prop = new Properties();
//        String path = XgDao.class.getProtectionDomain().getCodeSource().getLocation().getPath()
//                + "com/LibrarySystem/dao/db.properties";
//
//        //System.out.println(path);
//        try (FileReader fr = new FileReader(path)) {
//            prop.load(fr);
//            driver = prop.getProperty("oracle.jdbc.driver.OracleDriver");
//            url = prop.getProperty("jdbc:oracle:thin:@192.168.123.100:1521:orcl");
//            user = prop.getProperty("scott");
//            pass = prop.getProperty("a1");
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//    }

    public static boolean isExist(String sql) {
        //System.out.println(sql);
        try (Connection con = canConnect()) {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            return rs.next();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return false;
    }

    public static boolean isExist(String sql, Object params[]) {
        System.out.println(sql);
        try (Connection con = canConnect()) {
            PreparedStatement pst = con.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                pst.setObject(i + 1, params[i]);
            }
            ResultSet rs = pst.executeQuery();
            return rs.next();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return false;
    }

    public static boolean canExecute(String sql) {
        try (Connection con = canConnect()) {
            Statement st = con.createStatement();
            return st.executeUpdate(sql) > 0;
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return false;
    }

    public static Connection canConnect() {
    	
        //System.out.println("hello1?");
        Connection connection = null;
        try {
            InitialContext context = new InitialContext();
            DataSource ds = (DataSource) context.lookup("java:comp/env/jdbc");
            connection = ds.getConnection();

//          Class.forName("oracle.jdbc.driver.OracleDriver");
//          connection = DriverManager.getConnection("jdbc:oracle:thin:@192.168.123.100:1521:orcl", "scott", "a1");
          

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return connection;
    }

	public static ArrayList<StudentInfo> getStudentInfos(String sql) {
        ArrayList<StudentInfo> lstStudentInfo = new ArrayList<>(10);
        try (Connection con = canConnect()) {
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                StudentInfo studentInfo = new StudentInfo();
                studentInfo.setSid(rs.getString("sid"));
                studentInfo.setSname(rs.getString("sname"));
                studentInfo.setSsex(rs.getString("ssex"));
                studentInfo.setSage(rs.getInt("sage"));
                studentInfo.setSmail(rs.getString("smail"));
                lstStudentInfo.add(studentInfo);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return lstStudentInfo;
    }

    public static ArrayList<TeacherInfo> getTeacherInfos(String sql) {
        ArrayList<TeacherInfo> lstTeacherInfo = new ArrayList<>(10);
        try (Connection con = canConnect()) {
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                TeacherInfo teacherInfo = new TeacherInfo();
                teacherInfo.setTid(rs.getString("tid"));
                teacherInfo.setTname(rs.getString("tname"));
                teacherInfo.setTsex(rs.getString("tsex"));
                teacherInfo.setTage(rs.getInt("tage"));
                teacherInfo.setTlvl(rs.getString("tlvl"));
                teacherInfo.setTmail(rs.getString("tmail"));
                lstTeacherInfo.add(teacherInfo);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return lstTeacherInfo;
    }

    public static ArrayList<ApplyInfo> getApplyInfos(String sql) {
        ArrayList<ApplyInfo> lstApplyInfos = new ArrayList<>(20);
        try (Connection con = canConnect()) {
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                ApplyInfo applyInfo = new ApplyInfo();
                applyInfo.setApplyid(rs.getString("applyid"));
                applyInfo.setSid(rs.getString("sid"));
                applyInfo.setGdtid(rs.getString("gdtid"));
                applyInfo.setGdtname(rs.getString("gdtname"));
                applyInfo.setApplytime(rs.getTimestamp("applytime"));
                applyInfo.setReplytime(rs.getTimestamp("replytime"));
                applyInfo.setStatus(rs.getString("status"));
                lstApplyInfos.add(applyInfo);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return lstApplyInfos;
    }

    public static ArrayList<GdtInfo> getGdtInfos(String sql) {
        ArrayList<GdtInfo> lstGdtInfos = new ArrayList<>(20);
        try (Connection con = canConnect()) {
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                GdtInfo gdtInfo = new GdtInfo();
                gdtInfo.setGdtid(rs.getString("gdtid"));
                gdtInfo.setGdtname(rs.getString("gdtname"));
                gdtInfo.setGdttype(rs.getInt("gdttype"));
                gdtInfo.setGdtreq(rs.getString("gdtreq"));
                gdtInfo.setGdtintro(rs.getString("gdtintro"));
                gdtInfo.setTid(rs.getString("tid"));
                gdtInfo.setSid(rs.getString("sid"));
                lstGdtInfos.add(gdtInfo);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return lstGdtInfos;
    }

    public static ArrayList<GdtType> getGdtTypes(String sql) {
        ArrayList<GdtType> lstgdtTypes = new ArrayList<>(20);
        try (Connection con = canConnect()) {
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                GdtType gdtType = new GdtType();
                gdtType.setTypeid(rs.getInt("typeid"));
                gdtType.setTypename(rs.getString("typename"));
                lstgdtTypes.add(gdtType);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return lstgdtTypes;
    }

    public static ArrayList<GdtInfo> getGdtInfoInfos(String sql) {
        ArrayList<GdtInfo> lstgdtInfos = new ArrayList<>(20);
        try (Connection con = canConnect()) {
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                GdtInfo gdtInfo = new GdtInfo();
                gdtInfo.setGdtid(rs.getString("gdtid"));
                gdtInfo.setGdtname(rs.getString("gdtname"));
                gdtInfo.setGdttype(rs.getInt("gdttype"));
                gdtInfo.setGdtreq(rs.getString("gdtreq"));
                gdtInfo.setGdtintro(rs.getString("gdtintro"));
                gdtInfo.setTid(rs.getString("tid"));
                gdtInfo.setSid(rs.getString("sid"));
                lstgdtInfos.add(gdtInfo);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return lstgdtInfos;
    }

    public static ArrayList getDatas(String sql) {
        ArrayList lst = new ArrayList();
        try (Connection con = canConnect()) {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            ResultSetMetaData md = rs.getMetaData();
            while (rs.next()) {
                HashMap m = new HashMap();
                for (int i = 1; i <= md.getColumnCount(); i++) {
                    String fldName = md.getColumnName(i);
                    String fldValue = rs.getString(i);
                    m.put(fldName.toLowerCase(), fldValue);
                }
                lst.add(m);
            }
        } catch (Exception e) {
        }

        return lst;
    }

    public static ArrayList getDatas(String sql,Object params[]) {
        ArrayList lst = new ArrayList();
        try (Connection con = canConnect()) {
            Statement pst = con.createStatement();
            ResultSet rs = pst.executeQuery(sql);
            ResultSetMetaData md = rs.getMetaData();
            while (rs.next()) {
                HashMap m = new HashMap();
                for (int i = 1; i <= md.getColumnCount(); i++) {
                    String fldName = md.getColumnName(i);
                    String fldValue = rs.getString(i);
                    m.put(fldName.toLowerCase(), fldValue);
                }
                lst.add(m);
            }
        } catch (Exception e) {
        }

        return lst;
    }

    public static int getCount(String sql) {
        //System.out.println(sql);
        try (Connection con = canConnect()) {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return 0;
    }
}