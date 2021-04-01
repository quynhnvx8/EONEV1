/**
 * 
 */
package eone.base.process;

import org.compiere.util.Env;

import eone.base.model.X_AD_FieldSuggestion;
import eone.base.process.SvrProcess;

/**
 * @author hengsin
 *
 */
public class RejectFieldSuggestion extends SvrProcess {

	/**
	 * 
	 */
	public RejectFieldSuggestion() {
	}

	@Override
	protected void prepare() {
	}

	@Override
	protected String doIt() throws Exception {
		X_AD_FieldSuggestion suggestion = new X_AD_FieldSuggestion(Env.getCtx(), getRecord_ID(),get_TrxName());
		suggestion.setIsApproved(false);
		suggestion.setProcessed(true);
		suggestion.saveEx();
		
		return "Suggestion rejected";
	}

}
