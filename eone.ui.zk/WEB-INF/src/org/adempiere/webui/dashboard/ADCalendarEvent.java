/******************************************************************************
 * Copyright (C) 2008 Elaine Tan                                              *
 * Copyright (C) 2008 Idalica Corporation                                     *
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
package org.adempiere.webui.dashboard;

import org.zkoss.calendar.impl.SimpleCalendarEvent;

/**
 * 
 * @author Elaine
 *
 */
public class ADCalendarEvent extends SimpleCalendarEvent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2289841014956779967L;
	
	private int p_HR_Employee_ID;
	private int p_HR_ItemLine02_ID;

	public int getHR_Employee_ID() {
		return p_HR_Employee_ID;
	}

	public void setHR_Employee_ID(int p_HR_Employee_ID) {
		this.p_HR_Employee_ID = p_HR_Employee_ID;
	}
	
	public int getR_RequestType_ID() {
		return p_HR_ItemLine02_ID;
	}

	public void setHR_ItemLine02_ID(int p_HR_ItemLine02_ID) {
		this.p_HR_ItemLine02_ID = p_HR_ItemLine02_ID;
	}
}
