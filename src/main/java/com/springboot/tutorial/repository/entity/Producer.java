package com.springboot.tutorial.repository.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Table(name = "producer")
public class Producer extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    @ToString.Exclude
    // M:N 연관관계에서 리스트를 필드로 가지는 객체에서는 외래키를 가지지 않기 때문에
    // @JoinColumn 어노테이션을 따로 설정할 필요가 없다.
    // 또한, 외래키도 Producer 테이블에 생성되지 않으며 조인을 위한 중간 테이블이 생성된다.
    private List<Product> products = new ArrayList<>();

    public void addProduct(Product product) {
        products.add(product);
    }
}
