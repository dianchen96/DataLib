//��˽����õ���

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
	
	//�������
	public JTree un_tree;
	DefaultMutableTreeNode un_root;
    //��ѡ����	
	public JTree ed_tree;
	DefaultMutableTreeNode ed_root;
	
	//���ڴ����������ڵ��·��ֵ  
	String subPath = "";
	//�����жϵ�ǰ�ڵ��Ƿ����ӽڵ�
	
	public ExamineTree() {
		//������
		un_root = setUNRoot();
		ed_root = setEDRoot();
		un_tree = new JTree(un_root);
		ed_tree = new JTree(ed_root);
		//���ø��ڵ㲻����
		un_tree.setRootVisible(false);
		ed_tree.setRootVisible(false);
        //���õ�Ԫ������
		un_tree.setCellRenderer(new IconNodeRenderer());
		ed_tree.setCellRenderer(new IconNodeRenderer());
		//����ѡ��ģʽ
		un_tree.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
		ed_tree.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
		
	}
	
	//Ϊ����˸��ڵ���ӽڵ�
	private DefaultMutableTreeNode setUNRoot() {
		un_root = new DefaultMutableTreeNode();
		addNode(un_root);
		//�����һ��յĽڵ�
		for (int i = 0; i < un_root.getChildCount(); i++) {                   //������������data�ļ��е��ļ�����ֱ�Ӵ���ڸ�Ŀ¼
			if (un_root.getChildAt(i).isLeaf()) {
				un_root.remove(i);
				i = 0;
			}
		}
		return un_root;
	}
	
	//Ϊ��ѡ����ڵ���ӽڵ�
	private DefaultMutableTreeNode setEDRoot() {
		ed_root = new DefaultMutableTreeNode();
		
		return ed_root;
	}
	
	//Ϊ����ӽڵ�
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
	
	//�õ��ýڵ��FTP·��
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

