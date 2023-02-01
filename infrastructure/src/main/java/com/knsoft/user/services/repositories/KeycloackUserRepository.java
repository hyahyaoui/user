package com.knsoft.user.services.repositories;


import com.knsoft.commons.exceptions.TechnicalException;
import com.knsoft.user.mappers.UserMapper;
import com.knsoft.user.model.User;
import org.keycloak.common.util.KeycloakUriBuilder;
import org.keycloak.representations.account.UserRepresentation;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId;


public class KeycloackUserRepository implements UserRepository {

    private final WebClient webClient;

    public KeycloackUserRepository(WebClient webClient) {
        this.webClient = webClient;
    }


    @Override
    public List<User> findAll(String realm, int start, int end, int resultPerPage) {
        return null;
    }

    @Override
    public List<User> findAll(String realm) {
        if (realm == null) {
            throw new IllegalArgumentException("realm cannot be null");
        }
        return webClient.get()
                .uri("http://localhost:8081/admin/realms/" + realm + "/users")
                .attributes(clientRegistrationId(realm))
                .retrieve()
                .onStatus(HttpStatus -> HttpStatus.isError(), response -> {
                    // handle error
                    return Mono.error(new TechnicalException(response.statusCode().toString()));
                })
                .bodyToMono(new ParameterizedTypeReference<List<UserRepresentation>>() {
                })
                .block()
                .stream()
                .map(userRepresentation -> UserMapper.mapUserRepresentationToUser(userRepresentation))
                .collect(Collectors.toList());
    }

    @Override
    public void create(String realm, User user) {
        if (realm == null) {
            throw new IllegalArgumentException("realm cannot be null");
        }
    }

    @Override
    public void find(String realm, String uid) {
        if (realm == null) {
            throw new IllegalArgumentException("realm cannot be null");
        }
    }

    @Override
    public void update(String realm, String uid, User user) {
        if (realm == null) {
            throw new IllegalArgumentException("realm cannot be null");
        }
    }

    @Override
    public void delete(String realm, String uid) {
        if (realm == null) {
            throw new IllegalArgumentException("realm cannot be null");
        }
    }
}
