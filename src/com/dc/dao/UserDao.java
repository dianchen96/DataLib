//专门用于处理用户和与Frame兼容的Dao类

package com.dc.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {//定义数据驱动类的名称
	static final String ClassName = "net.sourceforge.jtds.jdbc.Driver";
	//定义访问数据库的URL
	static final String Url = "jdbc:jtds:sqlserver://127.0.0.1;;DatabaseName=db_manhoursys";
	//定义访问数据库用户名和密码
	static final String User = "sa";
	static final String Pwd = "123";
	//声明连接对象
	public static Connection con = null;
	
	static {
		try {
			if (con == null) {
				Class.forName(ClassName).newInstance();
				con = DriverManager.getConnection(Url, User, Pwd);
			}
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	private UserDao() {
		
	}
	
	//判断登录的方法
	public static boolean getLogin(String id, String pass) throws SQLException {
		boolean result = false;
		//向数据库查询
		String sql = "select * from tb_users where id = ? and pass = ?";
		PreparedStatement pst = con.prepareStatement(sql);
		pst.setString(1, id);
		pst.setString(2, pass);
		ResultSet rs = pst.executeQuery();
		if (rs.next()) {
			//账号密码正确
			result = true;
		} else {
			//账号密码不正确
		}
		return result;
	}
	//得到指定工号用户的姓名的方法
	public static String getName(String id) throws SQLException {
		String sql = "select * from tb_users where id = ?";
		PreparedStatement pst = con.prepareStatement(sql);
		pst.setString(1, id);
		ResultSet rs = pst.executeQuery();
		rs.next();
		//获取姓名
		return rs.getString("name");
	}
	
	//得到指定id的权限的方法
	public static int getPermission(String id) throws SQLException {
		String sql = "select * from tb_users where id = ?";
		PreparedStatement pst = con.prepareStatement(sql);
		pst.setString(1, id);
		ResultSet rs = pst.executeQuery();
		rs.next();
		return rs.getInt("permission");
	}
	
	//得到指定用户级别的方法
	public static String getRank(String id) throws SQLException {
		String sql = "select rank from tb_users where id = ?";
		PreparedStatement pst = con.prepareStatement(sql);
		pst.setString(1, id);
		ResultSet rs = pst.executeQuery();
		rs.next();
		return rs.getString("rank");
	}
	

}
