package jpabook.jpashop.service;


import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service //컴포넌트 스캔의 대상
@Transactional(readOnly = true) //jpa를 통한 데이터 변경은 어지간하면 transactional(readonly=false) 안에서 이루어져야함, 조회가 많으면(readOnly=true)하는게 좋음
@RequiredArgsConstructor //롬복 통한 생성자 주입 가능(final로 된 애트리뷰트만 생성자 주입)
//이 클래스는 조회하는 기능이 많으므로 전체 PUBLIC 메서드에 대해서 default로 readOnly해주고, 회원 가입같은 쓰기 메서드는 별도로 그냥 Transactional해주기
public class MemberService {

    //스프링 빈에 의한 injection 받음
    //spring data jpa 구현체가 실행 시점에 여기에 주입됨
    private final MemberRepository memberRepository;

//    @Autowired //생성자 주입
//    public MemberService(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

    //회원 가입 기능
    @Transactional//얘는 readonly안할거이므로 명시적으로 적어줌
    public Long join(Member member){

        //중복회원 방지 메서드 호출
        validateDuplicateMember(member); //중복회원 검증 -> 중복회원이면 예외 터뜨리기
        memberRepository.save(member); //문제 없으면 회원을 db에 저장
        return member.getId(); //성공적인 저장에 대한 회원 id반환. Member엔티티를 만든 순간 db에 저장 안돼도 Member의 pk는 영속성 컨텍스트가 해줌
    }

    private void validateDuplicateMember(Member member) {
        //Exception 더뜨리는 코드
        List<Member> findMembers = memberRepository.findByName(member.getName());//같은 이름으로 된 회원이 있는지 조회
        if(!findMembers.isEmpty()) //중복 회원이 있다면
        {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    //회원 전체 조회 기능
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    //회원 한명 조회
    public Member findOne(Long memberId){
        return memberRepository.findById(memberId).get(); //data jpa는 optional로 반환하므로 get으로 한번 더 벗겨주기
    }

    //회원 정보 수정 관련 (api이용, 변경 감지 기능 사용)
    @Transactional
    public void update(Long id, String name)
    {
        Member member = memberRepository.findById(id).get(); //트랜잭션 안에서 가져왔으므로 member는 jpa가 관리하는 영속상태다!
        member.setName(name);
    }
}
