//�ϴ��ļ���ftp

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
	
	//��ǰ�Ѷ��ֽ���
	public FileUploadProgress fileUploadProgress;
	
	/*
	 * ���췽��
	 */
	public FtpUpload(File file, FileUploadProgress fileUploadProgress) {
		this.file = file;
		this.fileUploadProgress = fileUploadProgress;
	}
	
	/*
	 * ����·���ķ���
	 */
	private void setPath() {
		Calendar cal = Calendar.getInstance();
		int year, month, day;
		year = cal.get(Calendar.YEAR);
		month = cal.get(Calendar.MONTH)+1;
		day = cal.get(Calendar.DAY_OF_MONTH);
		path = year+"-"+month+"-"+day;
		//Ѱ��·��
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
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		} catch (IOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}

	}
	

	/*
	 * �ϴ��ļ��ķ���
	 */
	
	public void run () {
		FTPClient ftp = new FTPClient();
		try {
			ftp.connect(ip, port);
			ftp.login(username, password);

			//����·��
			setPath();
			ftp.changeWorkingDirectory(path);
			ftp.setControlEncoding(encoding);
			ftp.setFileType(FTP.BINARY_FILE_TYPE);
			
			InputStream input = new FileInputStream(file);
			//���û�������С
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
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		} catch (IOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		
	}
	
	/*
	 * ��1024�ֽڵ�byteArray��Ϊ256��4�ֽڵ�int
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
		for (int k = 0; k < a.length; k++) {     //��b��β��(��intֵ�ĵ�λ)��ʼcopy����  
			byte[] x = new byte[4];
			byte[] y = a[k];
			int i = x.length - 1, j = y.length - 1;
			for (; i <= 0; i--, j--) {
				if (j >= 0) {
					x[i] = y[j];
				} else {
					x[i] = 0;                    //���b.length����4,�򽫸�λ��0  
				}
			}
			int v0 = (x[0] & 0xff) << 24;//&0xff��byteֵ�޲���ת��int,����Java�Զ�����������,�ᱣ����λ�ķ���λ  
		    int v1 = (x[1] & 0xff) << 16;  
		    int v2 = (x[2] & 0xff) << 8;  
		    int v3 = (x[3] & 0xff) ;  
		    result[k] = v0+v1+v2+v3;
		}
		
		return result;
	}
	 */
	
	
	/*
	 * �����ļ����ķ���
	 * @param fileName
	 * return 
	 */
	public static String makeName(String fileName) {
		SimpleDateFormat df = new SimpleDateFormat("HHmmss");
		return df.format(new Date())+"_"+fileName;
	}
}
