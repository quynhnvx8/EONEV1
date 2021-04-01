
package eone.base.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;


public class MInOutLine extends X_M_InOutLine
{
	/**
	 *
	 */
	private static final long serialVersionUID = 8630611882798722864L;

	
	public MInOutLine (Properties ctx, int M_InOutLine_ID, String trxName)
	{
		super (ctx, M_InOutLine_ID, trxName);
		getParent();
		
	}	//	MInOutLine


	public MInOutLine (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MInOutLine

	/**
	 *  Parent Constructor
	 *  @param inout parent
	 */
	public MInOutLine (MInOut inout)
	{
		this (inout.getCtx(), 0, inout.get_TrxName());
		setClientOrg (inout);
		setM_InOut_ID (inout.getM_InOut_ID());
		m_parent = inout;
	}	//	MInOutLine

	/**	Product					*/
	private MProduct 		m_product = null;
	
	private MInOut			m_parent = null;

	/**
	 * 	Get Parent
	 *	@return parent
	 */
	public MInOut getParent()
	{
		if (m_parent == null)
			m_parent = new MInOut (getCtx(), getM_InOut_ID(), get_TrxName());
		return m_parent;
	}	//	getParent

	
	public MProduct getProduct()
	{
		if (m_product == null && getM_Product_ID() != 0)
			m_product = MProduct.get (getCtx(), getM_Product_ID());
		return m_product;
	}	//	getProduct

	/**
	 * 	Set Product
	 *	@param product product
	 */
	public void setProduct (MProduct product)
	{
		m_product = product;
		if (m_product != null)
		{
			setM_Product_ID(m_product.getM_Product_ID());
			setC_UOM_ID (m_product.getC_UOM_ID());
		}
		else
		{
			setM_Product_ID(0);
			setC_UOM_ID (0);
		}
	}	//	setProduct

	/**
	 * 	Set M_Product_ID
	 *	@param M_Product_ID product
	 *	@param setUOM also set UOM from product
	 */
	public void setM_Product_ID (int M_Product_ID, boolean setUOM)
	{
		if (setUOM)
			setProduct(MProduct.get(getCtx(), M_Product_ID));
		else
			super.setM_Product_ID (M_Product_ID);
	}	//	setM_Product_ID

	/**
	 * 	Set Product and UOM
	 *	@param M_Product_ID product
	 *	@param C_UOM_ID uom
	 */
	public void setM_Product_ID (int M_Product_ID, int C_UOM_ID)
	{
		if (M_Product_ID != 0)
			super.setM_Product_ID (M_Product_ID);
		super.setC_UOM_ID(C_UOM_ID);
		m_product = null;
	}	//	setM_Product_ID

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

	
	
	protected boolean beforeSave (boolean newRecord)
	{
		MInOut inout = new MInOut(getCtx(), getM_InOut_ID(), get_TrxName());
		MDocType mDocyType = MDocType.get(getCtx(), inout.getC_DocType_ID());
		boolean check = mDocyType.getDocType().equals(MDocType.DOCTYPE_Output);
		//Kiem tra so luong ton kho khi lam lenh xuat. 
		//Bat buoc check tu bang M_Storage vi cau truc logic cua he thong.
		
		//Neu la xuat va co cau hinh check so luong ton kho trong bang M_Storage (Voi dieu kien bang nay du lieu phai input day du)
		//De input day du can kiem tra nghiep vu: CO, RA va UpdateOpenBalanceWarehouse.java
		if (check && Env.checkRemainQty()) {
			BigDecimal qtyRemain = MStorage.getQtyRemain(getM_Product_ID(), inout.getM_Warehouse_Cr_ID(), inout.getDateAcct());
			if (qtyRemain.compareTo(getQty()) < 0)
			{
				log.saveError("Error!", "Not enough quantity!");
				return false;
			}			
		}
		log.fine("");
		if (newRecord && getParent().isComplete()) {
			log.saveError("ParentComplete", Msg.translate(getCtx(), "M_InOutLine"));
			return false;
		}
		
		if (getQty().compareTo(Env.ZERO) < 0) {
			log.saveError("Error!", "Quantity must be than zero!");
			return false;
		}
		//	Get Line No
		if (getLine() == 0)
		{
			String sql = "SELECT COALESCE(MAX(Line),0)+1 FROM M_InOutLine WHERE M_InOut_ID=?";
			int ii = DB.getSQLValueEx (get_TrxName(), sql, getM_InOut_ID());
			setLine (ii);
		}
		//	UOM
		if (getC_UOM_ID() == 0)
			setC_UOM_ID (Env.getContextAsInt(getCtx(), "#C_UOM_ID"));
		
		return true;
	}	//	beforeSave

	protected boolean afterSave (boolean newRecord, boolean success)
	{
		String sql = "UPDATE M_InOut o"
				+ " SET (Amount, AmountConvert, TaxAmt, TaxBaseAmt) ="
				+ "(SELECT Sum(Amount), Sum(AmountConvert), Sum(TaxAmt), Sum(TaxBaseAmt)"
				+ " FROM M_InOutLine ol WHERE ol.M_InOut_ID=o.M_InOut_ID) "
				+ "WHERE M_InOut_ID=?";
		DB.executeUpdateEx(sql, new Object[] {getM_InOut_ID()}, get_TrxName());
		
		return true;
	}	
	@Override
	protected boolean afterDelete(boolean success) {
		String sql = "UPDATE M_InOut o"
				+ " SET (Amount, AmountConvert, TaxAmt, TaxBaseAmt) ="
				+ "(SELECT Sum(Amount), Sum(AmountConvert), Sum(TaxAmt), Sum(TaxBaseAmt)"
				+ " FROM M_InOutLine ol WHERE ol.M_InOut_ID=o.M_InOut_ID) "
				+ "WHERE M_InOut_ID=?";
		int no = DB.executeUpdateEx(sql, new Object[] {getM_InOut_ID()}, get_TrxName());
		if (no < 0)
			return false;
		return true;
	}

	/**
	 * 	Before Delete
	 *	@return true if drafted
	 */
	protected boolean beforeDelete ()
	{
		if (! getParent().getDocStatus().equals(MInOut.DOCSTATUS_Drafted)) {
			log.saveError("Error", Msg.getMsg(getCtx(), "CannotDelete"));
			return false;
		}
		
		return true;
	}	//	beforeDelete

	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuilder sb = new StringBuilder ("MInOutLine[").append (get_ID())
			.append(",M_Product_ID=").append(getM_Product_ID())
			.append ("]");
		return sb.toString ();
	}	//	toString

	/**
	 * 	Get Base value for Cost Distribution
	 *	@param CostDistribution cost Distribution
	 *	@return base number
	 */
	public BigDecimal getBase (String CostDistribution)
	{
		
		return Env.ZERO;
	}	//	getBase

	
}	//	MInOutLine
