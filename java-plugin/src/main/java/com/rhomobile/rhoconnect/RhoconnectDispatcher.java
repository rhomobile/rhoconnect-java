/**
* RhoconnectDispatcher.java 
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
    
	@Override
	public void setApplicationContext(ApplicationContext ctx) throws BeansException {
		this.ctx = ctx;		
	}

	/*
	 * Mapping resource (model) name to service bean.	 
	 * You need to add to your spring configuration file property 'maps' and set mapping directly:
	  
	 * <bean id="dispatcher" class = "com.rhomobile.rhoconnect.RhoconnectDispatcher">
 	 *   <property name="maps">
	 *	   <map>
	 *	     <entry key="Contact" value-ref="contactResource" />
	 *       <!-- yet another entry ... -->
	 *	   </map>
	 *   </property>
	 * </bean>	 
	 */
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
			logger.error("RhoconnectImpl#query_objects: resource unknown: " + resource);    		
    	}
		return null;
    }

    public Integer create(String resource, String partition, Map<String, Object> attributes) {
    	RhoconnectResource service = getService(resource);
    	if (service != null) {
    		return service.rhoconnectCreate(partition, attributes);
    	} else {
			logger.error("RhoconnectImpl#create: resource unknown: " + resource);    		
        }
        return null;
    }

    public Integer update(String resource, String partition, Map<String, Object> attributes) {
    	RhoconnectResource service = getService(resource);
    	if (service != null) {
    		return service.rhoconnectUpdate(partition, attributes);
    	} else {
			logger.error("RhoconnectImpl#update: resource unknown: " + resource);    		
        }
        return null;
    }

    public Integer delete(String resource, String partition, Map<String, Object> attributes) {
    	RhoconnectResource service = getService(resource);
    	if (service != null) {
    		return service.rhoconnetDelete(partition, attributes);
    	} else {
			logger.error("RhoconnectImpl#delete: resource unknown: " + resource);    		
        }
        return null;
    }
    
	private RhoconnectResource getService(String resource) {
		String id = resource.toLowerCase() + "ServiceImpl"; // "Contact" -> "contactServiceImpl"
		try {
			Map<String, RhoconnectResource> map = ctx.getBeansOfType(RhoconnectResource.class);
			RhoconnectResource bean = map.get(id);
			return bean; 
		} catch (Exception e) {
			logger.error("RhoconnectImpl#getService: bean not found: " + id);
			logger.error("RhoconnectImpl#getService: " + e.getMessage());			
			// direct mapping: resource -> bean
			if (maps != null)
				return (RhoconnectResource)maps.get(resource);
		}
		return null;
    }
}
