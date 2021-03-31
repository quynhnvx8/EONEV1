/******************************************************************************
 * Copyright (C) 2013 Nur Yasmin                                              *
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
package org.adempiere.base.event;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.StringTokenizer;

import org.compiere.util.Util;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.event.Event;

/**
 * Request event handler
 * @author Nur Yasmin
 *
 */
public class RequestEventHandler extends AbstractEventHandler implements ManagedService
{
	
	@Override
	protected void doHandleEvent(Event event) 
	{
		
	}

	@Override
	protected void initialize() 
	{
		registerEvent(IEventTopics.REQUEST_SEND_EMAIL);
		
	}
	
	

	public static final String IGNORE_REQUEST_TYPES = "ignoreRequestTypes";
	private static ArrayList<String> ignoreRequestTypes = new ArrayList<String>();
	
	@SuppressWarnings("rawtypes")
	@Override
	public void updated(Dictionary properties) throws ConfigurationException {
		if (properties != null) {
			String p = (String) properties.get(IGNORE_REQUEST_TYPES);
			if (!Util.isEmpty(p)) {
				ignoreRequestTypes.clear();
				
				StringTokenizer st = new StringTokenizer(p, ";");
				while (st.hasMoreTokens())
					ignoreRequestTypes.add(st.nextToken().trim());
			}
		}
	}
}
