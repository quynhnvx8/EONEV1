/******************************************************************************
 * Copyright (C) 2013 Elaine Tan                                              *
 * Copyright (C) 2013 Trek Global
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 *****************************************************************************/
package org.adempiere.webui.apps.form;

import org.compiere.grid.ICreateFrom;
import org.compiere.grid.ICreateFromFactory;

import eone.base.model.GridTab;
import eone.base.model.I_C_Bank;
import eone.base.model.I_C_Invoice;

/**
 * 
 * @author Elaine
 *
 */
public class DefaultCreateFromFactory implements ICreateFromFactory 
{

	@Override
	public ICreateFrom create(GridTab mTab) 
	{
		String tableName = mTab.getTableName();
		if (tableName.equals(I_C_Invoice.Table_Name))
			return new WCreateFromInvoiceUI(mTab);
		else if (tableName.equals(I_C_Bank.Table_Name))
			return new WCreateFromStatementUI(mTab);
		return null;
	}

}