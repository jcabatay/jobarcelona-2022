package com.ascii274.jobarcelona22.repository;

import com.ascii274.jobarcelona22.model.UserDto;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserDtoRepository extends MongoRepository<UserDto,Long> {
}
