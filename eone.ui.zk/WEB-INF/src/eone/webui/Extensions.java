/******************************************************************************
 * This file is part of Adempiere ERP Bazaar                                  *
 * http://www.adempiere.org                                                   *
 *                                                                            *
 * Copyright (C) Jorg Viola			                                          *
 * Copyright (C) Contributors												  *
 *                                                                            *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 *                                                                            *
 * Contributors:                                                              *
 * - Heng Sin Low                                                             *
 *****************************************************************************/
package eone.webui;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.adempiere.base.IServiceReferenceHolder;
import org.adempiere.base.Service;
import org.adempiere.base.ServiceQuery;
import org.compiere.grid.ICreateFrom;
import org.compiere.grid.ICreateFromFactory;
import org.compiere.util.CCache;
import org.zkoss.zk.ui.Component;

import eone.base.model.GridTab;
import eone.webui.apps.IChartRendererService;
import eone.webui.apps.IProcessParameterListener;
import eone.webui.factory.IDashboardGadgetFactory;
import eone.webui.factory.IFormFactory;
import eone.webui.panel.ADForm;

/**
 *
 * @author viola
 * @author hengsin
 *
 */
public class Extensions {

	/**
	 *
	 * @param formId Java class name or equinox extension Id
	 * @return IFormController instance or null if formId not found
	 */
	private final static CCache<String, IServiceReferenceHolder<IFormFactory>> s_formFactoryCache = new CCache<>(null, "IFormFactory", 100, false);
	
	public static ADForm getForm(String formId) {
		IServiceReferenceHolder<IFormFactory> cache = s_formFactoryCache.get(formId);
		if (cache != null) {
			IFormFactory service = cache.getService();
			if (service != null) {
				ADForm form = service.newFormInstance(formId);
				if (form != null)
					return form;
			}
			s_formFactoryCache.remove(formId);
		}
		List<IServiceReferenceHolder<IFormFactory>> factories = Service.locator().list(IFormFactory.class).getServiceReferences();
		if (factories != null) {
			for(IServiceReferenceHolder<IFormFactory> factory : factories) {
				IFormFactory service = factory.getService();
				if (service != null) {
					ADForm form = service.newFormInstance(formId);
					if (form != null) {
						s_formFactoryCache.put(formId, factory);
						return form;
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @param processClass
	 * @param columnName
	 * @return list of parameter listener
	 */
	private final static CCache<String, List<IServiceReferenceHolder<IProcessParameterListener>>> s_processParameterListenerCache = new CCache<>(null, "List<IProcessParameterListener>", 100, false);
	
	public static List<IProcessParameterListener> getProcessParameterListeners(String processClass, String columnName) {
		String cacheKey = processClass + "|" + columnName;
		List<IServiceReferenceHolder<IProcessParameterListener>> listeners = s_processParameterListenerCache.get(cacheKey);
		if (listeners != null)
			return listeners.stream().filter(e -> e.getService() != null).map(e -> e.getService()).collect(Collectors.toCollection(ArrayList::new));
		
		ServiceQuery query = new ServiceQuery();
		query.put("ProcessClass", processClass);
		if (columnName != null)
			query.put("|(ColumnName", columnName+")(ColumnName="+columnName+",*)(ColumnName="+"*,"+columnName+",*)(ColumnName=*,"+columnName+")");
		listeners = Service.locator().list(IProcessParameterListener.class, null, query).getServiceReferences();
		if (listeners == null)
			listeners = new ArrayList<>();
		s_processParameterListenerCache.put(cacheKey, listeners);
		return listeners.stream().filter(e -> e.getService() != null).map(e -> e.getService()).collect(Collectors.toCollection(ArrayList::new));
	}
	
	private final static CCache<String, IServiceReferenceHolder<ICreateFromFactory>> s_createFromFactoryCache = new CCache<>(null, "ICreateFromFactory", 100, false);
	
	/**
	 * 
	 * @param mTab
	 * @return ICreateFrom instance
	 */
	public static ICreateFrom getCreateFrom(GridTab mTab) {
		ICreateFrom createFrom = null;
		String cacheKey = Integer.toString(mTab.getAD_Tab_ID());
		IServiceReferenceHolder<ICreateFromFactory> cache = s_createFromFactoryCache.get(cacheKey);
		if (cache != null) {
			ICreateFromFactory service = cache.getService();
			if (service != null) {
				createFrom = service.create(mTab);
				if (createFrom != null) {
					return createFrom;
				}
			}
			s_createFromFactoryCache.remove(cacheKey);
		}
		
		List<IServiceReferenceHolder<ICreateFromFactory>> factories = Service.locator().list(ICreateFromFactory.class).getServiceReferences();
		for (IServiceReferenceHolder<ICreateFromFactory> factory : factories) 
		{
			ICreateFromFactory service = factory.getService();
			if (service != null) {
				createFrom = service.create(mTab);
				if (createFrom != null) {
					s_createFromFactoryCache.put(cacheKey, factory);
					return createFrom;
				}
			}
		}
		
		return null;
	}
	
	
	private static final CCache<String, IServiceReferenceHolder<IDashboardGadgetFactory>> s_dashboardGadgetFactoryCache = new CCache<>(null, "IDashboardGadgetFactory", 100, false);
	
	/**
	 * 
	 * @param url
	 * @param parent
	 * @return Gadget component
	 */
	public static final Component getDashboardGadget(String url, Component parent) {
		IServiceReferenceHolder<IDashboardGadgetFactory> cache = s_dashboardGadgetFactoryCache.get(url);
		if (cache != null) {
			IDashboardGadgetFactory service = cache.getService();
			if (service != null) {
				Component component = service.getGadget(url,parent);
	            if(component != null)
	            	return component;
			}
			s_dashboardGadgetFactoryCache.remove(url);
		}
		
		List<IServiceReferenceHolder<IDashboardGadgetFactory>> f = Service.locator().list(IDashboardGadgetFactory.class).getServiceReferences();
        for (IServiceReferenceHolder<IDashboardGadgetFactory> factory : f) {
        	IDashboardGadgetFactory service = factory.getService();
        	if (service != null) {
	        	Component component = service.getGadget(url,parent);
	            if(component != null)
	            	return component;
        	}
        }
        
        return null;
	}
	
	/**
	 * 
	 * @return list of {@link IChartRendererService}
	 */
	public static final List<IChartRendererService> getChartRendererServices() {
		return Service.locator().list(IChartRendererService.class).getServices();
	}	
}
