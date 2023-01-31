package com.company.keycloak.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConfigurationProperties(prefix = "user-app.oauth2")
public class Oauth2ClientConfig {

    private Map<String, Map<String, String>> clients;

    public Map<String, Map<String, String>> getClients() {
        return clients;
    }

    public void setClients(Map<String, Map<String, String>> clients) {
        this.clients = clients;
    }
}
