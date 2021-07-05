
package eone.webui.apps;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;

import javax.servlet.ServletRequest;

import org.compiere.util.CCache;
import org.compiere.util.CLogger;
import org.compiere.util.CacheMgt;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.Ini;
import org.compiere.util.Language;
import org.compiere.util.Util;
import org.zkoss.web.servlet.Servlets;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

import eone.base.model.GridWindowVO;
import eone.base.model.I_AD_Window;
import eone.base.model.Lookup;
import eone.base.model.MClient;
import eone.base.model.MLanguage;
import eone.base.model.MLookup;
import eone.base.model.MLookupFactory;
import eone.base.model.MQuery;
import eone.base.model.MReference;
import eone.base.model.MSysConfig;
import eone.base.model.MTable;
import eone.base.model.MZoomCondition;
import eone.base.model.PO;
import eone.webui.ClientInfo;
import eone.webui.adwindow.ADWindow;
import eone.webui.component.Window;
import eone.webui.editor.WTableDirEditor;
import eone.webui.info.InfoWindow;
import eone.webui.session.SessionManager;
import eone.webui.theme.ThemeManager;
import eone.webui.util.IServerPushCallback;
import eone.webui.util.ServerPushTemplate;


public final class AEnv
{
	public static final String LOCALE = Env.LOCALE;
	

	public static void showCenterScreen(Window window)
	{
		if (SessionManager.getAppDesktop() != null)
			SessionManager.getAppDesktop().showWindow(window, "center");
		else 
		{
			window.setPosition("center");
			window.setPage(getDesktop().getFirstPage());
			Object objMode = window.getAttribute(Window.MODE_KEY);
			final String mode = objMode != null ? objMode.toString() : Window.MODE_HIGHLIGHTED;
			if (Window.MODE_MODAL.equals(mode))
				window.doModal();
			else
				window.doHighlighted();
		}
	}   //  showCenterScreen


	public static void showScreen(Window window, String position)
	{
		SessionManager.getAppDesktop().showWindow(window, position);
	}   //  showScreen


	public static void showCenterWindow(Window parent, Window window)
	{
		parent.appendChild(window);
		showScreen(window, "parent,center");
	}   //  showCenterWindow


	public static char getMnemonic (String text)
	{

		int pos = text.indexOf('&');
		if (pos != -1)					//	We have a nemonic
			return text.charAt(pos+1);
		return 0;

	}   //  getMnemonic



	public static void zoom (int AD_Table_ID, int Record_ID)
	{
		int AD_Window_ID = Env.getZoomWindowID(AD_Table_ID, Record_ID);
		//  Nothing to Zoom to
		if (AD_Window_ID == 0)
			return;
		MTable table = MTable.get(Env.getCtx(), AD_Table_ID);
		MQuery query = MQuery.getEqualQuery(table.getKeyColumns()[0], Record_ID);
		query.setZoomTableName(table.getTableName());
		query.setZoomColumnName(table.getKeyColumns()[0]);
		query.setZoomValue(Record_ID);
		zoom(AD_Window_ID, query);
	}	//	zoom
	
	public static void zoom (int AD_Table_ID, int Record_ID, int AD_Window_ID)
	{
		MTable table = MTable.get(Env.getCtx(), AD_Table_ID);
		MQuery query = MQuery.getEqualQuery(table.getKeyColumns()[0], Record_ID);
		query.setZoomTableName(table.getTableName());
		query.setZoomColumnName(table.getKeyColumns()[0]);
		query.setZoomValue(Record_ID);
		zoom(AD_Window_ID, query);
	}


	public static void zoom (int AD_Table_ID, int Record_ID, MQuery query, int windowNo)
	{
		int AD_Window_ID = Env.getZoomWindowID(AD_Table_ID, Record_ID, windowNo);
		//  Nothing to Zoom to
		if (AD_Window_ID == 0)
			return;
		zoom(AD_Window_ID, query);
	}	//	zoom

