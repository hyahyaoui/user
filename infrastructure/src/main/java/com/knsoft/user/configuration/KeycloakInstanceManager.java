package com.knsoft.user.configuration;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * KeycloakInstanceManager is a class responsible for managing Keycloak instances.
 * It contains a map of Keycloak instances, where the key is the realm name and the value is the Keycloak instance.
 * The class also has the necessary setters and getters to set the Keycloak configuration properties.
 *
 * @author  KnSoft
 * @version 1.0
 * @since 1.0.0
 */
@Component
@ConfigurationProperties(prefix = "keycloak")
public class KeycloakInstanceManager {

    private String serverUrl;
    private String adminClientId;
    private Map<String, RealmProperties> realms = new HashMap<>();
    private final Map<String, Keycloak> INSTANCES = new HashMap<>();

    /**
     * Returns a Keycloak instance for the specified realm.
     * If the Keycloak instance for the specified realm does not exist, it creates a new one and adds it to the INSTANCES map.
     *
     * @param realmName the name of the realm
     * @return the Keycloak instance for the specified realm
     */
    public Keycloak getInstance(String realmName) {
        if (!INSTANCES.containsKey(realmName)) {
            RealmProperties realmProperties = realms.get(realmName);
            Keycloak realm = KeycloakBuilder.builder()
                    .serverUrl(serverUrl)
                    .realm(realmProperties.getName())
                    .clientId(realmProperties.getClientId())
                    .clientSecret(realmProperties.getClientSecret())
                    .grantType(realmProperties.getGrantType())
                    .build();
            INSTANCES.put(realmName, realm);
        }
        return INSTANCES.get(realmName);
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public String getAdminClientId() {
        return adminClientId;
    }

    public void setAdminClientId(String adminClientId) {
        this.adminClientId = adminClientId;
    }

    public Map<String, RealmProperties> getRealms() {
        return realms;
    }

    public void setRealms(Map<String, RealmProperties> realms) {
        this.realms = realms;
    }

    static class RealmProperties {
        private String name;
        private String clientId;
        private String clientSecret;

        private String grantType;
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getClientId() {
            return clientId;
        }

        public void setClientId(String clientId) {
            this.clientId = clientId;
        }

        public String getClientSecret() {
            return clientSecret;
        }

        public void setClientSecret(String clientSecret) {
            this.clientSecret = clientSecret;
        }

        public String getGrantType() {
            return grantType;
        }

        public void setGrantType(String grantType) {
            this.grantType = grantType;
        }
    }
}
