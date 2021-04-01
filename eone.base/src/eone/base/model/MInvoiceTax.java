
package eone.base.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.Env;


public class MInvoiceTax extends X_C_InvoiceTax
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5560880305482497098L;


	public static MInvoiceTax get (MInvoiceLine line, int precision, 
		boolean oldTax, String trxName)
	{
		MInvoiceTax retValue = null;
		if (line == null || line.getC_Invoice_ID() == 0)
			return null;
		
		return retValue;
	}	//	get
	
	public MInvoiceTax (Properties ctx, int ignored, String trxName)
	{
		super(ctx, 0, trxName);
		if (ignored != 0)
			throw new IllegalArgumentException("Multi-Key");
		setTaxAmt (Env.ZERO);
		setTaxBaseAmt (Env.ZERO);
	}	//	MInvoiceTax


	public MInvoiceTax (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MInvoiceTax
	
	/** Tax							*/
	private MTax 		m_tax = null;

	
	
	
	protected MTax getTax()
	{
		if (m_tax == null)
			m_tax = MTax.get(getCtx(), getC_Tax_ID());
		return m_tax;
	}	//	getTax
	
	
	public String toString ()
	{
		StringBuilder sb = new StringBuilder ("MInvoiceTax[");
		sb.append("C_Invoice_ID=").append(getC_Invoice_ID())
			.append(",C_Tax_ID=").append(getC_Tax_ID())
			.append(", Base=").append(getTaxBaseAmt()).append(",Tax=").append(getTaxAmt())
			.append ("]");
		return sb.toString ();
	}	//	toString

}	//	MInvoiceTax
