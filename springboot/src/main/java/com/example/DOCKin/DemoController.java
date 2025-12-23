package com.example.DOCKin;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @GetMapping
    public String hello(){
        return System.getenv("docker-app");
    }

}
