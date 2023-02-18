package com.knsoft.user.rest;

import com.knsoft.commons.data.Page;
import com.knsoft.user.model.User;
import com.knsoft.user.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UsersController {

    private final UserService userService;

    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public ResponseEntity<Page<User>> findAll(@PathVariable String applicationName,
                                              int start,
                                              int end,
                                              int resultsPerPage) {
        Page<User> users = userService.findAll(start, end, resultsPerPage);
        return users.getResults().isEmpty()
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{uid}")
    public ResponseEntity<User> findUser(@PathVariable String applicationName, @PathVariable String uid) {
        Optional<User> user = userService.findUserByUid(uid);
        return user.map(u -> new ResponseEntity<>(u, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("")
    public ResponseEntity<User> createUser(@PathVariable String applicationName, @RequestBody User user) {
        user = userService.create(user);
        return ResponseEntity.created(URI.create("/users/" + applicationName + "/" + user.getUid()))
                .body(user);
    }

    @PutMapping("/{uid}")
    public ResponseEntity<User> update(@PathVariable String applicationName, @PathVariable String uid,
                                       @RequestBody User user) {

        user = userService.update(uid, user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("/{uid}")
    public ResponseEntity<Void> delete(@PathVariable String applicationName, @PathVariable String uid) {
        userService.delete(uid);
        return ResponseEntity.noContent().build();
    }
}
