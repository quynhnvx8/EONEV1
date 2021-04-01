/******************************************************************************
 * Product: iDempiere ERP & CRM Smart Business Solution                       *
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
package org.adempiere.base;

import static org.compiere.util.DisplayType.Account;
import static org.compiere.util.DisplayType.Location;
import static org.compiere.util.DisplayType.Locator;
import static org.compiere.util.DisplayType.PAttribute;
import static org.compiere.util.DisplayType.Payment;

import org.compiere.util.DisplayType;

import eone.base.model.GridFieldVO;
import eone.base.model.Lookup;
import eone.base.model.MAccountLookup;
import eone.base.model.MLookup;
import eone.base.model.MPAttributeLookup;
import eone.base.model.MPaymentLookup; 

/**
 * @author Jan Thielemann - jan.thielemann@evenos.de
 * @author hengsin
 *
 */
public class DefaultLookupFactory implements ILookupFactory{

	@Override
	public Lookup getLookup(GridFieldVO gridFieldVO) {
		Lookup lookup = null;
		if (gridFieldVO.lookupInfo == null && DisplayType.isLookup(gridFieldVO.displayType)) // IDEMPIERE-913
			gridFieldVO.loadLookupInfo();
		if (gridFieldVO.displayType == Account)    //  not cached
		{
			lookup = new MAccountLookup (gridFieldVO.ctx, gridFieldVO.WindowNo);
		}
		else if (gridFieldVO.displayType == PAttribute)    //  not cached
		{
			lookup = new MPAttributeLookup (gridFieldVO.ctx, gridFieldVO.WindowNo);
		}
		else if (gridFieldVO.displayType == Payment)
		{
			lookup = new MPaymentLookup (gridFieldVO.ctx, gridFieldVO.WindowNo, gridFieldVO.ValidationCode);
		}
		else if (DisplayType.isLookup(gridFieldVO.displayType) && gridFieldVO.lookupInfo != null)
		{
			lookup = new MLookup (gridFieldVO.lookupInfo, gridFieldVO.TabNo);
		}
		return lookup;
	}

	@Override
	public boolean isLookup(GridFieldVO gridFieldVO) {
		if (gridFieldVO.displayType == Location
			|| gridFieldVO.displayType == Locator
			|| gridFieldVO.displayType == Account
			|| gridFieldVO.displayType == PAttribute
			|| gridFieldVO.displayType == Payment
			|| DisplayType.isLookup(gridFieldVO.displayType))
			return true;
				
		return false;
	}

}
