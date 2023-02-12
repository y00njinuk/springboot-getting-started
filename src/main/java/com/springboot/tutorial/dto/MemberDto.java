package com.springboot.tutorial.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class MemberDto {
    private String name;
    private String email;
    private String organization;

    @Override
    public String toString() {
        return "MemberDto{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", organization='" + organization + '\'' +
                '}';
    }

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
