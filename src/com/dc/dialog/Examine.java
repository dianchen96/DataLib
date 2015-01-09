//审核文件的弹出窗

package com.dc.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JTree;
import javax.swing.WindowConstants;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.apache.commons.net.ftp.FTPFile;

import com.dc.MainFrame;
import com.dc.dao.Dao;
import com.dc.dao.UserDao;
import com.dc.ftp.FtpExamine;
import com.dc.model.DtlFile;
import com.dc.model.Tag;
import com.dc.myframe.panel.ShadePanelH;
import com.dc.myframe.panel.ShadePanelV;
import com.dc.myframe.table.P_Data_InfoTable;
import com.dc.myframe.tree.ExamineTree;

public class Examine extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3296500051163617726L;

	private JScrollPane EDscrollPane;
	
	public Examine(final JFrame mother) {
		super(mother, true);
		setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		setTitle("审核文件");
		setSize(500, 650);
		setLocation(mother.getX()+150, mother.getY()-20);
		JSplitPane splitPane = new JSplitPane();
		splitPane.setDividerLocation(140);
		splitPane.setContinuousLayout(true);
		splitPane.setOneTouchExpandable(true);
		//分隔板左边
		JPanel lPane = new JPanel();
		lPane.setLayout(new BorderLayout());
		JLabel title = new JLabel("待审核文件");
		title.setFont(new Font("Microsoft YaHei UI", 0, 12));
		title.setIcon(new ImageIcon(MainFrame.class.getResource("/image/icon_purple.gif")));
		ShadePanelV titlePanel = new ShadePanelV();
		titlePanel.setLayout(new BorderLayout());
		titlePanel.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.LIGHT_GRAY));
		titlePanel.add(BorderLayout.NORTH, title);
		lPane.add(BorderLayout.NORTH, titlePanel);
		
		ExamineTree examineTree = new ExamineTree();
		final JTree un_tree = examineTree.un_tree;
		
		JScrollPane scrollPane = new JScrollPane(un_tree);
		lPane.add(BorderLayout.CENTER, scrollPane);
		
		JSplitPane lSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		lSplit.setDividerLocation(300);
		lSplit.setLeftComponent(lPane);
		
		JPanel rPane = new JPanel();
		rPane.setLayout(new BorderLayout());
		JLabel title2 = new JLabel("已选文件");
		title2.setFont(new Font("Microsoft YaHei UI", 0, 12));
		title2.setIcon(new ImageIcon(MainFrame.class.getResource("/image/icon_green.gif")));
		ShadePanelV title2Panel = new ShadePanelV();
		title2Panel.setLayout(new BorderLayout());
		title2Panel.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.LIGHT_GRAY));
		title2Panel.add(BorderLayout.NORTH, title2);
		rPane.add(BorderLayout.NORTH, title2Panel);
		
		//设置第二棵树
		final JTree ed_tree = examineTree.ed_tree;
		JScrollPane treeScroll = new JScrollPane(ed_tree);
		rPane.add(BorderLayout.CENTER, treeScroll);
		//内部类右键弹出菜单
		class UN_TreePopupMenu extends JPopupMenu{

			private static final long serialVersionUID = -8459214669489067181L;

			public UN_TreePopupMenu() {
				super();
				JMenuItem addItem = new JMenuItem("添加");
				addItem.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e) {
						TreePath[] nodepaths = un_tree.getSelectionPaths();
						DefaultMutableTreeNode ed_root = (DefaultMutableTreeNode) ed_tree.getModel().getRoot();
						for (int i = 0; i < nodepaths.length; i++) {
							DefaultMutableTreeNode node = new DefaultMutableTreeNode ((DefaultMutableTreeNode) nodepaths[i].getLastPathComponent());
							if (node.toString().indexOf(".")!=-1) {
								boolean flag = true;
								for (int j = 0; j < ed_tree.getRowCount(); j++) {
									if (node.toString().equals(ed_root.getChildAt(j).toString())) {
										flag = false;
										break;
									}
								}
								if (flag) { 
									DefaultTreeModel model = (DefaultTreeModel) ed_tree.getModel();
									DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
									model.insertNodeInto(node, root, 0);
									ed_tree.setModel(model);
								}
								
							}
						}
						ed_tree.updateUI();
					}
							
				});
				add(addItem);
			}
					
		}
		un_tree.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton()==3) {
					new UN_TreePopupMenu().show(un_tree, e.getX(), e.getY());
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e) {}
			@Override
			public void mouseReleased(MouseEvent e) {}
			
		});
		class ED_TreePopupMenu extends JPopupMenu {
			/**
			 * 
			 */
			private static final long serialVersionUID = 2661487061580001293L;

			public ED_TreePopupMenu() {
				super();
				JMenuItem deleteItem = new JMenuItem("移除");
				deleteItem.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						TreePath[] nodepaths = ed_tree.getSelectionPaths();
						DefaultMutableTreeNode ed_root = (DefaultMutableTreeNode) ed_tree.getModel().getRoot();
						DefaultTreeModel model = (DefaultTreeModel) ed_tree.getModel();
						
						for (int i = 0; i < nodepaths.length; i++) {
							DefaultMutableTreeNode node = (DefaultMutableTreeNode) nodepaths[i].getLastPathComponent();
							for (int j = 0; j < ed_root.getChildCount(); j++) {
								DefaultMutableTreeNode ed_node = (DefaultMutableTreeNode) ed_root.getChildAt(j);
								
								if (ed_node.toString().equals(node.toString())) {
									model.removeNodeFromParent(ed_node);
								}
							}
						}
						ed_tree.setModel(model);
						ed_tree.updateUI();
					}
					
				});
				add(deleteItem);
			}
			
		}
		ed_tree.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton()==3) {
					new ED_TreePopupMenu().show(ed_tree, e.getX(), e.getY());
				} 
			}

			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e) {}
			@Override
			public void mouseReleased(MouseEvent e) {}
			
		});
		
		lSplit.setRightComponent(rPane);
		
		JPanel rPlane = new JPanel();
		rPlane.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY));
		rPlane.setLayout(new BorderLayout());
		JLabel title3 = new JLabel("文件信息");
		title3.setFont(new Font("Microsoft YaHei UI", 0, 12));
		title3.setIcon(new ImageIcon(MainFrame.class.getResource("/image/icon_orange.gif")));
		ShadePanelH title3Panel = new ShadePanelH();
		title3Panel.setLayout(new BorderLayout());
		title3Panel.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.LIGHT_GRAY));
		title3Panel.add(BorderLayout.NORTH, title3);
		rPlane.add(BorderLayout.NORTH, title3Panel);
		final JPanel infotable = new JPanel();
		infotable.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY));
		infotable.setLayout(new GridLayout(1,1,0,0));
		JPanel rUnderPlane = new JPanel();
		rUnderPlane.setLayout(new GridLayout(4,1,0,0));
		rUnderPlane.add(infotable);
		rPlane.add(BorderLayout.CENTER, rUnderPlane);
		
		JPanel tagandanno = new JPanel();
		tagandanno.setLayout(new GridBagLayout());
		
		JPanel tagb = new JPanel();
		tagb.setLayout(new BorderLayout());
		JLabel tagboardtitle = new JLabel(" -已选择标签");
		tagboardtitle.setForeground(Color.DARK_GRAY);
		tagboardtitle.setFont(new Font("Microsoft YaHei UI",0,12));
		ShadePanelH tagboardtitlepanel = new ShadePanelH();
		tagboardtitlepanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.LIGHT_GRAY));
		tagboardtitlepanel.setLayout(new BorderLayout());
		tagboardtitlepanel.add(BorderLayout.NORTH, tagboardtitle);
		tagb.add(BorderLayout.NORTH, tagboardtitlepanel);
		//标签面板
		final JPanel tagBoard = new JPanel();
		tagBoard.setLayout(new FlowLayout(FlowLayout.LEFT,5,5));
		final JScrollPane tagScrollPane = new JScrollPane(tagBoard);
		tagScrollPane.setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, Color.LIGHT_GRAY));
		tagb.add(BorderLayout.CENTER, tagScrollPane);

		GridBagConstraints tagb_con = new GridBagConstraints();
		tagb_con.gridx = 0;
		tagb_con.gridy = 0;
		tagb_con.insets = new Insets(1,1,1,1);
		tagb_con.weightx = 70;
		tagb_con.weighty = 40;
		
		tagb_con.fill = GridBagConstraints.BOTH;
		tagandanno.add(tagb, tagb_con);
		
		//标签树
		Tag[] tags = null;
		try {
			tags = Dao.getTags();
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		DefaultMutableTreeNode root = new DefaultMutableTreeNode();
		//为根节点添加标签名称
		for (int i = 0; i < tags.length; i++) {
			DefaultMutableTreeNode tag_name = new DefaultMutableTreeNode(tags[i].getTagName());

			for (int j = 0; j < tags[i].getTagValuesAsArray().length; j++) {
				DefaultMutableTreeNode tag_value = new DefaultMutableTreeNode(tags[i].getTagValuesAsArray()[j]);
				tag_name.add(tag_value);
				
			}
			root.add(tag_name);
		}
		final JTree tree = new JTree(root);
		//设置树的描绘器
		DefaultTreeCellRenderer cellRenderer = (DefaultTreeCellRenderer) tree.getCellRenderer();
		cellRenderer.setOpenIcon(new ImageIcon(MainFrame.class.getResource("/image/tag.gif")));
		cellRenderer.setClosedIcon(new ImageIcon(MainFrame.class.getResource("/image/tag_closed.gif")));
		cellRenderer.setLeafIcon(new ImageIcon(MainFrame.class.getResource("/image/tag_value.gif")));
		tree.setCellRenderer(cellRenderer);
		tree.setRootVisible(false);
		tree.setFont(new Font("Microsoft YaHei UI",0,12));
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		//为树添加监听器
		tree.addMouseListener(new MouseListener(){
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					TreePath path = tree.getPathForLocation(e.getX(), e.getY());
					if (path != null) {
						DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) path.getLastPathComponent();
						if (selectedNode.isLeaf()) {
							DefaultMutableTreeNode tag_name = (DefaultMutableTreeNode) selectedNode.getParent();   //标签值
	                        String txt = tag_name.toString()+"："+selectedNode.toString();
							JToggleButton tag = new JToggleButton(txt);
							
							boolean flag = true;
							int count = tagBoard.getComponentCount();
							int width = tagScrollPane.getWidth()-10;
							int totalwidth = 0;
							
							for (int i = 0; i < count; i++) {
								JToggleButton eachButton = (JToggleButton) tagBoard.getComponent(i);
								String eachtag_name = eachButton.getText().split("：")[0];
								if (tag_name.toString().equals(eachtag_name)) {
									totalwidth = totalwidth + tagBoard.getComponent(i).getWidth();
									flag = false;
								}
							}
							
							if (flag) {
								tagBoard.add(tag);
								
								int row = totalwidth / width + 1;
								int height = row * 60;
								tagBoard.setPreferredSize(new Dimension(width, height));
								tagScrollPane.updateUI();
							} else {
								JOptionPane.showMessageDialog(null, "相同属性的标签只能添加一个");
							}
						}
					}
				}
			}
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			@Override
			public void mouseExited(MouseEvent e) {
			}
			@Override
			public void mousePressed(MouseEvent e) {
			}
			@Override
			public void mouseReleased(MouseEvent e) {
			}
			
		});
		JPanel tagSelect = new JPanel();
		tagSelect.setLayout(new BorderLayout());
		JLabel tagselecttitle = new JLabel(" -标签列表");
		tagselecttitle.setForeground(Color.DARK_GRAY);
		tagselecttitle.setFont(new Font("Microsoft YaHei UI",0,12));
		ShadePanelH tagselecttitlepanel = new ShadePanelH();
		tagselecttitlepanel.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.LIGHT_GRAY));
		tagselecttitlepanel.setLayout(new BorderLayout());
		tagselecttitlepanel.add(BorderLayout.NORTH, tagselecttitle);
		tagSelect.add(BorderLayout.NORTH, tagselecttitlepanel);
		
		final JScrollPane tagtreeScroll = new JScrollPane(tree);
		tagtreeScroll.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY));
		tagSelect.add(BorderLayout.CENTER, tagtreeScroll);
		JButton delete = new JButton("移除标签");
		delete.setToolTipText("移除选中的标签");
		delete.setFont(new Font("Microsoft YaHei UI",0,12));
		delete.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Vector<JToggleButton> unselected = new Vector<JToggleButton>();
				for (int i = 0; i < tagBoard.getComponentCount(); i++) {
					JToggleButton eachbutton = (JToggleButton) tagBoard.getComponent(i);
					if (!eachbutton.getModel().isSelected()) {
						unselected.add(eachbutton);
					}
				}
				tagBoard.removeAll();
				for (int i = 0; i < unselected.size(); i++) {
					tagBoard.add(unselected.get(i));
				}
				int count = tagBoard.getComponentCount();
				int width = tagtreeScroll.getWidth()-10;
				int totalwidth = 0;
				for (int i = 0; i < count; i++) {
					totalwidth = totalwidth + tagBoard.getComponent(i).getWidth();
				}
				int row = totalwidth / width + 1;
				int height = row * 60;
				tagBoard.setPreferredSize(new Dimension(width, height));
				tagBoard.updateUI();
			}
			
		});
		tagSelect.add(BorderLayout.SOUTH, delete);
		GridBagConstraints tags_con = new GridBagConstraints();
		tags_con.gridx = 1;
		tags_con.gridy = 0;
		tags_con.insets = new Insets(1,1,1,1);
		tags_con.weightx = 30;
		tags_con.weighty = 30;
		
		tags_con.fill = GridBagConstraints.BOTH;
		tagandanno.add(tagSelect, tags_con);
		
		final JPanel annopanel = new JPanel();
		annopanel.setLayout(new BorderLayout());
		JLabel annotitle = new JLabel(" -文件注释");
		annotitle.setForeground(Color.DARK_GRAY);
		annotitle.setFont(new Font("Microsoft YaHei UI",0,12));
		ShadePanelH annotitlepanel = new ShadePanelH();
		annotitlepanel.setLayout(new BorderLayout());
		annotitlepanel.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.LIGHT_GRAY));
		annotitlepanel.add(BorderLayout.NORTH, annotitle);
		annopanel.add(BorderLayout.NORTH, annotitlepanel);
		final JTextArea type = new JTextArea();
		type.setBounds(10,3,317,60);
		type.setLineWrap(true);
		type.setFont(new Font("Microsoft YaHei", 0, 12));
		annopanel.add(BorderLayout.CENTER, type);
		
		
		JPanel pathnbutton = new JPanel();
		pathnbutton.setLayout(new BorderLayout());
		
		JLabel pathtitle = new JLabel(" -存放路径和级别");
		pathtitle.setFont(new Font("Microsoft YaHei UI",0,12));
		pathtitle.setForeground(Color.DARK_GRAY);
		ShadePanelH pathtitlepanel = new ShadePanelH();
		pathtitlepanel.setLayout(new BorderLayout());
		pathtitlepanel.add(BorderLayout.NORTH, pathtitle);
	    pathnbutton.add(BorderLayout.NORTH, pathtitlepanel);
		JPanel pathText = new JPanel();
		pathText.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY));
	    pathText.setLayout(null);
	    final JTextField textField = new JTextField();
	    textField.setEditable(false);
	    textField.setFont(new Font("Microsoft YaHei UI",0,12));
	    textField.setBounds(5, 5, 250, 25);
	    pathText.add(textField);
	    JButton check = new JButton("浏览");
	    check.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				textField.setText(new PathSelect(mother).getSelectedPath());
				
			}
	    	
	    });
	    check.setFont(new Font("Microsoft YaHei UI",0,12));
	    check.setBounds(260, 5, 70, 25);
	    pathText.add(check);
	    pathnbutton.add(pathText);
	    
	    String[] ranks ={"见习生","4.1","4.2","4.3","5.1","5.2","5.3","6.1","6.2","7.1","7.2","8.1","8.2"};
	    final JComboBox<String> setranks = new JComboBox<String>(ranks);
	    setranks.setFont(new Font("Microsoft YaHei UI",0,12));
	    setranks.setBounds(5,50,150,25);
	    pathText.add(setranks);
	    
	    final JPanel buttons = new JPanel();
	    buttons.setLayout(new FlowLayout(FlowLayout.RIGHT,5,5));
	    final JButton download = new JButton("保存到本地");
	    ed_tree.addTreeSelectionListener(new TreeSelectionListener() {
			@Override
			public void valueChanged(TreeSelectionEvent arg0) {
				if (!ed_tree.isSelectionEmpty()) {
					download.setEnabled(true);
					TreePath[] selectedPaths = ed_tree.getSelectionPaths();
					if (selectedPaths.length == 1) {
						TreePath path = ed_tree.getSelectionPath();
						String name = path.getLastPathComponent().toString();
						infotable.removeAll();
						P_Data_InfoTable info = new P_Data_InfoTable(name);
						infotable.add(info);
						try {
							tagBoard.removeAll();
							String[] tags = Dao.getFileTags(name);
							if (tags!=null) {
								for (int i = 0; i < tags.length; i++) {
									JToggleButton eachbutt = new JToggleButton(tags[i]);
									tagBoard.add(eachbutt);
								}
							}
							int count = tagBoard.getComponentCount();
							int width = tagtreeScroll.getWidth()-10;
							int totalwidth = 0;
							for (int i = 0; i < count; i++) {
								totalwidth = totalwidth + tagBoard.getComponent(i).getWidth();
							}
							int row = totalwidth / width + 1;
							int height = row * 60;
							tagBoard.setPreferredSize(new Dimension(width, height));
							type.setText(Dao.getAnnotate(name));
							
							infotable.updateUI();
							tagBoard.updateUI();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					} else {
						infotable.removeAll();
						infotable.updateUI();
					}
					
				} else {
					download.setEnabled(false);
				}
				
			}
			
		});
	    download.setEnabled(false);
	    download.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				TreePath[] selectedPaths = ed_tree.getSelectionPaths();
				String[] fileNames = new String[selectedPaths.length];
				for (int i = 0; i < fileNames.length; i++) {
					fileNames[i] = selectedPaths[i].getLastPathComponent().toString();
				}
				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int i = chooser.showSaveDialog(annopanel);
				if (i == JFileChooser.APPROVE_OPTION) {
					String savePath = chooser.getSelectedFile().getAbsolutePath();
					FtpExamine.seeFile(fileNames, savePath);
				}
				
			}
	    	
	    });
	    download.setFont(new Font("Microsoft YaHei UI",0,12));
	    buttons.add(download);
	    
	    JButton confirm = new JButton("确定");
	    confirm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!ed_tree.isSelectionEmpty()) {
					TreePath[] selectedPaths = ed_tree.getSelectionPaths();
					int x = JOptionPane.showConfirmDialog(null, "确认要审核通过所选文件吗？", "确认审核", JOptionPane.YES_NO_OPTION);
					if (x == JOptionPane.YES_OPTION) {
						for (int i = 0; i < selectedPaths.length; i++) {
						String fileName = selectedPaths[i].getLastPathComponent().toString();
						Object[][] P_Info = null;
						try {
							P_Info = Dao.getPreDtlFile(fileName);
							
						} catch (SQLException e1) {
							// TODO 自动生成的 catch 块
							e1.printStackTrace();
						}
						String tagbrand = "";
						for (int j = 0; j < tagBoard.getComponentCount(); j++) {
							JToggleButton button = (JToggleButton) tagBoard.getComponent(j);
							tagbrand = ";" + button.getText() + tagbrand;
						}
						String annotate;
						String filename;
						String filetype;
						long filesize;
						String filedate;
						String uploader;
						String filepath;
						String downRank;
						if (!P_Info[0][1].equals("")) { 
							annotate = type.getText();
							filename = P_Info[0][1].toString();
							filetype = P_Info[1][1].toString();
							filesize = Integer.parseInt(P_Info[2][1].toString());
							filedate = P_Info[3][1].toString();
							uploader = P_Info[4][1].toString();
							filepath = textField.getText();
						} else {
							annotate = type.getText();
							filename = fileName;
							filetype = filename.substring(fileName.indexOf(".")+1);
							FTPFile file = FtpExamine.getFile(filename);
							filesize = file.getSize();
							filedate = getDate();
							uploader = "";
							try {
								uploader = MainFrame.user_id+"-"+UserDao.getName(MainFrame.user_id);
							} catch (SQLException e1) {
								// TODO 自动生成的 catch 块
								e1.printStackTrace();
							}
							filepath = textField.getText();
						}
						downRank = setranks.getSelectedItem().toString();
						DtlFile fileInfo = new DtlFile(filename,filetype,filesize,filedate,filepath,uploader,tagbrand,annotate,downRank);
						try {
							Dao.deletePreDtlFile(filename);
							Dao.writeDTLB(fileInfo);
							FtpExamine.moveFileTo(filename, textField.getText());
							JOptionPane.showMessageDialog(null, "文件已审核成功！");
							setVisible(false);
						} catch (SQLException e1) {
							// TODO 自动生成的 catch 块
							e1.printStackTrace();
						}

					}
					}
				}
			}
	    	
	    });
	    confirm.setFont(new Font("Microsoft YaHei UI",0,12));
		buttons.add(confirm);
	    JButton cancel = new JButton("取消");
	    cancel.setFont(new Font("Microsoft YaHei UI",0,12));
	    cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
				
			}
	    	
	    });
		buttons.add(cancel);

	    pathnbutton.add(BorderLayout.SOUTH, buttons);
	    
	    
	    rUnderPlane.add(tagandanno);
		rUnderPlane.add(annopanel);
		rUnderPlane.add(pathnbutton);
		
		splitPane.setLeftComponent(lSplit);
		splitPane.setRightComponent(rPlane);
		
		
		getContentPane().add(splitPane);
		setVisible(true);
	}
	
	public JScrollPane getEDScrollPane() {
		return EDscrollPane;
	}
	
	private String getDate() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		return df.format(new Date());
	}
}
