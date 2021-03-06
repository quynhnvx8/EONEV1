/******************************************************************************
 * Product: Compiere ERP & CRM Smart Business Solution                        *
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
package eone.base.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;

import org.compiere.util.DB;
import org.compiere.util.Env;

import eone.base.model.MProduct;

/**
 * 	Validate BOM
 *	
 *  @author Jorg Janke
 *  @version $Id: BOMVerify.java,v 1.1 2007/07/23 05:34:35 mfuggle Exp $
 */
public class BOMVerify extends SvrProcess
{
	/**	The Product			*/
	private int		p_M_Product_ID = 0;
	/** Product Category	*/
	private int		p_M_Product_Category_ID = 0;
	/** Re-Validate			*/
	private boolean	p_IsReValidate = false;

	//private boolean	p_fromButton = false;
	
	
	/**
	 * 	Prepare
	 */
	protected void prepare ()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("M_Product_ID"))
				p_M_Product_ID = para[i].getParameterAsInt();
			else if (name.equals("M_Product_Category_ID"))
				p_M_Product_Category_ID = para[i].getParameterAsInt();
			else if (name.equals("IsReValidate"))
				p_IsReValidate = "Y".equals(para[i].getParameter());
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		if ( p_M_Product_ID == 0 )
			p_M_Product_ID = getRecord_ID();
		//p_fromButton = (getRecord_ID() > 0);
	}	//	prepare

	/**
	 * 	Process
	 *	@return Info
	 *	@throws Exception
	 */
	protected String doIt() throws Exception
	{
		if (p_M_Product_ID != 0)
		{
			if (log.isLoggable(Level.INFO)) log.info("M_Product_ID=" + p_M_Product_ID);
			checkProduct(new MProduct(getCtx(), p_M_Product_ID, get_TrxName()));
			return "Product Checked";
		}
		if (log.isLoggable(Level.INFO)) log.info("M_Product_Category_ID=" + p_M_Product_Category_ID
			+ ", IsReValidate=" + p_IsReValidate);
		//
		int counter = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT M_Product_ID FROM M_Product "
			+ "WHERE IsBOM='Y' AND ";
		if (p_M_Product_Category_ID == 0)
			sql += "AD_Client_ID=? ";
		else
			sql += "M_Product_Category_ID=? ";
		if (!p_IsReValidate)
			sql += "AND IsVerified<>'Y' ";
		sql += "ORDER BY Name";
		int AD_Client_ID = Env.getAD_Client_ID(getCtx());
		try
		{
			pstmt = DB.prepareStatement (sql, get_TrxName());
			if (p_M_Product_Category_ID == 0)
				pstmt.setInt (1, AD_Client_ID);
			else
				pstmt.setInt(1, p_M_Product_Category_ID);
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				p_M_Product_ID = rs.getInt(1); //ADAXA - validate the product retrieved from database
				checkProduct(new MProduct(getCtx(), p_M_Product_ID, get_TrxName()));
				
				counter++;
			}
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		return "#" + counter;
	}	//	doIt

	private void checkProduct(MProduct product)
	{
		
		
	}
	
	
}	//	BOMValidate
