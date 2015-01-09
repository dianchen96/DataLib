package com.dc.ftp;

import java.util.Vector;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

public class FtpUpload {

	static private String ip = "127.0.0.1";
	static private String username = "uploader";
	static private String password = "N7T35V2X";
	static private int port = 21;
	
	/*
	 * 返回ftp上当前文件夹的所有文件
	 * @return ftpFile[]
	 */
	
	public static FTPFile[] ftpdir(String pathname) {
		FTPFile[] ftpFiles = null;
		FTPFile[] result = null;
		FTPClient ftp = new FTPClient();
		try {
			ftp.connect(ip, port);
			ftp.login(username, password);
			ftpFiles = ftp.listFiles(pathname);
			//剔除.和..
			Vector<FTPFile> pre_result = new Vector<FTPFile>();
			for (int i = 0; i < ftpFiles.length; i++) {
				if (ftpFiles[i].getName().equals(".")||ftpFiles[i].getName().equals("..")) {
					
				} else{
					pre_result.add(ftpFiles[i]);
				}
			}
			result = new FTPFile[pre_result.size()];
			for (int i = 0; i < result.length; i++) {
				result[i] = pre_result.get(i);
			}
			ftp.logout();
			ftp.disconnect();
			ftp = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	/*
	 * 返回ftp上指定路径的文件
	 * @return ftpFile
	 */
	public static FTPFile ftpget(String pathname) {
		FTPFile[] ftpFiles = null;
		FTPFile result = null;
		try {
			//拆分路径
			String path;
			String filename;
			if (pathname.indexOf("/") > -1) {
				path = pathname.substring(0, pathname.lastIndexOf("/"));
				filename = pathname.substring(pathname.lastIndexOf("/")+1);
			} else {
				path = "";
				filename = pathname;
			}
			
			ftpFiles = ftpdir(path);
			
			for (int i = 0; i < ftpFiles.length; i++) {
				if (ftpFiles[i].getName().equals(filename)) {
					result = ftpFiles[i];
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	/*
	 * 返回ftp上的文件
	 * @param fileName
	 * @return ftpFile
	 */
	
}
