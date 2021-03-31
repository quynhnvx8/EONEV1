
package org.compiere.model;

import java.sql.ResultSet;
import java.util.Properties;


public class MFactAcct extends X_Fact_Acct
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5251847162314796574L;

	
	public MFactAcct (Properties ctx, int Fact_Acct_ID, String trxName)
	{
		super (ctx, Fact_Acct_ID, trxName);
	}	//	MFactAcct

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName transaction
	 */
	public MFactAcct (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MFactAcct

	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuilder sb = new StringBuilder ("MFactAcct[");
		sb.append(get_ID()).append("-Acct=").append(getAccount_Dr_ID()).append("|").append(getAccount_Cr_ID())
			.append(",Dr=").append(getAmount()).append("|").append(getAmountConvert())
			.append(",Cr=").append(getAmount()).append("|").append(getAmountConvert())
			.append ("]");
		return sb.toString ();
	}	//	toString

	/**
	 * 	Derive MAccount from record
	 *	@return Valid Account Combination
	 */
	public MElementValue getMAccountDr()
	{
		return  MElementValue.get (getCtx(), getAccount_Dr_ID());
	}	//	getMAccount

	public MElementValue getMAccountCr()
	{
		return  MElementValue.get (getCtx(), getAccount_Cr_ID());
	}
}	//	MFactAcct
