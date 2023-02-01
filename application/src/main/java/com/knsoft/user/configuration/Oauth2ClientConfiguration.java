package com.knsoft.user.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class Oauth2ClientConfiguration {


    @Bean
    WebClient webClient(ClientRegistrationRepository clientRegistrations,
                        OAuth2AuthorizedClientRepository authorizedClients) {
        ServletOAuth2AuthorizedClientExchangeFilterFunction oauth =
                new ServletOAuth2AuthorizedClientExchangeFilterFunction(clientRegistrations, authorizedClients);
        // (optional) explicitly opt into using the oauth2Login to provide an access token implicitly
        oauth.setDefaultOAuth2AuthorizedClient(true);
        // (optional) set a default ClientRegistration.registrationId
        // oauth.setDefaultClientRegistrationId("client-registration-id");
        return WebClient.builder()
                .apply(oauth.oauth2Configuration())
                .build();
    }


    @Bean
    ClientRegistrationRepository clientRegistrationRepository(
            Oauth2ClientConfig oauth2ClientConfig) {

        final List<ClientRegistration> clientRegistrations = oauth2ClientConfig.getClients()
                .values()
                .stream()
                .map(entry -> ClientRegistration.withRegistrationId(entry.get("registration-id"))
                        .clientId(entry.get("client-id"))
                        .clientSecret(entry.get("client-secret"))
                        .authorizationGrantType(new AuthorizationGrantType(entry.get("authorization-grant-type")))
                        .tokenUri(entry.get("token-uri")).build())
                .collect(Collectors.toList());

        return new InMemoryClientRegistrationRepository(clientRegistrations);
    }

}
