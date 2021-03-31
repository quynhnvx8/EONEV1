
package org.adempiere.base;

import org.osgi.framework.ServiceReference;

public interface IServiceReferenceHolder<T> {


	public T getService();
	

	public ServiceReference<T> getServiceReference();
}
