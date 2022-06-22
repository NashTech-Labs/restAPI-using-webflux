package com.example.webflux.controller;

import com.example.webflux.dao.UserServiceImpl;
import com.example.webflux.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/")
public class UserController {

    @Autowired
    UserServiceImpl userServiceImpl;

    @PostMapping("/create")
    public ResponseEntity<Mono<User>> save(@Valid @RequestBody User user) {
        return userServiceImpl.createUser(user);
    }

    @GetMapping(value = "/get/all")
    public Flux<User> findAll() {
        return userServiceImpl.findAllUser();
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Mono<User>> findByUserID(@PathVariable("id") String id) {
        return userServiceImpl.findByUserId(id);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> deleteById(@PathVariable("id") String id) {
        return userServiceImpl.deleteUser(id);
    }

}
