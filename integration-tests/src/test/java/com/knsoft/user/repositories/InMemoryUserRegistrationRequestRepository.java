package com.knsoft.user.repositories;

import com.knsoft.user.model.UserRegistrationRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class InMemoryUserRegistrationRequestRepository implements UserRegistrationRequestRepository {

    private final List<UserRegistrationRequest> userRegistrationRequests = new ArrayList<>();

    @Override
    public UserRegistrationRequest create(UserRegistrationRequest userRegistrationRequest) {
        userRegistrationRequests.add(userRegistrationRequest);
        return userRegistrationRequest;
    }

    @Override
    public Optional<UserRegistrationRequest> findRegistrationRequestById(String registrationRequestId) {
        return userRegistrationRequests.stream()
                .filter(r -> r.getUid().equals(registrationRequestId))
                .findFirst();
    }

    @Override
    public void delete(String registrationRequestId) {
        userRegistrationRequests.removeIf(r -> r.getUid().equals(registrationRequestId));
    }

    @Override
    public List<UserRegistrationRequest> findRegistrationRequestBefore(long creationDateThreshold) {
        return userRegistrationRequests.stream()
                .filter(r -> r.getRegistrationRequestDate() < creationDateThreshold)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UserRegistrationRequest> findRegistrationRequestByUserUid(String uid) {
        return userRegistrationRequests.stream()
                .filter(r -> r.getUserUid().equals(uid))
                .findFirst();
    }
}
