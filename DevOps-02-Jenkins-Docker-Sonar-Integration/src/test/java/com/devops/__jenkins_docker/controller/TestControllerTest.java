package com.devops.__jenkins_docker.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestControllerTest {

    @InjectMocks TestController testController;

    @Test
    public void test(){
        Assertions.assertNotNull(testController.getHello());

    }
}
