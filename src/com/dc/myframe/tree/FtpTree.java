//����չʾFTP�ļ��еĶ�����

package com.dc.myframe.tree;


import java.io.UnsupportedEncodingException;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreeSelectionModel;

import org.apache.commons.net.ftp.FTPFile;

import com.dc.ftp.FtpCon;


public class FtpTree{
	
	
	public JTree tree;
	
	//���ڴ����������ڵ��·��ֵ  
	String subPath = "";
	
	
	public FtpTree() {
		//���ø��ڵ�
		DefaultMutableTreeNode root = setRoot();
		//������
		tree = new JTree(root);
		tree.setRootVisible(false);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		//���������
		tree.setCellRenderer(new IconNodeRenderer());
	}
	
	//Ϊ���ڵ���ӽڵ�
	private DefaultMutableTreeNode setRoot() {
		DefaultMutableTreeNode root = new DefaultMutableTreeNode();
		addNode(root);
		
		return root;
	}
	
	//Ϊ����ӽڵ�
	private void addNode(DefaultMutableTreeNode node) {
		subPath = "";
		
		String pathname = getNodeFtpPath(node);
		FTPFile[] ftpFiles = null;
		ftpFiles = FtpCon.ftpdir(pathname);
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
