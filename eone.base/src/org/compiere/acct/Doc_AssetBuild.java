
package org.compiere.acct;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.logging.Level;

import org.compiere.model.MAssetBuild;
import org.compiere.model.MDocType;
import org.compiere.model.MElementValue;
import org.compiere.model.X_C_DocType;
import org.compiere.util.Env;


public class Doc_AssetBuild extends Doc
{
	
	public Doc_AssetBuild (ResultSet rs, String trxName)
	{
		super(MAssetBuild.class, rs, trxName);
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
	private DocLine[] loadLines(MDepreciation exp)
	{
		ArrayList<DocLine> list = new ArrayList<DocLine>();
		MDepreciationExp[] lines = exp.getLines(false);
		for (int i = 0; i < lines.length; i++)
		{
			MDepreciationExp line = lines[i];
			DocLine docLine = new DocLine (line, this);
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
		int C_Currency_ID = Env.getContextAsInt(getCtx(), "#C_CurrencyDefault_ID");
		MAssetBuild assetBuild = (MAssetBuild)getPO();
		MDocType dt = MDocType.get(getCtx(), assetBuild.getC_DocType_ID());
		if (X_C_DocType.DOCBASETYPE_211AddNew.equals(dt.getDocBaseType())) {
			if (!postAddNew(fact, assetBuild, C_Currency_ID, dt)) {
				return null;
			}			
		}
		
		if (X_C_DocType.DOCBASETYPE_242New.equals(dt.getDocBaseType())) {
			if (!postAddNew(fact, assetBuild, C_Currency_ID, dt)) {
				return null;
			}			
		}
		
		if (X_C_DocType.DOCBASETYPE_211Openbalance.equals(dt.getDocBaseType())) {
			if (!postOpenBalance(fact, assetBuild, C_Currency_ID, dt)) {
				return null;
			}			
		}
		
		if (X_C_DocType.DOCBASETYPE_242Openbalance.equals(dt.getDocBaseType())) {
			if (!postOpenBalance(fact, assetBuild, C_Currency_ID, dt)) {
				return null;
			}			
		}
		
		if (X_C_DocType.DOCBASETYPE_153Expense.equals(dt.getDocBaseType())
				|| X_C_DocType.DOCBASETYPE_153ONE.equalsIgnoreCase(dt.getDocBaseType())
		) {
			if (!postAddNew(fact, assetBuild, C_Currency_ID, dt)) {
				return null;
			}			
		}
		
		ArrayList<Fact> facts = new ArrayList<Fact>();
		facts.add(fact);
		return facts;
	}   //  createFact

	//Dung cho ca tai san, chi phi tra truoc.
	private boolean postAddNew(Fact fact, MAssetBuild assetBuild, int C_Currency_ID, MDocType dt) {

		Timestamp DateAcct = assetBuild.getDateAcct();
		BigDecimal rate = Env.getRateByCurrency(C_Currency_ID, DateAcct);
		
		MElementValue dr = null;
		MElementValue cr = null;
		dr = MElementValue.get(getCtx(), assetBuild.getAccount_Dr_ID());
		cr = MElementValue.get(getCtx(), assetBuild.getAccount_Cr_ID());
		
		FactLine f = fact.createHeader(dr, cr, C_Currency_ID, rate, assetBuild.getAmount(), assetBuild.getAmount());
		if (f == null) {
			p_Error = "Not Create Fact";
			log.log(Level.SEVERE, p_Error);
			return false;
		}
		f.setC_TypeCost_ID(assetBuild.getC_TypeCost_ID());
		f.setC_TypeRevenue_ID(assetBuild.getC_TypeRevenue_ID());
		f.setA_Asset_ID(assetBuild.getA_Asset_ID());
		if (assetBuild.getM_Product_ID() > 0)
			f.setM_Product_ID(assetBuild.getM_Product_ID());
		
		if (assetBuild.getTaxAmt().compareTo(Env.ZERO) != 0) {
			dr = MElementValue.get(getCtx(), assetBuild.getAccount_Tax_ID());
			FactLine f1 = fact.createHeader(dr, cr, C_Currency_ID, rate, assetBuild.getTaxAmt(), assetBuild.getTaxAmt());
			f1.setC_TypeCost_ID(assetBuild.getC_TypeCost_ID());
			f1.setC_TypeRevenue_ID(assetBuild.getC_TypeRevenue_ID());
			f1.setInvoiceNo(assetBuild.getInvoiceNo());
			f1.setDateInvoiced(assetBuild.getDateInvoiced());
		}
		return true;
	}
	
	private boolean postOpenBalance(Fact fact, MAssetBuild assetBuild, int C_Currency_ID, MDocType dt) {

		Timestamp DateAcct = assetBuild.getDateAcct();
		BigDecimal rate = Env.getRateByCurrency(C_Currency_ID, DateAcct);
		
		MElementValue dr = null;
		MElementValue cr = null;
		dr = MElementValue.get(getCtx(), assetBuild.getAccount_Dr_ID());
		cr = MElementValue.get(getCtx(), assetBuild.getAccount_Cr_ID());
		BigDecimal amount = assetBuild.getAmount();
		if (X_C_DocType.DOCBASETYPE_242Openbalance.equals(dt.getDocBaseType())) {
			amount = assetBuild.getAmount().subtract(assetBuild.getAccumulateAmt());
		}
		
		FactLine f = fact.createHeader(dr, null, C_Currency_ID, rate, amount, amount);
		if (f == null) {
			p_Error = "Not Create Fact";
			log.log(Level.SEVERE, p_Error);
			return false;
		}
		f.setA_Asset_ID(assetBuild.getA_Asset_ID());
		
		if (X_C_DocType.DOCBASETYPE_211Openbalance.equals(dt.getDocBaseType())) {
			FactLine f1 = fact.createHeader(null, cr, C_Currency_ID, rate, assetBuild.getAccumulateAmt(), assetBuild.getAccumulateAmt());
			if (f1 == null) {
				p_Error = "Not Create Fact";
				log.log(Level.SEVERE, p_Error);
				return false;
			}
			f1.setA_Asset_ID(assetBuild.getA_Asset_ID());
		}
		return true;
	}
}   
