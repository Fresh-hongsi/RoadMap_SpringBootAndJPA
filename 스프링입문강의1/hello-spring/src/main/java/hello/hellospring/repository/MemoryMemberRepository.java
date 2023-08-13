package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.springframework.stereotype.Repository;

import java.util.*;


public class MemoryMemberRepository implements MemberRepository{

    private static Map<Long,Member> store = new HashMap<>();
    private static long sequence= 0L;
    @Override
    public Member save(Member member) {
        //매개변수로 넘어오는 member의 이름은 회원이 이름을 입력한 값으로넘어오고,
        //id는 시스템이 해당 멤버를 저장소에 저장하는 과정에서 부여해준다
        member.setId(++sequence); //멤버 하나 만들때마다 키 값 자동으로 1씩 증가시키기
        store.put(member.getId(),member);
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(store.get(id));
        //널값이 반환되는 것을 고려하여 Optional.ofNullable함수 사용
    }

    @Override
    public Optional<Member> findByName(String name) {
        return store.values().stream() //루프를 돌리면서
                .filter(member->member.getName().equals(name))
                //해당 루프의 멤버이름이 파라미터로 들어온 name과 같은 경우에만 필터링
                .findAny(); //하나라도 찾으면 즉시 반환을 함
    }

    @Override
    public List<Member> findAll() {
        return new ArrayList<>(store.values()); //store의 모든 member값을 리스트에 담아 반환
    }

    public void clearStore()
    {
        store.clear();
    }
}
