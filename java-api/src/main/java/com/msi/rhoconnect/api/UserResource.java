/**
 * 
 */
package com.msi.rhoconnect.api;

import org.json.simple.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class UserResource {	
	public static ClientResponse list(String url, String token) {
		Client client = Client.create();
		String path = String.format("%s/rc/v1/users", url);
		WebResource webResource = client.resource(path);
		
		ClientResponse response = webResource.accept("application/json")
			.header("X-RhoConnect-API-TOKEN", token)
		    .get(ClientResponse.class);
		return response;
	}

	public static ClientResponse create(String url, String token, String login, String password) {
		Client client = Client.create();
		String path = String.format("%s/rc/v1/users", url);
		WebResource webResource = client.resource(path);
		
		String credentials = 
				String.format("{\"attributes\":{\"login\":\"%s\",\"password\":\"%s\"}}", login, password);
		
		ClientResponse response = webResource.type("application/json")
			.header("X-RhoConnect-API-TOKEN", token)
		    .post(ClientResponse.class, credentials);
		return response;
	}

	public static ClientResponse delete(String url, String token, String userId) {
		Client client = Client.create();
		String path = String.format("%s/rc/v1/users/%s", url, userId);
		WebResource webResource = client.resource(path);
				
		ClientResponse response = webResource.type("application/json")
			.header("X-RhoConnect-API-TOKEN", token)
		    .delete(ClientResponse.class);
		return response;
	}
	
	// userAttributes is a hash of her attribute/value pairs:
	// {:a_user_specific_attribute => a_user_specific_attribute_value} 
	public static ClientResponse update(String url, String token, String userId, JSONObject userAttributes) {
		Client client = Client.create();
		String path = String.format("%s/rc/v1/users/%s", url, userId);
		WebResource webResource = client.resource(path);
		
		JSONObject obj=new JSONObject();
		obj.put("attributes", userAttributes);
		String content = JSONObject.toJSONString(obj);
		
		ClientResponse response = webResource.type("application/json")
				.header("X-RhoConnect-API-TOKEN", token)
			    .put(ClientResponse.class, content);		
		return response;
	}
	
	public static ClientResponse show(String url, String token, String userId) {
		Client client = Client.create();
		String path = String.format("%s/rc/v1/users/%s", url, userId);
		WebResource webResource = client.resource(path);
		
		ClientResponse response = webResource.accept("application/json")
			.header("X-RhoConnect-API-TOKEN", token)
			.get(ClientResponse.class);
		return response;
	}
	
	public static ClientResponse clients(String url, String token, String userId) {
		Client client = Client.create();
		String path = String.format("%s/rc/v1/users/%s/clients", url, userId);
		WebResource webResource = client.resource(path);
		
		ClientResponse response = webResource.accept("application/json")
			.header("X-RhoConnect-API-TOKEN", token)
			.get(ClientResponse.class);
		return response;
	}

	public static ClientResponse deleteClient(String url, String token, String userId, String clientId) {
		Client client = Client.create();
		String path = String.format("%s/rc/v1/users/%s/clients/%s", url, userId, clientId);
		WebResource webResource = client.resource(path);
		
		ClientResponse response = webResource.type("application/json")
			.header("X-RhoConnect-API-TOKEN", token)
			.delete(ClientResponse.class);
		return response;
	}
	
	public static ClientResponse sourcesDocnames(String url, String token, String userId, String sources) {
		Client client = Client.create();
		String path = String.format("%s/rc/v1/users/%s/sources/%s/docnames", url, userId, sources);
		WebResource webResource = client.resource(path);

		ClientResponse response = webResource.accept("application/json")
			.header("X-RhoConnect-API-TOKEN", token)
			.get(ClientResponse.class);
		return response;
	}
	
	public static ClientResponse ping(String url, String token, JSONObject pingParams) {
		Client client = Client.create();
		String path = String.format("%s/rc/v1/users/ping", url);
		WebResource webResource = client.resource(path);

		String content = JSONObject.toJSONString(pingParams);		
		ClientResponse response = webResource.type("application/json")
			.header("X-RhoConnect-API-TOKEN", token)
			.post(ClientResponse.class, content);
		return response;
	}
	
	public static ClientResponse getSourcesDocs(String url, String token, String userId, String sourceId, String docname) {
		Client client = Client.create();
		String path = String.format("%s/rc/v1/users/%s/sources/%s/docs/%s", url, userId, sourceId, docname);
		WebResource webResource = client.resource(path);
		
		ClientResponse response = webResource.accept("application/json")
				.header("X-RhoConnect-API-TOKEN", token)
				.get(ClientResponse.class);
		return response;
	}
		
	public static ClientResponse setSourcesDocs(String url, String token, String userId, String sourceId, 
			String docname, String data, boolean append) {
		JSONObject obj=new JSONObject();
		obj.put("data", data);
		obj.put("append", new Boolean(append));
		String content = JSONObject.toJSONString(obj);

		return set_source_docs(url, token, userId, sourceId, docname, content, append);
	}
	
	public static ClientResponse setSourcesDocs(String url, String token, String userId, String sourceId,
			String docname, JSONObject data, boolean append) {
		JSONObject obj = new JSONObject();
		obj.put("data", data);
		obj.put("append", new Boolean(append));
		String content = JSONObject.toJSONString(obj);

		return set_source_docs(url, token, userId, sourceId, docname, content, append);
	}
	
	private static ClientResponse set_source_docs(String url, String token, String userId, String sourceId,
			String docname, String jsonData, boolean append) {
		Client client = Client.create();
		String path = String.format("%s/rc/v1/users/%s/sources/%s/docs/%s", url, userId, sourceId, docname);
		// System.out.println(jsonData);
		
		WebResource webResource = client.resource(path);
		ClientResponse response = webResource.accept("application/json")
				.header("X-RhoConnect-API-TOKEN", token)
				.post(ClientResponse.class, jsonData);
		return response;
	}
	
}
