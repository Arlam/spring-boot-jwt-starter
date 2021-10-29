package com.aa.test.jwt;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {

    @RequestMapping("/hello")
    public @ResponseBody
    String helloWorld() {
        return "Hello, World";
    }
}
