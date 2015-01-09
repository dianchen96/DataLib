package com.dc.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Vector;

import com.dc.model.DtlFile;
import com.dc.model.PreDtlFile;
import com.dc.model.Tag;

public class Dao {
	static final String ClassName = "net.sourceforge.jtds.jdbc.Driver";
	//����������ݿ��URL
	static final String Url = "jdbc:jtds:sqlserver://127.0.0.1:1433;;DatabaseName=db_datalib";
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
	private Dao() {
		
	}
	
	//д���±�ǩ�ķ���
	public static void writeNewTag(Tag tag) throws SQLException {

		//��ȡ��ǩ����
		String tag_name = tag.getTagName();
		String tag_values = tag.getTagValues();
		
		String sql = "insert into tb_tags values(?,?)";
		PreparedStatement pst = con.prepareStatement(sql);
		pst.setString(1, tag_name);
		pst.setString(2, tag_values);
		pst.executeUpdate();
	}
	
	//��ȡ��ǩ�ķ���
	public static Tag[] getTags() throws SQLException {
		String sql = "select * from tb_tags";
		PreparedStatement pst = con.prepareStatement(sql);
		ResultSet rs = pst.executeQuery();
		
		Vector<Tag> p_result = new Vector<Tag>();
		Tag[] result;

		while (rs.next()){
			String tag_name = rs.getString("tag_name");
			String tag_values = rs.getString("tag_values");
			String[] p_eachTV = tag_values.split(";");
			String[] eachTV = Arrays.copyOfRange(p_eachTV, 1, p_eachTV.length);
			p_result.add(new Tag(tag_name, eachTV));
		}
		//��vector��������
		result = new Tag[p_result.size()];
		for (int i = 0; i < result.length; i++) {
			result[i] = p_result.get(i);
		}
		
		return result;
	}
	
	//���һ����ǩ������ֵ
	public static String[] getTagValue(String tagName) throws SQLException {
		String sql = "select tag_values from tb_tags where tag_name = ?";
		PreparedStatement pst = con.prepareStatement(sql);
		pst.setString(1, tagName);
		ResultSet rs = pst.executeQuery();
		rs.next();
		String tag_values = rs.getString("tag_values");
		String[] p_eachTV = tag_values.split(";");
		String[] result = Arrays.copyOfRange(p_eachTV, 1, p_eachTV.length);
		
		return result;
	}
	
	public static void writeTag(Tag tag) throws SQLException {
		String sql = "insert into tb_tags values(?,?)";
		PreparedStatement pst = con.prepareStatement(sql);
		pst.setString(1, tag.getTagName());
		pst.setString(2, tag.getTagValues());
		pst.executeUpdate();
	}
	
	//ɾ��һ����ǩ
	public static void deleteTag(String tag_name) throws SQLException {
		String sql = "delete from tb_tags where tag_name = ?";
		PreparedStatement pst = con.prepareStatement(sql);
		pst.setString(1, tag_name);
		pst.executeUpdate();
	}
	
	//����һ����ǩ
	public static void updateTag(String tagName, String tagValue) throws SQLException {
		String sql = "update tb_tags set tag_values = ? where tag_name = ?";
		PreparedStatement pst = con.prepareStatement(sql);
		pst.setString(1, tagValue);
		pst.setString(2, tagName);
		
		pst.executeUpdate();
	}
	
	//д����ʱ������Ϣģ�͵ķ���
	public static void writePreDTLB(PreDtlFile file) throws SQLException {
		String fileName = file.fileName;
		String fileType = file.fileType;
		long fileSize = file.fileSize;
		String fileDate = file.fileDate;
		String upLoader = file.upLoader;
		String tagBrand = file.tagBrand;
		String annoTate = file.annoTate;
		
		String sql = "insert into tb_preDTLB values(?,?,?,?,?,?,?)";
		PreparedStatement pst = con.prepareStatement(sql);
		pst.setString(1, fileName);
		pst.setString(2, fileType);
		pst.setLong(3, fileSize);
		pst.setString(4, fileDate);
		pst.setString(5, upLoader);
		pst.setString(6, tagBrand);
		pst.setString(7, annoTate);
		
		pst.executeUpdate();
	}
	
	/*
	 * �õ�һ���û������ؼ�¼
	 */
	public static Object[][] getDownHis(String username) throws SQLException {
		String sql = "select * from tb_downhis where downLoader = ?";
		PreparedStatement pst = con.prepareStatement(sql);
		pst.setString(1, username);
		ResultSet rs = pst.executeQuery();
		Vector<String[]> p_result = new Vector<String[]> ();
		
		while(rs.next()) {
			String[] thisrow = new String[2];
			thisrow[0] = rs.getString("fileName");
			thisrow[1] = rs.getString("downloadDate");
			p_result.add(thisrow);
		}
		Object[][] result = new Object[p_result.size()][2];
		
		for (int i = 0; i < p_result.size(); i++) {
			result[i] = p_result.get(i);
		}
		
		return result;
	}
	
