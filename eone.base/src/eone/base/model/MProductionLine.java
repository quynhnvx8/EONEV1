package eone.base.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.DB;
import org.compiere.util.Env;


public class MProductionLine extends X_M_ProductionLine {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3720901152312853611L;

	protected MProduction productionParent;


	/**
	 * 	Standard Constructor
	 *	@param ctx ctx
	 *	@param M_ProductionLine_ID id
	 */
	public MProductionLine (Properties ctx, int M_ProductionLine_ID, String trxName)
	{
		super (ctx, M_ProductionLine_ID, trxName);
		if (M_ProductionLine_ID == 0)
		{
			setLine (0);	// @SQL=SELECT NVL(MAX(Line),0)+10 AS DefaultValue FROM M_ProductionLine WHERE M_Production_ID=@M_Production_ID@
			setM_ProductionLine_ID (0);
			setM_Production_ID (0);
			setMovementQty (Env.ZERO);
			setProcessed (false);
		}
			
	}	// MProductionLine
	
	public MProductionLine (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MProductionLine
	
	/**
	 * Parent Constructor
	 * @param plan
	 */
	public MProductionLine( MProduction header ) {
		super( header.getCtx(), 0, header.get_TrxName() );
		setM_Production_ID( header.get_ID());
		setAD_Client_ID(header.getAD_Client_ID());
		setAD_Org_ID(header.getAD_Org_ID());
		productionParent = header;
	}
	
	public MProductionLine( MProductionPlan header ) {
		super( header.getCtx(), 0, header.get_TrxName() );
		setM_ProductionPlan_ID( header.get_ID());
		setAD_Client_ID(header.getAD_Client_ID());
		setAD_Org_ID(header.getAD_Org_ID());
	}
	

	

	public String toString() {
		if ( getM_Product_ID() == 0 )
			return ("No product defined for production line " + getLine());
		MProduct product = new MProduct(getCtx(),getM_Product_ID(), get_TrxName());
		return ( "Production line:" + getLine() + " -- " + getMovementQty() + " of " + product.getValue());
	}

	@Override
	protected boolean beforeSave(boolean newRecord) 
	{
		if (productionParent == null && getM_Production_ID() > 0)
			productionParent = new MProduction(getCtx(), getM_Production_ID(), get_TrxName());

		if (getM_Production_ID() > 0) 
		{
			if ( productionParent.getM_Product_ID() == getM_Product_ID() && productionParent.getProductionQty().signum() == getMovementQty().signum())
				setIsEndProduct(true);
			else 
				setIsEndProduct(false);
		} 
		else 
		{
			I_M_ProductionPlan plan = getM_ProductionPlan();
			if (plan.getM_Product_ID() == getM_Product_ID() && plan.getProductionQty().signum() == getMovementQty().signum())
				setIsEndProduct(true);
			else 
				setIsEndProduct(false);
		}
		
		
		
		if ( !isEndProduct() )
		{
			setMovementQty(getQtyUsed().negate());
		}
		
		return true;
	}
	
	@Override
	protected boolean beforeDelete() {
		
		return true;
	}

	/**
	 * 	Get Reversal_ID of parent production
	 *	@return Reversal_ID
	 */
	public int getProductionReversalId() {
		if (getM_Production_ID() > 0)
			return DB.getSQLValueEx(get_TrxName(), "SELECT Reversal_ID FROM M_Production WHERE M_Production_ID=?", getM_Production_ID());
		else
			return DB.getSQLValueEx(get_TrxName(), "SELECT p.Reversal_ID FROM M_ProductionPlan pp INNER JOIN M_Production p ON (pp.M_Production_ID = p.M_Production_ID) WHERE pp.M_ProductionPlan_ID=?", getM_ProductionPlan_ID());
	}

	
}
