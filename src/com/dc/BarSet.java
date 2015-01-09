//设置菜单栏和工具栏的类

package com.dc;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.border.SoftBevelBorder;

import com.dc.dao.UserDao;
import com.dc.dialog.Examine;
import com.dc.dialog.Settings;
import com.dc.dialog.Upload;
import com.dc.dialog.UserHis;
import com.dc.myframe.panel.ShadePanelH;

public class BarSet {
	public static JMenuBar setMenuBar(String id) {
		//设置菜单栏
		JMenuBar menuBar = new JMenuBar();
		//设置文件菜单
		JMenu fileMenu = new JMenu("文件（F）");                        
		fileMenu.setFont(new Font("Microsoft YaHei UI", 0, 12));
		fileMenu.setMnemonic('F');
		menuBar.add(fileMenu);
		
		JMenuItem newFile = new JMenuItem("新建（N）");                     //新建
		newFile.setFont(new Font("Microsoft YaHei UI", 0, 12));
		newFile.setMnemonic('n');
		newFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_MASK));
		newFile.setIcon(new ImageIcon(MainFrame.class.getResource("/image/new_p.png")));
		fileMenu.add(newFile);
		
		JMenuItem openFile = new JMenuItem("上传（U)");                    //导入
		openFile.setFont(new Font("Microsoft YaHei UI", 0, 12));
		openFile.setMnemonic('o');
		openFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, KeyEvent.CTRL_MASK));
		openFile.setIcon(new ImageIcon(MainFrame.class.getResource("/image/open_p.png")));
		//设置监听器
		openFile.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO 自动生成的方法存根
				new Upload();
			}
			
		});
		fileMenu.add(openFile);
		
		JMenuItem saveFile = new JMenuItem("下载（D)");                    //下载
		saveFile.setFont(new Font("Microsoft YaHei UI", 0, 12));
		saveFile.setMnemonic('d');
		saveFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, KeyEvent.CTRL_MASK));
		saveFile.setIcon(new ImageIcon(MainFrame.class.getResource("/image/save_p.png")));
		saveFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO 自动生成的方法存根
				MainFrame.tabPane.setSelectedIndex(0);
			}
			
		});
		fileMenu.add(saveFile);
		
		/*
		 * JMenu changeUI = new JMenu("更改外观");
		ButtonGroup group = new ButtonGroup();
		changeUI.setFont(new Font("Microsoft YaHei UI", 0, 12));                            //预留菜单
		JRadioButton defaultUI = new JRadioButton("默认外观");
		group.add(defaultUI);        
		defaultUI.setFont(new Font("Microsoft YaHei UI", 0, 12));
		defaultUI.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO 自动生成的方法存根
				try {
					UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
					MainFrame.mainframe.setVisible(false);
					MainFrame.mainframe.setVisible(true);
				} catch (ClassNotFoundException | InstantiationException
						| IllegalAccessException
						| UnsupportedLookAndFeelException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}
			
		});
		changeUI.add(defaultUI);
		JRadioButton systemUI = new JRadioButton("系统外观");
		systemUI.setFont(new Font("Microsoft YaHei UI", 0, 12));
		systemUI.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO 自动生成的方法存根
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					
				} catch (ClassNotFoundException | InstantiationException
						| IllegalAccessException
						| UnsupportedLookAndFeelException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}
			
		});
		group.add(systemUI);
		changeUI.add(systemUI);
		fileMenu.add(changeUI);
		 */
		
		JMenuItem exitFile = new JMenuItem("退出");                       //退出
		exitFile.setFont(new Font("Microsoft YaHei UI", 0, 12));
		exitFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, KeyEvent.ALT_MASK));
		exitFile.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}	
		});
		
		fileMenu.add(exitFile);
		
		fileMenu.insertSeparator(1);
		fileMenu.insertSeparator(4);
		//fileMenu.insertSeparator(6);
		
		//编辑菜单
		JMenu editMenu = new JMenu("搜索（E）");                        
		editMenu.setFont(new Font("Microsoft YaHei UI", 0, 12));
		editMenu.setMnemonic('E');
		menuBar.add(editMenu);
		JMenuItem search = new JMenuItem ("标签搜索");
		search.setFont(new Font("Microsoft YaHei UI", 0, 12));
		search.setIcon(new ImageIcon(MainFrame.class.getResource("/image/search_p.png")));
		search.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_MASK));
		search.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO 自动生成的方法存根
				MainFrame.tabPane.setSelectedIndex(1);
			}
			
		});
		editMenu.add(search);
		JMenuItem search_m = new JMenuItem ("注释搜索");
		search_m.setFont(new Font("Microsoft YaHei UI", 0, 12));
		search_m.setIcon(new ImageIcon(MainFrame.class.getResource("/image/search_m_p.png")));
		search_m.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, KeyEvent.CTRL_MASK));
		search_m.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO 自动生成的方法存根
				MainFrame.tabPane.setSelectedIndex(2);
			}
			
		});
		editMenu.add(search_m);
		
		//管理菜单
		JMenu manaMenu = new JMenu("管理（M）");                        
		manaMenu.setFont(new Font("Microsoft YaHei UI", 0, 12));
		manaMenu.setMnemonic('M');
		JMenuItem examine = new JMenuItem("审核（X）");                             //审核
		//examine.setIcon(new ImageIcon(MainFrame.class.getResource("/image/examine.png")));
		examine.setFont(new Font("Microsoft YaHei UI", 0, 12));
		examine.setMnemonic('x');
		examine.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_MASK));
		examine.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				new Examine(MainFrame.mainframe);
			}
			
		});
		
		manaMenu.add(examine);
		JMenuItem users = new JMenuItem("用户");
		users.setFont(new Font("Microsoft YaHei UI", 0, 12));
		users.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO 自动生成的方法存根
				new UserHis(MainFrame.mainframe);
			}
			
		});
		manaMenu.add(users);
		JMenuItem settings = new JMenuItem("设置（P）");
		settings.setFont(new Font("Microsoft YaHei UI", 0, 12));
		settings.setIcon(new ImageIcon(MainFrame.class.getResource("/image/settings.png")));
		settings.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO 自动生成的方法存根
				new Settings();
			}
			
		});
		settings.setMnemonic('p');
		manaMenu.add(settings);
		manaMenu.insertSeparator(2);
		menuBar.add(manaMenu);
		
		//若不是秘书权限则禁用菜单
		try {
			if (UserDao.getPermission(id)!=1) {
				examine.setEnabled(false);
				users.setEnabled(false);
				settings.setEnabled(false);
			}
		} catch (SQLException e1) {
			// TODO 自动生成的 catch 块
			e1.printStackTrace();
		}
		//关于菜单
		JMenu helpMenu = new JMenu("帮助（H）");                        
		helpMenu.setFont(new Font("Microsoft YaHei UI", 0, 12));
		helpMenu.setMnemonic('h');
		menuBar.add(helpMenu);
		JMenuItem about = new JMenuItem("关于");
		about.setFont(new Font("Microsoft YaHei UI",0,12));
		about.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO 自动生成的方法存根
				JTextArea name = new JTextArea();
				name.setText("DataLib -v1.1     @2014.4.2    by DianChen");
				name.setLineWrap(true);
				name.setEditable(false);
				name.setFont(new Font("Microsoft YaHei UI",0,12));
				JOptionPane.showMessageDialog(null, name);
			}
			
		});
		helpMenu.add(about);
		
		return menuBar;
	}
	
	public static JToolBar setToolBar() {
		JToolBar toolBar = new JToolBar("工具栏");
		
		//新建
		JButton newFile = new JButton();
		newFile.setIcon(new ImageIcon(MainFrame.class.getResource("/image/new.png")));
		newFile.setRolloverIcon(new ImageIcon(MainFrame.class.getResource("/image/new_r.png")));
		newFile.setBorderPainted(false);
		newFile.setFocusPainted(false);
		newFile.setContentAreaFilled(false);
		newFile.setFocusable(true);
		newFile.setMargin(new Insets(0, 0, 0, 0));
		toolBar.add(newFile);

		//导入
		JButton openFile = new JButton();
		openFile.setIcon(new ImageIcon(MainFrame.class.getResource("/image/open.png")));
		openFile.setRolloverIcon(new ImageIcon(MainFrame.class.getResource("/image/open_r.png")));
		openFile.setBorderPainted(false);
		openFile.setFocusPainted(false);
		openFile.setContentAreaFilled(false);
		openFile.setFocusable(true);
		openFile.setMargin(new Insets(0, 0, 0, 0));
		//设置监听器
		openFile.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO 自动生成的方法存根
				new Upload();
			}
			
		});
		
		toolBar.add(openFile);
		//导出
		JButton saveFile = new JButton();
		saveFile.setIcon(new ImageIcon(MainFrame.class.getResource("/image/save.png")));
		saveFile.setRolloverIcon(new ImageIcon(MainFrame.class.getResource("/image/save_r.png")));
		saveFile.setBorderPainted(false);
		saveFile.setFocusPainted(false);
		saveFile.setContentAreaFilled(false);
		saveFile.setFocusable(true);
		saveFile.setMargin(new Insets(0, 0, 0, 0));
		saveFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO 自动生成的方法存根
				MainFrame.tabPane.setSelectedIndex(0);
			}
			
		});
		toolBar.add(saveFile);
		toolBar.addSeparator(new Dimension(20,0));
		//搜索
		JButton searchFile = new JButton();
		searchFile.setIcon(new ImageIcon(MainFrame.class.getResource("/image/search.png")));
		searchFile.setRolloverIcon(new ImageIcon(MainFrame.class.getResource("/image/search_r.png")));
		searchFile.setBorderPainted(false);
		searchFile.setFocusPainted(false);
		searchFile.setContentAreaFilled(false);
		searchFile.setFocusable(true);
		searchFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO 自动生成的方法存根
				MainFrame.tabPane.setSelectedIndex(1);
			}
			
		});
		searchFile.setMargin(new Insets(0, 0, 0, 0));
		toolBar.add(searchFile);
		//模糊搜索
		JButton search_mFile = new JButton();
		search_mFile.setIcon(new ImageIcon(MainFrame.class.getResource("/image/search_m.png")));
		search_mFile.setRolloverIcon(new ImageIcon(MainFrame.class.getResource("/image/search_m_r.png")));
		search_mFile.setBorderPainted(false);
		search_mFile.setFocusPainted(false);
		search_mFile.setContentAreaFilled(false);
		search_mFile.setFocusable(true);
		search_mFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO 自动生成的方法存根
				MainFrame.tabPane.setSelectedIndex(2);
			}
			
		});
		search_mFile.setMargin(new Insets(0, 0, 0, 0));
		toolBar.add(search_mFile);
		
		return toolBar;
	}
	
	public static JPanel setStatePanel(String id) {
		ShadePanelH statePanel = new ShadePanelH();
		//设置布局
		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints cons_1 = new GridBagConstraints();
		cons_1.gridx = 0;
		cons_1.gridy = 0;
		cons_1.weightx = 50;
		cons_1.fill = GridBagConstraints.HORIZONTAL;
		statePanel.setLayout(gridbag);
		statePanel.setBorder(BorderFactory.createSoftBevelBorder(SoftBevelBorder.RAISED));
		
		//显示当前登录时间
		Calendar cal = Calendar.getInstance();
		int year, month, day;
		year = cal.get(Calendar.YEAR);
		month = cal.get(Calendar.MONTH) + 1;
		day = cal.get(Calendar.DATE);
		JLabel nowDateLabel = new JLabel(year+"-"+month+"-"+day);
		nowDateLabel.setFont(new Font("Microsoft YaHei UI",0,12));
		statePanel.add(nowDateLabel,cons_1);
		//显示当前用户
		JLabel user;
		try {
			user = new JLabel(id+"-"+UserDao.getName(id)+" ");
			user.setFont(new Font("Microsoft YaHei UI",0,12));
			GridBagConstraints cons_2 = new GridBagConstraints();
			cons_2.gridx = 1;
			cons_2.gridy = 0;
			cons_2.fill = GridBagConstraints.HORIZONTAL;
			statePanel.add(user, cons_2);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		GridBagConstraints cons_3 = new GridBagConstraints();
		cons_3.gridx = 2;
		cons_3.gridy = 0;
		JSeparator sep = new JSeparator();
		sep.setOrientation(JSeparator.VERTICAL);
		sep.setPreferredSize(new Dimension(6,18));
		statePanel.add(sep, cons_3);
		//显示公司信息
		GridBagConstraints cons_5 = new GridBagConstraints();
		cons_5.gridx = 3;
		cons_5.gridy = 0;
		cons_5.fill = GridBagConstraints.HORIZONTAL;
		JLabel nameLabel = new JLabel("Created by DianChen");
		nameLabel.setFont(new Font("Microsoft YaHei UI",0,12));
		statePanel.add(nameLabel,cons_5);
		return statePanel;
	}
}
