package com.ascii274.jobarcelona22.repository;

import com.ascii274.jobarcelona22.model.User;
import com.ascii274.jobarcelona22.model.UserDto;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User,Long> {
    User findByUsername(String username);
}
