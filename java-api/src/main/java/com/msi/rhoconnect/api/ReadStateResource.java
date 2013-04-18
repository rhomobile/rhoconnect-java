package com.msi.rhoconnect.api;

import java.util.HashMap;
import java.util.Map;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class ReadStateResource {
  // Sets source poll interval to “current time plus x seconds”.
  // Calling method with refreshTime = null will trigger a refresh on the sync
  // request for the source.
  public static ClientResponse setPollInterval(String url, String token, String userId, String sourceName,
      Integer refreshTime) {
    Client client = Client.create();
    // PUT /rc/v1/readstate/users/:user_id/sources/:source_id
    String path = String.format("%s/rc/v1/readstate/users/%s/sources/%s", url, userId, sourceName);
    WebResource webResource = client.resource(path);

    Map<String, Object> obj = new HashMap<String, Object>();
    if (refreshTime != null) {
      obj.put("refresh_time", refreshTime);
    }
    String content = JSONUtil.toJSONString(obj);
    // System.out.println(content);

    ClientResponse response = webResource.type("application/json").header("X-RhoConnect-API-TOKEN", token)
        .put(ClientResponse.class, content);

    return response;
  }
}
