package study.datajpa.repository;

import java.util.List;

import study.datajpa.entity.Member;

/**
 * MemberRepositoryCustom
 */
public interface MemberRepositoryCustom {

      List<Member> findMemberCustom();
      List<Member> findMemberByUsernameCustom(String username);
}