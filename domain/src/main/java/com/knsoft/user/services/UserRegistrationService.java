package com.knsoft.user.services;

import com.knsoft.user.model.User;
import com.knsoft.user.model.UserRegistration;
import com.knsoft.user.services.repositories.UserRegistrationRepository;

import java.util.List;
import java.util.UUID;

public class UserRegistrationService {

    private final UserRegistrationRepository userRegistrationRepository;
    private final UserService userService;

    public UserRegistrationService(UserRegistrationRepository userRegistrationRepository, UserService userService) {
        this.userRegistrationRepository = userRegistrationRepository;
        this.userService = userService;
    }

    public User register(String applicationName, User user) {
        UserRegistration userRegistration = new UserRegistration();
        userRegistration.setId(UUID.randomUUID().toString());
        userRegistration.setUid(user.getUid());
        userRegistration.setCreationDate(0);
        return userService.create(applicationName, user);
    }

    public void invalidate(String registrationId) {
        final UserRegistration userRegistration = userRegistrationRepository.findById(registrationId);
        userRegistrationRepository.delete(registrationId);
        userService.delete(userRegistration.getApplicationName(), userRegistration.getId());
    }

    public void invalidateExpiredRegistrations(long lapseTimeInMilliseconds) {
        final long creationDateThreshold = System.currentTimeMillis() - lapseTimeInMilliseconds;
        final List<UserRegistration> expiredRegistrations = userRegistrationRepository.findByCreationDateLessThan(creationDateThreshold);
        for (UserRegistration expiredRegistration : expiredRegistrations) {
            invalidate(expiredRegistration.getId());
        }
    }

}
