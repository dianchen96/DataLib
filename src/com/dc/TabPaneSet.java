package com.dc;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.dc.dao.Dao;
import com.dc.ftp.download.FtpDownload;
import com.dc.model.Tag;
import com.dc.myframe.FileDownloadProgress;
import com.dc.myframe.FileProgressStatus;
import com.dc.myframe.panel.ShadePanelH;

public class TabPaneSet {
	public static JTabbedPane setTabbedPane() throws SQLException {
		
		JTabbedPane tabPane = new JTabbedPane();
		
		
		final JPanel selected = new JPanel();
		selected.setLayout(new GridLayout(2,1,0,0));
		String[] columnNameV = {"文件名","文件类型","文件大小","上传时间","上传用户"};
		Object[][] tableValueV = new Object[50][5];
		MainFrame.selectedFiles = new JTable(tableValueV,columnNameV) {

			private static final long serialVersionUID = 1299380084252104158L;

			@Override
			public boolean isCellEditable(int row, int column) { 
			    return false;
		    }
		};
		final JTable selectedFiles = MainFrame.selectedFiles;
		
		
		selectedFiles.getTableHeader().setReorderingAllowed(false); 
		class filesPop extends JPopupMenu {
			/**
			 * 
			 */
			private static final long serialVersionUID = -3391191431940484790L;

			public filesPop() {
				super();
				JMenuItem download = new JMenuItem("下载所选文件");
				download.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent arg0) {
						int[] rows = selectedFiles.getSelectedRows();
						Vector<String> p_fileNames = new Vector<String>();
						long totalLength = 0;
						for (int i = 0; i < rows.length; i++) {	
							if (selectedFiles.getValueAt(rows[i], 0)!=null) {
								if (!selectedFiles.getValueAt(rows[i], 0).equals("")) {
									p_fileNames.add(selectedFiles.getValueAt(rows[i], 0).toString());
									totalLength = Integer.parseInt(selectedFiles.getValueAt(rows[i], 2).toString())+totalLength;
								}
							}
						}
						String[] fileNames = new String[p_fileNames.size()];
						for (int i = 0; i < fileNames.length; i++) {
							fileNames[i] = p_fileNames.get(i);
						}
						if (fileNames.length == 0) {
							return;
						}
						JFileChooser chooser = new JFileChooser();
						chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
						int i = chooser.showSaveDialog(selected);
						if (i == JFileChooser.APPROVE_OPTION) {
							String savePath = chooser.getSelectedFile().getAbsolutePath();
							FileProgressStatus status = new FileProgressStatus(totalLength);
							FileDownloadProgress fdp = new FileDownloadProgress(status, selected);
							Thread p = new Thread(fdp);
							p.start();
							new FtpDownload(fileNames, savePath, fdp).run();
						}
						
					}
					
				});
				add(download);
				
