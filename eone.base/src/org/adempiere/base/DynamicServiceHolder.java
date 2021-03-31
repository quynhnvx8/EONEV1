package org.adempiere.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;

/**
 * @author Quynhnv.x8: mod
 *
 */
public class DynamicServiceHolder<T> implements IServiceHolder<T>, IServicesHolder<T> {

	private ServiceTracker<T, T> serviceTracker;

	/**
	 * @param tracker
	 */
	public DynamicServiceHolder(ServiceTracker<T, T> tracker) {
		serviceTracker = tracker;
		if (serviceTracker.getTrackingCount() == -1)
			serviceTracker.open();
	}

	@Override
	public T getService() {
		T service = serviceTracker.getService();
		return service;
	}

	@Override
	public List<T> getServices() {
		List<T> services = new ArrayList<T>();
		ServiceReference<T>[] objects = serviceTracker.getServiceReferences();
		List<ServiceReference<T>> references = new ArrayList<ServiceReference<T>>();
		if (objects != null && objects.length > 0) {
			references = Arrays.asList(objects);
		}
		if (references.size() > 1)
			Collections.sort(references, ServiceRankingComparator.INSTANCE);
		Collections.sort(references, Collections.reverseOrder());
		for(ServiceReference<T> reference : references) {
			services.add(serviceTracker.getService(reference));
		}
		return services;
	}

	@Override
	public IServiceReferenceHolder<T> getServiceReference() {
		ServiceReference<T> v = serviceTracker.getServiceReference();
		if (v != null)
			return new DynamicServiceReference<T>(serviceTracker, v);
		return null;
	}

	@Override
	public List<IServiceReferenceHolder<T>> getServiceReferences() {
		List<IServiceReferenceHolder<T>> services = new ArrayList<>();
		ServiceReference<T>[] objects = serviceTracker.getServiceReferences();
		List<ServiceReference<T>> references = new ArrayList<ServiceReference<T>>();
		if (objects != null && objects.length > 0) {
			references = Arrays.asList(objects);
		}
		if (references.size() > 1)
			Collections.sort(references, ServiceRankingComparator.INSTANCE);
		for(ServiceReference<T> reference : references) {
			services.add(new DynamicServiceReference<T>(serviceTracker, reference));
		}
		return services;
	}	
}
