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
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetAttributes() {
		ClientResponse response = ClientResource.getAttributes(URL, token, "e1bdf184a497424cb8a8c780b0308082");
		assertEquals("Status code", 200, response.getStatus());
		String attributes = response.getEntity(String.class);
		JSONArray o = (JSONArray)JSONValue.parse(attributes);
		for(int i = 0; i < o.size(); i++) {
			JSONObject rec = (JSONObject) o.get(i);
			if(rec.get("name").equals("app_id")) 
				assertTrue(rec.get("value").equals("application"));
		}
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
