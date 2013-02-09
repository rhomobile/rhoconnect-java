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

import com.msi.rhoconnect.api.PluginResource;
import com.msi.rhoconnect.api.StoreResource;
import com.msi.rhoconnect.api.UserResource;
import com.sun.jersey.api.client.ClientResponse;

public class PluginResourceTest {
	static String URL = "http://localhost:9292";
	static String api_token;
	
	String token;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		api_token = Helper.getToken(URL);
		Helper.reset(URL, api_token);
		
		ClientResponse response = UserResource.create(URL, api_token, "testuser1", "testpass1");
		assertEquals("Status code", 200, response.getStatus());
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
	public void testPushObjects() {
		// "should push new objects to rhoconnect's :md"
		// data = {'1' => product1}
		// product = { 'name' => 'Samsung Galaxy', 'brand' => 'Android', 'price' => '249.99'}
		JSONObject data = new JSONObject();
		JSONObject product = new JSONObject();
		product.put("name", "Samsung Galaxy");
		product.put("brand", "Android");
		product.put("price", "249.99");
		data.put("1", product);
		
		ClientResponse response = PluginResource.pushObjects(URL, token, "testuser1", "RhoInternalBenchmarkAdapter", data);
		assertEquals("Response code", 200, response.getStatus());

		response = UserResource.sourcesDocnames(URL, token, "testuser1", "RhoInternalBenchmarkAdapter");
		assertEquals("Status code", 200, response.getStatus());
		String body = response.getEntity(String.class);
		JSONObject o = (JSONObject)JSONValue.parse(body);
		assertTrue(o.get("md") != null);
		assertTrue(o.get("md_size") != null);
		String mdsize = (String)o.get("md_size");
		response = StoreResource.get(URL, token, mdsize);
		assertTrue(Integer.parseInt(response.getEntity(String.class)) == 1);		
		
		String mdname = (String)o.get("md");
		response = StoreResource.get(URL, token, mdname);
		assertEquals("Response code", 200, response.getStatus());
		body = response.getEntity(String.class);
		assertEquals("Verify doc result", data, JSONValue.parse(body));
	}

	@Test
	public void testDeleteObjects() {
		// "should delete object from :md"
		JSONArray list = new JSONArray();
		list.add("1");
		
		ClientResponse response = PluginResource.deleteObjects(URL, token, "testuser1", "RhoInternalBenchmarkAdapter", list);
		assertEquals("Response code", 200, response.getStatus());
		response = UserResource.sourcesDocnames(URL, token, "testuser1", "RhoInternalBenchmarkAdapter");
		assertEquals("Status code", 200, response.getStatus());
		String body = response.getEntity(String.class);
		JSONObject o = (JSONObject)JSONValue.parse(body);
		assertTrue(o.get("md") != null);
		assertTrue(o.get("md_size") != null);
		String mdsize = (String)o.get("md_size");
		response = StoreResource.get(URL, token, mdsize);
		assertTrue(Integer.parseInt(response.getEntity(String.class)) == 0);		
	}

}
