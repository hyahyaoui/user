package com.knsoft.user.services.repositories;

import com.knsoft.user.model.UserRegistration;

import java.util.List;

public interface UserRegistrationRepository {

    UserRegistration create(UserRegistration userRegistration);

    UserRegistration findById(String registrationId);

    void delete(String registrationId);

    List<UserRegistration> findByCreationDateLessThan(long creationDateThreshold);
}