	public static void zoom (int AD_Table_ID, int Record_ID, MQuery query) {
		zoom (AD_Table_ID, Record_ID, query, 0);
	}

	/**
	 *	Exit System
	 *  @param status System exit status (usually 0 for no error)
	 */
	public static void exit (int status)
	{
		Env.exitEnv(status);
	}	//	exit

	/**
	 * logout AD_Session
	 */
	public static void logout()
	{
		String sessionID = Env.getContext(Env.getCtx(), "#AD_Session_ID");
		synchronized (windowCache)
		{
			CCache<String,GridWindowVO> cache = windowCache.get(sessionID);
			if (cache != null)
			{
				cache.clear();
				CacheMgt.get().unregister(cache);
			}
		}
		windowCache.remove(sessionID);
		//	End Session
		/*Quynhnv.x8: Bo session luu database va lay tu database.
		MSession session = MSession.get(Env.getCtx(), false);	//	finish
		if (session != null)
			session.logout();
		*/
		Env.setContext(Env.getCtx(), "#AD_Session_ID", (String)null);
		//
	}

	
	private static final CLogger log = CLogger.getCLogger(AEnv.class);

	/**	Window Cache		*/
	private static Map<String, CCache<String,GridWindowVO>> windowCache = new HashMap<String, CCache<String,GridWindowVO>>();


	public static GridWindowVO getMWindowVO (int WindowNo, int AD_Window_ID, int AD_Menu_ID)
	{

		if (log.isLoggable(Level.CONFIG)) log.config("Window=" + WindowNo + ", AD_Window_ID=" + AD_Window_ID);
		GridWindowVO mWindowVO = null;
		String sessionID = Env.getContext(Env.getCtx(), "#AD_Session_ID");
		if (AD_Window_ID != 0 && Ini.isCacheWindow())	//	try cache
		{
			synchronized (windowCache)
			{
				CCache<String,GridWindowVO> cache = windowCache.get(sessionID);
				if (cache != null)
				{
					mWindowVO = cache.get(Integer.toString(AD_Window_ID));
					if (mWindowVO != null)
					{
						mWindowVO = mWindowVO.clone(WindowNo);
						if (log.isLoggable(Level.INFO))
							log.info("Cached=" + mWindowVO);
					}
				}
			}
		}

		//  Create Window Model on Client
		if (mWindowVO == null)
		{
			log.config("create local");
			mWindowVO = GridWindowVO.create (Env.getCtx(), WindowNo, AD_Window_ID, AD_Menu_ID);
			if (mWindowVO != null && Ini.isCacheWindow())
			{
				synchronized (windowCache)
				{
					CCache<String,GridWindowVO> cache = windowCache.get(sessionID);
					if (cache == null)
					{
						cache = new CCache<String, GridWindowVO>(I_AD_Window.Table_Name, I_AD_Window.Table_Name+"|GridWindowVO|Session|"+sessionID, 10);
						windowCache.put(sessionID, cache);
					}
					cache.put(Integer.toString(AD_Window_ID), mWindowVO);
				}
			}
		}	//	from Client
		if (mWindowVO == null)
			return null;

		//  Check (remote) context
		if (!mWindowVO.ctx.equals(Env.getCtx()))
		{
			//  Remote Context is called by value, not reference
			//  Add Window properties to context
			Enumeration<Object> keyEnum = mWindowVO.ctx.keys();
			while (keyEnum.hasMoreElements())
			{
				String key = (String)keyEnum.nextElement();
				if (key.startsWith(WindowNo+"|"))
				{
					String value = mWindowVO.ctx.getProperty (key);
					Env.setContext(Env.getCtx(), key, value);
				}
			}
			//  Sync Context
			mWindowVO.setCtx(Env.getCtx());
		}
		return mWindowVO;

	}   //  getWindow

	
	public static void cacheReset (String tableName, int Record_ID)
	{

		if (log.isLoggable(Level.CONFIG)) log.config("TableName=" + tableName + ", Record_ID=" + Record_ID);

		CacheMgt.get().reset(tableName, Record_ID);
	}   //  cacheReset

