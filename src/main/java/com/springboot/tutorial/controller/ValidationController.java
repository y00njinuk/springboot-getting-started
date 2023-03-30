package com.springboot.tutorial.controller;

import com.springboot.tutorial.config.validate.ValidationGroup1;
import com.springboot.tutorial.config.validate.ValidationGroup2;
import com.springboot.tutorial.dto.ValidatedRequestDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/validation")
public class ValidationController {
    private final Logger LOGGER = LoggerFactory.getLogger(ValidationController.class);

    // Valid 어노테이션은 자바에서 지원하는 어노테이션으로 주어진 객체에 대한 유효성 검사를 수행한다.
    @PostMapping("/byValid")
    public ResponseEntity<String> checkValidationByValid(
            @Valid @RequestBody ValidatedRequestDto validatedRequestDto) {

        LOGGER.info(validatedRequestDto.toString());
        return ResponseEntity.status(HttpStatus.OK).body(validatedRequestDto.toString());
    }

    /**
     * Valid 어노테이션에 속성을 지정하지 않고 Validated 어노테이션을 활용해 그룹으로 지정이 가능하다.
     * 그룹에 대한 검증 방식은 ValidatedRequestDto 클래스에 해당 그룹으로 설정된 필드를 확인한다.
     * 어떤 상황에 사용할지를 적절하게 설계해야 의도한 대로 유효성 검사를 실시할 수 있다.
     **/
    @PostMapping("/byValidatedGroup")
    public ResponseEntity<String> checkValidationByValidGroup(
            @Validated({ValidationGroup1.class, ValidationGroup2.class})
            @RequestBody ValidatedRequestDto validatedRequestDto) {

        LOGGER.info(validatedRequestDto.toString());
        return ResponseEntity.status(HttpStatus.OK).body(validatedRequestDto.toString());
    }
}