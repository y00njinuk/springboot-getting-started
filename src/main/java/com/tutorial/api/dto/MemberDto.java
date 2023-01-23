package com.tutorial.api.dto;

import java.util.Objects;

public class MemberDto {
    private String name;
    private String email;
    private String organization;

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getOrganization() {
        return organization;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

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
