/**
* RhoconnectImpl.java 
*/
package com.rhomobile.rhoconnect;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class RhoconnectDispatcher implements ApplicationContextAware {
	private static final Logger logger = Logger.getLogger(RhoconnectDispatcher.class);	

    private ApplicationContext ctx = null;

//    public ApplicationContext getApplicationContext() {
//		return ctx;
//	}
    
	@Override
	public void setApplicationContext(ApplicationContext ctx) throws BeansException {
		this.ctx = ctx;		
	}
	
    private Map<String, Object> maps;
    
    public Map<String, Object> getMaps() {
		return maps;
	}

	public void setMaps(Map<String, Object> maps) {
		this.maps = maps;
	}

    public Map<String, Object> query_objects(String resource, String partition) {
    	RhoconnectResource service = getService(resource);
    	if (service != null) {
    		return service.rhoconnectQuery(partition);    		
    	} else {
			logger.info("RhoconnectImpl#query_objects: resource unknown: " + resource);    		
    	}
		return null;
    }

    public Integer create(String resource, String partition, Map<String, Object> attributes) {
    	RhoconnectResource service = getService(resource);
    	if (service != null) { // => contactService;
    		return service.rhoconnectCreate(partition, attributes);
    	} else {
			logger.info("RhoconnectImpl#create: resource unknown: " + resource);    		
        }
        return null;
    }

    public Integer update(String resource, String partition, Map<String, Object> attributes) {
    	RhoconnectResource service = getService(resource);
    	if (service != null) {
    		return service.rhoconnectUpdate(partition, attributes);
    	} else {
			logger.info("RhoconnectImpl#update: resource unknown: " + resource);    		
        }
        return null;
    }

    public Integer delete(String resource, String partition, Map<String, Object> attributes) {
    	RhoconnectResource service = getService(resource);
    	if (service != null) {
    		return service.rhoconnetDelete(partition, attributes);
    	} else {
			logger.info("RhoconnectImpl#delete: resource unknown: " + resource);    		
        }
        return null;
    }
    
	private RhoconnectResource getService(String resource) {
		String id = resource.toLowerCase() + "Service"; // "Contact" -> "contactService"
		try {			
			RhoconnectResource bean	= ctx.getBean(id, RhoconnectResource.class);
			logger.info("RhoconnectImpl#getService: partition = " + bean.getPartition());
			return bean; 

		} catch (Exception e) {
			logger.info("RhoconnectImpl#getService: bean not found: " + id);
			logger.info("RhoconnectImpl#getService: " + e.getMessage());			
			// mapping: resource -> bean 
			return (RhoconnectResource)maps.get(resource);
		}
    }

}
