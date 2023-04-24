package com.knsoft.user.model;

import java.util.UUID;

/**
 * The {@code UserRegistrationRequest} class represents a registration request for a user.
 *
 * <p>This class contains the UID of the request, the UID of the user, and the date the request was created.
 * </p>
 *
 * @author hyahyaoui
 * @version 1.0
 * @since 1.0.0
 */
public class UserRegistrationRequest {

    private String uid = UUID.randomUUID().toString();
    private String userUid;
    private long registrationRequestDate;

    public UserRegistrationRequest() {
    }

    public UserRegistrationRequest(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public long getRegistrationRequestDate() {
        return registrationRequestDate;
    }

    public void setRegistrationRequestDate(long registrationRequestDate) {
        this.registrationRequestDate = registrationRequestDate;
    }
}
