
package eone.base.process;

import java.util.ArrayList;

import org.compiere.util.Env;


public class ProcessInfoUtil
{
	
		
	public static void setParameterFromDB (ProcessInfo pi)
	{
		
		ArrayList<ProcessInfoParameter> lists = new ArrayList<ProcessInfoParameter>();
		ProcessInfoParameter [] list = pi.getProcessPara();
		for (int i = 0; i < list.length; i ++) {
			String ParameterName = list[i].getParameterName();
			Object Parameter = list[i].getP_String();
			Object Parameter_To = list[i].getP_String_To();
			//	Big Decimal
			if (Parameter == null && Parameter_To == null)
			{
				Parameter = list[i].getP_Number();
				Parameter_To = list[i].getP_Number_To();
			}
			//	Timestamp
			if (Parameter == null && Parameter_To == null)
			{
				Parameter = list[i].getP_Date();
				Parameter_To = list[i].getP_Date_To();
			}
			//	Info
			String Info = list[i].getInfo();
			String Info_To = list[i].getInfo_To();
			//
			lists.add (new ProcessInfoParameter(ParameterName, Parameter, Parameter_To, Info, Info_To));
			//
			if (pi.getAD_Client_ID() == null)
				pi.setAD_Client_ID (Env.getContextAsInt(Env.getCtx(), "#AD_Client_ID"));
			if (pi.getAD_User_ID() == null)
				pi.setAD_User_ID(Env.getContextAsInt(Env.getCtx(), "#AD_User_ID"));
		}
		
		ProcessInfoParameter[] pars = new ProcessInfoParameter[lists.size()];
		lists.toArray(pars);
		pi.setParameter(pars);
	}   //  setParameterFromDB


}	//	ProcessInfoUtil
