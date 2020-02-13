package study.datajpa.repository;

import java.util.List;

import javax.persistence.EntityManager;

import lombok.RequiredArgsConstructor;
import study.datajpa.entity.Member;

/**
 * MemberRepositoryImpl  네이밍 규칙 
 * MemberRepository + Impl
 * MemberRepository - MemberRepository.java 클래스를 받아서 상요 
 * Impl - 구현체의 이름에 Impl로 적어줘야 인식가능
 */
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final EntityManager em;

    @Override
    public List<Member> findMemberCustom() {
        return em.createQuery("select m from Member m", Member.class).getResultList();
    }

    @Override
    public List<Member> findMemberByUsernameCustom(String username) {
        return em.createQuery("select m from Member m where m.username = :username", Member.class)
                .setParameter("username", username).getResultList();
    }
}