package dev.d3athwarrior.libraryservice.controller;

import dev.d3athwarrior.libraryservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
/*
 * Ideally a spring security filter should be used to add authentication to the project but for the
 * sake of simplicity and sanity, a login endpoint is added which the user will call
 */
public class SessionController {

    private UserService userService;

    @Autowired
    public SessionController(final UserService userService) {
        this.userService = userService;
    }

    /**
     * End point to login a user and return the user's id if valid user
     *
     * @param userId the user id to validate
     * @return the user id once validated. Is -1 if an invalid user was sent for validation
     */
    @PostMapping("login")
    public Long loginUser(@RequestBody Long userId) {
        return userService.validateUser(userId);
    }
}
