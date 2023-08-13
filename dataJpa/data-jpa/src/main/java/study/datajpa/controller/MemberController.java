package study.datajpa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.repository.MemberRepository;

import javax.annotation.PostConstruct;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    //pathvariable로 넘어온 id값을 바탕으로 datajpa에 쿼리날림
    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long id)
    {
        Member member = memberRepository.findById(id).get();
        return member.getUsername();
    }

    //Web 확장(앞선 getMapping v1확장) - 도메인 클래스 컨버터 이용
    // HTTP 파라미터로 넘어온 엔티티의 아이디로 엔티티 객체를 찾아서 바인딩
    @GetMapping("/members2/{id}")
    public String findMember2(@PathVariable("id") Member member) //이렇게 해도 자동으로 연결을 해준다!!
//    주의: 도메인 클래스 컨버터로 엔티티를 파라미터로 받으면, 이 엔티티는 단순 조회용으로만 사용해야 한다.
//            (트랜잭션이 없는 범위에서 엔티티를 조회했으므로, 엔티티를 변경해도 DB에 반영되지 않는다.)
    {
        return member.getUsername();
    }


//    Web 확장 - 페이징과 정렬
//    스프링 데이터가 제공하는 페이징과 정렬 기능을 스프링 MVC에서 편리하게 사용할 수 있다
    @GetMapping("/members")
    public Page<MemberDto> list(@PageableDefault(size = 5) Pageable pageable){ //글로벌보다 우선한건 로컬 페이지 세팅이다! PageableDefault이건 로컬 세팅
        Page<Member> page = memberRepository.findAll(pageable); //json으로 모든 회원의 정보가 page형태로 나감(기본은 20개씩 잘리는듯?->global setting은 yml파일에서!)
        //만약 localhost://...members?page=2이라고 적으면 2번째 페이징 된 애들이 뿌려짐
        //만약 localhost://...members?page=2&size=3이라고 적으면 2번째 페이징 된 애들중 상위 3개만 뿌려짐(7,8,9)가 나온다
        //http://localhost:8080/members?page=2&size=3&sort=username,desc -> username기준 내림차순, 2번쨰 page, size는 3이므로 94,93,92가 나온다

        //Member자체를 절대 밖으로 노출시키면 안되므로 dto로 변환
        Page<MemberDto> map = page.map(member -> new MemberDto(member)); //member자체를 dto에 넣는 방식 사용(여기에선)
        return map;
    }



//    @PostConstruct //어플리케이션이 다 조립되고 주입 다 된 후에 실행
    public void init()
    {
        for(int i=0;i<100;i++)
        {
            memberRepository.save(new Member("user"+i, i));
        }
    }
}
