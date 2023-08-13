package study.querydsl.repository;

//사용자 정의 리포지토리 사용법
//        1. 사용자 정의 인터페이스 작성
//        2. 사용자 정의 인터페이스 구현
//        3. 스프링 데이터 리포지토리에 사용자 정의 인터페이스 상속

//spring data jpa는 인터페이스이므로, 쿼리dsl같은 직접적으로 쿼리를 날리는 걸 인퍼페이스화 시키려면,
// memberRepository는 JpaRepository, 사용자정의인터페이스인 memberRepositoryCustom을 상속받도록 해야한다. 그리고 memberRepositoryCustom구현체인 MemberRepositoryImpl에서 쿼리dsl사용한다.


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import study.querydsl.dto.MemberSearchCondition;
import study.querydsl.dto.MemberTeamDto;

import java.util.List;

public interface MemberRepositoryCustom {
    List<MemberTeamDto> search(MemberSearchCondition condition); //검색 조건에 의한 조회

    Page<MemberTeamDto> searchPageSimple(MemberSearchCondition condition, Pageable pageable); //검색 조건에 의한 조회 - 단순 페이징

    Page<MemberTeamDto> searchPageComplex(MemberSearchCondition condition, Pageable pageable); //검색 조건에 의한 조회 - 복잡한 페이징(카운트 쿼리를 따로 분리)


}
