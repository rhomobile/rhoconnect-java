/**
 * 
 */
package com.msi.rhoconnect.api;

import org.json.simple.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class UserResource {
	// List users registered with this RhoConnect application
	public static ClientResponse list(String url, String token) {
		Client client = Client.create();
		// GET /rc/v1/users
		String path = String.format("%s/rc/v1/users", url);
		WebResource webResource = client.resource(path);
		
		ClientResponse response = webResource.accept("application/json")
			.header("X-RhoConnect-API-TOKEN", token)
		    .get(ClientResponse.class);
		return response;
	}
	
	// Create a user in this RhoConnect application
	public static ClientResponse create(String url, String token, String login, String password) {
		Client client = Client.create();
		// POST /rc/v1/users
		String path = String.format("%s/rc/v1/users", url);
		WebResource webResource = client.resource(path);
		
		String credentials = 
				String.format("{\"attributes\":{\"login\":\"%s\",\"password\":\"%s\"}}", login, password);
		
		ClientResponse response = webResource.type("application/json")
			.header("X-RhoConnect-API-TOKEN", token)
		    .post(ClientResponse.class, credentials);
		return response;
	}

	// Delete User and all associated devices from the RhoConnect application
	public static ClientResponse delete(String url, String token, String userId) {
		Client client = Client.create();
		// DELETE /rc/v1/users/:user_id
		String path = String.format("%s/rc/v1/users/%s", url, userId);
		WebResource webResource = client.resource(path);
				
		ClientResponse response = webResource.type("application/json")
			.header("X-RhoConnect-API-TOKEN", token)
		    .delete(ClientResponse.class);
		return response;
	}
	
	// Update attributes for a user on this RhoConnect application
	// userAttributes is a hash of her attribute/value pairs:
	// {:a_user_specific_attribute => a_user_specific_attribute_value} 
	public static ClientResponse update(String url, String token, String userId, JSONObject userAttributes) {
		Client client = Client.create();
		// PUT /rc/v1/users/:user_id
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
	
	// Returns the information for the specified user
	public static ClientResponse show(String url, String token, String userId) {
		Client client = Client.create();
		// GET /rc/v1/users/:user_id
		String path = String.format("%s/rc/v1/users/%s", url, userId);
		WebResource webResource = client.resource(path);
		
		ClientResponse response = webResource.accept("application/json")
			.header("X-RhoConnect-API-TOKEN", token)
			.get(ClientResponse.class);
		return response;
	}
	
	// List clients (devices) associated with given user
	public static ClientResponse listClients(String url, String token, String userId) {
		Client client = Client.create();
		// GET /rc/v1/users/:user_id/clients
		String path = String.format("%s/rc/v1/users/%s/clients", url, userId);
		WebResource webResource = client.resource(path);
		
		ClientResponse response = webResource.accept("application/json")
			.header("X-RhoConnect-API-TOKEN", token)
			.get(ClientResponse.class);
		return response;
	}

	// Deletes the specified client (device) for the given user
	public static ClientResponse deleteClient(String url, String token, String userId, String clientId) {
		Client client = Client.create();
		// DELETE /rc/v1/users/:user_id/clients/:client_id
		String path = String.format("%s/rc/v1/users/%s/clients/%s", url, userId, clientId);
		WebResource webResource = client.resource(path);
		
		ClientResponse response = webResource.type("application/json")
			.header("X-RhoConnect-API-TOKEN", token)
			.delete(ClientResponse.class);
		return response;
	}
	
	// Return list of document keys associated with given source and user
	// If userId set to '*', this call will return list of keys for 'shared' documents.
	public static ClientResponse sourcesDocnames(String url, String token, String userId, String sources) {
		Client client = Client.create();
		// GET /rc/v1/users/:user_id/sources/:source_id/docnames
		String path = String.format("%s/rc/v1/users/%s/sources/%s/docnames", url, userId, sources);
		WebResource webResource = client.resource(path);

		ClientResponse response = webResource.accept("application/json")
			.header("X-RhoConnect-API-TOKEN", token)
			.get(ClientResponse.class);
		return response;
	}
	
	// Sends PUSH message to all devices of the specified user(s)
	public static ClientResponse ping(String url, String token, JSONObject pingParams) {
		Client client = Client.create();
		// POST /rc/v1/users/ping
		String path = String.format("%s/rc/v1/users/ping", url);
		WebResource webResource = client.resource(path);

		String content = JSONObject.toJSONString(pingParams);		
		ClientResponse response = webResource.type("application/json")
			.header("X-RhoConnect-API-TOKEN", token)
			.post(ClientResponse.class, content);
		return response;
	}
	
	// Return content of a given source document for the specified user
	public static ClientResponse getSourcesDocs(String url, String token, String userId, String sourceId, String docname) {
		Client client = Client.create();
		// GET /rc/v1/users/:user_id/sources/:source_id/docs/:doc
		String path = String.format("%s/rc/v1/users/%s/sources/%s/docs/%s", url, userId, sourceId, docname);
		WebResource webResource = client.resource(path);
		
		ClientResponse response = webResource.accept("application/json")
				.header("X-RhoConnect-API-TOKEN", token)
				.get(ClientResponse.class);
		return response;
	}
	
	// Sets the content of the specified source document for the given user. 
	// Data should be either a string or hash of hashes. 
	// If append flag is set to true , the data is appended to the current doc (if it exists) 
	// instead of replacing it.	
	public static ClientResponse setSourcesDocs(String url, String token, String userId, String sourceId, 
			String docname, String data, boolean append) {
		JSONObject obj=new JSONObject();
		obj.put("data", data);
		obj.put("append", new Boolean(append));
		String content = JSONObject.toJSONString(obj);

		// POST /rc/v1/users/:user_id/sources/:source_id/docs/:doc
		return set_source_docs(url, token, userId, sourceId, docname, content, append);
	}
	
	// Sets the content of the specified source document for the given user. 
	// Data should be either a string or hash of hashes. 
	// If append flag is set to true , the data is appended to the current doc (if it exists) 
	// instead of replacing it.	
	public static ClientResponse setSourcesDocs(String url, String token, String userId, String sourceId,
			String docname, JSONObject data, boolean append) {
		JSONObject obj = new JSONObject();
		obj.put("data", data);
		obj.put("append", new Boolean(append));
		String content = JSONObject.toJSONString(obj);

		// POST /rc/v1/users/:user_id/sources/:source_id/docs/:doc
		return set_source_docs(url, token, userId, sourceId, docname, content, append);
	}
	
	private static ClientResponse set_source_docs(String url, String token, String userId, String sourceId,
			String docname, String jsonData, boolean append) {
		Client client = Client.create();
		// POST /rc/v1/users/:user_id/sources/:source_id/docs/:doc
		String path = String.format("%s/rc/v1/users/%s/sources/%s/docs/%s", url, userId, sourceId, docname);
		// System.out.println(jsonData);
		
		WebResource webResource = client.resource(path);
		ClientResponse response = webResource.accept("application/json")
				.header("X-RhoConnect-API-TOKEN", token)
				.post(ClientResponse.class, jsonData);
		return response;
	}
	
}
