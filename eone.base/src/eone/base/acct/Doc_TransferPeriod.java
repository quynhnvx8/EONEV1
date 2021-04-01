
package eone.base.acct;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.logging.Level;

import eone.base.model.MElementValue;
import eone.base.model.MTransferPeriod;
import eone.base.model.MTransferPeriodLine;

public class Doc_TransferPeriod extends Doc
{

	public Doc_TransferPeriod (ResultSet rs, String trxName)
	{
		super (MTransferPeriod.class, rs, trxName);
	}   //  DocInOut



	protected String loadDocumentDetails()
	{
		MTransferPeriod inout = (MTransferPeriod)getPO();
		p_lines = loadLines(inout);
		if (log.isLoggable(Level.FINE)) log.fine("Lines=" + p_lines.length);

		
		
		return null;
	}   //  loadDocumentDetails


	private DocLine[] loadLines(MTransferPeriod inout)
	{
		ArrayList<DocLine> list = new ArrayList<DocLine>();
		MTransferPeriodLine[] lines = inout.getLines(false);
		for (int i = 0; i < lines.length; i++)
		{
			MTransferPeriodLine line = lines[i];
			

			DocLine docLine = new DocLine (line, this);
			
			
			list.add (docLine);
		}

		//	Return Array
		DocLine[] dls = new DocLine[list.size()];
		list.toArray(dls);
		return dls;
	}	//	loadLines

	
	public ArrayList<Fact> createFacts ()
	{
		Fact fact = new Fact(this, Fact.POST_Actual);
		MTransferPeriod transfer = (MTransferPeriod)getPO();
		MElementValue dr = null;
		MElementValue cr = null;
		
		
		for (int i = 0; i < p_lines.length; i++)
		{
			DocLine line = p_lines[i];
			MTransferPeriodLine transferLine = (MTransferPeriodLine) line.getPO();
			dr = MElementValue.get(getCtx(), transferLine.getAccount_Dr_ID());
			cr = MElementValue.get(getCtx(), transferLine.getAccount_Cr_ID());
			FactLine f = fact.createLine(line, dr, cr, transfer.getC_Currency_ID(), transfer.getCurrencyRate(), transferLine.getAmount(), transferLine.getAmountConvert());
			f.setM_Product_ID(transferLine.getM_Product_ID());
		}
		ArrayList<Fact> facts = new ArrayList<Fact>();
		facts.add(fact);
		return facts;
	}   //  createFact

	
}   //  Doc_InOut
