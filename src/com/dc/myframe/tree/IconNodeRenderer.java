package com.dc.myframe.tree;

import java.awt.Component;
import java.io.File;
import java.io.IOException;

import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;  


public class IconNodeRenderer extends DefaultTreeCellRenderer {

	private static final long serialVersionUID = 1698232263394179735L;
	
	String subPath= "";
	
	public Component getTreeCellRendererComponent(JTree tree, 
			Object value,boolean sel, boolean expanded, boolean leaf, int row,boolean hasFocus) {
		
		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);  //调用父类该方法
		setText(value.toString());
		
		subPath = "";
		
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
		
		if (!node.isRoot()) {
			int index = node.toString().indexOf(".");
			if (index != -1) {
				String extension = node.toString().substring(index+1);
				setIcon(getExtensionIcon(extension));
			}

			
		}
		
		return this;
	}
	
	
	
	
	//得到指定后缀名的系统图标
	private Icon getExtensionIcon(String ext) {
		File file;
		Icon icon = null;
		try {
			file = File.createTempFile("icon", "."+ext);
			FileSystemView view = FileSystemView.getFileSystemView();
			icon = view.getSystemIcon(file);
			file.delete();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
		return icon;
	}
}

