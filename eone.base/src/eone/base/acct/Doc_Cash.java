/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2006 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package eone.base.acct;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.compiere.util.Env;

import eone.base.model.MCash;
import eone.base.model.MDocType;
import eone.base.model.MElementValue;
import eone.base.model.X_C_Cash;
import eone.base.model.X_C_DocType;

/**
 *  Post Invoice Documents.
 *  <pre>
 *  Table:              C_Cash (407)
 *  Document Types:     CMC
 *  </pre>
 *  @author Jorg Janke
 *  @version  $Id: Doc_Cash.java,v 1.3 2006/07/30 00:53:33 jjanke Exp $
 */
public class Doc_Cash extends Doc
{
	/**
	 *  Constructor
	 * 	@param as accounting schema
	 * 	@param rs record
	 * 	@param trxName trx
	 */
	public Doc_Cash (ResultSet rs, String trxName)
	{
		super(MCash.class, rs, trxName);
	}	//	Doc_Cash

	/**
	 *  Load Specific Document Details
	 *  @return error message or null
	 */
	protected String loadDocumentDetails ()
	{
		//MCash cash = (MCash)getPO();

		//headLines = loadLines(cash);
		//if (log.isLoggable(Level.FINE)) log.fine("Lines=" + p_lines.length);
		return null;
	}   //  loadDocumentDetails

	
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
		MCash cash = (MCash)getPO();
		MElementValue dr = null;
		MElementValue cr = null;
		MElementValue tax = null;
		BigDecimal taxAmt = Env.ZERO;
		BigDecimal taxAmtConvert = Env.ZERO;
		
		if (X_C_Cash.INCLUDETAX_Included.equalsIgnoreCase(cash.getIncludeTax()) && cash.getTaxAmt().compareTo(Env.ZERO) != 0) {
			taxAmt = cash.getTaxAmt();
			taxAmtConvert = taxAmt.multiply(cash.getCurrencyRate());
			tax = MElementValue.get(getCtx(), cash.getAccount_Tax_ID());			
		}
		
		dr = MElementValue.get(getCtx(), cash.getAccount_Dr_ID());
		cr = MElementValue.get(getCtx(), cash.getAccount_Cr_ID());
		
		FactLine f = fact.createHeader(dr, cr, cash.getC_Currency_ID(), cash.getCurrencyRate(), cash.getAmount(), cash.getAmountConvert());
		f.setC_BPartner_Dr_ID(cash.getC_BPartner_Dr_ID());
		f.setC_BPartner_Cr_ID(cash.getC_BPartner_Cr_ID());
		f.setC_TypeCost_ID(cash.getC_TypeCost_ID());
		f.setC_TypeRevenue_ID(cash.getC_TypeRevenue_ID());
		f.setPA_ReportLine_ID(cash.getPA_ReportLine_ID());
		
		if (Env.DisContract)
		{
			f.setC_Contract_ID(cash.getC_Contract_ID());
			f.setC_ContractAnnex_ID(cash.getC_ContractAnnex_ID());
		}
		if(Env.DisContractLine)
			f.setC_ContractLine_ID(cash.getC_Contract_ID());
		
		if (Env.DisProject)
			f.setC_Project_ID(cash.getC_Project_ID());
		if (Env.DisProjectLine)
			f.setC_ProjectLine_ID(cash.getC_ProjectLine_ID());
		
		if (Env.DisConstruction)
			f.setC_Construction_ID(cash.getC_Construction_ID());
		if (Env.DisConstructionLine)
			f.setC_ConstructionLine_ID(cash.getC_ConstructionLine_ID());
		
		if (X_C_Cash.INCLUDETAX_Included.equalsIgnoreCase(cash.getIncludeTax()) && cash.getTaxAmt().compareTo(Env.ZERO) != 0) {
			MDocType dt =  MDocType.get(getCtx(), cash.getC_DocType_ID());
			if (X_C_DocType.DOCBASETYPE_111Credit.equalsIgnoreCase(dt.getDocBaseType())) {
				dr = tax;			
			}
			
			if (X_C_DocType.DOCBASETYPE_111Debit.equalsIgnoreCase(dt.getDocBaseType())) {
				cr = tax;
			}
			FactLine f1 = fact.createHeader(dr, cr, cash.getC_Currency_ID(), cash.getCurrencyRate(), taxAmt, taxAmtConvert);
			f1.setC_BPartner_Dr_ID(cash.getC_BPartner_Dr_ID());
			f1.setC_BPartner_Cr_ID(cash.getC_BPartner_Cr_ID());
			f1.setC_TypeCost_ID(cash.getC_TypeCost_ID());
			f1.setC_TypeRevenue_ID(cash.getC_TypeRevenue_ID());
			if (Env.DisContract)
			{
				f1.setC_Contract_ID(cash.getC_Contract_ID());
				f1.setC_ContractAnnex_ID(cash.getC_ContractAnnex_ID());
			}
			if(Env.DisContractLine)
				f1.setC_ContractLine_ID(cash.getC_Contract_ID());
			
			if (Env.DisProject)
				f1.setC_Project_ID(cash.getC_Project_ID());
			if (Env.DisProjectLine)
				f1.setC_ProjectLine_ID(cash.getC_ProjectLine_ID());
			
			if (Env.DisConstruction)
				f1.setC_Construction_ID(cash.getC_Construction_ID());
			if (Env.DisConstructionLine)
				f1.setC_ConstructionLine_ID(cash.getC_ConstructionLine_ID());
		}
		ArrayList<Fact> facts = new ArrayList<Fact>();
		facts.add(fact);
		return facts;
	}   //  createFact

	
}   //  Doc_Cash
