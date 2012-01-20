package com.rhomobile.rhoconnect;

import java.util.Map;

public interface Rhoconnect {
    String authenticate(String userName, String password, Map<String, Object> attributes);
}
