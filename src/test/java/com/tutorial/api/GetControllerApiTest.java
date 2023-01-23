package com.tutorial.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tutorial.api.dto.MemberDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class GetControllerApiTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void GET_getHello() {
        String res = restTemplate.getForObject(
                "/api/v1/get-api/hello",
                String.class
        );

        assertThat(res).isEqualTo("Hello World");
    }

    @Test
    public void GET_getRequestParam() {
        String erString = "test";

        String arString = restTemplate.getForObject(
                "/api/v1/get-api/request/" + erString,
                String.class
        );

        assertThat(erString).isEqualTo(arString);
    }

    @Test
    public void GET_getRequestParams() throws JsonProcessingException {
        Map<String, Object> erParams = new HashMap<>();
        erParams.put("key1", "value1");
        erParams.put("key2", "value2");

        StringBuilder sb = new StringBuilder();
        erParams.entrySet().forEach (
                map -> sb.append(map.getKey() + "=" + map.getValue() + "&")
        );

        String res = restTemplate.getForObject(
                "/api/v1/get-api/request2?" + sb.substring(0, sb.length()-1),
                String.class
        );

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> arParams = mapper.readValue(res, Map.class);

        assertThat(erParams).isEqualTo(arParams);
    }

    @Test
    public void GET_getRequestParamsToDto() {
        MemberDto erDto = new MemberDto();
        erDto.setName("John");
        erDto.setEmail("John@gmail.com");
        erDto.setOrganization("None");

        Map<String, Object> erParams = new HashMap<>();
        erParams.put("name", erDto.getName());
        erParams.put("email", erDto.getEmail());
        erParams.put("organization", erDto.getOrganization());

        StringBuilder sb = new StringBuilder();
        erParams.entrySet().forEach (
                map -> sb.append(map.getKey() + "=" + map.getValue() + "&")
        );

        MemberDto arDto = restTemplate.getForObject(
                "/api/v1/get-api/request3?" + sb.substring(0, sb.length()-1),
                MemberDto.class
        );

        assertThat(erDto).isEqualTo(arDto);
    }
}
