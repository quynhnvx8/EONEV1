
package org.adempiere.base.equinox;

import org.adempiere.base.ServiceQuery;


public class EquinoxExtensionLocator {
	
	private final static EquinoxExtensionLocator INSTANCE = new EquinoxExtensionLocator(); 

	private EquinoxExtensionLocator() {}
	
	public static EquinoxExtensionLocator instance() {
		return INSTANCE;
	}
	

	public <T> EquinoxExtensionHolder<T> list(Class<T> type) {
		return list(type, type.getName());
	}
	

	public <T> EquinoxExtensionHolder<T> list(Class<T> type, String extensionPointId) {
		ExtensionList<T> list = new ExtensionList<T>(type, extensionPointId);
		return new EquinoxExtensionHolder<T>(list);
	}


	public <T> EquinoxExtensionHolder<T> list(Class<T> type, ServiceQuery query) {
		return list(type, type.getName(), null, query);
	}


	public <T> EquinoxExtensionHolder<T> list(Class<T> type, String extensionId, ServiceQuery query) {
		ExtensionList<T> list = new ExtensionList<T>(type, null, extensionId, query);
		return new EquinoxExtensionHolder<T>(list);
	}
	

	public <T> EquinoxExtensionHolder<T> list(Class<T> type, String extensionPointId, String extensionId,
			ServiceQuery query) {
		ExtensionList<T> list = new ExtensionList<T>(type, extensionPointId, extensionId, query);
		return new EquinoxExtensionHolder<T>(list);
	}
		

	public <T> EquinoxExtensionHolder<T> locate(Class<T> type) {
		return locate(type, type.getName());
	}


	public <T> EquinoxExtensionHolder<T> locate(Class<T> type, String extensionPointId) {
		ExtensionList<T> list = new ExtensionList<T>(type, extensionPointId);
		return new EquinoxExtensionHolder<T>(list);
	}
	

	public <T> EquinoxExtensionHolder<T> locate(Class<T> type, ServiceQuery query) {
		return locate(type, type.getName(), null, query);
	}


	public <T> EquinoxExtensionHolder<T> locate(Class<T> type, String extensionId, ServiceQuery query) {
		ExtensionList<T> list = new ExtensionList<T>(type, null, extensionId, query);
		return new EquinoxExtensionHolder<T>(list);
	}
	

	public <T> EquinoxExtensionHolder<T> locate(Class<T> type, String extensionPointId, String extensionId, ServiceQuery query) {
		ExtensionList<T> list = new ExtensionList<T>(type, extensionPointId, extensionId, query);
		return new EquinoxExtensionHolder<T>(list);
	}	
}
