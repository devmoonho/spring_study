package study.datajpa.entity;

import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import study.datajpa.repository.MemberRepository;

/**
 * MemberTest
 */

@SpringBootTest
@Transactional
@Rollback(false)
// @TestPropertySource(properties =
// "spring.config.location=classpath:application-test.yml" )
public class MemberTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    MemberRepository memberRepository;

    @Test
    public void BaseEntity상속2_테스트() throws Exception {
        // public class Member  extends BaseEntity 
        // action
        Member member = new Member("member1");
        memberRepository.save(member); // @PrePersist() 발생
    
        Thread.sleep(100);

        member.setUsername("member2");

        em.flush();  //@PreUpdate() 발생
        em.clear();

        // result
        Member findMember = memberRepository.findById(member.getId()).get();

        System.out.println("######################");
        System.out.println("");
    
        System.out.println("findMember.createDate : " + findMember.getCreatedDate());
        System.out.println("findMember.lastModifiedDate : " + findMember.getLastModifiedDate());
        System.out.println("findMember.createdBy: " + findMember.getCreatedBy());
        System.out.println("findMember.lastModifiedBy : " + findMember.getLastModifiedBy());
        
        System.out.println("");
        System.out.println("######################");
        
        // diff
        assertEquals(0 , 0);
        assertThat(0).isEqualTo(0);
    }
  
    @Test
    public void BaseEntity상속_테스트() throws Exception {
        // public class Member  extends JpaBaseEntity 
        // action
        Member member = new Member("member1");
        memberRepository.save(member); // @PrePersist() 발생
    
        Thread.sleep(100);

        member.setUsername("member2");

        em.flush();  //@PreUpdate() 발생
        em.clear();

        // result
        Member findMember = memberRepository.findById(member.getId()).get();
    
        System.out.println("findMember.createDate : " + findMember.getCreatedDate());
        System.out.println("findMember.lastModifiedDate : " + findMember.getLastModifiedDate());
        
        // diff
        assertEquals(0 , 0);
        assertThat(0).isEqualTo(0);
    }
    
    @Test
    public void testMemberEnity() throws Exception {
        // given
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        // Team teamC = new Team("teamC");
        // Team teamD = new Team("teamD");
        em.persist(teamA);
        em.persist(teamB);
        // em.persist(teamC);
        // em.persist(teamD);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);
        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        em.flush();
        em.clear();

        // when
        List<Member> members = em.createQuery("select m from Member m", Member.class).getResultList();

        // then
        for (Member member : members) {
           System.out.println("member : " + member); 
           System.out.println("team : " + member.getTeam());
        }

        // assertEquals(0 , 0);
        // assertThat(0).isEqualTo(0);
    }
}