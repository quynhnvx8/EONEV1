package eone.install;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import org.compiere.util.CLogger;

public class ConfigurationPanel extends JPanel implements ActionListener, IDBConfigMonitor
{
	private static final long serialVersionUID = -5113669370606054608L;


	public ConfigurationPanel (JLabel statusBar)
	{
		m_statusBar = statusBar;
		try
		{
			jbInit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}	//	ConfigurationPanel

	/** Error Info				*/
	private String				m_errorString;
	/** Test Success			*/
	private volatile boolean	m_success = false;
	/** Sync					*/
	private volatile boolean	m_testing = false;

	/** Translation				*/
	static ResourceBundle 		res = ResourceBundle.getBundle("eone.install.SetupRes");

	/** Status Bar				*/
	private JLabel 				m_statusBar;
	/**	Configuration Data		*/
	private ConfigurationData	m_data = new ConfigurationData(this);

	private static ImageIcon iOpen = new ImageIcon(ConfigurationPanel.class.getResource("images/openFile.gif"));
	private static ImageIcon iSave = new ImageIcon(ConfigurationPanel.class.getResource("images/Save16.gif"));


	//	-------------	Static UI
	private GridBagLayout gridBagLayout = new GridBagLayout();
	private static final int	FIELDLENGTH = 15;
	//	Java
	private JLabel 		lJavaHome = new JLabel();
	JTextField 	fJavaHome = new JTextField(FIELDLENGTH);
	JCheckBox 	okJavaHome = new JCheckBox();
	private JButton 	bJavaHome = new JButton(iOpen);
	//	Adempiere - KeyStore
	private JLabel 		lAdempiereHome = new JLabel();
	JTextField 	fAdempiereHome = new JTextField(FIELDLENGTH);
	JCheckBox 	okAdempiereHome = new JCheckBox();
	private JButton 	bAdempiereHome = new JButton(iOpen);
	private JLabel 		lKeyStore = new JLabel();
	JPasswordField 	fKeyStore = new JPasswordField();
	JCheckBox 	okKeyStore = new JCheckBox();
	//	Apps Server  - Type
	JLabel lAppsServer = new JLabel();
	JTextField fAppsServer = new JTextField(FIELDLENGTH);
	JCheckBox okAppsServer = new JCheckBox();
	//	Web Ports
	private JLabel lWebPort = new JLabel();
	JTextField fWebPort = new JTextField(FIELDLENGTH);
	JCheckBox okWebPort = new JCheckBox();
	private JLabel lSSLPort = new JLabel();
	JTextField fSSLPort = new JTextField(FIELDLENGTH);
	JCheckBox okSSLPort = new JCheckBox();
	//	Database
	private JLabel lDatabaseType = new JLabel();
	JComboBox<Object> fDatabaseType = new JComboBox<Object>(ConfigurationData.DBTYPE);
	//
	JLabel lDatabaseServer = new JLabel();
	JTextField fDatabaseServer = new JTextField(FIELDLENGTH);
	private JLabel lDatabaseName = new JLabel();
	JTextField fDatabaseName = new JTextField(FIELDLENGTH);
	private JLabel lDatabasePort = new JLabel();
	JTextField fDatabasePort = new JTextField(FIELDLENGTH);
	private JLabel lDatabaseUser = new JLabel();
	JTextField fDatabaseUser = new JTextField(FIELDLENGTH);
	private JLabel lDatabasePassword = new JLabel();
	JPasswordField fDatabasePassword = new JPasswordField();
	public JCheckBox okDatabaseServer = new JCheckBox();
	public JCheckBox okDatabaseUser = new JCheckBox();
	public JCheckBox okDatabaseSystem = new JCheckBox();
	public JCheckBox okDatabaseSQL = new JCheckBox();
	public JCheckBox okdbExists = new JCheckBox();
	//
	private JLabel lTypeEx = new JLabel();
	JComboBox<Object> fTypeEx = new JComboBox<Object>(ConfigurationData.DBTYPE);
	JLabel lDBHostEx = new JLabel();
	JTextField fDBHostEx = new JTextField(FIELDLENGTH);
	JLabel lDBNameEx = new JLabel();
	JTextField fDBNameEx = new JTextField(FIELDLENGTH);
	private JLabel lDBPortEx = new JLabel();
	JTextField fDBPortEx = new JTextField(FIELDLENGTH);
	private JLabel lDBUserEx = new JLabel();
	JTextField fDBUserEx = new JTextField(FIELDLENGTH);
	private JLabel lDBPassEx = new JLabel();
	JPasswordField fDBPassEx = new JPasswordField();
	//
	private JButton bTest = new JButton();
	private JButton bSave = new JButton(iSave);


	/**
	 * 	Static Layout Init
	 *  @throws Exception
	 */
	private void jbInit() throws Exception
	{
		this.setLayout(gridBagLayout);
		Insets bInsets = new Insets(0, 5, 0, 5);
		TitledBorder titledBorder = new TitledBorder("dummy");

		//	Java
		lJavaHome.setToolTipText(res.getString("JavaHomeInfo"));
		lJavaHome.setText(res.getString("JavaHome"));
		fJavaHome.setText(".");
		okJavaHome.setEnabled(false);
		bJavaHome.setMargin(bInsets);
		bJavaHome.setToolTipText(res.getString("JavaHomeInfo"));

		JLabel sectionLabel = new JLabel("Java");
		sectionLabel.setForeground(titledBorder.getTitleColor());
		JSeparator separator = new JSeparator();
		this.add(sectionLabel,    new GridBagConstraints(0, 0, 7, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(15, 5, 0, 10), 0, 0));
		this.add(separator,    new GridBagConstraints(0, 1, 7, 1, 1.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 10), 0, 0));

		this.add(lJavaHome,    new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 2, 5), 0, 0));
		this.add(fJavaHome,    new GridBagConstraints(1, 2, 1, 1, 0.5, 0.0,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 0), 0, 0));
		this.add(okJavaHome,   new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 0, 2, 5), 0, 0));
		this.add(bJavaHome,    new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		
		//	AdempiereHome - KeyStore
		lAdempiereHome.setToolTipText(res.getString("AdempiereHomeInfo"));
		lAdempiereHome.setText(res.getString("AdempiereHome"));
		fAdempiereHome.setText(".");
		okAdempiereHome.setEnabled(false);
		bAdempiereHome.setMargin(bInsets);
		bAdempiereHome.setToolTipText(res.getString("AdempiereHomeInfo"));
		lKeyStore.setText(res.getString("KeyStorePassword"));
		lKeyStore.setToolTipText(res.getString("KeyStorePasswordInfo"));
		fKeyStore.setText("");
		okKeyStore.setEnabled(false);

		sectionLabel = new JLabel("EONE");
		sectionLabel.setForeground(titledBorder.getTitleColor());
		separator = new JSeparator();
		this.add(sectionLabel,    new GridBagConstraints(0, 3, 7, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(15, 5, 0, 0), 0, 0));
		this.add(separator,    new GridBagConstraints(0, 4, 7, 1, 1.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 10), 0, 0));
		this.add(lAdempiereHome,		new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 2, 5), 0, 0));
		this.add(fAdempiereHome,		new GridBagConstraints(1, 5, 1, 1, 0.5, 0.0,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 2, 0), 0, 0));
		this.add(okAdempiereHome,	new GridBagConstraints(2, 5, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 0, 2, 5), 0, 0));
		this.add(bAdempiereHome,     new GridBagConstraints(3, 5, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(lKeyStore,  		new GridBagConstraints(4, 5, 1, 1, 0.0, 0.0	,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(fKeyStore,  		new GridBagConstraints(5, 5, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 2, 0), 0, 0));
		this.add(okKeyStore,  		new GridBagConstraints(6, 5, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 2, 5), 0, 0));
		//	Apps Server - Type
		lAppsServer.setToolTipText(res.getString("AppsServerInfo"));
		lAppsServer.setText(res.getString("AppsServer"));
		lAppsServer.setFont(lAppsServer.getFont().deriveFont(Font.BOLD));
		fAppsServer.setText(".");
		okAppsServer.setEnabled(false);
		sectionLabel = new JLabel(res.getString("AppsServer"));
		sectionLabel.setForeground(titledBorder.getTitleColor());
		separator = new JSeparator();
		this.add(sectionLabel,    new GridBagConstraints(0, 6, 6, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(15, 5, 0, 0), 0, 0));
		this.add(separator,    new GridBagConstraints(0, 7, 7, 1, 1.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 10), 0, 0));
		this.add(lAppsServer,   new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 2, 5), 0, 0));
		this.add(fAppsServer,   new GridBagConstraints(1, 8, 1, 1, 0.5, 0.0,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 2, 0), 0, 0));
		this.add(okAppsServer,  new GridBagConstraints(2, 8, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 0, 2, 5), 0, 0));
		//	Web Ports
		lWebPort.setToolTipText(res.getString("WebPortInfo"));
		lWebPort.setText(res.getString("WebPort"));
		fWebPort.setText(".");
		okWebPort.setEnabled(false);
		lSSLPort.setText("SSL");
		fSSLPort.setText(".");
		okSSLPort.setEnabled(false);
		this.add(lWebPort,   new GridBagConstraints(0, 10, 1, 1, 0.0, 0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(2, 5, 2, 5), 0, 0));
		this.add(fWebPort,   new GridBagConstraints(1, 10, 1, 1, 0.5, 0.0,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2, 5, 2, 0), 0, 0));
		this.add(okWebPort,  new GridBagConstraints(2, 10, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 0, 2, 5), 0, 0));
		this.add(lSSLPort,   new GridBagConstraints(4, 10, 1, 1, 0.0, 0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(2, 5, 2, 5), 0, 0));
		this.add(fSSLPort,   new GridBagConstraints(5, 10, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2, 5, 2, 0), 0, 0));
		this.add(okSSLPort,  new GridBagConstraints(6, 10, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 0, 2, 5), 0, 0));
		//	Database Server - Type
		lDatabaseServer.setToolTipText(res.getString("DatabaseServerInfo"));
		lDatabaseServer.setText(res.getString("DatabaseServer"));
		lDatabaseServer.setFont(lDatabaseServer.getFont().deriveFont(Font.BOLD));
		okDatabaseServer.setEnabled(false);
		lDatabaseType.setToolTipText(res.getString("DatabaseTypeInfo"));
		lDatabaseType.setText(res.getString("DatabaseType"));
		fDatabaseType.setPreferredSize(fDatabaseServer.getPreferredSize());
		sectionLabel = new JLabel(res.getString("DatabaseServer"));
		sectionLabel.setForeground(titledBorder.getTitleColor());
		separator = new JSeparator();
			
		this.add(sectionLabel,    new GridBagConstraints(0, 11, 6, 1, 0.0, 0.0	,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(15, 5, 0, 0), 0, 0));		
		this.add(separator,    new GridBagConstraints(0, 12, 7, 1, 1.0, 0.0	,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 10), 0, 0));
		this.add(lDatabaseServer,	new GridBagConstraints(0, 14, 1, 1, 0.0, 0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 2, 5), 0, 0));
		this.add(fDatabaseServer,   new GridBagConstraints(1, 14, 1, 1, 0.5, 0.0,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 2, 0), 0, 0));
		this.add(okDatabaseServer,  new GridBagConstraints(2, 14, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 0, 2, 5), 0, 0));
		this.add(lDatabaseType,		new GridBagConstraints(4, 14, 1, 1, 0.0, 0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 2, 5), 0, 0));
		this.add(fDatabaseType,     new GridBagConstraints(5, 14, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 2, 0), 0, 0));
		
		//Database/Service Name
		lDatabaseName.setToolTipText(res.getString("DatabaseNameInfo"));
		lDatabaseName.setText(res.getString("DatabaseName"));
		fDatabaseName.setText(".");

		//TNS/Native connection
		lDatabasePort.setToolTipText(res.getString("DatabasePortInfo"));
		lDatabasePort.setText(res.getString("DatabasePort"));
		fDatabasePort.setText(".");
		
		okDatabaseSQL.setEnabled(false);
		this.add(lDatabaseName,		new GridBagConstraints(0, 15, 1, 1, 0.0, 0.0 ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(2, 5, 2, 5), 0, 0));
		this.add(fDatabaseName,		new GridBagConstraints(1, 15, 1, 1, 0.5, 0.0	,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2, 5, 2, 0), 0, 0));
		this.add(okDatabaseSQL, 		new GridBagConstraints(2, 15, 1, 1, 0.0, 0.0	,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 0, 2, 5), 0, 0));
		this.add(lDatabasePort,  	new GridBagConstraints(4, 15, 1, 1, 0.0, 0.0 ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(2, 0, 2, 5), 0, 0));
		this.add(fDatabasePort,  	new GridBagConstraints(5, 15, 1, 1, 0.5, 0.0 ,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2, 5, 2, 0), 0, 0));
		//	Port - System
		
		//	User - Password
		lDatabaseUser.setToolTipText(res.getString("DatabaseUserInfo"));
		lDatabaseUser.setText(res.getString("DatabaseUser"));
		fDatabaseUser.setText(".");
		lDatabasePassword.setToolTipText(res.getString("DatabasePasswordInfo"));
		lDatabasePassword.setText(res.getString("DatabasePassword"));
		fDatabasePassword.setText(".");
		okDatabaseUser.setEnabled(false);
		this.add(lDatabaseUser,     new GridBagConstraints(0, 17, 1, 1, 0.0, 0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(2, 5, 2, 5), 0, 0));
		this.add(fDatabaseUser,		new GridBagConstraints(1, 17, 1, 1, 0.5, 0.0,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2, 5, 2, 0), 0, 0));
		this.add(lDatabasePassword, new GridBagConstraints(4, 17, 1, 1, 0.0, 0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(2, 5, 2, 5), 0, 0));
		this.add(fDatabasePassword, new GridBagConstraints(5, 17, 1, 1, 0.5, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 0), 0, 0));
		this.add(okDatabaseUser,	new GridBagConstraints(6, 17, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 0, 2, 5), 0, 0));

		//DB Second
		
		sectionLabel = new JLabel(res.getString("DBSecond"));
		sectionLabel.setForeground(titledBorder.getTitleColor());
		separator = new JSeparator();
		this.add(sectionLabel,    	new GridBagConstraints(0, 18, 6, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(15, 5, 0, 0), 0, 0));
		this.add(separator,    		new GridBagConstraints(0, 19, 7, 1, 1.0, 0.0	,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 10), 0, 0));
		
		//Host And Type
		lDBHostEx.setText(res.getString("DBHostEx"));
		lDBHostEx.setFont(lDBHostEx.getFont().deriveFont(Font.BOLD));
		lTypeEx.setText(res.getString("DatabaseType"));
		fTypeEx.setPreferredSize(fTypeEx.getPreferredSize());
				
		this.add(lDBHostEx, new GridBagConstraints(0, 20, 1, 1, 0.0, 0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 2, 5), 0, 0));
		this.add(fDBHostEx, new GridBagConstraints(1, 20, 1, 1, 0.5, 0.0,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 2, 0), 0, 0));
		this.add(lTypeEx, 	new GridBagConstraints(4, 20, 1, 1, 0.0, 0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 2, 5), 0, 0));
		this.add(fTypeEx, 	new GridBagConstraints(5, 20, 1, 1, 0.5, 0.0,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 2, 0), 0, 0));

		//	DB Name And Port
		lDBNameEx.setText(res.getString("DBNameEx"));
		lDBNameEx.setFont(lDBNameEx.getFont().deriveFont(Font.BOLD));
		fDBNameEx.setText(".");
		lDBPortEx.setText(res.getString("DBPortEx"));
		fDBPortEx.setText(".");
		
		this.add(lDBNameEx,	new GridBagConstraints(0, 21, 1, 1, 0.0, 0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(2, 5, 2, 5), 0, 0));
		this.add(fDBNameEx, new GridBagConstraints(1, 21, 1, 1, 0.5, 0.0,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2, 5, 2, 0), 0, 0));
		this.add(lDBPortEx, new GridBagConstraints(4, 21, 1, 1, 0.0, 0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(2, 5, 2, 5), 0, 0));
		this.add(fDBPortEx, new GridBagConstraints(5, 21, 1, 1, 0.5, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 0), 0, 0));

		//	DB User And Password
		lDBUserEx.setText(res.getString("DBUserEx"));
		fDBUserEx.setText(".");
		lDBPassEx.setText(res.getString("DBPassEx"));
		fDBPassEx.setText(".");
		this.add(lDBUserEx,	new GridBagConstraints(0, 22, 1, 1, 0.0, 0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(2, 5, 2, 5), 0, 0));
		this.add(fDBUserEx, new GridBagConstraints(1, 22, 1, 1, 0.5, 0.0,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2, 5, 2, 0), 0, 0));
		this.add(lDBPassEx, new GridBagConstraints(4, 22, 1, 1, 0.0, 0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(2, 5, 2, 5), 0, 0));
		this.add(fDBPassEx, new GridBagConstraints(5, 22, 1, 1, 0.5, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 0), 0, 0));

		//grap extra space when window is maximized
		JPanel filler = new JPanel();
		filler.setOpaque(false);
		filler.setBorder(null);
		this.add(filler,    		new GridBagConstraints(0, 23, 1, 1, 0.0, 1.0,GridBagConstraints.WEST, GridBagConstraints.VERTICAL, new Insets(0, 0, 0, 0), 0, 0));

		//	End
		bTest.setToolTipText(res.getString("TestInfo"));
		bTest.setText(res.getString("Test"));
		bSave.setToolTipText(res.getString("SaveInfo"));
		bSave.setText(res.getString("Save"));
		this.add(bTest,    		new GridBagConstraints(0, 24, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(15, 5, 10, 5), 0, 0));
		this.add(bSave,         new GridBagConstraints(5, 24, 2, 1, 0.0, 0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(15, 5, 10, 5), 0, 0));
		//
		bAdempiereHome.addActionListener(this);
		bJavaHome.addActionListener(this);
		fDatabaseType.addActionListener(this);
		bTest.addActionListener(this);
		bSave.addActionListener(this);
	}	//	jbInit

	/**
	 * 	Dynamic Initial.
	 * 	Called by Setup
	 *	@return true if success
	 */
	public boolean dynInit()
	{
		return m_data.load();
	}	//	dynInit

	/**
	 * 	Set Status Bar Text
	 *	@param text text
	 */
	protected void setStatusBar(String text)
	{
		m_statusBar.setText(text);
	}	//	setStatusBar


	/**************************************************************************
	 * 	ActionListener
	 *  @param e event
	 */
	public void actionPerformed(ActionEvent e)
	{
		if (m_testing)
			return;
		if (e.getSource() == fDatabaseType)
			m_data.initDatabase("");
		//
		else if (e.getSource() == bJavaHome)
			setPath (fJavaHome);
		else if (e.getSource() == bAdempiereHome)
			setPath (fAdempiereHome);
		else if (e.getSource() == bTest)
			startTest(false);
		else if (e.getSource() == bSave)
			startTest(true);
	}	//	actionPerformed



	/**
	 * 	Set Path in Field
	 * 	@param field field to set Path
	 */
	private void setPath (JTextField field)
	{
		JFileChooser fc = new JFileChooser(field.getText());
		fc.setDialogType(JFileChooser.OPEN_DIALOG);
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fc.setMultiSelectionEnabled(false);
		fc.setDialogTitle(field.getToolTipText());
		if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
			field.setText(fc.getSelectedFile().getAbsolutePath());
	}	//	setPath


	/**************************************************************************
	 * 	Start Test Async.
	 * 	@param saveIt save
	 *  @return SwingWorker
	 */
	private eone.install.SwingWorker startTest(final boolean saveIt)
	{
		eone.install.SwingWorker worker = new eone.install.SwingWorker()
		{
			//	Start it
			public Object construct()
			{
				m_testing = true;
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				bTest.setEnabled(false);
				m_success = false;
				m_errorString = null;
				try
				{
					test();
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
					m_errorString += "\n" + ex.toString();
				}
				//
				setCursor(Cursor.getDefaultCursor());
				if (m_errorString == null)
					m_success = true;
				bTest.setEnabled(true);
				m_testing = false;
				return Boolean.valueOf(m_success);
			}
			//	Finish it
			public void finished()
			{
				if (m_errorString != null)
				{
					CLogger.get().severe(m_errorString);
					JOptionPane.showConfirmDialog (m_statusBar.getParent(),
						m_errorString,
						res.getString("ServerError"),
						JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
				}
				else if (saveIt)
					save();
			}
		};
		worker.start();
		return worker;
	}	//	startIt

	/**
	 *	Test it
	 * 	@throws Exception
	 */
	private void test() throws Exception
	{
		cleanSignalOk ();
		/*
		 * In order to allow the user see a refresh on the screen 
		 * this Thread goes into sleep for 1000L milliseconds
		 */
		Thread.sleep(1000L);
		bSave.setEnabled(false);
		if (!m_data.test(this))
			return;
		//
		m_statusBar.setText(res.getString("Ok"));
		bSave.setEnabled(true);
		m_errorString = null;
	}	//	test

	/**
	 * 	UI Signal OK
	 *	@param cb check box
	 *	@param resString resource string key
	 *	@param pass true if test passed
	 *	@param critical true if critical
	 *	@param errorMsg error Message
	 */
	void signalOK (JCheckBox cb, String resString,
		boolean pass, boolean critical, String errorMsg)
	{
		m_errorString = res.getString(resString);
		cb.setSelected(pass);
		if (pass)
			cb.setToolTipText(null);
		else
		{
			cb.setToolTipText(errorMsg);
			m_errorString += " \n(" + errorMsg + ")";
		}
		if (!pass && critical)
			cb.setBackground(Color.RED);
		else if (!pass && !critical)
			cb.setBackground(Color.YELLOW);
		else
			cb.setBackground(Color.GREEN);
	}	//	setOK

	void cleanSignalOk (){
		okJavaHome.setBackground(null);
		okJavaHome.setSelected(false);
		okAdempiereHome.setBackground(null);
		okAdempiereHome.setSelected(false);
		okKeyStore.setBackground(null);
		okKeyStore.setSelected(false);
		okAppsServer.setBackground(null);
		okAppsServer.setSelected(false);
		okWebPort.setBackground(null);
		okWebPort.setSelected(false);
		okSSLPort.setBackground(null);
		okSSLPort.setSelected(false);
		okDatabaseServer.setBackground(null);
		okDatabaseServer.setSelected(false);
		okDatabaseUser.setBackground(null);
		okDatabaseUser.setSelected(false);
		okDatabaseSystem.setBackground(null);
		okDatabaseSystem.setSelected(false);
		okDatabaseSQL.setBackground(null);
		okDatabaseSQL.setSelected(false);
	}

	/**************************************************************************
	 * 	Save Settings.
	 * 	Called from startTest.finished()
	 */
	private void save()
	{
		if (!m_success)
			return;

		bSave.setEnabled(false);
		bTest.setEnabled(false);
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		if (!m_data.save())
			return;

		((Frame)SwingUtilities.getWindowAncestor(this)).dispose();
	}	//	save


	@Override
	public void update(DBConfigStatus status) {
		if (status.getStatusType().equals(DBConfigStatus.DATABASE_SERVER))
		{
			signalOK(okDatabaseServer, status.getResourseString(),	status.isPass(), status.isCritical(), status.getErrorMessage());
		}
		else if (status.getStatusType().equals(DBConfigStatus.DATABASE_SYSTEM_PASSWORD))
		{
			signalOK(okDatabaseSystem, status.getResourseString(), status.isPass(),	status.isCritical(), status.getErrorMessage());
		}
		else if (status.getStatusType().equals(DBConfigStatus.DATABASE_USER))
		{
			signalOK(okDatabaseUser, status.getResourseString(), status.isPass(), status.isCritical(), status.getErrorMessage());
		}
		else if (status.getStatusType().equals(DBConfigStatus.DATABASE_SQL_TEST))
		{
			signalOK(okDatabaseSQL, status.getResourseString(), status.isPass(),status.isCritical(), status.getErrorMessage());
		}
	}

}	//	ConfigurationPanel
