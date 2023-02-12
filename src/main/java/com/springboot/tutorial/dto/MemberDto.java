package com.springboot.tutorial.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

@Getter
@Setter
@ToString // toString 메서드를 자동으로 생성
@EqualsAndHashCode // 객체의 동등성(equality)과 동일성(Identity)를 비교하는 equals 메서드 생성
public class MemberDto {
    private String name;
    private String email;
    private String organization;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemberDto memberDto = (MemberDto) o;
        return Objects.equals(name, memberDto.name) && Objects.equals(email, memberDto.email) && Objects.equals(organization, memberDto.organization);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, email, organization);
    }
}
