
package org.compiere.acct;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;

import org.compiere.model.MElementValue;
import org.compiere.model.MGeneral;
import org.compiere.util.Env;


public class Doc_General extends Doc
{
	
	public Doc_General (ResultSet rs, String trxName)
	{
		super(MGeneral.class, rs, trxName);
	}	

	
	protected String loadDocumentDetails ()
	{
		//MCash cash = (MCash)getPO();

		//headLines = loadLines(cash);
		//if (log.isLoggable(Level.FINE)) log.fine("Lines=" + p_lines.length);
		return null;
	}  

	
	/**
	 *	Load Cash Line
	 *	@param cash journal
	 *	@param cb cash book
	 *  @return DocLine Array
	 */
	/*
	private DocLine[] loadLines(MCash cash)
	{
		ArrayList<DocLine> list = new ArrayList<DocLine>();
		MCashLine[] lines = cash.getLines(false);
		for (int i = 0; i < lines.length; i++)
		{
			MCashLine line = lines[i];
			DocLine_Cash docLine = new DocLine_Cash (line, this);
			//
			list.add(docLine);
		}

		//	Return Array
		DocLine[] dls = new DocLine[list.size()];
		list.toArray(dls);
		return dls;
	}	//	loadLines

	*/
	
	
	public ArrayList<Fact> createFacts ()
	{
		
		Fact fact = new Fact(this, Fact.POST_Actual);
		MGeneral general = (MGeneral)getPO();
		MElementValue dr = null;
		MElementValue cr = null;
		
		int C_Currency_ID = Env.getContextAsInt(getCtx(), "#C_CurrencyDefault_ID");
		Timestamp DateAcct = general.getDateAcct();
		BigDecimal rate = Env.getRateByCurrency(C_Currency_ID, DateAcct);
		
		dr = MElementValue.get(getCtx(), general.getAccount_Dr_ID());
		cr = MElementValue.get(getCtx(), general.getAccount_Cr_ID());
		FactLine f = fact.createHeader(dr, cr, C_Currency_ID, rate, general.getAmount(), general.getAmountConvert());
		
		f.setC_TypeCost_ID(general.getC_TypeCost_ID());
		f.setC_TypeRevenue_ID(general.getC_TypeRevenue_ID());
		
		if (Env.DisProduct) {
			f.setM_Product_ID(general.getM_Product_ID());
			f.setQty(general.getQty());
			f.setPrice(general.getPrice());
			
			f.setM_Warehouse_Dr_ID(general.getM_Warehouse_Dr_ID());
			f.setM_Warehouse_Cr_ID(general.getM_Warehouse_Cr_ID());
			
		}
		
		f.setC_BPartner_Cr_ID(general.getC_BPartner_Cr_ID());
		f.setC_BPartner_Dr_ID(general.getC_BPartner_Dr_ID());
		f.setC_BPartnerInfo_Cr_ID(general.getC_BPartnerInfo_Cr_ID());
		f.setC_BPartnerInfo_Dr_ID(general.getC_BPartnerInfo_Dr_ID());
		
		
		if (Env.DisContract)
		{
			f.setC_Contract_ID(general.getC_Contract_ID());
			f.setC_ContractAnnex_ID(general.getC_ContractAnnex_ID());
		}
		if(Env.DisContractLine)
			f.setC_ContractLine_ID(general.getC_Contract_ID());
		
		if (Env.DisProject)
			f.setC_Project_ID(general.getC_Project_ID());
		if (Env.DisProjectLine)
			f.setC_ProjectLine_ID(general.getC_ProjectLine_ID());
		
		if (Env.DisConstruction)
			f.setC_Construction_ID(general.getC_Construction_ID());
		if (Env.DisConstructionLine)
			f.setC_ConstructionLine_ID(general.getC_ConstructionLine_ID());
		
		ArrayList<Fact> facts = new ArrayList<Fact>();
		facts.add(fact);
		return facts;
	}   //  createFact

	
}   
