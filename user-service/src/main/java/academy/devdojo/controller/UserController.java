package academy.devdojo.controller;

import academy.devdojo.mapper.UserMapper;
import academy.devdojo.request.UserPostRequest;
import academy.devdojo.response.UserGetResponse;
import academy.devdojo.response.UserPostResponse;
import academy.devdojo.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("v1/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService service;
    private final UserMapper mapper;

    @GetMapping
    public ResponseEntity<List<UserGetResponse>> findAll(@RequestParam(required = false) String firstName) {
        log.debug("Request received to list all users, param first name '{}'", firstName);
        var users = service.findAll(firstName);
        var userGetResponses = mapper.toUserGetResponseList(users);
        return ResponseEntity.ok(userGetResponses);
    }

    @GetMapping("{id}")
    public ResponseEntity<UserGetResponse> findById(@PathVariable Long id) {
        log.debug("Request to find user by id: {}", id);
        var user = service.findByIdOrThrowNotFound(id);
        var userGetResponse = mapper.toUserGetResponse(user);
        return ResponseEntity.ok(userGetResponse);
    }

    @PostMapping
    public ResponseEntity<UserPostResponse> save(@RequestBody UserPostRequest request) {
        log.debug("Request to save user : {}", request);
        var user = mapper.toUser(request);
        var userSaved = service.save(user);
        var userPostResponse = mapper.toUserPostResponse(userSaved);
        return ResponseEntity.status(HttpStatus.CREATED).body(userPostResponse);
    }
}