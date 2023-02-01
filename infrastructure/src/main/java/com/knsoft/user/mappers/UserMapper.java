package com.knsoft.user.mappers;

import com.knsoft.user.model.User;
import org.keycloak.representations.account.UserRepresentation;

public class UserMapper {
    public static User mapUserRepresentationToUser(UserRepresentation userRepresentation) {
        User user = new User();
        user.uid = userRepresentation.getUsername();
        user.firstName = userRepresentation.getFirstName();
        user.lastName = userRepresentation.getLastName();
        return user;
    }
}