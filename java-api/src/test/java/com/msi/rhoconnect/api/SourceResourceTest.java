package com.msi.rhoconnect.api;

import static org.junit.Assert.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.msi.rhoconnect.api.SourceResource;
import com.msi.rhoconnect.api.UserResource;
import com.sun.jersey.api.client.ClientResponse;

public class SourceResourceTest {
	static String URL = "http://localhost:9292";
	static String api_token;	
	String token;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		api_token = Helper.getToken(URL);
		Helper.reset(URL, api_token);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		token = api_token;
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetSources() {
//		ClientResponse response = SourceResource.getSources(URL, token, "all");
		ClientResponse response = SourceResource.getSources(URL, token, SourceResource.PartitionType.All);
		assertEquals("Status code", 200, response.getStatus());
		String sources = response.getEntity(String.class);
		JSONArray array = (JSONArray)JSONValue.parse(sources);
		int numOfAllSources = array.size();
//		System.out.println("1:");
//		System.out.println(array); // => 		
//		assertTrue(array.size() == 0);

		response = SourceResource.getSources(URL, token, SourceResource.PartitionType.User);
		assertEquals("Status code", 200, response.getStatus());
		sources = response.getEntity(String.class);
		array = (JSONArray)JSONValue.parse(sources);
		int numOfUserSources = array.size();
//		System.out.println("2:");
//		System.out.println(array); // => 
//		assertTrue(array.size() == 0);

		response = SourceResource.getSources(URL, token, SourceResource.PartitionType.App);
		assertEquals("Status code", 200, response.getStatus());
		sources = response.getEntity(String.class);
		array = (JSONArray)JSONValue.parse(sources);
		int numOfAppSources = array.size();
//		System.out.println("3:");
//		System.out.println(array); // => 
		assertTrue((numOfUserSources + numOfAppSources) == numOfAllSources );
	}

	@Test
	public void testGetAttributes() {
		ClientResponse response = SourceResource.getAttributes(URL, token, "RhoInternalBenchmarkAdapter");
		assertEquals("Status code", 200, response.getStatus());
		String attributes = response.getEntity(String.class);
		//System.out.println(attributes); // => 
		// [{"name":"id","value":"RhoInternalBenchmarkAdapter","type":"string"},
		//  {"name":"rho__id","value":"RhoInternalBenchmarkAdapter","type":"string"},
		//  {"name":"name","value":"RhoInternalBenchmarkAdapter","type":"string"},
		//  {"name":"url","value":"","type":"string"},
		//  {"name":"login","value":"","type":"string"},
		//  {"name":"password","value":"","type":"string"},
		//  {"name":"callback_url","value":null,"type":"string"},
		//  {"name":"partition_type","value":"user","type":"string"},
		//  {"name":"sync_type","value":"incremental","type":"string"},
		//  {"name":"queue","value":null,"type":"string"},
		//  {"name":"query_queue","value":null,"type":"string"},
		//  {"name":"cud_queue","value":null,"type":"string"},
		//  {"name":"belongs_to","value":null,"type":"string"},
		//  {"name":"has_many","value":null,"type":"string"},
		//  {"name":"pass_through","value":null,"type":"string"},
		//  {"name":"push_notify","value":true,"type":"string"},
		//  {"name":"source_id","value":null,"type":"integer"},
		//  {"name":"priority","value":3,"type":"integer"},
		//  {"name":"retry_limit","value":0,"type":"integer"},
		//  {"name":"simulate_time","value":0,"type":"integer"},
		//  {"name":"poll_interval","value":300,"type":"integer"}]
		JSONArray array = (JSONArray)JSONValue.parse(attributes);
		assertTrue(array.size() != 0);
	}

	@Test
	public void testSetAttributes() {
		JSONObject attributes = new JSONObject();
		attributes.put("poll_interval", new Integer(99));
		ClientResponse response = SourceResource.setAttributes(URL, token, "RhoInternalBenchmarkAdapter", attributes);
		assertEquals("Status code", 200, response.getStatus());

		response = SourceResource.getAttributes(URL, token, "RhoInternalBenchmarkAdapter");
		assertEquals("Status code", 200, response.getStatus());
		String body = response.getEntity(String.class);

		JSONArray o = (JSONArray)JSONValue.parse(body);
		for(int i = 0; i < o.size(); i++) {
			JSONObject rec = (JSONObject) o.get(i);
			if(rec.get("name").equals("poll_interval")) {
				Long val = (Long)rec.get("value");
				assertTrue(val.intValue() == 99);
			}	
		}
	}

}
