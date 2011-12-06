package com.rhomobile.rhoconnect;

import java.util.*;

import org.apache.log4j.Logger;
import org.apache.commons.beanutils.PropertyUtils;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


public class RhoconnectClient {
	private static final Logger logger = Logger.getLogger(RhoconnectClient.class);	
	private static final String RhoconnectControllerName = 
		"com.rhomobile.rhoconnect.controller.RhoconnectController";
	
	private String endpointUrl;
	private String appEndpoint;
    private String apiToken;
    private RestTemplate restTemplate;
    
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

	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public RestTemplate getRestTemplate() {
		return restTemplate;
	}
		
	public void setAppEndpoint(/* String endpoint, String appEndpoint, String api_token */) {
		HashMap<String, Object> reqHash = new HashMap<String, Object>();
		reqHash.put("api_token", apiToken);
		HashMap<String, Object> attrHash = new HashMap<String, Object>();
		attrHash.put("adapter_url", appEndpoint);
        reqHash.put("attributes", attrHash);

		/*
		 * URL: endpointUrl/api/source/save_adapter
		 * 
		 * { "api_token" => api_token,
		 *   "attributes" => { "adapter_url" => appEndpoint } 
		 * }
		 */
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);				 
		HttpEntity<HashMap> entity = new HttpEntity<HashMap>(reqHash, headers);
		ResponseEntity<String> response = restTemplate.exchange(
				endpointUrl + "/api/source/save_adapter", 
				HttpMethod.POST, entity, String.class);
		HttpStatus statusCode = response.getStatusCode();

		if(statusCode.value() != 200) {
			// TODO: raise exception! 
		}
	}

	public void setAppEndpoint(String endpoint, String appEndpoint, String api_token) {
		endpointUrl = endpoint;
		this.appEndpoint = appEndpoint;
	    apiToken = api_token;

	    setAppEndpoint();
	}

	public boolean notifyOnCreate(String sourceName, String partition, Object objId, Object object) {
		// Ignore notification calls from RhoconnectController
		if (stackTraceInclude(RhoconnectControllerName)) {
			logger.info("Notifications from RhoconnectController ingnored");
			return true;
		}

		HashMap<String, Object> objects = new HashMap<String, Object>();
		objects.put((String)objId, daoToMap(object));
		HashMap<String, Object> reqHash = new HashMap<String, Object>();
		reqHash.put("objects", objects);
		return sendObjects("push_objects", sourceName, partition, reqHash);
	}	
	
	public boolean notifyOnUpdate(String sourceName, String partition, Object objId, Object object) {
		// Ignore notification calls from RhoconnectController
		if (stackTraceInclude(RhoconnectControllerName)) {
			logger.info("Notifications from RhoconnectController ingnored");
			return true;
		}	
			
		HashMap<String, Object> objects = new HashMap<String, Object>();
		objects.put((String)objId, daoToMap(object));			
		HashMap<String, Object> reqHash = new HashMap<String, Object>();
		reqHash.put("objects", objects);
		return sendObjects("push_objects", sourceName, partition, reqHash);		
	}	
	
	public boolean notifyOnDelete(String sourceName, String partition, Object objId) {
		// Ignore notification calls from RhoconnectController
		if (stackTraceInclude(RhoconnectControllerName)) {
			logger.info("Notifications from RhoconnectController ingnored");
			return true;
		}	
		
		HashMap<String, Object> reqHash = new HashMap<String, Object>();
		// Array of ids of objects to be deleted
		Object [] deletes = { objId }; 
		reqHash.put("objects", deletes);
        return sendObjects("push_deletes", sourceName, partition, reqHash);	
	}

	private boolean sendObjects(String method, String sourceName, String partition, HashMap<String, Object> hash) {
		hash.put("api_token", apiToken);
		hash.put("source_id", sourceName);
		hash.put("user_id", partition);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);				 
		HttpEntity<Map> entity = new HttpEntity<Map>(hash, headers);

		logger.debug("RhoconnectClient#sendObjects: method: " + method);
		logger.debug("RhoconnectClient#sendObjects: source/partition: " + sourceName + '/' +	partition);
		logger.debug("RhoconnectClient#sendObjects: hash: " + hash.toString());

		ResponseEntity<String> response = restTemplate.exchange(
				endpointUrl + "/api/source/" + method, 
				HttpMethod.POST,
				entity,
				String.class);
		HttpStatus statusCode = response.getStatusCode();
		logger.debug("RhoconnectClient#sendObjects: statusCode: " + statusCode.value());

		return(statusCode.value() == 200);
	}

	// Helper methods
	// Convert DAO object to hash map
	public static Map daoToMap(Object model) {
		HashMap<String, Object> hash = new HashMap<String, Object>();

		try {
			Map map = PropertyUtils.describe(model);
			Set<String> keys = map.keySet();
			Iterator it = keys.iterator();
			while (it.hasNext()) {
				String propName = (String)it.next();
				if (!propName.equals("class")) // skip class property
					hash.put(propName, map.get(propName).toString());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return hash;
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