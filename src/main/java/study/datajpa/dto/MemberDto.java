package study.datajpa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * MemberDto
 */

@Data
@AllArgsConstructor
public class MemberDto {
    private Long id;
    private String username;
    private String teamname;
}