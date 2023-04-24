package com.knsoft.user.repositories;

import com.knsoft.commons.data.Page;
import com.knsoft.user.model.User;

import java.util.Optional;

/**
 * An interface that defines the contract for accessing and manipulating user data.
 */
public interface UserRepository {

    /**
     * Retrieves a paginated list of all users for a given application.
     *
     * @param start         the starting index for pagination
     * @param end           the ending index for pagination
     * @param resultPerPage the number of results per page
     * @return a {@link Page} of {@link User} objects
     */
    Page<User> findAllUsers(int start, int end, int resultPerPage);

    /**
     * Retrieves a user by their unique identifier and the application they belong to.
     *
     * @param uid the unique identifier of the user
     * @return an {@link Optional} containing the {@link User} if found, empty otherwise
     */
    Optional<User> findUserById(String uid);


    Optional<User> findUserByEmail(String email);

    Optional<User> findUserByUserName(String email);

    /**
     * Creates a new user for a given application.
     *
     * @param user the user to create
     * @return the created {@link User} object
     */
    User create(User user);

    /**
     * Updates an existing user for a given application.
     *
     * @param uid  the unique identifier of the user
     * @param user the updated user information
     * @return the updated {@link User} object
     */
    User update(String uid, User user);

    /**
     * Deletes a user by their unique identifier and the application they belong to.
     *
     * @param uid the unique identifier of the user
     */
    void delete(String uid);

}
