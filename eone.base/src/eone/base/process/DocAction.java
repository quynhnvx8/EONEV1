
package eone.base.process;

import java.util.Properties;

import org.compiere.util.CLogger;

import eone.base.model.SystemIDs;
import eone.exceptions.EONEException;


public interface DocAction
{
	public static final String ACTION_Complete = "CO";
	public static final String ACTION_ReActivate = "RE";
	
	/** Drafted = DR */
	public static final String STATUS_Drafted = "DR";
	/** Completed = CO */
	public static final String STATUS_Completed = "CO";
	
	
	/** DocAction Ref_List values **/
	public static final int AD_REFERENCE_ID = SystemIDs.REFERENCE_DOCUMENTACTION;
	
	
	public void setDocStatus (String newStatus);

	public String getDocStatus();
	
	public boolean processIt (String action, int AD_Window_ID) throws Exception;
	
	
	public String completeIt();
	
	public boolean reActivateIt();
		
	public String getProcessMsg ();
	
	public void setProcessMsg(String text);
	
	public boolean save();
	
	public void saveEx() throws EONEException;
	
	public Properties getCtx();
	
	public int get_ID();
	
	public int get_Table_ID();
	
	public CLogger get_Logger();

	public String get_TrxName();
	
	public String getSummary();
	
	public int getAD_Window_ID();
	
}	//	DocAction
