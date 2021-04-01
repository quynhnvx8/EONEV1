
package eone.base.callout;

import java.util.Properties;

import org.compiere.util.Env;

import eone.base.model.CalloutEngine;
import eone.base.model.GridField;
import eone.base.model.GridTab;
import eone.base.model.MSalaryTableLine;
import eone.base.model.X_HR_SalaryExtra;
import eone.base.model.X_HR_SalaryTableLine;

//eone.base.model.CalloutEmployee.fillSalaryExtra

public class CalloutEmployee extends CalloutEngine
{

	

	public String fillSalaryExtra (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if (isCalloutActive())	
			return "";

		Object salaryLine = mTab.getValue("HR_SalaryTableLine_ID");
		int HR_SalaryTableLine_ID = 0;
		if (salaryLine != null) {
			HR_SalaryTableLine_ID = Integer.parseInt(salaryLine.toString());
			MSalaryTableLine line = MSalaryTableLine.get(ctx, HR_SalaryTableLine_ID, null);
			mTab.setValue(X_HR_SalaryExtra.COLUMNNAME_TypeExtra, line.getTypeExtra());
			mTab.setValue(X_HR_SalaryExtra.COLUMNNAME_Percent, line.getPercent());
			mTab.setValue(X_HR_SalaryExtra.COLUMNNAME_FormulaSetup, line.getFormulaSetup());
			
		} else {
			mTab.setValue(X_HR_SalaryExtra.COLUMNNAME_Percent, null);
			mTab.setValue(X_HR_SalaryExtra.COLUMNNAME_FormulaSetup, null);
		}
		return "";
	}

	public String fillRate (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if (isCalloutActive())	
			return "";

		Object TypeExtra = mTab.getValue("TypeExtra");
		if (TypeExtra != null ) {
			if (X_HR_SalaryTableLine.TYPEEXTRA_ByRate.equalsIgnoreCase(TypeExtra.toString())) {
				mTab.setValue(X_HR_SalaryTableLine.COLUMNNAME_Percent, null);
			} else {
				mTab.setValue(X_HR_SalaryTableLine.COLUMNNAME_Percent, Env.ONE);
			}
			
		} 
		
		return "";
	}
}	//	CalloutInOut