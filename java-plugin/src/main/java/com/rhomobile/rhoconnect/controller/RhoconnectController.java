package com.rhomobile.rhoconnect.controller;

import java.util.Map;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rhomobile.rhoconnect.Rhoconnect;
import com.rhomobile.rhoconnect.RhoconnectClient;
import com.rhomobile.rhoconnect.RhoconnectDispatcher;

@Controller
@RequestMapping("/rhoconnect/*")
public class RhoconnectController {
	private static final Logger logger = Logger.getLogger(RhoconnectController.class);	

	@Autowired
	private RhoconnectDispatcher dispatcher; 
	@Autowired
	private Rhoconnect rhoconnect;
	@Autowired
	private RhoconnectClient client;

	// POST /rhoconnect/authenticate
	@RequestMapping(method=RequestMethod.POST, value="authenticate", headers="Accept=application/json")
	public ResponseEntity<String> authenticate(@RequestBody Map<String, Object> body) {
		logger.debug("RhoconnectController#authenticate " + body.toString());
		String login = (String)body.get("login");
		String password = (String)body.get("password");
		
		HttpHeaders responseHeaders = new HttpHeaders();
	    responseHeaders.setContentType(MediaType.TEXT_PLAIN);
	    
	    String partition = rhoconnect.authenticate(login, password, body);
	    if(partition == null) // In case of error/exception return UNAUTHORIZED(401)
	    	return new ResponseEntity<String>("Authentication has failed", responseHeaders, HttpStatus.UNAUTHORIZED);

	    client.setUserName(login);
	    client.setPartion(partition);
        return new ResponseEntity<String>(partition, responseHeaders, HttpStatus.OK);
    }

    // POST /rhoconnect/query
	@RequestMapping(method=RequestMethod.POST, value="/query", headers="Accept=application/json")	
	public @ResponseBody Map<String, Object> query_objects(@RequestBody Map<String, Object> body) {
		logger.debug("RhoconnectController#query_objects");
		logger.debug(body.toString());
		// { resource=Contact, partition=alexb, attributes=null, api_token=sometokenforme}
		
		String resource = (String)body.get("resource");
		String partition = (String)body.get("partition");
		Map<String, Object> h = dispatcher.query_objects(resource, partition);		
		logger.debug(h.toString());

		return h;
    } 

	 // POST /rhoconnect/create
	@RequestMapping(method=RequestMethod.POST, value="create", headers="Accept=application/json")
	public ResponseEntity<String> create(@RequestBody Map<String, Object> body) {
		logger.debug("RhoconnectController#create");
		logger.debug(body.toString());
		// {resource=Contact, partition="", 
		//  attributes={telephone=me@mail.com, lastname=world, firstname=hello, email=123-456-6789}, api_token=sometokenforme}

		String resource = (String)body.get("resource");
		String partition = (String)body.get("partition");
		Map<String, Object> attributes = (Map<String, Object>) body.get("attributes");
		Integer id = dispatcher.create(resource, partition, attributes);
		logger.debug("RhoconnectController#create: id = " + id);
		
	    HttpHeaders responseHeaders = new HttpHeaders();
	    responseHeaders.setContentType(MediaType.TEXT_PLAIN);
        return new ResponseEntity<String>(Integer.toString(id), responseHeaders, HttpStatus.OK);        
	}
	
	// POST /rhoconnect/update
	@RequestMapping(method=RequestMethod.POST, value="update", headers="Accept=application/json")
	public ResponseEntity<String> update(@RequestBody Map<String, Object> body) {
		logger.debug("RhoconnectController#update");
		logger.debug(body.toString());		
		//{ resource=Contact, partition="", attributes={firstname=Alex, id=29}, api_token=sometokenforme}

		String resource = (String)body.get("resource");
		String partition = (String)body.get("partition");
		Map<String, Object> attributes = (Map<String, Object>) body.get("attributes");
		Integer id = dispatcher.update(resource, partition, attributes);
		logger.debug("RhoconnectController#update: id = " + id);
		
	    HttpHeaders responseHeaders = new HttpHeaders();
	    responseHeaders.setContentType(MediaType.TEXT_PLAIN);	    
        return new ResponseEntity<String>(Integer.toString(id), responseHeaders, HttpStatus.OK);
    }
	
	// POST /rhoconnect/delete
	@RequestMapping(method=RequestMethod.POST, value="delete", headers="Accept=application/json")
	public ResponseEntity<String> delete(@RequestBody Map<String, Object> body) {
		logger.debug("RhoconnectController#delete");
		logger.debug(body.toString());
		// { resource=Contact, partition="", 
		//   attributes={email=maxz@mail.ru, telephone=650-666-7878, firstname=Alex, lastname=Bab, id=29}, api_token=sometokenforme}

		String resource = (String)body.get("resource");
		String partition = (String)body.get("partition");		
		Map<String, Object> attributes = (Map<String, Object>) body.get("attributes");
		Integer id = dispatcher.delete(resource, partition, attributes);
		logger.debug("RhoconnectController#delete: id = " + id);
		
		HttpHeaders responseHeaders = new HttpHeaders();
	    responseHeaders.setContentType(MediaType.TEXT_PLAIN);	    
        return new ResponseEntity<String>(Integer.toString(id), responseHeaders, HttpStatus.OK);
    }
}
