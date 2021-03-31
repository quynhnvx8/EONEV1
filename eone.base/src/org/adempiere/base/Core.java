package org.adempiere.base;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;

import org.compiere.model.Callout;
import org.compiere.model.ModelValidator;
import org.compiere.util.CCache;
import org.idempiere.distributed.ICacheService;
import org.idempiere.distributed.IClusterService;
import org.idempiere.distributed.IMessageService;

import eone.base.process.ProcessCall;



public class Core {

	private static final CCache<String, IServiceReferenceHolder<IResourceFinder>> s_resourceFinderCache = new CCache<>(null, "IResourceFinder", 100, false);
	

	public static IResourceFinder getResourceFinder() {
		return new IResourceFinder() {

			public URL getResource(String name) {
				IServiceReferenceHolder<IResourceFinder> cache = s_resourceFinderCache.get(name);
				if (cache != null) {
					IResourceFinder service = cache.getService();
					if (service != null) {
						URL url = service.getResource(name);
						if (url!=null)
							return url;
					}
					s_resourceFinderCache.remove(name);
				}
				List<IServiceReferenceHolder<IResourceFinder>> f = Service.locator().list(IResourceFinder.class).getServiceReferences();
				for (IServiceReferenceHolder<IResourceFinder> finder : f) {
					IResourceFinder service = finder.getService();
					if (service != null) {
						URL url = service.getResource(name);
						if (url!=null) {
							s_resourceFinderCache.put(name, finder);
							return url;
						}
					}
				}
				return null;
			}
		};		
	}

	/**
	 *
	 * @param tableName
	 * @param columnName
	 * @return list of callout register for tableName.columnName
	 */
	private static final CCache<String, List<IServiceReferenceHolder<IColumnCalloutFactory>>> s_columnCalloutFactoryCache = new CCache<>(null, "List<IColumnCalloutFactory>", 100, false);

	
	public static List<IColumnCallout> findCallout(String tableName, String columnName) {
		List<IColumnCallout> list = new ArrayList<IColumnCallout>();
		
		String cacheKey = tableName + "." + columnName;
		List<IServiceReferenceHolder<IColumnCalloutFactory>> cache = s_columnCalloutFactoryCache.get(cacheKey);
		if (cache != null) {
			boolean staleReference = false;
			for (IServiceReferenceHolder<IColumnCalloutFactory> factory : cache) {
				IColumnCalloutFactory service = factory.getService();
				if (service != null) {
					IColumnCallout[] callouts = service.getColumnCallouts(tableName, columnName);
					if (callouts != null && callouts.length > 0) {
						for(IColumnCallout callout : callouts) {
							list.add(callout);
						}
					} else {						
						staleReference = true;
						break;
					}
				} else {
					staleReference = true;
					break;
				}
			}
			if (!staleReference)
				return list;
			else
				s_columnCalloutFactoryCache.remove(cacheKey);
		}
		
		List<IServiceReferenceHolder<IColumnCalloutFactory>> factories = Service.locator().list(IColumnCalloutFactory.class).getServiceReferences();
		List<IServiceReferenceHolder<IColumnCalloutFactory>> found = new ArrayList<>();
		if (factories != null) {
			for(IServiceReferenceHolder<IColumnCalloutFactory> factory : factories) {
				IColumnCalloutFactory service = factory.getService();
				if (service != null) {
					IColumnCallout[] callouts = service.getColumnCallouts(tableName, columnName);
					if (callouts != null && callouts.length > 0) {
						for(IColumnCallout callout : callouts) {
							list.add(callout);						
						}
						found.add(factory);
					}
				}
			}
			s_columnCalloutFactoryCache.put(cacheKey, found);
		}
		return list;
	}

	// IDEMPIERE-2732
	/**
	 *
	 * @param className
	 * @param method 
	 * @return callout for className
	 */
	private static final CCache<String, IServiceReferenceHolder<ICalloutFactory>> s_calloutFactoryCache = new CCache<>(null, "ICalloutFactory", 100, false);
	
	public static Callout getCallout(String className, String methodName) {
		String cacheKey = className + "::" + methodName;
		IServiceReferenceHolder<ICalloutFactory> cache = s_calloutFactoryCache.get(cacheKey);
		if (cache != null) {
			ICalloutFactory service = cache.getService();
			if (service != null) {
				Callout callout = service.getCallout(className, methodName);
				if (callout != null) {
					return callout;
				}
			}
			s_calloutFactoryCache.remove(cacheKey);
		}
		List<IServiceReferenceHolder<ICalloutFactory>> factories = Service.locator().list(ICalloutFactory.class).getServiceReferences();
		if (factories != null) {
			for(IServiceReferenceHolder<ICalloutFactory> factory : factories) {
				ICalloutFactory service = factory.getService();
				if (service != null) {
					Callout callout = service.getCallout(className, methodName);
					if (callout != null) {
						s_calloutFactoryCache.put(cacheKey, factory);
						return callout;
					}
				}
			}
		}
		return null;
	}

