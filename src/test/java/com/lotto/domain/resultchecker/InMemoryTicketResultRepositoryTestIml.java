package com.lotto.domain.resultchecker;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

class InMemoryTicketResultRepositoryTestIml implements TicketResultRepository {

    Map<String, TicketResult> inMemoryDatabase = new ConcurrentHashMap<>();

    @Override
    public Optional<TicketResult> findById(final String ticketId) {
        return Optional.ofNullable(inMemoryDatabase.get(ticketId));
    }

    @Override
    public boolean existsById(final String s) {
        return false;
    }

    @Override
    public <S extends TicketResult> S save(final S entity) {
        return null;
    }

    @Override
    public <S extends TicketResult> List<S> saveAll(final Iterable<S> entities) {
        Stream<S> stream = StreamSupport.stream(entities.spliterator(), false);
        List<S> list = stream.toList();
        list.forEach(ticket -> inMemoryDatabase.put(ticket.hash(), ticket));
        return list;
    }

    private <S extends TicketResult> S saveEntity(S entity) {
        inMemoryDatabase.put(entity.hash(), entity);
        return entity;
    }

    @Override
    public List<TicketResult> findAll() {
        return List.of();
    }

    @Override
    public Iterable<TicketResult> findAllById(final Iterable<String> strings) {
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
    public void delete(final TicketResult entity) {

    }

    @Override
    public void deleteAllById(final Iterable<? extends String> strings) {

    }

    @Override
    public void deleteAll(final Iterable<? extends TicketResult> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<TicketResult> findAll(final Sort sort) {
        return List.of();
    }

    @Override
    public Page<TicketResult> findAll(final Pageable pageable) {
        return null;
    }

    @Override
    public <S extends TicketResult> S insert(final S entity) {
        return null;
    }

    @Override
    public <S extends TicketResult> List<S> insert(final Iterable<S> entities) {
        return List.of();
    }

    @Override
    public <S extends TicketResult> Optional<S> findOne(final Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends TicketResult> List<S> findAll(final Example<S> example) {
        return List.of();
    }

    @Override
    public <S extends TicketResult> List<S> findAll(final Example<S> example, final Sort sort) {
        return List.of();
    }

    @Override
    public <S extends TicketResult> Page<S> findAll(final Example<S> example, final Pageable pageable) {
        return null;
    }

    @Override
    public <S extends TicketResult> long count(final Example<S> example) {
        return 0;
    }

    @Override
    public <S extends TicketResult> boolean exists(final Example<S> example) {
        return false;
    }

    @Override
    public <S extends TicketResult, R> R findBy(final Example<S> example, final Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }
}
