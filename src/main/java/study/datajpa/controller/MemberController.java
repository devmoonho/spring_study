package study.datajpa.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.repository.MemberRepository;

/**
 * MemberController
 */

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    // 조회
    @GetMapping("/member/{id}")
    public String findMember(@PathVariable("id") Long id) {
        Member item = memberRepository.findById(id).get();
        return item.getUsername();
    }

    /**
     * 권장하지 않는 방법 Param에 key 값이 아닌 Entity를 넘김 반드시 조회용으로만 사용할것
     * 
     * @param member
     * @return
     */
    @GetMapping("/member2/{id}")
    public String findMember2(@PathVariable("id") Member member) {
        return member.getUsername();
    }

    @GetMapping("/members/global")
    public Page<Member> list_global(Pageable pageable) {
        Page<Member> page = memberRepository.findAll(pageable);
        return page;
    }

    @GetMapping("/members/param")
    public Page<Member> list_param(@PageableDefault(size = 5, sort = "username") Pageable pageable) {
        Page<Member> page = memberRepository.findAll(pageable);
        return page;
    }

    /**
     * Dto로 무조건 변환해서 Api 제작해야함
     * @param pageable
     * @return
     */
    @GetMapping("/members/dto")
    public Page<MemberDto> list_dto(@PageableDefault(size = 5, sort = "username") Pageable pageable) {
        Page<Member> page = memberRepository.findAll(pageable);
        return page.map(p-> new MemberDto(p.getId(), p.getUsername(), null));
    }

    // @PostConstruct
    public void init() {
        for (int i = 0; i < 100; i++) {
            memberRepository.save(new Member("user" + i, 10 + i));
        }
    }

}