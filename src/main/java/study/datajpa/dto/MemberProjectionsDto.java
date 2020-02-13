package study.datajpa.dto;

/**
 * MemberProjectionsDto
 */
public interface MemberProjectionsDto {

    String getUsername();

    TeamInfo getTeam();

    interface TeamInfo {
        String getName();
    }
}