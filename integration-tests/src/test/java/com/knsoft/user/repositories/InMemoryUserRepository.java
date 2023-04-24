package com.knsoft.user.repositories;

import com.knsoft.commons.data.Page;
import com.knsoft.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryUserRepository implements UserRepository {

    private final Map<String, User> userMap = new HashMap<>();

    @Override
    public Page<User> findAllUsers(int start, int end, int resultPerPage) {
        List<User> userList = new ArrayList<>(userMap.values());
        int totalResults = userList.size();

        if (end >= totalResults) {
            end = totalResults - 1;
        }

        if (start > end) {
            return new Page.Builder<User>()
                    .withCurrentPage(0)
                    .withNumberOfPages(0)
                    .withResultsPerPage(resultPerPage)
                    .withResults(new ArrayList<>())
                    .build();
        }

        userList = userList.subList(start, end + 1);
        int totalPage = (int) Math.ceil((double) totalResults / resultPerPage);

        return new Page.Builder<User>()
                .withCurrentPage(0)
                .withNumberOfPages(totalPage)
                .withResultsPerPage(resultPerPage)
                .withResults(userList)
                .build();
    }

    @Override
    public Optional<User> findUserById(String uid) {
        User user = userMap.get(uid);
        return user != null ? Optional.of(user) : Optional.empty();
    }

    @Override
    public User create(User user) {
        userMap.put(user.getUid(), user);
        return user;
    }

    @Override
    public User update(String uid, User user) {
        userMap.put(uid, user);
        return user;
    }

    @Override
    public void delete(String uid) {
        userMap.remove(uid);
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return userMap.values().stream()
                .filter(user -> email.equalsIgnoreCase(user.getEmail()))
                .findFirst();
    }

    @Override
    public Optional<User> findUserByUserName(String username) {
        return userMap.values().stream()
                .filter(user -> username.equalsIgnoreCase(user.getUserName()))
                .findFirst();
    }
}