	//����һ���ļ�����Ϣ
	public static Object[][] getPreDtlFile(String name) throws SQLException {
		String sql = "select * from tb_preDTLB where fileName = ?";
		PreparedStatement pst = con.prepareStatement(sql);
		pst.setString(1, name);
		ResultSet rs = pst.executeQuery();
		Object[][] result = new Object[5][2];
		String[] names = {"�ļ���","�ļ�����","�ļ���С","�ϴ�ʱ��","�ϴ��û�"};
		String[] names_en = {"fileName","fileType","fileSize","fileDate","upLoader"};
		
		if (rs.next()) {
			for (int i = 0; i < result.length; i++) {
				result[i][0] = names[i];
				result[i][1] = rs.getString(names_en[i]);
			}
		} else {
			for (int i = 0; i < result.length; i++) {
				result[i][0] = names[i];
				result[i][1] = "";
			}
		}
		
		return result;
	}
	
	//���һ����ʱ�ļ��Ĵ�С
	public static long getPreDtlLength(String name) throws SQLException {
		String sql = "select fileSize from tb_preDTLB where fileName = ?";
		PreparedStatement pst = con.prepareStatement(sql);
		pst.setString(1, name);
		ResultSet rs = pst.executeQuery();
		rs.next();
		return rs.getLong("fileSize");
	}
	
	//ɾ��һ���ļ�����ʱ�ļ���Ϣ
		public static void deletePreDtlFile(String name) throws SQLException {
			String sql = "delete from tb_preDTLB where fileName = ?";
			PreparedStatement pst = con.prepareStatement(sql);
			pst.setString(1, name);
			pst.executeUpdate();
		}
		
		
		
	//����һ���ļ��ı�ǩ
	public static String[] getFileTags(String fileName) throws SQLException {
		String sql = "select * from tb_preDTLB where fileName = ?";
		PreparedStatement pst = con.prepareStatement(sql);
		pst.setString(1, fileName);
		ResultSet rs = pst.executeQuery();
		String tagBrand = "";
		String[] result;
		if (rs.next()) {
			tagBrand = rs.getString("tagBrand");
			String[] p_result = tagBrand.split(";");
			
			try {
				result = Arrays.copyOfRange(p_result, 1, p_result.length);
			} catch (Exception e) {
				result = null;
			}
			return result;
		} else {
			result = null;
			return result;
		}
	}
	//����һ���ļ��ı�ǩ
		public static String[] getFileTagsDTLB(String fileName) throws SQLException {
			String sql = "select * from tb_DTLB where fileName = ?";
			PreparedStatement pst = con.prepareStatement(sql);
			pst.setString(1, fileName);
			ResultSet rs = pst.executeQuery();
			String tagBrand = "";
			String[] result;
			if (rs.next()) {
				tagBrand = rs.getString("tagBrand");
				String[] p_result = tagBrand.split(";");
				
				try {
					result = Arrays.copyOfRange(p_result, 1, p_result.length);
				} catch (Exception e) {
					result = null;
				}
				return result;
			} else {
				result = null;
				return result;
			}
		}
	
	
	
	//����һ���ļ���ע��
	public static String getAnnotate(String fileName) throws SQLException {
		String sql = "select annoTate from tb_preDTLB where fileName = ?";
		PreparedStatement pst = con.prepareStatement(sql);
		pst.setString(1, fileName);
		ResultSet rs = pst.executeQuery();
		String result = "";
		if (rs.next()) {
			result = rs.getString("annoTate");
			return result;
		} else {
			return result;
		}
		
	}
	
	//����һ���ļ���ע��
	public static String getAnnotateDTLB(String fileName) throws SQLException {
		String sql = "select annoTate from tb_DTLB where fileName = ?";
		PreparedStatement pst = con.prepareStatement(sql);
		pst.setString(1, fileName);
		ResultSet rs = pst.executeQuery();
		String result = "";
		if (rs.next()) {
			result = rs.getString("annoTate");
			return result;
		} else {
			return result;
		}
		
	}
	
	
	//д����ʱ������Ϣģ�͵ķ���
	public static void writeDTLB(DtlFile file) throws SQLException {
		String fileName = file.fileName;
		String fileType = file.fileType;
		long fileSize = file.fileSize;
		String fileDate = file.fileDate;
		String filePath = file.filePath;
		String upLoader = file.upLoader;
		String tagBrand = file.tagBrand;
		String annoTate = file.annoTate;
		String downRank = file.downRank;
		
		String sql = "insert into tb_DTLB values(?,?,?,?,?,?,?,?,?)";
		PreparedStatement pst = con.prepareStatement(sql);
		pst.setString(1, fileName);
		pst.setString(2, fileType);
		pst.setLong(3, fileSize);
		pst.setString(4, fileDate);
		pst.setString(5, filePath);
		pst.setString(6, upLoader);
		pst.setString(7, tagBrand);
		pst.setString(8, annoTate);
		pst.setString(9, downRank);
		
		pst.executeUpdate();
	}
	
