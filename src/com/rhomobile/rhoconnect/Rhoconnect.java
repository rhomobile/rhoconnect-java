package com.rhomobile.rhoconnect;

import java.util.Map;

public interface Rhoconnect {

	boolean authenticate(String login, String password, Map<String, Object> attribures);

	Map<String, Object> query_objects(String resource, String partition);
	Integer create(String resource, String partition, Map<String, Object> attributes);
	Integer update(String resource, String partition, Map<String, Object> attributes);
	Integer delete(String resource, String partition, Map<String, Object> attributes);

}
