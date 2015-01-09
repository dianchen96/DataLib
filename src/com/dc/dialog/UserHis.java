package com.dc.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import com.dc.dao.Dao;

public class UserHis extends JDialog{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3320331223692602013L;
	
	public UserHis(JFrame mother) {
		super(mother,true);
		setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		setTitle("用户记录");
		setSize(400, 450);
		setResizable(false);
		setLayout(new BorderLayout());
		setLocation(mother.getX()+150, mother.getY()+50);
		
		String[] users = null;
		try {
			users = Dao.getHisUsers();
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		final JComboBox<Object> userchoose = new JComboBox<Object>(users);
		
		add(BorderLayout.NORTH, userchoose);
		
		JPanel center = new JPanel();
		center.setLayout(new GridLayout(2,1,0,0));
		
		JPanel downPanel = new JPanel();
		downPanel.setLayout(new GridLayout(1,2,0,0));
		final JPanel tagBoard = new JPanel();
		tagBoard.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		final JScrollPane tagScroll = new JScrollPane(tagBoard);
		downPanel.add(tagScroll);
		final JTextArea type = new JTextArea();
		type.setLineWrap(true);
		type.setEditable(false);
		downPanel.add(type);
		
		
		final String[] columnNames = {"文件名","下载日期"}; 
		Object[][] tableValues = new Object[0][2];
		DefaultTableModel dtm = new DefaultTableModel(tableValues, columnNames);
		final JTable table = new JTable(dtm);
		table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getTableHeader().setReorderingAllowed(false);
		ListSelectionModel listmodel = table.getSelectionModel();
		listmodel.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				// TODO 自动生成的方法存根
				int row = table.getSelectedRow();
				String fileName = table.getValueAt(row, 0).toString();
				tagBoard.removeAll();
				try {
					String[] tags = Dao.getFileTagsDTLB(fileName);
					if (tags!=null) {
						for (int i = 0; i < tags.length; i++) {
							JToggleButton eachbutt = new JToggleButton(tags[i]);
							eachbutt.setEnabled(false);
							tagBoard.add(eachbutt);
						}
					}
					int count = tagBoard.getComponentCount();
					int width = tagScroll.getWidth()-10;
					int totalwidth = 0;
					for (int i = 0; i < count; i++) {
						totalwidth = totalwidth + tagBoard.getComponent(i).getWidth();
					}
					int arow = totalwidth / width + 1;
					int height = arow * 60;
					tagBoard.setPreferredSize(new Dimension(width, height));
					String anno = Dao.getAnnotateDTLB(fileName);
					type.setText(anno);
					
					tagBoard.updateUI();
					type.updateUI();
				} catch (Exception e) {
					
				}
			}
		});
		final JScrollPane tablePane = new JScrollPane(table);
		
		center.add(tablePane);
		center.add(downPanel);
		userchoose.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				// TODO 自动生成的方法存根
				String user = userchoose.getSelectedItem().toString();
				try {
					Object[][] value = Dao.getDownHis(user);
					DefaultTableModel model = (DefaultTableModel) table.getModel();
					int rowcount = model.getRowCount();
					for (int i = 0; i < rowcount; i++) {
						if (i < value.length) {
							for (int j = 0; j < value[i].length; j++) {
								model.setValueAt(value[i][j], i, j);
							}
						}
					}
					for (int i = rowcount; i < value.length; i++) {
						Vector<Object> thisrow = new Vector<Object> ();
						thisrow.add(value[i][0]);
						thisrow.add(value[i][1]);
						model.addRow(thisrow);
					}
					table.setModel(model);
					table.updateUI();
					
				} catch (SQLException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}
			
		});
		
		add(BorderLayout.CENTER, center);
		
		setVisible(true);
	}
}
