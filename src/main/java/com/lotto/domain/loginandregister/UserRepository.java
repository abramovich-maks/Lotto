package com.lotto.domain.loginandregister;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findFirstByEmail(String email);

    boolean existsByEmail(String email);

    List<User> findAllByUsername();
}

