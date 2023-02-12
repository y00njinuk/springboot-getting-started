package com.springboot.tutorial.controller;

import com.springboot.tutorial.dto.MemberDto;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/post-api")
public class PostController {

    // RequestMapping을 활용하여 GET 메서드에 대한 응답을 정의
    @RequestMapping(value = "/hello", method= RequestMethod.POST)
    public String postHello() {

        return "Hello World";
    }

    // PostMapping을 활용하여 매개변수를 전달하는 POST 메서드에 대한 응답을 정의
    @PostMapping(value="/request")
    public String postMemberDto(@RequestBody MemberDto memberDto) {

        return memberDto.toString();
    }
}
