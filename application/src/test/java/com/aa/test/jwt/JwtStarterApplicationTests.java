package com.aa.test.jwt;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class JwtStarterApplicationTests {

    @Autowired
    private HelloWorldController helloWorldController;


    @Test
    public void contextLoads() {
        assertThat(helloWorldController).isNotNull();
    }

}
