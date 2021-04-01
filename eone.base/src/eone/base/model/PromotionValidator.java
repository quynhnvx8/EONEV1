/******************************************************************************
 * Copyright (C) 2009 Low Heng Sin                                            *
 * Copyright (C) 2009 Idalica Corporation                                     *
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

import org.adempiere.exceptions.AdempiereException;

/**
 *
 * @author hengsin
 *
 */
public class PromotionValidator implements ModelValidator {

	private int m_AD_Client_ID;

	public String docValidate(PO po, int timing) {
		if (po instanceof MOrder ) {
			if (timing == TIMING_AFTER_PREPARE) {
				MOrder order = (MOrder) po;
				try {
					PromotionRule.applyPromotions(order);
					order.getLines(true, null);
					order.calculateTaxTotal();
					order.saveEx();
					increasePromotionCounter(order);
				} catch (Exception e) {
					if (e instanceof RuntimeException)
						throw (RuntimeException)e;
					else
						throw new AdempiereException(e.getLocalizedMessage(), e);
				}
			} else if (timing == TIMING_AFTER_VOID) {
				MOrder order = (MOrder) po;
				decreasePromotionCounter(order);
			}
		}
		return null;
	}

	private void increasePromotionCounter(MOrder order) {
		
	}

	private void decreasePromotionCounter(MOrder order) {
		
	}

	
	public int getAD_Client_ID() {
		return m_AD_Client_ID;
	}

	public void initialize(ModelValidationEngine engine, MClient client) {
		if (client != null)
			m_AD_Client_ID = client.getAD_Client_ID();
		engine.addDocValidate(I_C_Order.Table_Name, this);
		engine.addModelChange(I_C_OrderLine.Table_Name, this);
		
	}

	public String login(int AD_Org_ID, int AD_Role_ID, int AD_User_ID) {
		return null;
	}

	public String modelChange(PO po, int type) throws Exception {
		
		return null;
	}
}
