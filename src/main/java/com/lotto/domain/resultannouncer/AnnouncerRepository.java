package com.lotto.domain.resultannouncer;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
interface AnnouncerRepository extends MongoRepository<ResultResponse, String> {
}
