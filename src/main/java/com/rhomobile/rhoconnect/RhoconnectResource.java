package com.rhomobile.rhoconnect;

import java.util.Map;

public interface RhoconnectResource {
	Map<String, Object> rhoconnectQuery(String partition);
	Integer rhoconnectCreate(String partition, Map<String, Object> attributes);
	Integer rhoconnectUpdate(String partition, Map<String, Object> attributes);
	Integer rhoconnetDelete(String partition, Map<String, Object> attributes);
}
