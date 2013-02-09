/**
 * 
 */
package com.msi.rhoconnect.api;

import org.json.simple.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

/**
 * @author alexb
 *
 */
public class ClientResource {
	// Returns client (device) attributes, such as device_type, device_pin, device_port
	public static ClientResponse getAttributes(String url, String token, String clientId) {
		Client client = Client.create();
		// GET /rc/v1/clients/:client_id
		String path = String.format("%s/rc/v1/clients/%s", url, clientId);
		WebResource webResource = client.resource(path);

		ClientResponse response = webResource.accept("application/json")
			.header("X-RhoConnect-API-TOKEN", token)
			.get(ClientResponse.class);
		return response;
	}

	// Returns list of document keys associated with particular client for a given source
	public static ClientResponse getSourcesDocnames(String url, String token, String clientId, String sourceId) {
		Client client = Client.create();
		// GET /rc/v1/clients/:client_id/sources/:source_id/docnames
		String path = String.format("%s/rc/v1/clients/%s/sources/%s/docnames", url, clientId, sourceId);
		WebResource webResource = client.resource(path);

		ClientResponse response = webResource.accept("application/json")
			.header("X-RhoConnect-API-TOKEN", token)
			.get(ClientResponse.class);
		return response;
	}

	// Return content of a given source document for the specified client
	public static ClientResponse getSourceDocument(String url, String token, String clientId, String sourceId, String docname) {
		Client client = Client.create();
		// GET /rc/v1/clients/:client_id/sources/:source_id/docs/:doc
		String path = String.format("%s/rc/v1/clients/%s/sources/%s/docs/%s", url, clientId, sourceId, docname);
		WebResource webResource = client.resource(path);

		ClientResponse response = webResource.accept("application/json")
				.header("X-RhoConnect-API-TOKEN", token)
				.get(ClientResponse.class);
		return response;
	}	
	

	// Sets the content of the specified source document for the given client. 
	// Data should be either a string or hash of hashes. If append flag is set to true , 
	// the data is appended to the current doc (if it exists) instead of replacing it.
	public static ClientResponse setSourcesDocnames(String url, String token, String clientId, String sourceId, 
			String data, boolean append) {
		JSONObject obj=new JSONObject();
		obj.put("data", data);
		obj.put("append", new Boolean(append));
		String content = JSONObject.toJSONString(obj);

		return set_source_docs(url, token, clientId, sourceId, content, append);
	}

	public static ClientResponse setSourcesDocnames(String url, String token, String clientId, String sourceId, 
			JSONObject data, boolean append) {
		JSONObject obj=new JSONObject();
		obj.put("data", data);
		obj.put("append", new Boolean(append));
		String content = JSONObject.toJSONString(obj);

		return set_source_docs(url, token, clientId, sourceId, content, append);
	}
	
	private static ClientResponse set_source_docs(String url, String token, String clientId, String sourceId,
			String jsonData, boolean append) {
		Client client = Client.create();
		// POST /rc/v1/clients/:client_id/sources/:source_id/docnames
		String path = String.format("%s/rc/v1/clients/%s/sources/%s/docnames", url, clientId, sourceId);
	    System.out.println(jsonData);
		
		WebResource webResource = client.resource(path);
		ClientResponse response = webResource.accept("application/json")
				.header("X-RhoConnect-API-TOKEN", token)
				.post(ClientResponse.class, jsonData);
		return response;
	}
	
}
