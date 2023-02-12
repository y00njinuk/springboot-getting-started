package com.springboot.tutorial.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor // 클래스의 모든 필드를 파라미터로 가지는 생성자 생성
@NoArgsConstructor  // 파라미터가 없는 생성자 생성
public class ProductResponseDto {
    private Long number;
    private String name;
    private int price;
    private int stock;
}
