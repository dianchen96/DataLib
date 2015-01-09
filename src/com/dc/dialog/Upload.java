//弹出上传对话框

package com.dc.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JTree;
import javax.swing.WindowConstants;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.dc.MainFrame;
import com.dc.dao.Dao;
import com.dc.ftp.upload.FtpUpload;
import com.dc.model.Tag;
import com.dc.model.TagStringMaker;
import com.dc.myframe.FileProgressStatus;
import com.dc.myframe.FileUploadProgress;
import com.dc.myframe.panel.ShadePanelH;
import com.dc.myframe.panel.ShadePanelV;
import com.dc.myframe.table.FileInfo;

public class Upload {
	
	public static JDialog updialog;
	
	public Upload() {
		

		
		updialog = new JDialog(MainFrame.mainframe);
		
		
		//设置标签框标题
		JLabel fortagsTitle = new JLabel("添加标签");
		fortagsTitle.setFont(new Font("Microsoft YaHei UI",0,12));
		fortagsTitle.setIcon(new ImageIcon(MainFrame.class.getResource("/image/icon_blue.gif")));
		ShadePanelV fortagsTitlePanel = new ShadePanelV();
		fortagsTitlePanel.setBounds(0, 0, 380, 20);
		fortagsTitlePanel.setLayout(new BorderLayout());
		fortagsTitlePanel.add(BorderLayout.NORTH,fortagsTitle);
		fortagsTitlePanel.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.LIGHT_GRAY));
		updialog.add(fortagsTitlePanel);
		//设置标签框下部
		//左下部
		JPanel leftPanel = new JPanel();
		leftPanel.setBounds(1,21,229,232);
		leftPanel.setLayout(new BorderLayout());
		JLabel leftPanelTitle = new JLabel("    -已添加的标签");
		leftPanelTitle.setForeground(Color.DARK_GRAY);
		leftPanelTitle.setFont(new Font("Microsoft YaHei UI",0,12));
		ShadePanelH leftPanelTitlePanel = new ShadePanelH();
		leftPanelTitlePanel.setBounds(0, 0, 380, 20);
		leftPanelTitlePanel.setLayout(new BorderLayout());
		leftPanelTitlePanel.add(BorderLayout.NORTH, leftPanelTitle);
		leftPanelTitlePanel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 0, Color.LIGHT_GRAY));
		leftPanel.add(BorderLayout.NORTH,leftPanelTitlePanel);

		final JPanel tagBoard = new JPanel();
		final JScrollPane fortagsScroll = new JScrollPane(tagBoard);
		
		
		tagBoard.setLayout(new FlowLayout(FlowLayout.LEFT,5,5));
		
		leftPanel.add(BorderLayout.CENTER, fortagsScroll);
		//右部
		
		
		Tag[] tags = null;
		try {
			tags = Dao.getTags();
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		//右上部
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
							int width = fortagsScroll.getWidth()-10;
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
								fortagsScroll.updateUI();
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
		
		//将树添加到标签选择器
		JPanel tagChooser = new JPanel();
		tagChooser.setLayout(new BorderLayout());
		JLabel tagChooserTitle = new JLabel("   -标签列表");
		tagChooserTitle.setFont(new Font("Microsoft YaHei UI",0,12));
		tagChooserTitle.setForeground(Color.DARK_GRAY);
		ShadePanelH tagChooserTitlePanel = new ShadePanelH();
		tagChooserTitlePanel.setLayout(new BorderLayout());
		tagChooserTitlePanel.add(BorderLayout.NORTH, tagChooserTitle);
		tagChooserTitlePanel.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 1, Color.LIGHT_GRAY));
		
		tagChooser.setBounds(230,21,150,200);
		tagChooser.add(BorderLayout.NORTH, tagChooserTitlePanel);
		tagChooser.add(BorderLayout.CENTER, new JScrollPane(tree));
		//将标签选择器加入标签框
		updialog.add(tagChooser);
		//设置按钮
		JButton addTag = new JButton("添加");
		addTag.setToolTipText("添加标签到标签版");
		addTag.setFont(new Font("Microsoft YaHei UI",0,12));
		addTag.setBounds(232, 222, 70, 30);
		addTag.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				if (!tree.isSelectionEmpty()) {
					//获取被选取节点
					DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
					
					if (selectedNode.isLeaf()) {
						DefaultMutableTreeNode tag_name = (DefaultMutableTreeNode) selectedNode.getParent();   //标签值
                        String txt = tag_name.toString()+"："+selectedNode.toString();
						JToggleButton tag = new JToggleButton(txt);
						
						boolean flag = true;
						int count = tagBoard.getComponentCount();
						int width = fortagsScroll.getWidth()-10;
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
							fortagsScroll.updateUI();
						} else {
							JOptionPane.showMessageDialog(null, "相同属性的标签只能添加一个");
						}
					}

					
				}
			}
		});
		updialog.add(addTag);
		
		
		JButton removeTag = new JButton("移除");
		removeTag.setToolTipText("移除按下的标签");
		removeTag.setFont(new Font("Microsoft YaHei UI",0,12));
		removeTag.setBounds(307, 222, 70, 30);
		removeTag.addActionListener(new ActionListener(){
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
				int width = fortagsScroll.getWidth()-10;
				int totalwidth = 0;
				for (int i = 0; i < count; i++) {
					totalwidth = totalwidth + tagBoard.getComponent(i).getWidth();
				}
				int row = totalwidth / width + 1;
				int height = row * 60;
				tagBoard.setPreferredSize(new Dimension(width, height));
				fortagsScroll.updateUI();
			}
			
		});
		updialog.add(removeTag);
		
		
		//将标签版加入面板
		updialog.add(leftPanel);
		
		
		//设置文件选择框
		JLabel fileInfoTitle = new JLabel("选择文件");                     //标题
		fileInfoTitle.setFont(new Font("Microsoft YaHei UI",0,12));
		fileInfoTitle.setIcon(new ImageIcon(MainFrame.class.getResource("/image/icon_orange.gif")));
		ShadePanelV fileInfoTitlePanel = new ShadePanelV();
		fileInfoTitlePanel.setBounds(0, 256, 380, 20);
		fileInfoTitlePanel.setLayout(new BorderLayout());
		fileInfoTitlePanel.add(BorderLayout.NORTH,fileInfoTitle);
		fileInfoTitlePanel.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.LIGHT_GRAY));
		updialog.add(fileInfoTitlePanel);
		
		JPanel fileInfo = new JPanel();
		fileInfo.setLayout(null);
		fileInfo.setBounds(2, 279, 379, 42);
		fileInfo.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY));
		final JTextField filePath = new JTextField(200);
		filePath.setEditable(false);
		filePath.setBounds(2, 3, 300, 24);
		fileInfo.add(filePath);
		JButton browse = new JButton("浏览");
		browse.setFont(new Font("Microsoft YaHei UI", 0, 12));
		browse.setBounds(307, 3 ,65,24);

		final JPanel fileD = new JPanel();
		fileD.setBounds(2, 345, 380, 170);
		fileD.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY));
		fileD.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 15));
		updialog.add(fileD);
		
		//为浏览按钮添加监听器	
		browse.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser open = new JFileChooser(filePath.getText());
				int i = open.showOpenDialog(updialog);
				if (i == JFileChooser.APPROVE_OPTION) {
					fileD.removeAll();
					File selectedFile = open.getSelectedFile();
					if (selectedFile.getName().indexOf(" ")==-1 && selectedFile.exists()) {
						String path = selectedFile.getAbsolutePath();
						filePath.setText(path);
						//将信息设置到fileInfo
						JLabel[] infoTags = new FileInfo().getFileInfos(selectedFile);
						for (JLabel x: infoTags) {
							fileD.add(x);
						}
						fileD.updateUI();
					} else {
						//文件名中含空格
						JOptionPane.showMessageDialog(null, "文件不存在或文件名中有空格", "消息", JOptionPane.ERROR_MESSAGE);
					}
					
				}
			}
			
		});
		fileInfo.add(browse);
		updialog.add(fileInfo);
		
		//设置文件信息框
		JLabel fileDTitle = new JLabel("    -文件信息");                     //标题
		fileDTitle.setForeground(Color.DARK_GRAY);
		fileDTitle.setFont(new Font("Microsoft YaHei UI",0,12));
		ShadePanelH fileDTitlePanel = new ShadePanelH();
		fileDTitlePanel.setBounds(2, 323, 380, 20);
		fileDTitlePanel.setLayout(new BorderLayout());
		fileDTitlePanel.add(BorderLayout.NORTH,fileDTitle);
		fileDTitlePanel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY));
		updialog.add(fileDTitlePanel);

		//设置按钮
		JButton upload = new JButton("上传");
		upload.setFont(new Font("Microsoft YaHei UI", 0, 12));
		upload.setBounds(237, 535, 70, 30);
		upload.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (filePath.getText().length()!=0) {
					File file =  new File(filePath.getText());
					FileProgressStatus status = new FileProgressStatus(file.length());	
					//获取标签
					String tagString = TagStringMaker.getTagString(tagBoard);
					FileUploadProgress fup = new FileUploadProgress(status, updialog, file, tagString, MainFrame.user_id);
					Thread p = new Thread(fup);
					p.start();
					new FtpUpload(file, fup).start();
					
					
					
					updialog.setVisible(false);
					
				}
				
			}
			
		});
		updialog.add(upload);
		JButton cancel = new JButton("取消");
		cancel.setFont(new Font("Microsoft YaHei UI", 0, 12));
		cancel.setBounds(312, 535, 70, 30);
		cancel.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				updialog.setVisible(false);
			}
			
		});
		updialog.add(cancel);
		
		
		updialog.setLayout(null);

		
		updialog.setTitle("上传文件");
		updialog.setSize(390,600);
		updialog.setLocation(MainFrame.mainframe.getX()+updialog.getWidth()/2, 
				MainFrame.mainframe.getY()+updialog.getHeight()/2-300);
		updialog.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		updialog.setResizable(false);
		
		
		
		updialog.setVisible(true);
	}
	
	
	
}
  