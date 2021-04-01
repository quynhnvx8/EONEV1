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
package org.compiere.report;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.util.DB;

import eone.base.model.X_PA_ReportColumn;

/**
 *  Report Column Model
 *
 *  @author Jorg Janke
 *  @version $Id: MReportColumn.java,v 1.3 2006/08/03 22:16:52 jjanke Exp $
 */
public class MReportColumn extends X_PA_ReportColumn
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2395905882810790219L;

	/**
	 * 	Constructor
	 * 	@param ctx context
	 * 	@param PA_ReportColumn_ID id
	 * 	@param trxName transaction
	 */
	public MReportColumn (Properties ctx, int PA_ReportColumn_ID, String trxName)
	{
		super (ctx, PA_ReportColumn_ID, trxName);
		if (PA_ReportColumn_ID == 0)
		{
			setIsPrinted (true);
			setSeqNo (0);
		}
		loadColumns();
	}	//	MReportColumn

	/**
	 * 	Constructor
	 * 	@param ctx context
	 * 	@param rs ResultSet to load from
	 * 	@param trxName transaction
	 */
	public MReportColumn (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MReportColumn

	/**************************************************************************
	 * 	Get Column SQL Select Clause.
	 * 	@param withSum with SUM() function
	 * 	@return select clause - AmtAcctCR+AmtAcctDR/etc or "null" if not defined
	 */
	public String getSelectClause (boolean withSum)
	{
		StringBuilder sb = new StringBuilder();
		if (withSum)
			sb.append("SUM(");
		
		if (withSum)
			sb.append(")");
		return sb.toString();
	}	//	getSelectClause

	
	/**
	 * 	Get Segment Value Where Clause
	 * 	@param PA_Hierarchy_ID hierarchy
	 * 	@return where clause
	 */
	public String getWhereClause(int PA_Hierarchy_ID)
	{
		
		
		return "";
	}	//	getWhereClause
	
	
	public String toString ()
	{
		StringBuilder sb = new StringBuilder ("MReportColumn[")
			.append(get_ID()).append(" - ").append(getName()).append(" - ").append(getDescription())
			.append(", SeqNo=").append(getSeqNo());
		
		sb.append ("]");
		return sb.toString ();
	}	//	toString

	
	@Override
	protected boolean beforeSave(boolean newRecord)
	{
		
		return true;
	}	//	beforeSave
	/**************************************************************************

	/**
	 * 	Copy
	 * 	@param ctx context
	 * 	@param AD_Client_ID parent
	 * 	@param AD_Org_ID parent
	 * 	@param PA_ReportColumnSet_ID parent
	 * 	@param source copy source
	 * 	@param trxName transaction
	 * 	@return Report Column
	 */
	public static MReportColumn copy (Properties ctx, int AD_Client_ID, int AD_Org_ID, 
		int PA_ReportColumnSet_ID, MReportColumn source, String trxName)
	{
		MReportColumn retValue = new MReportColumn (ctx, 0, trxName);
		MReportColumn.copyValues(source, retValue, AD_Client_ID, AD_Org_ID);
		retValue.setOper_1_ID(0);
		retValue.setOper_2_ID(0);
		return retValue;
	}	//	copy
	
	/** Contained Columns		*/
	private MReportColumn[]	m_columns = null;


	private void loadColumns()
	{
		ArrayList<MReportColumn> list = new ArrayList<MReportColumn>();
		String sql = "SELECT * FROM PA_ReportColumn WHERE PA_Report_ID=? AND IsActive='Y' ORDER BY SeqNo";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_TrxName());
			pstmt.setInt(1, getPA_Report_ID());
			rs = pstmt.executeQuery();
			while (rs.next())
				list.add(new MReportColumn (getCtx(), rs, null));
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
		//
		m_columns = new MReportColumn[list.size()];
		list.toArray(m_columns);
		if (log.isLoggable(Level.FINEST)) log.finest("ID=" + getPA_ReportColumn_ID() 
			+ " - Size=" + list.size());
	}	//	loadColumns


	public MReportColumn[] getColumns()
	{
		return m_columns;
	}	//	getColumns
}	//	MReportColumn
