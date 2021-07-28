package eone.base.model;
import java.sql.ResultSet;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.util.CCache;
import org.compiere.util.DB;

import eone.base.process.DocAction;
import eone.base.process.DocumentEngine;



public class MDepreciation extends X_A_Depreciation implements DocAction
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -632058079835100100L;

	/** Standard Constructor */
	public MDepreciation (Properties ctx, int A_Depreciation_ID, String trxName)
	{
		super (ctx, A_Depreciation_ID, trxName);
	}	//	MDepreciation

	/**
	 *  Load Constructor
	 *  @param ctx context
	 *  @param rs result set record
	 */
	public MDepreciation (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	//	MDepreciation

	/**		Cache									*/
	private static CCache<Integer,MDepreciation>
	s_cache = new CCache<Integer,MDepreciation>(Table_Name, 5);
	/**		Cache for type							*/
	private static CCache<String,MDepreciation>
	s_cache_forType = new CCache<String,MDepreciation>(Table_Name, Table_Name+"_DepreciationType", 5);
	
		
	/* Constrants */
	
	private static void addToCache(MDepreciation depr)
	{
		if (depr == null)
		{
			return ;
		}
		
		s_cache.put(depr.get_ID(), depr);
		String key = "" + depr.getA_Depreciation_ID();
		s_cache_forType.put(key, depr);
	}
 
	/**
	 * Get Depreciation method
	 * @param ctx
	 * @param A_Depreciation_ID depreciation id
	 */
	public static MDepreciation get(Properties ctx, int A_Depreciation_ID)
	{
		MDepreciation depr = s_cache.get(A_Depreciation_ID);
		if (depr != null)
		{
			return depr;
		}
		depr = new MDepreciation(ctx, A_Depreciation_ID, null);
		if (depr.get_ID() > 0)
		{
			addToCache(depr);
		}
		else
		{
			depr = null;
		}
		return depr;
	} // get

	protected MDepreciationExp[]	m_lines = null;
	
	public MDepreciationExp[] getLines (boolean requery)
	{
		if (m_lines != null) {
			set_TrxName(m_lines, get_TrxName());
			return m_lines;
		}
		List<MDepreciationExp> list = new Query(getCtx(), I_A_Depreciation_Exp.Table_Name, "A_Depreciation_ID=? And IsSelected = 'Y'", get_TrxName())
		.setParameters(getA_Depreciation_ID())
		.list();
		//
		m_lines = new MDepreciationExp[list.size()];
		list.toArray(m_lines);
		return m_lines;
	}

	//Implements DocAction
	protected void updateProcessed(boolean status) {
		String sql = "Update A_Depreciation_Exp set Processed = ? Where A_Depreciation_ID = ?";
		DB.executeUpdate(sql, new Object [] {status, getA_Depreciation_ID()}, true, get_TrxName());
	}
	
	protected String		m_processMsg = null;
	
	@Override
	public boolean processIt(String action, int AD_Window_ID) throws Exception {
		m_processMsg = null;
		DocumentEngine engine = new DocumentEngine (this, getDocStatus(), AD_Window_ID);
		return engine.processIt (action, getDocStatus());
	}


	@Override
	public String completeIt() {
		
		if (!MPeriod.isOpen(getCtx(), getDateAcct(), getAD_Org_ID()))
		{
			m_processMsg = "@PeriodClosed@";
			return DocAction.STATUS_Drafted;
		}
		
		
		setProcessed(true);
		updateProcessed(true);
		updateInfoAsset(true);
		return DocAction.STATUS_Completed;
	}



	@Override
	public boolean reActivateIt() {
		if (log.isLoggable(Level.INFO)) log.info(toString());
		
		if (!MPeriod.isOpen(getCtx(), getDateAcct(), getAD_Org_ID()))
		{
			m_processMsg = "@PeriodClosed@";
			return false;
		}
		if(!super.reActivate())
			return false;
		
		setProcessed(false);
		updateProcessed(false);
		updateInfoAsset(false);
		return true;
	}

	
	private void updateInfoAsset(boolean isComplete) {
		String sql = 
				"With t1 as "+
				"( "+
				" 	select A_Asset_ID, sum(Amount) Amount From A_Depreciation_Exp  "+
				" 	Where A_Depreciation_ID = ? And IsSelected = 'Y' "+
				" 	Group by A_Asset_ID "+
				"), "+
				"t2 as "+
				"( "+
				" 	select sum(Amount) Amount, A_Asset_ID From A_Depreciation_Exp f	 "+
				" 	Where exists  (Select 1 From t1 where f.A_Asset_ID = t1.A_Asset_ID)  "+
				" 	Group by A_Asset_ID "+
				"), "+
				"t3 as "+
				"( "+
				" 	select t1.A_Asset_ID, t2.Amount - t1.Amount Amount "+
				" 	From t2 inner join t1 On t2.A_Asset_ID = t1.A_Asset_ID "+
				") ";
		if (isComplete)
			sql = sql +	"Update A_Asset r set AccumulateAmt =(Select Amount From t2 where r.A_Asset_ID = t2.A_Asset_ID), "+
				"	RemainAmt = BaseAmtCurrent - (Select Amount From t2 where r.A_Asset_ID = t2.A_Asset_ID) "+
				"where exists (select 1 from t2 where r.A_Asset_ID = t2.A_Asset_ID)";
		else
			sql = sql +	"Update A_Asset r set AccumulateAmt =(Select Amount From t3 where r.A_Asset_ID = t3.A_Asset_ID), "+
					"	RemainAmt = BaseAmtCurrent - (Select Amount From t3 where r.A_Asset_ID = t3.A_Asset_ID) "+
					"where exists (select 1 from t3 where r.A_Asset_ID = t3.A_Asset_ID)";
		Object [] params = new Object [] {getA_Depreciation_ID()};
		DB.executeUpdate(sql, params, true, get_TrxName());		
	}


	@Override
	public String getProcessMsg() {
		if (m_processMsg != null) {
			setProcessed(false);
			
		}
		return m_processMsg;
	}

	@Override
	public void setProcessMsg(String text) {
		m_processMsg = text;
	}

	@Override
	public String getSummary() {
		
		return "";
	}

	@Override
	public int getAD_Window_ID() {
		// TODO Auto-generated method stub
		return 0;
	}
}