    public static void actionRefresh(Lookup lookup, Object value, boolean mandatory, boolean shortList) // IDEMPIERE 90
    {
        if (lookup == null)
            return;

        lookup.refresh();
        if (lookup.isValidated())
            lookup.fillComboBox(mandatory, false, false, false, shortList); // IDEMPIERE 90
        else
            lookup.fillComboBox(mandatory, true, false, false, shortList); // IDEMPIERE 90
    }
    /**
     *
     * @param lookup
     * @param value
     */
    public static void actionZoom(Lookup lookup, Object value)
    {
        if (lookup == null)
            return;
        //
        MQuery zoomQuery = lookup.getZoomQuery();
		// still null means the field is empty or not selected item
		if (value == null)
			value = -1;

        //  If not already exist or exact value
        if (zoomQuery == null || value != null)
        {
        	zoomQuery = new MQuery();   //  ColumnName might be changed in MTab.validateQuery
        	String column = lookup.getColumnName();
//        	Check if it is a List Reference
        	if (lookup instanceof MLookup)
        	{
        		int AD_Reference_ID = ((MLookup)lookup).getAD_Reference_Value_ID();
        		if (AD_Reference_ID > 0)
        		{
        			MReference reference = MReference.get(AD_Reference_ID);
        			if (reference.getValidationType().equals(MReference.VALIDATIONTYPE_ListValidation))
        			{
		        		column = "AD_Ref_List_ID";
		        		value = DB.getSQLValue(null, "SELECT AD_Ref_List_ID FROM AD_Ref_List WHERE AD_Reference_ID=? AND Value=?", AD_Reference_ID, value);
        			}
        		}
        	}
        	//strip off table name, fully qualify name doesn't work when zoom into detail tab
        	if (column.indexOf(".") > 0)
        	{
        		int p = column.indexOf(".");
        		String tableName = column.substring(0, p);
        		column = column.substring(column.indexOf(".")+1);
        		zoomQuery.setZoomTableName(tableName);
        		zoomQuery.setZoomColumnName(column);            	
        	}
        	else
        	{
        		zoomQuery.setZoomColumnName(column);
        		//remove _ID to get table name
        		zoomQuery.setZoomTableName(column.substring(0, column.length() - 3));
        	}
        	zoomQuery.setZoomValue(value);
        	zoomQuery.addRestriction(column, MQuery.EQUAL, value);
        	zoomQuery.setRecordCount(1);    //  guess
        }
        if (value instanceof Integer && ((Integer) value).intValue() >= 0 && zoomQuery != null && zoomQuery.getZoomTableName() != null) {
        	int tableId = MTable.getTable_ID(zoomQuery.getZoomTableName());
        	zoom(tableId, ((Integer) value).intValue(), zoomQuery, lookup.getWindowNo());
        } else {
        	int windowId = lookup.getZoom(zoomQuery);
        	zoom(windowId, zoomQuery, lookup.getWindowNo());
        }
    }

    /**
     * open zoom window with query
     * @param AD_Window_ID
     * @param query
     */
    public static void showZoomWindow(int AD_Window_ID, MQuery query)
    {
    	SessionManager.getAppDesktop().showZoomWindow(AD_Window_ID, query);
    }
    
	/**
	 * Zoom to a window with the provided window id and filters according to the
	 * query
	 * @param AD_Window_ID Window on which to zoom
	 * @param query Filter to be applied on the records.
	 */
	public static void zoom(int AD_Window_ID, MQuery query, int windowNo)
	{
		int zoomId = MZoomCondition.findZoomWindowByWindowId(AD_Window_ID, query, windowNo);
        showZoomWindow(zoomId > 0 ? zoomId : AD_Window_ID, query);
	}
	