	private static final CCache<String, IServiceReferenceHolder<IProcessFactory>> s_processFactoryCache = new CCache<>(null, "IProcessFactory", 100, false);
	
	public static ProcessCall getProcess(String processId) {
		IServiceReferenceHolder<IProcessFactory> cache = s_processFactoryCache.get(processId);
		if (cache != null) {
			IProcessFactory service = cache.getService();
			if (service != null) {
				ProcessCall process = service.newProcessInstance(processId);
				if (process != null)
					return process;
			}
			s_processFactoryCache.remove(processId);
		}
		
		List<IServiceReferenceHolder<IProcessFactory>> factories = getProcessFactories();
		if (factories != null && !factories.isEmpty()) {
			for(IServiceReferenceHolder<IProcessFactory> factory : factories) {
				IProcessFactory service = factory.getService();
				if (service != null) {
					ProcessCall process = service.newProcessInstance(processId);
					if (process != null) {
						s_processFactoryCache.put(processId, factory);
						return process;
					}
				}
			}
		}
		return null; 		
	}

	private static List<IServiceReferenceHolder<IProcessFactory>> getProcessFactories() {
		List<IServiceReferenceHolder<IProcessFactory>> factories = null;
		int maxIterations = 5;
		int waitMillis = 1000;
		int iterations = 0;
		boolean foundDefault = false;
		while (true) {
			factories = Service.locator().list(IProcessFactory.class).getServiceReferences();
			if (factories != null && !factories.isEmpty()) {
				for(IServiceReferenceHolder<IProcessFactory> factory : factories) {
					// wait until DefaultProcessFactory is loaded
					IProcessFactory service = factory.getService();
					if (service instanceof DefaultProcessFactory) {
						foundDefault = true;
						break;
					}
				}
			}
			iterations++;
			if (foundDefault || iterations >= maxIterations) {
				break;
			}
			try {
				Thread.sleep(waitMillis);
			} catch (InterruptedException e) {
			}
		}
		return factories;
	}

private static final CCache<String, IServiceReferenceHolder<IModelValidatorFactory>> s_modelValidatorFactoryCache = new CCache<>(null, "IModelValidatorFactory", 100, false);
	
	/**
	 *
	 * @param validatorId Java class name or equinox extension Id
	 * @return ModelValidator instance of null if validatorId not found
	 */
	public static ModelValidator getModelValidator(String validatorId) {
		IServiceReferenceHolder<IModelValidatorFactory> cache = s_modelValidatorFactoryCache.get(validatorId);
		if (cache != null) {
			IModelValidatorFactory service = cache.getService();
			if (service != null) {
				ModelValidator validator = service.newModelValidatorInstance(validatorId);
				if (validator != null)
					return validator;
			}
			s_modelValidatorFactoryCache.remove(validatorId);
		}
		List<IServiceReferenceHolder<IModelValidatorFactory>> factoryList = Service.locator().list(IModelValidatorFactory.class).getServiceReferences();
		if (factoryList != null) {
			for(IServiceReferenceHolder<IModelValidatorFactory> factory : factoryList) {
				IModelValidatorFactory service = factory.getService();
				if (service != null) {
					ModelValidator validator = service.newModelValidatorInstance(validatorId);
					if (validator != null) {
						s_modelValidatorFactoryCache.put(validatorId, factory);
						return validator;
					}
				}
			}
		}
		
		return null;
	}

	private static IServiceReferenceHolder<IKeyStore> s_keystoreServiceReference = null;
	
	/**
	 * 
	 * @return {@link IKeyStore}
	 */
	public static IKeyStore getKeyStore(){
		IKeyStore keystoreService = null;
		if (s_keystoreServiceReference != null) {
			keystoreService = s_keystoreServiceReference.getService();
			if (keystoreService != null)
				return keystoreService;
		}
		IServiceReferenceHolder<IKeyStore> serviceReference = Service.locator().locate(IKeyStore.class).getServiceReference();
		if (serviceReference != null) {
			keystoreService = serviceReference.getService();
			s_keystoreServiceReference = serviceReference;
		}
		return keystoreService;
	}
	
	
	private final static CCache<String, IServiceReferenceHolder<ScriptEngineFactory>> s_scriptEngineFactoryCache = new CCache<>(null, "ScriptEngineFactory", 100, false);
	
