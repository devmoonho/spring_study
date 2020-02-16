package study.querydsl.repository;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.dto.MemberSearchCondition;
import study.querydsl.dto.MemberTeamDto;
import study.querydsl.entity.Member;
import study.querydsl.entity.Team;

/**
 * MemberRepositoryTest
 */

@SpringBootTest
@Transactional
// @Rollback(false)
@TestPropertySource(properties = "spring.config.location=classpath:application-test.yml")
public class MemberRepositoryTest {

    @Autowired
    EntityManager em;

    @Autowired
    MemberRepository memberRepository;

    @BeforeEach
    public void before() {

    }

    @Test
    public void search_page_test() throws Exception {
        // action
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);
        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        MemberSearchCondition cond = new MemberSearchCondition();
        PageRequest pageable = PageRequest.of(0, 3);

        // result
        Page<MemberTeamDto> result_withCount = memberRepository.searchPageWithCount(cond, pageable);

        // diff
        assertThat(result_withCount.getSize()).isEqualTo(3);
        assertThat(result_withCount.getContent()).extracting("username").containsExactly("member1",
                "member2", "member3");

        // result
        System.out.println("### RESULT #########################");
        Page<MemberTeamDto> result_withoutCount =
                memberRepository.searchPageWithCount(cond, pageable);

        // diff
        assertThat(result_withoutCount.getSize()).isEqualTo(3);
        assertThat(result_withoutCount.getContent()).extracting("username")
                .containsExactly("member1", "member2", "member3");

        // result
        System.out.println("### RESULT #########################");
        Page<MemberTeamDto> result_OptimizedCount =
                memberRepository.searchPageOptimizedCount(cond, pageable);

        // diff
        assertThat(result_OptimizedCount.getSize()).isEqualTo(3);
        assertThat(result_OptimizedCount.getContent()).extracting("username")
                .containsExactly("member1", "member2", "member3");

        assertThat(0).isEqualTo(0);
    }

    @Test
    public void search_test() throws Exception {
        // action
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);
        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        MemberSearchCondition searchCond = new MemberSearchCondition();
        searchCond.setAgeGoe(35);
        searchCond.setAgeLoe(40);
        searchCond.setUsername("member4");
        searchCond.setTeamName("teamB");

        // result
        List<MemberTeamDto> result = memberRepository.search(searchCond);

        // diff
        assertThat(result).extracting("username").containsExactly("member4");

        assertThat(0).isEqualTo(0);
    }

    @Test
    public void basic_test() throws Exception {
        // action
        Member member = new Member("member1", 10);
        memberRepository.save(member);

        // result
        Member findMember = memberRepository.findById(member.getId()).get();

        // diff
        assertThat(findMember).isEqualTo(member);

        // result 
        List<Member> result1 = memberRepository.findAll();

        // diff
        assertThat(result1).containsExactly(member);

        // result 
        List<Member> result2 = memberRepository.findByUsername("member1");

        // diff
        assertThat(result2).containsExactly(member);

        assertThat(0).isEqualTo(0);;
    }

}
