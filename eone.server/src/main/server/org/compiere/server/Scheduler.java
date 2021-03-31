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
package org.compiere.server;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.model.MAttachment;
import org.compiere.model.MClient;
import org.compiere.model.MNote;
import org.compiere.model.MOrgInfo;
import org.compiere.model.MProcess;
import org.compiere.model.MRole;
import org.compiere.model.MScheduler;
import org.compiere.model.MSchedulerLog;
import org.compiere.model.MUser;
import org.compiere.util.CCache;
import org.compiere.util.Env;
import org.compiere.util.TimeUtil;
import org.compiere.util.Trx;
import org.compiere.util.Util;

import eone.base.process.ProcessInfo;
import eone.base.process.ServerProcessCtl;


/**
 *	Scheduler
 *
 *  @author Jorg Janke
 *  @version $Id: Scheduler.java,v 1.5 2006/07/30 00:53:33 jjanke Exp $
 *  
 *  Contributors:
 *    Carlos Ruiz - globalqss - FR [3135351] - Enable Scheduler for buttons
 */
public class Scheduler extends AdempiereServer
{
	/**
	 * 	Scheduler
	 *	@param model model
	 */
	public Scheduler (MScheduler model)
	{
		super (model, 30);	//	30 seconds delay
		AD_Scheduler_ID = model.getAD_Scheduler_ID();
	//	m_client = MClient.get(model.getCtx(), model.getAD_Client_ID());
	}	//	Scheduler

	/**	Last Summary				*/
	protected StringBuffer 		m_summary = new StringBuffer();
	/** Transaction					*/
	protected Trx					m_trx = null;

	protected int AD_Scheduler_ID;

	private static CCache<Integer,MScheduler> s_cache = new CCache<Integer,MScheduler>(MScheduler.Table_Name, 10, 60, true);

	/**
	 * 	Work
	 */
	protected void doWork ()
	{
		MScheduler scheduler = get(getCtx(), AD_Scheduler_ID);
		m_summary = new StringBuffer(scheduler.toString())
			.append(" - ");

		// Prepare a ctx for the report/process - BF [1966880]
		MClient schedclient = MClient.get(getCtx(), scheduler.getAD_Client_ID());
		Env.setContext(getCtx(), "#AD_Client_ID", schedclient.getAD_Client_ID());
		Env.setContext(getCtx(), "#AD_Language", schedclient.getAD_Language());
		Env.setContext(getCtx(), "#AD_Org_ID", scheduler.getAD_Org_ID());
		if (scheduler.getAD_Org_ID() != 0) {
			MOrgInfo schedorg = MOrgInfo.get(getCtx(), scheduler.getAD_Org_ID(), null);
			if (schedorg.getM_Warehouse_ID() > 0)
				Env.setContext(getCtx(), "#M_Warehouse_ID", schedorg.getM_Warehouse_ID());
		}
		Env.setContext(getCtx(), "#AD_User_ID", getAD_User_ID());
		Env.setContext(getCtx(), "#SalesRep_ID", getAD_User_ID());
		// TODO: It can be convenient to add  AD_Scheduler.AD_Role_ID
		MUser scheduser = MUser.get(getCtx(), getAD_User_ID());
		MRole[] schedroles = scheduser.getRoles(scheduler.getAD_Org_ID());
		if (schedroles != null && schedroles.length > 0)
			Env.setContext(getCtx(), "#AD_Role_ID", schedroles[0].getAD_Role_ID()); // first role, ordered by AD_Role_ID
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		SimpleDateFormat dateFormat4Timestamp = new SimpleDateFormat("dd-MM-yyyy"); 
		Env.setContext(getCtx(), "#Date", dateFormat4Timestamp.format(ts)+" 00:00:00" );    //  JDBC format

		MProcess process = new MProcess(getCtx(), scheduler.getAD_Process_ID(), null);
		try
		{
			m_trx = Trx.get(Trx.createTrxName("Scheduler"), true);
			m_trx.setDisplayName(getClass().getName()+"_"+getModel().getName()+"_doWork");
			m_summary.append(runProcess(process));
			m_trx.commit(true);
		}
		catch (Throwable e)
		{
			if (m_trx != null)
				m_trx.rollback();
			log.log(Level.WARNING, process.toString(), e);
			m_summary.append(e.toString());
		}
		finally
		{
			if (m_trx != null)
				m_trx.close();
		}
		
		//
		int no = scheduler.deleteLog();
		m_summary.append(" Logs deleted=").append(no);
		//
		MSchedulerLog pLog = new MSchedulerLog(scheduler, m_summary.toString());
		pLog.setReference("#" + String.valueOf(p_runCount)
			+ " - " + TimeUtil.formatElapsed(new Timestamp(p_startWork)));
		pLog.saveEx();
	}	//	doWork

