package study.datajpa.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

//data jpa에서 생성일자, 수정일자, 생성자, 수정자를 관리하는 엔티티
@EntityListeners(AuditingEntityListener.class) //이벤트라는 걸 명시적으로 적어줘야함!!
@MappedSuperclass
@Getter
public class BaseEntity extends BaseTimeEntity{


    @CreatedBy //넣어주기
    @Column(updatable = false)
    private String createdBy; //생성한 사람

    @LastModifiedBy //넣어주기
    private String lastModifiedBy; //수정한 사람
}
