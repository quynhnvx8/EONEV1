
package eone.base.acct;

import java.sql.ResultSet;
import java.util.ArrayList;

import eone.base.model.MDocType;
import eone.base.model.MElementValue;
import eone.base.model.MInvoice;
import eone.base.model.X_C_DocType;

public class Doc_Invoice extends Doc
{

	public Doc_Invoice(ResultSet rs, String trxName)
	{
		super (MInvoice.class, rs, trxName);
	}	//	Doc_Invoice

	/** Contained Optional Tax Lines    */
	protected DocTax[]        m_taxes = null;
	/** Currency Precision				*/
	protected int				m_precision = -1;
	/** All lines are Service			*/
	protected boolean			m_allLinesService = true;
	/** All lines are product item		*/
	protected boolean			m_allLinesItem = true;


	protected String loadDocumentDetails ()
	{
		/*
		MInvoice invoice = (MInvoice)getPO();
		//	Amounts
		//setAmount(Doc.AMTTYPE_Gross, invoice.getGrandTotal());
		setAmount(Doc.AMTTYPE_Net, invoice.getTotalLines());

		//	Contained Objects
		m_taxes = loadTaxes();
		p_lines = loadLines(invoice);
		if (log.isLoggable(Level.FINE)) log.fine("Lines=" + p_lines.length + ", Taxes=" + m_taxes.length);
		*/
		return null;
	}   //  loadDocumentDetails

	
	/*
	private DocLine[] loadLines (MInvoice invoice)
	{
		ArrayList<DocLine> list = new ArrayList<DocLine>();
		//
		MInvoiceLine[] lines = invoice.getLines(false);
		for (int i = 0; i < lines.length; i++)
		{
			MInvoiceLine line = lines[i];
			if (line.isDescription())
				continue;
			DocLine docLine = new DocLine(line, this);
			//	Qty
			BigDecimal LineNetAmt = line.getLineNetAmt();
			BigDecimal PriceList = line.getPriceList();
			int C_Tax_ID = docLine.getC_Tax_ID();
			//	Correct included Tax
			if (isTaxIncluded() && C_Tax_ID != 0)
			{
				MTax tax = MTax.get(getCtx(), C_Tax_ID);
				if (!tax.isZeroTax())
				{
					BigDecimal LineNetAmtTax = tax.calculateTax(LineNetAmt, true, getStdPrecision());
					if (log.isLoggable(Level.FINE)) log.fine("LineNetAmt=" + LineNetAmt + " - Tax=" + LineNetAmtTax);
					LineNetAmt = LineNetAmt.subtract(LineNetAmtTax);

					
					BigDecimal PriceListTax = tax.calculateTax(PriceList, true, getStdPrecision());
					PriceList = PriceList.subtract(PriceListTax);
				}
			}	//	correct included Tax

			docLine.setAmount (LineNetAmt);	//	qty for discount calc
			
			if (log.isLoggable(Level.FINE)) log.fine(docLine.toString());
			list.add(docLine);
		}

		//	Convert to Array
		DocLine[] dls = new DocLine[list.size()];
		list.toArray(dls);

	
		//	Return Array
		return dls;
	}	//	loadLines
	*/
	
	public ArrayList<Fact> createFacts ()
	{
		Fact fact = new Fact(this, Fact.POST_Actual);
		MInvoice invoice = (MInvoice)getPO();
		MElementValue dr = null;
		MElementValue cr = null;
		MElementValue tax = null;
		
		MDocType dt =  MDocType.get(getCtx(), invoice.getC_DocType_ID());
		
		
		dr = MElementValue.get(getCtx(), invoice.getAccount_Dr_ID());
		cr = MElementValue.get(getCtx(), invoice.getAccount_Cr_ID());
		tax = MElementValue.get(getCtx(), invoice.getAccount_Tax_ID());
		
		//Tien truoc thue
		FactLine f = fact.createHeader(dr, cr, invoice.getC_Currency_ID(), invoice.getCurrencyRate(), invoice.getAmount(), invoice.getAmountConvert());
		f.setC_BPartner_Dr_ID(invoice.getC_BPartner_Dr_ID());
		f.setC_BPartner_Cr_ID(invoice.getC_BPartner_Cr_ID());
		f.setC_TypeCost_ID(invoice.getC_TypeCost_ID());
		f.setC_TypeRevenue_ID(invoice.getC_TypeRevenue_ID());
		
		//Tien thue
		if (X_C_DocType.DOCBASETYPE_133Input.equalsIgnoreCase(dt.getDocBaseType())) {
			dr = tax;			
		}
		
		if (X_C_DocType.DOCBASETYPE_333Onput.equalsIgnoreCase(dt.getDocBaseType())) {
			cr = tax;
		}
		
		FactLine f1 = fact.createHeader(dr, cr, invoice.getC_Currency_ID(), invoice.getCurrencyRate(), invoice.getTaxAmt(), invoice.getTaxAmt());
		f1.setC_BPartner_Dr_ID(invoice.getC_BPartner_Dr_ID());
		f1.setC_BPartner_Cr_ID(invoice.getC_BPartner_Cr_ID());
		f1.setC_TypeCost_ID(invoice.getC_TypeCost_ID());
		f1.setC_TypeRevenue_ID(invoice.getC_TypeRevenue_ID());
		f1.setC_Tax_ID(invoice.getC_Tax_ID());
		//
		ArrayList<Fact> facts = new ArrayList<Fact>();
		facts.add(fact);
		return facts;
	}   //  createFact

		
}   //  Doc_Invoice
