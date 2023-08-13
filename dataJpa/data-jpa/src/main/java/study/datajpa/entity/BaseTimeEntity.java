package study.datajpa.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class) //이벤트라는 걸 명시적으로 적어줘야함!!
@MappedSuperclass
@Getter
public class BaseTimeEntity {

    @CreatedDate //넣어주기
    @Column(updatable = false)
    private LocalDateTime createdDate; //생성일자

    @LastModifiedDate //넣어주기
    private LocalDateTime lastModifiedDate; //수정일자
}
