package com.knsoft.user.services;

import com.knsoft.commons.data.Page;
import com.knsoft.commons.exceptions.EntityNotFoundException;
import com.knsoft.user.model.User;
import com.knsoft.user.services.repositories.UserRepository;

import java.util.Optional;

/**
 * The UserService class provides methods for performing operations on user entities.
 * It uses the UserRepository for accessing user data from persistence layer.
 *
 * @author KNsoft
 * @since 1.0.0
 */
public class UserService {

    private final UserRepository userRepository;

    /**
     * Creates a new instance of the UserService class.
     *
     * @param userRepository the UserRepository to use for accessing user data from persistence layer.
     */
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Returns a list of all users associated with the specified application.
     *
     * @param applicationName the name of the application.
     * @return a list of users.
     * @throws IllegalArgumentException if applicationName is null or empty.
     */
    public Page<User> findAll(String applicationName, int start, int end, int resultsPerPage) {
        validateApplicationName(applicationName);
        return userRepository.findAll(applicationName, start, end, resultsPerPage);
    }


    /**
     * Returns an Optional containing the user with the specified uid,
     * or an empty Optional if no such user exists.
     *
     * @param applicationName the name of the application.
     * @param uid             the uid of the user.
     * @return an Optional containing the user.
     * @throws IllegalArgumentException if applicationName or uid is null or empty.
     */
    public Optional<User> find(String applicationName, String uid) {
        validateApplicationName(applicationName);
        validateUid(uid);
        return userRepository.find(applicationName, uid);
    }

    /**
     * Creates a new user and returns it.
     *
     * @param applicationName the name of the application.
     * @param user            the user to be created.
     * @return the created user.
     * @throws IllegalArgumentException if applicationName or user is null or empty.
     */
    public User create(String applicationName, User user) {
        validateApplicationName(applicationName);
        validateUser(user);
        return userRepository.create(applicationName, user);
    }


    /**
     * Updates a User in the specified application with the specified uid.
     *
     * @param applicationName the name of the application where the user resides
     * @param uid             the unique identifier of the user to update
     * @param user            the updated user information
     * @return the updated user
     * @throws IllegalArgumentException if the `applicationName` is null or empty
     * @throws IllegalArgumentException if the `uid` is null or empty
     * @throws IllegalArgumentException if the `user` is null
     * @throws EntityNotFoundException  if no user is found with the specified uid
     */
    public User update(String applicationName, String uid, User user) {
        validateApplicationName(applicationName);
        validateUid(uid);
        validateUser(user);
        find(applicationName, uid).orElseThrow(() ->
                new EntityNotFoundException("No user found with uid " + uid));
        return userRepository.update(applicationName, uid, user);
    }

    /**
     * Deletes a user for the given applicationName and uid.
     *
     * @param applicationName the name of the application.
     * @param uid             the unique identifier of the user.
     * @throws IllegalArgumentException if applicationName is null or empty.
     * @throws IllegalArgumentException if uid is null or empty.
     * @throws EntityNotFoundException  if no user is found with the given uid in the specified applicationName.
     */
    public void delete(String applicationName, String uid) {
        validateApplicationName(applicationName);
        validateUid(uid);
        find(applicationName, uid).orElseThrow(() ->
                new EntityNotFoundException("No user found with uid " + uid));
        userRepository.delete(applicationName, uid);
    }

    private void validateUid(String uid) {
        if (uid == null || uid.trim().isEmpty()) {
            throw new IllegalArgumentException("uid must not be null or empty");
        }
    }

    private void validateApplicationName(String applicationName) {
        if (applicationName == null || applicationName.trim().isEmpty()) {
            throw new IllegalArgumentException("applicationName must not be null or empty");
        }
    }

    private void validateUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User must not be null");
        }
    }



}
