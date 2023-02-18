package com.knsoft.user.repositories;

import com.knsoft.user.model.UserRegistrationRequest;

import java.util.List;
import java.util.Optional;

/**
 * An interface that defines the contract for accessing and manipulating user registration data.
 */
public interface UserRegistrationRequestRepository {

    /**
     * Creates a new user registration.
     *
     * @param userRegistrationRequest the user registration to create
     * @return the created {@link UserRegistrationRequest} object
     */
    UserRegistrationRequest create(UserRegistrationRequest userRegistrationRequest);

    /**
     * Retrieves a user registration by its unique identifier.
     *
     * @param registrationRequestId the unique identifier of the user registration
     * @return the {@link UserRegistrationRequest} object if found, null otherwise
     */
    Optional<UserRegistrationRequest> findRegistrationRequestById(String registrationRequestId);

    /**
     * Deletes a user registration by its unique identifier.
     *
     * @param registrationRequestId the unique identifier of the user registration
     */
    void delete(String registrationRequestId);

    /**
     * Retrieves a list of user registrations that were created before a given date.
     *
     * @param creationDateThreshold the date threshold to retrieve user registrations before
     * @return a list of {@link UserRegistrationRequest} objects
     */
    List<UserRegistrationRequest> findRegistrationBefore(long creationDateThreshold);

    Optional<UserRegistrationRequest> findRegistrationRequestByUserUid(String uid);
}
