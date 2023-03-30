package com.springboot.tutorial.dto;

import com.springboot.tutorial.config.validate.ValidationGroup1;
import com.springboot.tutorial.config.validate.ValidationGroup2;
import lombok.*;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ValidatedRequestDto {

    @NotBlank // null, "", " "를 허용하지 않는다.
    String name;

    @Email // 이메일 형식을 검사한다.
    String email;

    // 정규식에 맞게 표현된 값으로 검증한다.
    @Pattern(regexp = "01(?:0|1|[6-9])[.-]?(\\d{3}|\\d{4})[.-]?(\\d{4})$")
    String phoneNumber;

    // 최대/최소값을 검증한다.
    // 특정 그룹에 맞춰서 유효성 검증을 할 때는 groups 어노테이션을 활용한다.
    @Min(value = 20, groups = ValidationGroup1.class)
    @Max(value = 40, groups = ValidationGroup2.class)
    int age;

    @Size(min = 0, max = 40) // 문자열 길이를 검증한다.
    String description;

    @Positive // 양수만을 허용한다.
    int count;

    @AssertTrue // true 값인지 체크한다.
    boolean booleanCheck;
}