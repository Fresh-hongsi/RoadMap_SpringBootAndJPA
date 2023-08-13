package jpabook.jpashop.api;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.stream.Collectors;


@RestController //@Controller @ResponseBody를 합친 어노테이션 : @ResponseBody의 역할: 데이터 자체를 바로 json,xml로 바로 보내자!
@RequiredArgsConstructor
public class MemberApiController {
    private final MemberService memberService;

    //회원 조회 api -> 안좋은 버전
    @GetMapping("/api/v1/members")
    public List<Member> membersV1() {
        return memberService.findMembers(); //restController 사용하므로 멤버 리스트가 json으로 바뀌어서 쭉 반환됨, 그러나 배열 안에 json있으므로 json형식이 깨진다
    }

    //회원 조회 api -> 발전 버전(응답값 자체를 json스펙에 맞추도록 껍데기(Result)를 씌우는 전략
    @GetMapping("/api/v2/members")
    public Result memberV2(){
        List<Member> findMembers = memberService.findMembers();

        List<MemberDto> collect = findMembers.stream() //모든 찾은 회원들을 반복하면서
                .map(m -> new MemberDto(m.getName())) //각 회원의 이름을 MemberDto라는 객체에 세팅
                .collect(Collectors.toList());//각 MemberDto객체들의 모음을 리스트로 변환

        return new Result(collect.size(),collect); //memberDto리스트를 빈 껍데기 Result객체 안에  넣어서 json형식 맞추도록 함

    }

    //모든 회원 조회에 사용할 껍데기 클래스
    @Data
    @AllArgsConstructor
    static class Result<T>
    {
        private int count;
        private T data; //result객체 안의 데이터는 collect
    }

    //회원 조회에 사용할 dto
    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String name;
    }


    //회원 등록 api v1 -> 좋은 방식 아님. 회원 name에 대한 필수값 여부 및 검증을 @notEmpty통해 하는데, 이는 화면에서 해야할 validation이 내부 entity에서 검증되므로 좋은 방법이 아님
    //또한 name이 아닌 username으로 attribute가 바뀌면 api스펙 자체가 바뀌게 됨
    //따라서 api를 위한 dto를 별도로 만들어야 문제 해결!
    //api를 만들때, 반드시 entity를 파라미터로 받아서는 안된다!!
    @PostMapping("api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) { //@리케스트 바디에 의해 json으로 온 바디를 parameter인 member 엔티티에 매핑해서 넣어줌, member엔티티에서 validation가능
        Long id = memberService.join(member);
        return new CreateMemberResponse(id); //아래 클래스 실행을 통해 id값을 json으로 반환
    }

    //별도의 dto인 CreateMemberRequest통해 v1의 문제 해결하는 방식
    //api가 엔티티에 영향받지 않음, 엔티티 스펙이 바뀌면 컴파일 에러가 발생하므로 배포 전에 수정하면 됨
    @PostMapping("api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request){ //@리케스트 바디에 의해 json으로 온 바디를 parameter인 CREATEmEMBERrESQUEST DTO에 매핑해서 넣어줌,DTO에서 validation가능

        //회원 가입은 merge하는 거랑 상관이 없으므로 준영속도 아니고, 그냥 transaction내에서 생성만하면된다!
        //여기 내에서 엔티티랑 dto랑 매핑진행
        Member member = new Member();
        member.setName(request.getName());
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);

    }





    @PutMapping("/api/v2/members/{id}") //put은 멱등하지만 post는 멱등하지 않다고 할 수 있습니다. -> post는 같은 요청 3번 날리면 3번 다 처리, put은 같은 요청 날리면 한번만 처리
    public UpdateMemberResponse updateMemberV2(@PathVariable("id") Long id, @RequestBody @Valid UpdateMemberRequest request)
    {
        //가급적 수정은 변경감지 기능 사용해라!
        //가급적 커맨드와 쿼리는 분리해라 원칙 적용
        memberService.update(id,request.getName()); //수정, 트랜잭션 커밋으로 db반영, 1차 캐시에 변경내용 반영해 저장
        Member findMember = memberService.findOne(id); //조회, 1차 캐시에서 찾아옴
        return new UpdateMemberResponse(findMember.getId(),findMember.getName());
    }








    //수정 관련 dto클래스(진입)
    @Data
    static class UpdateMemberRequest
    {
        private String name;

    }

    //수정 관련 dto역할(반환)
    @Data
    @AllArgsConstructor //DTO 떄는 단순 데이터 전송이므로 그냥 AllArgsConstructor써도 된다!
    static class UpdateMemberResponse
    {
        //수정 응답 dto엔 id랑 name둘다 실어서 보냄
        private Long id;
        private String name;
    }

    //@Data @Getter , @Setter , @RequiredArgsConstructor , @ToString , @EqualsAndHashCode 을 한꺼번에 설정해주는 어노테이션입니다.
    //회원 가입 관련 dto역할(진입)
    @Data
    static class CreateMemberRequest
    {
        @NotEmpty //여기서 VALIDATION할 수 있게 됨
        private String name;
    }

    //회원 가입 관련 dto역할(반환)
    @Data
    static class CreateMemberResponse {
        private Long id;

        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }
}
