package com.springboot.tutorial.repository.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String code;

    private String name;

    @OneToMany(fetch = FetchType.EAGER) // 외래키가 Product 테이블에 생기게 된다.
                                        // 즉, N:1 방식과는 다르게 외래키를 설정하기 위해 Product 테이블에 Update 쿼리를 발생시킨다.
    @JoinColumn(name = "category_id")   // JoinColumn은 필수사항은 아니며, 사용하지 않으면 중간 테이블로 Join 테이블이 생성
    private List<Product> products = new ArrayList<>();
}
