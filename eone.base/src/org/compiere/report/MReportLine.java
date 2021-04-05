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

import eone.base.model.X_PA_ReportLine;


/**
 *	Report Line Model
 *
 *  @author Jorg Janke
 *  @version $Id: MReportLine.java,v 1.3 2006/08/03 22:16:52 jjanke Exp $
 */
public class MReportLine extends X_PA_ReportLine
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3957315092529097396L;

	/**
	 * 	Constructor
	 * 	@param ctx context
	 * 	@param PA_ReportLine_ID id
	 * 	@param trxName transaction
	 */
	public MReportLine (Properties ctx, int PA_ReportLine_ID, String trxName)
	{
		super (ctx, PA_ReportLine_ID, trxName);
		if (PA_ReportLine_ID == 0)
		{
			setSeqNo (0);
		}
		loadLines();
	}	//	MReportLine

	/**
	 * 	Constructor
	 * 	@param ctx context
	 * 	@param rs ResultSet to load from
	 * 	@param trxName transaction
	 */
	public MReportLine (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MReportLine


		
	/**
	 * 	List Info
	 */
	public void list()
	{
		System.out.println("- " + toString());
	
	}	//	list


	/**
	 * 	Get Source Column Name
	 * 	@return Source ColumnName
	 */
	public String getSourceColumnName()
	{
		
		return "";
	}	//	getColumnName

	/**
	 *  Get Value Query for Segment Type
	 * 	@return Query for first source element or null
	 */
	public String getSourceValueQuery()
	{
		return null;
	}	//


	/**
	 * 	Get SQL Select Clause.
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

	
	
	
	public String toString ()
	{
		StringBuilder sb = new StringBuilder ("MReportLine[")
			.append(get_ID()).append(" - ").append(getName()).append(" - ").append(getDescription())
			.append(", SeqNo=").append(getSeqNo());
		
		sb.append ("]");
		return sb.toString ();
	}	//	toString

	
	protected boolean beforeSave (boolean newRecord)
	{
		if (!isSummary())
		{
			
		}
		return true;
	}	//	beforeSave
	

	/**************************************************************************
	 * 	Copy
	 * 	@param ctx context
	 * 	@param AD_Client_ID parent
	 * 	@param AD_Org_ID parent
	 * 	@param PA_ReportLineSet_ID parent
	 * 	@param source copy source
	 * 	@param trxName transaction
	 * 	@return Report Line
	 */
	public static MReportLine copy (Properties ctx, int AD_Client_ID, int AD_Org_ID, 
		int PA_ReportLineSet_ID, MReportLine source, String trxName)
	{
		MReportLine retValue = new MReportLine (ctx, 0, trxName);
		MReportLine.copyValues(source, retValue, AD_Client_ID, AD_Org_ID);
		return retValue;
	}	//	copy


	
	/**	Contained Lines			*/
	private MReportLine[]	m_lines = null;

	/**
	 * 	Load Lines
	 */
	private void loadLines()
	{
		ArrayList<MReportLine> list = new ArrayList<MReportLine>();
		String sql = "SELECT * FROM PA_ReportLine "
			+ "WHERE PA_Report_ID=? AND IsActive='Y' "
			+ "ORDER BY SeqNo";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_TrxName());
			pstmt.setInt(1, getPA_Report_ID());
			rs = pstmt.executeQuery();
			while (rs.next())
				list.add(new MReportLine (getCtx(), rs, get_TrxName()));
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
		m_lines = new MReportLine[list.size()];
		list.toArray(m_lines);
		if (log.isLoggable(Level.FINEST)) log.finest("ID=" + getPA_Report_ID()
			+ " - Size=" + list.size());
	}	//	loadColumns

	/**
	 * 	Get Liness
	 *	@return array of lines
	 */
	public MReportLine[] getLiness()
	{
		return m_lines;
	}	//	getLines
}	//	MReportLine
