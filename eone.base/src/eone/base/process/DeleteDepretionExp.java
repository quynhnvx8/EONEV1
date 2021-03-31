package eone.base.process;

import java.util.logging.Level;

import org.compiere.util.DB;


public class DeleteDepretionExp extends SvrProcess
{
	private int p_Record_ID = 0;
	protected void prepare ()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null && para[i].getParameter_To() == null)
				;
			else if (name.equals("C_DocTypeTarget_ID"))
				;
			
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		p_Record_ID = getRecord_ID();
	}	//	prepare


	protected String doIt () throws Exception
	{
		String sql = "Delete From A_Depreciation_Exp Where A_Depreciation_ID = ?";
		DB.executeUpdate(sql, p_Record_ID, true, get_TrxName());
		
		return "Success!";
	}	//	doIt
	
	
}	