	/*�õ������ļ�����
	 *
	 *@return String[]
	 */
	public static String[] getAllFileType() throws SQLException {
		String sql = "select fileType from tb_DTLB";
		PreparedStatement pst = con.prepareStatement(sql);
		ResultSet rs = pst.executeQuery();
		Vector<String> p_result = new Vector<String>();
		
		while (rs.next()) {
			String type = rs.getString("fileType").toLowerCase();
			boolean flag = true;
			for (int i = 0; i < p_result.size(); i++) {
				String thistype = p_result.get(i).toLowerCase();
				if (thistype.equals(type)) {
					flag = false;
					break;
				}
			} 
			if (flag) {
				p_result.add(type);
			}
		}
		String[] result = new String[p_result.size()+1];
		result[0] = "";
		for (int i = 1; i < result.length; i++) {
			result[i] = p_result.get(i-1);
		}
		
		return result;
	}
	
	/*
	 * �õ������û�
	 * @return String[]
	 */
	public static String[] getAllUsers() throws SQLException {
		String sql = "select upLoader from tb_DTLB";
		PreparedStatement pst = con.prepareStatement(sql);
		ResultSet rs = pst.executeQuery();
		Vector<String> p_result = new Vector<String>();
		
		while (rs.next()) {
			String type = rs.getString("upLoader");
			boolean flag = true;
			for (int i = 0; i < p_result.size(); i++) {
				String thistype = p_result.get(i);
				if (thistype.equals(type)) {
					flag = false;
					break;
				}
			} 
			if (flag) {
				p_result.add(type);
			}
		}
		String[] result = new String[p_result.size()+1];
		result[0] = "";
		for (int i = 1; i < result.length; i++) {
			result[i] = p_result.get(i-1);
		}
		
		return result;
	}
	
