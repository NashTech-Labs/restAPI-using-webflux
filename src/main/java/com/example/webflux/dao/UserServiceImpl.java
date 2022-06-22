package com.example.webflux.dao;

import com.example.webflux.model.User;
import com.example.webflux.repository.UserRepository;
import com.example.webflux.service.UserServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserServiceImpl implements UserServiceInterface {

    @Autowired
    UserRepository userRepo;

    public ResponseEntity<Mono<User>> createUser(User user) {
        try {
            Mono<User> response = userRepo.save(user);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Mono<User>> findByUserId(String id) {
        Mono<User> user = userRepo.findById(id);
        return new ResponseEntity<Mono<User>>(user, user != null ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    public Flux<User> findAllUser() {
        return userRepo.findAll();
    }

    public Mono<User> updateUser(User user) {
        return userRepo.save(user);
    }

    public ResponseEntity<Boolean> deleteUser(String id) {
        Mono<User> user = userRepo.findById(id);
        if (user.block() == null) {
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
        }
        userRepo.deleteById(id);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

}