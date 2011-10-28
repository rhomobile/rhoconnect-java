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
		 * URL: endpointUr/api/source/save_adapter
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
	
	public boolean notifyOnCreate(String sourceName, String partition, HashMap<String, Object> objects) {
		HashMap<String, Object> reqHash = new HashMap<String, Object>();
		reqHash.put("objects", objects);

		return sendObjects("push_objects", sourceName, partition, reqHash);
	}
	
	public boolean notifyOnUpdate(String sourceName, String partition, HashMap<String, Object> objects) {
		HashMap<String, Object> reqHash = new HashMap<String, Object>();
		reqHash.put("objects", objects);

		return sendObjects("push_objects", sourceName, partition, reqHash);
	}
	
	// bool notify_on_delete(String source_name, String partition, Object id)
	public boolean notifyOnDelete(String sourceName, String partition, Object id) {
		HashMap<String, Object> reqHash = new HashMap<String, Object>();
		// Array of ids of objects to be deleted
		Object [] deletes = { id }; 
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

		logger.info("RhoconnectClient#sendObjects: method: " + method);
		logger.info("RhoconnectClient#sendObjects: source/partition: " + sourceName + '/' +	partition);
		logger.info("RhoconnectClient#sendObjects: hash: " + hash.toString());

		ResponseEntity<String> response = restTemplate.exchange(
				endpointUrl + "/api/source/" + method, 
				HttpMethod.POST,
				entity,
				String.class);
		HttpStatus statusCode = response.getStatusCode();
		logger.info("RhoconnectClient#sendObjects: statusCode: " + statusCode.value());

		return(statusCode.value() == 200);
	}

	// Helper methods
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
	
}