	public static void zoom(int AD_Window_ID, MQuery query) {
		zoom(AD_Window_ID, query, 0);
	}

	public static void showWindow(Window win)
	{
		SessionManager.getAppDesktop().showWindow(win);
	}

	/**
	 * 	Zoom
	 *	@param query query
	 */
	public static void zoom (MQuery query)
	{
		if (query == null || query.getTableName() == null || query.getTableName().length() == 0)
			return;
		
		int AD_Window_ID = query.getZoomWindowID();

		if (AD_Window_ID <= 0)
			AD_Window_ID = Env.getZoomWindowID(query);

		//  Nothing to Zoom to
		if (AD_Window_ID == 0)
			return;

		showZoomWindow(AD_Window_ID, query);
	}


    public static URI getImage(String fileNameInImageDir)
    {
        URI uri = null;
        try
        {
            uri = new URI(ThemeManager.getThemeResource("images/" + fileNameInImageDir));
        }
        catch (URISyntaxException exception)
        {
            log.log(Level.SEVERE, "Not found: " +  fileNameInImageDir);
            return null;
        }
        return uri;
    }   //  getImageIcon

    /**
     *
     * @return boolean
     */
    public static boolean isFirefox2() {
    	Execution execution = Executions.getCurrent();
    	if (execution == null)
    		return false;

    	Object n = execution.getNativeRequest();
    	if (n instanceof ServletRequest) {
    		String userAgent = Servlets.getUserAgent((ServletRequest) n);
    		return userAgent.indexOf("Firefox/2") >= 0;
    	} else {
    		return false;
    	}
    }

    /**
     * @return boolean
     * @deprecated See IDEMPIERE-1022
     */
    public static boolean isBrowserSupported() {
    	Execution execution = Executions.getCurrent();
    	if (execution == null)
    		return false;

    	Object n = execution.getNativeRequest();
    	if (n instanceof ServletRequest) {
    		Double version = Servlets.getBrowser((ServletRequest) n, "ff");
    		if (version != null) {
    			return true;
    		}
    		
    		version = Servlets.getBrowser((ServletRequest) n, "chrome");
    		if (version != null) {
    			return true;
    		}
    		
    		version = Servlets.getBrowser((ServletRequest) n, "webkit");
    		if (version != null) {
    			return true;
    		}
    		
    		version = Servlets.getBrowser((ServletRequest) n, "ie");
    		if (version != null && version.intValue() >= 8)
    			return true;
    	}
    	return false;
    }

    /**
     * @return true if user agent is internet explorer
     */
    public static boolean isInternetExplorer()
    {
    	Execution execution = Executions.getCurrent();
    	if (execution == null)
    		return false;

    	Object n = execution.getNativeRequest();
    	if (n instanceof ServletRequest) {
    		String browser = Servlets.getBrowser((ServletRequest) n);
    		if (browser != null && browser.equals("ie"))
    			return true;
    		else
    			return false;
    	}
    	return false;
    }


    public static boolean contains(Component parent, Component child) {
    	if (child == parent)
    		return true;

    	Component c = child.getParent();
    	while (c != null) {
    		if (c == parent)
    			return true;
    		c = c.getParent();
    	}

    	return false;
    }


    public static void mergePdf(List<File> pdfList, File outFile) throws IOException,
			DocumentException, FileNotFoundException {
		Document document = null;
		PdfWriter copy = null;
		
		List<PdfReader> pdfReaders = new ArrayList<PdfReader>();
		
		try
		{		
			for (File f : pdfList)
			{
				PdfReader reader = new PdfReader(f.getAbsolutePath());
				
				pdfReaders.add(reader);
				
				if (document == null)
				{
					document = new Document(reader.getPageSizeWithRotation(1));
					copy = PdfWriter.getInstance(document, new FileOutputStream(outFile));
					document.open();
				}
				int pages = reader.getNumberOfPages();
				PdfContentByte cb = copy.getDirectContent();
				for (int i = 1; i <= pages; i++) {
					document.newPage();
					copy.newPage();
					PdfImportedPage page = copy.getImportedPage(reader, i);
					cb.addTemplate(page, 0, 0);
					copy.releaseTemplate(page);
				}
			}
			document.close();
		}
		finally
		{
			for(PdfReader reader:pdfReaders)
			{
				reader.close();
			}
		}
   }

