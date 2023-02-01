package com.knsoft.user.services.repositories;

import com.knsoft.commons.data.Page;
import com.knsoft.user.model.User;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

/**
 * An interface that defines the contract for accessing and manipulating user data.
 */
public interface UserRepository {

    /**
     * Retrieves a paginated list of all users for a given application.
     *
     * @param applicationName the name of the application
     * @param start the starting index for pagination
     * @param end the ending index for pagination
     * @param resultPerPage the number of results per page
     * @return a list of users
     */
    Page<User> findAll(String applicationName, int start, int end, int resultPerPage);

    /**
     * Retrieves a user by their unique identifier and the application they belong to.
     *
     * @param applicationName the name of the application
     * @param uid the unique identifier of the user
     * @return an optional containing the user if found, empty otherwise
     */
    Optional<User> find(String applicationName, String uid);

    /**
     * Creates a new user for a given application.
     *
     * @param applicationName the name of the application
     * @param user the user to create
     * @return the created user
     */
    User create(String applicationName, User user);

    /**
     * Updates an existing user for a given application.
     *
     * @param applicationName the name of the application
     * @param uid the unique identifier of the user
     * @param user the updated user information
     * @return the updated user
     */
    User update(String applicationName, String uid, User user);

    /**
     * Deletes a user by their unique identifier and the application they belong to.
     *
     * @param applicationName the name of the application
     * @param uid the unique identifier of the user
     */
    void delete(String applicationName, String uid);
}
