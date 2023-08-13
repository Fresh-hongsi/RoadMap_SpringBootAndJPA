package hello.hellospring.aop;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect //Aop를 적용하기 위해서 반드시 적어야 하는 어노테이션
@Component //빈에 직접 등록하는 방식 아닌 컴포넌트 스캔 방식으로 해보기
public class TimeTraceAop {

    @Around("execution(* hello.hellospring..*(..))") //공통 관심사항 묶어주기(우리 패키지 하위에 대해 모두 적용하라는 의미)
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        System.out.println("START: "+joinPoint.toString());
        try {
            //다음 메소드 진행
            return joinPoint.proceed(); //인라인화: ctrl+alt+shift+t
        }finally
        {
            long finish = System.currentTimeMillis();
            long timeMs=finish-start;
            System.out.println("END: "+joinPoint.toString()+" "+timeMs + "ms");
        }


    }
}
