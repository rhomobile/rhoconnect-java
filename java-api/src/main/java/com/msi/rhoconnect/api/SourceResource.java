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


public class SourceResource {
	public enum PartitionType {
		App("app"), User("user"), All("all");
		
		private String value;
		private PartitionType(String value) {
			this.value = value;
		}
	}
	
	// Return list of source adapters for this RhoConnect application for the given partition type. 
	// Partition type is one of PartitionType.App, PartitionType.User, PartitionType.All.
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
	
	// Return attributes associated with a given source
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
	
	// Updates attributes associated with a given source. Attributes defined as a hash of attribute/value pair(s).
	public static ClientResponse setAttributes(String url, String token, String sourceId, Map<String,Object> sourceParams) {
		Client client = Client.create();
		// PUT /rc/v1/sources/:source_id
		String path = String.format("%s/rc/v1/sources/%s", url, sourceId);
		WebResource webResource = client.resource(path);
		
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("data", sourceParams);
		String content = JSONUtil.toJSONString(obj);
		
		ClientResponse response = webResource.type("application/json")
				.header("X-RhoConnect-API-TOKEN", token)
			    .put(ClientResponse.class, content);		
		return response;		
	}
	
}
