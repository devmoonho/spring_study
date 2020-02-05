package jpabook.jpashop.domain.item;

import javax.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

/**
 * Album
 */
@Entity
@Getter
@Setter
public class Album  extends Item{
    private String artiest;
    private String etc;
}