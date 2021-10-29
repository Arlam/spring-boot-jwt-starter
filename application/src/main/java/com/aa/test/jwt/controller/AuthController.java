package com.aa.test.jwt.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @PostMapping(path = "/login")
    public Credentials login(@RequestBody Credentials credentials) {
        System.out.println(credentials);
        return credentials;

    }
}
