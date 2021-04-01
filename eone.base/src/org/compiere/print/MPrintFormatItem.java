package org.compiere.print;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.Language;

import eone.base.model.X_AD_PrintFormatItem;

/**
 * 	@author 	Quynhnv.x8
 * 	@version 	22/10/2020
 */
public class MPrintFormatItem extends X_AD_PrintFormatItem
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7145503984951798641L;

	/**
	 *	Constructor
	 *  @param ctx context
	 *  @param AD_PrintFormatItem_ID AD_PrintFormatItem_ID
	 *	@param trxName transaction
	 */
	public MPrintFormatItem (Properties ctx, int AD_PrintFormatItem_ID, String trxName)
	{
		super (ctx, AD_PrintFormatItem_ID, trxName);
		//	Default Setting
		if (AD_PrintFormatItem_ID == 0)
		{
			setFieldAlignmentType(FIELDALIGNMENTTYPE_Default);
			setPrintFormatType(PRINTFORMATTYPE_Text);
			setPrintAreaType(PRINTAREATYPE_Content);
			//
			setMaxWidth(0);
			setMaxHeight(0);
			setIsSummarized(false);
			setIsAveraged(false);
			setIsCounted(false);
			setIsMinCalc(false);
			
			setIsMaxCalc(false);
			setIsCountedGroup(false);
		}
	}	//	MPrintFormatItem

	
	/**
	 *	Constructor
	 *  @param ctx context
	 *  @param rs ResultSet
	 *	@param trxName transaction
	 */
	public MPrintFormatItem (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MPrintFormatItem
	/** Locally cached label translations			*/
	private HashMap<String,String>	m_translationLabel;
	String m_newTranslationLabel = null;
	boolean m_translationLabelChanged = false;
	
	
	public String getName (Language language, int windowNo)
	{
		if (language == null || Env.isBaseLanguage(language, "AD_PrintFormatItem"))
			return convertFomular(getName(), windowNo);
		loadTranslations();
		String retValue = (String)m_translationLabel.get(language.getAD_Language());
		if (retValue == null || retValue.length() == 0)
			return convertFomular(getName(), windowNo);
		return convertFomular(retValue, windowNo);
	}	//	getPrintName

	private String convertFomular(String item, int windowNo) {
		String formula = "";
		if (item == null || item.isEmpty())
			return "";
		if (!item.contains("@") && !item.contains("{")) {
			return item;
		}
		if (item.contains("@")) 
			item = Env.parseContext(Env.getCtx(), windowNo, item, false);
		if (item.contains("{") && item.contains("}")) {
			int start = item.indexOf("{");
			formula = item.substring(item.indexOf("{") + 1, item.indexOf("}"));
			double num = Env.getValueByFormula(formula);
			String value = Env.numToChar((int)num, 2);
			item = item.substring(0, start) + value + item.substring(item.indexOf("}") + 1);
		}
		
		return item;
	}
	/**************************************************************************
	 *	Set print name on language
	 * 	@param language language - ignored if IsMultiLingualDocument not 'Y'
	 */
	public void setName (Language language, String printName)
	{
		if (language == null || Env.isBaseLanguage(language, "AD_PrintFormatItem")) {
			setName(printName);
			return;
		}
		loadTranslations();
		String retValue = (String)m_translationLabel.get(language.getAD_Language());
		if ((retValue != null && ! retValue.equals(printName))
			|| (retValue == null && printName != null)) {
			m_newTranslationLabel = printName;
			m_translationLabelChanged = true;
			m_translationLabel.put(language.getAD_Language(), printName);
		}
	}	//	getPrintName

	
	/**
	 * 	Load Translations
	 */
	private void loadTranslations()
	{
		if (m_translationLabel == null)
		{
			m_translationLabel = new HashMap<String,String>();
			String sql = "SELECT AD_Language, Name FROM AD_PrintFormatItem_Trl WHERE AD_PrintFormatItem_ID=?";
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try
			{
				pstmt = DB.prepareStatement(sql, get_TrxName());
				pstmt.setInt(1, get_ID());
				rs = pstmt.executeQuery();
				while (rs.next())
				{
					m_translationLabel.put (rs.getString (1), rs.getString (2));
				}
			}
			catch (SQLException e)
			{
				log.log(Level.SEVERE, "loadTrl", e);
			}
			finally {
				DB.close(rs, pstmt);
				rs = null; pstmt = null;
			}
		}
	}	//	loadTranslations


	
	/**
	 * 	Type Text
	 *	@return true if text
	 */
	public boolean isTypeText()
	{
		return getPrintFormatType().equals(PRINTFORMATTYPE_Text);
	}
	
	public boolean isTypeImage()
	{
		return getPrintFormatType().equals(PRINTFORMATTYPE_Image);
	}
	
	public boolean isFieldCenter()
	{
		return getFieldAlignmentType().equals(FIELDALIGNMENTTYPE_Center);
	}
	
	public boolean isFieldAlignLeft()
	{
		return getFieldAlignmentType().equals(FIELDALIGNMENTTYPE_LeadingLeft);
	}
	
	public boolean isFieldAlignRight()
	{
		return getFieldAlignmentType().equals(FIELDALIGNMENTTYPE_TrailingRight);
	}
	/**
	 * 	Field Align Block
	 *	@return true if block
	 */
	public boolean isFieldAlignBlock()
	{
		return getFieldAlignmentType().equals(FIELDALIGNMENTTYPE_Block);
	}
	/**
	 * 	Field Align Default
	 *	@return true if default alignment
	 */
	public boolean isFieldAlignDefault()
	{
		return getFieldAlignmentType().equals(FIELDALIGNMENTTYPE_Default);
	}
	
	public boolean isHeader()
	{
		return getPrintAreaType().equals(PRINTAREATYPE_Header);
	}
	/**
	 * 	Content
	 *	@return true if area is centent
	 */
	public boolean isContent()
	{
		return getPrintAreaType().equals(PRINTAREATYPE_Content);
	}
	/**
	 * 	Footer
	 *	@return true if area is footer
	 */
	public boolean isFooter()
	{
		return getPrintAreaType().equals(PRINTAREATYPE_Footer);
	}
	
	
	public boolean isNumber() {
		return DisplayType.isNumeric(getAD_Reference_ID());
	}
	
	public boolean isDate() {
		return DisplayType.isDate(getAD_Reference_ID());
	}
	
	/**
	 * 	Barcode
	 *	@return true if barcode selected
	 */
	public boolean isBarcode()
	{
		String s = getBarcodeType();
		return s != null && s.length() > 0;
	}
	
	
	/**************************************************************************
	 * 	String representation
	 * 	@return info
	 */
	public String toString()
	{
		StringBuilder sb = new StringBuilder("MPrintFormatItem[");
		sb.append("ID=").append(get_ID())
			.append(",Name=").append(getName())
			.append(", Seq=").append(getSeqNo())
			.append(", Area=").append(getPrintAreaType())
			.append(", MaxWidth=").append(getMaxWidth())
			.append(",MaxHeight=").append(getMaxHeight());
		sb.append(",FieldAlign=").append(getFieldAlignmentType());
		//
		sb.append(", Type=").append(getPrintFormatType());
		if (isTypeText())
			;
		else if (isTypeImage())
			;
		//
		sb.append(", Printed=").append(isPrinted())
			.append(",SeqNo=").append(getSeqNo())
			.append(",Summarized=").append(isSummarized());
		sb.append("]");
		return sb.toString();
	}	//	toString

	/**
	 * 	Get ColumnName from AD_Column_ID
	 *  @return ColumnName
	 */
	public String getColumnName()
	{
		return getName();
	}	//	getColumnName

	/**
	 * 	Copy existing Definition To Client
	 * 	@param To_Client_ID to client
	 *  @param AD_PrintFormat_ID parent print format
	 * 	@return print format item
	 */
	public MPrintFormatItem copyToClient (int To_Client_ID, int AD_PrintFormat_ID)
	{
		MPrintFormatItem to = new MPrintFormatItem (p_ctx, 0, null);
		MPrintFormatItem.copyValues(this, to);
		to.setClientOrg(To_Client_ID, 0);
		to.setAD_PrintFormat_ID(AD_PrintFormat_ID);
		to.saveEx();
		return to;
	}	//	copyToClient

	
	/**
	 * 	Before Save
	 *	@param newRecord
	 *	@return true if ok
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		
		return true;
	}	//	beforeSave
	
	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return success
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		
		if (m_translationLabelChanged) {
			String sql = "UPDATE AD_PrintFormatItem_Trl "
					+ "SET Name = ? "
					+ "WHERE AD_PrintFormatItem_ID = ?"
					+ " AND AD_Language=?";
			int no = DB.executeUpdateEx(sql, new Object[] {m_newTranslationLabel, get_ID(), Language.getLoginLanguage().getAD_Language()}, get_TrxName());
			if (log.isLoggable(Level.FINE)) log.fine("translations updated #" + no);
			
			m_newTranslationLabel = null;
			m_translationLabelChanged = false;
		}

		return success;
	}	//	afterSave
	
	@Override
	public boolean is_Changed() {
		if (m_translationLabelChanged)
			return true;
		return super.is_Changed();
	}
	
	public int getAlignment() {
		if ("T".equals(getFieldAlignmentType())) {//Right
			return 2;
		}
		else if ("C".equals(getFieldAlignmentType())) {//Center
			return 1;
		}else {//Left
			return 0;
		}
	}
	
}	//	MPrintFormatItem
