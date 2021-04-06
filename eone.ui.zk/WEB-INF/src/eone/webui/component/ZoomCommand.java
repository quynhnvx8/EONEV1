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
package eone.webui.component;

import java.util.Map;

import org.compiere.util.Env;
import org.zkoss.json.JSONArray;
import org.zkoss.lang.Objects;
import org.zkoss.zk.au.AuRequest;
import org.zkoss.zk.au.AuService;
import org.zkoss.zk.mesg.MZk;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.event.Events;

import eone.base.model.MQuery;
import eone.base.model.MWindow;
import eone.webui.event.ZoomEvent;

/**
 * 
 * @author hengsin
 *
 */
public class ZoomCommand implements AuService {

	public ZoomCommand() {
	}

	public boolean service(AuRequest request, boolean everError) {
		if (!ZoomEvent.EVENT_NAME.equals(request.getCommand()))
			return false;

		Map<?, ?> map = request.getData();
		JSONArray data = (JSONArray) map.get("data");
		
		final Component comp = request.getComponent();
		if (comp == null)
			throw new UiException(MZk.ILLEGAL_REQUEST_COMPONENT_REQUIRED, this);
		
		if (data == null || data.size() < 2)
			throw new UiException(MZk.ILLEGAL_REQUEST_WRONG_DATA, new Object[] {
					Objects.toString(data), this });
		
		String tableName = (String) data.get(0);
		String columnName = "";
		if (tableName.contains(".")) {
			String [] s = tableName.split(".");
			tableName = s[0];
			columnName = s[1];
		}
		Object code = null; 
		int windowID = 0;
		
		code = data.get(1);
		
		String zoomLogic = "";
		
		if (data.size() == 3) {
			zoomLogic = data.get(2).toString();			
		}

		if (   data.size() > 3 && data.get(3) != null
			&& data.get(2) != null && data.get(2).toString().equalsIgnoreCase("AD_Window_UU"))
		{
			String windowUU = (String) data.get(3);
			MWindow window = MWindow.get(Env.getCtx(), windowUU);
			if (window != null)
				windowID = window.getAD_Window_ID();
		}
		//
		MQuery query = new MQuery(tableName);
		query.addRestriction(columnName, MQuery.EQUAL, code);
		query.setRecordCount(1);
		query.setZoomTableName(tableName);
		query.setZoomColumnName(columnName);
		query.setZoomValue(code);
		query.setZoomLogic(zoomLogic);
		query.setZoomWindowID(windowID);

		Events.postEvent(new ZoomEvent(comp, query));

		return true;
	}

}
