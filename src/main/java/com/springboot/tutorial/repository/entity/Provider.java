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
@Table(name = "provider")
public class Provider extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    // mappedBy로 설정된 필드는 실제 데이터베이스 컬럼에는 변경 및 적용되지 않는다.
    // 즉, 양방향 연관관계 일 때 RDBMS의 형식처럼 사용하기 위함이다.
    @OneToMany(mappedBy = "provider", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Product> product = new ArrayList<>();
}