				JMenuItem delete = new JMenuItem("移除");
				delete.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						int[] rows = selectedFiles.getSelectedRows();
						TableModel model = selectedFiles.getModel();
						for (int i = 0; i < rows.length; i++) {
							for (int j = 0; j < model.getColumnCount(); j++) {
								model.setValueAt("", rows[i], j);
							}
						}
						selectedFiles.setModel(model);
					}
					
				});
				add(delete);
			}
		}
		selectedFiles.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (arg0.getButton() == 3) {
					new filesPop().show(selectedFiles, arg0.getX(), arg0.getY());
				}
			}
			@Override
			public void mouseEntered(MouseEvent arg0) {
			}
			@Override
			public void mouseExited(MouseEvent arg0) {}
			@Override
			public void mousePressed(MouseEvent arg0) {}
			@Override
			public void mouseReleased(MouseEvent arg0) {}
			
		});
		
		
		JScrollPane selectedScroll = new JScrollPane(selectedFiles);
		selected.add(selectedScroll);
		
		JPanel underInfo = new JPanel();
		underInfo.setLayout(new GridLayout(1,2,0,2));
		//标签面板
		JPanel tagPanel = new JPanel();
		tagPanel.setLayout(new BorderLayout());
		JLabel tagtitle = new JLabel ("文件标签");
		tagtitle.setIcon(new ImageIcon(MainFrame.class.getResource("/image/icon_blue.gif")));
		tagtitle.setFont(new Font("Microsoft YaHei UI",0,12));
		ShadePanelH tagtitlepanel = new ShadePanelH();
		tagtitlepanel.setLayout(new BorderLayout());
		tagtitlepanel.add(BorderLayout.NORTH, tagtitle);
		tagPanel.add(BorderLayout.NORTH, tagtitlepanel);
		final JPanel tagBoard = new JPanel();
		tagBoard.setLayout(new FlowLayout(FlowLayout.LEFT,5,5));
		final JScrollPane tagScrollPane = new JScrollPane(tagBoard);
		tagPanel.add(BorderLayout.CENTER, tagScrollPane);
		
		//注释面板
		final JPanel annopanel = new JPanel();
		annopanel.setLayout(new BorderLayout());
		JLabel annotitle = new JLabel("文件注释");
		annotitle.setFont(new Font("Microsoft YaHei UI",0,12));
		annotitle.setIcon(new ImageIcon(MainFrame.class.getResource("/image/icon_orange.gif")));
		ShadePanelH annotitlepanel = new ShadePanelH();
		annotitlepanel.setLayout(new BorderLayout());
		annotitlepanel.add(BorderLayout.NORTH, annotitle);
		annopanel.add(BorderLayout.NORTH, annotitlepanel);
		final JTextArea type = new JTextArea();
		type.setLineWrap(true);
		type.setEditable(false);
		type.setFont(new Font("Microsoft YaHei", 0, 12));
		annopanel.add(BorderLayout.CENTER, type);
		underInfo.add(tagPanel);
		underInfo.add(annopanel);
		ListSelectionModel model = selectedFiles.getSelectionModel();
		model.addListSelectionListener(new ListSelectionListener(){		
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				 int rows[] = selectedFiles.getSelectedRows();
				 if (rows.length == 1) {
					 if (selectedFiles.getValueAt(rows[0], 0)!=null) {
						 if (!selectedFiles.getValueAt(rows[0], 0).equals("")) {
							String fileName = selectedFiles.getValueAt(rows[0], 0).toString();
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
								int width = tagScrollPane.getWidth()-10;
								int totalwidth = 0;
								for (int i = 0; i < count; i++) {
									totalwidth = totalwidth + tagBoard.getComponent(i).getWidth();
								}
								int row = totalwidth / width + 1;
								int height = row * 60;
								tagBoard.setPreferredSize(new Dimension(width, height));
								String anno = Dao.getAnnotateDTLB(fileName);
								type.setText(anno);
								
								tagBoard.updateUI();
								type.updateUI();
							} catch (SQLException e) {
								// TODO 自动生成的 catch 块
								e.printStackTrace();
							}
					     } else {
					    	 tagBoard.removeAll();
							 type.setText("");
							 tagBoard.setPreferredSize(new Dimension(tagScrollPane.getWidth()-10, tagScrollPane.getHeight()-15));
							 tagBoard.updateUI();
							 type.updateUI();
					     }
					 } else {
						 tagBoard.removeAll();
						 type.setText("");
						 tagBoard.setPreferredSize(new Dimension(tagScrollPane.getWidth()-10, tagScrollPane.getHeight()-15));
						 tagBoard.updateUI();
						 type.updateUI();
					 }
				 } else {
					 tagBoard.removeAll();
					 type.setText("");
					 tagBoard.setPreferredSize(new Dimension(tagScrollPane.getWidth()-10, tagScrollPane.getHeight()-15));
					 tagBoard.updateUI();
					 type.updateUI();
				 }
			} 
		});
		selectedFiles.setSelectionModel(model);
		
		selected.add(underInfo);
		tabPane.addTab("已选中的文件", selected);
		
		JPanel simsearch = new JPanel();
		simsearch.setLayout(new GridLayout(2,1,0,0));
		//筛选面板
		JPanel filterpanel = new JPanel();
		filterpanel.setLayout(new BorderLayout());
		JPanel upPanel = new JPanel();
		upPanel.setLayout(new GridLayout(1,4,0,0));
		final JPanel StagBoard = new JPanel();
		StagBoard.setLayout(new FlowLayout(FlowLayout.LEFT,5,5));
		final JScrollPane StagScroll = new JScrollPane(StagBoard);		
		JPanel StagPanel = new JPanel();
		StagPanel.setLayout(new BorderLayout());
		StagPanel.add(BorderLayout.CENTER, StagScroll);
		JLabel Stagtitle = new JLabel("已选标签");
		Stagtitle.setFont(new Font("Microsoft YaHei UI",0,12));
		Stagtitle.setIcon(new ImageIcon(MainFrame.class.getResource("/image/icon_orange.gif")));
		ShadePanelH Stagtitlepanel = new ShadePanelH();
		Stagtitlepanel.setLayout(new BorderLayout());
		Stagtitlepanel.add(BorderLayout.NORTH, Stagtitle);
		StagPanel.add(BorderLayout.NORTH, Stagtitlepanel);
		StagPanel.add(BorderLayout.CENTER, StagScroll);
		
		
		JPanel StreePanel = new JPanel();
		StreePanel.setLayout(new BorderLayout());
		JButton delete = new JButton("移除标签");
		delete.setToolTipText("移除选中的标签");
		delete.setFont(new Font("Microsoft YaHei UI",0,12));
		delete.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Vector<JToggleButton> unselected = new Vector<JToggleButton>();
				for (int i = 0; i < StagBoard.getComponentCount(); i++) {
					JToggleButton eachbutton = (JToggleButton) StagBoard.getComponent(i);
					if (!eachbutton.getModel().isSelected()) {
						unselected.add(eachbutton);
					}
				}
				StagBoard.removeAll();
				for (int i = 0; i < unselected.size(); i++) {
					StagBoard.add(unselected.get(i));
				}
				int count = StagBoard.getComponentCount();
				int width = StagScroll.getWidth()-10;
				int totalwidth = 0;
				for (int i = 0; i < count; i++) {
					totalwidth = totalwidth + StagBoard.getComponent(i).getWidth();
				}
				int row = totalwidth / width + 1;
				int height = row * 60;
				StagBoard.setPreferredSize(new Dimension(width, height));
				StagBoard.updateUI();
			}
			
		});
		
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
		final JTree Stree = new JTree(root);
		//设置树的描绘器
		DefaultTreeCellRenderer cellRenderer = (DefaultTreeCellRenderer) Stree.getCellRenderer();
		cellRenderer.setOpenIcon(new ImageIcon(MainFrame.class.getResource("/image/tag.gif")));
		cellRenderer.setClosedIcon(new ImageIcon(MainFrame.class.getResource("/image/tag_closed.gif")));
		cellRenderer.setLeafIcon(new ImageIcon(MainFrame.class.getResource("/image/tag_value.gif")));
		Stree.setCellRenderer(cellRenderer);
		Stree.setRootVisible(false);
		Stree.setFont(new Font("Microsoft YaHei UI",0,12));
		Stree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		
		final JScrollPane StreeScroll = new JScrollPane(Stree);
		JLabel treetitle = new JLabel("标签列表");
		treetitle.setFont(new Font("Microsoft YaHei UI",0,12));
		treetitle.setIcon(new ImageIcon(MainFrame.class.getResource("/image/icon_blue.gif")));
		ShadePanelH treetitlepanel = new ShadePanelH();
		treetitlepanel.setLayout(new BorderLayout());
		treetitlepanel.add(BorderLayout.NORTH, treetitle);
		StreePanel.add(BorderLayout.NORTH, treetitlepanel);
		StreePanel.add(BorderLayout.CENTER, StreeScroll);
		StreePanel.add(BorderLayout.SOUTH, delete);
		Stree.addMouseListener(new MouseListener(){
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					TreePath path = Stree.getPathForLocation(e.getX(), e.getY());
					if (path != null) {
						DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) path.getLastPathComponent();
						if (selectedNode.isLeaf()) {
							DefaultMutableTreeNode tag_name = (DefaultMutableTreeNode) selectedNode.getParent();   //标签值
	                        String txt = tag_name.toString()+"："+selectedNode.toString();
							JToggleButton tag = new JToggleButton(txt);
							
							boolean flag = true;
							int count = StagBoard.getComponentCount();
							int width = StreeScroll.getWidth()-10;
							int totalwidth = 0;
							
							for (int i = 0; i < count; i++) {
								JToggleButton eachButton = (JToggleButton) StagBoard.getComponent(i);
								String eachtag_name = eachButton.getText().split("：")[0];
								if (tag_name.toString().equals(eachtag_name)) {
									totalwidth = totalwidth + StagBoard.getComponent(i).getWidth();
									flag = false;
								}
							}
							
							if (flag) {
								StagBoard.add(tag);
								
								int row = totalwidth / width + 1;
								int height = row * 60;
								StagBoard.setPreferredSize(new Dimension(width, height));
								StagBoard.updateUI();
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
		
		JPanel filterPanel = new JPanel();
		filterPanel.setLayout(new GridLayout(2,1,0,0));
		JPanel filter1 = new JPanel();
		filter1.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.LIGHT_GRAY));
		filter1.setLayout(new BorderLayout());
		JLabel guide1 = new JLabel("文件类型");
		guide1.setFont(new Font("Microsoft YaHei UI",0,12));
		guide1.setIcon(new ImageIcon(MainFrame.class.getResource("/image/icon_green.gif")));
		ShadePanelH guidepanel1 = new ShadePanelH();
		guidepanel1.setLayout(new BorderLayout());
		guidepanel1.add(BorderLayout.NORTH, guide1);
		String[] types = Dao.getAllFileType();
		final JComboBox<String> typechoose = new JComboBox<String>(types);
		filter1.add(BorderLayout.CENTER, typechoose);
		filter1.add(BorderLayout.NORTH, guidepanel1);
		
		filterPanel.add(filter1);
		JPanel filter2 = new JPanel();
		filter2.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.LIGHT_GRAY));
		filter2.setLayout(new BorderLayout());
		JLabel guide2 = new JLabel("上传用户");
		guide2.setFont(new Font("Microsoft YaHei UI",0,12));
		guide2.setIcon(new ImageIcon(MainFrame.class.getResource("/image/icon_green.gif")));
		ShadePanelH guidepanel2 = new ShadePanelH();
		guidepanel2.setLayout(new BorderLayout());
		guidepanel2.add(BorderLayout.NORTH, guide2);
		filter2.add(BorderLayout.NORTH, guidepanel2);
		String[] users = Dao.getAllUsers();
		final JComboBox<String> userchoose = new JComboBox<String>(users);
		filter2.add(BorderLayout.CENTER, userchoose);
		filterPanel.add(filter2);
		
		String columnName[] = {"文件名","文件类型","文件大小","上传时间","上传用户"};
		Object[][] tableValue = new Object[50][5];
		
		final JTable simsearchtable = new JTable(tableValue, columnName) {

			private static final long serialVersionUID = 8213056448247081567L;

			@Override
			public boolean isCellEditable(int row, int column) { 
			    return false;
		    }
		};
		simsearchtable.getTableHeader().setReorderingAllowed(false); 
		class searchPop extends JPopupMenu {

			private static final long serialVersionUID = -3096758621922489231L;

			public searchPop() {
				super();
				JMenuItem addItem = new JMenuItem("添加到已选");
				addItem.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent arg0) {
						int [] rows= simsearchtable.getSelectedRows();
						int index = 0;
						TableModel model = selectedFiles.getModel();
						for (int i = 0; i < model.getRowCount(); i++) {
							if (model.getValueAt(i, 0) == null) {
								index = i;
								break;
							} else {
								if(model.getValueAt(i, 0).equals("")) {
									index = i;
									break;
								}
							}
						}
						try {
							for (int i = 0; i < rows.length; i++) {
								boolean flag = true;
								String[] thisrow = new String[5];
								//判断用户与文件的级别
								String fileName = simsearchtable.getValueAt(rows[i], 0).toString();
								if (!Dao.isDownloadable(fileName, MainFrame.user_id)) {
									JOptionPane.showMessageDialog(null, fileName+"需要更高的用户权限，您无法下载");
									flag = false;
								}
								if (flag) {
									for (int j = 0; j < thisrow.length; j++) {
										model.setValueAt(simsearchtable.getValueAt(rows[i], j).toString(), index+i, j);
									}
								}
							}
							selectedFiles.setModel(model);
							selectedFiles.updateUI();
						} catch (Exception e) {
							
						}
					}
					
				});
				add(addItem);
			}
		}
		simsearchtable.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (arg0.getButton()==3) {
					new searchPop().show(simsearchtable, arg0.getX(), arg0.getY());
				}
			}
			@Override
			public void mouseEntered(MouseEvent arg0) {}
			@Override
			public void mouseExited(MouseEvent arg0) {}
			@Override
			public void mousePressed(MouseEvent arg0) {}
			@Override
			public void mouseReleased(MouseEvent arg0) {}
			
		});
		JScrollPane simsearchScroll = new JScrollPane(simsearchtable);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BorderLayout());
		JPanel lowpanel = new JPanel();
		lowpanel.setLayout(new FlowLayout(FlowLayout.RIGHT,5,5));
		JButton search = new JButton("搜索");
		search.setFont(new Font("Microsoft YaHei UI",0,12));
		lowpanel.add(search);
		search.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				//处理标签
				Vector<String> p_tagString = new Vector<String>();
				for (int i = 0; i < StagBoard.getComponentCount(); i++) {
					JToggleButton butt = (JToggleButton) StagBoard.getComponent(i);
					p_tagString.add(butt.getText());
				}
				String tagString[] = new String[p_tagString.size()];
				for (int i = 0; i < tagString.length; i++) {
					tagString[i] = p_tagString.get(i);
				}
				//处理筛选
				String fileType = typechoose.getSelectedItem().toString();
				String upLoader = userchoose.getSelectedItem().toString();
				
				String[][] result = null;
				try {

					result = Dao.search(tagString, fileType, upLoader);
				} catch (SQLException e1) {
					// TODO 自动生成的 catch 块
					e1.printStackTrace();
				}

				TableModel model = simsearchtable.getModel();
				for (int i = 0; i < result.length; i++) {
					for (int j = 0; j < result[i].length; j++) {
						model.setValueAt(result[i][j], i, j);
					}
					
				}
				for (int i = result.length; i < model.getRowCount(); i++) {
					for (int j = 0; j < model.getColumnCount(); j++) {
						model.setValueAt("", i, j);
					}
				}
				simsearchtable.setModel(model);
				simsearchtable.updateUI();
			}
			
		});
		JButton clear = new JButton("清空");
		clear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				StagBoard.removeAll();
				StagBoard.setPreferredSize(new Dimension(StagScroll.getWidth()-10,StagScroll.getHeight()-15));
				typechoose.setSelectedItem("");
				userchoose.setSelectedItem("");
				TableModel model = simsearchtable.getModel();
				for (int i = 0; i < model.getRowCount(); i++) {
					for (int j = 0; j < model.getColumnCount(); j++) {
						model.setValueAt("", i, j);
					}
				}
				simsearchtable.setModel(model);
				simsearchtable.updateUI();
				StagBoard.updateUI();
			}
			
		});
		clear.setFont(new Font("Microsoft YaHei UI",0,12));
		lowpanel.add(clear);
		buttonPanel.add(BorderLayout.SOUTH, lowpanel);
		
		upPanel.add(StreePanel);
		upPanel.add(StagPanel);
		upPanel.add(filterPanel);
		upPanel.add(buttonPanel);
		
		simsearch.add(upPanel);
		
		
		simsearch.add(simsearchScroll);
		
		tabPane.addTab("标签搜索", new ImageIcon(MainFrame.class.getResource("/image/search_p.png")), simsearch);
		
		
		
		
		JPanel annosearch = new JPanel();
		annosearch.setLayout(new GridLayout(2,1,0,0));
		JPanel aupPanel = new JPanel();
		aupPanel.setLayout(new GridLayout(1,3,0,0));
		final JTextArea atype = new JTextArea();
		atype.setLineWrap(true);
		atype.setFont(new Font("Microsoft YaHei", 0, 12));
		JPanel anno = new JPanel(new BorderLayout());
		JLabel aannotitle = new JLabel("文件注释");
		aannotitle.setFont(new Font("Microsoft YaHei UI",0,12));
		aannotitle.setIcon(new ImageIcon(MainFrame.class.getResource("/image/icon_orange.gif")));
		ShadePanelH aannotitlepanel = new ShadePanelH();
		aannotitlepanel.setLayout(new BorderLayout());
		aannotitlepanel.add(BorderLayout.NORTH, aannotitle);
		anno.add(BorderLayout.NORTH, aannotitlepanel);
		anno.add(BorderLayout.CENTER, atype);
		aupPanel.add(anno);
		
		JPanel afilter = new JPanel();
		afilter.setLayout(new GridLayout(2,1,0,0));
		JPanel afiletypepanel = new JPanel();
		afiletypepanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.LIGHT_GRAY));
		afiletypepanel.setLayout(new BorderLayout());
		JLabel afiletypetitle = new JLabel("文件类型");
		afiletypetitle.setFont(new Font("Microsoft YaHei UI",0,12));
		afiletypetitle.setIcon(new ImageIcon(MainFrame.class.getResource("/image/icon_green.gif")));
		ShadePanelH afiletypetitlepanel = new ShadePanelH();
		afiletypetitlepanel.setLayout(new BorderLayout());
		afiletypetitlepanel.add(BorderLayout.NORTH, afiletypetitle);
		afiletypepanel.add(BorderLayout.NORTH, afiletypetitlepanel);
		final JComboBox<String> atypechoose = new JComboBox<String>(types);
		afiletypepanel.add(BorderLayout.CENTER, atypechoose);
		
		JPanel auploaderpanel = new JPanel();
		auploaderpanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.LIGHT_GRAY));
		auploaderpanel.setLayout(new BorderLayout());
		JLabel auploadertitle = new JLabel("上传用户");
		auploadertitle.setFont(new Font("Microsoft YaHei UI",0,12));
		auploadertitle.setIcon(new ImageIcon(MainFrame.class.getResource("/image/icon_green.gif")));
		ShadePanelH auploadertitlepanel = new ShadePanelH();
		auploadertitlepanel.setLayout(new BorderLayout());
		auploadertitlepanel.add(BorderLayout.NORTH, auploadertitle);
		auploaderpanel.add(BorderLayout.NORTH, auploadertitlepanel);
		final JComboBox<String> auploader = new JComboBox<String>(users);
		auploaderpanel.add(BorderLayout.CENTER, auploader);
		
		
		afilter.add(afiletypepanel);
		afilter.add(auploaderpanel);
		aupPanel.add(afilter);
		
		final JTable asearchtable = new JTable(tableValue, columnName) {

			private static final long serialVersionUID = 3746271623153699164L;

			@Override
			public boolean isCellEditable(int row, int column) { 
			    return false;
		    }
		};
		asearchtable.getTableHeader().setReorderingAllowed(false); 
		
		JPanel buttonpanel = new JPanel();
		buttonpanel.setLayout(new BorderLayout());
		JPanel south = new JPanel();
		south.setLayout(new FlowLayout(FlowLayout.RIGHT,5,5));
		JButton asearch = new JButton("搜索");
		asearch.setFont(new Font("Microsoft YaHei UI",0,12));
		asearch.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO 自动生成的方法存根
				//处理筛选
				String fileType = atypechoose.getSelectedItem().toString();
				String upLoader = auploader.getSelectedItem().toString();
				String annoTate = atype.getText();
				
				String[][] result = null;
				try {

					result = Dao.search(annoTate, fileType, upLoader);
				} catch (SQLException e1) {
					// TODO 自动生成的 catch 块
					e1.printStackTrace();
				}

				TableModel model = asearchtable.getModel();
				for (int i = 0; i < result.length; i++) {
					for (int j = 0; j < result[i].length; j++) {
						model.setValueAt(result[i][j], i, j);
					}
					
				}
				for (int i = result.length; i < model.getRowCount(); i++) {
					for (int j = 0; j < model.getColumnCount(); j++) {
						model.setValueAt("", i, j);
					}
				}
				asearchtable.setModel(model);
				asearchtable.updateUI();
			}
			
		});
		south.add(asearch);
		JButton aclear = new JButton("清空");
		aclear.setFont(new Font("Microsoft YaHei UI",0,12));
		aclear.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO 自动生成的方法存根
				atype.setText("");
				atypechoose.setSelectedItem("");
				auploader.setSelectedItem("");
				TableModel model = asearchtable.getModel();
				for (int i = 0; i < model.getRowCount(); i++) {
					for (int j = 0; j < model.getColumnCount(); j++) {
						model.setValueAt("", i, j);
					}
				}
				asearchtable.setModel(model);
				asearchtable.updateUI();
			}
			
		});
		south.add(aclear);
		buttonpanel.add(BorderLayout.SOUTH, south);
		aupPanel.add(buttonpanel);
		
		class asearchPop extends JPopupMenu {

			private static final long serialVersionUID = -402700165918212487L;

			public asearchPop() {
				super();
				JMenuItem addItem = new JMenuItem("添加到已选");
				addItem.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent arg0) {
						int [] rows= asearchtable.getSelectedRows();
						int index = 0;
						TableModel model = selectedFiles.getModel();
						for (int i = 0; i < model.getRowCount(); i++) {
							if (model.getValueAt(i, 0) == null) {
								index = i;
								break;
							} else {
								if(model.getValueAt(i, 0).equals("")) {
									index = i;
									break;
								}
							}
						}
						try {
							for (int i = 0; i < rows.length; i++) {
								boolean flag = true;
								String[] thisrow = new String[5];
								//判断用户与文件的级别
								String fileName = simsearchtable.getValueAt(rows[i], 0).toString();
								if (!Dao.isDownloadable(fileName, MainFrame.user_id)) {
									JOptionPane.showMessageDialog(null, fileName+"需要更高的用户权限，您无法下载");
									flag = false;
								}
								if (flag) {
									for (int j = 0; j < thisrow.length; j++) {
										model.setValueAt(simsearchtable.getValueAt(rows[i], j).toString(), index+i, j);
									}
								}
							}
							selectedFiles.setModel(model);
							selectedFiles.updateUI();
						} catch (Exception e) {
							
						}
					}
					
				});
				add(addItem);
			}
		}
		asearchtable.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (arg0.getButton()==3) {
					new asearchPop().show(asearchtable, arg0.getX(), arg0.getY());
				}
			}
			@Override
			public void mouseEntered(MouseEvent arg0) {}
			@Override
			public void mouseExited(MouseEvent arg0) {}
			@Override
			public void mousePressed(MouseEvent arg0) {}
			@Override
			public void mouseReleased(MouseEvent arg0) {}
			
		});
		JScrollPane asearchScroll = new JScrollPane(asearchtable);
		
		annosearch.add(aupPanel);
		annosearch.add(asearchScroll);
		
		
		tabPane.addTab("注释搜索", new ImageIcon(MainFrame.class.getResource("/image/search_m_p.png")), annosearch);
		
		
		return tabPane;
	}
}
