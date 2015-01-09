//上传文件到ftp

package com.dc.ftp.upload;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import com.dc.ftp.FtpCon;
import com.dc.myframe.FileUploadProgress;

public class FtpUpload extends Thread {
	private static String ip = "127.0.0.1";
	private static String username = "uploader";
	private static String password = "N7T35V2X";
	private static int port = 21;
	private static String encoding = System.getProperty("file.encoding");
	
	private String path;
	private File file;
	
	//当前已读字节数
	public FileUploadProgress fileUploadProgress;
	
	/*
	 * 构造方法
	 */
	public FtpUpload(File file, FileUploadProgress fileUploadProgress) {
		this.file = file;
		this.fileUploadProgress = fileUploadProgress;
	}
	
	/*
	 * 设置路径的方法
	 */
	private void setPath() {
		Calendar cal = Calendar.getInstance();
		int year, month, day;
		year = cal.get(Calendar.YEAR);
		month = cal.get(Calendar.MONTH)+1;
		day = cal.get(Calendar.DAY_OF_MONTH);
		path = year+"-"+month+"-"+day;
		//寻找路径
		FTPClient ftp = new FTPClient();
		
		try {
			ftp.connect(ip, port);
			ftp.login(username, password);
			FTPFile testDir = FtpCon.ftpget(path);
			
			if (testDir == null) {
				ftp.makeDirectory(path);
			}
			ftp.logout();
			ftp.disconnect();
			ftp = null;
		} catch (SocketException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}

	}
	

	/*
	 * 上传文件的方法
	 */
	
	public void run () {
		FTPClient ftp = new FTPClient();
		try {
			ftp.connect(ip, port);
			ftp.login(username, password);

			//设置路径
			setPath();
			ftp.changeWorkingDirectory(path);
			ftp.setControlEncoding(encoding);
			ftp.setFileType(FTP.BINARY_FILE_TYPE);
			
			InputStream input = new FileInputStream(file);
			//设置缓冲区大小
			byte[] buffer = new byte[1024];
			
			int n = -1;
			int trans = 0;
			
			String name = makeName(file.getName());

			FileUploadProgress.name = name;
			OutputStream output = ftp.storeFileStream(new String(name.getBytes("utf-8"),"iso-8859-1"));
			
			
			while((n = input.read(buffer,0,1024)) != -1) {
				output.write(buffer, 0, n);
				trans += n;
				fileUploadProgress.update(trans);
				
			}
			
			input.close();
			output.flush();
			output.close();
			
			ftp.logout();
			ftp.disconnect();
			ftp = null;
			
		} catch (SocketException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
	}
	
	/*
	 * 将1024字节的byteArray化为256个4字节的int
	 * @param b
	 * @return
	 */
	
	/*
	 * private static int[] byte2int(byte[] b) {
		byte[][] a = new byte[256][4];
		int[] result = new int[256];
		for (int j = 0; j < a.length; j++) {
			for (int k = 0; k < a[j].length; k++) {
				a[j][k] = b[4*j+k];
			}
		}
		for (int k = 0; k < a.length; k++) {     //从b的尾部(即int值的低位)开始copy数据  
			byte[] x = new byte[4];
			byte[] y = a[k];
			int i = x.length - 1, j = y.length - 1;
			for (; i <= 0; i--, j--) {
				if (j >= 0) {
					x[i] = y[j];
				} else {
					x[i] = 0;                    //如果b.length不足4,则将高位补0  
				}
			}
			int v0 = (x[0] & 0xff) << 24;//&0xff将byte值无差异转成int,避免Java自动类型提升后,会保留高位的符号位  
		    int v1 = (x[1] & 0xff) << 16;  
		    int v2 = (x[2] & 0xff) << 8;  
		    int v3 = (x[3] & 0xff) ;  
		    result[k] = v0+v1+v2+v3;
		}
		
		return result;
	}
	 */
	
	
	/*
	 * 生成文件名的方法
	 * @param fileName
	 * return 
	 */
	public static String makeName(String fileName) {
		SimpleDateFormat df = new SimpleDateFormat("HHmmss");
		return df.format(new Date())+"_"+fileName;
	}
}
