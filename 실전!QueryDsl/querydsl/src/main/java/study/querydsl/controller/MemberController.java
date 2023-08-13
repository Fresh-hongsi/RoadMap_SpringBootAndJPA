package study.querydsl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import study.querydsl.dto.MemberSearchCondition;
import study.querydsl.dto.MemberTeamDto;
import study.querydsl.repository.MemberJpaRepository;
import study.querydsl.repository.MemberRepository;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberJpaRepository memberJpaRepository; //순수 jpa로 구현한 거
    private final MemberRepository memberRepository; //spring data jpa에서 구현한 거

    //검색 조건에 의한 조회 api
    @GetMapping("/v1/members")
    public List<MemberTeamDto> searchMemberV1(MemberSearchCondition condition) //http://localhost:8080/v1/members?teamName=teamB이라고 치면 검색조건에 teamName부분 들어감, @modelAttribute생략가능
    {
        //http://localhost:8080/v1/members?teamName=teamB&ageGoe=31&ageLoe=35&username=member31 이런식으로도 활용 가능

        return memberJpaRepository.search(condition);
    }

    //검색 조건 + paging에 의한 조회 api -쿼리랑 count둘다 나가는 simple
    @GetMapping("/v2/members")
    public Page<MemberTeamDto> searchMemberV2(MemberSearchCondition memberSearchCondition, Pageable pageable)
    //pageable도 http://localhost:8080/v2/members?page=0&size=5 바로 받아 바인딩 가능하다!
    //http://localhost:8080/v2/members?page=1&size=5 이런식으로 page번호만 바꿔주면 알아서 다음 데이터가 나옴
    {
        return memberRepository.searchPageSimple(memberSearchCondition,pageable);
    }

    //검색 조건 + paging에 의한 조회 api - 쿼리랑 count별도로 나가는 complex
    @GetMapping("/v3/members")
    public Page<MemberTeamDto> searchMemberV3(MemberSearchCondition memberSearchCondition, Pageable pageable)
    //pageable도 http://localhost:8080/v3/members?page=0&size=5에서 바로 받아 바인딩 가능하다!
    //http://localhost:8080/v3/members?page=1&size=5 이런식으로 page번호만 바꿔주면 알아서 다음 데이터가 나옴
    {
        return memberRepository.searchPageComplex(memberSearchCondition,pageable);
    }


}
