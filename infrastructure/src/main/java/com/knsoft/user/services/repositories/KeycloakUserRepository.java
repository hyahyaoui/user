package com.knsoft.user.services.repositories;


import com.knsoft.commons.data.Page;
import com.knsoft.user.configuration.KeycloakInstanceManager;
import com.knsoft.user.mappers.UserMapper;
import com.knsoft.user.model.User;
import com.knsoft.user.repositories.UserRepository;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * KeycloakUserRepository class implements the {@link UserRepository} interface.
 * It implements the CRUD operations of the users in Keycloak identity management.
 *
 * @author KNsoft
 * @version 1.0
 * @since 1.0
 */
public class KeycloakUserRepository implements UserRepository {

    private final KeycloakInstanceManager keycloakInstanceManager;

    private final String REALM;
    private final Logger LOGGER = LoggerFactory.getLogger(KeycloakUserRepository.class);


    /**
     * Constructor to create an instance of KeycloakUserRepository
     *
     * @param keycloakInstanceManager instance of KeycloakInstanceManager class
     * @param realm
     */
    public KeycloakUserRepository(KeycloakInstanceManager keycloakInstanceManager, String realm) {
        this.keycloakInstanceManager = keycloakInstanceManager;
        REALM = realm;
    }

    /**
     * Retrieves a list of users for a given realm from Keycloak using KeycloakAdminClient.
     *
     * @param start          the starting position of the range of users to retrieve
     * @param end            the ending position of the range of users to retrieve
     * @param resultsPerPage the number of users to retrieve per page
     * @return a Page object containing the results of the query, including the current page, the number of pages,
     * the number of results per page, and the list of users
     */
    @Override
    public Page<User> findAllUsers(int start, int end, int resultsPerPage) {

        if (start < 0 || end < 0 || resultsPerPage < 1) {
            LOGGER.error("start, end, and resultsPerPage must be non-negative and resultsPerPage must be greater than 0");
            throw new IllegalArgumentException("start, end, and resultsPerPage must be non-negative, and resultsPerPage must be greater than 0");
        }
        if (start > end) {
            LOGGER.error("start must be less than or equal to end");
            throw new IllegalArgumentException("start must be less than or equal to end");
        }

        try {
            List<UserRepresentation> userRepresentations = keycloakInstanceManager.getInstance(REALM)
                    .realm(REALM)
                    .users()
                    .list(start, resultsPerPage);

            List<User> users = userRepresentations.stream()
                    .map(UserMapper::toUser)
                    .collect(Collectors.toList());

            int numberOfPages = (int) Math.ceil((double) keycloakInstanceManager.getInstance(REALM)
                    .realm(REALM)
                    .users()
                    .count() / resultsPerPage);

            LOGGER.debug("Found {} users for realm {}", users.size(), REALM);
            return new Page.Builder<User>()
                    .withCurrentPage(start / resultsPerPage + 1)
                    .withNumberOfPages(numberOfPages)
                    .withResultsPerPage(resultsPerPage)
                    .withResults(users)
                    .build();
        } catch (Exception e) {
            LOGGER.error("Error while finding all users: ", e);
            throw e;
        }
    }


    /**
     * Create a user in the given realm.
     *
     * @param user the user to create
     * @return the created user
     * @throws IllegalArgumentException if the realm or the user is null
     */
    @Override
    public User create(User user) {
        UserRepresentation ur = UserMapper.toUserRepresentation(user);
        ur.setEnabled(true);

        try {
            User result = (User) keycloakInstanceManager.getInstance(REALM)
                    .realm(REALM)
                    .users()
                    .create(ur)
                    .getEntity();
            return result;
        } catch (Exception e) {
            LOGGER.error("Error while creating a user: ", e);
            throw e;
        }
    }

    /**
     * Finds a user by realm and uid.
     *
     * @param realm the realm of the user
     * @param uid   the unique identifier of the user
     * @return an Optional containing the user if found, otherwise empty
     * @throws IllegalArgumentException if realm or uid is null
     */
    @Override
    public Optional<User> findUserById(String uid) {


        if (uid == null) {
            LOGGER.error("uid cannot be null");
            throw new IllegalArgumentException("uid cannot be null");
        }

        try {
            final UserRepresentation userRepresentation = keycloakInstanceManager.getInstance(REALM)
                    .realm(REALM)
                    .users()
                    .get(uid).toRepresentation();
            return Optional.ofNullable(UserMapper.toUser(userRepresentation));
        } catch (Exception e) {
            LOGGER.error("Error while trying to find user with uid '{}' in realm '{}': {}", uid, REALM, e.getMessage(), e);
            throw e;
        }
    }


    /**
     * Updates a user by realm, uid and user object.
     *
     * @param uid   the unique identifier of the user
     * @param user  the updated user object
     * @return the updated user
     * @throws IllegalArgumentException if realm, uid or user is null
     */
    @Override
    public User update(String uid, User user) {

        if (uid == null || uid.trim().isEmpty()) {
            LOGGER.error("uid cannot be null or empty");
            throw new IllegalArgumentException("uid cannot be null or empty");
        }
        if (user == null) {
            LOGGER.error("user cannot be null");
            throw new IllegalArgumentException("user cannot be null");
        }
        try {
            final UserResource userResource = keycloakInstanceManager.getInstance(REALM)
                    .realm(REALM)
                    .users()
                    .get(uid);

            userResource.update(UserMapper.toUserRepresentation(user));

            LOGGER.debug("Successfully updated user with realm {} and uid {}", REALM, uid);
            return findUserById(uid).get();
        } catch (Exception e) {
            LOGGER.error("Error updating user with realm {} and uid {}: {}", REALM, uid, e.getMessage());
            throw e;
        }
    }

    /**
     * Deletes a user by realm and uid.
     *
     * @param uid   the unique identifier of the user
     * @throws IllegalArgumentException if realm or uid is null
     */
    @Override
    public void delete( String uid) {


        if (uid == null || uid.isEmpty()) {
            LOGGER.error("UID argument is null or empty");
            throw new IllegalArgumentException("UID cannot be null or empty");
        }

        try {
            keycloakInstanceManager.getInstance(REALM)
                    .realm(REALM)
                    .users()
                    .delete(uid);
            LOGGER.info("Successfully deleted user with UID: {} from realm: {}", uid, REALM);
        } catch (Exception e) {
            LOGGER.error("Error deleting user with UID: {} from realm: {}", uid, REALM, e);
            throw e;
        }
    }
}
