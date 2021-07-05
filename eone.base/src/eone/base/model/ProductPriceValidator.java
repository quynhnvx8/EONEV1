/******************************************************************************
 * Copyright (C) 2013 Elaine Tan                                              *
 * Copyright (C) 2013 Trek Global											  *
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
package eone.base.model;

/**
 * Auto sync corresponding price list with the price list schema and base price list.
 * @author Elaine
 *
 */
public class ProductPriceValidator implements ModelValidator {
	
	//private static final CLogger log = CLogger.getCLogger(ProductPriceValidator.class);
	private int m_AD_Client_ID;
	
	public int getAD_Client_ID() {
		return m_AD_Client_ID;
	}

	public void initialize(ModelValidationEngine engine, MClient client) {
		engine.addModelChange(MPrice.Table_Name, this);
		if (client != null)
			m_AD_Client_ID = client.getAD_Client_ID();
	}

	public String login(int AD_Org_ID, int AD_Role_ID, int AD_User_ID) {
		return null;
	}

	public String modelChange(PO po, int type) throws Exception {
		
		return "";
	}
	
	public String docValidate(PO po, int timing) {
		return null;
	}
}