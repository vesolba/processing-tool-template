package dbmanager;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

@SuppressWarnings("serial")
public class DBCreationDialog extends JDialog {

	private String initialDBName = "";
	private String initialNbuser = "";
	private String initialPwd = "";
	private String initialDescription = "";
	private String initialDBLocation = "";

	private boolean isDBase;
	private final JPanel contentPanel = new JPanel();
	private JTextField txtDBName;
	private JTextField txtNbuser;
	private JTextField txtDescription;
	private JTextField txtDBLocation;
	private boolean alreadyRegistered = false;
	private JPasswordField txtPwd;
	private JButton okButton;
	public JButton button;
	public boolean isDBCreation;
	public boolean isDBRegistration;
	private boolean registerDB = false;
	private boolean createDB = false;

	/**
	 * Create the dialog.
	 * 
	 */
	public DBCreationDialog(String initialDBName, String initialNbuser, String initialPwd, String initialDescription,
			String initialDBLocation, String command) {

		this.initialDBName = initialDBName;
		this.initialNbuser = initialNbuser;
		this.initialPwd = initialPwd;
		this.initialDescription = initialDescription;
		this.initialDBLocation = initialDBLocation;
		isDBCreation = command.equals("Create Database...");
		isDBRegistration = command.equals("Register Database...");

		jbInit();
	}

	private void jbInit() {
		setModal(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setIconImage(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/data/DBM4P3-32.png")));
		setBounds(100, 100, 599, 310);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.NORTH);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[] { 150, 100, 100, 0 };
		gbl_contentPanel.rowHeights = new int[] { 26, 0, 0, 0, 0, 0, 0 };
		gbl_contentPanel.columnWeights = new double[] { 0.0, 1.0, 1.0, 0.0 };
		gbl_contentPanel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		contentPanel.setLayout(gbl_contentPanel);
		{
			JLabel lblDatabaseName = new JLabel("Database Name: ");
			GridBagConstraints gbc_lblDatabaseName = new GridBagConstraints();
			gbc_lblDatabaseName.anchor = GridBagConstraints.EAST;
			gbc_lblDatabaseName.insets = new Insets(0, 0, 5, 5);
			gbc_lblDatabaseName.gridx = 0;
			gbc_lblDatabaseName.gridy = 1;
			contentPanel.add(lblDatabaseName, gbc_lblDatabaseName);
		}

		{
			txtDBName = new JTextField(initialDBName);
			txtDBName.addFocusListener(new FocusAdapter() {

				@Override
				public void focusLost(FocusEvent e) {
					if (txtDBName.getText().trim().equals("") || txtDBLocation.getText().trim().equals("")) {
						okButton.setEnabled(false);
					} else {
						okButton.setEnabled(true);
					}

				}

			});

			txtDBName.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if (txtDBName.getText().trim().equals("") || txtDBLocation.getText().trim().equals("")) {
						okButton.setEnabled(false);
					} else {
						okButton.setEnabled(true);
					}
				}
			});

