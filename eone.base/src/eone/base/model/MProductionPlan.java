/**
 * 
 */
package eone.base.model;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

import org.compiere.util.DB;

import eone.exceptions.EONEException;

/**
 * @author hengsin
 *
 */
public class MProductionPlan extends X_M_ProductionPlan {

	/**
	 * generated serial id
	 */
	private static final long serialVersionUID = -8189507724698695756L;

	/**
	 * @param ctx
	 * @param M_ProductionPlan_ID
	 * @param trxName
	 */
	public MProductionPlan(Properties ctx, int M_ProductionPlan_ID,
			String trxName) {
		super(ctx, M_ProductionPlan_ID, trxName);
	}

	/**
	 * @param ctx
	 * @param rs
	 * @param trxName
	 */
	public MProductionPlan(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	public MProductionLine[] getLines() {
		ArrayList<MProductionLine> list = new ArrayList<MProductionLine>();
		
		String sql = "SELECT pl.M_ProductionLine_ID "
			+ "FROM M_ProductionLine pl "
			+ "WHERE pl.M_ProductionPlan_ID = ?";
		
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
			throw new EONEException("Unable to load production lines", ex);
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
		
		int lineno = 100;

		int count = 0;

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
		
		count = count + createLines(mustBeStocked, finishedProduct, getProductionQty(), lineno);
		
		return count;
	}

	private int createLines(boolean mustBeStocked, MProduct finishedProduct, BigDecimal requiredQty, int lineno) {
		
		

		return 0;
	}
	
	@Override
	protected boolean beforeDelete() {
		deleteLines(get_TrxName());
		return true;
	}
}
