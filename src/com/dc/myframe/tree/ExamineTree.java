//审核界面用的树

package com.dc.myframe.tree;

import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.UnsupportedEncodingException;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.apache.commons.net.ftp.FTPFile;

import com.dc.dialog.Examine;
import com.dc.ftp.FtpCon;
import com.dc.ftp.FtpUpload;

public class ExamineTree {
	
	//待审核树
	public JTree un_tree;
	DefaultMutableTreeNode un_root;
    //已选择树	
	public JTree ed_tree;
	DefaultMutableTreeNode ed_root;
	
	//用于存放鼠标点击树节点的路径值  
	String subPath = "";
	//用于判断当前节点是否有子节点
	
	public ExamineTree() {
		//创建树
		un_root = setUNRoot();
		ed_root = setEDRoot();
		un_tree = new JTree(un_root);
		ed_tree = new JTree(ed_root);
		//设置根节点不可视
		un_tree.setRootVisible(false);
		ed_tree.setRootVisible(false);
        //设置单元描述器
		un_tree.setCellRenderer(new IconNodeRenderer());
		ed_tree.setCellRenderer(new IconNodeRenderer());
		//设置选择模式
		un_tree.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
		ed_tree.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
		
	}
	
	//为待审核根节点添加节点
	private DefaultMutableTreeNode setUNRoot() {
		un_root = new DefaultMutableTreeNode();
		addNode(un_root);
		//处理第一层空的节点
		for (int i = 0; i < un_root.getChildCount(); i++) {                   //由于这样处理，data文件夹的文件不能直接存放于根目录
			if (un_root.getChildAt(i).isLeaf()) {
				un_root.remove(i);
				i = 0;
			}
		}
		return un_root;
	}
	
	//为已选择根节点添加节点
	private DefaultMutableTreeNode setEDRoot() {
		ed_root = new DefaultMutableTreeNode();
		
		return ed_root;
	}
	
	//为树添加节点
	private void addNode(DefaultMutableTreeNode node) {
		subPath = "";
			
		String pathname = getNodeFtpPath(node);
		FTPFile[] ftpFiles = null;
		ftpFiles = FtpUpload.ftpdir(pathname);
		for (int i = 0; i < ftpFiles.length; i++) {
			DefaultMutableTreeNode newNode;

			try {
				newNode = new DefaultMutableTreeNode(new String(ftpFiles[i].getName().getBytes("iso-8859-1"),"utf-8"));
				node.add(newNode);
				if (ftpFiles[i].isDirectory()) {
					addNode(newNode);
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
			
	}
	
	//得到该节点的FTP路径
	private String getNodeFtpPath(DefaultMutableTreeNode node) {
			
		if (!node.isRoot()) {
			TreeNode[] fatherNodes = node.getPath();
			for (int i = 1; i < fatherNodes.length-1; i++) {
				subPath = subPath+fatherNodes[i].toString()+"/";
			}
			subPath = subPath+fatherNodes[fatherNodes.length-1].toString();
				
		}

		return subPath;
	}
}

