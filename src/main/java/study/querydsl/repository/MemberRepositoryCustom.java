package study.querydsl.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import study.querydsl.dto.MemberSearchCondition;
import study.querydsl.dto.MemberTeamDto;

/**
 * MemberRepositoryCustom
 */
public interface MemberRepositoryCustom {
    List<MemberTeamDto> search(MemberSearchCondition cond);

    Page<MemberTeamDto> searchPageWithCount(MemberSearchCondition cond, Pageable pageable);

    Page<MemberTeamDto> searchPageWithoutCount(MemberSearchCondition cond, Pageable pageable);

    Page<MemberTeamDto> searchPageOptimizedCount(MemberSearchCondition cond, Pageable pageable);
}
