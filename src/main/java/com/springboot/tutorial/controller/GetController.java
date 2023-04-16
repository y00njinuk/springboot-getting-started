package com.springboot.tutorial.controller;

import com.springboot.tutorial.dto.MemberDto;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/get-api")
@Path("/api/v1/get-api")
@Api(value = "GetController", description = "Operations about pets")
public class GetController {
    private final Logger LOGGER = LoggerFactory.getLogger(GetController.class);

    // RequestMapping을 활용하여 GET 메서드에 대한 응답을 정의
    @RequestMapping(value="/hello", method= RequestMethod.GET)
    @GET
    @Path(value = "/hello")
    @ApiOperation(
            value="GET 메서드 예제 (1)",
            notes="기본적인 GET 메서드를 테스트한다.",
            response = String.class)
    public String getHello() {
        LOGGER.info("getHello 메서드가 호출되었습니다.");
        return "Hello World";
    }

    // GetMapping을 활용하여 매개변수를 전달하는 GET 메서드에 대한 응답을 정의
    @GetMapping(value="/request/{variable}")
    @GET
    @Path(value = "/request")
    @ApiOperation(
            value="GET 메서드 예제 (2)",
            notes="@PathVariable을 활용한 GET 메서드를 테스트한다.",
            response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Invalid user supplied"),
            @ApiResponse(code = 404, message = "Internal Server Error")
    })
    public String getRequestParam(
            @ApiParam(value = "GET 파라미터", required = true) @PathVariable("variable") String variable){
        LOGGER.info("getRequestParam 메서드가 호출되었습니다.");
        LOGGER.info("@PathVariable를 통해 들어온 값 : {}", variable);
        return variable;
    }

    // GET 호출 시 request 파라미터(?key1=value1&key2=...)를 참고하여 Map 컬렉션에 관련 정보들을 저장
    @GetMapping(value = "/request2")
    @GET
    @Path(value = "/request2")
    @ApiOperation(
            value="GET 메서드 예제 (3)",
            notes="@RequestParam을 활용한 GET 메서드를 테스트한다.",
            response = String.class)
    @ApiResponse(code = 404, message = "Internal Server Error")
    public String getRequestParams(
            @ApiParam(value = "GET 파라미터", required = true) @RequestParam Map<String, String> params) {
        JSONObject jsonObject = new JSONObject(params);
        LOGGER.info("getRequestParams 메서드가 호출되었습니다.");
        LOGGER.info("@RequestParam를 통해 들어온 값 : {}", params);
        return jsonObject.toString();
    }

    // GET 호출 시 request 파라미터(?name=value1&email=...)를 참고하여 DTO에 관련 정보들을 저장
    @GetMapping(value = "/request3")
    @GET
    @Path(value = "/request3")
    @ApiOperation(
            value="GET 메서드 예제 (4)",
            notes="DTO을 활용한 GET 메서드를 테스트한다.",
            response = MemberDto.class)
    @ApiResponse(code = 404, message = "Internal Server Error")
    public MemberDto getRequestParamsToDto(
            @ApiParam(value = "Member 정보", required = true) MemberDto memberDto) {
        LOGGER.info("getRequestParamsToDto 메서드가 호출되었습니다.");
        LOGGER.info("DTO 값 : {}", memberDto.toString());
        return memberDto;
    }
}
