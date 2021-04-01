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

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.DB;
import org.compiere.util.Trx;


/**
 *	Package Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MPackage.java,v 1.3 2006/07/30 00:51:04 jjanke Exp $
 */
public class MPackage extends X_M_Package
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6082002551560148518L;

	
	public MPackage (Properties ctx, int M_Package_ID, String trxName)
	{
		super (ctx, M_Package_ID, trxName);
		
	}	//	MPackage

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName transaction
	 */
	public MPackage (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MPackage
	
	
	
	protected boolean beforeSave(boolean newRecord)
	{
		if (getWeight() == null || getWeight().compareTo(BigDecimal.ZERO) == 0)
		{
			String sql = "SELECT SUM(LineWeight) FROM X_PackageLineWeight plw WHERE plw.M_Package_ID=?";
			BigDecimal weight = DB.getSQLValueBD(get_TrxName(), sql, getM_Package_ID());
			if (weight == null)
				weight = BigDecimal.ZERO;
			setWeight(weight);
		}
		
		return true;
	}
	
	protected boolean afterSave(boolean newRecord, boolean success)
	{
		if (!success)
			return success;
		
		
		
		
		return success;
	}
	
	protected boolean beforeDelete()
	{
		String sql = "DELETE FROM M_PackageLine WHERE M_PackageMPS_ID IN (SELECT M_PackageMPS_ID FROM M_PackageMPS WHERE M_Package_ID = ?)";
		DB.executeUpdate(sql, getM_Package_ID(), get_TrxName());
		
		sql = "DELETE FROM M_PackageMPS WHERE M_Package_ID = ?";
		DB.executeUpdate(sql, getM_Package_ID(), get_TrxName());
		return true;
	}
	
	/** Error Message						*/
	private String				m_errorMessage = null;
	
	public void setErrorMessage(String errorMessage)
	{
		m_errorMessage = errorMessage;
	}
	
	public String getErrorMessage()
	{
		return m_errorMessage;
	}
	
	public boolean processOnline(String action, boolean isPriviledgedRate)
	{
		setErrorMessage(null);

		Trx trx = Trx.get(Trx.createTrxName("spt-"), true);
		trx.setDisplayName(getClass().getName()+"_processOnline");
		
		return true;
	}
	
	
	
}	//	MPackage
