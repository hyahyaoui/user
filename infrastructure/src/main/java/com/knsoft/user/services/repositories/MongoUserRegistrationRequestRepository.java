package com.knsoft.user.services.repositories;

import com.knsoft.user.model.UserRegistrationRequest;
import com.knsoft.user.repositories.UserRegistrationRequestRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MongoUserRegistrationRequestRepository implements UserRegistrationRequestRepository {

    private final MongoTemplate mongoTemplate;

    public MongoUserRegistrationRequestRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public UserRegistrationRequest create(UserRegistrationRequest userRegistrationRequest) {
        mongoTemplate.save(userRegistrationRequest);
        return userRegistrationRequest;
    }

    @Override
    public Optional<UserRegistrationRequest> findRegistrationRequestById(String registrationRequestId) {
        Query query = new Query(Criteria.where("_id").is(registrationRequestId));
        UserRegistrationRequest result = mongoTemplate.findOne(query, UserRegistrationRequest.class);
        return Optional.ofNullable(result);
    }

    @Override
    public void delete(String registrationRequestId) {
        Query query = new Query(Criteria.where("_id").is(registrationRequestId));
        mongoTemplate.remove(query, UserRegistrationRequest.class);
    }

    @Override
    public List<UserRegistrationRequest> findRegistrationRequestBefore(long creationDateThreshold) {
        Query query = new Query(Criteria.where("registrationRequestDate").lt(creationDateThreshold));
        return mongoTemplate.find(query, UserRegistrationRequest.class);
    }


    @Override
    public Optional<UserRegistrationRequest> findRegistrationRequestByUserUid(String uid) {
        Query query = new Query(Criteria.where("userUid").is(uid));
        UserRegistrationRequest result = mongoTemplate.findOne(query, UserRegistrationRequest.class);
        return Optional.ofNullable(result);
    }
}
