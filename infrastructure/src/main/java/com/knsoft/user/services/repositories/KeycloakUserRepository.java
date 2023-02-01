package com.knsoft.user.services.repositories;


import com.knsoft.commons.data.Page;
import com.knsoft.user.configuration.KeycloakInstanceManager;
import com.knsoft.user.mappers.UserMapper;
import com.knsoft.user.model.User;
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
    private final Logger logger = LoggerFactory.getLogger(KeycloakUserRepository.class);


    /**
     * Constructor to create an instance of KeycloakUserRepository
     *
     * @param keycloakInstanceManager instance of KeycloakInstanceManager class
     */
    public KeycloakUserRepository(KeycloakInstanceManager keycloakInstanceManager) {
        this.keycloakInstanceManager = keycloakInstanceManager;
    }

    /**
     * Retrieves a list of users for a given realm from Keycloak using KeycloakAdminClient.
     *
     * @param realm          the name of the realm in Keycloak
     * @param start          the starting position of the range of users to retrieve
     * @param end            the ending position of the range of users to retrieve
     * @param resultsPerPage the number of users to retrieve per page
     * @return a Page object containing the results of the query, including the current page, the number of pages,
     * the number of results per page, and the list of users
     */
    @Override
    public Page<User> findAll(String realm, int start, int end, int resultsPerPage) {
        if (realm == null || realm.isEmpty()) {
            logger.error("realm cannot be null or empty");
            throw new IllegalArgumentException("realm cannot be null or empty");
        }
        if (start < 0 || end < 0 || resultsPerPage < 1) {
            logger.error("start, end, and resultsPerPage must be non-negative and resultsPerPage must be greater than 0");
            throw new IllegalArgumentException("start, end, and resultsPerPage must be non-negative, and resultsPerPage must be greater than 0");
        }
        if (start > end) {
            logger.error("start must be less than or equal to end");
            throw new IllegalArgumentException("start must be less than or equal to end");
        }

        try {
            List<UserRepresentation> userRepresentations = keycloakInstanceManager.getInstance(realm)
                    .realm(realm)
                    .users()
                    .list(start, resultsPerPage);

            List<User> users = userRepresentations.stream()
                    .map(UserMapper::toUser)
                    .collect(Collectors.toList());

            int numberOfPages = (int) Math.ceil((double) keycloakInstanceManager.getInstance(realm)
                    .realm(realm)
                    .users()
                    .count() / resultsPerPage);

            logger.debug("Found {} users for realm {}", users.size(), realm);
            return new Page.Builder<User>()
                    .withCurrentPage(start / resultsPerPage + 1)
                    .withNumberOfPages(numberOfPages)
                    .withResultsPerPage(resultsPerPage)
                    .withResults(users)
                    .build();
        } catch (Exception e) {
            logger.error("Error while finding all users: ", e);
            throw e;
        }
    }


    /**
     * Create a user in the given realm.
     *
     * @param realm the name of the realm in Keycloak
     * @param user  the user to create
     * @return the created user
     * @throws IllegalArgumentException if the realm or the user is null
     */
    @Override
    public User create(String realm, User user) {
        if (realm == null || realm.isEmpty()) {
            logger.error("realm cannot be null or empty");
            throw new IllegalArgumentException("realm cannot be null or empty");
        }

        UserRepresentation ur = UserMapper.toUserRepresentation(user);
        ur.setEnabled(true);

        try {
            User result = (User) keycloakInstanceManager.getInstance(realm)
                    .realm(realm)
                    .users()
                    .create(ur)
                    .getEntity();
            return result;
        } catch (Exception e) {
            logger.error("Error while creating a user: ", e);
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
    public Optional<User> find(String realm, String uid) {
        if (realm == null) {
            logger.error("realm cannot be null");
            throw new IllegalArgumentException("realm cannot be null");
        }

        if (uid == null) {
            logger.error("uid cannot be null");
            throw new IllegalArgumentException("uid cannot be null");
        }

        try {
            final UserRepresentation userRepresentation = keycloakInstanceManager.getInstance(realm)
                    .realm(realm)
                    .users()
                    .get(uid).toRepresentation();
            return Optional.ofNullable(UserMapper.toUser(userRepresentation));
        } catch (Exception e) {
            logger.error("Error while trying to find user with uid '{}' in realm '{}': {}", uid, realm, e.getMessage(), e);
            throw e;
        }
    }


    /**
     * Updates a user by realm, uid and user object.
     *
     * @param realm the realm of the user
     * @param uid   the unique identifier of the user
     * @param user  the updated user object
     * @return the updated user
     * @throws IllegalArgumentException if realm, uid or user is null
     */
    @Override
    public User update(String realm, String uid, User user) {
        if (realm == null || realm.trim().isEmpty()) {
            logger.error("realm cannot be null or empty");
            throw new IllegalArgumentException("realm cannot be null or empty");
        }
        if (uid == null || uid.trim().isEmpty()) {
            logger.error("uid cannot be null or empty");
            throw new IllegalArgumentException("uid cannot be null or empty");
        }
        if (user == null) {
            logger.error("user cannot be null");
            throw new IllegalArgumentException("user cannot be null");
        }
        try {
            final UserResource userResource = keycloakInstanceManager.getInstance(realm)
                    .realm(realm)
                    .users()
                    .get(uid);

            userResource.update(UserMapper.toUserRepresentation(user));

            logger.debug("Successfully updated user with realm {} and uid {}", realm, uid);
            return find(realm, uid).get();
        } catch (Exception e) {
            logger.error("Error updating user with realm {} and uid {}: {}", realm, uid, e.getMessage());
            throw e;
        }
    }

    /**
     * Deletes a user by realm and uid.
     *
     * @param realm the realm of the user
     * @param uid   the unique identifier of the user
     * @throws IllegalArgumentException if realm or uid is null
     */
    @Override
    public void delete(String realm, String uid) {
        if (realm == null || realm.isEmpty()) {
            logger.error("Realm argument is null or empty");
            throw new IllegalArgumentException("realm cannot be null or empty");
        }

        if (uid == null || uid.isEmpty()) {
            logger.error("UID argument is null or empty");
            throw new IllegalArgumentException("UID cannot be null or empty");
        }

        try {
            keycloakInstanceManager.getInstance(realm)
                    .realm(realm)
                    .users()
                    .delete(uid);
            logger.info("Successfully deleted user with UID: {} from realm: {}", uid, realm);
        } catch (Exception e) {
            logger.error("Error deleting user with UID: {} from realm: {}", uid, realm, e);
            throw e;
        }
    }
}
