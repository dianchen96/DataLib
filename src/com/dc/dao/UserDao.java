//ר�����ڴ����û�����Frame���ݵ�Dao��

package com.dc.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {//�������������������
	static final String ClassName = "net.sourceforge.jtds.jdbc.Driver";
	//����������ݿ��URL
	static final String Url = "jdbc:jtds:sqlserver://127.0.0.1;;DatabaseName=db_manhoursys";
	//����������ݿ��û���������
	static final String User = "sa";
	static final String Pwd = "123";
	//�������Ӷ���
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
	
	//�жϵ�¼�ķ���
	public static boolean getLogin(String id, String pass) throws SQLException {
		boolean result = false;
		//�����ݿ��ѯ
		String sql = "select * from tb_users where id = ? and pass = ?";
		PreparedStatement pst = con.prepareStatement(sql);
		pst.setString(1, id);
		pst.setString(2, pass);
		ResultSet rs = pst.executeQuery();
		if (rs.next()) {
			//�˺�������ȷ
			result = true;
		} else {
			//�˺����벻��ȷ
		}
		return result;
	}
	//�õ�ָ�������û��������ķ���
	public static String getName(String id) throws SQLException {
		String sql = "select * from tb_users where id = ?";
		PreparedStatement pst = con.prepareStatement(sql);
		pst.setString(1, id);
		ResultSet rs = pst.executeQuery();
		rs.next();
		//��ȡ����
		return rs.getString("name");
	}
	
	//�õ�ָ��id��Ȩ�޵ķ���
	public static int getPermission(String id) throws SQLException {
		String sql = "select * from tb_users where id = ?";
		PreparedStatement pst = con.prepareStatement(sql);
		pst.setString(1, id);
		ResultSet rs = pst.executeQuery();
		rs.next();
		return rs.getInt("permission");
	}
	
	//�õ�ָ���û�����ķ���
	public static String getRank(String id) throws SQLException {
		String sql = "select rank from tb_users where id = ?";
		PreparedStatement pst = con.prepareStatement(sql);
		pst.setString(1, id);
		ResultSet rs = pst.executeQuery();
		rs.next();
		return rs.getString("rank");
	}
	

}
