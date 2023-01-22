package com.tutorial.api.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/delete-api")
public class DeleteController {

    // DeleteMapping 활용하여 매개변수를 전달하는 DELETE 메서드에 대한 응답을 정의
    @DeleteMapping(value="/{variable}")
    public String deleteVariable(@PathVariable String variable) {
        return variable;
    }

    // DELETE 호출 시 request 파라미터(?email=value1...)를 참고하여 관련 정보들을 저장
    @DeleteMapping(value="/request")
    public String deleteRequestParam(@RequestParam String email) {
        return "e-mail : " + email;
    }
}
