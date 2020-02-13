package study.datajpa.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import study.datajpa.dto.MemberDto;
import study.datajpa.dto.MemberNativeQueryDto;
import study.datajpa.entity.Member;

/**
 * MemberRepository
 */
public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    List<Member> findTop3HelloBy();

    @Query(name = "Member.findByUsername")
    List<Member> findByUsername(@Param("username") String username);

    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select m.username from Member m")
    List<String> findUsername();

    @Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name)" + " from Member m" + " join m.team t ")
    List<MemberDto> findMemberDtos();

    @Query("select m from Member m where m.username in :names ")
    List<Member> findByNames(@Param("names") Collection<String> names);

    /** 지원 반환타입 */
    @Query("select m from Member m where m.username = :name")
    Optional<Member> findOptionalMemberByName(@Param("name") String name);

    /** 페이징 쿼리 */
    Page<Member> findPageByAge(@Param("age") int age, Pageable Pageable);

    Slice<Member> findSliceByAge(@Param("age") int age, Pageable Pageable);

    /** 페이징 쿼리 최적화 */
    @Query("select m from Member m left join m.team t")
    Page<Member> findNoOptimizeByAge(@Param("age") int age, Pageable Pageable);

    @Query(value = "select m from Member m left join m.team t", countQuery = "select count(m) from Member m ")
    Page<Member> findOptimizeByAge(@Param("age") int age, Pageable Pageable);

    /** 벌크업데이트 */
    @Modifying(clearAutomatically = true) // jpa 의 executeUpdate() 와 같다.
    @Query("update Member m set m.age = m.age + 1 where m.age > :age")
    int bulkAgePlus(@Param("age") int age);

    /**
     * 페치조인 맴버변수를 LAZY로 호출하지 않고 join fetch를 통해 한번 쿼리로 받아옴
     */
    @Query("select m from Member m join fetch m.team")
    List<Member> findMemberFetchJoin();

    // join fetch Override 법법
    @Override
    @EntityGraph(attributePaths = { "team" })
    List<Member> findAll();

    // query 작성 fetch 방법
    @EntityGraph(attributePaths = { "team" })
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();

    // 메서드네임 작성 fetch 방법
    @EntityGraph(attributePaths = { "team" })
    List<Member> findEntityGraphByUsername(@Param("username") String username);

    /**
     * ReadOnly 최적화 읽기 전용으로 변경되지 않고, 내부적으로 최적화 되어 있어 성능우수 실무에서는 잘 쓰지 않고 꼭 필요한 곳에만
     * 적용한다
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Member findReadOnlyByUsername(String username);

    /**
     * DB lock
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findLockByUsername(String username);

    /**
     * Projections
     * 
     * root 는 최적화 가능 2depth 부터 전체 entity 조회 ( 모든 필드를 조회 ) interface
     * MemberProjectionsDto 참고
     * 
     */
    List<UsernameOnly> findProjectionsByUsername(String username);

    List<UsernameOnlyDto> findProjectionsDtoByUsername(String username);

    <T> List<T> findProjectionsGenericDtoByUsername(String username, Class<T> type);

    /**
     * Native Query
     */
    @Query(value = "select * from member where username =?", nativeQuery = true)
    Member findByNativeQuery(String username);

    @Query(value = "select m.member_id as id, m.username, t.name as teamName"
            + " from member m left join team t ", countQuery = "select count(*) from member", nativeQuery = true)
    Page<MemberNativeQueryDto> findByNativeProjectionQuery(Pageable pageable);

}
