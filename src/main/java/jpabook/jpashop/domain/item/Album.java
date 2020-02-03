package jpabook.jpashop.domain.item;

import javax.persistence.Entity;

import lombok.Getter;

/**
 * Album
 */
@Entity
@Getter
public class Album  extends Item{
    private String artiest;
    private String etc;
}