package jpabook.jpashop.domain.item;

import javax.persistence.Entity;

import lombok.Getter;

/**
 * Movie
 */
@Entity
@Getter
public class Movie extends Item{
    private String director;
    private String actor;
}