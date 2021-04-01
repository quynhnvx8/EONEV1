
package eone.base.acct;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.logging.Level;

import org.compiere.util.Env;

import eone.base.model.MDocType;
import eone.base.model.MElementValue;
import eone.base.model.MInOut;
import eone.base.model.MInOutLine;
import eone.base.model.X_C_DocType;
import eone.base.model.X_M_InOut;

public class Doc_InOut extends Doc
{

	public Doc_InOut (ResultSet rs, String trxName)
	{
		super (MInOut.class, rs, trxName);
	}   //  DocInOut



	protected String loadDocumentDetails()
	{
		MInOut inout = (MInOut)getPO();
		p_lines = loadLines(inout);
		if (log.isLoggable(Level.FINE)) log.fine("Lines=" + p_lines.length);
		return null;
	}   //  loadDocumentDetails


	private DocLine[] loadLines(MInOut inout)
	{
		ArrayList<DocLine> list = new ArrayList<DocLine>();
		MInOutLine[] lines = inout.getLines(false);
		for (int i = 0; i < lines.length; i++)
		{
			MInOutLine line = lines[i];
			if (line.getM_Product_ID() == 0)
			{
				if (log.isLoggable(Level.FINER)) log.finer("Ignored: " + line);
				continue;
			}

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
		MInOut inout = (MInOut)getPO();
		MElementValue dr = null;
		MElementValue cr = null;
		MElementValue cogs = null;
		MElementValue revenue = null;
		
		MDocType dt = MDocType.get(getCtx(), inout.getC_DocType_ID());
		
		dr = MElementValue.get(getCtx(), inout.getAccount_Dr_ID());
		cr = MElementValue.get(getCtx(), inout.getAccount_Cr_ID());
		if (MDocType.DOCTYPE_Output.equals(dt.getDocType()))
		{
			if (inout.getAccount_COGS_ID() > 0) 
				cogs = MElementValue.get(getCtx(), inout.getAccount_COGS_ID());
			if (inout.getAccount_REV_ID() > 0)
				revenue = MElementValue.get(getCtx(), inout.getAccount_REV_ID());
			for (int i = 0; i < p_lines.length; i++)
			{
				DocLine line = p_lines[i];
				MInOutLine inoutLine = (MInOutLine) line.getPO();
				//COGS
				FactLine f = fact.createLine(line, cogs, cr, inout.getC_Currency_ID(), inout.getCurrencyRate(), inoutLine.getAmount(), inoutLine.getAmount());
				f.setM_Product_ID(inoutLine.getM_Product_ID());
				f.setM_Product_Cr_ID(inoutLine.getM_Product_ID());
				f.setQty(inoutLine.getQty());
				f.setM_Warehouse_Dr_ID(inout.getM_Warehouse_Dr_ID());
				f.setM_Warehouse_Cr_ID(inout.getM_Warehouse_Cr_ID());
				f.setC_BPartner_Cr_ID(inout.getC_BPartner_Cr_ID());
				f.setC_BPartner_Dr_ID(inout.getC_BPartner_Dr_ID());			
				f.setPrice(inoutLine.getPrice());
				
				//Revenue
				BigDecimal amtRev = inoutLine.getAmount().subtract(inoutLine.getDiscountAmt());
				if (amtRev.compareTo(Env.ZERO) != 0) {
					f = fact.createLine(line, dr, revenue, inout.getC_Currency_ID(), inout.getCurrencyRate(), amtRev, amtRev);
					f.setC_BPartner_Cr_ID(inout.getC_BPartner_Cr_ID());
					f.setC_BPartner_Dr_ID(inout.getC_BPartner_Dr_ID());			
					
				}
				
				
				//Neu co thue
				if (inout.getC_Tax_ID() > 0 && X_M_InOut.INCLUDETAXTAB_TAXS.equalsIgnoreCase(inout.getIncludeTaxTab())) {
					if (X_C_DocType.DOCBASETYPE_152New.equalsIgnoreCase(dt.getDocBaseType()) 
							|| X_C_DocType.DOCBASETYPE_153New.equalsIgnoreCase(dt.getDocBaseType())
							|| X_C_DocType.DOCBASETYPE_155New.equalsIgnoreCase(dt.getDocBaseType())
							|| X_C_DocType.DOCBASETYPE_156New.equalsIgnoreCase(dt.getDocBaseType())) {
						dr =  MElementValue.get(getCtx(), inout.getAccount_Tax_ID());
					} else {
						cr =  MElementValue.get(getCtx(), inout.getAccount_Tax_ID());
					}
					
					FactLine f1 = fact.createLine(line, dr, cr, inout.getC_Currency_ID(), inout.getCurrencyRate(), inoutLine.getTaxAmt(), inoutLine.getTaxAmt());
					f1.setC_BPartner_Cr_ID(inout.getC_BPartner_Cr_ID());
					f1.setC_BPartner_Dr_ID(inout.getC_BPartner_Dr_ID());
					f1.setC_Tax_ID(inout.getC_Tax_ID());
					f1.setInvoiceNo(inout.getInvoiceNo());
					f1.setDateInvoiced(inout.getDateInvoiced());
				}
			}//end for
		} else {
			for (int i = 0; i < p_lines.length; i++)
			{
				DocLine line = p_lines[i];
				MInOutLine inoutLine = (MInOutLine) line.getPO();
				FactLine f = fact.createLine(line, dr, cr, inout.getC_Currency_ID(), inout.getCurrencyRate(), inoutLine.getAmount(), inoutLine.getAmount());
				f.setM_Product_ID(inoutLine.getM_Product_ID());
				f.setM_Product_Cr_ID(inoutLine.getM_Product_ID());
				f.setQty(inoutLine.getQty());
				f.setM_Warehouse_Dr_ID(inout.getM_Warehouse_Dr_ID());
				f.setM_Warehouse_Cr_ID(inout.getM_Warehouse_Cr_ID());
				f.setC_BPartner_Cr_ID(inout.getC_BPartner_Cr_ID());
				f.setC_BPartner_Dr_ID(inout.getC_BPartner_Dr_ID());			
				f.setPrice(inoutLine.getPrice());
				
				
				//Neu co thue
				if (inout.getC_Tax_ID() > 0 && X_M_InOut.INCLUDETAXTAB_TAXS.equalsIgnoreCase(inout.getIncludeTaxTab())) {
					if (X_C_DocType.DOCBASETYPE_152New.equalsIgnoreCase(dt.getDocBaseType()) 
							|| X_C_DocType.DOCBASETYPE_153New.equalsIgnoreCase(dt.getDocBaseType())
							|| X_C_DocType.DOCBASETYPE_155New.equalsIgnoreCase(dt.getDocBaseType())
							|| X_C_DocType.DOCBASETYPE_156New.equalsIgnoreCase(dt.getDocBaseType())) {
						dr =  MElementValue.get(getCtx(), inout.getAccount_Tax_ID());
					} else {
						cr =  MElementValue.get(getCtx(), inout.getAccount_Tax_ID());
					}
					
					FactLine f1 = fact.createLine(line, dr, cr, inout.getC_Currency_ID(), inout.getCurrencyRate(), inoutLine.getTaxAmt(), inoutLine.getTaxAmt());
					f1.setC_BPartner_Cr_ID(inout.getC_BPartner_Cr_ID());
					f1.setC_BPartner_Dr_ID(inout.getC_BPartner_Dr_ID());
					f1.setC_Tax_ID(inout.getC_Tax_ID());
					f1.setInvoiceNo(inout.getInvoiceNo());
					f1.setDateInvoiced(inout.getDateInvoiced());
				}
			}//end for
		}//end nhap
		
		
		
		ArrayList<Fact> facts = new ArrayList<Fact>();
		facts.add(fact);
		return facts;
	}   //  createFact

	
}   //  Doc_InOut
