package com.dc.dialog;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.WindowConstants;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.dc.myframe.tree.FtpTree;

public class PathSelect extends JDialog {	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7623279584433308410L;
	String selectedPath;
	
	public PathSelect(JFrame mother) {
		super(mother, true);
		setTitle("选择路径");
		setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		setSize(200,400);
		setLayout(new BorderLayout());
		setLocation(mother.getX()+300, mother.getY()-50);
		JTree tree = new FtpTree().tree;
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			@Override
			public void valueChanged(TreeSelectionEvent arg0) {
				TreePath path = arg0.getPath();
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
				if (node.isLeaf()) {
					selectedPath = "";
				} else {
					selectedPath = "/"+node.toString();
				}
				while (!node.isRoot()) {
					node = (DefaultMutableTreeNode)node.getParent();
					selectedPath = "/"+node.toString()+selectedPath;
				}
				selectedPath = selectedPath.substring(1, selectedPath.length());
			}
			
		});
		JScrollPane treeScroll = new JScrollPane(tree);
		add(BorderLayout.CENTER, treeScroll);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BorderLayout());
		JButton close = new JButton("确定");
		close.setFont(new Font("Microsoft YaHei UI",0,12));
		close.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				
			}
					
		});
		buttonPanel.add(BorderLayout.EAST, close);
		add(BorderLayout.SOUTH, buttonPanel);
		

	}
	
	public String getSelectedPath() {
		setVisible(true);
		
		return selectedPath;
	}
}
