//程序主界面的方法

package com.dc;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;



import javax.swing.table.TableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import com.dc.dao.Dao;
import com.dc.dao.UserDao;
import com.dc.myframe.tree.FtpTree;
import com.dc.myframe.panel.ShadePanelV;

public class MainFrame {
	
	public static JTabbedPane tabPane;
	public static JTable selectedFiles;
	
	static JFrame Login;
	public static JLabel pic;
	//获取屏幕中央的方法
	public static Point getMidDimesion(Dimension d) {
       Point p = new Point();
       Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
       p.setLocation((dim.width - d.width)/2,(dim.height - d.height)/2);
       return p;
    }
	//登录界面
	private static void login() {
		//窗体设置
		Login = new JFrame("登录");
		Login.setResizable(false);
		Login.setLayout(null);
		Container container = Login.getContentPane();
		Login.setSize(272, 190);
	    Login.setLocation(getMidDimesion(new Dimension(Login.getWidth(),Login.getHeight())));
		Login.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		Login.addWindowListener(new WindowAdapter(){
		   	public void windowClosing(WindowEvent e){
		   	  System.exit(0);	
		   	}
		});
		//文字设置
		JLabel gonghao = new JLabel("账号");
		JLabel mima = new JLabel("密码");	
		gonghao.setFont(new Font("Microsoft YaHei UI",1,12));
		mima.setFont(new Font("Microsoft YaHei UI",1,12));
		gonghao.setBounds(30, 65, 50, 15);
	    mima.setBounds(30, 95, 50, 15);
		container.add(gonghao);
		container.add(mima);
		
		//输入框设置
		final JTextField Account = new JTextField(100);
		Account.setBounds(80, 65, 100, 15);
		Login.add(Account);
		//密码框设置
		final JPasswordField Password = new JPasswordField(100);
		Password.setBounds(80, 95, 100, 15);
		Login.add(Password);
		
		
		//图片设置
		pic = new JLabel();
		final ImageIcon entered = new ImageIcon(MainFrame.class.getResource("/image/Upper_dt_C.jpg"));
		final ImageIcon exited = new ImageIcon(MainFrame.class.getResource("/image/Upper_dt.jpg"));
		pic.setIcon(exited);
		pic.setBounds(0, 0, 300, 38);
		//类按钮
		pic.addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent e) {	
			}
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			@Override
			public void mouseExited(MouseEvent e) {
				pic.setIcon(exited);
				pic.repaint();
			}
			@Override
			public void mousePressed(MouseEvent e) {}
			@Override
			public void mouseReleased(MouseEvent e) {
				float x_pos = e.getX();
				float y_pos = e.getY();
				if (x_pos>214&&x_pos<236&&y_pos>6&&y_pos<28) {
					//获取工号和密码
					String id = Account.getText();
					String pass = new String (Password.getPassword());
					try {
						if (UserDao.getLogin(id, pass)) {
							//工号和密码正确
							Login.setVisible(false);
							frame(id);
						} else {
							//工号和密码不正确
							JOptionPane.showMessageDialog(Login, "用户名或密码不正确", "登录失败", JOptionPane.ERROR_MESSAGE);
						}
					} catch (Exception x) {
						// TODO 自动生成的 catch 块
						x.printStackTrace();
					}
				}	
			}			
		});
		pic.addMouseMotionListener(new MouseMotionListener(){
			
			@Override
			public void mouseDragged(MouseEvent arg0) {				
			}

			@Override
			public void mouseMoved(MouseEvent arg0) {
				float x_pos = arg0.getX();
				float y_pos = arg0.getY();
				if (x_pos>214&&x_pos<236&&y_pos>6&&y_pos<28) {
					pic.setIcon(entered);
					pic.repaint();
				} else {
					pic.setIcon(exited);
					pic.repaint();
				}
			}
			
		});
		
		Login.add(pic);
		
		//账户框的监听器设置
		Account.addKeyListener(new KeyAdapter(){
			public void keyTyped(KeyEvent e) {
				if (e.getKeyChar()=='\n') {
					Password.requestFocus();
				}
			}
		});
		//密码框的监听设置
		Password.addKeyListener(new KeyAdapter(){
			public void keyTyped(KeyEvent e) {
				if (e.getKeyChar()=='\n') {
					try {
						new LoginRobot().run();
					} catch (Exception e1) {
						// TODO 自动生成的 catch 块
						e1.printStackTrace();
					}
				}
			}
		});
	}
		
	//***************主窗体设计**********//
	public static JFrame mainframe;
	public static String user_id;
	
	static Container c;
	static JPanel jp;
	
	private static void frame(String id) {
		user_id = id.toUpperCase();

		//设置look&feel
		try {
			//UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		
		//初始化窗体
		mainframe = new JFrame("DataLib -v1.1");
		mainframe.setResizable(false);
		c = mainframe.getContentPane();
		mainframe.setSize(800, 600);
		mainframe.setLocation(getMidDimesion(new Dimension(mainframe.getWidth(),mainframe.getHeight())));
		mainframe.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		//设置菜单栏
		mainframe.setJMenuBar(BarSet.setMenuBar(id));
		//设置工具栏
		mainframe.add(BarSet.setToolBar(), BorderLayout.NORTH);
		//设置状态栏
		mainframe.add(BarSet.setStatePanel(user_id), BorderLayout.SOUTH);
		
		//设置分割面板
		JSplitPane hSplit = new JSplitPane();          
		hSplit.setDividerLocation(120); 
		hSplit.setContinuousLayout(true);
		hSplit.setOneTouchExpandable(true);
		c.add(hSplit, BorderLayout.CENTER);
		
		JSplitPane treeSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true);         //树面板
		hSplit.setLeftComponent(treeSplit);
		//设置树形图
		JPanel treePanel = new JPanel();                          //树标题
		treePanel.setLayout(new BorderLayout());
		JLabel treeTitle = new JLabel("数据目录");
		treeTitle.setFont(new Font("Microsoft YaHei UI",0,12));
		treeTitle.setIcon(new ImageIcon(MainFrame.class.getResource("/image/tree.gif")));
		ShadePanelV treeTitlePanel = new ShadePanelV();
		treeTitlePanel.setLayout(new BorderLayout());
		treeTitlePanel.add(BorderLayout.NORTH,treeTitle);
		treeTitlePanel.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.LIGHT_GRAY));
		treePanel.add(BorderLayout.NORTH, treeTitlePanel);
		
		FtpTree treeBoard = new FtpTree();
		final JTree tree = treeBoard.tree;
		tree.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO 自动生成的方法存根
				if (e.getClickCount() == 2) {
					TreePath path = tree.getPathForLocation(e.getX(), e.getY());
					if (path != null) {
						DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) path.getLastPathComponent();
						if (selectedNode.isLeaf()) {
							try {
								String[] result = Dao.search(selectedNode.toString());
								int index = 0;
								TableModel model = selectedFiles.getModel();
								for (int i = 0; i < model.getRowCount(); i++) {
									if (model.getValueAt(i, 0) == null) {
										index = i;
										break;
									} else {
										if(model.getValueAt(i, 0).equals("")) {
											index = i;
											break;
										}
									}
								}
								if (Dao.isDownloadable(result[0], user_id)) {
									for (int i = 0; i < model.getColumnCount(); i++) {
										model.setValueAt(result[i], index, i);
									}
									selectedFiles.setModel(model);
								} else {
									JOptionPane.showMessageDialog(null, selectedNode+"需要更高的用户权限，您无法下载");		
								}
								selectedFiles.setModel(model);
							} catch (SQLException e1) {
								// TODO 自动生成的 catch 块
								e1.printStackTrace();
							}
							
	                        
						}
					}
				}
			}
			@Override
			public void mouseEntered(MouseEvent arg0) {}
			@Override
			public void mouseExited(MouseEvent arg0) {}
			@Override
			public void mousePressed(MouseEvent arg0) {}
			@Override
			public void mouseReleased(MouseEvent arg0) {}
			
		});
		JScrollPane treeScrollPane = new JScrollPane(tree);
		treePanel.add(BorderLayout.CENTER, treeScrollPane);
		
		try {
			tabPane = TabPaneSet.setTabbedPane();
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		tabPane.setFont(new Font("Microsoft YaHei UI",0,12));
		tabPane.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));
		hSplit.setRightComponent(tabPane);
		
		
		
		
		
		treeSplit.setLeftComponent(treePanel);
		
		
		
		mainframe.setVisible(true);
	}
	
	
	public static void main(String[] args) {
		login();
		new Splash().start();
		try {
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		Login.setVisible(true);
	}


}
