
package eone.base.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.util.CCache;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;


public class MBPartner extends X_C_BPartner
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5534148976588041343L;


	
	public static MBPartner get (Properties ctx, String Value) {
		return get(ctx,Value,null);		
	}


	public static MBPartner get (Properties ctx, String Value, String trxName)
	{
		if (Value == null || Value.length() == 0)
			return null;
		final String whereClause = "Value=? AND AD_Client_ID=?";
		MBPartner retValue = new Query(ctx, I_C_BPartner.Table_Name, whereClause, trxName)
		.setParameters(Value,Env.getAD_Client_ID(ctx))
		.firstOnly();
		return retValue;
	}	//	get
	

	public static MBPartner getFirstWithTaxID (Properties ctx, String taxID, String trxName)
	{
		final String whereClause = "TaxID=? AND AD_Client_ID=?";
		MBPartner retValue = new Query(ctx, Table_Name, whereClause, trxName)
		.setParameters(taxID, Env.getAD_Client_ID(ctx))
		.setOrderBy(COLUMNNAME_C_BPartner_ID)
		.first();
		return retValue;
	}	//	getFirstWithTaxID


	public static MBPartner get (Properties ctx, int C_BPartner_ID)
	{
		return get(ctx,C_BPartner_ID,null);
	}
	
	protected static CCache<String,MBPartner> s_cache = new CCache<String,MBPartner>(Table_Name, 50 );

	public static MBPartner get (Properties ctx, int C_BPartner_ID, String trxName)
	{
		String key = String.valueOf(C_BPartner_ID);
		MBPartner retValue = (MBPartner)s_cache.get(key);
		if (retValue != null)
			return retValue;
		final String whereClause = "C_BPartner_ID=?";
		retValue = new Query(ctx,I_C_BPartner.Table_Name,whereClause,trxName)
		.setParameters(C_BPartner_ID)
		.firstOnly();
		s_cache.put (key, retValue);
		return retValue;
	}	//	get

	

	public MBPartner (Properties ctx)
	{
		this (ctx, -1, null);
	}	//	MBPartner


	public MBPartner (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MBPartner


	public MBPartner (Properties ctx, int C_BPartner_ID, String trxName)
	{
		super (ctx, C_BPartner_ID, trxName);
		
	}	//	MBPartner

	
	/** Users							*/
	protected MUser[]				m_contacts = null;
	/** BP Bank Accounts				*/
	protected MBPartnerInfo[]		m_accounts = null;
	/** Prim User						*/
	protected Integer				m_primaryAD_User_ID = null;
	/** BP Group						*/
	protected MBPGroup				m_group = null;
	
	/**
	 * 	Load Default BPartner
	 * 	@param AD_Client_ID client
	 * 	@return true if loaded
	 */
	protected boolean initTemplate (int AD_Client_ID)
	{
		if (AD_Client_ID == 0)
			throw new IllegalArgumentException ("Client_ID=0");

		boolean success = true;
		String sql = "SELECT * FROM C_BPartner Where AD_Client_ID=?";
			
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, AD_Client_ID);
			rs = pstmt.executeQuery();
			if (rs.next())
				success = load (rs);
			else
			{
				load(0, null);
				success = false;
				log.severe ("None found");
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}
		setStandardDefaults();
		//	Reset
		set_ValueNoCheck ("C_BPartner_ID", I_ZERO);
		setValue ("");
		setName ("");
		setName2(null);
		set_ValueNoCheck ("C_BPartner_UU", "");
		return success;
	}	//	getTemplate


	/**
	 * 	Get All Contacts
	 * 	@param reload if true users will be requeried
	 *	@return contacts
	 */
	public MUser[] getContacts (boolean reload)
	{
		if (reload || m_contacts == null || m_contacts.length == 0)
			;
		else
			return m_contacts;
		//
		ArrayList<MUser> list = new ArrayList<MUser>();
		final String sql = "SELECT * FROM AD_User WHERE C_BPartner_ID=? ORDER BY AD_User_ID";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_TrxName());
			pstmt.setInt(1, getC_BPartner_ID());
			rs = pstmt.executeQuery();
			while (rs.next())
				list.add(new MUser (getCtx(), rs, get_TrxName()));
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}

		m_contacts = new MUser[list.size()];
		list.toArray(m_contacts);
		return m_contacts;
	}	//	getContacts

	/**
	 * 	Get specified or first Contact
	 * 	@param AD_User_ID optional user
	 *	@return contact or null
	 */
	public MUser getContact (int AD_User_ID)
	{
		MUser[] users = getContacts(false);
		if (users.length == 0)
			return null;
		for (int i = 0; AD_User_ID != 0 && i < users.length; i++)
		{
			if (users[i].getAD_User_ID() == AD_User_ID)
				return users[i];
		}
		return users[0];
	}	//	getContact
	
	
	
	
	/**
	 * 	Get Bank Accounts
	 * 	@param requery requery
	 *	@return Bank Accounts
	 */
	public MBPartnerInfo[] getBankAccounts (boolean requery)
	{
		if (m_accounts != null && m_accounts.length >= 0 && !requery)	//	re-load
			return m_accounts;
		//
		List<MBPartnerInfo> list = new Query(getCtx(), X_C_BPartnerInfo.Table_Name, "C_BPartner_ID=? AND IsActive='Y'", get_TrxName())
				.setParameters(getC_BPartner_ID())
				.list();
		m_accounts = new MBPartnerInfo[list.size()];
		list.toArray(m_accounts);
		return m_accounts;
	}	//	getBankAccounts

	
	public static MBPartnerInfo getInfoDefaultBPartner (Properties ctx, int C_BPartner_ID)
	{
		MBPartnerInfo value = new Query(ctx,I_C_BPartnerInfo.Table_Name,"C_BPartner_ID = ? And IsActive = 'Y' And IsDefault = 'Y'",null)
		.setParameters(C_BPartner_ID)
		.setOnlyActiveRecords(true)
		.first();
		
		return value;
	}
	
	
	/**************************************************************************
	 *	String Representation
	 * 	@return info
	 */
	public String toString ()
	{
		StringBuilder sb = new StringBuilder ("MBPartner[ID=")
			.append(get_ID())
			.append(",Value=").append(getValue())
			.append(",Name=").append(getName())
			.append ("]");
		return sb.toString ();
	}	//	toString

	
	public MBPGroup getBPGroup()
	{
		if (m_group == null)
		{
			if (getC_BP_Group_ID() == 0)
				m_group = MBPGroup.getDefault(getCtx());
			else
				m_group = MBPGroup.get(getCtx(), getC_BP_Group_ID(), get_TrxName());
		}
		return m_group;
	}	//	getBPGroup

	/**
	 * 	Get BP Group
	 *	@param group group
	 */
	public void setBPGroup(MBPGroup group)
	{
		m_group = group;
		if (m_group == null)
			return;
		setC_BP_Group_ID(m_group.getC_BP_Group_ID());
		
	}	//	setBPGroup

	
	protected boolean beforeSave (boolean newRecord)
	{
		if (newRecord || is_ValueChanged("Value") || isActive() || is_ValueChanged("Name")) {
			List<MProduct> relValue = new Query(getCtx(), Table_Name, "C_BPartner_ID != ? And (Value = ? Or Name = ?) And AD_Client_ID = ? And IsActive = 'Y'", get_TrxName())
					.setParameters(getC_BPartner_ID(), getValue(), getName(), getAD_Client_ID())
					.list();
			if (relValue.size() >= 1) {
				log.saveError("Error", "Value Or Name exists");
				return false;
			}

		}
		return true;
	}	//	beforeSave
	
	@Override
	protected boolean beforeDelete() {
		if (isRecordUsed()) {
			log.saveError("Error", Msg.getMsg(getCtx(), "RecordUsed"));
			return false;
		}
		return super.beforeDelete();
	}


	/**************************************************************************
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return success
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if (!success)
			return success;
		
		//if (newRecord || is_ValueChanged(COLUMNNAME_Value))
		//	update_Tree(MTree_Base.TREETYPE_BPartner);

		
		return success;
	}	//	afterSave

	/**
	 * 	After Delete
	 *	@param success
	 *	@return deleted
	 */
	protected boolean afterDelete (boolean success)
	{
		if (isAutoCreate()) {
			log.saveError("Error", "Can't delete this record!");
			return false;
		}
		//if (success)
		//	delete_Tree(MTree_Base.TREETYPE_BPartner);
		return success;
	}	//	afterDelete

	@Override
	protected boolean postDelete() {
		if (getLogo_ID() > 0) {
			MImage img = new MImage(getCtx(), getLogo_ID(), get_TrxName());
			if (!img.delete(true)) {
				log.warning("Associated image could not be deleted for bpartner - AD_Image_ID=" + getLogo_ID());
				return false;
			}
		}
		return true;
	}

}	//	MBPartner
