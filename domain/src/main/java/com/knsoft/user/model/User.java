package com.knsoft.user.model;

/**
 * The User class represents a user with a unique identifier, username, first name and last name.
 *
 * @author YourName
 */
public class User {

    /**
     * A unique identifier for the user
     */
    private String uid;

    /**
     * The username of the user
     */
    private String userName;

    /**
     * The first name of the user
     */
    private String firstName;

    /**
     * The last name of the user
     */
    private String lastName;

    /**
     * Gets the unique identifier of the user
     *
     * @return the unique identifier of the user
     */
    public String getUid() {
        return uid;
    }

    /**
     * Sets the unique identifier of the user
     *
     * @param uid the unique identifier of the user
     */
    public void setUid(String uid) {
        this.uid = uid;
    }

    /**
     * Gets the username of the user
     *
     * @return the username of the user
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets the username of the user
     *
     * @param userName the username of the user
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Gets the first name of the user
     *
     * @return the first name of the user
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of the user
     *
     * @param firstName the first name of the user
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the last name of the user
     *
     * @return the last name of the user
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of the user
     *
     * @param lastName the last name of the user
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}