package com.msi.rhoconnect.api;

import static org.junit.Assert.*;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.msi.rhoconnect.api.SystemResource;
import com.sun.jersey.api.client.ClientResponse;

public class SystemResourceTest {
	static String URL = "http://localhost:9292";
	static String token;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testLogin() {
		ClientResponse response = SystemResource.login(URL, "");
		assertEquals("Response code", 200, response.getStatus());
		String token = response.getEntity(String.class);
		//System.out.println(token);
		SystemResourceTest.token = token;		
	}

	@Test
	public void testReset() {
		ClientResponse response = SystemResource.reset(URL, token);
		assertEquals("Response code", 200, response.getStatus());
		String body = response.getEntity(String.class);
		//System.out.println(body);
		assertEquals("Body", "DB reset", body);
	}
	
	@Test
	public void testLicense() {
		ClientResponse response = SystemResource.license(URL, token);
		assertEquals("Response code", 200, response.getStatus());
		String body = response.getEntity(String.class);
		//System.out.println(body); 
		// => 
		// {"rhoconnect_version":"Version 1","licensee":"Rhomobile","seats":10,"issued":"Fri Apr 23 17:20:13 -0700 2010","available":10}
		JSONObject o = (JSONObject)JSONValue.parse(body);
		assertEquals("rhoconnect_version", "Version 1", o.get("rhoconnect_version"));
		assertEquals("licensee", "Rhomobile", o.get("licensee"));
		assertEquals("seats", new Long(10), o.get("seats"));
		assertEquals("issued", "Fri Apr 23 17:20:13 -0700 2010", o.get("issued"));
	}

	@Test
	public void testGetAppServer() {
		ClientResponse response = SystemResource.getAppserver(URL, token);
		assertEquals("Response code", 200, response.getStatus());
		String body = response.getEntity(String.class);
		//System.out.println(body); // => {"adapter_url":null}
	}
	
	@Test
	public void testSetAppServer() {
		ClientResponse response = SystemResource.setAppserver(URL, token, "http://localhost:3000");
		assertEquals("Response code", 200, response.getStatus());
		//String body = response.getEntity(String.class);
		//System.out.println(body); // => http://localhost:3000

		response = SystemResource.getAppserver(URL, token);
		String body = response.getEntity(String.class);
		Object obj=JSONValue.parse(body);
		JSONObject o = (JSONObject)obj;
		assertEquals("adapter_url", "http://localhost:3000", o.get("adapter_url"));
	}

	@Test
	@Ignore
	public void testStats() {
		// TODO: enable stats and add sources 
		ClientResponse response = SystemResource.statsMetricPattern(URL, token, "source:*:Product");
		assertEquals("Response code", 200, response.getStatus());
		
		// TODO:
		// ?names=pattern
		// ?metric=foo&start=0&stop=-1
		//
		// RestClient.get(url,{'X-RhoConnect-API-TOKEN' => token, :params => {:names=>'source:*:Product'}})
		// => "[\"source:query:Product\"]"
		//
	}
	
}
