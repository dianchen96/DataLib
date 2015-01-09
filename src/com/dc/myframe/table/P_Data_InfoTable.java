package com.dc.myframe.table;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTable;

import com.dc.dao.Dao;

public class P_Data_InfoTable extends JPanel{
	
	String fileName;
	Object[][] tableValues;
	JTable table;
	//表头
	String[] columnNames = {"属性","值"};
	
	
	
	public P_Data_InfoTable(String fileName) {
		super();
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY));
		this.fileName = fileName;
		setValue();
		table = new JTable(tableValues, columnNames);
		add(BorderLayout.CENTER, table);
		
	}
	
	private void setValue() {
		try {
			tableValues = Dao.getPreDtlFile(fileName);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
}
