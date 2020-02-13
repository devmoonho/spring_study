package study.datajpa.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.Persistable;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * id를 @GeneratedValue 사용하지 않고 save 호출시 
 * persist 하지 않고 merge 하기때문에 의도하지 않게 값을 덮어 쓰게 됨
 * Entity에 Persistable 구현하여 Override로 해결
 * 
 * Item
 */

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item implements Persistable<String> {

    @Id
    @Column(name = "item_id")
    private String id;

    @CreatedDate
    private LocalDateTime createdDate;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return createdDate == null;
    }
}