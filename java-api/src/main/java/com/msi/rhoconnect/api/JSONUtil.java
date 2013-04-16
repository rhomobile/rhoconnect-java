package com.msi.rhoconnect.api;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;

public class JSONUtil {
  private static ObjectMapper mapper;
  static {
    mapper = new ObjectMapper();
    mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_EMPTY);
    mapper.disable(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS);
  }

  public static <T> String toJSONString(T object) {
    try {
      return mapper.writeValueAsString(object);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "";
  }

}
