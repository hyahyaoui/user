package com.company.keycloak.services;

import com.company.keycloak.model.User;
import reactor.core.publisher.Mono;

import java.util.List;

public interface UserRepository {

    List<User> findAll(int start, int end, int resultPerPage);

    List<User> findAll(String applicationName);

    void find(String uid);

    void create(User user);

    void update(String uid, User user);

    void delete(String uid);
}
