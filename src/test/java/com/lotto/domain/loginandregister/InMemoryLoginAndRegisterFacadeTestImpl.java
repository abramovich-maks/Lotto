package com.lotto.domain.loginandregister;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

class InMemoryLoginAndRegisterFacadeTestImpl implements UserRepository {

    Map<String, User> inMemoryDatabase = new ConcurrentHashMap<>();

    @Override
    public Optional<User> findFirstByEmail(final String email) {
        return inMemoryDatabase.values().stream()
                .filter(user -> user.getEmail().equals(email)).findFirst();
    }

    @Override
    public <S extends User> S save(final S entity) {
        String id = UUID.randomUUID().toString();
        entity.setUserId(id);
        inMemoryDatabase.put(entity.getEmail(), entity);
        return entity;
    }

    @Override
    public boolean existsByEmail(final String email) {
        long count = inMemoryDatabase.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .count();
        return count == 1;
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(inMemoryDatabase.values());
    }

    @Override
    public Optional<User> findByConfirmationToken(final String confirmationToken) {
        return Optional.empty();
    }


    @Override
    public <S extends User> List<S> saveAll(final Iterable<S> entities) {
        return List.of();
    }

    @Override
    public Optional<User> findById(final String s) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(final String s) {
        return false;
    }

    @Override
    public Iterable<User> findAllById(final Iterable<String> strings) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(final String s) {

    }

    @Override
    public void delete(final User entity) {

    }

    @Override
    public void deleteAllById(final Iterable<? extends String> strings) {

    }

    @Override
    public void deleteAll(final Iterable<? extends User> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<User> findAll(final Sort sort) {
        return List.of();
    }

    @Override
    public Page<User> findAll(final Pageable pageable) {
        return null;
    }

    @Override
    public <S extends User> S insert(final S entity) {
        return null;
    }

    @Override
    public <S extends User> List<S> insert(final Iterable<S> entities) {
        return List.of();
    }

    @Override
    public <S extends User> Optional<S> findOne(final Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends User> List<S> findAll(final Example<S> example) {
        return List.of();
    }

    @Override
    public <S extends User> List<S> findAll(final Example<S> example, final Sort sort) {
        return List.of();
    }

    @Override
    public <S extends User> Page<S> findAll(final Example<S> example, final Pageable pageable) {
        return null;
    }

    @Override
    public <S extends User> long count(final Example<S> example) {
        return 0;
    }

    @Override
    public <S extends User> boolean exists(final Example<S> example) {
        return false;
    }

    @Override
    public <S extends User, R> R findBy(final Example<S> example, final Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }
}