package com.msi.rhoconnect.api;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.msi.rhoconnect.api.ClientResource;
import com.sun.jersey.api.client.ClientResponse;

import org.junit.Rule;
import org.junit.ClassRule;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class ClientResourceTest {
	@ClassRule
	@Rule
	public static WireMockRule wireMockRule = new WireMockRule(8089);
	static String URL = "http://localhost:8089";
	static String api_token;	
	String token;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		//api_token = Helper.getToken(URL);
		//Helper.reset(URL, api_token);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		//token = api_token;
		token = "my-rhoconnect-token";
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetAttributes() {
		String url = String.format("/rc/v1/clients/%s", "e1bdf184a497424cb8a8c780b0308082");		
		String body = "[{\"name\":\"rho__id\",\"type\":\"string\",\"value\":\"e1bdf184a497424cb8a8c780b0308082\"}," + 
				  "{\"name\":\"device_type\",\"type\":\"string\",\"value\":\"ANDROID\"}," + 
				  "{\"name\":\"app_id\",\"type\":\"string\",\"value\":\"application\"}]";
		stubFor(get(urlEqualTo(url))
				.withHeader("Content-Type", equalTo("application/json"))
		        .withHeader("X-RhoConnect-API-TOKEN", equalTo(token))
					.willReturn(aResponse()
		                .withStatus(200)
		                .withHeader("Content-Type", "application/json")
		                .withBody(body)));
		
		ClientResponse response = ClientResource.getAttributes(URL, token, "e1bdf184a497424cb8a8c780b0308082");
		assertEquals("Status code", 200, response.getStatus());
		String attributes = response.getEntity(String.class);
		JSONArray o = (JSONArray)JSONValue.parse(attributes);
		for(int i = 0; i < o.size(); i++) {
			JSONObject rec = (JSONObject) o.get(i);
			if(rec.get("name").equals("app_id")) 
				assertTrue(rec.get("value").equals("application"));
		}
		//System.out.println(attributes); // =>
		// [{"name":"rho__id","type":"string","value":"e1bdf184a497424cb8a8c780b0308082"},
		//  {"name":"device_type","type":"string","value":"ANDROID"},
		//  {"name":"device_push_type","type":"string","value":"rhoconnect_push"},
		//  {"name":"device_pin","type":"string","value":"09853cb7-ae95-4ea0-9b4b-ad76b9c17fdd:a0024c059cc3d9eb79a2942a16c72e5a"},
		//  {"name":"device_port","type":"string","value":"100"},
		//  {"name":"phone_id","type":"string","value":"1f5a4332-db09-3345-8e53-a7de4a9d7a87"},
		//  {"name":"user_id","type":"string","value":"testuser1"},
		//  {"name":"last_sync","type":"datetime","value":"2013-02-01T15:58:38-08:00"},
		//  {"name":"app_id","type":"string","value":"application"}]
	}

	@Test
	@Ignore
	public void testGetSourcesDocnames() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testGetSourceDocument() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testSetSourcesDocnames() {
		fail("Not yet implemented");
	}

}
