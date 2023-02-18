package com.knsoft.user.services.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.knsoft.user.model.UserRegistrationRequest;
import com.knsoft.user.repositories.UserRegistrationRequestRepository;

/**
 * An implementation of the {@link UserRegistrationRequestRepository} interface that uses MongoDB as the data store.
 */
@Repository
public class MongoUserRegistrationRequestRepository implements UserRegistrationRequestRepository {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public MongoUserRegistrationRequestRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserRegistrationRequest create(UserRegistrationRequest userRegistrationRequest) {
        if (userRegistrationRequest == null) {
            throw new IllegalArgumentException("userRegistration cannot be null");
        }
        mongoTemplate.save(userRegistrationRequest);
        return userRegistrationRequest;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<UserRegistrationRequest> findRegistrationRequestById(String registrationRequestId) {
        if (registrationRequestId == null) {
            throw new IllegalArgumentException("registrationId cannot be null");
        }
        UserRegistrationRequest userRegistrationRequest = mongoTemplate.findById(registrationRequestId, UserRegistrationRequest.class);
        return Optional.ofNullable(userRegistrationRequest);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(String registrationRequestId) {
        if (registrationRequestId == null) {
            throw new IllegalArgumentException("registrationId cannot be null");
        }
        mongoTemplate.remove(new Query(Criteria.where("_id").is(registrationRequestId)), UserRegistrationRequest.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UserRegistrationRequest> findRegistrationBefore(long creationDateThreshold) {
        if (creationDateThreshold < 0) {
            throw new IllegalArgumentException("creationDateThreshold cannot be negative");
        }
        List<UserRegistrationRequest> userRegistrationRequests = mongoTemplate.find(
                new Query(Criteria.where("creationDate").lt(creationDateThreshold)), UserRegistrationRequest.class);
        return userRegistrationRequests;
    }
}
