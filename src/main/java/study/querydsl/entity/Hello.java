package study.querydsl.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

/**
 * Hello
 */

@Entity
@Getter
@Setter
// @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Hello {
    @Id
    @GeneratedValue
    @Column(name = "hello_id")
    private Long id;

    // === 생성 메서드 ===//

    // === 비즈니스 메서드 ===//

    // === 조회 메서드 ===//

}