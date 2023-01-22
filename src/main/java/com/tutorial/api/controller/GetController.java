package com.tutorial.api.controller;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/get-api")
public class GetController {

    // RequestMapping을 활용하여 GET 메서드에 대한 응답을 정의
    @RequestMapping(value="/hello", method= RequestMethod.GET)
    public String getHello() {

        return "Hello World";
    }

    // GetMapping을 활용하여 매개변수를 전달하는 GET 메서드에 대한 응답을 정의
    @GetMapping(value="/request/{variable}")
    public String getRequestParam(@PathVariable("variable") String variable){

        return variable;
    }
}
