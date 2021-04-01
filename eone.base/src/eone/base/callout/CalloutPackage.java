package eone.base.callout;

import java.util.Properties;

import eone.base.model.CalloutEngine;
import eone.base.model.GridField;
import eone.base.model.GridTab;

/**
 * User: cruiz - idalica
 * Date: Apr 16, 2008
 * <p/>
 * Packages - Callouts
 */
public class CalloutPackage extends CalloutEngine
{
    public String afterShipper(Properties ctx, int windowNo, GridTab mTab, GridField mField, Object value, Object oldValue)
    {
        if (isCalloutActive())
            return "";

       
        return "";
    }

    public String afterShipperSetDefaults(Properties ctx, int windowNo, GridTab mTab, GridField mField, Object value, Object oldValue)
    {
        if (isCalloutActive())
            return "";

        return "";
    }
    
}