package jpabook.jpashop;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

/**
 * Member
 */

@Entity
@Getter
@Setter
public class Member {

    @Id
    @GeneratedValue
    private Long id;
    private String username;
}