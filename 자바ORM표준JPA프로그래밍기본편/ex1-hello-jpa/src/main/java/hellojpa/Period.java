package hellojpa;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import java.time.LocalDateTime;

@Embeddable //명시적으로 이걸 적어줘야 임베디드된 값 타입임을 알 수 있음
public class Period {

    //기간 period
    private LocalDateTime startDate;
    private LocalDateTime endDate;



    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }
}
