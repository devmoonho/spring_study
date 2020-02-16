package study.querydsl.repository;

import static org.springframework.util.StringUtils.hasText;
import static study.querydsl.entity.QMember.member;
import static study.querydsl.entity.QTeam.team;
import java.util.List;
import javax.persistence.EntityManager;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.support.PageableExecutionUtils;
import study.querydsl.dto.MemberSearchCondition;
import study.querydsl.dto.MemberTeamDto;
import study.querydsl.dto.QMemberTeamDto;
import study.querydsl.entity.Member;

/**
 * naming rule - 반드시 MemberRepository + Impl 형태로 메소드명 작성
 *
 * MemberRepositoryImpl
 */
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory qf;

    public MemberRepositoryImpl(EntityManager em) {
        this.qf = new JPAQueryFactory(em);
    }



    @Override
    public Page<MemberTeamDto> searchPageWithCount(MemberSearchCondition cond, Pageable pageable) {
        QueryResults<MemberTeamDto> result = qf
                .select(new QMemberTeamDto(member.id.as("memberId"), member.username, member.age,
                        team.id.as("teamId"), team.name.as("teamName")))
                .from(member)
                .where(usernameEq(cond.getUsername()), teamNameEq(cond.getTeamName()),
                        ageGoe(cond.getAgeGoe()), ageLoe(cond.getAgeLoe()))
                .offset(pageable.getOffset()).limit(pageable.getPageSize()).fetchResults();

        List<MemberTeamDto> content = result.getResults();
        long total = result.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<MemberTeamDto> searchPageWithoutCount(MemberSearchCondition cond,
            Pageable pageable) {
        List<MemberTeamDto> content = qf
                .select(new QMemberTeamDto(member.id.as("memberId"), member.username, member.age,
                        team.id.as("teamId"), team.name.as("teamName")))
                .from(member)
                .where(usernameEq(cond.getUsername()), teamNameEq(cond.getTeamName()),
                        ageGoe(cond.getAgeGoe()), ageLoe(cond.getAgeLoe()))
                .offset(pageable.getOffset()).limit(pageable.getPageSize()).fetch();

        long total = qf.selectFrom(member).leftJoin(member.team, team)
                .where(usernameEq(cond.getUsername()), teamNameEq(cond.getTeamName()),
                        ageGoe(cond.getAgeGoe()), ageLoe(cond.getAgeLoe()))
                .fetchCount();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<MemberTeamDto> searchPageOptimizedCount(MemberSearchCondition cond,
            Pageable pageable) {
        List<MemberTeamDto> content = qf
                .select(new QMemberTeamDto(member.id.as("memberId"), member.username, member.age,
                        team.id.as("teamId"), team.name.as("teamName")))
                .from(member)
                .where(usernameEq(cond.getUsername()), teamNameEq(cond.getTeamName()),
                        ageGoe(cond.getAgeGoe()), ageLoe(cond.getAgeLoe()))
                .offset(pageable.getOffset()).limit(pageable.getPageSize()).fetch();

        JPAQuery<Member> countQuery = qf.selectFrom(member).leftJoin(member.team, team).where(
                usernameEq(cond.getUsername()), teamNameEq(cond.getTeamName()),
                ageGoe(cond.getAgeGoe()), ageLoe(cond.getAgeLoe()));

        /**
         * 첫페이지, 마지막페이지의 컨텐츠가 페이지 사이즈 보다 적을 경우 
         * count query가 발생하지 않음 
         */
        return PageableExecutionUtils.getPage(content, pageable, () -> countQuery.fetchCount());

    }

    @Override
    public List<MemberTeamDto> search(MemberSearchCondition cond) {
        return qf
                .select(new QMemberTeamDto(member.id.as("memberId"), member.username, member.age,
                        team.id.as("teamId"), team.name.as("teamName")))
                .from(member).where(usernameEq(cond.getUsername()), teamNameEq(cond.getTeamName()),
                        ageGoe(cond.getAgeGoe()), ageLoe(cond.getAgeLoe()))
                .fetch();
    }

    private BooleanExpression ageLoe(Integer ageLoe) {
        return ageLoe != null ? member.age.loe(ageLoe) : null;
    }

    private BooleanExpression ageGoe(Integer ageGoe) {
        return ageGoe != null ? member.age.goe(ageGoe) : null;
    }

    private BooleanExpression teamNameEq(String teamName) {
        return hasText(teamName) ? team.name.eq(teamName) : null;
    }

    private BooleanExpression usernameEq(String username) {
        return hasText(username) ? member.username.eq(username) : null;
    }



}
