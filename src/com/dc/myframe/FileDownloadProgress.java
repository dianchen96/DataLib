//���������ļ����ȵ���

package com.dc.myframe;

import java.awt.BorderLayout;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.WindowConstants;

import com.dc.MainFrame;

public class FileDownloadProgress extends JFrame implements Runnable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2542656458393965835L;
	
	private long doneLength;
	private long totalLength;
	
	private JProgressBar progressBar;
	
	/*
	 * ���췽��
	 */
	public FileDownloadProgress(FileProgressStatus fileProgressStatus, JPanel mother) {
		super("�ļ�����");
		setIconImage(Toolkit.getDefaultToolkit().getImage(MainFrame.class.getResource("/image/save_p.png")));
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		progressBar.setIndeterminate(false);
		
		setSize(200, 100);
		setLocation(mother.getX()+550, mother.getY()+300);
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setLayout(new BorderLayout());
		add(progressBar);
		setVisible(true);
		
		this.doneLength = fileProgressStatus.getDoneLength();
		this.totalLength = fileProgressStatus.getTotalLength();
	}
	
	/*
	 * ����������ֽ���
	 */
	public void update(long doneLength) {
		this.doneLength = doneLength;
		
	}
	
	/*
	 * �������ֽ��� 
	 */
	public long getTotalLength() {
		return totalLength;
	}
	
	public void setTotalLength(long totalLength) {
		this.totalLength = totalLength;
	}
	
	@Override
	public void run() {
		// TODO �Զ����ɵķ������
		while (doneLength != totalLength) {
			int percentage = Integer.valueOf(String.valueOf(doneLength * 100/ totalLength));
			progressBar.setValue(percentage);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}
		}
		
		int percentage;
		try {
			percentage = Integer.valueOf(String.valueOf(doneLength * 100 / totalLength));
		} catch (Exception e){
			percentage = 100;
		}
		
		progressBar.setValue(percentage);
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		
		if (totalLength!=0) {
			JOptionPane.showMessageDialog(this, "�ļ����سɹ�!");
		}
		
		setVisible(false);
	}
	
}
