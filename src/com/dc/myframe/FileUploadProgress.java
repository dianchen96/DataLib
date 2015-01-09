//�����ϴ��ļ����ȵ���

package com.dc.myframe;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.io.File;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.WindowConstants;

import com.dc.MainFrame;
import com.dc.dialog.Annotate;

public class FileUploadProgress extends JFrame implements Runnable {


	/**
	 * 
	 */
	private static final long serialVersionUID = -3889129565007716207L;
	
	private long doneLength;
	private long totalLength;
	
	private JProgressBar progressBar;
	private File file;
	private String tags;
	private String id;
	public static String name;
	
	/* 
	 * ���췽��
	 */
	
	public FileUploadProgress(FileProgressStatus fileProgressStatus, JDialog mother, File file, 
			String tags, String id) {
		super("�ļ��ϴ�");
		setIconImage(Toolkit.getDefaultToolkit().getImage(MainFrame.class.getResource("/image/open_p.png")));
		setLayout(null);
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		progressBar.setIndeterminate(false);
		progressBar.setBounds(5, 20, 180, 25);
	    
		setSize(200, 100);
		setLocation(mother.getX()+100, mother.getY()+250);
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setLayout(new BorderLayout());
		add(progressBar);
		setVisible(true);
		
		this.doneLength = fileProgressStatus.getDoneLength();
		this.totalLength = fileProgressStatus.getTotalLength();
		this.file = file;
		this.tags = tags;
		this.id = id;
	}
	
	/*
	 * ����������ֽ���
	 */
	public void update(long doneLength) {
		this.doneLength = doneLength;
		
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
		
		int percentage = Integer.valueOf(String.valueOf(doneLength * 100 / totalLength));
		progressBar.setValue(percentage);
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		
		setVisible(false);
		
		new Annotate(this, file, tags, id, name);
	}

	
	
	
}
