
package eone.base.acct;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.compiere.util.Env;

import eone.base.model.MBank;
import eone.base.model.MDocType;
import eone.base.model.MElementValue;
import eone.base.model.X_C_Cash;
import eone.base.model.X_C_DocType;


public class Doc_Bank extends Doc
{

	public Doc_Bank (ResultSet rs, String trxName)
	{
		super (MBank.class, rs, trxName);
	}	//	Doc_Bank

	
	protected String loadDocumentDetails ()
	{
		/*
		MBank bs = (MBank)getPO();
		setDateAcct(bs.getDateAcct());

		//	Contained Objects
		p_lines = loadLines(bs);
		if (log.isLoggable(Level.FINE)) log.fine("Lines=" + p_lines.length);
		*/
		return null;
	}   //  loadDocumentDetails


	/*
	protected DocLine[] loadLines(MBank bs)
	{
		ArrayList<DocLine> list = new ArrayList<DocLine>();
		MBankLine[] lines = bs.getLines(false);
		for (int i = 0; i < lines.length; i++)
		{
			MBankLine line = lines[i];
			if(line.isActive())
			{
				DocLine_Bank docLine = new DocLine_Bank(line, this);
				list.add(docLine);
			}
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
		MBank bank = (MBank)getPO();
		MElementValue dr = null;
		MElementValue cr = null;
		MElementValue tax = null;
		BigDecimal taxAmt = Env.ZERO;
		BigDecimal taxAmtConvert = Env.ZERO;
		
		if (X_C_Cash.INCLUDETAX_Included.equalsIgnoreCase(bank.getIncludeTax()) && bank.getTaxAmt().compareTo(Env.ZERO) != 0) {
			taxAmt = bank.getTaxAmt();
			taxAmtConvert = taxAmt.multiply(bank.getCurrencyRate());
			tax = MElementValue.get(getCtx(), bank.getAccount_Tax_ID());			
		}
		
		dr = MElementValue.get(getCtx(), bank.getAccount_Dr_ID());
		cr = MElementValue.get(getCtx(), bank.getAccount_Cr_ID());
		
		FactLine f = fact.createHeader(dr, cr, bank.getC_Currency_ID(), bank.getCurrencyRate(), bank.getAmount(), bank.getAmountConvert());
		f.setC_BPartner_Dr_ID(bank.getC_BPartner_Dr_ID());
		f.setC_BPartner_Cr_ID(bank.getC_BPartner_Cr_ID());
		f.setC_TypeCost_ID(bank.getC_TypeCost_ID());
		f.setC_TypeRevenue_ID(bank.getC_TypeRevenue_ID());
		f.setPA_ReportLine_ID(bank.getPA_ReportLine_ID());
		
		if (Env.DisContract)
		{
			f.setC_Contract_ID(bank.getC_Contract_ID());
			f.setC_ContractAnnex_ID(bank.getC_ContractAnnex_ID());
		}
		if(Env.DisContractLine)
			f.setC_ContractLine_ID(bank.getC_Contract_ID());
		
		if (Env.DisProject)
			f.setC_Project_ID(bank.getC_Project_ID());
		if (Env.DisProjectLine)
			f.setC_ProjectLine_ID(bank.getC_ProjectLine_ID());
		
		if (Env.DisConstruction)
			f.setC_Construction_ID(bank.getC_Construction_ID());
		if (Env.DisConstructionLine)
			f.setC_ConstructionLine_ID(bank.getC_ConstructionLine_ID());
		
		if (X_C_Cash.INCLUDETAX_Included.equalsIgnoreCase(bank.getIncludeTax()) && bank.getTaxAmt().compareTo(Env.ZERO) != 0) {
			MDocType dt =  MDocType.get(getCtx(), bank.getC_DocType_ID());
			if (X_C_DocType.DOCBASETYPE_111Credit.equalsIgnoreCase(dt.getDocBaseType())) {
				dr = tax;			
			}
			
			if (X_C_DocType.DOCBASETYPE_111Debit.equalsIgnoreCase(dt.getDocBaseType())) {
				cr = tax;
			}
			FactLine f1 = fact.createHeader(dr, cr, bank.getC_Currency_ID(), bank.getCurrencyRate(), taxAmt, taxAmtConvert);
			f1.setC_BPartner_Dr_ID(bank.getC_BPartner_Dr_ID());
			f1.setC_BPartner_Cr_ID(bank.getC_BPartner_Cr_ID());
			f1.setC_TypeCost_ID(bank.getC_TypeCost_ID());
			f1.setC_TypeRevenue_ID(bank.getC_TypeRevenue_ID());
			f1.setDateInvoiced(bank.getDateInvoiced());
			f1.setInvoiceNo(bank.getInvoiceNo());
			if (Env.DisContract)
			{
				f1.setC_Contract_ID(bank.getC_Contract_ID());
				f1.setC_ContractAnnex_ID(bank.getC_ContractAnnex_ID());
			}
			if(Env.DisContractLine)
				f1.setC_ContractLine_ID(bank.getC_Contract_ID());
			
			if (Env.DisProject)
				f1.setC_Project_ID(bank.getC_Project_ID());
			if (Env.DisProjectLine)
				f1.setC_ProjectLine_ID(bank.getC_ProjectLine_ID());
			
			if (Env.DisConstruction)
				f1.setC_Construction_ID(bank.getC_Construction_ID());
			if (Env.DisConstructionLine)
				f1.setC_ConstructionLine_ID(bank.getC_ConstructionLine_ID());
		}
		ArrayList<Fact> facts = new ArrayList<Fact>();
		facts.add(fact);
		return facts;
	} 
	
}   //  Doc_Bank
