package com.knsoft.user.configuration;

import com.knsoft.user.services.UserService;
import com.knsoft.user.services.repositories.KeycloakUserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class ApplicationConfiguration {

    @Bean
    public UserService userService(KeycloakInstanceManager keycloakInstanceManager) {
        return new UserService(new KeycloakUserRepository(keycloakInstanceManager));
    }
}
