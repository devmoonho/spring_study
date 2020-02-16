package study.querydsl.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * UserDto
 */

@Data
@NoArgsConstructor
public class UserDto {

    private String name;
    private int old;

    public UserDto(String name, int old) {
        this.name = name;
        this.old = old;
    }
}