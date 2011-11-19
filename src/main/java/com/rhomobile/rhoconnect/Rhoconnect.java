package com.rhomobile.rhoconnect;

import java.util.Map;

public interface Rhoconnect {
    boolean authenticate(String login, String password, Map<String, Object> attributes);
}
