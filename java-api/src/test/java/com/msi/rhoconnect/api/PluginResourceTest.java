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

import org.junit.Rule;
import org.junit.ClassRule;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertThat;

public class PluginResourceTest {
	@ClassRule
	@Rule
	public static WireMockRule wireMockRule = new WireMockRule(8089);
	static String URL = "http://localhost:8089";
	String token;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		//api_token = Helper.getToken(URL);
		//Helper.reset(URL, api_token);		
		//ClientResponse response = UserResource.create(URL, api_token, "testuser1", "testpass1");
		//assertEquals("Status code", 200, response.getStatus());
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		//wireMockRule.stopServer(); 
	}

	@Before
	public void setUp() throws Exception {
		token = "my-rhoconnect-token";
		//token = api_token;

		// POST /app/v1/:source_name/push_objects
		String url = String.format("/app/v1/%s/push_objects", "Product");
		stubFor(post(urlEqualTo(url))
				.withHeader("X-RhoConnect-API-TOKEN", equalTo(token))
				.withHeader("Content-Type", equalTo("application/json"))
						.willReturn(aResponse()
			                .withStatus(200)
			                .withHeader("Content-Type", "application/json")
			                .withBody("")));
		// POST /app/v1/:source_name/push_deletes
		url = String.format("/app/v1/%s/push_deletes", "Product");
		stubFor(post(urlEqualTo(url))
				.withHeader("X-RhoConnect-API-TOKEN", equalTo(token))
				.withHeader("Content-Type", equalTo("application/json"))
						.willReturn(aResponse()
			                .withStatus(200)
			                .withHeader("Content-Type", "application/json")
			                .withBody("")));	
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
		
		ClientResponse response = PluginResource.pushObjects(URL, token, "testuser1", "Product", data);
		assertEquals("Response code", 200, response.getStatus());

//		response = UserResource.sourcesDocnames(URL, token, "testuser1", "RhoInternalBenchmarkAdapter");
//		assertEquals("Status code", 200, response.getStatus());
//		String body = response.getEntity(String.class);
//		JSONObject o = (JSONObject)JSONValue.parse(body);
//		assertTrue(o.get("md") != null);
//		assertTrue(o.get("md_size") != null);
//		String mdsize = (String)o.get("md_size");
//		response = StoreResource.get(URL, token, mdsize);
//		assertTrue(Integer.parseInt(response.getEntity(String.class)) == 1);		
//		
//		String mdname = (String)o.get("md");
//		response = StoreResource.get(URL, token, mdname);
//		assertEquals("Response code", 200, response.getStatus());
//		body = response.getEntity(String.class);
//		assertEquals("Verify doc result", data, JSONValue.parse(body));
	}

	@Test
	public void testDeleteObjects() {
		// "should delete object from :md"
		JSONArray list = new JSONArray();
		list.add("1");
		
		ClientResponse response = PluginResource.deleteObjects(URL, token, "testuser1", "Product", list);
		assertEquals("Response code", 200, response.getStatus());
		
//		response = UserResource.sourcesDocnames(URL, token, "testuser1", "RhoInternalBenchmarkAdapter");
//		assertEquals("Status code", 200, response.getStatus());
//		String body = response.getEntity(String.class);
//		JSONObject o = (JSONObject)JSONValue.parse(body);
//		assertTrue(o.get("md") != null);
//		assertTrue(o.get("md_size") != null);
//		String mdsize = (String)o.get("md_size");
//		response = StoreResource.get(URL, token, mdsize);
//		assertTrue(Integer.parseInt(response.getEntity(String.class)) == 0);		
	}

}
