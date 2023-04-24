package com.knsoft.user.services;

import com.knsoft.commons.data.Page;
import com.knsoft.commons.data.exceptions.EntityAlreadyExistsException;
import com.knsoft.commons.data.exceptions.EntityNotFoundException;
import com.knsoft.user.model.User;
import com.knsoft.user.repositories.UserRepository;

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
     * @return a list of users.
     * @throws IllegalArgumentException if applicationName is null or empty.
     */
    public Page<User> findAll(int start, int end, int resultsPerPage) {
        return userRepository.findAllUsers(start, end, resultsPerPage);
    }


    /**
     * Returns an Optional containing the user with the specified uid,
     * or an empty Optional if no such user exists.
     *
     * @param uid the uid of the user.
     * @return an Optional containing the user.
     * @throws IllegalArgumentException if applicationName or uid is null or empty.
     */
    public Optional<User> findUserByUid(String uid) {
        validateUid(uid);
        return userRepository.findUserById(uid);
    }

    /**
     * Creates a new user and returns it.
     *
     * @param user the user to be created.
     * @return the created user.
     * @throws IllegalArgumentException if applicationName or user is null or empty.
     */
    public User create(User user) {
        validateUserToAdd(user);
        return userRepository.create(user);
    }


    /**
     * Updates a User in the specified application with the specified uid.
     *
     * @param uid  the unique identifier of the user to update
     * @param user the updated user information
     * @return the updated user
     * @throws IllegalArgumentException if the `applicationName` is null or empty
     * @throws IllegalArgumentException if the `uid` is null or empty
     * @throws IllegalArgumentException if the `user` is null
     * @throws EntityNotFoundException  if no user is found with the specified uid
     */
    public User update(String uid, User user) {
        validateUid(uid);
        validateUserToUpdate(user);
        findUserByUid(uid).orElseThrow(() ->
                new EntityNotFoundException("No user found with uid " + uid));
        return userRepository.update(uid, user);
    }

    public User activateUser(String uid) {
        validateUid(uid);
        final User user = findUserByUid(uid).orElseThrow(() ->
                new EntityNotFoundException("No user found with uid " + uid));
        user.setStatus(User.Status.ACTIVE);
        return userRepository.update(uid, user);
    }

    public Optional<User> findByUserName(String userName) {
        validateUserName(userName);
        return userRepository.findUserByUserName(userName);
    }

    /**
     * Deletes a user for the given applicationName and uid.
     *
     * @param uid the unique identifier of the user.
     * @throws IllegalArgumentException if applicationName is null or empty.
     * @throws IllegalArgumentException if uid is null or empty.
     * @throws EntityNotFoundException  if no user is found with the given uid in the specified applicationName.
     */
    public void delete(String uid) {
        validateUid(uid);
        findUserByUid(uid).orElseThrow(() ->
                new EntityNotFoundException("No user found with uid " + uid));
        userRepository.delete(uid);
    }

    private void validateUid(String uid) {
        if (uid == null || uid.trim().isEmpty()) {
            throw new IllegalArgumentException("uid must not be null or empty");
        }
    }
    private void validateUserName(String userName) {
        if (userName == null || userName.trim().isEmpty()) {
            throw new IllegalArgumentException("userName must not be null or empty");
        }
    }


    private void validateUserToAdd(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User must not be null");
        }

        validateEmailUniquenessForAdd(user.getEmail());
        validateUserNameUniquenessForAdd(user.getUserName());
    }

    private void validateUserToUpdate(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User must not be null");
        }

        validateEmailUniquenessForUpdate(user);
        validateUserNameUniquenessForUpdate(user);
    }

    private void validateEmailUniquenessForAdd(String email) {
        final Optional<User> user = userRepository.findUserByEmail(email);
        if (!user.isEmpty()) {
            throw new EntityAlreadyExistsException("User with email " + email + " already exists");
        }
    }

    private void validateUserNameUniquenessForAdd(String userName) {
        final Optional<User> user = userRepository.findUserByUserName(userName);
        if (!user.isEmpty()) {
            throw new EntityAlreadyExistsException("User with email " + userName + " already exists");
        }
    }

    private void validateEmailUniquenessForUpdate(User userToValidate) {
        final Optional<User> user = userRepository.findUserByEmail(userToValidate.getEmail());
        if (!user.isEmpty() && user.get().getUid() != userToValidate.getUid()) {
            throw new EntityAlreadyExistsException("User with email " + userToValidate.getEmail() + " already exists");
        }
    }

    private void validateUserNameUniquenessForUpdate(User userToValidate) {
        final Optional<User> user = userRepository.findUserByUserName(userToValidate.getUserName());
        if (!user.isEmpty() && !user.get().getUid().equals(userToValidate.getUid())) {
            throw new EntityAlreadyExistsException("User with username " + userToValidate.getUserName() + " already exists");
        }
    }


}
