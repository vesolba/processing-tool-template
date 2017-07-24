package dbmanager;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Box;
import javax.swing.DropMode;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import javafx.scene.layout.Pane;

@SuppressWarnings("serial")
public class ExecSQLPanel extends JPanel {

	private JSpinner spinFontSize;
	private JSpinner spinTable;
	private JTextField txtLastMessage;
	private JTable tableSQLResult;
	private static JTextPane txtSelected;
	private JTextPane textPaneInSQL;
	private JTextField textEditingElement;
	private String lastSelect = "";
	private String propFont = "";

	public static String SENTENCES_DELIMITER = ";";

	public ExecSQLPanel() {
		jbInit();
	}

	private void jbInit() {
		setLayout(new BorderLayout(0, 0));
		JSplitPane splPanExecSQL = new JSplitPane();
		splPanExecSQL.setResizeWeight(0.4);
		splPanExecSQL.setOrientation(JSplitPane.VERTICAL_SPLIT);
		add(splPanExecSQL);

		JPanel upperPanel = new JPanel();
		upperPanel.setAlignmentX(Component.RIGHT_ALIGNMENT);
		splPanExecSQL.setLeftComponent(upperPanel);
		upperPanel.setLayout(new BorderLayout(0, 0));

		JMenuBar menuBar_1 = new JMenuBar();
		menuBar_1.setAlignmentX(Component.RIGHT_ALIGNMENT);
		menuBar_1.setAlignmentY(Component.CENTER_ALIGNMENT);
		upperPanel.add(menuBar_1, BorderLayout.NORTH);

		JLabel lblWriteSQL = new JLabel(" Data From: ");
		menuBar_1.add(lblWriteSQL);

		txtSelected = new JTextPane();
		menuBar_1.add(txtSelected);

		Component horizontalGlue = Box.createHorizontalGlue();
		menuBar_1.add(horizontalGlue);

		JSeparator separator_3 = new JSeparator();
		separator_3.setOrientation(SwingConstants.VERTICAL);
		menuBar_1.add(separator_3);

		JMenu mnQuery = new JMenu("Query");
		mnQuery.setHorizontalAlignment(SwingConstants.RIGHT);
		menuBar_1.add(mnQuery);

		JMenuItem mntmSELECTgral = new JMenuItem("SELECT * FROM ");
		mntmSELECTgral.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				textPaneInSQL.setText(mntmSELECTgral.getText());
			}
		});
		mnQuery.add(mntmSELECTgral);

		JMenuItem mntmFullSelect = new JMenuItem("Full Select");
		mntmFullSelect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		mnQuery.add(mntmFullSelect);

		JSeparator separator = new JSeparator();
		separator.setMaximumSize(new Dimension(0, 1000));
		separator.setOrientation(SwingConstants.VERTICAL);
		menuBar_1.add(separator);

		JMenu mnUpdate = new JMenu("Update");
		mnUpdate.setHorizontalAlignment(SwingConstants.RIGHT);
		menuBar_1.add(mnUpdate);

		JMenuItem mntmFullUpdate = new JMenuItem("Full Update");
		mntmFullUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textPaneInSQL.setText(" UPDATE tableName [ [ AS ] correlationName ] ] \n"
						+ "SET columnName = value       \n" + " [ , columnName = value ]* \n" + "	[ WHERE clause ] \n"
						+ "  |  \n" + "UPDATE tableName \n" + "SET columnName = value \n"
						+ "[ , columnName = value ]*  \n" + "WHERE CURRENT OF ");
			}
		});
		mnUpdate.add(mntmFullUpdate);

		JMenuItem mntmFullInsert = new JMenuItem("Full Insert");
		mntmFullInsert.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textPaneInSQL.setText("");
			}
		});
		mnUpdate.add(mntmFullInsert);

		JSeparator separator_1 = new JSeparator();
		separator_1.setMaximumSize(new Dimension(0, 32767));
		separator_1.setOrientation(SwingConstants.VERTICAL);
		menuBar_1.add(separator_1);

		JMenu mnStructure = new JMenu("Structure");
		mnStructure.setHorizontalAlignment(SwingConstants.RIGHT);
		menuBar_1.add(mnStructure);

		JSeparator separator_4 = new JSeparator();
		separator_4.setOrientation(SwingConstants.VERTICAL);
		separator_4.setMaximumSize(new Dimension(0, 32767));
		menuBar_1.add(separator_4);

		JMenu mnOthers = new JMenu("Others");
		mnOthers.setHorizontalAlignment(SwingConstants.RIGHT);
		menuBar_1.add(mnOthers);

		JSeparator separator_2 = new JSeparator();
		separator_2.setSize(new Dimension(100, 0));
		separator_2.setMinimumSize(new Dimension(100, 0));
		separator_2.setMaximumSize(new Dimension(100, 32767));
		separator_2.setOrientation(SwingConstants.VERTICAL);
		menuBar_1.add(separator_2);

		spinFontSize = new JSpinner();
		spinFontSize.setModel(new SpinnerNumberModel(new Integer(12), null, null, new Integer(1)));
		menuBar_1.add(spinFontSize);

		spinFontSize.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				textPaneInSQL.setFont(
						textPaneInSQL.getFont().deriveFont(Float.parseFloat(spinFontSize.getValue().toString())));

				DBManager.propsDBM.setDBMProp("fontsize01", spinFontSize.getValue().toString());
				DBManager.propsDBM.saveProperties();
			}
		});
		textPaneInSQL = new JTextPane();
		textPaneInSQL.setDropMode(DropMode.INSERT);
		if (DBManager.propsDBM != null) {
			propFont = DBManager.propsDBM.getDBMProp("fontsize01");
			if (propFont != null && !propFont.isEmpty()) {
				spinFontSize.setValue(Integer.parseInt(propFont));
				textPaneInSQL.setFont(
						textPaneInSQL.getFont().deriveFont(Float.parseFloat(spinFontSize.getValue().toString())));
			}
		}

		JScrollPane scrollPane_1 = new JScrollPane(textPaneInSQL);

		JPopupMenu popupInSQL = new JPopupMenu();
		addPopup(textPaneInSQL, popupInSQL);

		JMenuItem mntmPaste = new JMenuItem("Paste");
		mntmPaste.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textPaneInSQL.paste();
			}
		});

		JMenuItem mntCut = new JMenuItem("Cut");
		mntCut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textPaneInSQL.cut();
			}
		});
		popupInSQL.add(mntCut);

		JMenuItem mntmCopy = new JMenuItem("Copy");
		mntmCopy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textPaneInSQL.copy();
			}
		});
		popupInSQL.add(mntmCopy);
		popupInSQL.add(mntmPaste);

		JSeparator separator_5 = new JSeparator();
		popupInSQL.add(separator_5);

		JMenuItem mntmClear = new JMenuItem("Clear");
		mntmClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textPaneInSQL.setText("");
			}
		});
		popupInSQL.add(mntmClear);
		upperPanel.add(scrollPane_1, BorderLayout.CENTER);

		Box horizontalBox = Box.createHorizontalBox();
		upperPanel.add(horizontalBox, BorderLayout.SOUTH);

		JButton btnExecSQL = new JButton(" Execute ");
		btnExecSQL.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		btnExecSQL.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				executeSQL(textPaneInSQL.getText(), "MODE_FILL");
			}
		});

		horizontalBox.add(btnExecSQL);

		Component horizontalGlue_1 = Box.createHorizontalGlue();
		horizontalBox.add(horizontalGlue_1);

		JLabel lblLastMessage = new JLabel("   Last Message: ");
		horizontalBox.add(lblLastMessage);

		txtLastMessage = new JTextField();
		horizontalBox.add(txtLastMessage);
		txtLastMessage.setColumns(10);

		JPanel lowPanel = new JPanel();
		splPanExecSQL.setRightComponent(lowPanel);
		lowPanel.setLayout(new BorderLayout(0, 0));

		tableSQLResult = new JTable() {
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
				Component c = super.prepareRenderer(renderer, row, column);

				int rendererWidth = c.getPreferredSize().width;
				TableColumn tableColumn = getColumnModel().getColumn(column);
				tableColumn.setPreferredWidth(
						Math.max(rendererWidth + getIntercellSpacing().width, tableColumn.getPreferredWidth()));

				// Alternate row color
				if (!isRowSelected(row))
					c.setBackground(row % 2 == 0 ? getBackground() : Color.LIGHT_GRAY);

				return c;
			}
		};

		// tableSQLResult.getModel().addTableModelListener(this);

		tableSQLResult.setIntercellSpacing(new Dimension(10, 2));
		tableSQLResult.setAutoscrolls(false);
		tableSQLResult.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tableSQLResult.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		tableSQLResult.setCellSelectionEnabled(true);
		tableSQLResult.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableSQLResult.setBackground(SystemColor.info);

		if (DBManager.propsDBM != null) {
			String propTable = DBManager.propsDBM.getDBMProp("spinTable");

			spinTable = new JSpinner();
			spinTable.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent arg0) {
					tableSQLResult.setFont(
							tableSQLResult.getFont().deriveFont(Float.parseFloat(spinTable.getValue().toString())));
					tableSQLResult.setRowHeight((int) Float.parseFloat(spinTable.getValue().toString()) + 5);
					DBManager.propsDBM.setDBMProp("spinTable", spinTable.getValue().toString());
					DBManager.propsDBM.saveProperties();
				}
			});
			spinTable.setToolTipText("Table Font Size");
			spinTable.setModel(new SpinnerNumberModel(new Integer(12), null, null, new Integer(1)));
			horizontalBox.add(spinTable);
			propTable = DBManager.propsDBM.getDBMProp("spinTable");

			if (propTable != null && !propTable.isEmpty()) {
				spinTable.setValue(Integer.parseInt(propTable));
				tableSQLResult.setFont(
						tableSQLResult.getFont().deriveFont(Float.parseFloat(spinTable.getValue().toString())));
				tableSQLResult.setRowHeight((int) Float.parseFloat(spinTable.getValue().toString()) + 2);
			}
		}
		JScrollPane scrollPane = new JScrollPane(tableSQLResult);
		scrollPane.setAutoscrolls(true);
		scrollPane.setViewportBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		lowPanel.add(scrollPane, BorderLayout.CENTER);

		JToolBar editTableToolsBar = new JToolBar();
		editTableToolsBar.setPreferredSize(new Dimension(1000, 30));
		editTableToolsBar.setMinimumSize(new Dimension(100, 50));
		editTableToolsBar.setMaximumSize(new Dimension(1000, 50));
		lowPanel.add(editTableToolsBar, BorderLayout.SOUTH);

		JButton btnDeleteRow = new JButton(" Delete Row ");
		btnDeleteRow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				// MyTableModel model = (MyTableModel) tableSQLResult.getModel();
				MyTableModel model = (MyTableModel) tableSQLResult.getModel();
				int rowSel = tableSQLResult.getSelectedRow();

				String text2Delete = " DELETE FROM " + textEditingElement.getText() + " WHERE ";

				boolean isFirst = true;

				int numCols = model.getColumnCount();
				for (int i = 0; i < numCols; i++) {
					if (model.colsSearchable.get(i)) {
						if (isFirst) {
							isFirst = false;
						} else {
							text2Delete += " AND ";
						}

						Object redValue = model.getValueAt(rowSel, i);
						System.out.println(" redvalue " + redValue);

						text2Delete += model.getColumnName(i) + " = ";
						String quotePrefix = (String) DBManager.dataTypeInfo(model.colsTypes.get(i), "LITERAL_PREFIX");
						String quoteSuffix = (String) DBManager.dataTypeInfo(model.colsTypes.get(i), "LITERAL_SUFFIX");
						if (quotePrefix != null)
							text2Delete += quotePrefix;
						text2Delete += redValue;
						if (quoteSuffix != null)
							text2Delete += quoteSuffix;
					}
				}

				text2Delete += ";";

				int deleteConfirm = JOptionPane.showConfirmDialog(btnDeleteRow,
						"Do you want really to send this command: \n" + text2Delete + " ?", "Delete?",
						JOptionPane.YES_NO_OPTION);

				if (deleteConfirm == JOptionPane.YES_OPTION) {
					System.out.println(" text2delete: " + text2Delete);

					executeSQL(text2Delete, "MODE_FILL");
					executeSQL(lastSelect, "MODE_FILL"); // To refresh JTable

				}
			}
		});
		btnDeleteRow.setPreferredSize(new Dimension(30, 0));
		btnDeleteRow.setMinimumSize(new Dimension(93, 50));
		btnDeleteRow.setMaximumSize(new Dimension(300, 50));
		btnDeleteRow.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		editTableToolsBar.add(btnDeleteRow);

		JButton btnInsertRow = new JButton(" Insert Row ");
		btnInsertRow.setPreferredSize(new Dimension(30, 0));
		btnInsertRow.setMaximumSize(new Dimension(300, 50));
		btnInsertRow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String query = "SELECT * FROM " + textEditingElement.getText();
				executeSQL(query, "MODE_NEW_ROW");
				tableSQLResult.editCellAt(tableSQLResult.getSelectedRow(), tableSQLResult.getSelectedColumn());
			}
		});
		btnInsertRow.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		editTableToolsBar.add(btnInsertRow);

		JButton btnUpdateRow = new JButton(" Edit Row ");
		btnUpdateRow.setPreferredSize(new Dimension(30, 0));
		btnUpdateRow.setMaximumSize(new Dimension(300, 50));
		btnUpdateRow.setHorizontalTextPosition(SwingConstants.CENTER);
		btnUpdateRow.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		editTableToolsBar.add(btnUpdateRow);

		JLabel lblNewLabel = new JLabel(" Table: ");
		lblNewLabel.setMaximumSize(new Dimension(60, 50));
		editTableToolsBar.add(lblNewLabel);

		textEditingElement = new JTextField();
		textEditingElement.setMinimumSize(new Dimension(20, 20));
		textEditingElement.setMaximumSize(new Dimension(1000, 50));
		editTableToolsBar.add(textEditingElement);
		textEditingElement.setColumns(10);

	}

	/**
	 * @return the txtSelected
	 */
	public static JTextPane getTxtSelected() {
		return txtSelected;
	}

	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}

			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}

			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}

	/**
	 * @return the textEditingElement
	 */
	public JTextField getTextEditingElement() {
		return textEditingElement;
	}

	/**
	 * @return the tableSQLResult
	 */
	public JTable getTableSQLResult() {
		return tableSQLResult;
	}

	/**
	 * @return the textPaneInSQL
	 */
	public JTextPane getTextPaneInSQL() {
		return textPaneInSQL;
	}

	/**
	 * @return the txtLastMessage
	 */
	public JTextField getTxtLastMessage() {
		return txtLastMessage;
	}

	/**
	 * Executes SQL commands from textPaneInSQL
	 *
	 * @param redQuery
	 *            text red in textPaneInSQL.
	 *
	 * @param mode
	 *            can be: MODE_FILL -- Fills the table with data from database
	 *            MODE_NEW_ROW -- Creates an editable row
	 */
	public void executeSQL(String redQuery, String mode) {
		Connection conn = null;
		try {
			conn = DBConnect.connect(!DBConnect.serverIsOn, getTxtSelected().getText(), "", null, false);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		System.out.println("redQuery: " + redQuery);

		if ("SELECT ".equalsIgnoreCase(redQuery.trim().substring(0, 7))) {

			lastSelect = redQuery;
			MyTableModel tModel = new MyTableModel(conn, redQuery.trim(), mode);
			tableSQLResult.setModel(tModel);
			tableSQLResult.updateUI();

		} else {
			if (redQuery != null && !redQuery.equals("")) {

				String sqlSentence = "";
				int inicIndex = 0;

				int endIndex = redQuery.indexOf(SENTENCES_DELIMITER);

				if (endIndex > inicIndex) {
					sqlSentence = redQuery.substring(inicIndex, endIndex);
				} else {
					sqlSentence = redQuery;
				}

				while (sqlSentence.length() > 0) {

					try {
						Statement statement = conn.createStatement();
						int c = statement.executeUpdate(sqlSentence);
						txtLastMessage.setText(c + "registers updated");
					} catch (SQLException ex) {
						/// Logger.getLogger(ExecSQLPanel.class.getName()).log(Level.SEVERE, null, ex);
						ex.printStackTrace();
					}

					inicIndex = endIndex + 1;
					endIndex = redQuery.indexOf(SENTENCES_DELIMITER, inicIndex);
					sqlSentence = "";

					if (endIndex > inicIndex) {
						sqlSentence = redQuery.substring(inicIndex, endIndex);
					}
				}

				// ((DefaultTreeModel) DBManager.dBtree.getModel()).reload();
				DBManager.dBtree.repaint();
				textPaneInSQL.setText("");

			}
		}
	}

	// @Override
	// public void tableChanged(TableModelEvent arg0) {
	//
	// int rowFirstIndex = arg0.getFirstRow();
	// int rowLastIndex = arg0.getLastRow();
	// int column = arg0.getColumn();
	//
	// MyTableModel model = (MyTableModel) arg0.getSource();
	// String columnName = model.getColumnName(column);
	// Object data = model.getValueAt(rowFirstIndex, column);
	//
	// if (arg0.getType() == TableModelEvent.DELETE) {
	//
	// if (arg0.getType() == TableModelEvent.UPDATE) {
	// int updatedColIndex = arg0.getColumn();
	// String updateColmn = txtSelected.getColumnName(updatedColIndex);
	// String updatedValue = (String) model.getValueAt(rowFirstIndex,
	// updatedColIndex);
	// System.out.println("column: " + updateColmn + " value: " + updatedValue);
	// updateDB(updateColmn, updatedValue);
	// uprs.updateFloat("PRICE", f * percentage);
	// uprs.updateRow();
	// }
	//
	// else if (arg0.getType() == TableModelEvent.INSERT) {
	// for (int i = rowFirstIndex; i <= rowLastIndex; i++) {
	// Vector rowData = (Vector) model.getDataVector().get(i);
	//
	// Map<String, String> dataMap = new HashMap<>();
	//
	// for (int j = 0; j < rowData.size(); j++)
	// dataMap.put(table.getColumnName(j), (String) rowData.get(j));
	//
	// InsertToDB(dataMap); // now it contains columndName corresponding to row
	// value
	//
	// }
	// }
	// }
	// }

}
