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

import com.msi.rhoconnect.api.UserResource;
import com.sun.jersey.api.client.ClientResponse;

public class UserResourceTest {
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
	public void testList() {
		ClientResponse response = UserResource.list(URL, token);
		assertEquals("Status code", 200, response.getStatus());
		String users = response.getEntity(String.class);
		Object o = JSONValue.parse(users);
		JSONArray array = (JSONArray)o;
		assertEquals("should return empty users list", 0, array.size());		
	}

	@Test
	public void testCreate() {
		ClientResponse response = UserResource.create(URL, token, "testuser1", "testpass1");
		assertEquals("Status code", 200, response.getStatus());
		assertEquals("Body", "User created", response.getEntity(String.class));

		response = UserResource.list(URL, token);
		assertEquals("Status code", 200, response.getStatus());
		String users = response.getEntity(String.class);
		Object o = JSONValue.parse(users);
		JSONArray array = (JSONArray)o;
		assertTrue(array.contains("testuser1"));
	}

	@Test
	public void testUpdate() {
		// "should update user successfully"
		JSONObject userAttributes = new JSONObject();
		userAttributes.put("new_password", "123");		
		ClientResponse response = UserResource.update(URL, token, "testuser1", userAttributes);
		assertTrue(200 == response.getStatus());

//		// "should fail to update user with wrong attributes"
//		userAttributes.clear();
//		userAttributes.put("missingattrib", "123");		
//		response = UserResource.update(URL, token, "testuser1", userAttributes);
//		assertTrue(500 == response.getStatus());
//		String body = response.getEntity(String.class);
//		//System.out.println(body);
//		assertTrue(body.startsWith("undefined method"));		
	}
	
	@Test
	public void testShow() {
		ClientResponse response = UserResource.show(URL, token, "testuser1");
		assertTrue(200 == response.getStatus());
		String body = response.getEntity(String.class);
		JSONArray o = (JSONArray)JSONValue.parse(body);
		//System.out.println(o);
		// =>
		// [{"name":"rho__id","type":"string","value":"testuser1"},
		//  {"name":"login","type":"string","value":"testuser1"},
		// ...
		//  {"name":"token_id","type":"string","value":null}]		
		JSONObject logInfo = new JSONObject();
		logInfo.put("name", "login");
		logInfo.put("value", "testuser1");
		logInfo.put("type", "string");		
		assertTrue(o.contains(logInfo));
	}
	
	@Test
	public void testClients() {
		ClientResponse response = UserResource.clients(URL, token, "testuser1");
		assertTrue(200 == response.getStatus());
		// "should handle empty client's list"
		String body = response.getEntity(String.class);
		JSONArray o = (JSONArray)JSONValue.parse(body);
		assertTrue(o.size() == 0);
		
		 // "should list user's clients"
		 // TODO: GET /application/clientcreate
		 response = UserResource.clients(URL, token, "testuser1");
		 assertTrue(200 == response.getStatus());
		 body = response.getEntity(String.class);
		 o = (JSONArray)JSONValue.parse(body);
		 //System.out.println(o); // => ["4a61e99ee6334919ae02dee98a9adbf4"]
	}

//	@Test
//	public void testDeleteClient() {
//		// TODO: GET /application/clientcreate
//		// ...
//		ClientResponse response = UserResource.deleteClient(URL, token, "testuser1", "4a61e99ee6334919ae02dee98a9adbf4");
//		// FIXME: undefined method `delete' for nil ...
//		// Should return 404?
//		assertEquals("Status code", 500, response.getStatus());
//	}
	
	@Test
	public void testSourcesDocnames() {
		// TODO: mockups for sources
		ClientResponse response = UserResource.sourcesDocnames(URL, token, "testuser1", "RhoInternalBenchmarkAdapter");
		assertEquals("Status code", 200, response.getStatus());
		String body = response.getEntity(String.class);
		JSONObject o = (JSONObject)JSONValue.parse(body);
		assertTrue(o.get("md") != null);
		assertTrue(o.get("md_size") != null);		
		assertTrue(o.get("md_copy") != null);		
		assertTrue(o.get("errors") != null);				

		// FIXME: undefined method `docname' for nil ...
		// Should return 404?
		// response = UserResource.sourcesDocnames(URL, token, "testuser1", "*");		
	}
	
	
	@Test
	public void testGetSourcesDocs() {
		ClientResponse response = UserResource.getSourcesDocs(URL, token, "testuser1", "RhoInternalBenchmarkAdapter", "md");
		assertEquals("Status code", 200, response.getStatus());
		System.out.println(response.getEntity(String.class));
		
//	      data = {'1' => {'foo' => 'bar'}}
//	      dockey = 'myuserdoc'
//	      post "/rc/#{Rhoconnect::API_VERSION}/users/#{@u.id}/sources/#{@s2.name}/docs/#{dockey}", {:data => data}, {Rhoconnect::API_TOKEN_HEADER => @api_token}
//	      last_response.should be_ok
//	      
//	      get "/rc/#{Rhoconnect::API_VERSION}/users/#{@u.id}/sources/#{@s2.name}/docs/#{dockey}", {}, {Rhoconnect::API_TOKEN_HEADER => @api_token}
//	      last_response.should be_ok
//	      JSON.parse(last_response.body).should == data
		JSONObject hash = new JSONObject();
		hash.put("foo", "bar");
		JSONObject data = new JSONObject();
		data.put("1", hash);
	    String dockey = "myuserdoc";
		response = UserResource.setSourcesDocs(URL, token, "testuser1", "RhoInternalBenchmarkAdapter", dockey, data, false);
		assertEquals("Status code", 200, response.getStatus());
		response = UserResource.getSourcesDocs(URL, token, "testuser1", "RhoInternalBenchmarkAdapter", dockey);
		assertEquals("Status code", 200, response.getStatus());
		
		// TODO:
//		assertEquals("Response body", data, JSONValue.parse(response.getEntity(String.class)));	
	}

	@Test
	public void testPing() {
		//	ping_params = {
		//		  :user_id => [array_of_users],
		//		  :sources => source_name,
		//		  :message => 'hello world',
		//		  :vibrate => 2000,
		//		  :sound => 'hello.mp3'
		//	}	
		JSONObject pingParams = new JSONObject();
		pingParams.put("user_id", "testuser1");
		pingParams.put("sources", "Product");
		pingParams.put("message", "hello world");
		pingParams.put("vibrate", new Integer(2000));
		pingParams.put("sound", "hello.mp3");

		ClientResponse response = UserResource.ping(URL, token, pingParams);
		assertEquals("Status code", 200, response.getStatus());
	}
	
	@Test
	public void testDelete() {
		ClientResponse response = UserResource.delete(URL, token, "testuser1");
		assertEquals("Status code", 200, response.getStatus());
		assertEquals("Body", "User deleted", response.getEntity(String.class));

		response = UserResource.list(URL, token);
		assertEquals("Status code", 200, response.getStatus());
		String users = response.getEntity(String.class);
		Object o = JSONValue.parse(users);
		JSONArray array = (JSONArray)o;
		assertFalse(array.contains("testuser1"));		
	}
}