			GridBagConstraints gbc_txtDBName = new GridBagConstraints();
			gbc_txtDBName.fill = GridBagConstraints.HORIZONTAL;
			gbc_txtDBName.gridwidth = 2;
			gbc_txtDBName.insets = new Insets(0, 0, 5, 5);
			gbc_txtDBName.anchor = GridBagConstraints.NORTHWEST;
			gbc_txtDBName.gridx = 1;
			gbc_txtDBName.gridy = 1;
			contentPanel.add(txtDBName, gbc_txtDBName);
			txtDBName.setColumns(10);
		}
		{
			JLabel lblUserName = new JLabel("User Name: ");
			GridBagConstraints gbc_lblUserName = new GridBagConstraints();
			gbc_lblUserName.anchor = GridBagConstraints.EAST;
			gbc_lblUserName.insets = new Insets(0, 0, 5, 5);
			gbc_lblUserName.gridx = 0;
			gbc_lblUserName.gridy = 2;
			contentPanel.add(lblUserName, gbc_lblUserName);
		}
		{
			txtNbuser = new JTextField(initialNbuser);
			GridBagConstraints gbc_txtNbuser = new GridBagConstraints();
			gbc_txtNbuser.gridwidth = 2;
			gbc_txtNbuser.insets = new Insets(0, 0, 5, 5);
			gbc_txtNbuser.fill = GridBagConstraints.HORIZONTAL;
			gbc_txtNbuser.gridx = 1;
			gbc_txtNbuser.gridy = 2;
			contentPanel.add(txtNbuser, gbc_txtNbuser);
			txtNbuser.setColumns(10);
		}
		{
			JLabel lblPassword = new JLabel("Password: ");
			GridBagConstraints gbc_lblPassword = new GridBagConstraints();
			gbc_lblPassword.anchor = GridBagConstraints.EAST;
			gbc_lblPassword.insets = new Insets(0, 0, 5, 5);
			gbc_lblPassword.gridx = 0;
			gbc_lblPassword.gridy = 3;
			contentPanel.add(lblPassword, gbc_lblPassword);
		}
		{
			txtPwd = new JPasswordField(initialPwd);
			txtPwd.setToolTipText("tooltip");
			GridBagConstraints gbc_txtPwd = new GridBagConstraints();
			gbc_txtPwd.insets = new Insets(0, 0, 5, 5);
			gbc_txtPwd.fill = GridBagConstraints.HORIZONTAL;
			gbc_txtPwd.gridx = 1;
			gbc_txtPwd.gridy = 3;
			contentPanel.add(txtPwd, gbc_txtPwd);
		}
		{
			JLabel lblDescription = new JLabel("Description: ");
			GridBagConstraints gbc_lblDescription = new GridBagConstraints();
			gbc_lblDescription.anchor = GridBagConstraints.EAST;
			gbc_lblDescription.insets = new Insets(0, 0, 5, 5);
			gbc_lblDescription.gridx = 0;
			gbc_lblDescription.gridy = 4;
			contentPanel.add(lblDescription, gbc_lblDescription);
		}
		{
			txtDescription = new JTextField(initialDescription);
			GridBagConstraints gbc_txtDescription = new GridBagConstraints();
			gbc_txtDescription.gridwidth = 2;
			gbc_txtDescription.insets = new Insets(0, 0, 5, 5);
			gbc_txtDescription.fill = GridBagConstraints.HORIZONTAL;
			gbc_txtDescription.gridx = 1;
			gbc_txtDescription.gridy = 4;
			contentPanel.add(txtDescription, gbc_txtDescription);
			txtDescription.setColumns(10);

		}
		{
			JLabel lblDatabaseLocation = new JLabel("Database Location: ");
			GridBagConstraints gbc_lblDatabaseLocation = new GridBagConstraints();
			gbc_lblDatabaseLocation.anchor = GridBagConstraints.EAST;
			gbc_lblDatabaseLocation.insets = new Insets(0, 0, 0, 5);
			gbc_lblDatabaseLocation.gridx = 0;
			gbc_lblDatabaseLocation.gridy = 5;
			contentPanel.add(lblDatabaseLocation, gbc_lblDatabaseLocation);
		}
		{
			txtDBLocation = new JTextField(initialDBLocation);
			txtDBLocation.addFocusListener(new FocusAdapter() {

				@Override
				public void focusLost(FocusEvent arg0) {
					if (txtDBName.getText().trim().equals("") || txtDBLocation.getText().trim().equals("")) {
						okButton.setEnabled(false);
					} else {
						okButton.setEnabled(true);
					}
				}
			});
			txtDBLocation.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (txtDBName.getText().trim().equals("") || txtDBLocation.getText().trim().equals("")) {
						okButton.setEnabled(false);
					} else {
						okButton.setEnabled(true);
					}
				}
			});
			txtDBLocation.setHorizontalAlignment(SwingConstants.LEFT);
			GridBagConstraints gbc_txtDBLocation = new GridBagConstraints();
			gbc_txtDBLocation.insets = new Insets(0, 0, 0, 5);
			gbc_txtDBLocation.gridwidth = 2;
			gbc_txtDBLocation.fill = GridBagConstraints.HORIZONTAL;
			gbc_txtDBLocation.gridx = 1;
			gbc_txtDBLocation.gridy = 5;
			contentPanel.add(txtDBLocation, gbc_txtDBLocation);
			txtDBLocation.setColumns(10);
		}
		button = new JButton("Browse...");
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				chooser.setCurrentDirectory(new File(txtDBLocation.getText()));
				int returnVal = chooser.showOpenDialog(DBCreationDialog.this);

				if (returnVal == JFileChooser.APPROVE_OPTION) {

					File dBaseLocation = chooser.getSelectedFile();

					// Check if there is a DBase present in the chosen dirs.
					// Derby databases are directories
					try {
						isDBase = isDBase(dBaseLocation);
					} catch (SQLException sqlex) {

						String sQLState = sqlex.getSQLState();

						if (sQLState.equals("08004")) { // Authentication error
							isDBase = true;
							// JOptionPane.showMessageDialog(chooser, "The choosen location, " +
							// dBaseLocation
							// + ", is a database already. "
							// + "Creating a database inside another can lead to errors and unwanted
							// deletions.");

							// JOptionPane.showMessageDialog(null,
							// "Error : " + sqlex.getSQLState() + " "
							// + sqlex.getErrorCode() + " " + sqlex.getMessage(),
							// "Authentication Error", JOptionPane.WARNING_MESSAGE);
							//
						} else {
							isDBase = false;
						}

						// if (sQLState.equals("XJ004")) {
						//
						// isDBase = false;
						// JOptionPane.showMessageDialog(null,
						// "Error : " + sQLState + " " + sqlex.getErrorCode() + " "
						// + sqlex.getMessage(),
						// "The folder is not a valid Java DB.", JOptionPane.ERROR_MESSAGE);
						// } else {
						// JOptionPane.showMessageDialog(null,
						// "Error : " + sQLState + " " + sqlex.getErrorCode() + " " +
						// sqlex.getMessage(),
						// "SQL Error", JOptionPane.ERROR_MESSAGE);
						// }

					}

					if (isDBase) {
						int dialogResult = 0;
						if (isDBCreation) {
							JOptionPane.showMessageDialog(chooser, "The choosen location, " + dBaseLocation
									+ ", is a database already. "
									+ "Creating a database inside another can lead to errors and unwanted deletions.");
						} else {
							int optionButtons = JOptionPane.YES_NO_OPTION;
							dialogResult = JOptionPane.showConfirmDialog(null,
									"Is " + dBaseLocation + " the database that you want to register?", "Warning",
									optionButtons);

							if (dialogResult == JOptionPane.YES_OPTION) {
								getTxtDBName().setText(dBaseLocation.getName());
								getTxtDBLocation().setText(dBaseLocation.getParentFile().toString());
								registerDB = true;

							}
						}
					} else {

						txtDBLocation.setText(chooser.getSelectedFile().getAbsolutePath());

						if (isDBCreation) {
							int optionButtons = JOptionPane.YES_NO_OPTION;
							int dialogResult = JOptionPane.showConfirmDialog(null,
									"Do you want to have this directory as default for your databases?", "Warning",
									optionButtons);

							if (dialogResult == JOptionPane.YES_OPTION) {
								DBManager.derbySystemHome = txtDBLocation.getText();
								DBManager.propsDBM.setDBMProp("derby.system.home", DBManager.derbySystemHome);
								DBManager.propsDBM.saveProperties();
							}
						} else {
							JOptionPane.showMessageDialog(chooser, "The choosen location, " + dBaseLocation
									+ ", is not a database. " + "It can not be registered in DBManager System.");

						}

					}
				}
			}

		});

		GridBagConstraints gbc_button = new GridBagConstraints();
		gbc_button.gridx = 3;
		gbc_button.gridy = 5;
		contentPanel.add(button, gbc_button);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				okButton = new JButton("OK");
				okButton.setEnabled(false);
				okButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {

						// Verifies that the database is not yet registered
						String newDBName = txtDBName.getText();
						String newDBPath = txtDBLocation.getText();
						alreadyRegistered = verifyIfRegistered(newDBPath, newDBName);
						createDB = false;
						registerDB = false;
						Connection testConnection = null;

						// Verifies if the database exists in the given file path. The system allows
						// duplicates if their paths and name in the registration table do not match.
						try {
							testConnection = DBConnect.connect(DBManager.prefInicConn, newDBPath + "/" + newDBName,
									txtNbuser.getText(), txtPwd.getPassword(), false);
						} catch (Exception g) {

						}

						if (alreadyRegistered) {
							if (testConnection != null) {

								JOptionPane.showMessageDialog(okButton,
										"It is not allowed duplicated databases in the same directory.",
										"That database already exists.", JOptionPane.ERROR_MESSAGE);
								try {
									testConnection.close();
								} catch (Exception g) {

								}
								txtDBName.invalidate();

							} else {

								int confirm = JOptionPane.showConfirmDialog(okButton,
										"The database was previously registered "
												+ "but it doesnt exists phisically in the given path. "
												+ "If you select \'OK\' it will be created.",
										"WARNING", JOptionPane.OK_CANCEL_OPTION);

								if (confirm == JOptionPane.OK_OPTION) {
									createDB = true;
								}
							}
						} else {
							if (testConnection == null) {
								createDB = true;
								registerDB = true;
							} else {
								int confirm = JOptionPane.showConfirmDialog(okButton,
										"The database already exists phisically " + "but it is not registered."
												+ "If you select \'OK\' it will be registered.",
										"WARNING", JOptionPane.OK_CANCEL_OPTION);

								if (confirm == JOptionPane.OK_OPTION) {
									registerDB = true;
								}
							}
						}

						if (createDB) { // The database is created.
							try {
								Connection creaConn = DBConnect.connect(DBManager.prefInicConn,
										newDBPath + "/" + newDBName, txtNbuser.getText(), txtPwd.getPassword(), true);
							} catch (Exception g) {
								System.out.println("create failed ");
								g.printStackTrace();
							}
						}

						if (registerDB) {

							try {
								Connection sysConn = DBConnect.connect(DBManager.prefInicConn,
										DBManager.pathToDBManager, "", null, false);

								Statement stmt = sysConn.createStatement();
								char[] paswd = txtPwd.getPassword();
								if (paswd != null && !paswd.toString().equals("")) {
								}

								String insert = "INSERT INTO DBLIST (DBMS, DBNAME, DESCRIPTION, FILEPATH) "
										+ "VALUES (\'Java DB\', \'" + txtDBName.getText() + "\', \'"
										+ txtDescription.getText() + "\', \'" + txtDBLocation.getText() + "\')";

								int result = stmt.executeUpdate(insert);

								if (result >= 0) {
									DefaultTreeModel model = (DefaultTreeModel) DBManager.dBtree.getModel();
									DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();

									model.insertNodeInto(new DefaultMutableTreeNode(
											new DBTreeNodeK("Java DB", txtDBName.getText(), txtDBLocation.getText(),
													"DBASE", txtDBName.getText(), txtDBName.getText(), "")),
											root, 0);
									DBManager.dBtree.updateUI();
								}

							} catch (Exception ey) {
								System.out.println("Error when DB registration.");
								ey.printStackTrace();

							}
						}

						setVisible(false);
						dispose();
					}

				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}

			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						setVisible(false);
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}

		// Just before to set the dialog visible
		if (txtDBName.getText().trim().equals("") || txtDBLocation.getText().trim().equals("")) {
			okButton.setEnabled(false);
		} else {
			okButton.setEnabled(true);
		}

	}

	// Tests if the given path is a Java database
	public static boolean isDBase(File filePath) throws SQLException {

		Connection conn = null;

		try {
			conn = DBConnect.connect(DBManager.prefInicConn, filePath.getAbsolutePath().toString(), "", null, false);

			// If success, there was a database in the path
			return true;

		} catch (Exception ex) {

			String sQLState = ((SQLException) ex).getSQLState();

			if (sQLState.equals("08004")) { // Authentication error
				JOptionPane
						.showMessageDialog(null,
								"Error : " + ((SQLException) ex).getSQLState() + " "
										+ ((SQLException) ex).getErrorCode() + " " + (ex).getMessage(),
								"Authentication Error", JOptionPane.WARNING_MESSAGE);
				return true;
			}

			if (sQLState.equals("XJ004")) {
				JOptionPane.showMessageDialog(null,
						"Error : " + sQLState + " " + ((SQLException) ex).getErrorCode() + " " + (ex).getMessage(),
						"The folder is not a valid Java DB.", JOptionPane.ERROR_MESSAGE);
				return false;
				// } else {
				// JOptionPane.showMessageDialog(null,
				// "Error : " + sQLState + " " + ((SQLException) ex).getErrorCode() + " " +
				// (ex).getMessage(), "SQL Error", JOptionPane.ERROR_MESSAGE);
				// }

			} else {
				JOptionPane.showMessageDialog(null, "It is locked by another process or it is not a valid folder.",
						"Folder not available.", JOptionPane.ERROR_MESSAGE);
				return false;
			}
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (SQLException e) {

			}
		}
	}

	// Verifies that there is a record in DBM4PROC sys DB for the given database.
	public boolean verifyIfRegistered(String DBPath, String DBName) {

		Connection connSYSDBM;
		boolean AuxRegistered = false;

		try {
			// Connect to SYSDBM
			connSYSDBM = DBConnect.connect(DBManager.prefInicConn, DBManager.pathToDBManager, txtNbuser.getText(),
					txtPwd.getPassword(), false);
			Statement stmt = connSYSDBM.createStatement();
			String sql = "SELECT * from DBLIST WHERE FILEPATH = \'" + DBPath + "\' AND  DBNAME = \'" + DBName + "\'";
			System.out.println(sql);

			ResultSet rs = stmt.executeQuery(sql);

			if (rs.next()) {
				AuxRegistered = true;
			}

		} catch (Exception ez) {
			JOptionPane.showMessageDialog(null, "System Database not available.", "Error", JOptionPane.ERROR_MESSAGE);
			ez.printStackTrace();
		}

		return AuxRegistered;
	}

	//
	// while (rs.next()) {
	// String redDBMS = rs.getString("DBMS");
	// String dBName = rs.getString("DBNAME");
	// String pathToTable = rs.getString("FILEPATH");
	// nodeInfo = new DBTreeNodeK(redDBMS, dBName, pathToTable);
	//
	// DefaultMutableTreeNode auxNode = new DefaultMutableTreeNode(nodeInfo);
	// partialHead.add(auxNode);
	// }
	//

	// DBManager.stmt = con.createStatement();
	//
	// String insert = "INSERT INTO DBLIST (DBMS, DBNAME, DESCRIPTION,
	// FILEPATH) "
	// + "VALUES ('Java DB', '" + txtDBName.getText() + "', '" +
	// txtDescription.getText() + "', '" +
	// txtDBLocation.getText()
	// + "')";
	// System.out.println("Query INSERT DATA: " + insert);
	// int result = DBManager.stmt.executeUpdate(insert);
	//
	// }catch(
	//
	// Exception ey)
	// {
	// System.out.println("Error when table creation.");
	// ey.printStackTrace();
	//
	// }break;
	//
	// return true;
	// }

	/**
	 * @return the txtDBName
	 */
	public JTextField getTxtDBName() {
		return txtDBName;
	}

	/**
	 * @return the txtNbuser
	 */
	public JTextField getTxtNbuser() {
		return txtNbuser;
	}

	/**
	 * @return the txtPassword
	 */
	public JTextField getTxtPassword() {
		return txtPwd;
	}

	/**
	 * @return the txtDescription
	 */
	public JTextField getTxtDescription() {
		return txtDescription;
	}

	/**
	 * @return the txtDBLocation
	 */
	public JTextField getTxtDBLocation() {
		return txtDBLocation;
	}

}
