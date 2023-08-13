package hello.hellospring.controller;

import hello.hellospring.domain.Member;
import hello.hellospring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class MemberController {

    //멤버 컨트롤러는 멤버 서비스를 가져다 써야함
    //private final MemberService memberService = new MemberService();
    //이렇게 직접 new해서 써도 되지만, 굳이 멤버 서비스를 여러개 만들 필요가 없음
    //스프링 컨테이너에 하나만 등록하면 됨
    private final MemberService memberService;
    @Autowired //멤버 컨트롤러는 스프링 컨테이너가 뜰 때 생성됨
    //오토와이어드가 있으면 스프링 컨테이너가 갖고있던 멤버 서비스와 이 컨트롤러를 연결해줌
    public MemberController(MemberService memberService)
    {
        this.memberService=memberService;
    }


    //members/new 도메인이 열렸을 때 실행될 부분
    @GetMapping("/members/new")
    public String createForm() { //이 도메인으로 들어오면 createForm함수 실행
        return "members/createMemberForm";
        //함수 내의 동작: members/createMemberForm의 파일 열기
    }
    //보통 데이터 조회는 get방식, 데이터 등록은 post방식 사용


    @PostMapping("/members/new")
    //post방식으로 요청 들어오면 create함수가 자동 실행
    public String create(MemberForm form){
        //신기한건 spring이 MemberForm 타입을 보고 알아서 form의 name에 post로 입력받은 name값을 넣어줌
        Member member=new Member();
        member.setName(form.getName());
        //사용자에게 입력받은 form의 name을 바탕으로 회원 이름 설정

        System.out.println("member = "+member.getName());
        memberService.join(member);

        return "redirect:/"; //home화면으로 다시 가라는 리다이렉트 명령
    }

    @GetMapping("/members")
    public String list(Model model)
    {
        List<Member> members = memberService.findMembers();
        model.addAttribute("members",members); //model객체에 members란 키에 받아온 멤버리스트를 삽입
        return "members/memberList"; //이 페이지로 넘어가도록 설정

    }

}
