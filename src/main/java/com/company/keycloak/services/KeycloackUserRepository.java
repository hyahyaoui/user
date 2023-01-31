package com.company.keycloak.services;

import com.company.keycloak.model.User;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId;


@Repository
public class KeycloackUserRepository implements UserRepository {

    private final WebClient webClient;

    public KeycloackUserRepository(WebClient webClient) {
        this.webClient = webClient;
    }


    @Override
    public List<User> findAll(int start, int end, int resultPerPage) {
        return null;
    }

    @Override
    public List<User> findAll(String realm) {
        List<User> list = (List<User>) webClient.get()
                .uri("http://localhost:8081/admin/realms/" + realm + "/users")
                .attributes(clientRegistrationId(realm))
                .retrieve()
                .bodyToMono(List.class)
                .block()
                .stream()
                .collect(Collectors.toList());
        return list;

    }

    @Override
    public void create(User user) {

    }

    @Override
    public void find(String uid) {

    }

    @Override
    public void update(String uid, User user) {

    }

    @Override
    public void delete(String uid) {

    }
}