    /**
	 *	Get window title
	 *  @param ctx context
	 *  @param WindowNo window
	 *  @return Header String
	 */
	public static String getWindowHeader(Properties ctx, int WindowNo)
	{
		StringBuilder sb = new StringBuilder();
		if (WindowNo > 0){
			sb.append(Env.getContext(ctx, WindowNo, "_WinInfo_WindowName", false)).append("  ");
			final String documentNo = Env.getContext(ctx, WindowNo, "DocumentNo", false);
			final String value = Env.getContext(ctx, WindowNo, "Value", false);
			final String name = Env.getContext(ctx, WindowNo, "Name", false);
			if(!"".equals(documentNo)) {
				sb.append(documentNo).append("  ");
			}
			if(!"".equals(value)) {
				sb.append(value).append("  ");
			}
			if(!"".equals(name)) {
				sb.append(name).append("  ");
			}
		}
		return sb.toString();
	}	//	getHeader

	/**
	 * @param ctx
	 * @return Language
	 */
	public static Language getLanguage(Properties ctx) {
		return Env.getLocaleLanguage(ctx);
	}

	/**
	 * @param ctx
	 * @return Locale
	 */
	public static Locale getLocale(Properties ctx) {
		return Env.getLocale(ctx);
	}


	public static String getDialogHeader(Properties ctx, int windowNo, String prefix) {
		StringBuilder sb = new StringBuilder();
		if (prefix != null)
			sb.append(prefix);
		if (windowNo > 0){
			sb.append(Env.getContext(ctx, windowNo, "_WinInfo_WindowName", false)).append(": ");
			String documentNo = Env.getContext(ctx, windowNo, "DocumentNo", false);
			if (Util.isEmpty(documentNo)) // try first tab
				documentNo = Env.getContext(ctx, windowNo, 0, "DocumentNo", false);
			String value = Env.getContext(ctx, windowNo, "Value", false);
			if (Util.isEmpty(value)) // try first tab
				value = Env.getContext(ctx, windowNo, 0, "Value", false);
			String name = Env.getContext(ctx, windowNo, "Name", false);
			if (Util.isEmpty(name)) // try first tab
				name = Env.getContext(ctx, windowNo, 0, "Name", false);
			if(!"".equals(documentNo)) {
				sb.append(documentNo).append("  ");
			}
			if(!"".equals(value)) {
				sb.append(value).append("  ");
			}
			if(!"".equals(name)) {
				sb.append(name).append("  ");
			}
		}
		String header = sb.toString().trim();
		if (header.length() == 0)
			header = ThemeManager.getBrowserTitle();
		if (header.endsWith(":"))
			header = header.substring(0, header.length()-1);
		return header;
	}

	public static String getDialogHeader(Properties ctx, int windowNo) {
		return 	getDialogHeader(ctx, windowNo, null);
	}
	
	/**
	 * Execute synchronous task in UI thread.
	 * @param runnable
	 */
	public static void executeDesktopTask(final Runnable runnable) {
		Desktop desktop = getDesktop();
		ServerPushTemplate template = new ServerPushTemplate(desktop);
		template.execute(new IServerPushCallback() {			
			@Override
			public void updateUI() {
				runnable.run();
			}
		});
	}
	
