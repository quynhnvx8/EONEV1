package eone.base.model;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.util.AdempiereUserError;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Util;

import eone.base.process.DocAction;
import eone.base.process.DocumentEngine;

public class MProduction extends X_M_Production implements DocAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4650232602150964606L;

	/**
	 * 
	 */
	/** Log								*/
	protected static CLogger		m_log = CLogger.getCLogger (MProduction.class);
	protected int lineno;
	protected int count;

	public MProduction(Properties ctx, int M_Production_ID, String trxName) {
		super(ctx, M_Production_ID, trxName);
		if (M_Production_ID == 0) {
			setDocStatus(DOCSTATUS_Drafted);
			setDocAction (DOCACTION_Complete);
		}
	}

	public MProduction(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}
	
	public MProduction( MOrderLine line ) {
		super( line.getCtx(), 0, line.get_TrxName());
		setAD_Client_ID(line.getAD_Client_ID());
		setAD_Org_ID(line.getAD_Org_ID());
	}

	public MProduction( MProjectLine line ) {
		super( line.getCtx(), 0, line.get_TrxName());
		MProject project = new MProject(line.getCtx(), line.getC_Project_ID(), line.get_TrxName());
		
		
		setAD_Client_ID(line.getAD_Client_ID());
		setAD_Org_ID(line.getAD_Org_ID());
		setDescription(project.getValue()+"_"+project.getName()+" Line: "+line.getLine()+" (project)");
		setC_Project_ID(line.getC_Project_ID());
		setC_BPartner_ID(project.getC_BPartner_ID());
	}

	@Override
	public String completeIt()
	{
		m_processMsg = null;
		if (m_processMsg != null)
			return DocAction.STATUS_Drafted;
		setProcessed(true);
		setDocAction(DOCACTION_Close);
		return DocAction.STATUS_Completed;
	}


	
	

	public MProductionLine[] getLines() {
		ArrayList<MProductionLine> list = new ArrayList<MProductionLine>();
		
		String sql = "SELECT pl.M_ProductionLine_ID "
			+ "FROM M_ProductionLine pl "
			+ "WHERE pl.M_Production_ID = ?";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_TrxName());
			pstmt.setInt(1, get_ID());
			rs = pstmt.executeQuery();
			while (rs.next())
				list.add( new MProductionLine( getCtx(), rs.getInt(1), get_TrxName() ) );	
		}
		catch (SQLException ex)
		{
			throw new AdempiereException("Unable to load production lines", ex);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}
		
		MProductionLine[] retValue = new MProductionLine[list.size()];
		list.toArray(retValue);
		return retValue;
	}
	
	public void deleteLines(String trxName) {

		for (MProductionLine line : getLines())
		{
			line.deleteEx(true);
		}

	}// deleteLines

	public int createLines(boolean mustBeStocked) {
		
		lineno = 100;

		count = 0;

		// product to be produced
		MProduct finishedProduct = new MProduct(getCtx(), getM_Product_ID(), get_TrxName());
		

		MProductionLine line = new MProductionLine( this );
		line.setLine( lineno );
		line.setM_Product_ID( finishedProduct.get_ID() );
		line.setM_Warehouse_ID( getM_Warehouse_ID() );
		line.setMovementQty( getProductionQty());
		line.setPlannedQty(getProductionQty());
		
		line.saveEx();
		count++;
		
		createLines(mustBeStocked, finishedProduct, getProductionQty());
		
		return count;
	}

	protected int createLines(boolean mustBeStocked, MProduct finishedProduct, BigDecimal requiredQty) {
		
		
		
		
		// products used in production
		String sql = "SELECT M_ProductBom_ID, BOMQty" + " FROM M_Product_BOM"
				+ " WHERE M_Product_ID=" + finishedProduct.getM_Product_ID() + " ORDER BY Line";

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = DB.prepareStatement(sql, get_TrxName());

			rs = pstmt.executeQuery();
			while (rs.next()) {
				
				lineno = lineno + 10;
				int BOMProduct_ID = rs.getInt(1);
				BigDecimal BOMQty = rs.getBigDecimal(2);
				BigDecimal BOMMovementQty = BOMQty.multiply(requiredQty);
				
				MProduct bomproduct = new MProduct(Env.getCtx(), BOMProduct_ID, get_TrxName());
				
				createLines(mustBeStocked, bomproduct, BOMMovementQty);
				
			} // for all bom products
		} catch (Exception e) {
			throw new AdempiereException("Failed to create production lines", e);
		}
		finally {
			DB.close(rs, pstmt);
		}

		return count;
	}
	
	@Override
	protected boolean beforeDelete() {
		deleteLines(get_TrxName());
		return true;
	}

	
	@Override
	public boolean processIt(String action, int AD_Window_ID) throws Exception {
		m_processMsg = null;
		DocumentEngine engine = new DocumentEngine (this, getDocStatus(), AD_Window_ID);
		return engine.processIt (action, getDocStatus());
	}

	/**	Process Message 			*/
	protected String		m_processMsg = null;
	/**	Just Prepared Flag			*/
	protected boolean		m_justPrepared = false;

	

	

	protected String validateEndProduct(int M_Product_ID) {
		String msg = isBom(M_Product_ID);
		if (!Util.isEmpty(msg))
			return msg;

		if (!costsOK(M_Product_ID)) {
			msg = "Excessive difference in standard costs";
			if (MSysConfig.getBooleanValue(MSysConfig.MFG_ValidateCostsDifferenceOnCreate, false, getAD_Client_ID())) {
				return msg;
			} else {
				log.warning(msg);
			}
		}

		return null;
	}

	protected String isBom(int M_Product_ID)
	{
		String bom = DB.getSQLValueString(get_TrxName(), "SELECT isbom FROM M_Product WHERE M_Product_ID = ?", M_Product_ID);
		if ("N".compareTo(bom) == 0)
		{
			return "Attempt to create product line for Non Bill Of Materials";
		}
		int materials = DB.getSQLValue(get_TrxName(), "SELECT count(M_Product_BOM_ID) FROM M_Product_BOM WHERE M_Product_ID = ?", M_Product_ID);
		if (materials == 0)
		{
			return "Attempt to create product line for Bill Of Materials with no BOM Products";
		}
		return null;
	}

	protected boolean costsOK(int M_Product_ID) throws AdempiereUserError {
		
		return true;
	}

	
	
	protected MProduction copyFrom(Timestamp reversalDate) {
		MProduction to = new MProduction(getCtx(), 0, get_TrxName());
		PO.copyValues (this, to, getAD_Client_ID(), getAD_Org_ID());

		to.set_ValueNoCheck ("DocumentNo", null);
		//
		to.setDocStatus (DOCSTATUS_Drafted);		//	Draft
		to.setDocAction(DOCACTION_Complete);
		to.setDateAcct(reversalDate);
		to.setProcessing(false);
		to.setProcessed(false);
		to.setProductionQty(getProductionQty().negate());	
		to.saveEx();
		MProductionLine[] flines = getLines();
		for(MProductionLine fline : flines) {
			MProductionLine tline = new MProductionLine(to);
			PO.copyValues (fline, tline, getAD_Client_ID(), getAD_Org_ID());
			tline.setM_Production_ID(to.getM_Production_ID());
			tline.setMovementQty(fline.getMovementQty().negate());
			tline.setPlannedQty(fline.getPlannedQty().negate());
			tline.setQtyUsed(fline.getQtyUsed().negate());
			tline.saveEx();
		}

		return to;
	}

	/**
	 * 	Add to Description
	 *	@param description text
	 */
	public void addDescription (String description)
	{
		String desc = getDescription();
		if (desc == null)
			setDescription(description);
		else{
			StringBuilder msgd = new StringBuilder(desc).append(" | ").append(description);
			setDescription(msgd.toString());
		}
	}	//	addDescription

	

	@Override
	public boolean reActivateIt() {
		if (log.isLoggable(Level.INFO)) log.info("reActivateIt - " + toString());
		
		return false;
	}

	@Override
	public String getSummary() {
		return getDocumentNo() + " - " + getDescription();
	}


	@Override
	public String getProcessMsg() {
		return m_processMsg;
	}

	@Override
	public void setProcessMsg(String text) {
		m_processMsg = text;
	}
	
	@Override
	protected boolean beforeSave(boolean newRecord) {
		
		return true;
	}

	@Override
	public int getAD_Window_ID() {
		// TODO Auto-generated method stub
		return 0;
	}
}
