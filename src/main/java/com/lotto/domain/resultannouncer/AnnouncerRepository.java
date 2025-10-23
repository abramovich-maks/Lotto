package com.lotto.domain.resultannouncer;

import java.util.Optional;

interface AnnouncerRepository {

    ResultResponse save(ResultResponse resultResponse);

    boolean existsById(String hash);

    Optional<ResultResponse> findById(String hash);
}
