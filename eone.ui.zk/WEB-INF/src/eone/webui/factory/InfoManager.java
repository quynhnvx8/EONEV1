
package eone.webui.factory;

import java.util.ArrayList;
import java.util.List;

import org.adempiere.base.IServiceReferenceHolder;
import org.adempiere.base.Service;
import org.compiere.util.CCache;
import org.osgi.framework.Constants;

import eone.base.model.GridField;
import eone.base.model.Lookup;
import eone.base.model.MLookup;
import eone.webui.info.InfoWindow;
import eone.webui.panel.InfoPanel;


public class InfoManager
{
	private final static CCache<Long, IServiceReferenceHolder<IInfoFactory>> s_infoFactoryCache = new CCache<Long, IServiceReferenceHolder<IInfoFactory>>(null, "IInfoFactory", 10, false);
	
	public static InfoPanel create (int WindowNo,
            String tableName, String keyColumn, String value,
            boolean multiSelection, String whereClause, boolean lookup)
    {
		InfoPanel info = null;
		
		List<Long> visitedIds = new ArrayList<Long>();
		if (!s_infoFactoryCache.isEmpty()) {
			Long[] keys = s_infoFactoryCache.keySet().toArray(new Long[0]);
			for (Long key : keys) {
				IServiceReferenceHolder<IInfoFactory> serviceReference = s_infoFactoryCache.get(key);
				if (serviceReference != null) {
					IInfoFactory service = serviceReference.getService();
					if (service != null) {
						visitedIds.add(key);
						info = service.create(WindowNo, tableName, keyColumn, value, multiSelection, whereClause, 0, lookup);
						if (info != null)
							return info;
					} else {
						s_infoFactoryCache.remove(key);
					}
				}
			}
		}
		        
		List<IServiceReferenceHolder<IInfoFactory>> serviceReferences = Service.locator().list(IInfoFactory.class).getServiceReferences();
		for(IServiceReferenceHolder<IInfoFactory> serviceReference : serviceReferences)
		{
			Long serviceId = (Long) serviceReference.getServiceReference().getProperty(Constants.SERVICE_ID);
			if (visitedIds.contains(serviceId))
				continue;
			IInfoFactory service = serviceReference.getService();
			if (service != null)
			{
				s_infoFactoryCache.put(serviceId, serviceReference);
				info = service.create(WindowNo, tableName, keyColumn, value, multiSelection, whereClause, 0, lookup);
				if (info != null)
					break;
			}
		}
        return info;
    }

	public static InfoPanel create(Lookup lookup, GridField field, String tableName,
			String keyColumn, String queryValue, boolean multiSelection,
			String whereClause)
	{
		InfoPanel ip = null;
		int AD_InfoWindow_ID = 0;
		if (lookup instanceof MLookup)
		{
			AD_InfoWindow_ID  = ((MLookup)lookup).getAD_InfoWindow_ID();
		}
		
		List<Long> visitedIds = new ArrayList<Long>();
		if (!s_infoFactoryCache.isEmpty()) {
			Long[] keys = s_infoFactoryCache.keySet().toArray(new Long[0]);
			for (Long key : keys) {
				IServiceReferenceHolder<IInfoFactory> serviceReference = s_infoFactoryCache.get(key);
				if (serviceReference != null) {
					IInfoFactory service = serviceReference.getService();
					if (service != null) {
						visitedIds.add(key);
						ip = service.create(lookup, field, tableName, keyColumn, queryValue, false, whereClause, AD_InfoWindow_ID);
						if (ip != null)
							return ip;
					} else {
						s_infoFactoryCache.remove(key);
					}
				}
			}
		}
				
		List<IServiceReferenceHolder<IInfoFactory>> serviceReferences = Service.locator().list(IInfoFactory.class).getServiceReferences();
		for(IServiceReferenceHolder<IInfoFactory> serviceReference : serviceReferences)
		{
			Long serviceId = (Long) serviceReference.getServiceReference().getProperty(Constants.SERVICE_ID);
			if (visitedIds.contains(serviceId))
				continue;
			IInfoFactory service = serviceReference.getService();
			if (service != null)
			{
				s_infoFactoryCache.put(serviceId, serviceReference);
				ip = service.create(lookup, field, tableName, keyColumn, queryValue, false, whereClause, AD_InfoWindow_ID);
				if (ip != null)
					break;
			}
		}
		return ip;
	}
	
	public static InfoWindow create (int AD_InfoWindow_ID)
    {
		InfoWindow info = null;

        List<Long> visitedIds = new ArrayList<Long>();
		if (!s_infoFactoryCache.isEmpty()) {
			Long[] keys = s_infoFactoryCache.keySet().toArray(new Long[0]);
			for (Long key : keys) {
				IServiceReferenceHolder<IInfoFactory> serviceReference = s_infoFactoryCache.get(key);
				if (serviceReference != null) {
					IInfoFactory service = serviceReference.getService();
					if (service != null) {
						visitedIds.add(key);
						info = service.create(AD_InfoWindow_ID);
						if (info != null)
							return info;
					} else {
						s_infoFactoryCache.remove(key);
					}
				}
			}
		}
		
		List<IServiceReferenceHolder<IInfoFactory>> serviceReferences = Service.locator().list(IInfoFactory.class).getServiceReferences();
		for(IServiceReferenceHolder<IInfoFactory> serviceReference : serviceReferences)
		{
			Long serviceId = (Long) serviceReference.getServiceReference().getProperty(Constants.SERVICE_ID);
			if (visitedIds.contains(serviceId))
				continue;
			IInfoFactory service = serviceReference.getService();
			if (service != null)
			{
				s_infoFactoryCache.put(serviceId, serviceReference);
				info = service.create(AD_InfoWindow_ID);
				if (info != null)
					break;
			}
		}
        return info;
    }
}
