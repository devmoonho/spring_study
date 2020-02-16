package study.querydsl;

import static com.querydsl.jpa.JPAExpressions.select;
import static org.assertj.core.api.Assertions.assertThat;
import static study.querydsl.entity.QMember.member;
import static study.querydsl.entity.QTeam.team;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.dto.MemberDto;
import study.querydsl.dto.UserDto;
import study.querydsl.entity.Member;
import study.querydsl.entity.QMember;
import study.querydsl.entity.Team;

/**
 * QuerydslBasicTest
 */

@SpringBootTest
@Transactional
// @Rollback(false)
@TestPropertySource(properties = "spring.config.location=classpath:application-test.yml" )
public class QuerydslBasicTest {

    @Autowired
    EntityManager em;

    JPAQueryFactory qf;

    @BeforeEach
    public void before() {
        qf = new JPAQueryFactory(em);
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 10, teamA);
        Member member3 = new Member("member3", 20, teamB);
        Member member4 = new Member("member4", 20, teamB);
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);
    }

    @Test
    public void sqlFunction2() throws Exception {
        // action

        // result
        List<String> result = qf.select(member.username).from(member)
                // .where(member.username
                //         .eq(Expressions.stringTemplate("function ('lower', {0})", member.username)))
                .where(member.username.eq(member.username.lower())).fetch();

        for (String s : result) {
            System.out.println("result : " + s);
        }
        // diff

        assertThat(0).isEqualTo(0);
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void sqlFunction() throws Exception {
        // action

        // result
        List<String> restult = qf.select(
                Expressions.stringTemplate("function( 'replace', {0} ,{1}, {2} )", member.username, "member", "M"))
                .from(member).fetch();

        for (String s : restult) {
            System.out.println("result :  " + s);
        }

        // diff

        assertThat(0).isEqualTo(0);
    }

    @Test
    public void bulkDelete() throws Exception {
        // action

        // result
        qf.delete(member).where(member.age.loe(10)).execute();

        // diff

        assertThat(0).isEqualTo(0);
    }

    /**
     * 영속성 컨택스트 내용이 DB Query 결과 값보다 우선하기 때문에 
     * 벌크 업데이트 이후 영속성 컨택스트를 비워줘서 flush(), clear() 
     * DB Query가 우선권을 가질 수 있게 해야한다. 
     */
    @Test
    public void bulkUpdate() throws Exception {
        // action

        // result
        // if age < 15 then username = "비회원"
        qf.update(member).set(member.username, "비회원").where(member.age.lt(15)).execute();

        // age -= 2;
        qf.update(member).set(member.age, member.age.add(-2)).execute();

        // age = age * 2
        qf.update(member).set(member.age, member.age.multiply(2)).execute();

        em.flush();
        em.clear();

        // diff

        assertThat(0).isEqualTo(0);
    }

    /**
     *  동적쿼리 
     *  WhereParam 이용
     *  where 절 내의 여러 메서드를 분리하여 
     *  필요할 때 마다 조합하여 사용할 수 있다.
     */
    @Test
    public void dynamicQuery_WhereParam() throws Exception {
        // action
        String usernameParam = "member1";
        Integer ageParam = null;

        // result
        List<Member> result = searchMember2(usernameParam, ageParam);
        System.out.println("result : " + result);

        // diff

        assertThat(0).isEqualTo(0);
    }

    /**
     * where 절 내의 값이 null 일 경우 무시됨
     * wehre 절 내의 조건들이 메서드로 분리되어 재사용성이 높아지고 
     * 쿼리 가독성이 높아진다.
     * 
     * @param usernameParam
     * @param ageParam
     * @return
     */
    private List<Member> searchMember2(String usernameParam, Integer ageParam) {
        return qf.selectFrom(member).where(usernameEq(usernameParam), ageEq(ageParam)).fetch();
    }

    private Predicate ageEq(Integer ageParam) {
        return ageParam != null ? member.age.eq(ageParam) : null;
    }

    private Predicate usernameEq(String usernameParam) {
        return usernameParam != null ? member.username.eq(usernameParam) : null;
    }

    /**
     * 동적쿼리 
     * BooleanBuilder 이용해서 쿼리 생성
     * 
     * @throws Exception
     */
    @Test
    public void dynamicQuery_Builder() throws Exception {
        // action
        String usernameParam = "member1";
        Integer ageParam = 10;

        // result 
        List<Member> result = searchMember1(usernameParam, ageParam);
        System.out.println("result : " + result);

        // diff

        assertThat(0).isEqualTo(0);
    }

    private List<Member> searchMember1(String usernameParam, Integer ageParam) {
        BooleanBuilder builder = new BooleanBuilder();

        if (usernameParam != null) {
            builder.and(member.username.eq(usernameParam));
        }

        if (ageParam != null) {
            builder.and(member.age.eq(ageParam));
        }

        return qf.selectFrom(member).where(builder).fetch();
    }

    /**
     * Dto 멤버변수와 QType Entity 멤버 변수명이 동일하지 않을 경우 Alias 사용
     * 
     * @throws Exception
     */
    @Test
    public void findDtoByQueryDsl_Alias() throws Exception {
        // action

        // result
        List<UserDto> result = qf
                .select(Projections.fields(UserDto.class, member.username.as("name"), member.age.as("old")))
                .from(member).fetch();

        for (UserDto userDto : result) {
            System.out.println("userDto : " + userDto);
        }

        /**
         * Alias와 SubQuery 조합
         */
        QMember memberSub = new QMember("memberSub");

        List<UserDto> result_alias_sub = qf
                .select(Projections.fields(UserDto.class, member.username.as("name"),
                        ExpressionUtils.as(JPAExpressions.select(memberSub.age.max()).from(memberSub), "old")))
                .from(member).fetch();

        for (UserDto userDto : result_alias_sub) {
            System.out.println("userDto sub : " + userDto);
        }

        // diff

        assertThat(0).isEqualTo(0);
    }

    @Test
    public void findDtoByQueryDsl() throws Exception {
        // action

        // result

        /**
         * setter 방식 
         * class에 기본 생성자, setter, getter 가 있어야함  
         */
        List<MemberDto> result_setter = qf.select(Projections.bean(MemberDto.class, member.username, member.age))
                .from(member).fetch();

        for (MemberDto memberDto : result_setter) {
            System.out.println("memberDto setter : " + memberDto);
        }

        /**
         * Field 방식
         * 멤버 변수에 직접 값 할당
         */
        List<MemberDto> result_field = qf.select(Projections.fields(MemberDto.class, member.username, member.age))
                .from(member).fetch();

        for (MemberDto memberDto : result_field) {
            System.out.println("memberDto field : " + memberDto);
        }

        /**
         * Constructor 방식
         */
        List<MemberDto> result_contstructor = qf
                .select(Projections.constructor(MemberDto.class, member.username, member.age)).from(member).fetch();

        for (MemberDto memberDto : result_contstructor) {
            System.out.println("memberDto constructor : " + memberDto);
        }

        // diff
        assertThat(0).isEqualTo(0);
    }

    @Test
    public void findDtoByJPQL() throws Exception {
        // action

        // result
        List<MemberDto> result = em
                .createQuery("select new study.querydsl.dto.MemberDto(m.username, m.age) " + " from Member m ",
                        MemberDto.class)
                .getResultList();

        for (MemberDto memberDto : result) {
            System.out.println("memberDto : " + memberDto);
        }

        // diff

        assertThat(0).isEqualTo(0);
    }

    /**
     * tuple 은 repository layer 에서만 사용하고 service , controller layer 까지 빼서 쓰지 말것 (Dto 사용할것)
     * tuple은 package com.querydsl.core  이기 때문에것
     * querydsl 을 다른 기술로 변경시 service, controller layer의 의존성을 가지게 된다.
     * 
     * @throws Exception
     */
    @Test
    public void projection() throws Exception {
        // action

        // result
        List<String> result = qf.select(member.username).from(member).fetch();

        for (String s : result) {
            System.out.println("result : " + s);
        }

        List<Tuple> result2 = qf.select(member.username, member.age).from(member).fetch();

        for (Tuple t : result2) {
            String username = t.get(member.username);
            Integer age = t.get(member.age);
            System.out.println("username : " + username);
            System.out.println("age : " + age);
        }

        // diff

        assertThat(0).isEqualTo(0);
    }

    @Test
    public void concat() throws Exception {
        // action

        // result
        // {username}_{age}
        List<String> result = qf.select(member.username.concat("_").concat(member.age.stringValue())).from(member)
                .fetch();

        for (String s : result) {
            System.out.println("result : " + s);
        }
        // diff

        assertThat(0).isEqualTo(0);
    }

    @Test
    public void constant() throws Exception {
        // action

        // result
        List<Tuple> result = qf.select(member.username, Expressions.constant("A")).from(member).fetch();

        for (Tuple tuple : result) {
            System.out.println("result : " + tuple);
        }

        // diff

        assertThat(0).isEqualTo(0);
    }

    @Test
    public void caseQuery() throws Exception {
        // action

        // result
        List<String> result = qf.select(member.age.when(10).then("열살").when(20).then("스무살").otherwise("기타"))
                .from(member).fetch();

        for (String s : result) {
            System.out.println("result : " + s);
        }

        List<String> result2 = qf.select(new CaseBuilder().when(member.age.between(0, 10)).then("0~10살")
                .when(member.age.between(11, 20)).then("11~20살").otherwise("기타")).from(member).from(member).from(member)
                .fetch();

        for (String s : result2) {
            System.out.println("result2 : " + s);
        }

        assertThat(0).isEqualTo(0);
    }

    @Test
    public void subQueryIn() throws Exception {
        // action
        QMember memberSub = new QMember("memberSub");

        // result
        // 나이가 sub query결과인 회원
        List<Member> result = qf.selectFrom(member)
                .where(member.age.in(select(memberSub.age).from(memberSub).where(memberSub.age.gt(10)))).fetch();

        // diff
        assertThat(result).extracting("age").containsExactly(20, 20);

        //result 
        List<Tuple> result1 = qf.select(member.username, select(memberSub.age.avg()).from(memberSub)).from(member)
                .fetch();

        for (Tuple tuple : result1) {
            System.out.println("tuple : " + tuple);
        }

        //diff

        assertThat(0).isEqualTo(0);
    }

    @Test
    public void subQuery() throws Exception {
        // action
        QMember memberSub = new QMember("memberSub");

        // result
        // 나이가 가장 많은 회원 찾기
        List<Member> result = qf.selectFrom(member)
                .where(member.age.eq(JPAExpressions.select(memberSub.age.max()).from(memberSub))).fetch();
        // diff
        assertThat(result).as("나이가 가장 많은 회원 찾기").extracting("age").containsExactly(20, 20);

        // 나이가 평균이상인 회원 찾기
        List<Member> result2 = qf.selectFrom(member)
                .where(member.age.goe(JPAExpressions.select(memberSub.age.avg()).from(memberSub))).fetch();

        assertThat(result2).as("나이가 평균이상인 회원 찾기").extracting("username").containsExactly("member3", "member4");

        assertThat(0).isEqualTo(0);
    }

    @PersistenceUnit
    EntityManagerFactory emf;

    @Test
    public void fetch_join() throws Exception {
        // action
        em.flush();
        em.clear();

        // result
        Member noFetch = qf.selectFrom(member).where(member.username.eq("member1")).fetchOne();
        boolean noFetchloaded = emf.getPersistenceUnitUtil().isLoaded(noFetch.getTeam());

        Member fetch = qf.selectFrom(member).join(member.team, team).fetchJoin().where(member.username.eq("member1"))
                .fetchOne();
        boolean fetchloaded = emf.getPersistenceUnitUtil().isLoaded(fetch.getTeam());

        // diff
        assertThat(noFetchloaded).as("페치조인 미적용").isFalse();
        assertThat(fetchloaded).as("페치조인 적용").isTrue();

        assertThat(0).isEqualTo(0);
    }

    /**
     * 연관관계가 없는 엔터티를 외부조인
     * 회원의 이름과 팀의 이름이 같은대상을 외부조인
     * 
     * join_on_filtering 테스트와 비교
     * leftJoin(member.team, team) = on member.team_id = team1.team_id
     * leftJoin(team).on(member.username.eq(team.name)) = username filtering 
     * 
     */
    @Test
    public void join_on_no_relation() throws Exception {
        // action
        em.persist(new Member("teamA"));
        em.persist(new Member("teamB"));
        em.persist(new Member("teamC"));

        // result
        List<Tuple> result = qf.select(member, team).from(member).join(team).on(member.username.eq(team.name)).fetch();

        for (Tuple tuple : result) {
            System.out.println("tuple : " + tuple);
        }
        // diff

        assertThat(0).isEqualTo(0);
    }

    /**
     * 예) 회원과 팀을 조인하면서, 팀 이름이 teamA인 팀만 조인, 회원은 모두 조회
     * JPQL: select m, t from Member m left join m.team t on t.name = 'teamA' 
     *  
     * join = inner join
     * 
     * @throws Exception
     */
    @Test
    public void join_on_filtering() throws Exception {
        // action

        // result
        List<Tuple> result = qf.select(member, team).from(member).leftJoin(member.team, team).on(team.name.eq("teamA"))
                .fetch();

        for (Tuple tuple : result) {
            System.out.println("tuple : " + tuple);
        }
        // diff

        assertThat(0).isEqualTo(0);
    }

    /**
     * 세타조인 
     * ex) 회원의 이름과 팀의 이름이 같은 회원 조회
     * foreign key 없이 서로다른 테이블을 통으로 조인
     *
     * @throws Exception
     */
    @Test
    public void join_theta() throws Exception {
        // action
        em.persist(new Member("teamA"));
        em.persist(new Member("teamB"));

        // result
        List<Member> result = qf.select(member).from(member, team).where(member.username.eq(team.name)).fetch();

        // diff
        assertThat(result).extracting("username").containsExactly("teamA", "teamB");

        assertThat(0).isEqualTo(0);
    }

    /**
     * TeamA 에 소속된 모든 회원을 찾아라
     * 
     * @throws Exception
     */
    @Test
    public void join() throws Exception {
        // action

        // result
        List<Member> result = qf.selectFrom(member).join(member.team, team).where(team.name.eq("teamA")).fetch();

        // diff
        assertThat(result).extracting("username").containsExactly("member1", "member2");

        assertThat(0).isEqualTo(0);
    }

    /**
     * 팀의 이름과 각 팀의 평균 연령을 구하라.
     * 
     * @throws Exception
     */
    @Test
    public void group() throws Exception {
        // action

        // result
        List<Tuple> result = qf.select(team.name, member.age.avg()).from(member).join(member.team, team)
                .groupBy(team.name).fetch();

        Tuple teamA = result.get(0);
        Tuple teamB = result.get(1);

        // diff
        assertThat(teamA.get(team.name)).isEqualTo("teamA");
        assertThat(teamA.get(member.age.avg())).isEqualTo(10);
        assertThat(teamB.get(team.name)).isEqualTo("teamB");
        assertThat(teamB.get(member.age.avg())).isEqualTo(20);

        assertThat(0).isEqualTo(0);
    }

    @Test
    public void aggregation() throws Exception {
        // action

        // result
        List<Tuple> result = qf
                .select(member.count(), member.age.sum(), member.age.avg(), member.age.max(), member.age.min())
                .from(member).fetch();
        Tuple tuple = result.get(0);

        // diff
        assertThat(tuple.get(member.count())).isEqualTo(4);
        assertThat(tuple.get(member.age.sum())).isEqualTo(60);
        assertThat(tuple.get(member.age.avg())).isEqualTo(15);
        assertThat(tuple.get(member.age.max())).isEqualTo(20);
        assertThat(tuple.get(member.age.min())).isEqualTo(10);

        assertThat(0).isEqualTo(0);
    }

    /**
     * fetchResult() 는 count query가 포함 실무에서는 
     * count query는 분리하는 것이 좋음 fetchResult는 관련 join 들이 모두 포함된
     * 상태에서 count query 하기 때문에 성능 최적화가 되지 않음
     */
    @Test
    public void paging2() throws Exception {
        // action

        // result
        QueryResults<Member> result = qf.selectFrom(member).orderBy(member.username.desc()).offset(1).limit(2)
                .fetchResults();

        // diff
        assertThat(result.getTotal()).isEqualTo(4);
        assertThat(result.getLimit()).isEqualTo(2);
        assertThat(result.getOffset()).isEqualTo(1);
        assertThat(result.getResults().size()).isEqualTo(2);

        assertThat(0).isEqualTo(0);
    }

    @Test
    public void paging1() throws Exception {
        // action

        // result
        List<Member> result = qf.selectFrom(member).orderBy(member.username.desc().nullsLast()).offset(0).limit(2)
                .fetch();

        for (Member member2 : result) {
            System.out.println("member.username : " + member2.getUsername());
        }
        // diff
        assertThat(result.size()).isEqualTo(2);
        assertThat(0).isEqualTo(0);

    }

    /**
     * 1.회원나이 내림차순 
     * 2.회원이름 오림차순 단 회원이름이 null 이면 마지막에 출력
     * 
     * @throws Exception
     */
    @Test
    public void sort() throws Exception {
        // action
        em.persist(new Member(null, 100));
        em.persist(new Member("member5", 100));
        em.persist(new Member("member6", 100));

        // result
        List<Member> result = qf.selectFrom(member).where(member.age.eq(100))
                .orderBy(member.age.desc(), member.username.asc().nullsLast()).fetch();

        Member member5 = result.get(0);
        Member member6 = result.get(1);
        Member memberNull = result.get(2);

        // diff
        assertThat(member5.getUsername()).isEqualTo("member5");
        assertThat(member6.getUsername()).isEqualTo("member6");
        assertThat(memberNull.getUsername()).isNull();

        assertThat(0).isEqualTo(0);
    }

    @Test
    public void resultFetch() throws Exception {
        // action

        // result
        qf.selectFrom(member).fetch();
        qf.selectFrom(member).where(member.username.eq("member1")).fetchOne();
        qf.selectFrom(member).fetchFirst();

        QueryResults<Member> result = qf.selectFrom(member).fetchResults();
        result.getTotal();
        List<Member> content = result.getResults();
        System.out.println("content : " + content);

        qf.selectFrom(member).fetchCount();

        // diff

        assertThat(0).isEqualTo(0);
    }

    @Test
    public void search() throws Exception {
        // action

        // result
        Member result = qf.selectFrom(member).where(member.username.eq("member1").and(member.age.eq(10))).fetchOne();

        // diff
        assertThat(result.getUsername()).isEqualTo("member1");
        assertThat(0).isEqualTo(0);
    }

    /**
     * 기본 - static import 로 사용할것 같은 테이블을 동시에 조인해야할 경우 구분해 주기 위해 
     * QMember m1 = new QMember("m1");
     * QMember m2 = new QMember("m2"); 
     * 위와 같이 alias 롤 구분할 수 있다.
     * 
     * @throws Exception
     */
    @Test
    public void startQueryDsl2() throws Exception {
        // action
        // import static study.querydsl.entity.QMember.member;
        // QMember m = member;

        // result
        Member result = qf.select(member).from(member).where(member.username.eq("member1")).fetchOne();

        // diff
        assertThat(result.getUsername()).isEqualTo("member1");

        assertThat(0).isEqualTo(0);
    }

    @Test
    public void startQueryDsl() throws Exception {
        // action
        // JPAQueryFactory qf = new JPAQueryFactory(em);
        QMember m = new QMember("m");

        // result
        Member result = qf.select(m).from(m).where(m.username.eq("member1")).fetchOne();

        // diff
        assertThat(result.getUsername()).isEqualTo("member1");
        assertThat(0).isEqualTo(0);
    }

    /**
     * 쿼리도 java로 만들수 있고 sql 문자 오류도 컴파일 시점에 찾아준다
     * 
     * @throws Exception
     */
    @Test
    public void startJPQL() throws Exception {
        // action

        // result
        String qlString = "select m from Member m where m.username = :username";
        Member result = em.createQuery(qlString, Member.class).setParameter("username", "member1").getSingleResult();

        // diff
        assertThat(result.getUsername()).isEqualTo("member1");
        assertThat(0).isEqualTo(0);
    }
}
