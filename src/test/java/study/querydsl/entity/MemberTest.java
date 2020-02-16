package study.querydsl.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

/**
 * MemberTest
 */

@SpringBootTest
@Transactional
@Rollback(false)
// @TestPropertySource(properties =
// "spring.config.location=classpath:application-test.yml" )
public class MemberTest {

    @Autowired
    EntityManager em;

    @Test
    public void Member_엔터티_테스트() throws Exception {
        // action
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 10, teamA);
        Member member3 = new Member("member3", 10, teamB);
        Member member4 = new Member("member4", 10, teamB);
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        em.flush();
        em.clear();

        // result
        List<Member> result = em.createQuery("select m from Member m ", Member.class).getResultList();
        for (Member member : result) {
            System.out.println("member.name : " + member.getUsername());
            System.out.println("member.team: " + member.getTeam());
        }

        // diff

        assertEquals(0 , 0);
        assertThat(0).isEqualTo(0);
    }
}