	/*
	 * �õ���������ʷ���û�
	 */
	public static String[] getHisUsers() throws SQLException {
		String sql = "select downLoader from tb_downhis";
		PreparedStatement pst = con.prepareStatement(sql);
		ResultSet rs = pst.executeQuery();
		Vector<String> p_result = new Vector<String>();
		
		while (rs.next()) {
			String type = rs.getString("downLoader");
			boolean flag = true;
			for (int i = 0; i < p_result.size(); i++) {
				String thistype = p_result.get(i);
				if (thistype.equals(type)) {
					flag = false;
					break;
				}
			} 
			if (flag) {
				p_result.add(type);
			}
		}
		String[] result = new String[p_result.size()+1];
		result[0] = "";
		for (int i = 1; i < result.length; i++) {
			result[i] = p_result.get(i-1);
		}
		
		return result;
	}
	/*
	 * ��ǩ����
	 * @param String
	 * @return String[][]
	 */
	public static String[][] search(String[] tagString, String fileType, String upLoader) throws SQLException {
		String sql = "select *from tb_DTLB where fileType = ? and upLoader = ?";
		PreparedStatement pst;
		if (fileType.length()==0 && upLoader.length()==0) {
			sql = "select *from tb_DTLB";
			pst = con.prepareStatement(sql);
		} else if (fileType.length()==0 && !(upLoader.length()==0)) {
			sql = "select *from tb_DTLB where upLoader = ?";
			pst = con.prepareStatement(sql);
			pst.setString(1, upLoader);
		} else if (!(fileType.length()==0) && upLoader.length()==0) {
			sql = "select *from tb_DTLB where fileType = ?";
			pst = con.prepareStatement(sql);
			pst.setString(1, fileType);
		} else {
			pst = con.prepareStatement(sql);
			pst.setString(1, fileType);
			pst.setString(2, upLoader);
		}
		
		ResultSet rs = pst.executeQuery();
		
		Vector<String[]> p_result = new Vector<String[]>();
		
		while(rs.next()) {
			String tagBrand = rs.getString("tagBrand");
			String[] p_tags = tagBrand.split(";");
			
			boolean flag = true;
					
			for (int i = 0; i < tagString.length; i++) {
				boolean lflag = false;
				for (int j = 0; j < p_tags.length; j++) {
					if (tagString[i].equals(p_tags[j])) {
						lflag = true;
						break;
					}
				}
				if (!lflag) {
					flag = false;
					break;
				}
			}
			if (flag) {
				//������������
				String[] thisone = new String[5];
				thisone[0] = rs.getString("fileName");
				thisone[1] = rs.getString("fileType");
				thisone[2] = rs.getString("filesize");
				thisone[3] = rs.getString("fileDate");
				thisone[4] = rs.getString("upLoader");
				p_result.add(thisone);
			}
			
		}
		String[][] result = new String[p_result.size()][5];
		for (int i = 0; i < result.length; i++) {
			for (int j = 0; j < result[i].length; j++) {
				result[i][j] = p_result.get(i)[j];
			}
		}
		
		return result;
	}
	
	
	/*
	 * ע������
	 * @param String
	 * @return String[][]
	 */
	public static String[][] search(String annoTate, String fileType, String upLoader) throws SQLException {
		String sql = "select * from tb_DTLB where fileType = ? and upLoader = ? and annoTate like '%"+annoTate+"%'";         //�˴�����
		PreparedStatement pst;
		if (fileType.length()==0 && upLoader.length()==0) {
			sql = "select *from tb_DTLB where annoTate like '%"+annoTate+"%'";
			pst = con.prepareStatement(sql);
		} else if (fileType.length()==0 && !(upLoader.length()==0)) {
			sql = "select *from tb_DTLB where upLoader = ? and annoTate like '%"+annoTate+"%'";
			pst = con.prepareStatement(sql);
			pst.setString(1, upLoader);
		} else if (!(fileType.length()==0) && upLoader.length()==0) {
			sql = "select *from tb_DTLB where fileType = ? and annoTate like '%"+annoTate+"%'";
			pst = con.prepareStatement(sql);
			pst.setString(1, fileType);
		} else {
			pst = con.prepareStatement(sql);
			pst.setString(1, fileType);
			pst.setString(2, upLoader);
		}
		
		ResultSet rs = pst.executeQuery();
		
		Vector<String[]> p_result = new Vector<String[]> ();
		
		while (rs.next()) {
			//������������
			String[] thisone = new String[5];
			thisone[0] = rs.getString("fileName");
			thisone[1] = rs.getString("fileType");
			thisone[2] = rs.getString("filesize");
			thisone[3] = rs.getString("fileDate");
			thisone[4] = rs.getString("upLoader");
			p_result.add(thisone);
		}
		
		String[][] result = new String[p_result.size()][5];
		for (int i = 0; i < result.length; i++) {
			for (int j = 0; j < result[i].length; j++) {
				result[i][j] = p_result.get(i)[j];
			}
		}
		
		return result; 
	}
	
	/*
	 * ���ļ�������
	 */
	public static String[] search(String fileName) throws SQLException {
		String sql = "select * from tb_DTLB where fileName = ?";
		PreparedStatement pst = con.prepareStatement(sql);
		pst.setString(1, fileName);
		ResultSet rs = pst.executeQuery();
		rs.next();
		String[] result = new String[5];
		result[0] = rs.getString("fileName");
		result[1] = rs.getString("fileType");
		result[2] = rs.getString("fileSize");
		result[3] = rs.getString("fileDate");
		result[4] = rs.getString("upLoader");
		return result;
	}
	
	/*
	 * �����ļ�·��
	 */
	public static String getFilePath(String fileName) throws SQLException {
		String sql = "select filePath from tb_DTLB where fileName = ?";
		PreparedStatement pst = con.prepareStatement(sql);
		pst.setString(1, fileName);
		ResultSet rs = pst.executeQuery();
		rs.next();
		return rs.getString("filePath")+"/"+fileName;
		
	}
	
	/*
	 * д��������ʷ
	 */
	public static void writeHis(String fileName, String downLoader) throws SQLException {
		//����ʱ��
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = df.format(new Date());
		
		String sql = "insert into tb_downhis values (?,?,?)";
		PreparedStatement pst = con.prepareStatement(sql);
		pst.setString(1, fileName);
		pst.setString(2, date);
		pst.setString(3, downLoader);
		
		pst.executeUpdate();
	}
	
	//�ж��ļ�����
	public static boolean isDownloadable(String fileName, String id) throws SQLException {
		String userRank = UserDao.getRank(id);
		
		String sql = "select downRank from tb_DTLB where fileName = ?";
		PreparedStatement pst = con.prepareStatement(sql);
		pst.setString(1, fileName);
		ResultSet rs = pst.executeQuery();
		rs.next();
		String fileRank = rs.getString("downRank");
		if (fileRank.equals("��ϰ��")) {
			return true;
		} else {
			if (userRank.equals("��ϰ��")) {
				return false;
			} else {
				float userrank = Float.parseFloat(userRank);
				float filerank = Float.parseFloat(fileRank);
				return userrank >= filerank ? true:false;
			}
		}
	}
}
