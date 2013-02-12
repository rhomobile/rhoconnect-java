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

public class ReadStateResourceTest {
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
	public void testSetPollInterval() {
		ClientResponse response = ReadStateResource.setPollInterval(URL, token, "testuser1", "RhoInternalBenchmarkAdapter", new Integer(100));
		assertEquals("Response code", 200, response.getStatus());
		//String body = response.getEntity(String.class);
		//System.out.println(body);		
	}
}
