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
package eone.base.model;

import java.sql.ResultSet;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.util.CCache;
import org.compiere.util.DB;
import org.compiere.util.ProcessUtil;
import org.compiere.util.Trx;

import eone.base.process.ProcessInfo;

/**
 *  Process Model
 *
 *  @author Jorg Janke
 *  @version $Id: MProcess.java,v 1.4 2006/07/30 00:58:04 jjanke Exp $
 * 
 * @author Teo Sarca, www.arhipac.ro
 * 			<li>BF [ 1757523 ] Server Processes are using Server's context
 * 			<li>FR [ 2214883 ] Remove SQL code and Replace for Query
 */
public class MProcess extends X_AD_Process
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7234986583327689692L;

	/**
	 * 	Get MProcess from Cache
	 *	@param ctx context
	 *	@param AD_Process_ID id
	 *	@return MProcess
	 */
	public static MProcess get (Properties ctx, int AD_Process_ID)
	{
		Integer key = Integer.valueOf(AD_Process_ID);
		MProcess retValue = (MProcess) s_cache.get (key);
		if (retValue != null)
			return retValue;
		retValue = new MProcess (ctx, AD_Process_ID, null);
		if (retValue.get_ID () != 0)
			s_cache.put (key, retValue);
		return retValue;
	}	//	get
	
	/**
	 * 	Get MProcess from Cache based on UUID
	 *	@param ctx context
	 *	@param AD_Process_UU UUID
	 *	@return MProcess
	 */
	public static MProcess get (Properties ctx, String AD_Process_UU)
	{
		MProcess retValue = s_cacheUU.get(AD_Process_UU);
		if (retValue != null)
			return retValue;
		int id = DB.getSQLValueEx(null, "SELECT AD_Process_ID FROM AD_Process WHERE AD_Process_UU = ? ", AD_Process_UU);
		retValue = new MProcess (ctx, id, null);
		
		return retValue;
	}	//	get

	/**
	 * 	Get MProcess from Menu
	 *	@param ctx context
	 *	@param AD_Menu_ID id
	 *	@return MProcess or null
	 */
	public static MProcess getFromMenu (Properties ctx, int AD_Menu_ID)
	{
		final String whereClause = "EXISTS (SELECT 1 FROM AD_Menu m"
							+" WHERE m.AD_Process_ID=AD_Process.AD_Process_ID AND m.AD_Menu_ID=?)";
		MProcess p = new Query(ctx, I_AD_Process.Table_Name, whereClause, null)
			.setParameters(AD_Menu_ID)
			.firstOnly();
		if (p != null)
		{
			s_cache.put (p.get_ID(), p);
		}
		return p;
	}	//	getFromMenu


	/**	Cache ID						*/
	private static CCache<Integer,MProcess>	s_cache	= new CCache<Integer,MProcess>(Table_Name, 20);
	/**	Cache UUID						*/
	private static CCache<String,MProcess>	s_cacheUU	= new CCache<String,MProcess>(Table_Name, Table_Name+"|AD_Process_UU", 20);
	
	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param AD_Process_ID process
	 *	@param trxName transaction name
	 */
	public MProcess (Properties ctx, int AD_Process_ID, String trxName)
	{
		super (ctx, AD_Process_ID, trxName);
		if (AD_Process_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setIsReport (false);
			setAccessLevel (ACCESSLEVEL_All);
			setEntityType (ENTITYTYPE_UserMaintained);
		}
	}	//	MProcess

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName transaction name
	 */
	public MProcess (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MProcess


	/**	Parameters					*/
	private MProcessPara[]		m_parameters = null;

	/**
	 * 	Get Parameters
	 *	@return parameters
	 */
	public MProcessPara[] getParameters()
	{
		if (m_parameters != null)
			return m_parameters;
		//
		final String whereClause = MProcessPara.COLUMNNAME_AD_Process_ID+"=?";
		List<MProcessPara> list = new Query(getCtx(), I_AD_Process_Para.Table_Name, whereClause, get_TrxName())
			.setParameters(get_ID())
			.setOnlyActiveRecords(true)
			.setOrderBy(MProcessPara.COLUMNNAME_SeqNo)
			.list();
		//
		m_parameters = new MProcessPara[list.size()];
		list.toArray(m_parameters);
		return m_parameters;
	}	//	getParameters

	/**
	 * 	Get Parameter with ColumnName
	 *	@param name column name
	 *	@return parameter or null
	 */
	public MProcessPara getParameter(String name)
	{
		getParameters();
		for (int i = 0; i < m_parameters.length; i++)
		{
			if (m_parameters[i].getColumnName().equals(name))
				return m_parameters[i];
		}
		return null;
	}	//	getParameter
	
	
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuilder sb = new StringBuilder ("MProcess[")
			.append (get_ID())
			.append("-").append(getName())
			.append ("]");
		return sb.toString ();
	}	//	toString

	
	public boolean processIt (ProcessInfo pi, Trx trx, boolean managedTrx)
	{
		
		boolean ok = false;

		//	Java Class
		String Classname = getClassname();
		if (Classname != null && Classname.length() > 0)
		{
			pi.setClassName(Classname);
			ok = startClass(pi, trx, managedTrx);
		}
		else
		{
			//	PL/SQL Procedure
			String ProcedureName = getProcedureName();
			if (ProcedureName != null && ProcedureName.length() > 0)
			{
				ok = startProcess (ProcedureName, pi, trx, managedTrx);
			}
			else
			{
				// BF IDEMPIERE-165
				if (this.isReport()) {
					ok = true;
				}
				else {
					String msg = "No Classname or ProcedureName for " + getName();
					pi.setSummary(msg, ok);
					log.warning(msg);
				}
			}
		}
		return ok;
	}	//	process

	/**
	 * 	Is this a Java Process
	 *	@return true if java process
	 */
	public boolean isJavaProcess()
	{
		String Classname = getClassname();
		return (Classname != null && Classname.length() > 0);
	}	//	is JavaProcess
	
	
	private boolean startProcess (String ProcedureName, ProcessInfo processInfo, Trx trx, boolean managedTrx)
	{
		String AD_Session_ID = processInfo.getAD_Session_ID();
		//  execute on this thread/connection
		if (log.isLoggable(Level.INFO)) log.info(ProcedureName + "(" + AD_Session_ID + ")");
		
		return ProcessUtil.startDatabaseProcedure(processInfo, ProcedureName, trx, managedTrx);
	}   //  startProcess


	/**
	 *  Start Java Class (sync).
	 *      instanciate the class implementing the interface ProcessCall.
	 *  The class can be a Server/Client class (when in Package
	 *  (e.g. in org.compiere.report)
	 *
	 *  @param Classname    name of the class to call
	 *  @param pi	process info
	 *  @param trx transaction
	 *  @param managedTrx false if trx is managed by caller
	 *  @return     true if success
	 *	see ProcessCtl.startClass
	 */
	private boolean startClass (ProcessInfo pi, Trx trx, boolean managedTrx)
	{
		if (log.isLoggable(Level.INFO)) log.info(pi.getClassName());

		if (pi.getClassName().toLowerCase().startsWith(MRule.SCRIPT_PREFIX)) {
			return ProcessUtil.startScriptProcess(getCtx(), pi, trx);
		} else {
			return ProcessUtil.startJavaProcess(getCtx(), pi, trx, managedTrx, null);
		}
	}   //  startClass

	
	
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if (!success)
			return success;
		if (newRecord)	//	Add to all automatic roles
		{
			MRole[] roles = MRole.getOf(getCtx(), "1=1");
			for (int i = 0; i < roles.length; i++)
			{
				
				MProcessAccess pa = new MProcessAccess(this, roles[i].getAD_Role_ID());
				pa.saveEx();
			}
		}
		//	Menu/Workflow
		else if (is_ValueChanged("IsActive") || is_ValueChanged("Name") 
			|| is_ValueChanged("Description") || is_ValueChanged("Help"))
		{
			MMenu[] menues = MMenu.get(getCtx(), "AD_Process_ID=" + getAD_Process_ID(), get_TrxName());
			for (int i = 0; i < menues.length; i++)
			{
				menues[i].setIsActive(isActive());
				menues[i].setName(getName());
				menues[i].setDescription(getDescription());
				menues[i].saveEx();
			}
			
		}
		return success;
	}	//	afterSave
	
	/**
	 * Grant independence to GenerateModel from AD_Process_ID
	 * @param value
	 * @param trxName
	 * @return
	 */
	public static int getProcess_ID(String value, String trxName)
	{
		int retValue = DB.getSQLValueEx(trxName, "SELECT AD_Process_ID FROM AD_Process WHERE Value=?", value);
		return retValue;
	}
	
	/**
	 * Copy settings from another process
	 * overwrites existing data
	 * (including translations)
	 * and saves.
	 * Not overwritten: name, value, entitytype
	 * @param source 
	 */
	public void copyFrom (MProcess source)
	{

		if (log.isLoggable(Level.FINE))log.log(Level.FINE, "Copying from:" + source + ", to: " + this);
		setAccessLevel(source.getAccessLevel());
		setClassname(source.getClassname());
		setDescription(source.getDescription());
		setIsReport(source.isReport());
		setJasperReport(source.getJasperReport());
		setProcedureName(source.getProcedureName());
		
		saveEx();
		
		// copy parameters 
		// TODO? Perhaps should delete existing first?
		MProcessPara[] parameters = source.getParameters();
		for ( MProcessPara sourcePara : parameters )
		{
			MProcessPara targetPara = new MProcessPara(this);
			targetPara.copyFrom (sourcePara);  // saves automatically
		}
	}

	/**
	 * 	Process It without closing the given transaction - used from workflow engine.
	 *	@param pi Process Info
	 *	@param trx transaction
	 *	@return true if OK
	 */
	public boolean processItWithoutTrxClose (ProcessInfo pi, Trx trx)
	{
		return processIt(pi, trx, false);
	}	//	processItWithoutTrxClose
	
}	//	MProcess