	/**
	 * Execute asynchronous task in UI thread.
	 * @param runnable
	 */
	public static void executeAsyncDesktopTask(final Runnable runnable) {
		Desktop desktop = getDesktop();
		ServerPushTemplate template = new ServerPushTemplate(desktop);
		template.executeAsync(new IServerPushCallback() {			
			@Override
			public void updateUI() {
				runnable.run();
			}
		});
	}
	
	/**
	 * Get current desktop
	 * @return Desktop
	 */
	public static Desktop getDesktop() {
		boolean inUIThread = Executions.getCurrent() != null;
		if (inUIThread) {
			return Executions.getCurrent().getDesktop();
		} else {
			WeakReference<Desktop> ref = DesktopRunnable.getThreadLocalDesktop();
			return ref != null ? ref.get() : null;
		}
	}
	
	/**
	 * @deprecated replace by ClientInfo.isMobile()
	 * @return true if running on a tablet
	 */
	public static boolean isTablet() {
		return ClientInfo.isMobile();
	}
	

	public static int getADWindowID (int windowNo){
		int adWindowID = 0;
		// form process parameter panel
		
		Object  window = SessionManager.getAppDesktop().findWindow(windowNo);
		// case show a process dialog, window is below window of process dialog
		if (window != null && window instanceof ADWindow){
			adWindowID = ((ADWindow)window).getAD_Window_ID();
		}else if (window != null && (window instanceof ProcessDialog || window instanceof InfoWindow)){
			// dummy window is use in case process or infoWindow open in stand-alone window
			// it help we separate case save preference for all window (windowId = 0, null) and case open in stand-alone (windowId = 200054)
			adWindowID = Env.adWindowDummyID;// dummy window
		}
					
		return adWindowID;
	}
	
	public static WTableDirEditor getListDocumentLanguage (MClient client) throws Exception {
		WTableDirEditor fLanguageType = null;
		if (client.isMultiLingualDocument()){
			Lookup lookupLanguage = MLookupFactory.get (Env.getCtx(), 0, 0, DisplayType.TableDir,
					Env.getLanguage(Env.getCtx()), MLanguage.COLUMNNAME_AD_Language_ID, 0, false, 
					" IsActive='Y' AND IsLoginLocale = 'Y' ");
			fLanguageType = new WTableDirEditor(MLanguage.COLUMNNAME_AD_Language_ID, false, false, true, lookupLanguage);
		}
		return fLanguageType;
	}

	private static String m_ApplicationUrl = null;
	public static String getApplicationUrl() {
		String url = MSysConfig.getValue(MSysConfig.APPLICATION_URL, Env.getAD_Client_ID(Env.getCtx()));
		if (!Util.isEmpty(url) && !url.equals("USE_HARDCODED"))
			return MSysConfig.getValue(MSysConfig.APPLICATION_URL, Env.getAD_Client_ID(Env.getCtx()));
		if (m_ApplicationUrl != null)
			return m_ApplicationUrl;
		int port = Executions.getCurrent().getServerPort();
		String sch = Executions.getCurrent().getScheme();
		String sport = null;
		if ( (sch.equals("http") && port == 80) || (sch.equals("https") && port == 443) )
			sport = "";
		else
			sport = ":" + port;
		m_ApplicationUrl = sch + "://" + Executions.getCurrent().getServerName() + sport + Executions.getCurrent().getContextPath() +  Executions.getCurrent().getDesktop().getRequestPath();
		return m_ApplicationUrl;
	}

	/** Return the link for direct access to the record using tableID */
	public static String getZoomUrlTableID(PO po)
	{
		return getApplicationUrl() + "?Action=Zoom&AD_Table_ID=" + po.get_Table_ID() + "&Record_ID=" + po.get_ID();
	}

	/** Return the link for direct access to the record using tablename */
	public static String getZoomUrlTableName(PO po)
	{
		return getApplicationUrl() + "?Action=Zoom&TableName" + po.get_TableName() + "&Record_ID=" + po.get_ID();
	}

}	//	AEnv
