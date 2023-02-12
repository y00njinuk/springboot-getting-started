package com.springboot.tutorial.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor // 클래스의 모든 필드를 파라미터로 가지는 생성자 생성
public class ProductDto {
    private String name;
    private int price;
    private int stock;
}
