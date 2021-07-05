
package eone.base.process;

import java.util.logging.Level;

import org.compiere.util.AdempiereSystemError;

import eone.base.model.MOrder;
 

public class OrderOpen extends SvrProcess
{
	private int		p_C_Order_ID = 0;

	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("C_Order_ID"))
				p_C_Order_ID = para[i].getParameterAsInt();
			else
				log.log(Level.SEVERE, "prepare - Unknown Parameter: " + name);
		}
	}	//	prepare

	protected String doIt() throws AdempiereSystemError
	{
		if (log.isLoggable(Level.INFO)) log.info("doIt - Open C_Order_ID=" + p_C_Order_ID);
		if (p_C_Order_ID == 0)
			return "";
		//
		MOrder order = new MOrder (getCtx(), p_C_Order_ID, get_TrxName());
		
		
		return order.save() ? "@OK@" : "@Error@";
	}	//	doIt
	
}	//	OrderOpen
