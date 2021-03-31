package org.compiere.report;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.model.X_PA_Report;
import org.compiere.util.DB;


public class MReport extends X_PA_Report
{
	
	private static final long serialVersionUID = 1765138347185608173L;


	public MReport (Properties ctx, int PA_Report_ID, String trxName)
	{
		super (ctx, PA_Report_ID, trxName);
		
	}	//	MReport


	public MReport (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
		
	}	//	MReport

	private MReportColumn	m_column = null;
	private MReportLine		m_line = null;
	
	public String getWhereClause()
	{
		
		return "";
	}	//	getWhereClause

	protected boolean beforeSave (boolean newRecord)
	{
		
		
		
		return true;
	}

	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if(!success)
		{
			return success;
		}
		
		if (isDetailReportLine()) {
			String sql = "Update PA_ReportColumn set AmountType = 'NO' Where PA_Report_ID = ? ";
			DB.executeUpdateEx(sql, new Object [] {getPA_Report_ID()}, null);
		}
		
		return true;
	}


	public String toString ()
	{
		StringBuilder sb = new StringBuilder ("MReport[")
			.append(get_ID()).append(" - ").append(getName());
		if (getDescription() != null)
			sb.append("(").append(getDescription()).append(")");
		sb.append ("]");
		return sb.toString ();
	}	//	toString


	public MReportColumn	getColumn()
	{
		return m_column;
	}


	public MReportLine getLine()
	{
		return m_line;
	}

}	//	MReport
