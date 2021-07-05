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

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.compiere.util.DB;

/**
 *
 * @author hengsin
 *
 */
public class PromotionRule {

	public static void applyPromotions(MOrder order) throws Exception {
		//key = C_OrderLine, value = Qty to distribution
		Map<Integer, BigDecimal> orderLineQty = new LinkedHashMap<Integer, BigDecimal>();
		//Map<Integer, MOrderLine> orderLineIndex = new HashMap<Integer, MOrderLine>();
		MOrderLine[] lines = order.getLines();
		boolean hasDeleteLine = false;
		for (MOrderLine ol : lines) {
			if (ol.getM_Product_ID() > 0) {
				if (ol.getQty().signum() > 0) {
					orderLineQty.put(ol.getC_OrderLine_ID(), ol.getQty());
					//orderLineIndex.put(ol.getC_OrderLine_ID(), ol);
				}
			} 
		}
		if (orderLineQty.isEmpty()) return;

		//refresh order
		if (hasDeleteLine) {
			order.getLines(true, null);
			order.setAmount(DB.getSQLValueBD(order.get_TrxName(), "SELECT Amount From C_Order WHERE C_Order_ID = ?", order.getC_Order_ID()));
		}

		Map<Integer, List<Integer>> promotions = PromotionRule.findM_Promotion_ID(order);

		if (promotions == null || promotions.isEmpty()) return;

		
	}

	

	/**
	 *
	 * @param order
	 * @return Map<M_Promotion_ID, List<M_PromotionLine_ID>>
	 * @throws Exception
	 */
	private static Map<Integer, List<Integer>> findM_Promotion_ID(MOrder order) throws Exception {
		

		return null;
	}

	

	static class DistributionSet {
		//<C_OrderLine_Id, DistributionQty>
		Map<Integer, BigDecimal> orderLines = new LinkedHashMap<Integer, BigDecimal>();
		BigDecimal setQty = BigDecimal.ZERO;
	}

	static class OrderLineComparator implements Comparator<Integer> {
		Map<Integer, MOrderLine> index;
		OrderLineComparator(Map<Integer, MOrderLine> olIndex) {
			index = olIndex;
		}

		public int compare(Integer ol1, Integer ol2) {
			return index.get(ol1).getPrice().compareTo(index.get(ol2).getPrice());
		}
	}
}
