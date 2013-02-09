package com.rhomobile.rhoconnect;

import java.util.*;

import org.apache.log4j.Logger;
import org.apache.commons.beanutils.PropertyUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


import com.msi.rhoconnect.api.PluginResource;
import com.msi.rhoconnect.api.SystemResource;
import com.sun.jersey.api.client.ClientResponse;

public class RhoconnectClient {
	private static final Logger logger = Logger.getLogger(RhoconnectClient.class);	
	private static final String RhoconnectControllerName = 
		"com.rhomobile.rhoconnect.controller.RhoconnectController";
	
	private String endpointUrl;
	private String appEndpoint;
    private String apiToken;
    private String partition;
    private String login;
    
    public String getEndpointUrl() {
		return endpointUrl;
	}
    public void setEndpointUrl(String endpointUrl) {
		this.endpointUrl = endpointUrl;
	}
 
	public String getAppEndpoint() {
		return appEndpoint;
	}
	public void setAppEndpoint(String appEndpoint) {
		this.appEndpoint = appEndpoint;
	}
	
    public String getApiToken() {
		return apiToken;
	}
	public void setApiToken(String apiToken) {
		this.apiToken = apiToken;
	}
	
    public String getPartition() {
    	return partition;
    }
    public void setPartion(String partition) {
		this.partition = partition;
	}
    
    public void setUserName(String userName) {
    	this.login = userName;
    }
    
	public void setAppEndpoint() {
		ClientResponse response = SystemResource.setAppserver(endpointUrl, apiToken, appEndpoint);
		if(response.getStatus() != 200) {
			logger.fatal("Failed to set appEndpoint for url " + endpointUrl);
			logger.fatal("apiToken: " + apiToken);
			logger.fatal("appEndpoint: " + appEndpoint);
			// TODO: raise exception! 
		}
	}

	public void setAppEndpoint(String endpoint, String appEndpoint, String api_token) {
		endpointUrl = endpoint;
		this.appEndpoint = appEndpoint;
	    apiToken = api_token;
	    setAppEndpoint();
	}

	public boolean notifyOnCreate(String sourceName, Object objId, Object object) {
		// Ignore notification calls from RhoconnectController
		if (stackTraceInclude(RhoconnectControllerName)) {
			logger.info("Notifications from RhoconnectController ignored");
			return true;
		}		

		JSONObject data = new JSONObject();;
		data.put(objId, modelToJSONObject(object)); // => { objId => { object properties } }
		ClientResponse response = 
				PluginResource.pushObjects(endpointUrl, apiToken, partition, sourceName, data);

		logger.debug("RhoconnectClient#notifyOnCreate: source/partition: " + sourceName + '/' +	partition);
		logger.debug("RhoconnectClient#notifyOnCreate: data: " + data.toString());
		logger.debug("RhoconnectClient#notifyOnCreate: getStatus: " + response.getStatus());
		
		return(response.getStatus() == 200);
	}	
	
	public boolean notifyOnUpdate(String sourceName, Object objId, Object object) {
		return notifyOnCreate(sourceName, objId, object);
	}	
	
	public boolean notifyOnDelete(String sourceName, Object objId) {
		// Ignore notification calls from RhoconnectController
		if (stackTraceInclude(RhoconnectControllerName)) {
			logger.info("Notifications from RhoconnectController ignored");
			return true;
		}	
        
		JSONArray data = new JSONArray();
		data.add(objId); // => [ objId ]
		ClientResponse response = 
				PluginResource.deleteObjects(endpointUrl, apiToken, partition, sourceName, data);

		logger.debug("RhoconnectClient#notifyOnDelete: source/partition: " + sourceName + '/' +	partition);
		logger.debug("RhoconnectClient#notifyOnDelete: data: " + data.toString());
		logger.debug("RhoconnectClient#notifyOnDelete: getStatus: " + response.getStatus());
		
		return(response.getStatus() == 200);		
	}

	// Helper methods
	// Convert model to JSONObject: { prop_name1 => prop_value1, ... }
	private static JSONObject modelToJSONObject(Object model) {
		JSONObject obj = new JSONObject();
		try {
			Map<String, ?> map = PropertyUtils.describe(model);
			Set<String> keys = map.keySet();
			Iterator<String> it = keys.iterator();
			while (it.hasNext()) {
				String propName = it.next();
				if (!propName.equals("class")) // skip class property
					obj.put(propName, map.get(propName).toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return obj;		
	}
	
	// Get stack trace and look for "controllerName" there
	private static boolean stackTraceInclude(String controllerName) {
		StackTraceElement[] stack = Thread.currentThread().getStackTrace();
		// stack[0] contains the method that created the stack frames.
		// stack[stack.length-1] contains the oldest method call.
		// Enumerate each stack element.		
		for (int i = 1; i < stack.length; i++) {
			String className = stack[i].getClassName();
			if (className.equals(controllerName))
				return true;
		}
		return false;
	}	
}