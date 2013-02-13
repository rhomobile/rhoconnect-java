package com.msi.rhoconnect.api;

import static org.junit.Assert.*;

import org.json.simple.JSONObject;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.msi.rhoconnect.api.ReadStateResource;
import com.msi.rhoconnect.api.UserResource;
import com.sun.jersey.api.client.ClientResponse;

import org.junit.Rule;
import org.junit.ClassRule;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ReadStateResourceTest {
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
//		ClientResponse response = UserResource.create(URL, api_token, "testuser1", "testpass1");
//		assertEquals("Status code", 200, response.getStatus());
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
//		token = api_token;
		token = "my-rhoconnect-token";
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testSetPollInterval() {
		String url = String.format("/rc/v1/readstate/users/%s/sources/%s", "testuser1", "RhoInternalBenchmarkAdapter");
		stubFor(put(urlEqualTo(url))
				.withHeader("Content-Type", equalTo("application/json"))
		        .withHeader("X-RhoConnect-API-TOKEN", equalTo(token))
					.willReturn(aResponse()
		                .withStatus(200)
		                .withBody("")));
		
		ClientResponse response = ReadStateResource.setPollInterval(URL, token, "testuser1", "RhoInternalBenchmarkAdapter", new Integer(100));
		assertEquals("Response code", 200, response.getStatus());
		//String body = response.getEntity(String.class);
		//System.out.println(body);		
	}
}
