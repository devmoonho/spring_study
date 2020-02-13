package study.datajpa.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import study.datajpa.entity.Member;

/**
 * MemberJpaRepositoryTest
 */

@SpringBootTest
@Transactional
@Rollback(false)
// @TestPropertySource(properties =
// "spring.config.location=classpath:application-test.yml" )
public class MemberJpaRepositoryTest {

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    @Test
    public void 벌크업데이트_테스트() throws Exception {
        // action
        Member member1 = new Member("member1", 10);
        Member member2 = new Member("member2", 11);
        Member member3 = new Member("member3", 13);
        Member member4 = new Member("member4", 16);
        Member member5 = new Member("member5", 16);
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);
        memberJpaRepository.save(member3);
        memberJpaRepository.save(member4);
        memberJpaRepository.save(member5);

        // result
        int result = memberJpaRepository.bulkAgePlus(13);
        
        Member member = memberJpaRepository.findByUsername("member5").get(0);

        // diff
        assertThat(result).isEqualTo(2);
        assertThat(member.getAge()).isEqualTo(17);

        assertEquals(0, 0);
        assertThat(0).isEqualTo(0);
    }

    @Test
    public void 페이징_테스트() throws Exception {
        // action
        Member member1 = new Member("member1", 10);
        Member member2 = new Member("member2", 10);
        Member member3 = new Member("member3", 10);
        Member member4 = new Member("member4", 10);
        Member member5 = new Member("member5", 10);
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);
        memberJpaRepository.save(member3);
        memberJpaRepository.save(member4);
        memberJpaRepository.save(member5);

        // result
        int age = 10;
        int offset = 1;
        int limit = 3;
        List<Member> result = memberJpaRepository.findByPage(age, offset, limit);
        Long count = memberJpaRepository.totalCount(age);

        // diff
        assertThat(result.size()).isEqualTo(3);
        assertThat(count).isEqualTo(5);

        assertEquals(0, 0);
        assertThat(0).isEqualTo(0);
    }

    @Test
    public void 네임드쿼리_테스트() throws Exception {
        // action
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        // result
        List<Member> result = memberJpaRepository.findByUsername("member1");

        // diff
        assertThat(result.get(0)).isEqualTo(member1);

        assertEquals(0, 0);
        assertThat(0).isEqualTo(0);
    }

    @Test
    public void testMemeber() throws Exception {
        // given
        final Member member = new Member("MemberA");
        final Member saveMember = memberJpaRepository.save(member);

        // when
        final Member findMember = memberJpaRepository.find(saveMember.getId());

        // then
        assertEquals(findMember.getId(), member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
    }

    @Test
    public void testCRUD() throws Exception {
        // given
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        // when
        Member findMember1 = memberJpaRepository.findById(member1.getId()).get();
        Member findMember2 = memberJpaRepository.findById(member2.getId()).get();

        // then
        assertThat(findMember1).isEqualTo(findMember1);
        assertThat(findMember2).isEqualTo(findMember2);

        // result
        List<Member> all = memberJpaRepository.findAll();

        // diff
        assertThat(all.size()).isEqualTo(2);

        // result
        Long count = memberJpaRepository.count();

        // diff
        assertThat(count).isEqualTo(2);

        // action
        memberJpaRepository.delete(member1);
        memberJpaRepository.delete(member2);

        // result
        Long deletedCount = memberJpaRepository.count();

        // diff
        assertThat(deletedCount).isEqualTo(0);

        assertEquals(0, 0);
        assertThat(0).isEqualTo(0);
    }
}