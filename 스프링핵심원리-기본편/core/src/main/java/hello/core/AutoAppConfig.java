package hello.core;

import hello.core.dicount.DiscountPolicy;
import hello.core.member.MemberRepository;
import hello.core.member.MemoryMemberRepository;
import hello.core.order.OrderService;
import hello.core.order.OrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

//참고: : 사실 애노테이션에는 상속관계라는 것이 없다. 그래서 이렇게 애노테이션이 특정 애노테이션을 들고
//있는 것을 인식할 수 있는 것은 자바 언어가 지원하는 기능은 아니고, 스프링이 지원하는 기능이다
@Configuration
@ComponentScan // 스프링 빈을 자동으로 끌어와서 등록하기 위함 -> @component 이 적힌 클래스를 찾아서 자동으로 스프링 빈으로 등록해줌
        (//자동으로 모든 컴포넌트들을 스캔하는데, 그중에서 뺼 거 등록하기 위함 (Configuration이 붙은 클래스 제외할 것->AppConfig,TestConfig 등등 제외)

                //basePackages = "hello.core.member", //이 위치부터 시작해서 하위를 뒤져서 컴포넌트 스캔을 해라-> 필요한 위치부터 탐색하도록 시작 위치 지정 가능
                //basePackageClasses = AutoAppConfig.class, //AutoAppConfig가 있는 패키지-> hello.core부터 다 뒤진다
                //만약 지정하지 않으면 @ComponentScan 이 붙은 설정 정보 클래스의 패키지가 시작 위치가 된다.

                excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Configuration.class) )
                //컴포넌트 스캔을 사용하면 @Configuration 이 붙은 설정 정보도 자동으로 등록되기 때문에,
                //AppConfig, TestConfig 등 앞서 만들어두었던 설정 정보도 함께 등록되고, 실행되어 버린다. 그래서
                //excludeFilters 를 이용해서 설정정보는 컴포넌트 스캔 대상에서 제외했다. 보통 설정 정보를 컴포넌트
                //스캔 대상에서 제외하지는 않지만, 기존 예제 코드를 최대한 남기고 유지하기 위해서 이 방법을 선택했다.
                //결국 지금 쓰고 있는 Configuration만 쓰도록 한다는 뜻!
public class AutoAppConfig {
    //@Bean으로 등록한 클래스가 하나도 없음
    //@Component를 다 찾고, 거기에서 @autowired통해 의존관계까지 자동으로 주입해줌


//    @Bean(name= "memoryMemberRepository") //기존에 @Component 붙어있는 거가 있는데도 불구하고 수동으로 똑같은 이름의 memoryMemberRepository란 이름의 빈 등록
//    MemberRepository memberRepository(){ //같은 이름으로 된 빈이 있음에도 불구하고 테스트 통과! why? 자동 빈 등록 vs 수동 빈 등록 중 수동 빈이 우선순위를 가짐
//      return new MemoryMemberRepository();
//    }
//

}


