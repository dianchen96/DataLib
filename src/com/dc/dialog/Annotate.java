//为文件标识说明的对话框

package com.dc.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;















import com.dc.MainFrame;
import com.dc.dao.Dao;
import com.dc.dao.UserDao;
import com.dc.model.PreDtlFile;
import com.dc.myframe.panel.ShadePanelH;
import com.dc.myframe.table.FileInfo;

public class Annotate extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7863003696873918960L;
	
	public Annotate(JFrame mother, final File uploadedfile, final String tags, final String id, final String name) {
		super(mother, true);
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setTitle("完成上传");
		setSize(350, 350);
		setResizable(false);
		setLayout(null);
		setLocation(mother.getX()-85, mother.getY()-120);
		
		//设置标题
		JLabel title = new JLabel("添加文件注释");
		title.setFont(new Font("Microsoft YaHei UI",0,12));
		title.setIcon(new ImageIcon(MainFrame.class.getResource("/image/icon_purple.gif")));
		ShadePanelH titlePanel = new ShadePanelH();
		titlePanel.setBounds(0, 0, 350, 20);
		titlePanel.setLayout(new BorderLayout());
		titlePanel.add(BorderLayout.NORTH,title);
		titlePanel.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.LIGHT_GRAY));
		add(titlePanel);
		//设置导航
		JPanel guide = new JPanel();
		guide.setBounds(2, 23, 340, 42);
		guide.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY));
		guide.setLayout(null);
		JTextArea words = new JTextArea();
		words.setText("您的文件已上传成功。为了您的文件能更快通过审核，建议您为文件添加注释。");
		words.setEditable(false);
		words.setBounds(10,3,317,40);
		words.setLineWrap(true);
		words.setForeground(Color.GRAY);
		words.setBackground(new Color(214,217,223));
		words.setBorder(new LineBorder(null, 0));
		words.setFont(new Font("Microsoft YaHei", 0, 12));
		guide.add(words,BorderLayout.WEST);
		add(guide);
		
		JPanel typeP = new JPanel();
		final JTextArea type = new JTextArea();
		typeP.setBounds(2,69,340,62);
		typeP.setBackground(new Color(217,224,236));
		type.setToolTipText("为文件添加注释");
		type.setBounds(10,3,317,60);
		type.setLineWrap(true);
		type.setBackground(new Color(217,224,236));
		type.setBorder(new LineBorder(null, 0));
		typeP.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY));
		type.setFont(new Font("Microsoft YaHei", 0, 12));
		type.setLayout(null);
		typeP.add(type);
		add(typeP);
		
		//设置文件筐标题
		JLabel titleF = new JLabel("文件信息");
		titleF.setFont(new Font("Microsoft YaHei UI",0,12));
		titleF.setIcon(new ImageIcon(MainFrame.class.getResource("/image/icon_green.gif")));
		ShadePanelH titleFPanel = new ShadePanelH();
		titleFPanel.setBounds(0, 135, 350, 20);
		titleFPanel.setLayout(new BorderLayout());
		titleFPanel.add(BorderLayout.NORTH,titleF);
		titleFPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.LIGHT_GRAY));
		add(titleFPanel);
		
		
		
		JPanel fileInfo = new JPanel();
		fileInfo.setLayout(new FlowLayout(FlowLayout.LEFT, 5,5));
		JScrollPane fileScroll = new JScrollPane(fileInfo);
		fileScroll.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY));
		fileScroll.setBounds(2, 157, 340, 120);
		FileInfo flf = new FileInfo();
		JLabel[] infos = flf.getFileInfos(uploadedfile);
		for (int i = 0; i < infos.length; i++) {
			JButton eachbutt = new JButton();
			eachbutt.setEnabled(false);
			eachbutt.setText(infos[i].getText());
			fileInfo.add(eachbutt);
		}
		fileInfo.setPreferredSize(new Dimension(300, infos.length * 40));
		add(fileScroll);
		
		//设置按钮
		JButton confirm = new JButton("确认");
		confirm.setFont(new Font("Microsoft YaHei UI",0,12));
		confirm.setBounds(270, 285, 65, 30);
		confirm.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String fileName = name;
				String fileType = fileName.substring(fileName.indexOf(".")+1);
				
				String fileDate = getDate();
				long fileSize = uploadedfile.length();
				String upLoader = null;
				try {
					upLoader = id+"-"+UserDao.getName(id);
				} catch (SQLException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				} 
				String annoTate = type.getText();
				PreDtlFile file = new PreDtlFile(fileName, fileType, fileSize, fileDate, upLoader, tags, annoTate);
				try {
					Dao.writePreDTLB(file);
					JLabel message = new JLabel("注释添加成功，请耐心等待审核。");
					message.setFont(new Font("Microsoft YaHei UI", 0, 12));		
					JOptionPane.showMessageDialog(null, message, "消息", JOptionPane.INFORMATION_MESSAGE);
				} catch (SQLException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
				setVisible(false);
			}
			
		});
		add(confirm);
		
		
		setVisible(true);
	}
	
	private String getDate() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		return df.format(new Date());
	}
}