	/** Get script engine 
	 * 
	 * @param engineName
	 * @return ScriptEngine found, or null
	 */
	public static ScriptEngine getScriptEngine(String engineName)
	{
		ScriptEngineManager manager = new ScriptEngineManager(Core.class.getClassLoader());
		ScriptEngine engine = manager.getEngineByName(engineName);
		if (engine != null)
			return engine;
		
		IServiceReferenceHolder<ScriptEngineFactory> cache = s_scriptEngineFactoryCache.get(engineName);
		if (cache != null) {
			ScriptEngineFactory service = cache.getService();
			if (service != null)
				return service.getScriptEngine();
			s_scriptEngineFactoryCache.remove(engineName);
		}
		List<IServiceReferenceHolder<ScriptEngineFactory>> factoryList = Service.locator().list(ScriptEngineFactory.class).getServiceReferences();
		if (factoryList != null) {
			for(IServiceReferenceHolder<ScriptEngineFactory> factory : factoryList) {
				ScriptEngineFactory service = factory.getService();
				if (service != null) {
					for (String name : service.getNames()) {
						if (engineName.equals(name)) {
							s_scriptEngineFactoryCache.put(engineName, factory);
							return service.getScriptEngine();
						}
					}
				}
			}
		}
		
		return null;
	}
	
	
private static IServiceReferenceHolder<IProductPricingFactory> s_productPricingFactoryCache = null;
	
	/**
	 * get ProductPricing instance
	 * 
	 * @return instance of the IProductPricing or null
	 */
	public static synchronized IProductPricing getProductPricing() {
		if (s_productPricingFactoryCache != null) {
			IProductPricingFactory service = s_productPricingFactoryCache.getService();
			if (service != null) {
				IProductPricing myProductPricing = service.newProductPricingInstance();
				if (myProductPricing != null)
					return myProductPricing;
			}
			s_productPricingFactoryCache = null;
		}
		IServiceReferenceHolder<IProductPricingFactory> factoryReference = Service.locator().locate(IProductPricingFactory.class).getServiceReference();
		if (factoryReference != null) {
			IProductPricingFactory service = factoryReference.getService();
			if (service != null) {
				IProductPricing myProductPricing = service.newProductPricingInstance();
				if (myProductPricing != null) {
					s_productPricingFactoryCache = factoryReference;
					return myProductPricing;
				}
			}
		}

		return null;
	}
	
	

	private static IServiceReferenceHolder<ICacheService> s_cacheServiceReference = null;
	
	/**
	 * 
	 * @return {@link ICacheService}
	 */
	public static synchronized ICacheService getCacheService() {
		ICacheService cacheService = null;
		if (s_cacheServiceReference != null) {
			cacheService = s_cacheServiceReference.getService();
			if (cacheService != null)
				return cacheService;
		}
		IServiceReferenceHolder<ICacheService> serviceReference = Service.locator().locate(ICacheService.class).getServiceReference();
		if (serviceReference != null) {
			cacheService = serviceReference.getService();
			s_cacheServiceReference = serviceReference;
		}
		return cacheService;
	}
	
	private static IServiceReferenceHolder<IClusterService> s_clusterServiceReference = null;
	
	/**
	 * 
	 * @return {@link IClusterService}
	 */
	public static synchronized IClusterService getClusterService() {
		IClusterService clusterService = null;
		if (s_clusterServiceReference != null) {
			clusterService = s_clusterServiceReference.getService();
			if (clusterService != null)
				return clusterService;
		}
		IServiceReferenceHolder<IClusterService> serviceReference = Service.locator().locate(IClusterService.class).getServiceReference();
		if (serviceReference != null) {
			clusterService = serviceReference.getService();
			s_clusterServiceReference = serviceReference;
		}
		return clusterService;
	}
	
	private static IServiceReferenceHolder<IMessageService> s_messageServiceReference = null;
	
	/**
	 * 
	 * @return {@link IMessageService}
	 */
	public static synchronized IMessageService getMessageService() {
		IMessageService messageService = null;
		if (s_messageServiceReference != null) {
			messageService = s_messageServiceReference.getService();
			if (messageService != null)
				return messageService;
		}
		IServiceReferenceHolder<IMessageService> serviceReference = Service.locator().locate(IMessageService.class).getServiceReference();
		if (serviceReference != null) {
			messageService = serviceReference.getService();
			s_messageServiceReference = serviceReference;
		}
		return messageService;
	}
	
}
