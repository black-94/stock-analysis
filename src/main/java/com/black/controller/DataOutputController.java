package com.black.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/data")
public class DataOutputController {

    @GetMapping("/hello")
    public Mono<String> helloworld(){
        return Mono.just("helloworld");
    }

}
