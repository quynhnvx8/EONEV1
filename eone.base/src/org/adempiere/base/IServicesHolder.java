
package org.adempiere.base;

import java.util.List;


public interface IServicesHolder<T> {
	

	public List<T> getServices();

	public List<IServiceReferenceHolder<T>> getServiceReferences();
}
