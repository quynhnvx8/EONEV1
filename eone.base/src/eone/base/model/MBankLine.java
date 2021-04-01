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
 * Contributor(s): Teo Sarca                                                  *
 *****************************************************************************/
package eone.base.model;
 
import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.DB;
import org.compiere.util.Msg;
 
/**
 *	Bank Statement Line Model
 *
 *	@author Eldir Tomassen/Jorg Janke
 *	@version $Id: MBankStatementLine.java,v 1.3 2006/07/30 00:51:02 jjanke Exp $
 *  
 *  Carlos Ruiz - globalqss - integrate bug fixing from Teo Sarca
 *    [ 1619076 ] Bank statement's StatementDifference becames NULL
 *
 * @author Teo Sarca, SC ARHIPAC SERVICE SRL
 * 			<li>BF [ 1896880 ] Unlink Payment if TrxAmt is zero
 * 			<li>BF [ 1896885 ] BS Line: don't update header if after save/delete fails
 */
 public class MBankLine extends X_C_BankLine
 {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3809130336412385420L;

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_BankStatementLine_ID id
	 *	@param trxName transaction
	 */
	public MBankLine (Properties ctx, int C_BankLine_ID, String trxName)
	{
		super (ctx, C_BankLine_ID, trxName);
		
	}	//	MBankStatementLine
	
	/**
	 *	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName transaction
	 */
	public MBankLine (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MBankStatementLine
	
	/**
	 * 	Parent Constructor
	 * 	@param statement Bank Statement that the line is part of
	 */
	public MBankLine(MBank statement)
	{
		this (statement.getCtx(), 0, statement.get_TrxName());
		setClientOrg(statement);
		setC_Bank_ID(statement.getC_Bank_ID());
	}	//	MBankStatementLine

	/**
	 * 	Parent Constructor
	 * 	@param statement Bank Statement that the line is part of
	 * 	@param lineNo position of the line within the statement
	 */
	public MBankLine(MBank statement, int lineNo)
	{
		this (statement);
		setLine(lineNo);
	}	//	MBankStatementLine



	
	public void addDescription (String description)
	{
		String desc = getDescription();
		if (desc == null)
			setDescription(description);
		else{
			StringBuilder msgsd = new StringBuilder(desc).append(" | ").append(description);
			setDescription(msgsd.toString());
		}
	}	//	addDescription

	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		if (newRecord && getParent().isComplete()) {
			log.saveError("ParentComplete", Msg.translate(getCtx(), "C_BankStatementLine"));
			return false;
		}
		
		return true;
	}	//	beforeSave
	
	/** Parent					*/
	protected MBank m_parent = null;
	
	/**
	 * 	Get Parent
	 *	@return parent
	 */
	public MBank getParent()
	{
		if (m_parent == null)
			m_parent = new MBank (getCtx(), getC_Bank_ID(), get_TrxName());
		return m_parent;
	}	//	getParent
	
	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return success
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if (!success)
			return success;
		return updateHeader();
	}	//	afterSave
	
	/**
	 * 	After Delete
	 *	@param success success
	 *	@return success
	 */
	protected boolean afterDelete (boolean success)
	{
		if (!success)
			return success;
		return updateHeader();
	}	//	afterSave

	/**
	 * 	Update Header
	 */
	protected boolean updateHeader()
	{
		StringBuilder sql = new StringBuilder("UPDATE C_Bank bs")
			.append(" SET (Amount, AmountConvert)=(SELECT COALESCE(SUM(Amount),0), COALESCE(SUM(AmountConvert),0) FROM C_BankLine bsl ")
				.append("WHERE bsl.C_Bank_ID=bs.C_Bank_ID AND bsl.IsActive='Y') ")
			.append("WHERE C_Bank_ID=").append(getC_Bank_ID());
		int no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (no != 1) {
			log.warning("StatementDifference #" + no);
			return false;
		}
		
		return true;
	}	//	updateHeader
	
 }	//	MBankStatementLine
