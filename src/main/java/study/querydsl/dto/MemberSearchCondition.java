package study.querydsl.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * MemberSearchCondition
 */

@Data
@NoArgsConstructor
public class MemberSearchCondition {
    private String username;
    private String teamName;
    private Integer ageGoe;
    private Integer ageLoe;
}
