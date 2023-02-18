package com.knsoft.user.services;

import com.knsoft.commons.exceptions.EntityNotFoundException;
import com.knsoft.user.exceptions.RegistrationRequestExpiredException;
import com.knsoft.user.model.User;
import com.knsoft.user.model.UserRegistrationRequest;
import com.knsoft.user.repositories.UserRegistrationRequestRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * The {@code UserRegistrationService} class provides the ability to register a new user and invalidate an existing user registration.
 *
 * <p>This class contains methods for registering a user, invalidating a user registration, and invalidating expired user registrations.
 * The user registration information is stored in a {@link UserRegistrationRequestRepository} and the user information is managed by a {@link UserService}.
 *
 * @author KnSoft
 * @version 1.0
 * @see UserRegistrationRequest
 * @see UserRegistrationRequestRepository
 * @see UserService
 */
public class UserRegistrationService {
    private final UserRegistrationRequestRepository userRegistrationRequestRepository;
    private final UserService userService;

    public long getRegistrationInvalidationLapseInMinute() {
        return registrationInvalidationLapseInMinute;
    }

    private final long registrationInvalidationLapseInMinute;
    /**
     * Constructs a new {@code UserRegistrationService} instance.
     *
     * @param userRegistrationRequestRepository     the repository to manage {@link UserRegistrationRequest} objects
     * @param userService                           the service to manage {@link User} objects
     * @param registrationInvalidationLapseInMinute
     */
    public UserRegistrationService(UserRegistrationRequestRepository userRegistrationRequestRepository,
                                   UserService userService,
                                   long registrationInvalidationLapseInMinute) {
        this.userRegistrationRequestRepository = userRegistrationRequestRepository;
        this.userService = userService;
        this.registrationInvalidationLapseInMinute = registrationInvalidationLapseInMinute;
    }

    /**
     * Registers a new user with the given application name and user information.
     *
     * @param user the user information for the new user
     * @return the registered user
     */
    /**
     * Registers a new user with the given application name and user information.
     *
     * @param user the user information for the new user
     * @return the registered user
     */
    public User register(User user) {
        UserRegistrationRequest userRegistrationRequest = new UserRegistrationRequest();
        userRegistrationRequest.setUserUid(user.getUid());
        userRegistrationRequest.setRegistrationRequestDate(Instant.now().toEpochMilli());
        user.setStatus(User.Status.INACTIVE);
        userRegistrationRequestRepository.create(userRegistrationRequest);
        return userService.create(user);
    }

    /**
     * Validates the registration request with the given validation code.
     * This method finds the registration request by searching for a matching UUID,
     * updates the user status to active, and deletes the registration request.
     *
     * @param registrationCode the UUID of the registration request to validate.
     * @throws {@code EntityNotFoundException} if no registration request is found with the given UUID.
     */
    public void validateRegistration(String registrationCode) {
        Optional<UserRegistrationRequest> userRegistrationRequest = userRegistrationRequestRepository.
                findRegistrationRequestById(registrationCode);
        if (userRegistrationRequest.isEmpty()) {
            throw new EntityNotFoundException("User registration not found with ID " + registrationCode);
        }

        // check if the registration is still valid
        long registrationRequestDate = userRegistrationRequest.get().getRegistrationRequestDate();
        long currentDate = Instant.now().toEpochMilli();
        long expirationTime = registrationRequestDate + registrationInvalidationLapseInMinute * 60 * 1000;
        if (currentDate > expirationTime) {
            throw new RegistrationRequestExpiredException("Registration request with ID " + registrationCode + " has expired");
        }

        String uid = userRegistrationRequest.get().getUserUid();
        User user = userService.findUserByUid(uid)
                .orElseThrow(() -> new EntityNotFoundException("User not found with UID " + uid));

        user.setStatus(User.Status.ACTIVE);
        userService.update(uid, user);
        userRegistrationRequestRepository.delete(userRegistrationRequest.get().getUid());
    }

    /**
     * Invalidates the user registration with the given registration ID.
     *
     * @param registrationId the ID of the user registration to invalidate
     */
    private void invalidateRegistration(String registrationId) {
        final Optional<UserRegistrationRequest> userRegistration = userRegistrationRequestRepository.findRegistrationRequestById(registrationId);
        userRegistrationRequestRepository.delete(registrationId);
        userService.delete(userRegistration.get().getUserUid());
    }

    /**
     * Invalidates all user registrations that were created before the given time lapse.
     *
     * @param lapseTimeInMilliseconds the amount of time in milliseconds to consider an user registration as expired
     */
    public void invalidateExpiredRegistrations(long lapseTimeInMilliseconds) {
        final long creationDateThreshold = System.currentTimeMillis() - lapseTimeInMilliseconds;
        final List<UserRegistrationRequest> expiredRegistrations = userRegistrationRequestRepository.findRegistrationBefore(creationDateThreshold);
        for (UserRegistrationRequest expiredRegistration : expiredRegistrations) {
            invalidateRegistration(expiredRegistration.getUid());
        }
    }
}