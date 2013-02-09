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


public class SourceResource {
	public enum PartitionType {
		App("app"), User("user"), All("all");
		
		private String value;
		private PartitionType(String value) {
			this.value = value;
		}
	}
	
	public static ClientResponse getSources(String url, String token, PartitionType partitionType) {
		Client client = Client.create();
		// GET /rc/v1/sources/type/:partition_type
		String path = String.format("%s/rc/v1/sources/type/%s", url, partitionType.value);
		WebResource webResource = client.resource(path);

		ClientResponse response = webResource.accept("application/json")
			.header("X-RhoConnect-API-TOKEN", token)
			.get(ClientResponse.class);
		return response;
	}
	
	public static ClientResponse getAttributes(String url, String token, String sourceId) {
		Client client = Client.create();
		// GET /rc/v1/sources/:source_id
		String path = String.format("%s/rc/v1/sources/%s", url, sourceId);
		WebResource webResource = client.resource(path);

		ClientResponse response = webResource.accept("application/json")
			.header("X-RhoConnect-API-TOKEN", token)
			.get(ClientResponse.class);
		return response;
	}
	
	public static ClientResponse setAttributes(String url, String token, String sourceId, JSONObject sourceParams) {
		Client client = Client.create();
		// PUT /rc/v1/sources/:source_id
		String path = String.format("%s/rc/v1/sources/%s", url, sourceId);
		WebResource webResource = client.resource(path);
		
		JSONObject obj=new JSONObject();
		obj.put("data", sourceParams);
		String content = JSONObject.toJSONString(obj);
		
		ClientResponse response = webResource.type("application/json")
				.header("X-RhoConnect-API-TOKEN", token)
			    .put(ClientResponse.class, content);		
		return response;		
	}
	
}
