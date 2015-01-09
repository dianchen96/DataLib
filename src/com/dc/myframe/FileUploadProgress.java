//处理上传文件进度的类

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
	 * 构造方法
	 */
	
	public FileUploadProgress(FileProgressStatus fileProgressStatus, JDialog mother, File file, 
			String tags, String id) {
		super("文件上传");
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
	 * 更新已完成字节数
	 */
	public void update(long doneLength) {
		this.doneLength = doneLength;
		
	}

	@Override
	public void run() {
		// TODO 自动生成的方法存根
		while (doneLength != totalLength) {
			int percentage = Integer.valueOf(String.valueOf(doneLength * 100/ totalLength));
			progressBar.setValue(percentage);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
		
		int percentage = Integer.valueOf(String.valueOf(doneLength * 100 / totalLength));
		progressBar.setValue(percentage);
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
		setVisible(false);
		
		new Annotate(this, file, tags, id, name);
	}

	
	
	
}
