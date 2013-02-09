/**
 * 
 */
package com.msi.rhoconnect.api;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;

import org.json.simple.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

/**
 * @author alexb
 *
 */
public class SystemResource {
	static String systemFormat = "%s/rc/v1/system/%s"; 

	// Login to the RhoConnect server and get API token
	public static ClientResponse login(String url, String password) {
		// POST /rc/v1/system/login
		Client client = Client.create();
		String path = String.format(systemFormat, url, "login");
		WebResource webResource = client.resource(path); 
		
		Map<String, String> credentials = new HashMap<String, String>();
		credentials.put("login", "rhoadmin");
		credentials.put("password", password);
		String content = JSONObject.toJSONString(credentials);

		ClientResponse response = webResource.type("application/json")
				.post(ClientResponse.class, content);		
		return response;
	}

	// Reset the server: flush db and re-bootstrap server
	public static ClientResponse reset(String url, String token) {
		// POST /rc/v1/system/reset
		Client client = Client.create();
		String path = String.format(systemFormat, url, "reset");
		WebResource webResource = client.resource(path); 

		String data = "{}";
		// TODO: 
		ClientResponse response = webResource.type("application/json")
			.header("X-RhoConnect-API-TOKEN", token)
			.post(ClientResponse.class, data);
		return response;		
	}
	
	// Returns license information of the currently used license
	public static ClientResponse license(String url, String token) {
		// GET /rc/v1/system/license
		Client client = Client.create();
		String path = String.format(systemFormat, url, "license");
		WebResource webResource = client.resource(path);
		
		ClientResponse response = webResource
			.header("X-RhoConnect-API-TOKEN", token)
	        .get(ClientResponse.class);
	 		
		return response;
	}
	
	// Returns the url of the plugin’s backend from the RhoConnect server
	public static ClientResponse getAppserver(String url, String token) {
		// GET /rc/v1/system/appserver
		Client client = Client.create();
		String path = String.format(systemFormat, url, "appserver");
		WebResource webResource = client.resource(path);

		ClientResponse response = webResource
			.header("X-RhoConnect-API-TOKEN", token)
	        .get(ClientResponse.class);
	 		
		return response;
	}
	
	// Saves the url of the plugin’s backend to the RhoConnect server
	public static ClientResponse setAppserver(String url, String token, String adapterUrl) {
		// POST /rc/v1/system/appserver
		Client client = Client.create();
		String path = String.format(systemFormat, url, "appserver");
		WebResource webResource = client.resource(path);

		Map<String, String> data = new HashMap<String, String>();
		data.put("adapter_url", adapterUrl);
		String content = JSONObject.toJSONString(data);
		
		ClientResponse response = webResource.type("application/json")
				.header("X-RhoConnect-API-TOKEN", token)
				.post(ClientResponse.class, content);
		
		return response;
	}

	// Retrieves stats for a given metric key
	public static ClientResponse statsMetricKey(String url, String token, String metricKey, int start, int finish) {
		// GET /rc/v1/system/stats
		Client client = Client.create();
		String path = String.format(systemFormat, url, "stats");
		WebResource webResource = client.resource(path);

		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
		queryParams.add("metric", metricKey);
		queryParams.add("start", Integer.toString(start));
		queryParams.add("finish", Integer.toString(finish));
		
		ClientResponse response = webResource
			.queryParams(queryParams)
			.header("X-RhoConnect-API-TOKEN", token)
		    .get(ClientResponse.class);		
		return response;
	}
	
	// Retrieves a list of metric keys matching a given pattern. This supports ‘glob’ or ‘*’ style pattern matching
	public static ClientResponse statsMetricPattern(String url, String token, String pattern) {
		Client client = Client.create();
		String path = String.format(systemFormat, url, "stats");
		WebResource webResource = client.resource(path);

		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
		queryParams.add("names", pattern);
		
		ClientResponse response = webResource
			.queryParams(queryParams)
			.header("X-RhoConnect-API-TOKEN", token)
	        .get(ClientResponse.class);		
		return response;
	}	
}
