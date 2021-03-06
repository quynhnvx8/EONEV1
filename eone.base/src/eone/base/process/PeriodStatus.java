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
package eone.base.process;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.compiere.util.AdempiereUserError;
import org.compiere.util.CacheMgt;
import org.compiere.util.DB;

import eone.base.model.MPeriod;

/**
 *	Open/Close all Period (Control)
 *	
 *  @author Jorg Janke
 *  @version $Id: PeriodStatus.java,v 1.2 2006/07/30 00:51:02 jjanke Exp $
 */
public class PeriodStatus extends SvrProcess
{
	/** Periods						*/
	private List<Integer> p_C_Period_IDs;
	/** Action						*/
	private String		p_PeriodAction = null;
	
	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("PeriodAction"))
				p_PeriodAction = (String)para[i].getParameter();
			else if (name.equals("*RecordIDs*"))
				;
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		p_C_Period_IDs = getRecord_IDs();
		if (p_C_Period_IDs == null || p_C_Period_IDs.size() == 0) {
			p_C_Period_IDs = new ArrayList<Integer>();
			p_C_Period_IDs.add(getRecord_ID());
		}
	}	//	prepare

	/**
	 * 	Process
	 *	@return message
	 *	@throws Exception
	 */
	protected String doIt() throws Exception
	{
	  int no = 0;
	  if (log.isLoggable(Level.INFO)) log.info ("C_Period_ID=" + p_C_Period_IDs + ", PeriodAction=" + p_PeriodAction);
	  for (int p_C_Period_ID : p_C_Period_IDs) {
		MPeriod period = new MPeriod (getCtx(), p_C_Period_ID, get_TrxName());
		if (period.get_ID() == 0)
			throw new AdempiereUserError("@NotFound@  @C_Period_ID@=" + p_C_Period_ID);

		StringBuilder sql = new StringBuilder ("UPDATE C_Period ");
		sql.append("SET PeriodStatus='");
		//	Open
		if (MPeriod.PERIODACTION_OpenPeriod.equals(p_PeriodAction))
			sql.append (MPeriod.PERIODACTION_OpenPeriod);
		//	Close
		else if (MPeriod.PERIODACTION_ClosePeriod.equals(p_PeriodAction))
			sql.append (MPeriod.PERIODACTION_ClosePeriod);
		//	Close Permanently
		else if (MPeriod.PERIODACTION_PermanentlyClosePeriod.equals(p_PeriodAction))
			sql.append (MPeriod.PERIODACTION_PermanentlyClosePeriod);
		else
			return "-";
		//
		sql.append("', PeriodAction='N', Updated=getDate(),UpdatedBy=").append(getAD_User_ID());
		//	WHERE
		sql.append(" WHERE C_Period_ID=").append(period.getC_Period_ID())
			.append(" AND PeriodStatus<>'P'")
			.append(" AND PeriodStatus<>'").append(p_PeriodAction).append("'");
			
		no += DB.executeUpdateEx(sql.toString(), get_TrxName());
		
		CacheMgt.get().reset("C_Period", p_C_Period_ID);
	  }
	  CacheMgt.get().reset("C_Period", 0);
	  StringBuilder msgreturn = new StringBuilder("@Updated@ #").append(no);
	  return msgreturn.toString();
	}	//	doIt

}	//	PeriodStatus
