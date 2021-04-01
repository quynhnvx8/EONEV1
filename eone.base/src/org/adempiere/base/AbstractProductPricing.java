
package org.adempiere.base;

import java.math.BigDecimal;
import java.sql.Timestamp;

import org.compiere.util.Env;

import eone.base.model.I_C_InvoiceLine;
import eone.base.model.I_C_OrderLine;
import eone.base.model.I_C_Project;
import eone.base.model.I_C_ProjectLine;

public abstract class AbstractProductPricing implements IProductPricing {
	
	protected int 		  m_M_Product_ID;
	protected int 		  m_C_BPartner_ID;
	protected BigDecimal  m_Qty = Env.ONE;
	protected boolean	  m_isSOTrx = true;
	protected String      trxName   = null;
	
	protected int   	  m_M_PriceList_Version_ID = 0;
	protected int		  m_M_PriceList_ID = 0;
	protected Timestamp   m_PriceDate;	

	public AbstractProductPricing() {}
	
	@Override
	public void setInitialValues(int M_Product_ID, int C_BPartner_ID, BigDecimal qty, boolean isSOTrx, String trxName) {
		this.trxName=trxName;
		m_M_Product_ID = M_Product_ID;
		m_C_BPartner_ID = C_BPartner_ID;
		
		if (qty != null && Env.ZERO.compareTo(qty) != 0)
			m_Qty = qty;
		m_isSOTrx = isSOTrx;
	}

	@Override
	public int getM_Product_ID() {
		return m_M_Product_ID;
	}

	@Override
	public int getM_PriceList_ID() {
		return m_M_PriceList_ID;
	}

	@Override
	public void setM_PriceList_ID(int M_PriceList_ID) {
		m_M_PriceList_ID = M_PriceList_ID;
	}

	@Override
	public void setM_PriceList_Version_ID(int M_PriceList_Version_ID) {
		m_M_PriceList_Version_ID = M_PriceList_Version_ID;
	}
	
	@Override
	public void setQty(BigDecimal qty) {
		m_Qty = qty;
	}

	@Override
	public Timestamp getPriceDate() {
		return m_PriceDate;
	}

	@Override
	public void setPriceDate(Timestamp priceDate) {
		m_PriceDate = priceDate;
	}
	
	@Override
	public void setOrderLine(I_C_OrderLine orderLine, String trxName) {
		m_M_Product_ID = orderLine.getM_Product_ID();
		
		m_C_BPartner_ID = orderLine.getC_BPartner_ID();
		BigDecimal qty = orderLine.getQtyOrdered();
		if (qty != null && Env.ZERO.compareTo(qty) != 0)
			m_Qty = qty;
		this.trxName = trxName;
	}
	
	@Override
	public void setInvoiceLine(I_C_InvoiceLine invoiceLine, String trxName) {
		
	}
	
	@Override
	public void setProjectLine(I_C_ProjectLine projectLine, String trxName) {
		if (projectLine.getC_Project_ID() > 0) {
			I_C_Project project = projectLine.getC_Project();
			m_C_BPartner_ID = project.getC_BPartner_ID();
		}
		m_isSOTrx = true;
		this.trxName = trxName;
	}
	
	
}
