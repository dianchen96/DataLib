package com.dc.ftp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.JOptionPane;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

public class FtpExamine {
	static private String ip = "127.0.0.1";
	static private String username = "admin";
	static private String password = "r55dNtfX";
	static private int port = 21;
	private static String encoding = System.getProperty("file.encoding");
	
	static private String huanchong = "p_data";
	
	
	
	/*
	 * 移动文件到指定路径
	 */
	public static void moveFileTo(String fileName, String path) {
		String filePath = null;
		String file = null;
		FTPClient ftp = new FTPClient();
		try {
			ftp.connect(ip, port);
			ftp.login(username, password);
			ftp.changeWorkingDirectory(huanchong);
			FTPFile[] directorys = ftp.listFiles();
			boolean flag = true;
			for (int i = 0; i < directorys.length; i++) {
				FTPFile[] files = ftp.listFiles(directorys[i].getName());
				if (flag) {
					for (int j = 0; j < files.length; j++) {
						String name = new String(files[j].getName().getBytes("iso-8859-1"),"utf-8");
						if (name.equals(fileName)) {
							filePath = huanchong+"/"+directorys[i].getName()+"/"+files[j].getName();
							file = files[j].getName();
							flag = false;
							break;
						}
					}
				}
			}
			ftp.changeToParentDirectory();
			ftp.rename(filePath, "data"+path+"/"+file);
			
			ftp.logout();
			ftp.disconnect();
			ftp = null;
			
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
		
	}
	
	//返回文件
	public static FTPFile getFile(String fileName) {
		FTPFile file = null;
		FTPClient ftp = new FTPClient();
		try {
			ftp.connect(ip, port);
			ftp.login(username, password);
			ftp.setControlEncoding(encoding);
			ftp.setFileType(FTP.BINARY_FILE_TYPE);
			ftp.changeWorkingDirectory(huanchong);
			FTPFile[] directorys = ftp.listFiles();
			boolean flag = true;
			for (int i = 0; i < directorys.length; i++) {
				FTPFile[] files = ftp.listFiles(directorys[i].getName());
				if (flag) {
					for (int j = 0; j < files.length; j++) {
						String name = new String(files[j].getName().getBytes("iso-8859-1"),"utf-8");
						if (name.equals(fileName)) {
							file = files[j];
							flag = false;
							break;
						}
					}
				}
			}
			ftp.logout();
			ftp.disconnect();
			ftp = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return file;
	}
	
	/*
	 * 下载文件
	 */
	public static void seeFile(String[] fileNames, String savePath) {
		FTPClient ftp;
		try {

			
			
			
			InputStream input;
			OutputStream output;
			byte[] buffer = new byte[1024];
			int n = -1;
			for (int i = 0; i < fileNames.length; i++) {
				ftp = new FTPClient();
				ftp.connect(ip, port);
				ftp.login(username, password);
				ftp.changeWorkingDirectory(huanchong);
				
				FTPFile[] dirs = ftp.listDirectories();
				for (int j = 0; j < dirs.length; j++) {
					if (!dirs[j].getName().equals(".") && !dirs[j].getName().equals("..")) {
						ftp.changeWorkingDirectory(dirs[j].getName());
						FTPFile[] files = ftp.listFiles();
						for (int k = 0; k < files.length; k++) {
							String name = new String(files[k].getName().getBytes("iso-8859-1"),"utf-8");

							if (name.equals(fileNames[i])) {
								ftp.setControlEncoding(encoding);
								ftp.setFileType(FTP.BINARY_FILE_TYPE);
								ftp.changeWorkingDirectory(dirs[j].getName());
								File localfile = new File(savePath+"/"+fileNames[i]);
								input = ftp.retrieveFileStream(new String((fileNames[i]).getBytes("utf-8"),"iso-8859-1"));
								output = new FileOutputStream(localfile);
								
								while ((n = input.read(buffer, 0, 1024)) != -1) {
									output.write(buffer, 0, n);
								}
								
								input.close();
								output.flush();
								output.close();
							    break;
							}
						}
						ftp.changeToParentDirectory();
					}
					
				}
				
				ftp.logout();
				ftp.disconnect();
				ftp = null;
			}
			JOptionPane.showMessageDialog(null, fileNames.length+"个待审核文件下载成功");

			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