	/**
	 * 	Run Process or Report
	 *	@param process process
	 *	@return summary
	 *	@throws Exception
	 */
	protected String runProcess(MProcess process) throws Exception
	{
		if (log.isLoggable(Level.INFO)) log.info(process.toString());
		MScheduler scheduler = get(getCtx(), AD_Scheduler_ID);
		
		boolean isReport = (process.isReport()  || process.getJasperReport() != null);
		String schedulerName = Env.parseContext(getCtx(), -1, scheduler.getName(), false, true);
		
		//	Process (see also MWFActivity.performWork
		int AD_Table_ID = scheduler.getAD_Table_ID();
		int Record_ID = scheduler.getRecord_ID();
		//
		//
		ProcessInfo pi = new ProcessInfo (process.getName(), process.getAD_Process_ID(), AD_Table_ID, Record_ID);
		pi.setAD_User_ID(getAD_User_ID());
		pi.setAD_Client_ID(scheduler.getAD_Client_ID());
		pi.setIsBatch(true);
		pi.setPrintPreview(true);
		pi.setReportType(scheduler.getReportOutputType());
		
		MUser from = new MUser(getCtx(), pi.getAD_User_ID(), null);
		
		pi.setTransactionName(m_trx != null ? m_trx.getTrxName() : null);
		if (!Util.isEmpty(process.getJasperReport())) 
		{
			pi.setExport(true);
			if ("HTML".equals(pi.getReportType())) 
				pi.setExportFileExtension("html");
			else if ("CSV".equals(pi.getReportType()))
				pi.setExportFileExtension("csv");
			else if ("XLS".equals(pi.getReportType()))
				pi.setExportFileExtension("xls");
			else if ("XLSX".equals(pi.getReportType()))
				pi.setExportFileExtension("xlsx");
			else
				pi.setExportFileExtension("pdf");
		}
		ServerProcessCtl.process(pi, m_trx);
		if ( pi.isError() ) // note, this call close the transaction, don't use m_trx below
		{
			// notify supervisor if error
			int supervisor = scheduler.getSupervisor_ID();
			if (supervisor > 0)
			{
				MUser user = new MUser(getCtx(), supervisor, null);
				boolean email = user.isNotificationEMail();
				boolean notice = user.isNotificationNote();
				
				if (email)
				{
					MClient client = MClient.get(scheduler.getCtx(), scheduler.getAD_Client_ID());
					client.sendEMail(from, user, schedulerName, pi.getSummary() + " " + pi.getLogInfo(), null);
				}
				if (notice) {
					int AD_Message_ID = 442; // HARDCODED ProcessRunError
					MNote note = new MNote(getCtx(), AD_Message_ID, supervisor, null);
					note.setClientOrg(scheduler.getAD_Client_ID(), scheduler.getAD_Org_ID());
					note.setTextMsg(schedulerName+"\n"+pi.getSummary());
					note.saveEx();
					String log = pi.getLogInfo(true);
					if (log != null &&  log.trim().length() > 0) {
						MAttachment attachment = new MAttachment (getCtx(), MNote.Table_ID, note.getAD_Note_ID(), null);
						attachment.setClientOrg(scheduler.getAD_Client_ID(), scheduler.getAD_Org_ID());
						attachment.setTextMsg(schedulerName);
						attachment.addEntry("ProcessLog.html", log.getBytes("UTF-8"));
						attachment.saveEx();
					}
				}
			}
		}
		
		// always notify recipients
		Integer[] userIDs = scheduler.getRecipientAD_User_IDs();
		if (userIDs.length > 0) 
		{
			List<File> fileList = new ArrayList<File>();
			if (isReport && pi.getPDFReport() != null) {
				fileList.add(pi.getPDFReport());
			}
			if (pi.isExport() && pi.getExportFile() != null)
			{
				fileList.add(pi.getExportFile());
			}
			
			for (int i = 0; i < userIDs.length; i++)
			{
				MUser user = new MUser(getCtx(), userIDs[i].intValue(), null);
				
				boolean notice = user.isNotificationNote();
								
				if (notice) {
					int AD_Message_ID = 441; // ProcessOK
					if (isReport)
						AD_Message_ID = 884; //	HARDCODED SchedulerResult
					MNote note = new MNote(getCtx(), AD_Message_ID, userIDs[i].intValue(), null);
					note.setClientOrg(scheduler.getAD_Client_ID(), scheduler.getAD_Org_ID());
					if (isReport) {
						note.setTextMsg(schedulerName);
						note.setDescription(scheduler.getDescription());
						note.setRecord(AD_Table_ID, Record_ID);
					} else {
						note.setTextMsg(schedulerName + "\n" + pi.getSummary());
					}
					if (note.save()) {
						MAttachment attachment = null;
						if (fileList != null && !fileList.isEmpty()) {
							//	Attachment
							attachment = new MAttachment (getCtx(), MNote.Table_ID, note.getAD_Note_ID(), null);
							attachment.setClientOrg(scheduler.getAD_Client_ID(), scheduler.getAD_Org_ID());
							attachment.setTextMsg(schedulerName);
							for (File entry : fileList)
								attachment.addEntry(entry);
							
						} 
						String log = pi.getLogInfo(true);
						if (log != null &&  log.trim().length() > 0) {
							if (attachment == null) {
								attachment = new MAttachment (getCtx(), MNote.Table_ID, note.getAD_Note_ID(), null);
								attachment.setClientOrg(scheduler.getAD_Client_ID(), scheduler.getAD_Org_ID());
								attachment.setTextMsg(schedulerName);
							}
							attachment.addEntry("ProcessLog.html", log.getBytes("UTF-8"));
							attachment.saveEx();
						}
						if (attachment != null)
							attachment.saveEx();
					}
				}
				
				
			}
			
			// IDEMPIERE-2864
			for(File file : fileList)
			{
				if(file.exists() && !file.delete())
					file.deleteOnExit();
			}
		}
		
		return pi.getSummary();
	}	//	runProcess

