/*******************************************************************************
 * Copyright (C) 2017 Trek Global Inc.										   *
 * Copyright (C) 2017 Low Heng Sin                                             *
 * This program is free software; you can redistribute it and/or modify it     *
 * under the terms version 2 of the GNU General Public License as published    *
 * by the Free Software Foundation. This program is distributed in the hope    *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied  *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.            *
 * See the GNU General Public License for more details.                        *
 * You should have received a copy of the GNU General Public License along     *
 * with this program; if not, write to the Free Software Foundation, Inc.,     *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                      *
 *******************************************************************************/
package org.adempiere.exceptions;

import java.math.BigDecimal;

/**
 * 
 * @author hengsin
 *
 */
public class NegativeInventoryDisallowedException extends AdempiereException
{	
	/**
	 * 
	 */
	private static final long serialVersionUID = 253224414462489886L;
	
	private int M_Warehouse_ID;
	private int M_Product_ID;
	private int M_AttributeSetInstance_ID;
	private int M_Locator_ID;
	private BigDecimal QtyOnHand;
	private BigDecimal MovementQty;

	

	public int getM_Warehouse_ID() {
		return M_Warehouse_ID;
	}

	public int getM_Product_ID() {
		return M_Product_ID;
	}

	public int getM_AttributeSetInstance_ID() {
		return M_AttributeSetInstance_ID;
	}

	public int getM_Locator_ID() {
		return M_Locator_ID;
	}

	public BigDecimal getQtyOnHand() {
		return QtyOnHand;
	}

	public BigDecimal getMovementQty() {
		return MovementQty;
	}
}
