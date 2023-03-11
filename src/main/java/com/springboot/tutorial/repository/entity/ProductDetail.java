package com.springboot.tutorial.repository.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "product_detail")
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ProductDetail extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    @OneToOne // optional=false? 외래키의 값이 null을 허용하지 않도록 설정
              // OneToOne에서 optional=false 옵션 설정 시 inner join 수행
    @JoinColumn(name = "product_number") // 원하는 칼럼명 지정 시 사용
    private Product product;
}
