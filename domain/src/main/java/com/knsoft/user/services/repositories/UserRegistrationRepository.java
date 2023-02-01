package com.knsoft.user.services.repositories;

import com.knsoft.user.model.UserRegistration;

public interface UserRegistrationRepository {

    public UserRegistration create(UserRegistration userRegistration);
    public void delete(UserRegistration userRegistration);
}
