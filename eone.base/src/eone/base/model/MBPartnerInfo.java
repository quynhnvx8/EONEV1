/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2006 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package eone.base.model;

import java.sql.ResultSet;
import java.util.List;
import java.util.Properties;

import org.compiere.util.CLogger;

/**
 *  BP Bank Account Model
 *
 *  @author Jorg Janke
 *  @version $Id: MBPBankAccount.java,v 1.3 2006/07/30 00:51:03 jjanke Exp $
 */
public class MBPartnerInfo extends X_C_BPartnerInfo
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6826961806519015878L;

	/**
	 * 	Get Accounts Of BPartner
	 *	@param ctx context
	 *	@param C_BPartner_ID bpartner
	 *	@return
	 */
	public static MBPartnerInfo[] getOfBPartner (Properties ctx, int C_BPartner_ID)
	{
		final String whereClause = MBPartnerInfo.COLUMNNAME_C_BPartner_ID+"=?";
		List<MBPartnerInfo>list = new Query(ctx,I_C_BPartnerInfo.Table_Name,whereClause,null)
		.setParameters(C_BPartner_ID)
		.setOnlyActiveRecords(true)
		.list();
		
		MBPartnerInfo[] retValue = new MBPartnerInfo[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	getOfBPartner

	/**	Logger	*/
	@SuppressWarnings("unused")
	private static CLogger s_log = CLogger.getCLogger(MBPartnerInfo.class);
	
	/**************************************************************************
	 * 	Constructor
	 *	@param ctx context
	 *	@param C_BP_BankAccount_ID BP bank account
	 *	@param trxName transaction
	 */
	public MBPartnerInfo (Properties ctx, int C_BPartnerInfo_ID, String trxName)
	{
		super (ctx, C_BPartnerInfo_ID, trxName);
		
	}	//	MBP_BankAccount

	/**
	 * 	Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName transaction
	 */
	public MBPartnerInfo (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MBP_BankAccount

	/**
	 * 	Constructor
	 *	@param ctx context
	 * 	@param bp BP
	 *	@param bpc BP Contact
	 * 	@param location Location
	 */
	public MBPartnerInfo (Properties ctx, MBPartner bp, MUser bpc)
	{
		this(ctx, 0, bp.get_TrxName());
		
		
	}	//	MBP_BankAccount

	/** Bank Link			*/

	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	protected boolean beforeSave(boolean newRecord) 
	{
		if (isDefault() || isActive()) {
			String sqlWhere = "C_BpartnerInfo_ID != ? And IsActive = 'Y' and IsDefault = 'Y' And C_BPartner_ID = ?";
			List<MBPartnerInfo> list = new Query(getCtx(), Table_Name, sqlWhere, get_TrxName())
					.setParameters(getC_BPartnerInfo_ID(), getC_BPartner_ID())
					.list();
			if (list.size() >= 1) {
				log.saveError("Error", "Default exists!");
				return false;
			}
		}

		return true;
	}	//	beforeSave
	
	/**
	 *	String Representation
	 * 	@return info
	 */
	public String toString ()
	{
		StringBuilder sb = new StringBuilder ("MBP_BankAccount[")
			.append (get_ID ())
			.append(", Name=").append(getName())
			.append ("]");
		return sb.toString ();
	}	//	toString

}	//	MBPBankAccount
