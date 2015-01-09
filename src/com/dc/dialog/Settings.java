package com.dc.dialog;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.WindowConstants;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import com.dc.MainFrame;
import com.dc.dao.Dao;
import com.dc.model.Tag;
import com.dc.myframe.panel.ShadePanelV;

public class Settings extends JDialog {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2002973964924265013L;
	
	public Settings() { 
		super(MainFrame.mainframe);
		setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		setTitle("设置文件");
		setSize(350, 450);
		setLocation(MainFrame.mainframe.getX()+200, MainFrame.mainframe.getY()+5);
		
		JSplitPane managetags = new JSplitPane();
		managetags.setDividerLocation(135);
		JPanel lPane = new JPanel();
		lPane.setLayout(new BorderLayout());
		
		JLabel tagtitle = new JLabel("标签列表");
		tagtitle.setFont(new Font("Microsoft YaHei UI",0,12));
		tagtitle.setIcon(new ImageIcon(MainFrame.class.getResource("/image/icon_red.gif")));
		ShadePanelV tagtitlepanel = new ShadePanelV();
		tagtitlepanel.setLayout(new BorderLayout());
		tagtitlepanel.add(tagtitle);
		lPane.add(BorderLayout.NORTH, tagtitlepanel);
		
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
		
		JPanel rPane = new JPanel();
		rPane.setLayout(new BorderLayout());
		
		String[] columnNames = {"标签值"};
		Object[][] tableValue = new Object[0][1];
		DefaultTableModel dtm = new DefaultTableModel(tableValue, columnNames);
		final JTable table = new JTable(dtm);
		JScrollPane tableScroll = new JScrollPane(table);
		rPane.add(BorderLayout.CENTER, tableScroll);
		JPanel dbuttons = new JPanel();
		dbuttons.setLayout(new BorderLayout());
		JButton addrow = new JButton("添加标签值");
		addrow.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO 自动生成的方法存根
				Vector<String> row = new Vector<String>();
				DefaultTableModel dtm = (DefaultTableModel) table.getModel();
				dtm.addRow(row);
				table.setModel(dtm);
				
				table.repaint();
			}
			
		});
		addrow.setFont(new Font("Microsoft YaHei UI",0,12));
		dbuttons.add(BorderLayout.WEST, addrow);
		JButton save = new JButton("保存");
		
		save.setFont(new Font("Microsoft YaHei UI",0,12));
		dbuttons.add(BorderLayout.EAST, save);
		rPane.add(BorderLayout.SOUTH, dbuttons);
		
		final JTree tree = new JTree(root);
		//设置树的描绘器
		final DefaultTreeCellRenderer cellRenderer = (DefaultTreeCellRenderer) tree.getCellRenderer();
		cellRenderer.setOpenIcon(new ImageIcon(MainFrame.class.getResource("/image/tag.gif")));
		cellRenderer.setClosedIcon(new ImageIcon(MainFrame.class.getResource("/image/tag_closed.gif")));
		cellRenderer.setLeafIcon(new ImageIcon(MainFrame.class.getResource("/image/tag_value.gif")));
		tree.setCellRenderer(cellRenderer);
		tree.setRootVisible(false);
		tree.setFont(new Font("Microsoft YaHei UI",0,12));
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			@Override
			public void valueChanged(TreeSelectionEvent arg0) {
				// TODO 自动生成的方法存根
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
				if (node==null) {
					return;
				}
				if (!node.isLeaf()) {
					try {
						String[] name = Dao.getTagValue(node.toString());
						DefaultTableModel model = (DefaultTableModel) table.getModel();
						int rowcount = model.getRowCount();
						for (int i = 0; i < rowcount; i++) {
							if (i < name.length) {
								model.setValueAt(name[i], i, 0);
							} else {
								model.removeRow(name.length);
							}
						}
						
						for (int i = rowcount; i < name.length; i++) {
							Vector<Object> thisrow = new Vector<Object> ();
							thisrow.add(name[i]);
							model.addRow(thisrow);
						}
						
						table.setModel(model);
						table.updateUI();
					} catch (SQLException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}
					
				}
			}
		});
		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO 自动生成的方法存根
				String tag_name;
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
				if (((DefaultMutableTreeNode) node.getParent()).isRoot()) {
					tag_name = node.toString();
				} else {
					tag_name = node.getParent().toString();	
				}
				
				String[] result = new String[table.getRowCount()];
				for (int i = 0; i < table.getRowCount(); i++) {
					try {
						result[i] = table.getValueAt(i, 0).toString();
					} catch (Exception e){
						result[i] = "";
					}
				}
				Arrays.sort(result);
				String tagString = "";
				for (int i = 0; i < result.length; i++) {
					tagString = ";" + result[i] + tagString;
				}
				
				try {
					Dao.updateTag(tag_name, tagString);
				} catch (SQLException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}
			
		});
		
		
		JPanel buttons = new JPanel();
		buttons.setLayout(new GridLayout(1,2,0,0));
		JButton newtagb = new JButton("新建");
		newtagb.setFont(new Font("Microsoft YaHei UI",0,12));
		newtagb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO 自动生成的方法存根
				String tagname = JOptionPane.showInputDialog("请输入新建标签的名称");
				if (tagname == null) {
					return;
				}
				String[] tagvalue = new String[0];
				Tag newtag = new Tag(tagname, tagvalue);
				try {
					Dao.writeNewTag(newtag);
					Tag[] tags = null;
					tags = Dao.getTags();
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
					((DefaultTreeModel) tree.getModel()).setRoot(root);
					
					tree.repaint();
					
				} catch (SQLException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}
			
		});
		buttons.add(newtagb);
		JButton detagb = new JButton("删除");
		detagb.setFont(new Font("Microsoft YaHei UI",0,12));
		detagb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO 自动生成的方法存根
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
				if (node == null) {
					return;
				}
				if (((DefaultMutableTreeNode) node.getParent()).isRoot()) {
					try {
						Dao.deleteTag(node.toString());
						Tag[] tags = null;
						tags = Dao.getTags();
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
						((DefaultTreeModel) tree.getModel()).setRoot(root);
						
						tree.repaint();
					} catch (SQLException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}
					
				}
			}
			
		});
		buttons.add(detagb);
		lPane.add(BorderLayout.SOUTH, buttons);
		
		JScrollPane treeScroll = new JScrollPane(tree);
		lPane.add(BorderLayout.CENTER, treeScroll);
		
		
		managetags.setLeftComponent(lPane);
		
		
		managetags.setRightComponent(rPane);
		
		
		JTabbedPane pane = new JTabbedPane();
		pane.setFont(new Font("Microsoft YaHei UI",0,12));
		pane.addTab("标签管理  ", new ImageIcon(MainFrame.class.getResource("/image/themes.gif")), managetags);
		
		
		add(pane);
		setVisible(true);
	}
}
