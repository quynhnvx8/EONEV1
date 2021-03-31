
package org.compiere.acct;

import java.sql.ResultSet;
import java.util.ArrayList;

import org.compiere.model.MAssetChange;
import org.compiere.model.MAssetChangeLine;
import org.compiere.model.MElementValue;
import org.compiere.util.Env;


public class Doc_AssetChange extends Doc
{
	
	public Doc_AssetChange (ResultSet rs, String trxName)
	{
		super(MAssetChange.class, rs, trxName);
	}	

	
	protected String loadDocumentDetails ()
	{
		MAssetChange header = (MAssetChange)getPO();

		p_lines = loadLines(header);
		//if (log.isLoggable(Level.FINE)) log.fine("Lines=" + p_lines.length);
		return null;
	}  

	

	private DocLine[] loadLines(MAssetChange exp)
	{
		ArrayList<DocLine> list = new ArrayList<DocLine>();
		MAssetChangeLine[] lines = exp.getLines(false);
		for (int i = 0; i < lines.length; i++)
		{
			MAssetChangeLine line = lines[i];
			DocLine docLine = new DocLine (line, this);
			//
			list.add(docLine);
		}

		//	Return Array
		DocLine[] dls = new DocLine[list.size()];
		list.toArray(dls);
		return dls;
	}	//	loadLines

	
	
	
	public ArrayList<Fact> createFacts ()
	{
		
		Fact fact = new Fact(this, Fact.POST_Actual);
		int C_Currency_ID = Env.getContextAsInt(getCtx(), "#C_CurrencyDefault_ID");
		MElementValue dr = null;
		MElementValue cr = null;
		for(int i = 0; i < p_lines.length; i++) {
			DocLine docLine = p_lines[i];
			MAssetChangeLine line = (MAssetChangeLine)docLine.getPO();
			dr = MElementValue.get(getCtx(), line.getAccount_Dr_ID()); 
			cr = MElementValue.get(getCtx(), line.getAccount_Cr_ID());
			
			if (dr == null || dr.getC_ElementValue_ID() == 0) {
				log.saveError("Error", "Account Dr is null");
				return null;
			}
			
			if (cr == null || cr.getC_ElementValue_ID() == 0) {
				log.saveError("Error", "Account Cr is null");
				return null;
			}
			
			FactLine f = fact.createLine(docLine, dr, cr, C_Currency_ID, Env.ONE, line.getAmount(), line.getAmount());
			f.setA_Asset_ID(line.getA_Asset_ID());
			f.setDescription(line.getDescription());
			if (line.getTaxAmt().compareTo(Env.ZERO) != 0) {
				dr = MElementValue.get(getCtx(), line.getAccount_Tax_ID());
				FactLine f1 = fact.createLine(docLine, dr, cr, C_Currency_ID, Env.ONE, line.getTaxAmt(), line.getTaxAmt());
				f1.setDescription(line.getDescription());
			}
		}
		
		ArrayList<Fact> facts = new ArrayList<Fact>();
		facts.add(fact);
		return facts;
	}   //  createFact

	
}   
