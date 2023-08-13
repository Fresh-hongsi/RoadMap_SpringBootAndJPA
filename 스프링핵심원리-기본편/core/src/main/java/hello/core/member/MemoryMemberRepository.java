package hello.core.member;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class MemoryMemberRepository implements MemberRepository { //MemberRepository:회원 저장소 에 대한 구현체

    private static Map<Long,Member> store = new HashMap<>(); //로컬 컴퓨터에 저장할 회원 저장소 ->원래 동시성 이슈떄문에 concurrent hashmap써야함
    @Override
    public void save(Member member) {

        store.put(member.getId(),member); //로컬 저장소에 회원 저장

    }

    @Override
    public Member findById(Long memberId) {
        return store.get(memberId); //파라미터로 받은 id값을 가지고 멤버 반환
    }

}
