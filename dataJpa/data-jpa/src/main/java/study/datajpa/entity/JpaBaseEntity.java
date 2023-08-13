package study.datajpa.entity;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

//생성일자, 수정일자, 생성자, 수정자를 관리하는 엔티티
//@PrePersist는 Java Persistence API (JPA)에서 사용되는 어노테이션 중 하나로,
// 엔티티 객체가 영속성 컨텍스트에 저장되기 전에 실행되어야 하는 메서드를 지정할 때 사용됩니다.
// JPA는 객체 지향 언어인 Java와 관계형 데이터베이스 사이의 매핑을 쉽게 해주는 기술로,
// @PrePersist 어노테이션은 이 매핑 과정 중에 사용되는 콜백 메서드를 정의하는 데 사용됩니다.
//@PrePersist 어노테이션이 달린 메서드는 해당 엔티티 객체가 데이터베이스에 저장되기 전에 자동으로 호출됩니다.
// 이를 이용하여 엔티티 객체의 특정 필드를 설정하거나 초기화하는 등의 작업을 수행할 수 있습니다.
// 주로 생성일자나 수정일자를 업데이트하거나, 특정 필드 값을 검증하거나 설정하는 등의 용도로 활용될 수 있습니다.

@Getter
@MappedSuperclass //속성값만 Member클래스에서 사용하도록 하는 어노테이션(데이터만 공유, 진짜 상속관계 x)
public class JpaBaseEntity {

    @Column(updatable = false) //생성일자는 업데이트 불가능하게 만듦
    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

    @PrePersist //persist하기 전에 해당 이벤트 발생
    public void prePersist(){
        LocalDateTime now= LocalDateTime.now();
        createdDate=now; //현재 시간으로 생성
        updatedDate=now; //현재 시간으로 업데이트
    }

    @PreUpdate //업데이트하기 전에 해당 이벤트 발생
    public void preUpdate(){
        updatedDate=LocalDateTime.now();
    }

}
