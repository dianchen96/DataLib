//���ڷ����ļ���Ϣ����

package com.dc.myframe.table;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JLabel;

public class FileInfo {
	
	//���ڴ�ű�ѡ���ļ��Ĵ�С
	String fileSize = "";
	
	public JLabel[] getFileInfos(File file) {
		Vector<JLabel> infoTags = new Vector<JLabel> ();
		for (int k = 0; k < 3; k++) {
			JLabel label;
			if (k == 0) {
				label = new JLabel("�ļ�����"+file.getName());
				label.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
				label.setFont(new Font("Microsoft YaHei UI", 0, 12));
				label.setForeground(Color.DARK_GRAY);
				infoTags.add(label);
				
			} else if (k == 1) {
				JFileChooser open = new JFileChooser();
				label = new JLabel("�ļ����ͣ�"+open.getTypeDescription(file));
				label.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
				label.setFont(new Font("Microsoft YaHei UI", 0, 12));
				label.setForeground(Color.DARK_GRAY);
				infoTags.add(label);
	
			} else if (k == 2) {
				fileSize = file.length()+"B";
				label = new JLabel("�ļ���С��"+ getFileSize()+"��"+file.length()+" �ֽڣ�");
				label.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
				label.setFont(new Font("Microsoft YaHei UI", 0, 12));
				label.setForeground(Color.DARK_GRAY);
				infoTags.add(label);
				
			} else if (k == 3) {	
				long time = file.lastModified();
				String ctime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date(time));
				label = new JLabel("����ʱ�䣺"+ ctime);
				label.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
				label.setFont(new Font("Microsoft YaHei UI", 0, 12));
				label.setForeground(Color.DARK_GRAY);
				infoTags.add(label);
			}
		}
		
		JLabel[] result = new JLabel[infoTags.size()];
		for (int i = 0; i < result.length; i++) {
			result[i] = infoTags.get(i);
		}
		
		return result;
	}
	
	
	//�û���ȡ�ļ���С�ķ���
	private String getFileSize() {
				
		boolean flag = true;
		int index = fileSize.length();
		for (int i = 0; i < fileSize.length(); i++) {
			if (Character.isUpperCase(fileSize.charAt(i))) {
				index = i;
				break;
			}
		}
			
		float num = Float.parseFloat(fileSize.substring(0, index));
		String ext = fileSize.substring(index);
			
		float trynum = num / 1024;
		BigDecimal b = new BigDecimal(trynum);
		trynum = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();			
		if (trynum < 1) {
			flag = false;
			
		} 
			
	    if (flag) {
			if (ext.equals("B")) {
				ext = "KB";
			} else if (ext.equals("KB")) {
				ext = "MB";
			} else if (ext.equals("MB")) {
				ext = "GB";
			}
			fileSize = Float.toString(trynum) + ext;
			getFileSize();
		}
			
		return fileSize;
	}
	
}
