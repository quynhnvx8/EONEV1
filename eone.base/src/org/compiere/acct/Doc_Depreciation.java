
package org.compiere.acct;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.logging.Level;

import org.compiere.model.MDepreciation;
import org.compiere.model.MDepreciationExp;
import org.compiere.model.MElementValue;
import org.compiere.util.Env;

public class Doc_Depreciation extends Doc
{

	public Doc_Depreciation (ResultSet rs, String trxName)
	{
		super (MDepreciation.class, rs, trxName);
	}   //  DocInOut



	protected String loadDocumentDetails()
	{
		MDepreciation inout = (MDepreciation)getPO();
		p_lines = loadLines(inout);
		if (log.isLoggable(Level.FINE)) log.fine("Lines=" + p_lines.length);
		return null;
	}   //  loadDocumentDetails


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

	
	
	public ArrayList<Fact> createFacts ()
	{
		Fact fact = new Fact(this, Fact.POST_Actual);
		MElementValue dr = null;
		MElementValue cr = null;
		int C_Currency_ID = Integer.parseInt(Env.getContext(getCtx(), "#C_CurrencyDefault_ID"));
		for (int i = 0; i < p_lines.length; i++)
		{
			DocLine line = p_lines[i];
			MDepreciationExp lineExp = (MDepreciationExp) line.getPO();
			dr = MElementValue.get(getCtx(), lineExp.getAccount_Dr_ID()); 
			cr = MElementValue.get(getCtx(), lineExp.getAccount_Cr_ID());
			
			if (dr == null || dr.getC_ElementValue_ID() == 0) {
				log.saveError("Error", "Account Dr is null");
				return null;
			}
			
			if (cr == null || cr.getC_ElementValue_ID() == 0) {
				log.saveError("Error", "Account Cr is null");
				return null;
			}
			
			FactLine f = fact.createLine(line, dr, cr, C_Currency_ID, Env.ONE, lineExp.getAmount(), lineExp.getAmount());
			f.setC_TypeCost_ID(lineExp.getC_TypeCost_ID());
			f.setA_Asset_ID(lineExp.getA_Asset_ID());
			
		}
		
		ArrayList<Fact> facts = new ArrayList<Fact>();
		facts.add(fact);
		return facts;
	}   //  createFact

	
}   //  Doc_InOut