	protected int getAD_User_ID() {
		MScheduler scheduler = get(getCtx(), AD_Scheduler_ID);
		int AD_User_ID;
		if (scheduler.getSupervisor_ID() > 0)
			AD_User_ID = scheduler.getSupervisor_ID();
		else if (scheduler.getCreatedBy() > 0)
			AD_User_ID = scheduler.getCreatedBy();
		else if (scheduler.getUpdatedBy() > 0)
			AD_User_ID = scheduler.getUpdatedBy();
		else
			AD_User_ID = 100; //fall back to SuperUser
		return AD_User_ID;
	}
	
	
	public String getServerInfo()
	{
		return "#" + p_runCount + " - Last=" + m_summary.toString();
	}	//	getServerInfo

	/**
	 * @param ctx
	 * @param AD_Scheduler_ID
	 * @return MScheduler
	 */
	protected static MScheduler get(Properties ctx, int AD_Scheduler_ID)
	{
		Integer key = Integer.valueOf(AD_Scheduler_ID);
		MScheduler retValue = (MScheduler)s_cache.get(key);
		if (retValue == null)
		{
			retValue = new MScheduler(ctx, AD_Scheduler_ID, null);
			if (AD_Scheduler_ID == 0)
			{
				String trxName = null;
				retValue.load(trxName);	//	load System Record
			}
			s_cache.put(key, retValue);
		}
		return retValue;
	}	//	get
}	//	Scheduler
