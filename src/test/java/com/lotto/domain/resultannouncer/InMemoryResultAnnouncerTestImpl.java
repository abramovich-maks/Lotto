package com.lotto.domain.resultannouncer;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

class InMemoryResultAnnouncerTestImpl implements AnnouncerRepository {

    Map<String, ResultResponse> inMemoryDatabase = new ConcurrentHashMap<>();

    @Override
    public ResultResponse save(final ResultResponse resultResponse) {
        inMemoryDatabase.put(resultResponse.hash(), resultResponse);
        return resultResponse;
    }

    @Override
    public boolean existsById(final String hash) {
        return inMemoryDatabase.containsKey(hash);
    }

    @Override
    public Optional<ResultResponse> findById(final String hash) {
        return Optional.ofNullable(inMemoryDatabase.get(hash));
    }
}
