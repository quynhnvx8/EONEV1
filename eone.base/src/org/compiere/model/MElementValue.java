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
package org.compiere.model;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.compiere.util.Env;
import org.compiere.util.Msg;

/**
 * 	Natural Account
 *
 *  @author Jorg Janke
 *  @version $Id: MElementValue.java,v 1.3 2006/07/30 00:58:37 jjanke Exp $
 *  
 * @author Teo Sarca, SC ARHIPAC SERVICE SRL
 * 			BF [ 1883533 ] Change to summary - valid combination issue
 * 			BF [ 2320411 ] Translate "Already posted to" message
 */
public class MElementValue extends X_C_ElementValue
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4765839867934329276L;

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_ElementValue_ID ID or 0 for new
	 *	@param trxName transaction
	 */
	public MElementValue(Properties ctx, int C_ElementValue_ID, String trxName)
	{
		super(ctx, C_ElementValue_ID, trxName);
		if (C_ElementValue_ID == 0)
		{
			setIsSummary (false);
			setAccountType (ACCOUNTTYPE_Expense);
			setIsBankAccount(false);
			//
			setPostActual (true);
		}
	}	//	MElementValue

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName transaction
	 */
	public MElementValue(Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MElementValue

	/**
	 * 	Full Constructor
	 *	@param ctx context
	 *	@param Value value
	 *	@param Name name
	 *	@param Description description
	 *	@param AccountType account type
	 *	@param AccountSign account sign
	 *	@param IsDocControlled doc controlled
	 *	@param IsSummary summary
	 *	@param trxName transaction
	 */
	public MElementValue (Properties ctx, String Value, String Name, String Description,
		String AccountType, String AccountSign,
		boolean IsDocControlled, boolean IsSummary, String trxName)
	{
		this (ctx, 0, trxName);
		setValue(Value);
		setName(Name);
		setDescription(Description);
		setAccountType(AccountType);
		setIsSummary(IsSummary);
	}	//	MElementValue
	
	
	public boolean isBalanceSheet()
	{
		String accountType = getAccountType();
		return (ACCOUNTTYPE_Asset.equals(accountType)
			|| ACCOUNTTYPE_Liability.equals(accountType)
			|| ACCOUNTTYPE_OwnerSEquity.equals(accountType));
	}	//	isBalanceSheet

	/**
	 * Is this an Activa Account
	 * @return boolean
	 */
	public boolean isActiva()
	{
		return ACCOUNTTYPE_Asset.equals(getAccountType());
	}	//	isActive

	/**
	 * Is this a Passiva Account
	 * @return boolean
	 */
	public boolean isPassiva()
	{
		String accountType = getAccountType();
		return (ACCOUNTTYPE_Liability.equals(accountType)
			|| ACCOUNTTYPE_OwnerSEquity.equals(accountType));
	}	//	isPassiva

	/**
	 * 	User String Representation
	 *	@return info value - name
	 */
	public String toString ()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(getValue()).append(" - ").append(getName());
		return sb.toString ();
	}	//	toString

	/**
	 * 	Extended String Representation
	 *	@return info
	 */
	public String toStringX ()
	{
		StringBuilder sb = new StringBuilder ("MElementValue[");
		sb.append(get_ID()).append(",").append(getValue()).append(" - ").append(getName())
			.append ("]");
		return sb.toString ();
	}	//	toStringX
	
	
	
	@Override
	protected boolean beforeSave (boolean newRecord)
	{
		
		return true;
	}	//	beforeSave
	
	@Override
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if (newRecord || is_ValueChanged("Value") || isActive()) {
			List<MProduct> relValue = new Query(getCtx(), Table_Name, "C_ElementValue_ID != ? And (Value = ? or Name=?) And AD_Client_ID = ? And IsActive = 'Y'", get_TrxName())
					.setParameters(getC_ElementValue_ID(), getValue(), getName(), getAD_Client_ID())
					.list();
			if (relValue.size() >= 1) {
				log.saveError("Error", Msg.getMsg(getCtx(), "ValueOrNameExists"));//ValueExists, NameExists
				return false;
			}

		}
		
		if (!success)
			return success;
		if (newRecord || is_ValueChanged(COLUMNNAME_Value))
		{
			// afalcone [Bugs #1837219]
			int ad_Tree_ID= (new MElement(getCtx(), getC_Element_ID(), get_TrxName())).getAD_Tree_ID();
			String treeType= (new MTree(getCtx(),ad_Tree_ID,get_TrxName())).getTreeType();

			if (newRecord)
				insert_Tree(treeType, getC_Element_ID());

			update_Tree(treeType);
		}
		
		

		return success;
	}	//	afterSave
	
	@Override
	protected boolean afterDelete (boolean success)
	{
		if (success)
			delete_Tree(MTree.TREETYPE_CustomTable);
		return success;
	}	//	afterDelete
	
	public static MElementValue get(Properties ctx, int Account_ID) {
		if (Account_ID > 0) {
			StringBuilder whereClause =  new StringBuilder("AD_Client_ID=?").append(" AND C_ElementValue_ID=?").append(" And C_Element_ID = ?");
			ArrayList<Object> params = new ArrayList<Object>();
			int AD_Client_ID = Env.getAD_Client_ID(ctx);
			int C_Element_ID = Env.getContextAsInt(ctx, "#C_Element_ID");
			params.add(AD_Client_ID);
			params.add(Account_ID);
			params.add(C_Element_ID);
			MElementValue acct = new Query(ctx, MElementValue.Table_Name, whereClause.toString(), null)
					.setParameters(params)
					.setOnlyActiveRecords(true)
					.first();
			if (acct != null) {
				return acct;
			}
		}
		return null;
	}

}	//	MElementValue
