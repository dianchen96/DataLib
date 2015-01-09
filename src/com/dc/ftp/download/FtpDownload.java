package com.dc.ftp.download;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import java.sql.SQLException;

import javax.swing.JOptionPane;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import com.dc.MainFrame;
import com.dc.dao.Dao;
import com.dc.dao.UserDao;
import com.dc.myframe.FileDownloadProgress;

public class FtpDownload extends Thread{
	private static String ip = "127.0.0.1";
	private static String username = "user";
	private static String password = "4Qt7532D";
	private static int port = 21;
	private static String encoding = System.getProperty("file.encoding");
	
	public FileDownloadProgress fileDownloadProgress;
	
	String[] fileNames;
	String[] filePaths;
	String savePath;
	
	/*
	 * 构造方法
	 */
	public FtpDownload(String[] fileNames, String savePath, FileDownloadProgress fileDownloadProgress) {
		
		this.fileNames = fileNames;
		this.fileDownloadProgress = fileDownloadProgress;
		this.savePath = savePath;
		
		filePaths = new String[fileNames.length];
		for (int i = 0; i < filePaths.length; i++) {
			try {
				filePaths[i] = Dao.getFilePath(fileNames[i]);
			} catch (SQLException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
	}
	
	/*
	 * 下载文件
	 */
	public void run() {
		try {
			
			
			InputStream input;
			OutputStream output;
			
			byte[] buffer = new byte[1024];

			int trans = 0;
			
			for (int i = 0; i < fileNames.length; i++) {
				boolean ifcontinue = true;
				File localfile = new File(savePath+"/"+fileNames[i]);
				if (localfile.exists()) {
					int x = JOptionPane.showConfirmDialog(fileDownloadProgress, "名为"+fileNames[i]+"的文件已存在， 是否要覆盖该文件？","是否覆盖", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					if (x != JOptionPane.YES_OPTION) {
						long thisfilelength = localfile.length();
						fileDownloadProgress.setTotalLength(fileDownloadProgress.getTotalLength()-thisfilelength);
						ifcontinue = false;
					}
				}
				
				if (ifcontinue) {
					FTPClient ftp = new FTPClient();
					ftp.connect(ip, port);
					ftp.login(username, password);
					ftp.setControlEncoding(encoding);
					ftp.setFileType(FTP.BINARY_FILE_TYPE);
					String[] paths = filePaths[i].split("/");
					for (int j = 0; j < paths.length; j++) {
						if (!paths[j].equals("")) {
							ftp.changeWorkingDirectory(paths[j]);
						}
					}
					
					int n = -1;
					
					input = ftp.retrieveFileStream(new String(fileNames[i].getBytes("utf-8"),"iso-8859-1"));
					output = new FileOutputStream(localfile);
					
					while ((n = input.read(buffer,0,1024)) != -1) {
						
						output.write(buffer, 0, n);
						trans += n;
						fileDownloadProgress.update(trans);
						
					}
					input.close();
					output.flush();
					output.close();
					
					Dao.writeHis(fileNames[i], MainFrame.user_id+"-"+UserDao.getName(MainFrame.user_id));
					
					ftp.logout();
					ftp.disconnect();
					ftp = null;
				}
				
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
