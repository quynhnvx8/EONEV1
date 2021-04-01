package eone.base.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;

import org.compiere.util.AdempiereSystemError;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;

import eone.base.model.MColumn;
import eone.base.model.MField;
import eone.base.model.MTab;
import eone.base.model.MTable;
import eone.base.model.PO;


public class TabCreateFields extends SvrProcess
{
	/**	Tab Number				*/
	private int	p_AD_Tab_ID= 0;
	
	/**
	 * 	prepare
	 */
	protected void prepare ()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		p_AD_Tab_ID = getRecord_ID();
	}	//	prepare

	/**
	 * 	Process
	 *	@return info
	 *	@throws Exception
	 */
	protected String doIt () throws Exception
	{
		MTab tab = new MTab (getCtx(), p_AD_Tab_ID, get_TrxName());
		if (p_AD_Tab_ID == 0 || tab == null || tab.get_ID() == 0)
			throw new AdempiereSystemError("@NotFound@: @AD_Tab_ID@ " + p_AD_Tab_ID);
		if (log.isLoggable(Level.INFO)) log.info(tab.toString());
		//
		int count = 0;
		String sql = "SELECT * FROM AD_Column c "
			+ "WHERE NOT EXISTS (SELECT * FROM AD_Field f "
				+ "WHERE c.AD_Column_ID=f.AD_Column_ID"
				+ " AND c.AD_Table_ID=?"	//	#1
				+ " AND f.AD_Tab_ID=?)"		//	#2
			+ " AND AD_Table_ID=?"			//	#3
			
			+ " AND IsActive='Y' ";

		

		sql += "ORDER  BY CASE "
			+ "            WHEN c.ColumnName = 'AD_Client_ID' THEN -100 "
			+ "            WHEN c.ColumnName = 'AD_Org_ID' THEN -90 "
			+ "            WHEN c.ColumnName = 'Value' THEN -80 "
			+ "            WHEN c.ColumnName = 'Name' THEN -70 "
			+ "            WHEN c.ColumnName = 'Description' THEN -60 "
			+ "            WHEN c.ColumnName = 'Help' THEN -50 "
			+ "            WHEN c.ColumnName = 'IsActive' THEN 1000"
			+ "            WHEN c.ColumnName = 'CreatedBy' THEN 1010 "
			+ "            WHEN c.ColumnName = 'Created' THEN 1020 "
			+ "            WHEN c.ColumnName = 'UpdatedBy' THEN 1030 "
			+ "            WHEN c.ColumnName = 'Updated' THEN 1040 "
			+ "            ELSE c.AD_Column_ID "
			+ "          END ";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int seqno = DB.getSQLValue(null, "SELECT MAX(SeqNo) FROM AD_Field WHERE AD_Tab_ID=?", tab.getAD_Tab_ID());
		int seqnogrid = DB.getSQLValue(null, "SELECT MAX(SeqNoGrid) FROM AD_Field WHERE AD_Tab_ID=?", tab.getAD_Tab_ID());
		seqno = seqno + 10;
		seqnogrid = seqnogrid + 10;
		try
		{
			pstmt = DB.prepareStatement (sql, get_TrxName());
			pstmt.setInt (1, tab.getAD_Table_ID());
			pstmt.setInt (2, tab.getAD_Tab_ID());
			pstmt.setInt (3, tab.getAD_Table_ID());
			
			rs = pstmt.executeQuery ();
			String uuidcolname = PO.getUUIDColumnName(tab.getAD_Table().getTableName());
			while (rs.next ())
			{
				MColumn column = new MColumn (getCtx(), rs, get_TrxName());
				//
				MField field = new MField (tab);
				field.setColumn(column);
								
				
				if ("D".equals(column.getEntityType()))
					field.setEntityType(tab.getEntityType());
				else
					field.setEntityType(column.getEntityType());
				
				// end F3P
				
				if (column.isKey() || uuidcolname.equalsIgnoreCase(field.getName())) {
					field.setIsDisplayed(false);
					field.setIsDisplayedGrid(false);
				}

				// Assign some default formatting
				if (column.getAD_Reference_ID() == DisplayType.Button || column.getAD_Reference_ID() == DisplayType.YesNo) {
					field.setXPosition(1);
				}
				field.setColumnSpan(2);
				if (column.getFieldLength() >= 60) {
					field.setColumnSpan(4);
				}
				if (column.getAD_Reference_ID() == DisplayType.Text) {
					field.setNumLines(3);
				} else if (column.getAD_Reference_ID() == DisplayType.TextLong) {
					field.setNumLines(5);
				} else if (column.getAD_Reference_ID() == DisplayType.Memo) {
					field.setNumLines(8);
				}
				String accessLevel = tab.getAD_Table().getAccessLevel();
				if (column.getColumnName().equals("AD_Org_ID")) {
					field.setXPosition(3);
					if (   accessLevel.equals(MTable.ACCESSLEVEL_ClientOnly)
						|| accessLevel.equals(MTable.ACCESSLEVEL_SystemOnly)
						|| accessLevel.equals(MTable.ACCESSLEVEL_SystemPlusClient)) {
						field.setIsDisplayedGrid(false);
					}
				}
				if (column.getColumnName().equals("AD_Client_ID")) {
					if (! (accessLevel.equals(MTable.ACCESSLEVEL_All)
						|| accessLevel.equals(MTable.ACCESSLEVEL_SystemPlusClient))) {
						field.setIsDisplayedGrid(false);
						field.setIsDisplayed(false);
					}
				}
				if (field.isDisplayed()) {
					field.setSeqNo(seqno);
					seqno = seqno + 10;
				}

				if (field.isDisplayedGrid()) {
					field.setSeqNoGrid(seqnogrid);
					seqnogrid = seqnogrid + 10;
				}

				if (column.getColumnName().equals("Created") 
						||column.getColumnName().equals("CreatedBy")
						||column.getColumnName().equals("Updated")
						||column.getColumnName().equals("UpdatedBy")) {
					field.setIsReadOnly(true);
					field.setIsDisplayed(false);
					field.setIsDisplayedGrid(false);
					if (column.getColumnName().equals("CreatedBy"))
					{
						field.setXPosition(1);
						field.setColumnSpan(1);
					}
					if (column.getColumnName().equals("Created"))
					{
						field.setXPosition(2);
						field.setColumnSpan(1);
					}
					if (column.getColumnName().equals("UpdatedBy"))
					{
						field.setXPosition(3);
						field.setColumnSpan(1);
					}
					if (column.getColumnName().equals("Updated"))
					{
						field.setXPosition(4);
						field.setColumnSpan(1);
					}
					
					if (column.getColumnName().equals("Processed"))
					{
						field.setIsDisplayedGrid(false);
						field.setIsDisplayed(false);
					}
					if (column.getColumnName().equals("IsRecordUsed"))
					{
						field.setIsDisplayedGrid(false);
						field.setIsDisplayed(false);
					}
				}

				if (field.save())
				{
					addLog(0, null, null, column.getName());
					count++;
				}
			}
 		}
		catch (Exception e)
		{
			log.log (Level.SEVERE, sql, e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		StringBuilder msgreturn = new StringBuilder("@Created@ #").append(count);
		return msgreturn.toString();
	}	//	doIt
	
}	
