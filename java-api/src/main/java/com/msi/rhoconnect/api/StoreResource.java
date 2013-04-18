/**
 * 
 */
package com.msi.rhoconnect.api;

import java.util.HashMap;
import java.util.Map;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

/**
 * @author alexb
 *
 */
public class StoreResource {
	static String storeFormat = "%s/rc/v1/store/%s"; 

	// Return content of a given document stored in Redis
	public static ClientResponse get(String url, String token, String docname) {
		Client client = Client.create();
		// GET /rc/v1/store/:doc
		String path = String.format(storeFormat, url, docname);
		WebResource webResource = client.resource(path); 

		ClientResponse response = webResource
				.header("X-RhoConnect-API-TOKEN", token)
		        .get(ClientResponse.class);
		 		
		return response;
	}

	// Sets the content of the specified server document. Data should be a string. 
	// If append flag is set to true , the data is appended to the current doc (if it exists) 
	// instead of replacing it.
	public static ClientResponse set(String url, String token, String docname, String data, boolean append) {
	  Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("data", data);
		obj.put("append", new Boolean(append));
		String content = JSONUtil.toJSONString(obj);
		
		// POST /rc/v1/store/:doc
		return set_db_doc(url, token, docname, content, append);		
	}

	// Sets the content of the specified server document. Data should be a hash of hashes. 
	// If append flag is set to true , the data is appended to the current doc (if it exists) 
	// instead of replacing it.
	public static ClientResponse set(String url, String token, String docname, Map<String,Object> data, boolean append) {
	  Map<String, Object> obj = new HashMap<String, Object>();
    obj.put("data", data);
    obj.put("append", new Boolean(append));
    String content = JSONUtil.toJSONString(obj);

		// POST /rc/v1/store/:doc
		return set_db_doc(url, token, docname, content, append);		
	}
	
	private static ClientResponse set_db_doc(String url, String token, String docname, String jsonString, boolean append) {
		Client client = Client.create();
		// POST /rc/v1/store/:doc
		String path = String.format(storeFormat, url, docname);
		WebResource webResource = client.resource(path);
		
		ClientResponse response = webResource.type("application/json")
		   .header("X-RhoConnect-API-TOKEN", token)
		   .post(ClientResponse.class, jsonString);

		return response;		
	}
		
}
