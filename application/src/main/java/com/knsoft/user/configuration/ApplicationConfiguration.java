package com.knsoft.user.configuration;

import com.knsoft.user.services.repositories.KeycloackUserRepository;
import com.knsoft.user.services.repositories.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class ApplicationConfiguration {

    @Bean
    public UserRepository userRepository(WebClient webClient) {
        return new KeycloackUserRepository(webClient);
    }
}
