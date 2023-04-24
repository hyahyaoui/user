package com.knsoft.user.repositories;

import com.knsoft.user.model.UserRegistrationRequest;

import java.util.List;
import java.util.Optional;

/**
 * An interface that defines the contract for accessing and manipulating user registration data.
 */
public interface UserRegistrationRequestRepository {


    UserRegistrationRequest create(UserRegistrationRequest userRegistrationRequest);


    Optional<UserRegistrationRequest> findRegistrationRequestById(String registrationRequestId);


    void delete(String registrationRequestId);


    List<UserRegistrationRequest> findRegistrationRequestBefore(long creationDateThreshold);

    Optional<UserRegistrationRequest> findRegistrationRequestByUserUid(String uid);
}
