package org.compiere.print;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.util.CCache;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Language;

import eone.base.model.X_AD_PrintFormat;

/**
 * 	@author 	Quynhnv.x8
 * 	@version 	mod 21/10/2020
 */
public class MPrintFormat extends X_AD_PrintFormat
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2979978408305853342L;

	
	public MPrintFormat (Properties ctx, int AD_PrintFormat_ID, String trxName)
	{
		super (ctx, AD_PrintFormat_ID, trxName);
		m_language = Env.getLanguage(ctx);
		
		getItems();
		
	}	//	MPrintFormat

	public void reloadItems() {
		getItems();
	}
	
	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName transaction
	 */
	public MPrintFormat (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
		m_language = Env.getLanguage(ctx);
		getItems();
	}	//	MPrintFormat

	private static CCache<Integer,MPrintFormatItem[]> s_itemsHeader = new CCache<Integer,MPrintFormatItem[]>(Table_Name, 5);
	private static CCache<Integer,MPrintFormatItem[]> s_itemsContent = new CCache<Integer,MPrintFormatItem[]>(Table_Name, 5);
	private static CCache<Integer,MPrintFormatItem[]> s_itemsGroup = new CCache<Integer,MPrintFormatItem[]>(Table_Name, 5);
	private static CCache<Integer,MPrintFormatItem[]> s_itemsFooter = new CCache<Integer,MPrintFormatItem[]>(Table_Name, 5);
	
	/** Items							*/
	private MPrintFormatItem[]		m_itemsContent = null;
	private MPrintFormatItem[]		m_itemsGroup = null;
	private MPrintFormatItem[]		m_itemsHeader = null;
	private MPrintFormatItem[]		m_itemsFooter = null;
	
	private Map<String, String> formula = new HashMap<String, String>();
	
	public Map<String, String> getFormula() {
		return formula;
	}

	
	public MPrintFormatItem[] getItemHeader() {
		if (s_itemsHeader.containsKey(getAD_PrintFormat_ID()))
			return s_itemsHeader.get(getAD_PrintFormat_ID());
		getItems();
		return m_itemsHeader;
	}
	
	public MPrintFormatItem[] getItemContent() {
		if (s_itemsContent.containsKey(getAD_PrintFormat_ID()))
			return s_itemsContent.get(getAD_PrintFormat_ID());
		getItems();
		return m_itemsContent;
	}
	
	public MPrintFormatItem[] getItemGroup() {
		if (s_itemsGroup.containsKey(getAD_PrintFormat_ID()))
			return s_itemsGroup.get(getAD_PrintFormat_ID());
		getItems();
		return m_itemsGroup;
	}
	
	public MPrintFormatItem[] getItemFooter() {
		if (s_itemsFooter.containsKey(getAD_PrintFormat_ID()))
			return s_itemsFooter.get(getAD_PrintFormat_ID());
		getItems();
		return m_itemsFooter;
	}

	
	private Language 				m_language;
	
	private static CLogger			s_log = CLogger.getCLogger (MPrintFormat.class);

	/**
	 * 	Get Language
	 *  @return language
	 */
	public Language getLanguage()
	{
		return m_language;
	}	//	getLanguage

	/**
	 * 	Set Language
	 *  @param language language
	 */
	public void setLanguage(Language language)
	{
		if (language != null)
		{
			m_language = language;
		}
	}	//	getLanguage

		
	private void getItems()
	{
		if (s_itemsContent.containsKey(getAD_PrintFormat_ID()))
			return;
		ArrayList<MPrintFormatItem> lsHeader = new ArrayList<MPrintFormatItem>();
		ArrayList<MPrintFormatItem> lsContent = new ArrayList<MPrintFormatItem>();
		ArrayList<MPrintFormatItem> lsGroup = new ArrayList<MPrintFormatItem>();
		ArrayList<MPrintFormatItem> lsFooter = new ArrayList<MPrintFormatItem>();
		String sql = "SELECT * FROM AD_PrintFormatItem pfi "
			+ "WHERE pfi.AD_PrintFormat_ID=? AND pfi.IsActive='Y' "
			+ "ORDER BY IsGroupBy Desc, SeqNo ASC";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_TrxName());
			pstmt.setInt(1, get_ID());
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				MPrintFormatItem pfi = new MPrintFormatItem(p_ctx, rs, get_TrxName());
				if (pfi.isContent())
					lsContent.add (pfi);
				else if (pfi.isHeader())
					lsHeader.add (pfi);
				else if (pfi.isFooter())
					lsFooter.add (pfi);
				else if(pfi.isContentOther())
					lsGroup.add(pfi);
			}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally {
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		//
		m_itemsContent = new MPrintFormatItem[lsContent.size()];
		lsContent.toArray(m_itemsContent);
		
		m_itemsGroup = new MPrintFormatItem[lsGroup.size()];
		lsGroup.toArray(m_itemsGroup);
		
		m_itemsHeader = new MPrintFormatItem[lsHeader.size()];
		lsHeader.toArray(m_itemsHeader);
		
		m_itemsFooter = new MPrintFormatItem[lsFooter.size()];
		lsFooter.toArray(m_itemsFooter);
		
		s_itemsContent.put(getAD_PrintFormat_ID(), m_itemsContent);
		s_itemsHeader.put(getAD_PrintFormat_ID(), m_itemsHeader);
		s_itemsFooter.put(getAD_PrintFormat_ID(), m_itemsFooter);
		s_itemsGroup.put(getAD_PrintFormat_ID(), m_itemsGroup);
		//s_formula.put(getAD_PrintFormat_ID(), formula);
	}
	
	
	private MPrintFormatItem[] getItemsNotIn(int AD_PrintFormat_ID)
	{
		ArrayList<MPrintFormatItem> list = new ArrayList<MPrintFormatItem>();
		String sql = "SELECT * FROM AD_PrintFormatItem pfi "
			+ "WHERE pfi.AD_PrintFormat_ID=? AND pfi.IsActive='Y'"
			+ " AND Name NOT IN (SELECT pfi.Name FROM AD_PrintFormatItem pfi WHERE pfi.AD_PrintFormat_ID=?) "
			+ "ORDER BY SeqNo";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_TrxName());
			pstmt.setInt(1, get_ID());
			pstmt.setInt(2, AD_PrintFormat_ID);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				MPrintFormatItem pfi = new MPrintFormatItem(p_ctx, rs, get_TrxName());
				list.add (pfi);
			}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally {
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		//
		MPrintFormatItem[] retValue = new MPrintFormatItem[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	getItemsNotIn

	/**
	 * 	Get Item Count
	 * 	@return number of items or -1 if items not defined
	 */
	public int getItemCount()
	{
		if (m_itemsContent == null)
			return -1;
		return m_itemsContent.length;
	}	//	getItemCount

	/**
	 * 	Get Print Format Item
	 * 	@param index index
	 * 	@return Print Format Item
	 */
	public MPrintFormatItem getItem (int index)
	{
		if (index < 0 || index >= m_itemsContent.length)
			throw new ArrayIndexOutOfBoundsException("Index=" + index + " - Length=" + m_itemsContent.length);
		return m_itemsContent[index];
	}	//	getItem
	
	public MPrintFormatItem getItemMapSQL (int index)
	{
		ArrayList<MPrintFormatItem> arr = new ArrayList<MPrintFormatItem>();
		if (index < 0 || index >= m_itemsContent.length)
			throw new ArrayIndexOutOfBoundsException("Index=" + index + " - Length=" + m_itemsContent.length);
		
		for(int i = 0; i < m_itemsContent.length; i++) {
			if (m_itemsContent[i].isMapColumnSelectSQL()) {
				arr.add(m_itemsContent[i]);
			}
		}
		MPrintFormatItem [] retVale = new MPrintFormatItem [arr.size()];
		retVale = arr.toArray(retVale);
		return retVale[index];
	}


	
	/**
	 * 	Sting Representation
	 * 	@return info
	 */
	public String toString()
	{
		StringBuilder sb = new StringBuilder ("MPrintFormat[ID=").append(get_ID())
			.append(",Name=").append(getName())
			.append(",Language=").append(getLanguage())
			.append(",Items=").append(getItemCount())
			.append("]");
		return sb.toString();
	}	//	toString



	/**
	 * 	Copy Items
	 *  @param fromFormat from print format
	 *  @param toFormat to print format (client, id)
	 * 	@return items
	 */
	static private MPrintFormatItem[] copyItems (MPrintFormat fromFormat, MPrintFormat toFormat)
	{
		if (s_log.isLoggable(Level.INFO)) s_log.info("From=" + fromFormat);
		ArrayList<MPrintFormatItem> list = new ArrayList<MPrintFormatItem>();

		MPrintFormatItem[] items = fromFormat.getItemsNotIn(toFormat.get_ID());
		for (int i = 0; i < items.length; i++)
		{
			MPrintFormatItem pfi = items[i].copyToClient (toFormat.getAD_Client_ID(), toFormat.get_ID());
			if (pfi != null)
				list.add (pfi);
		}
		//
		MPrintFormatItem[] retValue = new MPrintFormatItem[list.size()];
		list.toArray(retValue);
		copyTranslationItems (items, retValue);	//	JTP fix
		return retValue;
	}	//	copyItems

	/**
     *	Copy translation records (from - to)
     *	@param fromItems from items
     *	@param toItems to items
     */
    static private void copyTranslationItems (MPrintFormatItem[] fromItems,
    	MPrintFormatItem[] toItems)
    {
    	if (fromItems == null || toItems == null)
            return;		//	should not happen

    	int counter = 0;
        for (int i = 0; i < fromItems.length; i++)
        {
            int fromID = fromItems[i].getAD_PrintFormatItem_ID();
            int toID = toItems[i].getAD_PrintFormatItem_ID();

            StringBuilder sql = new StringBuilder("UPDATE AD_PrintFormatItem_Trl new ")
            	//	Set
            	.append("SET (PrintName, PrintNameSuffix, IsTranslated) = ")
            	.append("(")
            	.append("SELECT PrintName, PrintNameSuffix, IsTranslated ")
            	.append("FROM AD_PrintFormatItem_Trl old ")
            	.append("WHERE old.AD_Language=new.AD_Language")
            	.append(" AND AD_PrintFormatItem_ID =").append(fromID)
            	.append(") ")
            	//	WHERE
            	.append("WHERE  AD_PrintFormatItem_ID=").append(toID)
            	.append(" AND EXISTS (SELECT AD_PrintFormatItem_ID ")
            		.append(" FROM AD_PrintFormatItem_trl old")
            		.append(" WHERE old.AD_Language=new.AD_Language")
            		.append(" AND AD_PrintFormatItem_ID =").append(fromID)
            		.append(")");
            int no = DB.executeUpdate(sql.toString(), null);
            if (no == 0)	//	if first has no translation, the rest does neither
            	break;
            counter += no;
        }	//	for
        if (s_log.isLoggable(Level.FINEST)) s_log.finest("#" + counter);
    }	//	copyTranslationItems


	/**************************************************************************
	 * 	Copy existing Definition To Client
	 * 	@param ctx context
	 * 	@param from_AD_PrintFormat_ID format
	 * 	@param to_AD_PrintFormat_ID format
	 * 	@return print format
	 */
	public static MPrintFormat copy (Properties ctx,
		int from_AD_PrintFormat_ID, int to_AD_PrintFormat_ID)
	{
		return copy (ctx, from_AD_PrintFormat_ID, to_AD_PrintFormat_ID, -1);
	}	//	copy

	/**
	 * 	Copy existing Definition To Client
	 * 	@param ctx context
	 * 	@param AD_PrintFormat_ID format
	 * 	@param to_Client_ID to client
	 * 	@return print format
	 */
	public static MPrintFormat copyToClient (Properties ctx,
		int AD_PrintFormat_ID, int to_Client_ID)
	{
		return copy (ctx, AD_PrintFormat_ID, 0, to_Client_ID);
	}	//	copy

	/**
	 * 	Copy existing Definition To Client
	 * 	@param ctx context
	 * 	@param from_AD_PrintFormat_ID format
	 *  @param to_AD_PrintFormat_ID to format or 0 for new
	 * 	@param to_Client_ID to client (ignored, if to_AD_PrintFormat_ID <> 0)
	 * 	@return print format
	 */
	private static MPrintFormat copy (Properties ctx, int from_AD_PrintFormat_ID,
		int to_AD_PrintFormat_ID, int to_Client_ID)
	{
		
		MPrintFormat from = new MPrintFormat(ctx, from_AD_PrintFormat_ID, null);
		MPrintFormat to = new MPrintFormat (ctx, to_AD_PrintFormat_ID, null);		//	could be 0
		
		to.setItems(copyItems(from,to));
		return to;
	}	//	copyToClient

	private void setItems (MPrintFormatItem[] items)
	{
		if (items != null)
			m_itemsContent = items;
	}
	
	/** Cached Formats						*/
	static private CCache<Integer,MPrintFormat> s_formats = new CCache<Integer,MPrintFormat>(Table_Name, 30);

	/**
	 * 	Get Format
	 * 	@param ctx context
	 * 	@param AD_PrintFormat_ID id
	 *  @param readFromDisk refresh from disk
	 * 	@return Format
	 */
	static public MPrintFormat get (Properties ctx, int AD_PrintFormat_ID, boolean readFromDisk)
	{
		Integer key = Integer.valueOf(AD_PrintFormat_ID);
		MPrintFormat pf = null;
		if (!readFromDisk)
			pf = (MPrintFormat)s_formats.get(key);
		if (pf == null)
		{
			pf = new MPrintFormat (ctx, AD_PrintFormat_ID, null);
			if (pf.get_ID() <= 0)
				pf = null;
			else
				s_formats.put(key, pf);
		}

		return pf;
	}	//	get

	
}	//	MPrintFormat
