
package org.compiere.print;

import java.math.BigDecimal;
import java.util.logging.Level;

import eone.base.process.ProcessInfoParameter;
import eone.base.process.SvrProcess;

/**
 * 	@author 	Quynhnv.x8
 *  @date		21/10/2020
 *  Dung de copy format print item tren chuc nang PrintFormatItem
 */
public class MPrintFormatProcess extends SvrProcess
{
	/** PrintFormat             */
	private BigDecimal	m_AD_PrintFormat_ID;
	/** Table	                */

	/**
	 *  Prepare - get Parameters.
	 */
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("AD_PrintFormat_ID"))
				m_AD_PrintFormat_ID = ((BigDecimal)para[i].getParameter());
			else
				log.log(Level.SEVERE, "prepare - Unknown Parameter=" 
						+ para[i].getParameterName());
		}
	}   //  prepare

	
	protected String doIt() throws Exception
	{
		if (m_AD_PrintFormat_ID != null && m_AD_PrintFormat_ID.intValue() > 0)
		{
			if (log.isLoggable(Level.INFO)) log.info("Copy from AD_PrintFormat_ID=" + m_AD_PrintFormat_ID);
			MPrintFormat pf = MPrintFormat.copy (getCtx(), m_AD_PrintFormat_ID.intValue(), getRecord_ID());
			addLog(m_AD_PrintFormat_ID.intValue(), null, new BigDecimal(pf.getItemCount()), pf.getName());
			return pf.getName() + " #" + pf.getItemCount();
		}
		else
			throw new Exception (MSG_InvalidArguments);
	}	//	doIt

}	//	MPrintFormatProcess
