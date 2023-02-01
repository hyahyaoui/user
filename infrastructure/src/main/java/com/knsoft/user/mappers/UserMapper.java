package com.knsoft.user.mappers;

import com.knsoft.user.model.User;
import org.keycloak.representations.idm.UserRepresentation;

public class UserMapper {
    public static User toUser(UserRepresentation userRepresentation) {
        User user = new User();
        user.setUid(userRepresentation.getId());
        user.setUserName(userRepresentation.getUsername());
        user.setFirstName(userRepresentation.getFirstName());
        user.setLastName(userRepresentation.getLastName());
        return user;
    }

    public static UserRepresentation toUserRepresentation(User user) {
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setId(user.getUid());
        userRepresentation.setUsername(user.getUserName());
        userRepresentation.setFirstName(user.getFirstName());
        userRepresentation.setLastName(user.getLastName());
        return userRepresentation;
    }
}