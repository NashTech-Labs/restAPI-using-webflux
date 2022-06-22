package com.example.webflux;

import com.example.webflux.controller.UserController;
import com.example.webflux.dao.UserServiceImpl;
import com.example.webflux.model.User;
import com.example.webflux.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@WebMvcTest
public class UnitTest {

    static HttpStatus httpStatus;
    @MockBean
    UserServiceImpl userServiceImpl;
    @MockBean
    UserRepository userRepo;
    @Autowired
    ObjectMapper mapper;
    UserController userController = Mockito.mock(UserController.class);
    User user = new User();
    Mono<User> response;
    boolean isUserExist;
    private MockMvc mockMvc;

    @AfterAll
    public static void cleanUp() {
        httpStatus = null;
    }

    @BeforeEach
    public void setup() {
        user.setId("101");
        user.setEmail("string@str.com");
        user.setName("xyz");
        user.setAddress("India");
    }

    @Test
    public void save() {
        Mockito.when(userRepo.save(user)).thenReturn(Mono.just(user));
        Mono<User> response = userRepo.save(user);
        ResponseEntity<Mono<User>> responseEntity = new ResponseEntity<Mono<User>>(response, HttpStatus.CREATED);
        Mockito.when(userServiceImpl.createUser(user)).thenReturn(responseEntity);
        Assertions.assertEquals(responseEntity.getBody().block().getId(), "101");
    }

    @Test
    public void findAll() throws Exception {
        Flux<User> findAll = Flux.just(user);
        Mockito.when(userServiceImpl.findAllUser()).thenReturn(findAll);
        Assertions.assertEquals(userServiceImpl.findAllUser().count().block().intValue(), 1);

    }

    @Test
    public void findByUserID() {
        String userID = user.getId();
        checkStatus(userID);
        getData(userID);
        ResponseEntity<Mono<User>> responseEntity = new ResponseEntity<Mono<User>>(response, httpStatus);
        Mockito.when(userServiceImpl.findByUserId(userID)).thenReturn(responseEntity);
        Assertions.assertEquals(httpStatus, HttpStatus.OK);
    }

    @Test
    public void findByUserUnknownID() {
        String userID = "000";
        checkStatus(userID);
        getData(userID);
        ResponseEntity<Mono<User>> responseEntity = new ResponseEntity<Mono<User>>(response, httpStatus);
        Mockito.when(userServiceImpl.findByUserId(userID)).thenReturn(responseEntity);
        Assertions.assertEquals(httpStatus, HttpStatus.NOT_FOUND);
    }


    @Test
    public void deleteByValidID() {
        String userID = user.getId();
        boolean isValidHttpStatus = false;
        HttpStatus status = HttpStatus.NOT_FOUND;
        if (isUserExist(userID) == true) {
            isValidHttpStatus = true;
            status = HttpStatus.OK;
        }
        ResponseEntity<Boolean> responseEntity = new ResponseEntity<Boolean>(isValidHttpStatus, status);
        Assertions.assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);

    }

    @Test
    public void deleteByUnknownID() {
        String userID = "000";
        boolean isValidHttpStatus = false;
        HttpStatus status = HttpStatus.NOT_FOUND;
        Mockito.when(userRepo.findById(userID)).thenReturn(Mono.just(user));
        if (isUserExist(userID) == true) {
            isValidHttpStatus = true;
            status = HttpStatus.OK;
        }
        ResponseEntity<Boolean> responseEntity = new ResponseEntity<Boolean>(isValidHttpStatus, status);
        Assertions.assertEquals(responseEntity.getStatusCode(), HttpStatus.NOT_FOUND);

    }


    public HttpStatus checkStatus(String userID) {
        return httpStatus = userID == "101" ? HttpStatus.OK : HttpStatus.NOT_FOUND;
    }

    public Mono<User> getData(String userID) {
        return response = userID == "101" ? userRepo.findById(userID) : null;
    }

    public boolean isUserExist(String userID) {
        return isUserExist = userID == "101" ? true : false;
    }
}
