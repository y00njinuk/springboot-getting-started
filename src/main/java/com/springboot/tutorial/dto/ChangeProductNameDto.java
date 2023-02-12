package com.springboot.tutorial.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeProductNameDto {
    private Long number;
    private String name;

    public ChangeProductNameDto(Long number, String name) {
        this.number = number;
        this.name = name;
    }

    public ChangeProductNameDto() {
    }
}
