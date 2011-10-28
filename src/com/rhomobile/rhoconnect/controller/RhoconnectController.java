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

@Controller
@RequestMapping("/rhoconnect/*")
public class RhoconnectController {
    //get log4j handler
	private static final Logger logger = Logger.getLogger(RhoconnectController.class);	

	@Autowired
	private Rhoconnect rhoconnect;

	// POST /rhoconnect/authenticate
	@RequestMapping(method=RequestMethod.POST, value="authenticate", headers="Accept=application/json")
	public ResponseEntity<String> authenticate(@RequestBody Map<String, Object> body) {
		logger.info("RhoconnectController#authenticate " + body.toString());
		String login = (String)body.get("login");
		String password = (String)body.get("password");
		
		HttpHeaders responseHeaders = new HttpHeaders();
	    responseHeaders.setContentType(MediaType.TEXT_PLAIN);
	    
	    boolean authorized = rhoconnect.authenticate(login, password, body);
	    if(!authorized) // In case of error/exception return UNAUTHORIZED(401)
	    	return new ResponseEntity<String>("Authentication has failed", responseHeaders, HttpStatus.UNAUTHORIZED);
		
        return new ResponseEntity<String>(login, responseHeaders, HttpStatus.OK);
    }

    // POST /rhoconnect/query
	@RequestMapping(method=RequestMethod.POST, value="/query", headers="Accept=application/json")	
	public @ResponseBody Map<String, Object> query_objects(@RequestBody Map<String, Object> body) {
		logger.info("RhoconnectController#query_objects");
		logger.info(body.toString());
		//
		// { resource=Contact, partition=alexb, attributes=null, api_token=sometokenforme}
		//
		
		String resource = (String)body.get("resource");
		String partition = (String)body.get("partition");
		Map<String, Object> h = rhoconnect.query_objects(resource, partition);		
		logger.info(h.toString());

		return h;
    } 

	 // POST /rhoconnect/create
	@RequestMapping(method=RequestMethod.POST, value="create", headers="Accept=application/json")
	public ResponseEntity<String> create(@RequestBody Map<String, Object> body) {
		logger.info("RhoconnectController#create: request");
		logger.info(body.toString());
		//
		// {resource=Contact, partition="", 
		//  attributes={telephone=me@mail.com, lastname=world, firstname=hello, email=123-456-6789}, 
		//  api_token=sometokenforme}
		// 

		String resource = (String)body.get("resource");
		String partition = (String)body.get("partition");
		Map<String, Object> attributes = (Map<String, Object>) body.get("attributes");
		Integer id = rhoconnect.create(resource, partition, attributes);
		logger.info("RhoconnectController#create: id = " + id);
		
	    HttpHeaders responseHeaders = new HttpHeaders();
	    responseHeaders.setContentType(MediaType.TEXT_PLAIN);
        return new ResponseEntity<String>(Integer.toString(id), responseHeaders, HttpStatus.OK);        
	}
	
	// POST /rhoconnect/update
	@RequestMapping(method=RequestMethod.POST, value="update", headers="Accept=application/json")
	public ResponseEntity<String> update(@RequestBody Map<String, Object> body) {
		logger.info("RhoconnectController#update: request");
		logger.info(body.toString());
		//		
		//{ resource=Contact, partition="", attributes={firstname=Maxz, id=29}, api_token=sometokenforme}
		//

		String resource = (String)body.get("resource");
		String partition = (String)body.get("partition");
		Map<String, Object> attributes = (Map<String, Object>) body.get("attributes");
		Integer id = rhoconnect.update(resource, partition, attributes);
		logger.info("RhoconnectController#update: id = " + id);
		
	    HttpHeaders responseHeaders = new HttpHeaders();
	    responseHeaders.setContentType(MediaType.TEXT_PLAIN);	    
        return new ResponseEntity<String>(Integer.toString(id), responseHeaders, HttpStatus.OK);
    }
	
	// POST /rhoconnect/delete
	@RequestMapping(method=RequestMethod.POST, value="delete", headers="Accept=application/json")
	public ResponseEntity<String> delete(@RequestBody Map<String, Object> body) {
		logger.info("RhoconnectController#delete: request");
		logger.info(body.toString());
		//		
		// { resource=Contact, partition="", 
		//   attributes={email=maxz@mail.ru, telephone=650-666-7878, firstname=Maxz, lastname=Zverev, id=29}, 
		//   api_token=sometokenforme}
		//

		String resource = (String)body.get("resource");
		String partition = (String)body.get("partition");		
		Map<String, Object> attributes = (Map<String, Object>) body.get("attributes");
		Integer id = rhoconnect.delete(resource, partition, attributes);
		logger.info("RhoconnectController#delete: id = " + id);
		
		HttpHeaders responseHeaders = new HttpHeaders();
	    responseHeaders.setContentType(MediaType.TEXT_PLAIN);	    
        return new ResponseEntity<String>(Integer.toString(id), responseHeaders, HttpStatus.OK);
    }

}
