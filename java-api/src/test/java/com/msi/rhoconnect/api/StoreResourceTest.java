package com.msi.rhoconnect.api;

import static org.junit.Assert.*;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.msi.rhoconnect.api.StoreResource;
import com.sun.jersey.api.client.ClientResponse;

import org.junit.Rule;
import org.junit.ClassRule;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class StoreResourceTest {
	@ClassRule
	@Rule
	public static WireMockRule wireMockRule = new WireMockRule(8089);
	static String URL = "http://localhost:8089";
//	static String api_token;
	
	String token;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
//		api_token = Helper.getToken(URL);
//		Helper.reset(URL, api_token);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		token = "my-rhoconnect-token";
//		token = api_token;
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGet() {
		String docname = "api_token:" + token + ":value";
		String url = String.format("/rc/v1/store/%s", docname);
		stubFor(get(urlEqualTo(url))
				.withHeader("X-RhoConnect-API-TOKEN", equalTo(token))
						.willReturn(aResponse()
			                .withStatus(200)
			                .withHeader("Content-Type", "application/json")
			                .withBody(token)));
		ClientResponse response = StoreResource.get(URL, token, docname);
		assertEquals("Response code", 200, response.getStatus());
		String body = response.getEntity(String.class);
		assertEquals("should return db document by name", token, body);				
	}

	@Test
	public void testSet() {
		String docname = "abc:abc";		
		String doc = "some string";

		String url = String.format("/rc/v1/store/%s", docname);
		stubFor(post(urlEqualTo(url))
				.withHeader("X-RhoConnect-API-TOKEN", equalTo(token))
				.withHeader("Content-Type", equalTo("application/json"))
						.willReturn(aResponse()
			                .withStatus(200)
			                .withHeader("Content-Type", "application/json")
			                .withBody("")));
		
		ClientResponse response = StoreResource.set(URL, token, docname, doc, false);
		assertEquals("Response code", 200, response.getStatus());

		stubFor(get(urlEqualTo(url))
				.withHeader("X-RhoConnect-API-TOKEN", equalTo(token))
						.willReturn(aResponse()
			                .withStatus(200)
			                .withHeader("Content-Type", "application/json")
			                .withBody(doc)));
		
		response = StoreResource.get(URL, token, docname);
		assertEquals("Response code", 200, response.getStatus());
		String body = response.getEntity(String.class);
		assertEquals("should set db document by doc name, and data", doc, body);
		
		// "should append data in set db document by doc name and data"
		// data = {'1' => {'foo' => 'bar'}}
//		JSONObject obj = new JSONObject();
//		obj.put("foo", "bar");
//		JSONObject data = new JSONObject();
//		data.put("1", obj);
//		// data2 = {'2' => {'foo1' => 'bar1'}}
//		JSONObject obj2 = new JSONObject();
//		obj2.put("foo1", "bar1");		
//		JSONObject data2 = new JSONObject();
//		data2.put("2", obj2);
//		
//		JSONObject data3 = new JSONObject();
//		data3.putAll(data);
//		data3.putAll(data2);
//		
//		response = StoreResource.set(URL, token, docname, data, false);
//		assertEquals("Response code", 200, response.getStatus());
//		//response = StoreResource.get(URL, token, docname);
//		//String body = response.getEntity(String.class);
//		//assertEquals("should set db document by doc name, and data", JSONObject.toJSONString(data), body);
//		response = StoreResource.set(URL, token, docname, data2, true);
//		assertEquals("Response code", 200, response.getStatus());
//
//		response = StoreResource.get(URL, token, docname);
//		String body3 = response.getEntity(String.class);
//		assertEquals("should append data in set db document by doc name and data", JSONValue.parse(body3), data3);
	}

}
