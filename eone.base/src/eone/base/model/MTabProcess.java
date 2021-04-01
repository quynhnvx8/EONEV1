package eone.base.model;

import java.sql.ResultSet;
import java.util.Properties;

public class MTabProcess extends X_AD_TabProcess
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1163877751931787947L;
	
	public MTabProcess(Properties ctx, int AD_Tab_Process_ID, String trxName)
	{
		super(ctx, AD_Tab_Process_ID, trxName);
	}

	public MTabProcess(Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}
	
	
}
