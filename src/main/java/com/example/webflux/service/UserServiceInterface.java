package com.example.webflux.service;

import com.example.webflux.model.User;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserServiceInterface {
    ResponseEntity<Mono<User>> createUser(User e);

    ResponseEntity<Mono<User>> findByUserId(String id);

    Flux<User> findAllUser();

    Mono<User> updateUser(User e);

    ResponseEntity<Boolean> deleteUser(String id);
}