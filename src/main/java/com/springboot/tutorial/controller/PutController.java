package com.springboot.tutorial.controller;

import com.springboot.tutorial.dto.MemberDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/put-api")
public class PutController {

    // PUT 호출 시 request 파라미터(?key1=value1&key2=...)를 참고하여 Map 컬렉션에 관련 정보들을 저장
    @PutMapping(value="/request")
    public String putRequestParams(@RequestBody Map<String, Object> putData) {
        StringBuilder sb = new StringBuilder();

        putData.entrySet().forEach(map -> {
            sb.append(map.getKey() + " : " + map.getValue() + "\n");
        });


        return sb.toString();
    }

    // PUT 호출 시 request 파라미터(?name=value1&email=...)를 참고하여 DTO에 관련 정보들을 저장
    // MemberDto 객체 정보를 문자열로 반환
    @PutMapping(value="/request2")
    public String putMemberDto(@RequestBody MemberDto memberDto) {
        return memberDto.toString();
    }

    // PUT 호출 시 request 파라미터(?name=value1&email=...)를 참고하여 DTO에 관련 정보들을 저장
    // MemberDto 객체를 반환
    @PutMapping(value="/request3")
    public MemberDto putMemberDto2(@RequestBody MemberDto memberDto) {
        return memberDto;
    }

    // PUT 호출 시 request 파라미터(?name=value1&email=...)를 참고하여 DTO에 관련 정보들을 저장
    // ResponseEnitty를 활용하여 응답 객체를 생성하고 관련 정보를 생성하여 반환
    @PutMapping(value = "/request4")
    public ResponseEntity<MemberDto> postMemberDto3(@RequestBody MemberDto memberDto) {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(memberDto);
    }
}
