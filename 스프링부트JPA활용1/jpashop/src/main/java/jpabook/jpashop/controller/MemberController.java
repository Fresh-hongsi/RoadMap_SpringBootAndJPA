package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService; //컨트롤러는 서비스를 갖다 쓰는게 사실상 국룰임

    @GetMapping("/members/new") //회원 가입 최초 들어갔을 떄 나오는 페이지와 연결
    public String createForm(Model model)
    {
        model.addAttribute("memberForm",new MemberForm()); //컨트롤러에서 뷰로 넘어갈때 빈 껍데기 FORM 데이터를 실어서 보냄
        return "members/createMemberForm";
    }

    @PostMapping("/members/new") //회원 가입 입력필드를 입력받아 다시 넘어온 상태
    public String create(@Valid MemberForm form, BindingResult result) //get에서 넘겼던 parameter인 MemberForm이 다시 넘어옴 -> valid 어노테이션 써주면 spring이 memberform의 notempty어노테이션을 체크함
    {
        //@Valid뒤에 BingindResult가 파라미터로 있으면 아래 코드들이 담겨서 실행! -> 좋은 기능임
        if(result.hasErrors()) //만약 문제가 있으면
        {
            return "members/createMemberForm"; //이름 없이 회원가입 버튼 누르면 다시 이 페이지로 가
        }

        Address address= new Address(form.getCity(),form.getStreet(),form.getZipcode()); //form 데이터 일부 꺼내서 주소 객체 만듦
        Member member = new Member();
        member.setName(form.getName());
        member.setAddress(address);

        memberService.join(member); //멤버 서비스에 새로 생성한 멤버를 넘겨주면서 비즈니스 로직 수행
        return "redirect:/"; //수행 요청과 동시에 홈페이지로 리다이렉트

    }

    @GetMapping("/members") //회원 조회
    public String list(Model model)
    {
        List<Member> members = memberService.findMembers();
        model.addAttribute("members",members); //key:members, value: member의 list
        return "members/memberList";
    }

}
