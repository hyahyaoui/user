package com.knsoft.user.services.repositories;

import com.knsoft.user.model.User;

import java.util.List;

public interface UserRepository {

    List<User> findAll(String applicationName, int start, int end, int resultPerPage);

    List<User> findAll(String applicationName);

    void find(String applicationName, String uid);

    void create(String applicationName, User user);

    void update(String applicationName, String uid, User user);

    void delete(String applicationName, String uid);
}
