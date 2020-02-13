package study.datajpa.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import study.datajpa.dto.MemberDto;
import study.datajpa.dto.MemberNativeQueryDto;
import study.datajpa.dto.MemberProjectionsDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

/**
 * MemberRepositoryTest
 */

@SpringBootTest
@Transactional
@Rollback(false)
// @TestPropertySource(properties =
// "spring.config.location=classpath:application-test.yml")
public class MemberRepositoryTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TeamRepository teamRepository;

    @Test
    public void Native쿼리_WithProjections_테스트() throws Exception {
        // action
        Team team1 = new Team("team1");
        teamRepository.save(team1);

        Member member1 = new Member("member1", 10, team1);
        Member member2 = new Member("member2", 10, team1);
        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        // result
        Page<MemberNativeQueryDto> result = memberRepository.findByNativeProjectionQuery(PageRequest.of(0, 10));
        List<MemberNativeQueryDto> content = result.getContent();

        System.out.println("");
        for (MemberNativeQueryDto memberNativeQueryDto : content) {
            System.out.println("member.username : " + memberNativeQueryDto.getUsername());
            System.out.println("member.teamname: " + memberNativeQueryDto.getTeamName());
        }
        System.out.println("");

        // diff

        assertEquals(0, 0);
        assertThat(0).isEqualTo(0);
    }

    @Test
    public void Native쿼리_테스트() throws Exception {
        // action
        Team team1 = new Team("team1");
        teamRepository.save(team1);

        Member member1 = new Member("member1", 10, team1);
        Member member2 = new Member("member2", 10, team1);
        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        // result
        Member result = memberRepository.findByNativeQuery("member1");

        // diff
        System.out.println("member : " + result.getUsername());

        assertEquals(0, 0);
        assertThat(0).isEqualTo(0);
    }

    @Test
    public void Projections_테스트() throws Exception {
        // action
        Team team1 = new Team("team1");
        teamRepository.save(team1);

        Member member1 = new Member("member1", 10, team1);
        Member member2 = new Member("member2", 10, team1);
        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        // result
        List<UsernameOnly> result = memberRepository.findProjectionsByUsername("member1");
        List<UsernameOnlyDto> resultDto = memberRepository.findProjectionsDtoByUsername("member1");
        List<UsernameOnlyDto> resultDto2 = memberRepository.findProjectionsGenericDtoByUsername("member1",
                UsernameOnlyDto.class);
        List<MemberProjectionsDto> result2DepthDto = memberRepository.findProjectionsGenericDtoByUsername("member1",
                MemberProjectionsDto.class);

        // diff
        System.out.println("#################");
        for (UsernameOnly usernameOnly : result) {
            System.out.println("username : " + usernameOnly.getUsername());
        }

        for (UsernameOnlyDto usernameOnly : resultDto) {
            System.out.println("username Dto : " + usernameOnly.getUsername());
        }

        for (UsernameOnlyDto usernameOnly : resultDto2) {
            System.out.println("username Generic Dto : " + usernameOnly.getUsername());
        }
        for (MemberProjectionsDto usernameOnly : result2DepthDto) {
            System.out.println("username Generic Dto : " + usernameOnly.getUsername());
        }
        System.out.println("#################");

        assertEquals(0, 0);
        assertThat(0).isEqualTo(0);
    }

    @Test
    public void 사용자정의_쿼리_테스트() throws Exception {
        // action
        Member member1 = new Member("user1", 10);
        Member member2 = new Member("user2", 20);
        Member member3 = new Member("user3", 11);
        Member member4 = new Member("user4", 12);
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);

        // result
        List<Member> membersCustom = memberRepository.findMemberCustom();
        for (Member member : membersCustom) {
            System.out.println("memeber : " + member.getUsername());
        }

        List<Member> memberByUsername = memberRepository.findMemberByUsernameCustom("user3");
        for (Member member : memberByUsername) {
            System.out.println("member.username : " + member.getUsername());
        }

        // diff

        assertEquals(0, 0);
        assertThat(0).isEqualTo(0);
    }

    private void actionCreateMember() {
        Member member1 = new Member("user2", 10);
        Member member2 = new Member("user2", 20);
        Member member3 = new Member("user2", 11);
        Member member4 = new Member("user2", 12);
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);
    }

    @Test
    public void 쿼리락_테스트() throws Exception {
        // action
        Member member1 = new Member("member1", 10);
        memberRepository.save(member1);
        em.flush();
        em.clear();

        // result
        memberRepository.findLockByUsername("member1");

        // diff

        assertEquals(0, 0);
        assertThat(0).isEqualTo(0);
    }

    @Test
    public void 쿼리힌트_테스트() throws Exception {
        // action
        Member member1 = new Member("member1", 10);
        memberRepository.save(member1);
        em.flush();
        em.clear();

        // result
        Member result = memberRepository.findReadOnlyByUsername("member1");
        result.setUsername("member2"); // update 쿼리 발생하지 않음 => readonly

        em.flush();

        // diff

        assertEquals(0, 0);
        assertThat(0).isEqualTo(0);
    }

    @Test
    public void 페치조인_테스트() throws Exception {
        // action
        Team team1 = new Team("team1");
        Team team2 = new Team("team2");
        teamRepository.save(team1);
        teamRepository.save(team2);

        Member member1 = new Member("member1", 10, team1);
        Member member2 = new Member("member2", 10, team2);
        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        // result
        List<Member> result = memberRepository.findAll();

        // diff
        // 1 + n 문제 발생 (team 쿼리 갯수 만큼 쿼리 발생)
        for (Member member : result) {
            System.out.println("member.name : " + member.getUsername()); // 1
            System.out.println("member.team.name: " + member.getTeam().getName()); // n
        }

        // result
        List<Member> resultFetch = memberRepository.findMemberFetchJoin();

        // 1 + n 문제 해결 (쿼리를 한번만 호출 1 + 1)
        for (Member member : resultFetch) {
            System.out.println("member.name : " + member.getUsername()); // 1
            System.out.println("member.team.name: " + member.getTeam().getName()); // 1
        }

        assertEquals(0, 0);
        assertThat(0).isEqualTo(0);
    }

    @Test
    public void 벌크업데이트_테스트() throws Exception {
        // action
        Member member1 = new Member("member1", 10);
        Member member2 = new Member("member2", 11);
        Member member3 = new Member("member3", 13);
        Member member4 = new Member("member4", 16);
        Member member5 = new Member("member5", 16);
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);
        memberRepository.save(member5);

        // result
        int result = memberRepository.bulkAgePlus(13);

        Member member = memberRepository.findByNames(Arrays.asList("member5")).get(0);

        // diff
        assertThat(result).isEqualTo(2);

        assertThat(member.getAge()).isEqualTo(17);

        assertEquals(0, 0);
        assertThat(0).isEqualTo(0);
    }

    @Test
    public void 페이징_Dto반환_테스트() throws Exception {
        // action
        Member member1 = new Member("member1", 10);
        Member member2 = new Member("member2", 10);
        Member member3 = new Member("member3", 10);
        Member member4 = new Member("member4", 10);
        Member member5 = new Member("member5", 10);
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);
        memberRepository.save(member5);

        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        int age = 10;

        // result
        Page<Member> page = memberRepository.findPageByAge(age, pageRequest);
        Page<MemberDto> pageDto = page.map(member -> new MemberDto(member1.getId(), member1.getUsername(), null));

        // diff
        assertThat(pageDto.getSize()).isEqualTo(3);
        assertThat(pageDto.getNumber()).isEqualTo(0);
        assertThat(pageDto.hasNext()).isTrue();
        assertThat(pageDto.isFirst()).isTrue();
        assertThat(pageDto.getTotalPages()).isEqualTo(2);
        assertThat(pageDto.getTotalElements()).isEqualTo(5);

        assertEquals(0, 0);
        assertThat(0).isEqualTo(0);
    }

    @Test
    public void 슬라이스_테스트() throws Exception {
        // action
        Member member1 = new Member("member1", 10);
        Member member2 = new Member("member2", 10);
        Member member3 = new Member("member3", 10);
        Member member4 = new Member("member4", 10);
        Member member5 = new Member("member5", 10);
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);
        memberRepository.save(member5);

        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        int age = 10;

        // result
        Slice<Member> page = memberRepository.findSliceByAge(age, pageRequest);
        List<Member> contents = page.getContent();

        for (Member member : contents) {
            System.out.println("member : " + member);
        }

        // diff
        assertThat(page.getSize()).isEqualTo(3);
        // assertThat(page.getTotalElements()).isEqualTo(5);
        assertThat(page.getNumber()).isEqualTo(0);
        // assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();

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
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);
        memberRepository.save(member5);

        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        int age = 10;

        // result
        Page<Member> page = memberRepository.findPageByAge(age, pageRequest);
        List<Member> contents = page.getContent();
        long count = page.getTotalElements();

        for (Member member : contents) {
            System.out.println("member : " + member);
        }
        System.out.println("count : " + count);

        // diff

        assertThat(page.getSize()).isEqualTo(3);
        assertThat(page.getTotalElements()).isEqualTo(5);
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();

        assertEquals(0, 0);
        assertThat(0).isEqualTo(0);
    }

    @Test
    public void 페이징_최적화_비교_테스트() throws Exception {
        // action
        Member member1 = new Member("member1", 10);
        Member member2 = new Member("member2", 10);
        Member member3 = new Member("member3", 10);
        Member member4 = new Member("member4", 10);
        Member member5 = new Member("member5", 10);
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);
        memberRepository.save(member5);

        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        int age = 10;

        // result
        Page<Member> optPage = memberRepository.findOptimizeByAge(age, pageRequest);
        optPage.getContent(); // count 쿼리를 따로 작성하여 join 미포함

        Page<Member> noOptPage = memberRepository.findNoOptimizeByAge(age, pageRequest);
        noOptPage.getContent(); // count 쿼리애 join 포함 => 성능이슈 발생
        // result

        // diff

        assertEquals(0, 0);
        assertThat(0).isEqualTo(0);
    }

    @Test
    public void 페이징_최적화_테스트() throws Exception {
        // action
        Member member1 = new Member("member1", 10);
        Member member2 = new Member("member2", 10);
        Member member3 = new Member("member3", 10);
        Member member4 = new Member("member4", 10);
        Member member5 = new Member("member5", 10);
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);
        memberRepository.save(member5);

        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        int age = 10;

        // result
        Page<Member> optPage = memberRepository.findOptimizeByAge(age, pageRequest);
        List<Member> contents = optPage.getContent();
        long count = optPage.getTotalElements();

        for (Member member : contents) {
            System.out.println("member : " + member);
        }
        System.out.println("count : " + count);

        // diff
        assertThat(optPage.getSize()).isEqualTo(3);
        assertThat(optPage.getTotalElements()).isEqualTo(5);
        assertThat(optPage.getNumber()).isEqualTo(0);
        assertThat(optPage.getTotalPages()).isEqualTo(2);
        assertThat(optPage.isFirst()).isTrue();
        assertThat(optPage.hasNext()).isTrue();

        assertEquals(0, 0);
        assertThat(0).isEqualTo(0);
    }

    @Test
    public void 반환타입_Optional() throws Exception {
        // action
        Member member0 = new Member("user0", 0);
        Member member1 = new Member("user1", 10);
        Member member2 = new Member("user2", 20);
        Member member3 = new Member("user2", 11);
        Member member4 = new Member("user2", 12);
        memberRepository.save(member0);
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);

        // result
        Optional<Member> result1 = memberRepository.findOptionalMemberByName("user1");
        Optional<Member> result2 = memberRepository.findOptionalMemberByName("user1234");

        // diff
        assertThat(result1.get()).isEqualTo(member1);
        assertThat(result2.orElse(member0)).isEqualTo(member0);

        assertEquals(0, 0);
        assertThat(0).isEqualTo(0);
    }

    @Test
    public void JPQL_InQuery_테스트() throws Exception {
        // action
        Member member1 = new Member("user1", 10);
        Member member2 = new Member("user2", 20);
        Member member3 = new Member("user3", 11);
        Member member4 = new Member("user4", 12);
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);

        // result
        List<Member> result = memberRepository.findByNames(Arrays.asList("user2", "user3"));

        // diff
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getUsername()).isEqualTo("user2");
        assertThat(result.get(1).getUsername()).isEqualTo("user3");

        assertEquals(0, 0);
        assertThat(0).isEqualTo(0);
    }

    @Test
    public void JPQL_Dto반환_테스트() throws Exception {
        // action
        Team team1 = new Team("team1");
        Team team2 = new Team("team2");
        teamRepository.save(team1);
        teamRepository.save(team2);

        Member member1 = new Member("user2", 10);
        Member member2 = new Member("user2", 20);
        Member member3 = new Member("user2", 11);
        Member member4 = new Member("user2", 12);
        member1.setTeam(team1);
        member2.setTeam(team2);
        member3.setTeam(team1);
        member4.setTeam(team2);

        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);

        Member member5 = new Member("user5", 10, team2);
        memberRepository.save(member5);

        // result
        List<MemberDto> result = memberRepository.findMemberDtos();

        // diff
        assertThat(result.size()).isEqualTo(5);
        assertThat(result.get(0).getUsername()).isEqualTo("user2");
        assertThat(result.get(0).getTeamname()).isEqualTo("team1");
        assertThat(result.get(4).getUsername()).isEqualTo("user5");
        assertThat(result.get(4).getTeamname()).isEqualTo("team2");

        assertEquals(0, 0);
        assertThat(0).isEqualTo(0);
    }

    @Test
    public void JPQL_테스트2() throws Exception {
        // action
        Member member1 = new Member("user2", 10);
        Member member2 = new Member("user2", 20);
        Member member3 = new Member("user2", 11);
        Member member4 = new Member("user2", 12);
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);

        // result
        List<String> result = memberRepository.findUsername();

        // diff
        assertThat(result.size()).isEqualTo(4);
        assertThat(result.get(0)).isEqualTo(member1.getUsername());

        assertEquals(0, 0);
        assertThat(0).isEqualTo(0);
    }

    @Test
    public void 쿼리정의_테스트() throws Exception {
        // action
        Member member1 = new Member("user2", 10);
        Member member2 = new Member("user2", 20);
        Member member3 = new Member("user2", 11);
        Member member4 = new Member("user2", 12);
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);

        // result
        List<Member> result = memberRepository.findUser("user2", 20);

        // diff
        assertThat(result.get(0)).isEqualTo(member2);

        assertEquals(0, 0);
        assertThat(0).isEqualTo(0);
    }

    @Test
    public void 네임드쿼리_테스트() throws Exception {
        // action
        actionCreateMember();

        // result
        List<Member> result = memberRepository.findByUsername("user2");

        // diff
        assertThat(result.size()).isEqualTo(4);

        assertEquals(0, 0);
        assertThat(0).isEqualTo(0);
    }

    @Test
    public void 상위3개() throws Exception {
        // action
        actionCreateMember();

        // result
        List<Member> result = memberRepository.findTop3HelloBy();

        // diff
        assertThat(result.size()).isEqualTo(3);

        assertEquals(0, 0);
        assertThat(0).isEqualTo(0);
    }

    @Test
    public void 쿼리메소드_테스트() throws Exception {
        actionCreateMember();

        // result
        List<Member> members = memberRepository.findByUsernameAndAgeGreaterThan("user2", 11);

        // diff
        assertThat(members.get(0).getUsername()).isEqualTo("user2");
        assertThat(members.size()).isEqualTo(2);

        assertEquals(0, 0);
        assertThat(0).isEqualTo(0);
    }

    @Test
    public void testCRUD() throws Exception {
        // given
        final Member member1 = new Member("member1");
        final Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        // when
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();

        // then
        assertThat(findMember1).isEqualTo(findMember1);
        assertThat(findMember2).isEqualTo(findMember2);

        // result
        List<Member> all = memberRepository.findAll();

        // diff
        assertThat(all.size()).isEqualTo(2);

        // result
        Long count = memberRepository.count();

        // diff
        assertThat(count).isEqualTo(2);

        // action
        memberRepository.delete(member1);
        memberRepository.delete(member2);

        // result
        Long deletedCount = memberRepository.count();

        // diff
        assertThat(deletedCount).isEqualTo(0);

        assertEquals(0, 0);
        assertThat(0).isEqualTo(0);
    }

    @Test
    public void testMember() throws Exception {
        // given
        Member member = new Member("memberA");
        Member saveMember = memberRepository.save(member);

        // when
        Member findMember = memberRepository.findById(saveMember.getId()).get();

        // then
        // assertEquals("msg" ,0 , 0);
        assertThat(saveMember.getId()).isEqualTo(findMember.getId());
        assertThat(saveMember.getUsername()).isEqualTo(findMember.getUsername());
        assertThat(saveMember).isEqualTo(findMember);
    }
}